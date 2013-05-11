package com.software.reuze;
//package aima.core.logic.propositional.algorithms;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


/*import aima.core.logic.propositional.parsing.PEParser;
import aima.core.logic.propositional.parsing.ast.Sentence;
import aima.core.logic.propositional.parsing.ast.Symbol;
import aima.core.logic.propositional.parsing.ast.UnarySentence;
import aima.core.logic.propositional.visitors.CNFClauseGatherer;
import aima.core.logic.propositional.visitors.CNFTransformer;
import aima.core.logic.propositional.visitors.SymbolClassifier;
import aima.core.logic.propositional.visitors.SymbolCollector;
import aima.core.util.Converter;
import aima.core.util.SetOps;*/

/**
 * @author Ravi Mohan
 * @author Mike Stampone
 */
public class ml_DPLL {

	private static final d_ConvertListSet<ml_SentenceAtomicSymbol> SYMBOL_CONVERTER = new d_ConvertListSet<ml_SentenceAtomicSymbol>();

	/**
	 * Returns <code>true</code> if the specified sentence is satisfiable. A
	 * sentence is satisfiable if it is true in, or satisfied by, some model.
	 * 
	 * @param s
	 *            a sentence in propositional logic
	 * 
	 * @return <code>true</code> if the specified sentence is satisfiable.
	 */
	public boolean dpllSatisfiable(ml_a_ParseTreeSentence s) {

		return dpllSatisfiable(s, new ml_Model());
	}

	/**
	 * Returns <code>true</code> if the specified sentence is satisfiable. A
	 * sentence is satisfiable if it is true in, or satisfied by, some model.
	 * 
	 * @param string
	 *            a String representation of a Sentence in propositional logic
	 * 
	 * @return <code>true</code> if the specified sentence is satisfiable.
	 */
	public boolean dpllSatisfiable(String string) {
		ml_a_ParseTreeSentence sen = (ml_a_ParseTreeSentence) new ml_Parser().parse(string);
		return dpllSatisfiable(sen, new ml_Model());
	}

	/**
	 * Returns <code>true</code> if the specified sentence is satisfiable. A
	 * sentence is satisfiable if it is true in, or satisfied by, some model.
	 * 
	 * @param s
	 *            a sentence in propositional logic
	 * @param m
	 *            a model the sentence must be true in
	 * 
	 * @return <code>true</code> if the specified sentence is satisfiable.
	 */
	public boolean dpllSatisfiable(ml_a_ParseTreeSentence s, ml_Model m) {
		Set<ml_a_ParseTreeSentence> clauses = new mf_CNFClauseGatherer()
				.getClausesFrom(new mf_CNFTransformer().transform(s));
		List<ml_SentenceAtomicSymbol> symbols = SYMBOL_CONVERTER.setToList(new ml_SentenceVisitorSymbolCollector()
				.getSymbolsIn(s));
		// System.out.println(" numberOfSymbols = " + symbols.size());
		return dpll(clauses, symbols, m);
	}

	public List<ml_a_ParseTreeSentence> clausesWithNonTrueValues(List<ml_a_ParseTreeSentence> clauseList,
			ml_Model model) {
		List<ml_a_ParseTreeSentence> clausesWithNonTrueValues = new ArrayList<ml_a_ParseTreeSentence>();
		for (int i = 0; i < clauseList.size(); i++) {
			ml_a_ParseTreeSentence clause = clauseList.get(i);
			if (!(isClauseTrueInModel(clause, model))) {
				if (!(clausesWithNonTrueValues.contains(clause))) {// defensive
					// programming not really necessary
					clausesWithNonTrueValues.add(clause);
				}
			}

		}
		return clausesWithNonTrueValues;
	}

	public SymbolValuePair findPureSymbolValuePair(List<ml_a_ParseTreeSentence> clauseList,
			ml_Model model, List<ml_SentenceAtomicSymbol> symbols) {
		List<ml_a_ParseTreeSentence> clausesWithNonTrueValues = clausesWithNonTrueValues(
				clauseList, model);
		ml_a_ParseTreeSentence nonTrueClauses = ml_LogicUtils.chainWith("AND",
				clausesWithNonTrueValues);
		// System.out.println("Unsatisfied clauses = "
		// + clausesWithNonTrueValues.size());
		Set<ml_SentenceAtomicSymbol> symbolsAlreadyAssigned = model.getAssignedSymbols();

		// debug
		// List symList = asList(symbolsAlreadyAssigned);
		//
		// System.out.println(" assignedSymbols = " + symList.size());
		// if (symList.size() == 52) {
		// System.out.println("untrue clauses = " + clausesWithNonTrueValues);
		// System.out.println("model= " + model);
		// }

		// debug
		List<ml_SentenceAtomicSymbol> purePositiveSymbols = SYMBOL_CONVERTER.setToList(da_SetOps
				.difference(new ml_SymbolClassifier()
						.getPurePositiveSymbolsIn(nonTrueClauses),
						symbolsAlreadyAssigned));

		List<ml_SentenceAtomicSymbol> pureNegativeSymbols = SYMBOL_CONVERTER.setToList(da_SetOps
				.difference(new ml_SymbolClassifier()
						.getPureNegativeSymbolsIn(nonTrueClauses),
						symbolsAlreadyAssigned));
		// if none found return "not found
		if ((purePositiveSymbols.size() == 0)
				&& (pureNegativeSymbols.size() == 0)) {
			return new SymbolValuePair();// automatically set to null values
		} else {
			if (purePositiveSymbols.size() > 0) {
				ml_SentenceAtomicSymbol symbol = new ml_SentenceAtomicSymbol(
						(purePositiveSymbols.get(0)).getValue());
				if (pureNegativeSymbols.contains(symbol)) {
					throw new RuntimeException("Symbol " + symbol.getValue()
							+ "misclassified");
				}
				return new SymbolValuePair(symbol, true);
			} else {
				ml_SentenceAtomicSymbol symbol = new ml_SentenceAtomicSymbol(
						(pureNegativeSymbols.get(0)).getValue());
				if (purePositiveSymbols.contains(symbol)) {
					throw new RuntimeException("Symbol " + symbol.getValue()
							+ "misclassified");
				}
				return new SymbolValuePair(symbol, false);
			}
		}
	}

	//
	// PRIVATE METHODS
	//

	private boolean dpll(Set<ml_a_ParseTreeSentence> clauses, List<ml_SentenceAtomicSymbol> symbols,
			ml_Model model) {
		// List<Sentence> clauseList = asList(clauses);
		List<ml_a_ParseTreeSentence> clauseList = new d_ConvertListSet<ml_a_ParseTreeSentence>()
				.setToList(clauses);
		// System.out.println("clauses are " + clauses.toString());
		// if all clauses are true return true;
		if (areAllClausesTrue(model, clauseList)) {
			// System.out.println(model.toString());
			return true;
		}
		// if even one clause is false return false
		if (isEvenOneClauseFalse(model, clauseList)) {
			// System.out.println(model.toString());
			return false;
		}
		// System.out.println("At least one clause is unknown");
		// try to find a unit clause
		SymbolValuePair svp = findPureSymbolValuePair(clauseList, model,
				symbols);
		if (svp.notNull()) {
			List<ml_SentenceAtomicSymbol> newSymbols = new ArrayList<ml_SentenceAtomicSymbol>(symbols);
			newSymbols.remove(new ml_SentenceAtomicSymbol(svp.symbol.getValue()));
			ml_Model newModel = model.extend(new ml_SentenceAtomicSymbol(svp.symbol.getValue()),
					svp.value.booleanValue());
			return dpll(clauses, newSymbols, newModel);
		}

		SymbolValuePair svp2 = findUnitClause(clauseList, model, symbols);
		if (svp2.notNull()) {
			List<ml_SentenceAtomicSymbol> newSymbols = new ArrayList<ml_SentenceAtomicSymbol>(symbols);
			newSymbols.remove(new ml_SentenceAtomicSymbol(svp2.symbol.getValue()));
			ml_Model newModel = model.extend(new ml_SentenceAtomicSymbol(svp2.symbol.getValue()),
					svp2.value.booleanValue());
			return dpll(clauses, newSymbols, newModel);
		}

		ml_SentenceAtomicSymbol symbol = (ml_SentenceAtomicSymbol) symbols.get(0);
		// System.out.println("default behaviour selecting " + symbol);
		List<ml_SentenceAtomicSymbol> newSymbols = new ArrayList<ml_SentenceAtomicSymbol>(symbols);
		newSymbols.remove(0);
		return (dpll(clauses, newSymbols, model.extend(symbol, true)) || dpll(
				clauses, newSymbols, model.extend(symbol, false)));
	}

	private boolean isEvenOneClauseFalse(ml_Model model, List<ml_a_ParseTreeSentence> clauseList) {
		for (int i = 0; i < clauseList.size(); i++) {
			ml_a_ParseTreeSentence clause = clauseList.get(i);
			if (model.isFalse(clause)) {
				// System.out.println(clause.toString() + " is false");
				return true;
			}

		}

		return false;
	}

	private boolean areAllClausesTrue(ml_Model model, List<ml_a_ParseTreeSentence> clauseList) {

		for (int i = 0; i < clauseList.size(); i++) {
			ml_a_ParseTreeSentence clause = clauseList.get(i);
			// System.out.println("evaluating " + clause.toString());
			if (!isClauseTrueInModel(clause, model)) { // ie if false or
				// UNKNOWN
				// System.out.println(clause.toString()+ " is not true");
				return false;
			}

		}
		return true;
	}

	private boolean isClauseTrueInModel(ml_a_ParseTreeSentence clause, ml_Model model) {
		List<ml_SentenceAtomicSymbol> positiveSymbols = SYMBOL_CONVERTER
				.setToList(new ml_SymbolClassifier().getPositiveSymbolsIn(clause));
		List<ml_SentenceAtomicSymbol> negativeSymbols = SYMBOL_CONVERTER
				.setToList(new ml_SymbolClassifier().getNegativeSymbolsIn(clause));

		for (ml_SentenceAtomicSymbol symbol : positiveSymbols) {
			if ((model.isTrue(symbol))) {
				return true;
			}
		}
		for (ml_SentenceAtomicSymbol symbol : negativeSymbols) {
			if ((model.isFalse(symbol))) {
				return true;
			}
		}
		return false;

	}

	private SymbolValuePair findUnitClause(List<ml_a_ParseTreeSentence> clauseList,
			ml_Model model, List<ml_SentenceAtomicSymbol> symbols) {
		for (int i = 0; i < clauseList.size(); i++) {
			ml_a_ParseTreeSentence clause = (ml_a_ParseTreeSentence) clauseList.get(i);
			if ((clause instanceof ml_SentenceAtomicSymbol)
					&& (!(model.getAssignedSymbols().contains(clause)))) {
				// System.out.println("found unit clause - assigning");
				return new SymbolValuePair(new ml_SentenceAtomicSymbol(
						((ml_SentenceAtomicSymbol) clause).getValue()), true);
			}

			if (clause instanceof ml_SentenceComplexUnary) {
				ml_SentenceComplexUnary sentence = (ml_SentenceComplexUnary) clause;
				ml_a_ParseTreeSentence negated = sentence.getNegated();
				if ((negated instanceof ml_SentenceAtomicSymbol)
						&& (!(model.getAssignedSymbols().contains(negated)))) {
					// System.out.println("found unit clause type 2 -
					// assigning");
					return new SymbolValuePair(new ml_SentenceAtomicSymbol(
							((ml_SentenceAtomicSymbol) negated).getValue()), false);
				}
			}

		}

		return new SymbolValuePair();// failed to find any unit clause;

	}

	public class SymbolValuePair {
		public ml_SentenceAtomicSymbol symbol;// public to avoid unnecessary get and set

		// accessors

		public Boolean value;

		public SymbolValuePair() {
			// represents "No Symbol found with a boolean value that makes all
			// its literals true
			symbol = null;
			value = null;
		}

		public SymbolValuePair(ml_SentenceAtomicSymbol symbol, boolean bool) {
			// represents "Symbol found with a boolean value that makes all
			// its literals true
			this.symbol = symbol;
			value = new Boolean(bool);
		}

		public boolean notNull() {
			return (symbol != null) && (value != null);
		}

		@Override
		public String toString() {
			String symbolString, valueString;
			if (symbol == null) {
				symbolString = "NULL";
			} else {
				symbolString = symbol.toString();
			}
			if (value == null) {
				valueString = "NULL";
			} else {
				valueString = value.toString();
			}
			return symbolString + " -> " + valueString;
		}
	}
}