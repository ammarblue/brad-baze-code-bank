package com.software.reuze;
//package aima.core.logic.propositional.parsing.ast;

//import aima.core.logic.propositional.parsing.PLVisitor;

/**
 * @author Ravi Mohan
 * 
 */
public class ml_SentenceAtomicFalse extends ml_a_SentenceAtomic {
	@Override
	public String toString() {
		return "FALSE";
	}

	@Override
	public Object accept(ml_i_SentenceVisit plv, Object arg) {
		return plv.visitFalseSentence(this, arg);
	}
}