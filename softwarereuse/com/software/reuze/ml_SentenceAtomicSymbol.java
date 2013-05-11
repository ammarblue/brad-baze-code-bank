package com.software.reuze;
//package aima.core.logic.propositional.parsing.ast;

//import aima.core.logic.propositional.parsing.PLVisitor;

/**
 * @author Ravi Mohan
 * 
 */
public class ml_SentenceAtomicSymbol extends ml_a_SentenceAtomic {
	private String value;

	public ml_SentenceAtomicSymbol(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	@Override
	public boolean equals(Object o) {

		if (this == o) {
			return true;
		}
		if ((o == null) || (this.getClass() != o.getClass())) {
			return false;
		}
		ml_SentenceAtomicSymbol sym = (ml_SentenceAtomicSymbol) o;
		return (sym.getValue().equals(getValue()));

	}

	@Override
	public int hashCode() {
		int result = 17;
		result = 37 * result + value.hashCode();
		return result;
	}

	@Override
	public String toString() {
		return getValue();
	}

	@Override
	public Object accept(ml_i_SentenceVisit plv, Object arg) {
		return plv.visitSymbol(this, arg);
	}
}