package com.software.reuze;
//package aima.core.logic.propositional.visitors;

import java.util.Set;


//import aima.core.logic.propositional.parsing.ast.Sentence;
//import aima.core.logic.propositional.parsing.ast.Symbol;
//import aima.core.util.SetOps;

/**
 * @author Ravi Mohan
 * 
 */
public class ml_SymbolClassifier {

	public Set<ml_SentenceAtomicSymbol> getPositiveSymbolsIn(ml_a_ParseTreeSentence sentence) {
		return new ml_SentenceVisitorCollectorPositiveSymbol().getPositiveSymbolsIn(sentence);
	}

	public Set<ml_SentenceAtomicSymbol> getNegativeSymbolsIn(ml_a_ParseTreeSentence sentence) {
		return new ml_SentenceVisitorBasicNegativeSymbolCollector().getNegativeSymbolsIn(sentence);
	}

	public Set<ml_SentenceAtomicSymbol> getPureNegativeSymbolsIn(ml_a_ParseTreeSentence sentence) {
		Set<ml_SentenceAtomicSymbol> allNegatives = getNegativeSymbolsIn(sentence);
		Set<ml_SentenceAtomicSymbol> allPositives = getPositiveSymbolsIn(sentence);
		return da_SetOps.difference(allNegatives, allPositives);
	}

	public Set<ml_SentenceAtomicSymbol> getPurePositiveSymbolsIn(ml_a_ParseTreeSentence sentence) {
		Set<ml_SentenceAtomicSymbol> allNegatives = getNegativeSymbolsIn(sentence);
		Set<ml_SentenceAtomicSymbol> allPositives = getPositiveSymbolsIn(sentence);
		return da_SetOps.difference(allPositives, allNegatives);
	}

	public Set<ml_SentenceAtomicSymbol> getPureSymbolsIn(ml_a_ParseTreeSentence sentence) {
		Set<ml_SentenceAtomicSymbol> allPureNegatives = getPureNegativeSymbolsIn(sentence);
		Set<ml_SentenceAtomicSymbol> allPurePositives = getPurePositiveSymbolsIn(sentence);
		return da_SetOps.union(allPurePositives, allPureNegatives);
	}

	public Set<ml_SentenceAtomicSymbol> getImpureSymbolsIn(ml_a_ParseTreeSentence sentence) {
		Set<ml_SentenceAtomicSymbol> allNegatives = getNegativeSymbolsIn(sentence);
		Set<ml_SentenceAtomicSymbol> allPositives = getPositiveSymbolsIn(sentence);
		return da_SetOps.intersection(allPositives, allNegatives);
	}

	public Set<ml_SentenceAtomicSymbol> getSymbolsIn(ml_a_ParseTreeSentence sentence) {
		return new ml_SentenceVisitorSymbolCollector().getSymbolsIn(sentence);
	}
}