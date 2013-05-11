package com.software.reuze;
//package aima.core.logic.propositional.visitors;

import java.util.Set;


/*import aima.core.logic.propositional.parsing.PLVisitor;
import aima.core.logic.propositional.parsing.ast.BinarySentence;
import aima.core.logic.propositional.parsing.ast.FalseSentence;
import aima.core.logic.propositional.parsing.ast.MultiSentence;
import aima.core.logic.propositional.parsing.ast.Symbol;
import aima.core.logic.propositional.parsing.ast.TrueSentence;
import aima.core.logic.propositional.parsing.ast.UnarySentence;
import aima.core.util.SetOps;*/

/**
 * Super class of Visitors that are "read only" and gather information from an
 * existing parse tree .
 * 
 * @author Ravi Mohan
 * 
 */
public class ml_SentenceVisitorBasic implements ml_i_SentenceVisit {

	public Object visitSymbol(ml_SentenceAtomicSymbol s, Object arg) {
		return arg;
	}

	public Object visitTrueSentence(ml_SentenceAtomicTrue ts, Object arg) {
		return arg;
	}

	public Object visitFalseSentence(ml_SentenceAtomicFalse fs, Object arg) {
		return arg;
	}

	@SuppressWarnings("unchecked")
	public Object visitNotSentence(ml_SentenceComplexUnary ns, Object arg) {
		Set s = (Set) arg;
		return da_SetOps.union(s, (Set) ns.getNegated().accept(this, arg));
	}

	@SuppressWarnings("unchecked")
	public Object visitBinarySentence(ml_SentenceBinary bs, Object arg) {
		Set s = (Set) arg;
		Set termunion = da_SetOps.union((Set) bs.getFirst().accept(this, arg),
				(Set) bs.getSecond().accept(this, arg));
		return da_SetOps.union(s, termunion);
	}

	public Object visitMultiSentence(ml_SentenceComplexMulti fs, Object arg) {
		throw new RuntimeException("Can't handle MultiSentence");
	}
}
