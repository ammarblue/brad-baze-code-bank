package com.software.reuze;
//package aima.core.logic.propositional.parsing.ast;

//import aima.core.logic.propositional.parsing.PLVisitor;

/**
 * @author Ravi Mohan
 * 
 */
public class ml_SentenceComplexUnary extends ml_a_SentenceComplex {
	private ml_a_ParseTreeSentence negated;

	public ml_a_ParseTreeSentence getNegated() {
		return negated;
	}

	public ml_SentenceComplexUnary(ml_a_ParseTreeSentence negated) {
		this.negated = negated;
	}

	@Override
	public boolean equals(Object o) {

		if (this == o) {
			return true;
		}
		if ((o == null) || (this.getClass() != o.getClass())) {
			return false;
		}
		ml_SentenceComplexUnary ns = (ml_SentenceComplexUnary) o;
		return (ns.negated.equals(negated));

	}

	@Override
	public int hashCode() {
		int result = 17;
		result = 37 * result + negated.hashCode();
		return result;
	}

	@Override
	public String toString() {
		return " ( NOT " + negated.toString() + " ) ";
	}

	@Override
	public Object accept(ml_i_SentenceVisit plv, Object arg) {
		return plv.visitNotSentence(this, arg);
	}
}