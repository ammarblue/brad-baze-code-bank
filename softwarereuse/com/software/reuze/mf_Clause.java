package com.software.reuze;
//package aima.core.logic.fol.kb.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


/*import aima.core.logic.fol.StandardizeApart;
import aima.core.logic.fol.StandardizeApartIndexical;
import aima.core.logic.fol.StandardizeApartIndexicalFactory;
import aima.core.logic.fol.SubstVisitor;
import aima.core.logic.fol.Unifier;
import aima.core.logic.fol.VariableCollector;
import aima.core.logic.fol.inference.proof.ProofStep;
import aima.core.logic.fol.inference.proof.ProofStepClauseBinaryResolvent;
import aima.core.logic.fol.inference.proof.ProofStepClauseFactor;
import aima.core.logic.fol.inference.proof.ProofStepPremise;
import aima.core.logic.fol.parsing.FOLVisitor;
import aima.core.logic.fol.parsing.ast.AtomicSentence;
import aima.core.logic.fol.parsing.ast.ConnectedSentence;
import aima.core.logic.fol.parsing.ast.Constant;
import aima.core.logic.fol.parsing.ast.Function;
import aima.core.logic.fol.parsing.ast.NotSentence;
import aima.core.logic.fol.parsing.ast.Predicate;
import aima.core.logic.fol.parsing.ast.QuantifiedSentence;
import aima.core.logic.fol.parsing.ast.Term;
import aima.core.logic.fol.parsing.ast.TermEquality;
import aima.core.logic.fol.parsing.ast.Variable;
import aima.core.util.math.MixedRadixNumber;*/

/**
 * A Clause: A disjunction of literals.
 * 
 * 
 * @author Ciaran O'Reilly
 * 
 */
public class mf_Clause {
	//
	private static mf_StandardizeApartIndexical _saIndexical = mf_StandardizeApartIndexicalFactory
			.newStandardizeApartIndexical('c');
	private static ml_Unifier _unifier = new ml_Unifier();
	private static mf_VisitorSubstitution _substVisitor = new mf_VisitorSubstitution();
	private static mf_VariableCollector _variableCollector = new mf_VariableCollector();
	private static mf_StandardizeApart _standardizeApart = new mf_StandardizeApart();
	private static LiteralsSorter _literalSorter = new LiteralsSorter();
	//
	private final Set<mf_Literal> literals = new LinkedHashSet<mf_Literal>();
	private final List<mf_Literal> positiveLiterals = new ArrayList<mf_Literal>();
	private final List<mf_Literal> negativeLiterals = new ArrayList<mf_Literal>();
	private boolean immutable = false;
	private boolean saCheckRequired = true;
	private String equalityIdentity = "";
	private Set<mf_Clause> factors = null;
	private Set<mf_Clause> nonTrivialFactors = null;
	private String stringRep = null;
	private m_ProofStep proofStep = null;

	public mf_Clause() {
		// i.e. the empty clause
	}

	public mf_Clause(List<mf_Literal> lits) {
		this.literals.addAll(lits);
		for (mf_Literal l : literals) {
			if (l.isPositiveLiteral()) {
				this.positiveLiterals.add(l);
			} else {
				this.negativeLiterals.add(l);
			}
		}
		recalculateIdentity();
	}

	public mf_Clause(List<mf_Literal> lits1, List<mf_Literal> lits2) {
		literals.addAll(lits1);
		literals.addAll(lits2);
		for (mf_Literal l : literals) {
			if (l.isPositiveLiteral()) {
				this.positiveLiterals.add(l);
			} else {
				this.negativeLiterals.add(l);
			}
		}
		recalculateIdentity();
	}

	public m_ProofStep getProofStep() {
		if (null == proofStep) {
			// Assume was a premise
			proofStep = new mf_ProofStepPremise(this);
		}
		return proofStep;
	}

	public void setProofStep(m_ProofStep proofStep) {
		this.proofStep = proofStep;
	}

	public boolean isImmutable() {
		return immutable;
	}

	public void setImmutable() {
		immutable = true;
	}

	public boolean isStandardizedApartCheckRequired() {
		return saCheckRequired;
	}

	public void setStandardizedApartCheckNotRequired() {
		saCheckRequired = false;
	}

	public boolean isEmpty() {
		return literals.size() == 0;
	}

	public boolean isUnitClause() {
		return literals.size() == 1;
	}

	public boolean isDefiniteClause() {
		// A Definite Clause is a disjunction of literals of which exactly 1 is
		// positive.
		return !isEmpty() && positiveLiterals.size() == 1;
	}

	public boolean isImplicationDefiniteClause() {
		// An Implication Definite Clause is a disjunction of literals of
		// which exactly 1 is positive and there is 1 or more negative
		// literals.
		return isDefiniteClause() && negativeLiterals.size() >= 1;
	}

	public boolean isHornClause() {
		// A Horn clause is a disjunction of literals of which at most one is
		// positive.
		return !isEmpty() && positiveLiterals.size() <= 1;
	}

	public boolean isTautology() {

		for (mf_Literal pl : positiveLiterals) {
			// Literals in a clause must be exact complements
			// for tautology elimination to apply. Do not
			// remove non-identical literals just because
			// they are complements under unification, see pg16:
			// http://logic.stanford.edu/classes/cs157/2008/notes/chap09.pdf
			for (mf_Literal nl : negativeLiterals) {
				if (pl.getAtomicSentence().equals(nl.getAtomicSentence())) {
					return true;
				}
			}
		}

		return false;
	}

	public void addLiteral(mf_Literal literal) {
		if (isImmutable()) {
			throw new IllegalStateException(
					"Clause is immutable, cannot be updated.");
		}
		int origSize = literals.size();
		literals.add(literal);
		if (literals.size() > origSize) {
			if (literal.isPositiveLiteral()) {
				positiveLiterals.add(literal);
			} else {
				negativeLiterals.add(literal);
			}
		}
		recalculateIdentity();
	}

	public void addPositiveLiteral(mf_i_SentenceAtomic atom) {
		addLiteral(new mf_Literal(atom));
	}

	public void addNegativeLiteral(mf_i_SentenceAtomic atom) {
		addLiteral(new mf_Literal(atom, true));
	}

	public int getNumberLiterals() {
		return literals.size();
	}

	public int getNumberPositiveLiterals() {
		return positiveLiterals.size();
	}

	public int getNumberNegativeLiterals() {
		return negativeLiterals.size();
	}

	public Set<mf_Literal> getLiterals() {
		return Collections.unmodifiableSet(literals);
	}

	public List<mf_Literal> getPositiveLiterals() {
		return Collections.unmodifiableList(positiveLiterals);
	}

	public List<mf_Literal> getNegativeLiterals() {
		return Collections.unmodifiableList(negativeLiterals);
	}

	public Set<mf_Clause> getFactors() {
		if (null == factors) {
			calculateFactors(null);
		}
		return Collections.unmodifiableSet(factors);
	}

	public Set<mf_Clause> getNonTrivialFactors() {
		if (null == nonTrivialFactors) {
			calculateFactors(null);
		}
		return Collections.unmodifiableSet(nonTrivialFactors);
	}

	public boolean subsumes(mf_Clause othC) {
		boolean subsumes = false;

		// Equality is not subsumption
		if (!(this == othC)) {
			// Ensure this has less literals total and that
			// it is a subset of the other clauses positive and negative counts
			if (this.getNumberLiterals() < othC.getNumberLiterals()
					&& this.getNumberPositiveLiterals() <= othC
							.getNumberPositiveLiterals()
					&& this.getNumberNegativeLiterals() <= othC
							.getNumberNegativeLiterals()) {

				Map<String, List<mf_Literal>> thisToTry = collectLikeLiterals(this.literals);
				Map<String, List<mf_Literal>> othCToTry = collectLikeLiterals(othC.literals);
				// Ensure all like literals from this clause are a subset
				// of the other clause.
				if (othCToTry.keySet().containsAll(thisToTry.keySet())) {
					boolean isAPossSubset = true;
					// Ensure that each set of same named literals
					// from this clause is a subset of the other
					// clauses same named literals.
					for (String pk : thisToTry.keySet()) {
						if (thisToTry.get(pk).size() > othCToTry.get(pk).size()) {
							isAPossSubset = false;
							break;
						}
					}
					if (isAPossSubset) {
						// At this point I know this this Clause's
						// literal/arity names are a subset of the
						// other clauses literal/arity names
						subsumes = checkSubsumes(othC, thisToTry, othCToTry);
					}
				}
			}
		}

		return subsumes;
	}

	// Note: Applies binary resolution rule
	// Note: returns a set with an empty clause if both clauses
	// are empty, otherwise returns a set of binary resolvents.
	public Set<mf_Clause> binaryResolvents(mf_Clause othC) {
		Set<mf_Clause> resolvents = new LinkedHashSet<mf_Clause>();
		// Resolving two empty clauses
		// gives you an empty clause
		if (isEmpty() && othC.isEmpty()) {
			resolvents.add(new mf_Clause());
			return resolvents;
		}

		// Ensure Standardized Apart
		// Before attempting binary resolution
		othC = saIfRequired(othC);

		List<mf_Literal> allPosLits = new ArrayList<mf_Literal>();
		List<mf_Literal> allNegLits = new ArrayList<mf_Literal>();
		allPosLits.addAll(this.positiveLiterals);
		allPosLits.addAll(othC.positiveLiterals);
		allNegLits.addAll(this.negativeLiterals);
		allNegLits.addAll(othC.negativeLiterals);

		List<mf_Literal> trPosLits = new ArrayList<mf_Literal>();
		List<mf_Literal> trNegLits = new ArrayList<mf_Literal>();
		List<mf_Literal> copyRPosLits = new ArrayList<mf_Literal>();
		List<mf_Literal> copyRNegLits = new ArrayList<mf_Literal>();

		for (int i = 0; i < 2; i++) {
			trPosLits.clear();
			trNegLits.clear();

			if (i == 0) {
				// See if this clauses positives
				// unify with the other clauses
				// negatives
				trPosLits.addAll(this.positiveLiterals);
				trNegLits.addAll(othC.negativeLiterals);
			} else {
				// Try the other way round now
				trPosLits.addAll(othC.positiveLiterals);
				trNegLits.addAll(this.negativeLiterals);
			}

			// Now check to see if they resolve
			Map<mf_NodeTermVariable, mf_i_NodeTerm> copyRBindings = new LinkedHashMap<mf_NodeTermVariable, mf_i_NodeTerm>();
			for (mf_Literal pl : trPosLits) {
				for (mf_Literal nl : trNegLits) {
					copyRBindings.clear();
					if (null != _unifier.unify(pl.getAtomicSentence(),
							nl.getAtomicSentence(), copyRBindings)) {
						copyRPosLits.clear();
						copyRNegLits.clear();
						boolean found = false;
						for (mf_Literal l : allPosLits) {
							if (!found && pl.equals(l)) {
								found = true;
								continue;
							}
							copyRPosLits.add(_substVisitor.subst(copyRBindings,
									l));
						}
						found = false;
						for (mf_Literal l : allNegLits) {
							if (!found && nl.equals(l)) {
								found = true;
								continue;
							}
							copyRNegLits.add(_substVisitor.subst(copyRBindings,
									l));
						}
						// Ensure the resolvents are standardized apart
						Map<mf_NodeTermVariable, mf_i_NodeTerm> renameSubstitituon = _standardizeApart
								.standardizeApart(copyRPosLits, copyRNegLits,
										_saIndexical);
						mf_Clause c = new mf_Clause(copyRPosLits, copyRNegLits);
						c.setProofStep(new mf_ProofStepClauseBinaryResolvent(c,
								pl, nl, this, othC, copyRBindings,
								renameSubstitituon));
						if (isImmutable()) {
							c.setImmutable();
						}
						if (!isStandardizedApartCheckRequired()) {
							c.setStandardizedApartCheckNotRequired();
						}
						resolvents.add(c);
					}
				}
			}
		}

		return resolvents;
	}

	@Override
	public String toString() {
		if (null == stringRep) {
			List<mf_Literal> sortedLiterals = new ArrayList<mf_Literal>(literals);
			Collections.sort(sortedLiterals, _literalSorter);

			stringRep = sortedLiterals.toString();
		}
		return stringRep;
	}

	@Override
	public int hashCode() {
		return equalityIdentity.hashCode();
	}

	@Override
	public boolean equals(Object othObj) {
		if (null == othObj) {
			return false;
		}
		if (this == othObj) {
			return true;
		}
		if (!(othObj instanceof mf_Clause)) {
			return false;
		}
		mf_Clause othClause = (mf_Clause) othObj;

		return equalityIdentity.equals(othClause.equalityIdentity);
	}

	public String getEqualityIdentity() {
		return equalityIdentity;
	}

	//
	// PRIVATE METHODS
	//
	private void recalculateIdentity() {
		synchronized (equalityIdentity) {

			// Sort the literals first based on negation, atomic sentence,
			// constant, function and variable.
			List<mf_Literal> sortedLiterals = new ArrayList<mf_Literal>(literals);
			Collections.sort(sortedLiterals, _literalSorter);

			// All variables are considered the same as regards
			// sorting. Therefore, to determine if two clauses
			// are equivalent you need to determine
			// the # of unique variables they contain and
			// there positions across the clauses
			ClauseEqualityIdentityConstructor ceic = new ClauseEqualityIdentityConstructor(
					sortedLiterals, _literalSorter);

			equalityIdentity = ceic.getIdentity();

			// Reset, these as will need to re-calcualte
			// if requested for again, best to only
			// access lazily.
			factors = null;
			nonTrivialFactors = null;
			// Reset the objects string representation
			// until it is requested for.
			stringRep = null;
		}
	}

	private void calculateFactors(Set<mf_Clause> parentFactors) {
		nonTrivialFactors = new LinkedHashSet<mf_Clause>();

		Map<mf_NodeTermVariable, mf_i_NodeTerm> theta = new HashMap<mf_NodeTermVariable, mf_i_NodeTerm>();
		List<mf_Literal> lits = new ArrayList<mf_Literal>();
		for (int i = 0; i < 2; i++) {
			lits.clear();
			if (i == 0) {
				// Look at the positive literals
				lits.addAll(positiveLiterals);
			} else {
				// Look at the negative literals
				lits.addAll(negativeLiterals);
			}
			for (int x = 0; x < lits.size(); x++) {
				for (int y = x + 1; y < lits.size(); y++) {
					mf_Literal litX = lits.get(x);
					mf_Literal litY = lits.get(y);

					theta.clear();
					Map<mf_NodeTermVariable, mf_i_NodeTerm> substitution = _unifier.unify(
							litX.getAtomicSentence(), litY.getAtomicSentence(),
							theta);
					if (null != substitution) {
						List<mf_Literal> posLits = new ArrayList<mf_Literal>();
						List<mf_Literal> negLits = new ArrayList<mf_Literal>();
						if (i == 0) {
							posLits.add(_substVisitor.subst(substitution, litX));
						} else {
							negLits.add(_substVisitor.subst(substitution, litX));
						}
						for (mf_Literal pl : positiveLiterals) {
							if (pl == litX || pl == litY) {
								continue;
							}
							posLits.add(_substVisitor.subst(substitution, pl));
						}
						for (mf_Literal nl : negativeLiterals) {
							if (nl == litX || nl == litY) {
								continue;
							}
							negLits.add(_substVisitor.subst(substitution, nl));
						}
						// Ensure the non trivial factor is standardized apart
						Map<mf_NodeTermVariable, mf_i_NodeTerm> renameSubst = _standardizeApart
								.standardizeApart(posLits, negLits,
										_saIndexical);
						mf_Clause c = new mf_Clause(posLits, negLits);
						c.setProofStep(new mf_ProofStepClauseFactor(c, this, litX,
								litY, substitution, renameSubst));
						if (isImmutable()) {
							c.setImmutable();
						}
						if (!isStandardizedApartCheckRequired()) {
							c.setStandardizedApartCheckNotRequired();
						}
						if (null == parentFactors) {
							c.calculateFactors(nonTrivialFactors);
							nonTrivialFactors.addAll(c.getFactors());
						} else {
							if (!parentFactors.contains(c)) {
								c.calculateFactors(nonTrivialFactors);
								nonTrivialFactors.addAll(c.getFactors());
							}
						}
					}
				}
			}
		}

		factors = new LinkedHashSet<mf_Clause>();
		// Need to add self, even though a non-trivial
		// factor. See: slide 30
		// http://logic.stanford.edu/classes/cs157/2008/lectures/lecture10.pdf
		// for example of incompleteness when
		// trivial factor not included.
		factors.add(this);
		factors.addAll(nonTrivialFactors);
	}

	private mf_Clause saIfRequired(mf_Clause othClause) {

		// If performing resolution with self
		// then need to standardize apart in
		// order to work correctly.
		if (isStandardizedApartCheckRequired() || this == othClause) {
			Set<mf_NodeTermVariable> mVariables = _variableCollector
					.collectAllVariables(this);
			Set<mf_NodeTermVariable> oVariables = _variableCollector
					.collectAllVariables(othClause);

			Set<mf_NodeTermVariable> cVariables = new HashSet<mf_NodeTermVariable>();
			cVariables.addAll(mVariables);
			cVariables.addAll(oVariables);

			if (cVariables.size() < (mVariables.size() + oVariables.size())) {
				othClause = _standardizeApart.standardizeApart(othClause,
						_saIndexical);
			}
		}

		return othClause;
	}

	private Map<String, List<mf_Literal>> collectLikeLiterals(Set<mf_Literal> literals) {
		Map<String, List<mf_Literal>> likeLiterals = new HashMap<String, List<mf_Literal>>();
		for (mf_Literal l : literals) {
			// Want to ensure P(a, b) is considered different than P(a, b, c)
			// i.e. consider an atom's arity P/#.
			String literalName = (l.isNegativeLiteral() ? "~" : "")
					+ l.getAtomicSentence().getSymbolicName() + "/"
					+ l.getAtomicSentence().getArgs().size();
			List<mf_Literal> like = likeLiterals.get(literalName);
			if (null == like) {
				like = new ArrayList<mf_Literal>();
				likeLiterals.put(literalName, like);
			}
			like.add(l);
		}
		return likeLiterals;
	}

	private boolean checkSubsumes(mf_Clause othC,
			Map<String, List<mf_Literal>> thisToTry,
			Map<String, List<mf_Literal>> othCToTry) {
		boolean subsumes = false;

		List<mf_i_NodeTerm> thisTerms = new ArrayList<mf_i_NodeTerm>();
		List<mf_i_NodeTerm> othCTerms = new ArrayList<mf_i_NodeTerm>();

		// Want to track possible number of permuations
		List<Integer> radices = new ArrayList<Integer>();
		for (String literalName : thisToTry.keySet()) {
			int sizeT = thisToTry.get(literalName).size();
			int sizeO = othCToTry.get(literalName).size();

			if (sizeO > 1) {
				// The following is being used to
				// track the number of permutations
				// that can be mapped from the
				// other clauses like literals to this
				// clauses like literals.
				// i.e. n!/(n-r)!
				// where n=sizeO and r =sizeT
				for (int i = 0; i < sizeT; i++) {
					int r = sizeO - i;
					if (r > 1) {
						radices.add(r);
					}
				}
			}
			// Track the terms for this clause
			for (mf_Literal tl : thisToTry.get(literalName)) {
				thisTerms.addAll(tl.getAtomicSentence().getArgs());
			}
		}

		m_NumberMixedRadix permutation = null;
		long numPermutations = 1L;
		if (radices.size() > 0) {
			permutation = new m_NumberMixedRadix(0, radices);
			numPermutations = permutation.getMaxAllowedValue() + 1;
		}
		// Want to ensure none of the othCVariables are
		// part of the key set of a unification as
		// this indicates it is not a legal subsumption.
		Set<mf_NodeTermVariable> othCVariables = _variableCollector
				.collectAllVariables(othC);
		Map<mf_NodeTermVariable, mf_i_NodeTerm> theta = new LinkedHashMap<mf_NodeTermVariable, mf_i_NodeTerm>();
		List<mf_Literal> literalPermuations = new ArrayList<mf_Literal>();
		for (long l = 0L; l < numPermutations; l++) {
			// Track the other clause's terms for this
			// permutation.
			othCTerms.clear();
			int radixIdx = 0;
			for (String literalName : thisToTry.keySet()) {
				int sizeT = thisToTry.get(literalName).size();
				literalPermuations.clear();
				literalPermuations.addAll(othCToTry.get(literalName));
				int sizeO = literalPermuations.size();

				if (sizeO > 1) {
					for (int i = 0; i < sizeT; i++) {
						int r = sizeO - i;
						if (r > 1) {
							// If not a 1 to 1 mapping then you need
							// to use the correct permuation
							int numPos = permutation
									.getCurrentNumeralValue(radixIdx);
							othCTerms.addAll(literalPermuations.remove(numPos)
									.getAtomicSentence().getArgs());
							radixIdx++;
						} else {
							// is the last mapping, therefore
							// won't be on the radix
							othCTerms.addAll(literalPermuations.get(0)
									.getAtomicSentence().getArgs());
						}
					}
				} else {
					// a 1 to 1 mapping
					othCTerms.addAll(literalPermuations.get(0)
							.getAtomicSentence().getArgs());
				}
			}

			// Note: on unifier
			// unifier.unify(P(w, x), P(y, z)))={w=y, x=z}
			// unifier.unify(P(y, z), P(w, x)))={y=w, z=x}
			// Therefore want this clause to be the first
			// so can do the othCVariables check for an invalid
			// subsumes.
			theta.clear();
			if (null != _unifier.unify(thisTerms, othCTerms, theta)) {
				boolean containsAny = false;
				for (mf_NodeTermVariable v : theta.keySet()) {
					if (othCVariables.contains(v)) {
						containsAny = true;
						break;
					}
				}
				if (!containsAny) {
					subsumes = true;
					break;
				}
			}

			// If there is more than 1 mapping
			// keep track of where I am in the
			// possible number of mapping permutations.
			if (null != permutation) {
				permutation.increment();
			}
		}

		return subsumes;
	}
}

class LiteralsSorter implements Comparator<mf_Literal> {
	public int compare(mf_Literal o1, mf_Literal o2) {
		int rVal = 0;
		// If literals are not negated the same
		// then positive literals are considered
		// (by convention here) to be of higher
		// order than negative literals
		if (o1.isPositiveLiteral() != o2.isPositiveLiteral()) {
			if (o1.isPositiveLiteral()) {
				return 1;
			}
			return -1;
		}

		// Check their symbolic names for order first
		rVal = o1.getAtomicSentence().getSymbolicName()
				.compareTo(o2.getAtomicSentence().getSymbolicName());

		// If have same symbolic names
		// then need to compare individual arguments
		// for order.
		if (0 == rVal) {
			rVal = compareArgs(o1.getAtomicSentence().getArgs(), o2
					.getAtomicSentence().getArgs());
		}

		return rVal;
	}

	private int compareArgs(List<mf_i_NodeTerm> args1, List<mf_i_NodeTerm> args2) {
		int rVal = 0;

		// Compare argument sizes first
		rVal = args1.size() - args2.size();

		if (0 == rVal && args1.size() > 0) {
			// Move forward and compare the
			// first arguments
			mf_i_NodeTerm t1 = args1.get(0);
			mf_i_NodeTerm t2 = args2.get(0);

			if (t1.getClass() == t2.getClass()) {
				// Note: Variables are considered to have
				// the same order
				if (t1 instanceof mf_SymbolConstant) {
					rVal = t1.getSymbolicName().compareTo(t2.getSymbolicName());
				} else if (t1 instanceof mf_NodeTermFunction) {
					rVal = t1.getSymbolicName().compareTo(t2.getSymbolicName());
					if (0 == rVal) {
						// Same function names, therefore
						// compare the function arguments
						rVal = compareArgs(t1.getArgs(), t2.getArgs());
					}
				}

				// If the first args are the same
				// then compare the ordering of the
				// remaining arguments
				if (0 == rVal) {
					rVal = compareArgs(args1.subList(1, args1.size()),
							args2.subList(1, args2.size()));
				}
			} else {
				// Order for different Terms is:
				// Constant > Function > Variable
				if (t1 instanceof mf_SymbolConstant) {
					rVal = 1;
				} else if (t2 instanceof mf_SymbolConstant) {
					rVal = -1;
				} else if (t1 instanceof mf_NodeTermFunction) {
					rVal = 1;
				} else {
					rVal = -1;
				}
			}
		}

		return rVal;
	}
}

class ClauseEqualityIdentityConstructor implements mf_Visitor {
	private StringBuilder identity = new StringBuilder();
	private int noVarPositions = 0;
	private int[] clauseVarCounts = null;
	private int currentLiteral = 0;
	private Map<String, List<Integer>> varPositions = new HashMap<String, List<Integer>>();

	public ClauseEqualityIdentityConstructor(List<mf_Literal> literals,
			LiteralsSorter sorter) {

		clauseVarCounts = new int[literals.size()];

		for (mf_Literal l : literals) {
			if (l.isNegativeLiteral()) {
				identity.append("~");
			}
			identity.append(l.getAtomicSentence().getSymbolicName());
			identity.append("(");
			boolean firstTerm = true;
			for (mf_i_NodeTerm t : l.getAtomicSentence().getArgs()) {
				if (firstTerm) {
					firstTerm = false;
				} else {
					identity.append(",");
				}
				t.accept(this, null);
			}
			identity.append(")");
			currentLiteral++;
		}

		int min, max;
		min = max = 0;
		for (int i = 0; i < literals.size(); i++) {
			int incITo = i;
			int next = i + 1;
			max += clauseVarCounts[i];
			while (next < literals.size()) {
				if (0 != sorter.compare(literals.get(i), literals.get(next))) {
					break;
				}
				max += clauseVarCounts[next];
				incITo = next; // Need to skip to the end of the range
				next++;
			}
			// This indicates two or more literals are identical
			// except for variable naming (note: identical
			// same name would be removed as are working
			// with sets so don't need to worry about this).
			if ((next - i) > 1) {
				// Need to check each variable
				// and if it has a position within the
				// current min/max range then need
				// to include its alternative
				// sort order positions as well
				for (String key : varPositions.keySet()) {
					List<Integer> positions = varPositions.get(key);
					List<Integer> additPositions = new ArrayList<Integer>();
					// Add then subtract for all possible
					// positions in range
					for (int pos : positions) {
						if (pos >= min && pos < max) {
							int pPos = pos;
							int nPos = pos;
							for (int candSlot = i; candSlot < (next - 1); candSlot++) {
								pPos += clauseVarCounts[i];
								if (pPos >= min && pPos < max) {
									if (!positions.contains(pPos)
											&& !additPositions.contains(pPos)) {
										additPositions.add(pPos);
									}
								}
								nPos -= clauseVarCounts[i];
								if (nPos >= min && nPos < max) {
									if (!positions.contains(nPos)
											&& !additPositions.contains(nPos)) {
										additPositions.add(nPos);
									}
								}
							}
						}
					}
					positions.addAll(additPositions);
				}
			}
			min = max;
			i = incITo;
		}

		// Determine the maxWidth
		int maxWidth = 1;
		while (noVarPositions >= 10) {
			noVarPositions = noVarPositions / 10;
			maxWidth++;
		}
		String format = "%0" + maxWidth + "d";

		// Sort the individual position lists
		// And then add their string representations
		// together
		List<String> varOffsets = new ArrayList<String>();
		for (String key : varPositions.keySet()) {
			List<Integer> positions = varPositions.get(key);
			Collections.sort(positions);
			StringBuilder sb = new StringBuilder();
			for (int pos : positions) {
				sb.append(String.format(format, pos));
			}
			varOffsets.add(sb.toString());
		}
		Collections.sort(varOffsets);
		for (int i = 0; i < varOffsets.size(); i++) {
			identity.append(varOffsets.get(i));
			if (i < (varOffsets.size() - 1)) {
				identity.append(",");
			}
		}
	}

	public String getIdentity() {
		return identity.toString();
	}

	//
	// START-FOLVisitor
	public Object visitVariable(mf_NodeTermVariable var, Object arg) {
		// All variables will be marked with an *
		identity.append("*");

		List<Integer> positions = varPositions.get(var.getValue());
		if (null == positions) {
			positions = new ArrayList<Integer>();
			varPositions.put(var.getValue(), positions);
		}
		positions.add(noVarPositions);

		noVarPositions++;
		clauseVarCounts[currentLiteral]++;
		return var;
	}

	public Object visitConstant(mf_SymbolConstant constant, Object arg) {
		identity.append(constant.getValue());
		return constant;
	}

	public Object visitFunction(mf_NodeTermFunction function, Object arg) {
		boolean firstTerm = true;
		identity.append(function.getFunctionName());
		identity.append("(");
		for (mf_i_NodeTerm t : function.getTerms()) {
			if (firstTerm) {
				firstTerm = false;
			} else {
				identity.append(",");
			}
			t.accept(this, arg);
		}
		identity.append(")");

		return function;
	}

	public Object visitPredicate(mf_Predicate predicate, Object arg) {
		throw new IllegalStateException("Should not be called");
	}

	public Object visitTermEquality(mf_SentenceAtomicTermEquality equality, Object arg) {
		throw new IllegalStateException("Should not be called");
	}

	public Object visitQuantifiedSentence(mf_SentenceQuantified sentence,
			Object arg) {
		throw new IllegalStateException("Should not be called");
	}

	public Object visitNotSentence(mf_SentenceNot sentence, Object arg) {
		throw new IllegalStateException("Should not be called");
	}

	public Object visitConnectedSentence(mf_SentenceConnected sentence, Object arg) {
		throw new IllegalStateException("Should not be called");
	}

	// END-FOLVisitor
	//
}