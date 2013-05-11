package com.software.reuze;
//package aima.core.logic.fol.parsing.ast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//import aima.core.logic.fol.parsing.FOLVisitor;

/**
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 */
public class mf_SentenceConnected implements mf_i_Sentence {
	private String connector;
	private mf_i_Sentence first, second;
	private List<mf_i_Sentence> args = new ArrayList<mf_i_Sentence>();
	private String stringRep = null;
	private int hashCode = 0;

	public mf_SentenceConnected(String connector, mf_i_Sentence first, mf_i_Sentence second) {
		this.connector = connector;
		this.first = first;
		this.second = second;
		args.add(first);
		args.add(second);
	}

	public String getConnector() {
		return connector;
	}

	public mf_i_Sentence getFirst() {
		return first;
	}

	public mf_i_Sentence getSecond() {
		return second;
	}

	//
	// START-FOSentence
	public String getSymbolicName() {
		return getConnector();
	}

	public boolean isCompound() {
		return true;
	}

	public List<mf_i_Sentence> getArgs() {
		return Collections.unmodifiableList(args);
	}

	public Object accept(mf_Visitor v, Object arg) {
		return v.visitConnectedSentence(this, arg);
	}

	public mf_SentenceConnected copy() {
		return new mf_SentenceConnected(connector, first.copy(), second.copy());
	}

	// END-FOSentence
	//

	@Override
	public boolean equals(Object o) {

		if (this == o) {
			return true;
		}
		if ((o == null) || (this.getClass() != o.getClass())) {
			return false;
		}
		mf_SentenceConnected cs = (mf_SentenceConnected) o;
		return cs.getConnector().equals(getConnector())
				&& cs.getFirst().equals(getFirst())
				&& cs.getSecond().equals(getSecond());
	}

	@Override
	public int hashCode() {
		if (0 == hashCode) {
			hashCode = 17;
			hashCode = 37 * hashCode + getConnector().hashCode();
			hashCode = 37 * hashCode + getFirst().hashCode();
			hashCode = 37 * hashCode + getSecond().hashCode();
		}
		return hashCode;
	}

	@Override
	public String toString() {
		if (null == stringRep) {
			StringBuilder sb = new StringBuilder();
			sb.append("(");
			sb.append(first.toString());
			sb.append(" ");
			sb.append(connector);
			sb.append(" ");
			sb.append(second.toString());
			sb.append(")");
			stringRep = sb.toString();
		}
		return stringRep;
	}
}
