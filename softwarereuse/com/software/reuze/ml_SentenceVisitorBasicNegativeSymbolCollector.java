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
public class ml_SentenceVisitorBasicNegativeSymbolCollector extends ml_SentenceVisitorBasic {
	@SuppressWarnings("unchecked")
	@Override
	public Object visitNotSentence(ml_SentenceComplexUnary ns, Object arg) {
		Set<ml_SentenceAtomicSymbol> s = (Set<ml_SentenceAtomicSymbol>) arg;
		if (ns.getNegated() instanceof ml_SentenceAtomicSymbol) {
			s.add((ml_SentenceAtomicSymbol) ns.getNegated());
		} else {
			s = da_SetOps
					.union(s, (Set<ml_SentenceAtomicSymbol>) ns.getNegated().accept(this, arg));
		}
		return s;
	}

	@SuppressWarnings("unchecked")
	public Set<ml_SentenceAtomicSymbol> getNegativeSymbolsIn(ml_a_ParseTreeSentence s) {
		return (Set<ml_SentenceAtomicSymbol>) s.accept(this, new HashSet<ml_SentenceAtomicSymbol>());
	}
}
