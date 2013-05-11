package com.software.reuze;
//package aima.core.logic.propositional.visitors;

import java.util.HashSet;
import java.util.Set;


/*import aima.core.logic.propositional.parsing.ast.Sentence;
import aima.core.logic.propositional.parsing.ast.Symbol;
import aima.core.logic.propositional.parsing.ast.UnarySentence;
import aima.core.util.SetOps;*/

/**
 * @author Ravi Mohan
 * 
 */
public class ml_SentenceVisitorCollectorPositiveSymbol extends ml_SentenceVisitorBasic {
	@SuppressWarnings("unchecked")
	@Override
	public Object visitSymbol(ml_SentenceAtomicSymbol symbol, Object arg) {
		Set<ml_SentenceAtomicSymbol> s = (Set<ml_SentenceAtomicSymbol>) arg;
		s.add(symbol);// add ALL symbols not discarded by the visitNotSentence
		// mathod
		return arg;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object visitNotSentence(ml_SentenceComplexUnary ns, Object arg) {
		Set<ml_SentenceAtomicSymbol> s = (Set<ml_SentenceAtomicSymbol>) arg;
		if (ns.getNegated() instanceof ml_SentenceAtomicSymbol) {
			// do nothing .do NOT add a negated Symbol
		} else {
			s = da_SetOps
					.union(s, (Set<ml_SentenceAtomicSymbol>) ns.getNegated().accept(this, arg));
		}
		return s;
	}

	@SuppressWarnings("unchecked")
	public Set<ml_SentenceAtomicSymbol> getPositiveSymbolsIn(ml_a_ParseTreeSentence sentence) {
		return (Set<ml_SentenceAtomicSymbol>) sentence.accept(this, new HashSet<ml_SentenceAtomicSymbol>());
	}
}