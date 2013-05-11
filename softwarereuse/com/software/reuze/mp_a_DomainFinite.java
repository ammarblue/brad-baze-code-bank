package com.software.reuze;
//package aima.core.probability.domain;


import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.software.reuze.ml_i_DomainDiscreteFinite;

public abstract class mp_a_DomainFinite implements ml_i_DomainDiscreteFinite {

	private String toString = null;
	private Map<Object, Integer> valueToIdx = new HashMap<Object, Integer>();
	private Map<Integer, Object> idxToValue = new HashMap<Integer, Object>();

	public mp_a_DomainFinite() {

	}

	//
	// START-Domain
	public boolean isFinite() {
		return true;
	}

	public boolean isInfinite() {
		return false;
	}

	public abstract int size();

	public abstract boolean isOrdered();

	// END-Domain
	//

	//
	// START-FiniteDomain
	public abstract Set<? extends Object> getPossibleValues();

	public int getOffset(Object value) {
		Integer idx = valueToIdx.get(value);
		if (null == idx) {
			throw new IllegalArgumentException("Value [" + value
					+ "] is not a possible value of this domain.");
		}
		return idx.intValue();
	}

	public Object getValueAt(int offset) {
		return idxToValue.get(offset);
	}

	// END-FiniteDomain
	//

	@Override
	public String toString() {
		if (null == toString) {
			toString = getPossibleValues().toString();
		}
		return toString;
	}

	//
	// PROTECTED METHODS
	//
	protected void indexPossibleValues(Set<? extends Object> possibleValues) {
		int idx = 0;
		for (Object value : possibleValues) {
			valueToIdx.put(value, idx);
			idxToValue.put(idx, value);
			idx++;
		}
	}
}
