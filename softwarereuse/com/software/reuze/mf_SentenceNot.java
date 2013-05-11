package com.software.reuze;
//package aima.core.logic.fol.parsing.ast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//import aima.core.logic.fol.Connectors;
//import aima.core.logic.fol.parsing.FOLVisitor;

/**
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 */
public class mf_SentenceNot implements mf_i_Sentence {
	private mf_i_Sentence negated;
	private List<mf_i_Sentence> args = new ArrayList<mf_i_Sentence>();
	private String stringRep = null;
	private int hashCode = 0;

	public mf_SentenceNot(mf_i_Sentence negated) {
		this.negated = negated;
		args.add(negated);
	}

	public mf_i_Sentence getNegated() {
		return negated;
	}

	//
	// START-Sentence
	public String getSymbolicName() {
		return mf_SymbolsConnectors.NOT;
	}

	public boolean isCompound() {
		return true;
	}

	public List<mf_i_Sentence> getArgs() {
		return Collections.unmodifiableList(args);
	}

	public Object accept(mf_Visitor v, Object arg) {
		return v.visitNotSentence(this, arg);
	}

	public mf_SentenceNot copy() {
		return new mf_SentenceNot(negated.copy());
	}

	// END-Sentence
	//

	@Override
	public boolean equals(Object o) {

		if (this == o) {
			return true;
		}
		if ((o == null) || (this.getClass() != o.getClass())) {
			return false;
		}
		mf_SentenceNot ns = (mf_SentenceNot) o;
		return (ns.negated.equals(negated));
	}

	@Override
	public int hashCode() {
		if (0 == hashCode) {
			hashCode = 17;
			hashCode = 37 * hashCode + negated.hashCode();
		}
		return hashCode;
	}

	@Override
	public String toString() {
		if (null == stringRep) {
			StringBuilder sb = new StringBuilder();
			sb.append("NOT(");
			sb.append(negated.toString());
			sb.append(")");
			stringRep = sb.toString();
		}
		return stringRep;
	}
}
