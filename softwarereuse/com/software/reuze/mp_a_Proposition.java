package com.software.reuze;
//package aima.core.probability.proposition;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;


//import aima.core.probability.RandomVariable;

public abstract class mp_a_Proposition implements mp_i_Proposition {

	private LinkedHashSet<mp_i_RandomVariable> scope = new LinkedHashSet<mp_i_RandomVariable>();
	private LinkedHashSet<mp_i_RandomVariable> unboundScope = new LinkedHashSet<mp_i_RandomVariable>();

	public mp_a_Proposition() {

	}

	//
	// START-Proposition
	public Set<mp_i_RandomVariable> getScope() {
		return scope;
	}

	public Set<mp_i_RandomVariable> getUnboundScope() {
		return unboundScope;
	}

	public abstract boolean holds(Map<mp_i_RandomVariable, Object> possibleWorld);

	// END-Proposition
	//

	//
	// Protected Methods
	//
	protected void addScope(mp_i_RandomVariable var) {
		scope.add(var);
	}

	protected void addScope(Collection<mp_i_RandomVariable> vars) {
		scope.addAll(vars);
	}

	protected void addUnboundScope(mp_i_RandomVariable var) {
		unboundScope.add(var);
	}

	protected void addUnboundScope(Collection<mp_i_RandomVariable> vars) {
		unboundScope.addAll(vars);
	}
}
