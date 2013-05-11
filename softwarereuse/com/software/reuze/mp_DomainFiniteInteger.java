package com.software.reuze;
//package aima.core.probability.domain;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;


public class mp_DomainFiniteInteger extends mp_a_DomainFinite {

	private Set<Integer> possibleValues = null;

	public mp_DomainFiniteInteger(Integer... pValues) {
		// Keep consistent order
		possibleValues = new LinkedHashSet<Integer>();
		for (Integer v : pValues) {
			possibleValues.add(v);
		}
		// Ensure cannot be modified
		possibleValues = Collections.unmodifiableSet(possibleValues);

		indexPossibleValues(possibleValues);
	}

	//
	// START-Domain
	@Override
	public int size() {
		return possibleValues.size();
	}

	@Override
	public boolean isOrdered() {
		return true;
	}

	// END-Domain
	//

	//
	// START-DiscreteDomain
	@Override
	public Set<Integer> getPossibleValues() {
		return possibleValues;
	}

	// END-DiscreteDomain
	//

	@Override
	public boolean equals(Object o) {

		if (this == o) {
			return true;
		}
		if (!(o instanceof mp_DomainFiniteInteger)) {
			return false;
		}

		mp_DomainFiniteInteger other = (mp_DomainFiniteInteger) o;

		return this.possibleValues.equals(other.possibleValues);
	}

	@Override
	public int hashCode() {
		return possibleValues.hashCode();
	}
}
