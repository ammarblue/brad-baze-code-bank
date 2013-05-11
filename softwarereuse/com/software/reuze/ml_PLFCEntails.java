package com.software.reuze;
//package aima.core.logic.propositional.algorithms;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Stack;

/*import aima.core.logic.propositional.parsing.ast.BinarySentence;
import aima.core.logic.propositional.parsing.ast.Sentence;
import aima.core.logic.propositional.parsing.ast.Symbol;
import aima.core.logic.propositional.visitors.SymbolCollector;
import aima.core.util.Converter;*/

/**
 * @author Ravi Mohan
 * @author Mike Stampone
 */
public class ml_PLFCEntails {

	private Hashtable<HornClause, Integer> count;

	private Hashtable<ml_SentenceAtomicSymbol, Boolean> inferred;

	private Stack<ml_SentenceAtomicSymbol> agenda;

	public ml_PLFCEntails() {
		count = new Hashtable<HornClause, Integer>();
		inferred = new Hashtable<ml_SentenceAtomicSymbol, Boolean>();
		agenda = new Stack<ml_SentenceAtomicSymbol>();
	}

	/**
	 * Return the answer to the specified question using the PL-FC-Entails
	 * algorithm
	 * 
	 * @param kb
	 *            the knowledge base, a set of propositional definite clauses
	 * @param s
	 *            the query, a proposition symbol
	 * 
	 * @return the answer to the specified question using the PL-FC-Entails
	 *         algorithm
	 */
	public boolean plfcEntails(ml_KnowledgeBase kb, String s) {
		return plfcEntails(kb, new ml_SentenceAtomicSymbol(s));
	}

	/**
	 * Return the answer to the specified question using the PL-FC-Entails
	 * algorithm
	 * 
	 * @param kb
	 *            the knowledge base, a set of propositional definite clauses
	 * @param q
	 *            the query, a proposition symbol
	 * 
	 * @return the answer to the specified question using the PL-FC-Entails
	 *         algorithm
	 */
	public boolean plfcEntails(ml_KnowledgeBase kb, ml_SentenceAtomicSymbol q) {
		List<HornClause> hornClauses = asHornClauses(kb.getSentences());
		while (agenda.size() != 0) {
			ml_SentenceAtomicSymbol p = agenda.pop();
			while (!inferred(p)) {
				inferred.put(p, Boolean.TRUE);

				for (int i = 0; i < hornClauses.size(); i++) {
					HornClause hornClause = hornClauses.get(i);
					if (hornClause.premisesContainsSymbol(p)) {
						decrementCount(hornClause);
						if (countisZero(hornClause)) {
							if (hornClause.head().equals(q)) {
								return true;
							} else {
								agenda.push(hornClause.head());
							}
						}
					}
				}
			}
		}
		return false;
	}

	private List<HornClause> asHornClauses(List<ml_a_ParseTreeSentence> sentences) {
		List<HornClause> hornClauses = new ArrayList<HornClause>();
		for (int i = 0; i < sentences.size(); i++) {
			ml_a_ParseTreeSentence sentence = sentences.get(i);
			HornClause clause = new HornClause(sentence);
			hornClauses.add(clause);
		}
		return hornClauses;
	}

	private boolean countisZero(HornClause hornClause) {

		return (count.get(hornClause)).intValue() == 0;
	}

	private void decrementCount(HornClause hornClause) {
		int value = (count.get(hornClause)).intValue();
		count.put(hornClause, new Integer(value - 1));

	}

	private boolean inferred(ml_SentenceAtomicSymbol p) {
		Object value = inferred.get(p);
		return ((value == null) || value.equals(Boolean.TRUE));
	}

	public class HornClause {
		List<ml_SentenceAtomicSymbol> premiseSymbols;

		ml_SentenceAtomicSymbol head;

		/**
		 * Constructs a horn clause from the specified sentence.
		 * 
		 * @param sentence
		 *            a sentence in propositional logic
		 */
		public HornClause(ml_a_ParseTreeSentence sentence) {
			if (sentence instanceof ml_SentenceAtomicSymbol) {
				head = (ml_SentenceAtomicSymbol) sentence;
				agenda.push(head);
				premiseSymbols = new ArrayList<ml_SentenceAtomicSymbol>();
				count.put(this, new Integer(0));
				inferred.put(head, Boolean.FALSE);
			} else if (!isImpliedSentence(sentence)) {
				throw new RuntimeException("Sentence " + sentence
						+ " is not a horn clause");

			} else {
				ml_SentenceBinary bs = (ml_SentenceBinary) sentence;
				head = (ml_SentenceAtomicSymbol) bs.getSecond();
				inferred.put(head, Boolean.FALSE);
				Set<ml_SentenceAtomicSymbol> symbolsInPremise = new ml_SentenceVisitorSymbolCollector()
						.getSymbolsIn(bs.getFirst());
				Iterator<ml_SentenceAtomicSymbol> iter = symbolsInPremise.iterator();
				while (iter.hasNext()) {
					inferred.put(iter.next(), Boolean.FALSE);
				}
				premiseSymbols = new d_ConvertListSet<ml_SentenceAtomicSymbol>()
						.setToList(symbolsInPremise);
				count.put(this, new Integer(premiseSymbols.size()));
			}

		}

		private boolean isImpliedSentence(ml_a_ParseTreeSentence sentence) {
			return ((sentence instanceof ml_SentenceBinary) && ((ml_SentenceBinary) sentence)
					.getOperator().equals("=>"));
		}

		/**
		 * Returns the conclusion of this horn clause. In horn form, the premise
		 * is called the body, and the conclusion is called the head.
		 * 
		 * @return the conclusion of this horn clause.
		 */
		public ml_SentenceAtomicSymbol head() {

			return head;
		}

		/**
		 * Return <code>true</code> if the premise of this horn clause contains
		 * the specified symbol.
		 * 
		 * @param q
		 *            a symbol in propositional logic
		 * 
		 * @return <code>true</code> if the premise of this horn clause contains
		 *         the specified symbol.
		 */
		public boolean premisesContainsSymbol(ml_SentenceAtomicSymbol q) {
			return premiseSymbols.contains(q);
		}

		/**
		 * Returns a list of all the symbols in the premise of this horn clause
		 * 
		 * @return a list of all the symbols in the premise of this horn clause
		 */
		public List<ml_SentenceAtomicSymbol> getPremiseSymbols() {
			return premiseSymbols;
		}

		@Override
		public boolean equals(Object o) {

			if (this == o) {
				return true;
			}
			if ((o == null) || (this.getClass() != o.getClass())) {
				return false;
			}
			HornClause ohc = (HornClause) o;
			if (premiseSymbols.size() != ohc.premiseSymbols.size()) {
				return false;
			}
			for (ml_SentenceAtomicSymbol s : premiseSymbols) {
				if (!ohc.premiseSymbols.contains(s)) {
					return false;
				}
			}

			return true;
		}

		@Override
		public int hashCode() {
			int result = 17;
			for (ml_SentenceAtomicSymbol s : premiseSymbols) {
				result = 37 * result + s.hashCode();
			}
			return result;
		}

		@Override
		public String toString() {
			return premiseSymbols.toString() + " => " + head;
		}
	}
}