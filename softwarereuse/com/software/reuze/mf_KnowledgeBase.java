package com.software.reuze;
//package aima.core.logic.fol.kb;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/*import aima.core.logic.fol.CNFConverter;
import aima.core.logic.fol.StandardizeApart;
import aima.core.logic.fol.StandardizeApartIndexical;
import aima.core.logic.fol.StandardizeApartIndexicalFactory;
import aima.core.logic.fol.StandardizeApartResult;
import aima.core.logic.fol.SubstVisitor;
import aima.core.logic.fol.Unifier;
import aima.core.logic.fol.VariableCollector;
import aima.core.logic.fol.domain.FOLDomain;
import aima.core.logic.fol.inference.FOLOTTERLikeTheoremProver;
import aima.core.logic.fol.inference.InferenceProcedure;
import aima.core.logic.fol.inference.InferenceResult;
import aima.core.logic.fol.inference.proof.Proof;
import aima.core.logic.fol.inference.proof.ProofStepClauseClausifySentence;
import aima.core.logic.fol.kb.data.CNF;
import aima.core.logic.fol.kb.data.Chain;
import aima.core.logic.fol.kb.data.Clause;
import aima.core.logic.fol.kb.data.Literal;
import aima.core.logic.fol.parsing.FOLParser;
import aima.core.logic.fol.parsing.ast.FOLNode;
import aima.core.logic.fol.parsing.ast.Predicate;
import aima.core.logic.fol.parsing.ast.Sentence;
import aima.core.logic.fol.parsing.ast.Term;
import aima.core.logic.fol.parsing.ast.Variable;*/

/**
 * A First Order Logic (FOL) Knowledge Base.
 * 
 * @author Ciaran O'Reilly
 * 
 */
public class mf_KnowledgeBase {

	private mf_Parser parser;
	private mf_i_InferenceProcedure inferenceProcedure;
	private ml_Unifier unifier;
	private mf_VisitorSubstitution substVisitor;
	private mf_VariableCollector variableCollector;
	private mf_StandardizeApart standardizeApart;
	private mf_CNFConverter cnfConverter;
	//
	// Persistent data structures
	//
	// Keeps track of the Sentences in their original form as added to the
	// Knowledge base.
	private List<mf_i_Sentence> originalSentences = new ArrayList<mf_i_Sentence>();
	// The KB in clause form
	private Set<mf_Clause> clauses = new LinkedHashSet<mf_Clause>();
	// Keep track of all of the definite clauses in the database
	// along with those that represent implications.
	private List<mf_Clause> allDefiniteClauses = new ArrayList<mf_Clause>();
	private List<mf_Clause> implicationDefiniteClauses = new ArrayList<mf_Clause>();
	// All the facts in the KB indexed by Atomic Sentence name (Note: pg. 279)
	private Map<String, List<mf_Literal>> indexFacts = new HashMap<String, List<mf_Literal>>();
	// Keep track of indexical keys for uniquely standardizing apart sentences
	private mf_StandardizeApartIndexical variableIndexical = mf_StandardizeApartIndexicalFactory
			.newStandardizeApartIndexical('v');
	private mf_StandardizeApartIndexical queryIndexical = mf_StandardizeApartIndexicalFactory
			.newStandardizeApartIndexical('q');

	//
	// PUBLIC METHODS
	//
	public mf_KnowledgeBase(mf_Domain domain) {
		// Default to Full Resolution if not set.
		this(domain, new mf_OTTERLikeTheoremProver());
	}

	public mf_KnowledgeBase(mf_Domain domain,
			mf_i_InferenceProcedure inferenceProcedure) {
		this(domain, inferenceProcedure, new ml_Unifier());
	}

	public mf_KnowledgeBase(mf_Domain domain,
			mf_i_InferenceProcedure inferenceProcedure, ml_Unifier unifier) {
		this.parser = new mf_Parser(new mf_Domain(domain));
		this.inferenceProcedure = inferenceProcedure;
		this.unifier = unifier;
		//
		this.substVisitor = new mf_VisitorSubstitution();
		this.variableCollector = new mf_VariableCollector();
		this.standardizeApart = new mf_StandardizeApart(variableCollector,
				substVisitor);
		this.cnfConverter = new mf_CNFConverter(parser);
	}

	public void clear() {
		this.originalSentences.clear();
		this.clauses.clear();
		this.allDefiniteClauses.clear();
		this.implicationDefiniteClauses.clear();
		this.indexFacts.clear();
	}

	public mf_i_InferenceProcedure getInferenceProcedure() {
		return inferenceProcedure;
	}

	public void setInferenceProcedure(mf_i_InferenceProcedure inferenceProcedure) {
		if (null != inferenceProcedure) {
			this.inferenceProcedure = inferenceProcedure;
		}
	}

	public mf_i_Sentence tell(String sentence) {
		mf_i_Sentence s = parser.parse(sentence);
		tell(s);
		return s;
	}

	public void tell(List<? extends mf_i_Sentence> sentences) {
		for (mf_i_Sentence s : sentences) {
			tell(s);
		}
	}

	public void tell(mf_i_Sentence sentence) {
		store(sentence);
	}

	/**
	 * 
	 * @param querySentence
	 * @return an InferenceResult.
	 */
	public mf_i_InferenceResult ask(String querySentence) {
		return ask(parser.parse(querySentence));
	}

	public mf_i_InferenceResult ask(mf_i_Sentence query) {
		// Want to standardize apart the query to ensure
		// it does not clash with any of the sentences
		// in the database
		mf_StandardizeApartResult saResult = standardizeApart.standardizeApart(
				query, queryIndexical);

		// Need to map the result variables (as they are standardized apart)
		// to the original queries variables so that the caller can easily
		// understand and use the returned set of substitutions
		mf_i_InferenceResult infResult = getInferenceProcedure().ask(this,
				saResult.getStandardized());
		for (mf_i_Proof p : infResult.getProofs()) {
			Map<mf_NodeTermVariable, mf_i_NodeTerm> im = p.getAnswerBindings();
			Map<mf_NodeTermVariable, mf_i_NodeTerm> em = new LinkedHashMap<mf_NodeTermVariable, mf_i_NodeTerm>();
			for (mf_NodeTermVariable rev : saResult.getReverseSubstitution().keySet()) {
				em.put((mf_NodeTermVariable) saResult.getReverseSubstitution().get(rev),
						im.get(rev));
			}
			p.replaceAnswerBindings(em);
		}

		return infResult;
	}

	public int getNumberFacts() {
		return allDefiniteClauses.size() - implicationDefiniteClauses.size();
	}

	public int getNumberRules() {
		return clauses.size() - getNumberFacts();
	}

	public List<mf_i_Sentence> getOriginalSentences() {
		return Collections.unmodifiableList(originalSentences);
	}

	public List<mf_Clause> getAllDefiniteClauses() {
		return Collections.unmodifiableList(allDefiniteClauses);
	}

	public List<mf_Clause> getAllDefiniteClauseImplications() {
		return Collections.unmodifiableList(implicationDefiniteClauses);
	}

	public Set<mf_Clause> getAllClauses() {
		return Collections.unmodifiableSet(clauses);
	}

	// Note: pg 278, FETCH(q) concept.
	public synchronized Set<Map<mf_NodeTermVariable, mf_i_NodeTerm>> fetch(mf_Literal l) {
		// Get all of the substitutions in the KB that p unifies with
		Set<Map<mf_NodeTermVariable, mf_i_NodeTerm>> allUnifiers = new LinkedHashSet<Map<mf_NodeTermVariable, mf_i_NodeTerm>>();

		List<mf_Literal> matchingFacts = fetchMatchingFacts(l);
		if (null != matchingFacts) {
			for (mf_Literal fact : matchingFacts) {
				Map<mf_NodeTermVariable, mf_i_NodeTerm> substitution = unifier.unify(
						l.getAtomicSentence(), fact.getAtomicSentence());
				if (null != substitution) {
					allUnifiers.add(substitution);
				}
			}
		}

		return allUnifiers;
	}

	// Note: To support FOL-FC-Ask
	public Set<Map<mf_NodeTermVariable, mf_i_NodeTerm>> fetch(List<mf_Literal> literals) {
		Set<Map<mf_NodeTermVariable, mf_i_NodeTerm>> possibleSubstitutions = new LinkedHashSet<Map<mf_NodeTermVariable, mf_i_NodeTerm>>();

		if (literals.size() > 0) {
			mf_Literal first = literals.get(0);
			List<mf_Literal> rest = literals.subList(1, literals.size());

			recursiveFetch(new LinkedHashMap<mf_NodeTermVariable, mf_i_NodeTerm>(), first, rest,
					possibleSubstitutions);
		}

		return possibleSubstitutions;
	}

	public Map<mf_NodeTermVariable, mf_i_NodeTerm> unify(mf_i_Node x, mf_i_Node y) {
		return unifier.unify(x, y);
	}

	public mf_i_Sentence subst(Map<mf_NodeTermVariable, mf_i_NodeTerm> theta, mf_i_Sentence aSentence) {
		return substVisitor.subst(theta, aSentence);
	}

	public mf_Literal subst(Map<mf_NodeTermVariable, mf_i_NodeTerm> theta, mf_Literal l) {
		return substVisitor.subst(theta, l);
	}

	public mf_i_NodeTerm subst(Map<mf_NodeTermVariable, mf_i_NodeTerm> theta, mf_i_NodeTerm term) {
		return substVisitor.subst(theta, term);
	}

	// Note: see page 277.
	public mf_i_Sentence standardizeApart(mf_i_Sentence sentence) {
		return standardizeApart.standardizeApart(sentence, variableIndexical)
				.getStandardized();
	}

	public mf_Clause standardizeApart(mf_Clause clause) {
		return standardizeApart.standardizeApart(clause, variableIndexical);
	}

	public mf_ProofStepChain standardizeApart(mf_ProofStepChain chain) {
		return standardizeApart.standardizeApart(chain, variableIndexical);
	}

	public Set<mf_NodeTermVariable> collectAllVariables(mf_i_Sentence sentence) {
		return variableCollector.collectAllVariables(sentence);
	}

	public mf_CNF convertToCNF(mf_i_Sentence sentence) {
		return cnfConverter.convertToCNF(sentence);
	}

	public Set<mf_Clause> convertToClauses(mf_i_Sentence sentence) {
		mf_CNF cnf = cnfConverter.convertToCNF(sentence);

		return new LinkedHashSet<mf_Clause>(cnf.getConjunctionOfClauses());
	}

	public mf_Literal createAnswerLiteral(mf_i_Sentence forQuery) {
		String alName = parser.getFOLDomain().addAnswerLiteral();
		List<mf_i_NodeTerm> terms = new ArrayList<mf_i_NodeTerm>();

		Set<mf_NodeTermVariable> vars = variableCollector.collectAllVariables(forQuery);
		for (mf_NodeTermVariable v : vars) {
			// Ensure copies of the variables are used.
			terms.add(v.copy());
		}

		return new mf_Literal(new mf_Predicate(alName, terms));
	}

	// Note: see pg. 281
	public boolean isRenaming(mf_Literal l) {
		List<mf_Literal> possibleMatches = fetchMatchingFacts(l);
		if (null != possibleMatches) {
			return isRenaming(l, possibleMatches);
		}

		return false;
	}

	// Note: see pg. 281
	public boolean isRenaming(mf_Literal l, List<mf_Literal> possibleMatches) {

		for (mf_Literal q : possibleMatches) {
			if (l.isPositiveLiteral() != q.isPositiveLiteral()) {
				continue;
			}
			Map<mf_NodeTermVariable, mf_i_NodeTerm> subst = unifier.unify(l.getAtomicSentence(),
					q.getAtomicSentence());
			if (null != subst) {
				int cntVarTerms = 0;
				for (mf_i_NodeTerm t : subst.values()) {
					if (t instanceof mf_NodeTermVariable) {
						cntVarTerms++;
					}
				}
				// If all the substitutions, even if none, map to Variables
				// then this is a renaming
				if (subst.size() == cntVarTerms) {
					return true;
				}
			}
		}

		return false;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (mf_i_Sentence s : originalSentences) {
			sb.append(s.toString());
			sb.append("\n");
		}
		return sb.toString();
	}

	//
	// PROTECTED METHODS
	//

	protected mf_Parser getParser() {
		return parser;
	}

	//
	// PRIVATE METHODS
	//

	// Note: pg 278, STORE(s) concept.
	private synchronized void store(mf_i_Sentence sentence) {
		originalSentences.add(sentence);

		// Convert the sentence to CNF
		mf_CNF cnfOfOrig = cnfConverter.convertToCNF(sentence);
		for (mf_Clause c : cnfOfOrig.getConjunctionOfClauses()) {
			c.setProofStep(new mf_ProofStepClauseClausifySentence(c, sentence));
			if (c.isEmpty()) {
				// This should not happen, if so the user
				// is trying to add an unsatisfiable sentence
				// to the KB.
				throw new IllegalArgumentException(
						"Attempted to add unsatisfiable sentence to KB, orig=["
								+ sentence + "] CNF=" + cnfOfOrig);
			}

			// Ensure all clauses added to the KB are Standardized Apart.
			c = standardizeApart.standardizeApart(c, variableIndexical);

			// Will make all clauses immutable
			// so that they cannot be modified externally.
			c.setImmutable();
			if (clauses.add(c)) {
				// If added keep track of special types of
				// clauses, as useful for query purposes
				if (c.isDefiniteClause()) {
					allDefiniteClauses.add(c);
				}
				if (c.isImplicationDefiniteClause()) {
					implicationDefiniteClauses.add(c);
				}
				if (c.isUnitClause()) {
					indexFact(c.getLiterals().iterator().next());
				}
			}
		}
	}

	// Only if it is a unit clause does it get indexed as a fact
	// see pg. 279 for general idea.
	private void indexFact(mf_Literal fact) {
		String factKey = getFactKey(fact);
		if (!indexFacts.containsKey(factKey)) {
			indexFacts.put(factKey, new ArrayList<mf_Literal>());
		}

		indexFacts.get(factKey).add(fact);
	}

	private void recursiveFetch(Map<mf_NodeTermVariable, mf_i_NodeTerm> theta, mf_Literal l,
			List<mf_Literal> remainingLiterals,
			Set<Map<mf_NodeTermVariable, mf_i_NodeTerm>> possibleSubstitutions) {

		// Find all substitutions for current predicate based on the
		// substitutions of prior predicates in the list (i.e. SUBST with
		// theta).
		Set<Map<mf_NodeTermVariable, mf_i_NodeTerm>> pSubsts = fetch(subst(theta, l));

		// No substitutions, therefore cannot continue
		if (null == pSubsts) {
			return;
		}

		for (Map<mf_NodeTermVariable, mf_i_NodeTerm> psubst : pSubsts) {
			// Ensure all prior substitution information is maintained
			// along the chain of predicates (i.e. for shared variables
			// across the predicates).
			psubst.putAll(theta);
			if (remainingLiterals.size() == 0) {
				// This means I am at the end of the chain of predicates
				// and have found a valid substitution.
				possibleSubstitutions.add(psubst);
			} else {
				// Need to move to the next link in the chain of substitutions
				mf_Literal first = remainingLiterals.get(0);
				List<mf_Literal> rest = remainingLiterals.subList(1,
						remainingLiterals.size());

				recursiveFetch(psubst, first, rest, possibleSubstitutions);
			}
		}
	}

	private List<mf_Literal> fetchMatchingFacts(mf_Literal l) {
		return indexFacts.get(getFactKey(l));
	}

	private String getFactKey(mf_Literal l) {
		StringBuilder key = new StringBuilder();
		if (l.isPositiveLiteral()) {
			key.append("+");
		} else {
			key.append("-");
		}
		key.append(l.getAtomicSentence().getSymbolicName());

		return key.toString();
	}
}