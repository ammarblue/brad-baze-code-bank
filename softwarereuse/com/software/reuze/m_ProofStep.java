package com.software.reuze;
//package aima.core.logic.fol.inference.proof;

import java.util.List;

/**
 * @author Ciaran O'Reilly
 * 
 */
public interface m_ProofStep {
	int getStepNumber();

	void setStepNumber(int step);

	List<m_ProofStep> getPredecessorSteps();

	String getProof();

	String getJustification();
}
