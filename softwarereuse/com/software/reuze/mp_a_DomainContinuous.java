package com.software.reuze;
//package aima.core.probability.domain;

public abstract class mp_a_DomainContinuous implements mp_i_DomainContinuous {

	//
	// START-Domain
	public boolean isFinite() {
		return false;
	}

	public boolean isInfinite() {
		return true;
	}

	public int size() {
		throw new IllegalStateException(
				"You cannot determine the size of an infinite domain");
	}

	public abstract boolean isOrdered();
	// END-Domain
	//
}
