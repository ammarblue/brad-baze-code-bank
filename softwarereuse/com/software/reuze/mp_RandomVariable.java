package com.software.reuze;
//package aima.core.probability.util;


import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.software.reuze.mp_i_Domain;

//import aima.core.probability.RandomVariable;
//import aima.core.probability.domain.Domain;
//import aima.core.probability.proposition.TermProposition;

/**
 * Default implementation of the RandomVariable interface.
 * 
 * Note: Also implements the TermProposition interface so its easy to use
 * RandomVariables in conjunction with propositions about them in the
 * Probability Model APIs.
 * 
 * @author Ciaran O'Reilly
 */
public class mp_RandomVariable implements mp_i_RandomVariable, mp_i_PropositionTerm {
	private String name = null;
	private mp_i_Domain domain = null;
	private Set<mp_i_RandomVariable> scope = new HashSet<mp_i_RandomVariable>();

	public mp_RandomVariable(String name, mp_i_Domain domain) {
		mp_ProbUtil.checkValidRandomVariableName(name);
		if (null == domain) {
			throw new IllegalArgumentException(
					"Domain of RandomVariable must be specified.");
		}

		this.name = name;
		this.domain = domain;
		this.scope.add(this);
	}

	//
	// START-RandomVariable
	public String getName() {
		return name;
	}

	public mp_i_Domain getDomain() {
		return domain;
	}

	// END-RandomVariable
	//

	//
	// START-TermProposition
	public mp_i_RandomVariable getTermVariable() {
		return this;
	}

	public Set<mp_i_RandomVariable> getScope() {
		return scope;
	}

	public Set<mp_i_RandomVariable> getUnboundScope() {
		return scope;
	}

	public boolean holds(Map<mp_i_RandomVariable, Object> possibleWorld) {
		return possibleWorld.containsKey(getTermVariable());
	}

	// END-TermProposition
	//

	@Override
	public boolean equals(Object o) {

		if (this == o) {
			return true;
		}
		if (!(o instanceof mp_i_RandomVariable)) {
			return false;
		}

		// The name (not the name:domain combination) uniquely identifies a
		// Random Variable
		mp_i_RandomVariable other = (mp_i_RandomVariable) o;

		return this.name.equals(other.getName());
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	@Override
	public String toString() {
		return getName();
	}
}
