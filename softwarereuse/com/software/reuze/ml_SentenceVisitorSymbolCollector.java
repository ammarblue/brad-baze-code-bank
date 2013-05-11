package com.software.reuze;
//package aima.core.logic.propositional.visitors;

import java.util.HashSet;
import java.util.Set;

//import aima.core.logic.propositional.parsing.ast.Sentence;
//import aima.core.logic.propositional.parsing.ast.Symbol;

/**
 * @author Ravi Mohan
 * 
 */
public class ml_SentenceVisitorSymbolCollector extends ml_SentenceVisitorBasic {

	@SuppressWarnings("unchecked")
	@Override
	public Object visitSymbol(ml_SentenceAtomicSymbol s, Object arg) {
		Set<ml_SentenceAtomicSymbol> symbolsCollectedSoFar = (Set<ml_SentenceAtomicSymbol>) arg;
		symbolsCollectedSoFar.add(new ml_SentenceAtomicSymbol(s.getValue()));
		return symbolsCollectedSoFar;
	}

	@SuppressWarnings("unchecked")
	public Set<ml_SentenceAtomicSymbol> getSymbolsIn(ml_a_ParseTreeSentence s) {
		if (s == null) {// empty knowledge bases == null fix this later
			return new HashSet<ml_SentenceAtomicSymbol>();
		}
		return (Set<ml_SentenceAtomicSymbol>) s.accept(this, new HashSet<ml_SentenceAtomicSymbol>());
	}
}
