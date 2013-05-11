package com.software.reuze;
//package aima.core.logic.propositional.algorithms;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;


/*import aima.core.logic.propositional.parsing.PEParser;
import aima.core.logic.propositional.parsing.ast.Sentence;
import aima.core.logic.propositional.parsing.ast.Symbol;
import aima.core.logic.propositional.visitors.CNFClauseGatherer;
import aima.core.logic.propositional.visitors.CNFTransformer;
import aima.core.logic.propositional.visitors.SymbolCollector;
import aima.core.util.Converter;
import aima.core.util.Util;*/

/**
 * @author Ravi Mohan
 * @author Mike Stampone
 */
public class ml_WalkSAT {
	private ml_Model myModel;

	private Random random = new Random();

	/**
	 * Returns a satisfying model or failure (null).
	 * 
	 * @param logicalSentence
	 *            a set of clauses in propositional logic
	 * @param numberOfFlips
	 *            number of flips allowed before giving up
	 * @param probabilityOfRandomWalk
	 *            the probability of choosing to do a "random walk" move,
	 *            typically around 0.5
	 * 
	 * @return a satisfying model or failure (null).
	 */
	public ml_Model findModelFor(String logicalSentence, int numberOfFlips,
			double probabilityOfRandomWalk) {
		myModel = new ml_Model();
		ml_a_ParseTreeSentence s = (ml_a_ParseTreeSentence) new ml_Parser().parse(logicalSentence);
		mf_CNFTransformer transformer = new mf_CNFTransformer();
		mf_CNFClauseGatherer clauseGatherer = new mf_CNFClauseGatherer();
		ml_SentenceVisitorSymbolCollector sc = new ml_SentenceVisitorSymbolCollector();

		List<ml_SentenceAtomicSymbol> symbols = new d_ConvertListSet<ml_SentenceAtomicSymbol>().setToList(sc
				.getSymbolsIn(s));
		for (int i = 0; i < symbols.size(); i++) {
			ml_SentenceAtomicSymbol sym = (ml_SentenceAtomicSymbol) symbols.get(i);
			myModel = myModel.extend(sym, m_MathUtil2.randomBoolean());
		}
		List<ml_a_ParseTreeSentence> clauses = new d_ConvertListSet<ml_a_ParseTreeSentence>()
				.setToList(clauseGatherer.getClausesFrom(transformer
						.transform(s)));

		for (int i = 0; i < numberOfFlips; i++) {
			if (getNumberOfClausesSatisfiedIn(
					new d_ConvertListSet<ml_a_ParseTreeSentence>().listToSet(clauses), myModel) == clauses
					.size()) {
				return myModel;
			}
			ml_a_ParseTreeSentence clause = clauses.get(random.nextInt(clauses.size()));

			List<ml_SentenceAtomicSymbol> symbolsInClause = new d_ConvertListSet<ml_SentenceAtomicSymbol>().setToList(sc
					.getSymbolsIn(clause));
			if (random.nextDouble() >= probabilityOfRandomWalk) {
				ml_SentenceAtomicSymbol randomSymbol = symbolsInClause.get(random
						.nextInt(symbolsInClause.size()));
				myModel = myModel.flip(randomSymbol);
			} else {
				ml_SentenceAtomicSymbol symbolToFlip = getSymbolWhoseFlipMaximisesSatisfiedClauses(
						new d_ConvertListSet<ml_a_ParseTreeSentence>().listToSet(clauses),
						symbolsInClause, myModel);
				myModel = myModel.flip(symbolToFlip);
			}

		}
		return null;
	}

	private ml_SentenceAtomicSymbol getSymbolWhoseFlipMaximisesSatisfiedClauses(
			Set<ml_a_ParseTreeSentence> clauses, List<ml_SentenceAtomicSymbol> symbols, ml_Model model) {
		if (symbols.size() > 0) {
			ml_SentenceAtomicSymbol retVal = symbols.get(0);
			int maxClausesSatisfied = 0;
			for (int i = 0; i < symbols.size(); i++) {
				ml_SentenceAtomicSymbol sym = symbols.get(i);
				if (getNumberOfClausesSatisfiedIn(clauses, model.flip(sym)) > maxClausesSatisfied) {
					retVal = sym;
					maxClausesSatisfied = getNumberOfClausesSatisfiedIn(
							clauses, model.flip(sym));
				}
			}
			return retVal;
		} else {
			return null;
		}

	}

	private int getNumberOfClausesSatisfiedIn(Set<ml_a_ParseTreeSentence> clauses, ml_Model model) {
		int retVal = 0;
		Iterator<ml_a_ParseTreeSentence> i = clauses.iterator();
		while (i.hasNext()) {
			ml_a_ParseTreeSentence s = i.next();
			if (model.isTrue(s)) {
				retVal += 1;
			}
		}
		return retVal;
	}
}
