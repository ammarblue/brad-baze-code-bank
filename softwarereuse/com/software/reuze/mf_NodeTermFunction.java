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
public class mf_NodeTermFunction implements mf_i_NodeTerm {
	private String functionName;
	private List<mf_i_NodeTerm> terms = new ArrayList<mf_i_NodeTerm>();
	private String stringRep = null;
	private int hashCode = 0;

	public mf_NodeTermFunction(String functionName, List<mf_i_NodeTerm> terms) {
		this.functionName = functionName;
		this.terms.addAll(terms);
	}

	public String getFunctionName() {
		return functionName;
	}

	public List<mf_i_NodeTerm> getTerms() {
		return Collections.unmodifiableList(terms);
	}

	//
	// START-Term
	public String getSymbolicName() {
		return getFunctionName();
	}

	public boolean isCompound() {
		return true;
	}

	public List<mf_i_NodeTerm> getArgs() {
		return getTerms();
	}

	public Object accept(mf_Visitor v, Object arg) {
		return v.visitFunction(this, arg);
	}

	public mf_NodeTermFunction copy() {
		List<mf_i_NodeTerm> copyTerms = new ArrayList<mf_i_NodeTerm>();
		for (mf_i_NodeTerm t : terms) {
			copyTerms.add(t.copy());
		}
		return new mf_NodeTermFunction(functionName, copyTerms);
	}

	// END-Term
	//

	@Override
	public boolean equals(Object o) {

		if (this == o) {
			return true;
		}
		if (!(o instanceof mf_NodeTermFunction)) {
			return false;
		}

		mf_NodeTermFunction f = (mf_NodeTermFunction) o;

		return f.getFunctionName().equals(getFunctionName())
				&& f.getTerms().equals(getTerms());
	}

	@Override
	public int hashCode() {
		if (0 == hashCode) {
			hashCode = 17;
			hashCode = 37 * hashCode + functionName.hashCode();
			for (mf_i_NodeTerm t : terms) {
				hashCode = 37 * hashCode + t.hashCode();
			}
		}
		return hashCode;
	}

	@Override
	public String toString() {
		if (null == stringRep) {
			StringBuilder sb = new StringBuilder();
			sb.append(functionName);
			sb.append("(");

			boolean first = true;
			for (mf_i_NodeTerm t : terms) {
				if (first) {
					first = false;
				} else {
					sb.append(",");
				}
				sb.append(t.toString());
			}

			sb.append(")");

			stringRep = sb.toString();
		}
		return stringRep;
	}
}