package com.software.reuze;
//package aima.core.probability.proposition;

import java.util.Iterator;
import java.util.Map;


//import aima.core.probability.RandomVariable;

public class mp_PropositionDerivedEquivalent extends mp_a_PropositionDerived {
	//
	private String toString = null;

	public mp_PropositionDerivedEquivalent(String name, mp_i_RandomVariable... equivs) {
		super(name);

		if (null == equivs || 1 >= equivs.length) {
			throw new IllegalArgumentException(
					"Equivalent variables must be specified.");
		}
		for (mp_i_RandomVariable rv : equivs) {
			addScope(rv);
		}
	}

	//
	// START-Proposition
	public boolean holds(Map<mp_i_RandomVariable, Object> possibleWorld) {
		boolean holds = true;

		Iterator<mp_i_RandomVariable> i = getScope().iterator();
		mp_i_RandomVariable rvC, rvL = i.next();
		while (i.hasNext()) {
			rvC = i.next();
			if (!possibleWorld.get(rvL).equals(possibleWorld.get(rvC))) {
				holds = false;
				break;
			}
			rvL = rvC;
		}

		return holds;
	}

	// END-Proposition
	//

	@Override
	public String toString() {
		if (null == toString) {
			StringBuilder sb = new StringBuilder();
			sb.append(getDerivedName());
			for (mp_i_RandomVariable rv : getScope()) {
				sb.append(" = ");
				sb.append(rv);
			}
			toString = sb.toString();
		}
		return toString;
	}
}
