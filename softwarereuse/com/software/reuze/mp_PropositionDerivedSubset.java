package com.software.reuze;
//package aima.core.probability.proposition;


import java.util.Map;

import com.software.reuze.ml_i_DomainDiscreteFinite;
import com.software.reuze.mp_a_PropositionDerived;

//import aima.core.probability.RandomVariable;
//import aima.core.probability.domain.FiniteDomain;

public class mp_PropositionDerivedSubset extends mp_a_PropositionDerived {

	private ml_i_DomainDiscreteFinite subsetDomain = null;
	private mp_i_RandomVariable varSubsetOf = null;
	//
	private String toString = null;

	public mp_PropositionDerivedSubset(String name, ml_i_DomainDiscreteFinite subsetDomain,
			mp_i_RandomVariable ofVar) {
		super(name);

		if (null == subsetDomain) {
			throw new IllegalArgumentException("Sum Domain must be specified.");
		}
		this.subsetDomain = subsetDomain;
		this.varSubsetOf = ofVar;
		addScope(this.varSubsetOf);
	}

	//
	// START-Proposition
	public boolean holds(Map<mp_i_RandomVariable, Object> possibleWorld) {
		return subsetDomain.getPossibleValues().contains(
				possibleWorld.get(varSubsetOf));
	}

	// END-Proposition
	//

	@Override
	public String toString() {
		if (null == toString) {
			StringBuilder sb = new StringBuilder();
			sb.append(getDerivedName());
			sb.append(" = ");
			sb.append(subsetDomain.toString());
			toString = sb.toString();
		}
		return toString;
	}
}
