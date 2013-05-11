package com.software.reuze;
//package aima.core.logic.propositional.parsing.ast;

//import aima.core.logic.propositional.parsing.PLVisitor;

/**
 * @author Ravi Mohan
 * 
 */
public class ml_SentenceBinary extends ml_a_SentenceComplex {
	private String operator;

	private ml_a_ParseTreeSentence first;

	private ml_a_ParseTreeSentence second;

	public ml_SentenceBinary(String operator, ml_a_ParseTreeSentence first, ml_a_ParseTreeSentence second) {
		this.operator = operator;
		this.first = first;
		this.second = second;

	}

	public ml_a_ParseTreeSentence getFirst() {
		return first;
	}

	public String getOperator() {
		return operator;
	}

	public ml_a_ParseTreeSentence getSecond() {
		return second;
	}

	@Override
	public boolean equals(Object o) {

		if (this == o) {
			return true;
		}
		if ((o == null) || (this.getClass() != o.getClass())) {
			return false;
		}
		ml_SentenceBinary bs = (ml_SentenceBinary) o;
		return ((bs.getOperator().equals(getOperator()))
				&& (bs.getFirst().equals(first)) && (bs.getSecond()
				.equals(second)));

	}

	@Override
	public int hashCode() {
		int result = 17;
		result = 37 * result + first.hashCode();
		result = 37 * result + second.hashCode();
		return result;
	}

	@Override
	public String toString() {
		return " ( " + first.toString() + " " + operator + " "
				+ second.toString() + " )";
	}

	@Override
	public Object accept(ml_i_SentenceVisit plv, Object arg) {
		return plv.visitBinarySentence(this, arg);
	}

	public boolean isOrSentence() {
		return (getOperator().equals("OR"));
	}

	public boolean isAndSentence() {
		return (getOperator().equals("AND"));
	}

	public boolean isImplication() {
		return (getOperator().equals("=>"));
	}

	public boolean isBiconditional() {
		return (getOperator().equals("<=>"));
	}

	public boolean firstTermIsAndSentence() {
		return (getFirst() instanceof ml_SentenceBinary)
				&& (((ml_SentenceBinary) getFirst()).isAndSentence());
	}

	public boolean secondTermIsAndSentence() {
		return (getSecond() instanceof ml_SentenceBinary)
				&& (((ml_SentenceBinary) getSecond()).isAndSentence());
	}
}