package com.software.reuze;
//package aima.core.probability.proposition;

public abstract class mp_a_PropositionDerived extends mp_a_Proposition
		implements ml_SentencePropositionDerived {

	private String name = null;

	public mp_a_PropositionDerived(String name) {
		this.name = name;
	}

	//
	// START-DerivedProposition
	public String getDerivedName() {
		return name;
	}

	// END-DerivedProposition
	//
}