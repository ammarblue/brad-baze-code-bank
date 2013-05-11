package com.software.reuze;

//package aima.core.probability.proposition;

//import aima.core.probability.RandomVariable;

public abstract class mp_a_PropositionTerm extends mp_a_Proposition
		implements mp_i_PropositionTerm {

	private mp_i_RandomVariable termVariable = null;

	public mp_a_PropositionTerm(mp_i_RandomVariable var) {
		if (null == var) {
			throw new IllegalArgumentException(
					"The Random Variable for the Term must be specified.");
		}
		this.termVariable = var;
		addScope(this.termVariable);
	}

	public mp_i_RandomVariable getTermVariable() {
		return termVariable;
	}
}
