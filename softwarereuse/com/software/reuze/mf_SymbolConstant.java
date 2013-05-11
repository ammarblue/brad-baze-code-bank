package com.software.reuze;
//package aima.core.logic.fol.parsing.ast;

import java.util.List;

//import aima.core.logic.fol.parsing.FOLVisitor;

/**
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 */
public class mf_SymbolConstant implements mf_i_NodeTerm {
	private String value;
	private int hashCode = 0;

	public mf_SymbolConstant(String s) {
		value = s;
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
		return v.visitConstant(this, arg);
	}

	public mf_SymbolConstant copy() {
		return new mf_SymbolConstant(value);
	}

	// END-Term
	//

	@Override
	public boolean equals(Object o) {

		if (this == o) {
			return true;
		}
		if (!(o instanceof mf_SymbolConstant)) {
			return false;
		}
		mf_SymbolConstant c = (mf_SymbolConstant) o;
		return c.getValue().equals(getValue());

	}

	@Override
	public int hashCode() {
		if (0 == hashCode) {
			hashCode = 17;
			hashCode = 37 * hashCode + value.hashCode();
		}
		return hashCode;
	}

	@Override
	public String toString() {
		return value;
	}
}
