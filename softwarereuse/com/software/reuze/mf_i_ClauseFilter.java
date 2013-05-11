package com.software.reuze;
//package aima.core.logic.fol.inference.otter;

import java.util.Set;

//import aima.core.logic.fol.kb.data.Clause;

/**
 * @author Ciaran O'Reilly
 * 
 */
public interface mf_i_ClauseFilter {
	Set<mf_Clause> filter(Set<mf_Clause> clauses);
}
