package com.software.reuze;
//package aima.core.logic.fol.parsing.ast;

import java.util.List;

/**
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 */
public interface mf_i_NodeTerm extends mf_i_Node {
	List<mf_i_NodeTerm> getArgs();

	mf_i_NodeTerm copy();
}
