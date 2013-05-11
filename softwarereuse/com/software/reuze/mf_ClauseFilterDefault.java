package com.software.reuze;
//package aima.core.logic.fol.inference.otter.defaultimpl;

import java.util.Set;

//import aima.core.logic.fol.inference.otter.ClauseFilter;
//import aima.core.logic.fol.kb.data.Clause;

/**
 * @author Ciaran O'Reilly
 * 
 */
public class mf_ClauseFilterDefault implements mf_i_ClauseFilter {
	public mf_ClauseFilterDefault() {

	}

	//
	// START-ClauseFilter
	public Set<mf_Clause> filter(Set<mf_Clause> clauses) {
		return clauses;
	}

	// END-ClauseFilter
	//
}
