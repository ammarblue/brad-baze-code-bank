package com.software.reuze;
//package aima.core.probability.proposition;

import java.util.Map;


//import aima.core.probability.RandomVariable;

public class mp_PropositionConjunctive extends mp_a_Proposition implements
		ml_i_SentencePropositionBinary {

	private mp_i_Proposition left = null;
	private mp_i_Proposition right = null;
	//
	private String toString = null;

	public mp_PropositionConjunctive(mp_i_Proposition left, mp_i_Proposition right) {
		if (null == left) {
			throw new IllegalArgumentException(
					"Left side of conjunction must be specified.");
		}
		if (null == right) {
			throw new IllegalArgumentException(
					"Right side of conjunction must be specified.");
		}
		// Track nested scope
		addScope(left.getScope());
		addScope(right.getScope());
		addUnboundScope(left.getUnboundScope());
		addUnboundScope(right.getUnboundScope());

		this.left = left;
		this.right = right;
	}

	@Override
	public boolean holds(Map<mp_i_RandomVariable, Object> possibleWorld) {
		return left.holds(possibleWorld) && right.holds(possibleWorld);
	}

	@Override
	public String toString() {
		if (null == toString) {
			StringBuilder sb = new StringBuilder();
			sb.append("(");
			sb.append(left.toString());
			sb.append(" AND ");
			sb.append(right.toString());
			sb.append(")");

			toString = sb.toString();
		}

		return toString;
	}
}
