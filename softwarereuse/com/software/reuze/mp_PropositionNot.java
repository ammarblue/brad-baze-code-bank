package com.software.reuze;
//package aima.core.probability.proposition;


import java.util.Map;

import com.software.reuze.mp_a_Proposition;

//import aima.core.probability.RandomVariable;

public class mp_PropositionNot extends mp_a_Proposition implements
		mp_i_PropositionSentenceUnary {

	private mp_i_Proposition proposition;
	//
	private String toString = null;

	public mp_PropositionNot(mp_i_Proposition prop) {
		if (null == prop) {
			throw new IllegalArgumentException(
					"Proposition to be negated must be specified.");
		}
		// Track nested scope
		addScope(prop.getScope());
		addUnboundScope(prop.getUnboundScope());

		proposition = prop;
	}

	@Override
	public boolean holds(Map<mp_i_RandomVariable, Object> possibleWorld) {
		return !proposition.holds(possibleWorld);
	}

	@Override
	public String toString() {
		if (null == toString) {
			StringBuilder sb = new StringBuilder();
			sb.append("(NOT ");
			sb.append(proposition.toString());
			sb.append(")");

			toString = sb.toString();
		}
		return toString;
	}
}
