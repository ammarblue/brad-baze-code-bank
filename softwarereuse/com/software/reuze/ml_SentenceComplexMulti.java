package com.software.reuze;
//package aima.core.logic.propositional.parsing.ast;

import java.util.List;

//import aima.core.logic.propositional.parsing.PLVisitor;

/**
 * @author Ravi Mohan
 * 
 */
public class ml_SentenceComplexMulti extends ml_a_SentenceComplex {
	private String operator;

	private List<ml_a_ParseTreeSentence> sentences;

	public ml_SentenceComplexMulti(String operator, List<ml_a_ParseTreeSentence> sentences) {
		this.operator = operator;
		this.sentences = sentences;
	}

	public String getOperator() {
		return operator;
	}

	public List<ml_a_ParseTreeSentence> getSentences() {
		return sentences;
	}

	@Override
	public boolean equals(Object o) {

		if (this == o) {
			return true;
		}
		if ((o == null) || (this.getClass() != o.getClass())) {
			return false;
		}
		ml_SentenceComplexMulti sen = (ml_SentenceComplexMulti) o;
		return ((sen.getOperator().equals(getOperator())) && (sen
				.getSentences().equals(getSentences())));

	}

	@Override
	public int hashCode() {
		int result = 17;
		for (ml_a_ParseTreeSentence s : sentences) {
			result = 37 * result + s.hashCode();
		}
		return result;
	}

	@Override
	public String toString() {
		String part1 = "( " + getOperator() + " ";
		for (int i = 0; i < getSentences().size(); i++) {
			part1 = part1 + sentences.get(i).toString() + " ";
		}
		return part1 + " ) ";
	}

	@Override
	public Object accept(ml_i_SentenceVisit plv, Object arg) {
		return plv.visitMultiSentence(this, arg);
	}
}