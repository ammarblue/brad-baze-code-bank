package com.software.reuze;
//package aima.core.logic.fol.parsing.ast;

import java.util.List;

//import aima.core.logic.fol.parsing.FOLVisitor;

/**
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 */
public class mf_NodeTermVariable implements mf_i_NodeTerm {
	private String value;
	private int hashCode = 0;
	private int indexical = -1;

	public mf_NodeTermVariable(String s) {
		value = s.trim();
	}

	public mf_NodeTermVariable(String s, int idx) {
		value = s.trim();
		indexical = idx;
	}

	public String getValue() {
		return value;
	}

	//
	// START-Term
	public String getSymbolicName() {
		return getValue();
	}

	public boolean isCompound() {
		return false;
	}

	public List<mf_i_NodeTerm> getArgs() {
		// Is not Compound, therefore should
		// return null for its arguments
		return null;
	}

	public Object accept(mf_Visitor v, Object arg) {
		return v.visitVariable(this, arg);
	}

	public mf_NodeTermVariable copy() {
		return new mf_NodeTermVariable(value, indexical);
	}

	// END-Term
	//

	public int getIndexical() {
		return indexical;
	}

	public void setIndexical(int idx) {
		indexical = idx;
		hashCode = 0;
	}

	public String getIndexedValue() {
		return value + indexical;
	}

	@Override
	public boolean equals(Object o) {

		if (this == o) {
			return true;
		}
		if (!(o instanceof mf_NodeTermVariable)) {
			return false;
		}

		mf_NodeTermVariable v = (mf_NodeTermVariable) o;
		return v.getValue().equals(getValue())
				&& v.getIndexical() == getIndexical();
	}

	@Override
	public int hashCode() {
		if (0 == hashCode) {
			hashCode = 17;
			hashCode += indexical;
			hashCode = 37 * hashCode + value.hashCode();
		}

		return hashCode;
	}

	@Override
	public String toString() {
		return value;
	}
}