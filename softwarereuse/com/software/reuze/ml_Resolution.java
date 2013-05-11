package com.software.reuze;
//package aima.core.logic.propositional.algorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


/*import aima.core.logic.propositional.parsing.PEParser;
import aima.core.logic.propositional.parsing.ast.BinarySentence;
import aima.core.logic.propositional.parsing.ast.Sentence;
import aima.core.logic.propositional.parsing.ast.Symbol;
import aima.core.logic.propositional.parsing.ast.SymbolComparator;
import aima.core.logic.propositional.parsing.ast.UnarySentence;
import aima.core.logic.propositional.visitors.CNFClauseGatherer;
import aima.core.logic.propositional.visitors.CNFTransformer;
import aima.core.logic.propositional.visitors.SymbolClassifier;
import aima.core.util.Converter;
import aima.core.util.SetOps;*/

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 255.<br>
 * <br>
 * 
 * <pre>
 * <code>
 * function PL-RESOLUTION(KB, &alpha;) returns true or false
 *    inputs: KB, the knowledge base, a sentence in propositional logic
 *            &alpha;, the query, a sentence in propositional logic
 *            
 *    clauses &larr; the set of clauses in the CNF representation of KB &and; &not;&alpha;
 *    new &larr; {}
 *    loop do
 *       for each pair of clauses C<sub>i</sub>, C<sub>j</sub> in clauses do
 *          resolvents &larr; PL-RESOLVE(C<sub>i</sub>, C<sub>j</sub>)
 *          if resolvents contains the empty clause then return true
 *          new &larr; new &cup; resolvents
 *       if new &sube; clauses then return false
 *       clauses &larr; clauses &cup; new
 * </code>
 * </pre>
 * 
 * Figure 7.12 A simple resolution algorithm for propositional logic. The
 * function PL-RESOLVE returns the set of all possible clauses obtained by
 * resolving its two inputs.
 * 
 * @author Ravi Mohan
 * @author Mike Stampone
 */
public class ml_Resolution {

	/**
	 * Returns the answer to the specified question using PL-Resolution.
	 * 
	 * @param kb
	 *            the knowledge base, a sentence in propositional logic
	 * @param alpha
	 *            the query, a sentence in propositional logic
	 * 
	 * @return the answer to the specified question using PL-Resolution.
	 */
	public boolean plResolution(ml_KnowledgeBase kb, String alpha) {
		return plResolution(kb, (ml_a_ParseTreeSentence) new ml_Parser().parse(alpha));
	}

	/**
	 * Returns the answer to the specified question using PL-Resolution.
	 * 
	 * @param kb
	 *            the knowledge base, a sentence in propositional logic
	 * @param alpha
	 *            the query, a sentence in propositional logic
	 * 
	 * @return the answer to the specified question using PL-Resolution.
	 */
	public boolean plResolution(ml_KnowledgeBase kb, ml_a_ParseTreeSentence alpha) {
		ml_a_ParseTreeSentence kBAndNotAlpha = new ml_SentenceBinary("AND", kb.asSentence(),
				new ml_SentenceComplexUnary(alpha));
		Set<ml_a_ParseTreeSentence> clauses = new mf_CNFClauseGatherer()
				.getClausesFrom(new mf_CNFTransformer().transform(kBAndNotAlpha));
		clauses = filterOutClausesWithTwoComplementaryLiterals(clauses);
		Set<ml_a_ParseTreeSentence> newClauses = new HashSet<ml_a_ParseTreeSentence>();
		while (true) {
			List<List<ml_a_ParseTreeSentence>> pairs = getCombinationPairs(new d_ConvertListSet<ml_a_ParseTreeSentence>()
					.setToList(clauses));

			for (int i = 0; i < pairs.size(); i++) {
				List<ml_a_ParseTreeSentence> pair = pairs.get(i);
				// System.out.println("pair number" + i+" of "+pairs.size());
				Set<ml_a_ParseTreeSentence> resolvents = plResolve(pair.get(0), pair.get(1));
				resolvents = filterOutClausesWithTwoComplementaryLiterals(resolvents);

				if (resolvents.contains(new ml_SentenceAtomicSymbol("EMPTY_CLAUSE"))) {
					return true;
				}
				newClauses = da_SetOps.union(newClauses, resolvents);
				// System.out.println("clauseslist size = " +clauses.size());

			}
			if (da_SetOps.intersection(newClauses, clauses).size() == newClauses
					.size()) {// subset test
				return false;
			}
			clauses = da_SetOps.union(newClauses, clauses);
			clauses = filterOutClausesWithTwoComplementaryLiterals(clauses);
		}

	}

	public Set<ml_a_ParseTreeSentence> plResolve(ml_a_ParseTreeSentence clause1, ml_a_ParseTreeSentence clause2) {
		Set<ml_a_ParseTreeSentence> resolvents = new HashSet<ml_a_ParseTreeSentence>();
		ClauseSymbols cs = new ClauseSymbols(clause1, clause2);
		Iterator<ml_SentenceAtomicSymbol> iter = cs.getComplementedSymbols().iterator();
		while (iter.hasNext()) {
			ml_SentenceAtomicSymbol symbol = iter.next();
			resolvents.add(createResolventClause(cs, symbol));
		}

		return resolvents;
	}

	public boolean plResolution(String kbs, String alphaString) {
		ml_KnowledgeBase kb = new ml_KnowledgeBase();
		kb.tell(kbs);
		ml_a_ParseTreeSentence alpha = (ml_a_ParseTreeSentence) new ml_Parser().parse(alphaString);
		return plResolution(kb, alpha);
	}

	//
	// PRIVATE METHODS
	//

	private Set<ml_a_ParseTreeSentence> filterOutClausesWithTwoComplementaryLiterals(
			Set<ml_a_ParseTreeSentence> clauses) {
		Set<ml_a_ParseTreeSentence> filtered = new HashSet<ml_a_ParseTreeSentence>();
		ml_SymbolClassifier classifier = new ml_SymbolClassifier();
		Iterator<ml_a_ParseTreeSentence> iter = clauses.iterator();
		while (iter.hasNext()) {
			ml_a_ParseTreeSentence clause = iter.next();
			Set<ml_SentenceAtomicSymbol> positiveSymbols = classifier
					.getPositiveSymbolsIn(clause);
			Set<ml_SentenceAtomicSymbol> negativeSymbols = classifier
					.getNegativeSymbolsIn(clause);
			if ((da_SetOps.intersection(positiveSymbols, negativeSymbols).size() == 0)) {
				filtered.add(clause);
			}
		}
		return filtered;
	}

	private ml_a_ParseTreeSentence createResolventClause(ClauseSymbols cs, ml_SentenceAtomicSymbol toRemove) {
		List<ml_SentenceAtomicSymbol> positiveSymbols = new d_ConvertListSet<ml_SentenceAtomicSymbol>().setToList(da_SetOps
				.union(cs.clause1PositiveSymbols, cs.clause2PositiveSymbols));
		List<ml_SentenceAtomicSymbol> negativeSymbols = new d_ConvertListSet<ml_SentenceAtomicSymbol>().setToList(da_SetOps
				.union(cs.clause1NegativeSymbols, cs.clause2NegativeSymbols));
		if (positiveSymbols.contains(toRemove)) {
			positiveSymbols.remove(toRemove);
		}
		if (negativeSymbols.contains(toRemove)) {
			negativeSymbols.remove(toRemove);
		}

		Collections.sort(positiveSymbols, new ml_SymbolComparator());
		Collections.sort(negativeSymbols, new ml_SymbolComparator());

		List<ml_a_ParseTreeSentence> sentences = new ArrayList<ml_a_ParseTreeSentence>();
		for (int i = 0; i < positiveSymbols.size(); i++) {
			sentences.add(positiveSymbols.get(i));
		}
		for (int i = 0; i < negativeSymbols.size(); i++) {
			sentences.add(new ml_SentenceComplexUnary(negativeSymbols.get(i)));
		}
		if (sentences.size() == 0) {
			return new ml_SentenceAtomicSymbol("EMPTY_CLAUSE"); // == empty clause
		} else {
			return ml_LogicUtils.chainWith("OR", sentences);
		}

	}

	private List<List<ml_a_ParseTreeSentence>> getCombinationPairs(List<ml_a_ParseTreeSentence> clausesList) {
		// int odd = clausesList.size() % 2;
		// int midpoint = 0;
		// if (odd == 1) {
		// midpoint = (clausesList.size() / 2) + 1;
		// } else {
		// midpoint = (clausesList.size() / 2);
		// }

		List<List<ml_a_ParseTreeSentence>> pairs = new ArrayList<List<ml_a_ParseTreeSentence>>();
		for (int i = 0; i < clausesList.size(); i++) {
			for (int j = i; j < clausesList.size(); j++) {
				List<ml_a_ParseTreeSentence> pair = new ArrayList<ml_a_ParseTreeSentence>();
				ml_a_ParseTreeSentence first = clausesList.get(i);
				ml_a_ParseTreeSentence second = clausesList.get(j);

				if (!(first.equals(second))) {
					pair.add(first);
					pair.add(second);
					pairs.add(pair);
				}
			}
		}
		return pairs;
	}

	class ClauseSymbols {
		Set<ml_SentenceAtomicSymbol> clause1Symbols, clause1PositiveSymbols,
				clause1NegativeSymbols;

		Set<ml_SentenceAtomicSymbol> clause2Symbols, clause2PositiveSymbols,
				clause2NegativeSymbols;

		Set<ml_SentenceAtomicSymbol> positiveInClause1NegativeInClause2,
				negativeInClause1PositiveInClause2;

		public ClauseSymbols(ml_a_ParseTreeSentence clause1, ml_a_ParseTreeSentence clause2) {

			ml_SymbolClassifier classifier = new ml_SymbolClassifier();

			clause1Symbols = classifier.getSymbolsIn(clause1);
			clause1PositiveSymbols = classifier.getPositiveSymbolsIn(clause1);
			clause1NegativeSymbols = classifier.getNegativeSymbolsIn(clause1);

			clause2Symbols = classifier.getSymbolsIn(clause2);
			clause2PositiveSymbols = classifier.getPositiveSymbolsIn(clause2);
			clause2NegativeSymbols = classifier.getNegativeSymbolsIn(clause2);

			positiveInClause1NegativeInClause2 = da_SetOps.intersection(
					clause1PositiveSymbols, clause2NegativeSymbols);
			negativeInClause1PositiveInClause2 = da_SetOps.intersection(
					clause1NegativeSymbols, clause2PositiveSymbols);

		}

		public Set<ml_SentenceAtomicSymbol> getComplementedSymbols() {
			return da_SetOps.union(positiveInClause1NegativeInClause2,
					negativeInClause1PositiveInClause2);
		}

	}
}