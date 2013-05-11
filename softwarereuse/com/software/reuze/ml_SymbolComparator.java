package com.software.reuze;
//package aima.core.logic.propositional.parsing.ast;

import java.util.Comparator;

/**
 * @author Ravi Mohan
 * 
 */
public class ml_SymbolComparator implements Comparator<ml_SentenceAtomicSymbol> {

	public int compare(ml_SentenceAtomicSymbol one, ml_SentenceAtomicSymbol two) {
		return one.getValue().compareTo(two.getValue());
	}
}
