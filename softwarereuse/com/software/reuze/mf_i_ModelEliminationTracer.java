package com.software.reuze;
//package aima.core.logic.fol.inference.trace;

/**
 * @author Ciaran O'Reilly
 * 
 */
public interface mf_i_ModelEliminationTracer {
	void reset();

	void increment(int depth, int noFarParents);
}
