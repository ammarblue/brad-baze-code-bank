package com.software.reuze;
//package aima.core.probability.proposition;

import java.util.Map;


//import aima.core.probability.RandomVariable;

public class mp_PropositionTermOpsAssignment extends mp_a_PropositionTerm {
	private Object value = null;
	//
	private String toString = null;

	public mp_PropositionTermOpsAssignment(mp_i_RandomVariable forVariable, Object value) {
		super(forVariable);
		setValue(value);
	}

	public Object getValue() {
		return value;
	}
	
	public void setValue(Object value) {
		if (null == value) {
			throw new IllegalArgumentException(
					"The value for the Random Variable must be specified.");
		}
		this.value = value;
	}

	@Override
	public boolean holds(Map<mp_i_RandomVariable, Object> possibleWorld) {
		return value.equals(possibleWorld.get(getTermVariable()));
	}

	@Override
	public String toString() {
		if (null == toString) {
			StringBuilder sb = new StringBuilder();
			sb.append(getTermVariable().getName());
			sb.append(" = ");
			sb.append(value);

			toString = sb.toString();
		}
		return toString;
	}
}
