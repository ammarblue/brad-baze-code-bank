package com.software.reuze;
//package aima.core.logic.fol.inference.proof;

import java.util.List;

/**
 * @author Ciaran O'Reilly
 * 
 */
public abstract class mf_a_ProofStep implements m_ProofStep {
	private int step = 0;

	public mf_a_ProofStep() {

	}

	//
	// START-ProofStep
	public int getStepNumber() {
		return step;
	}

	public void setStepNumber(int step) {
		this.step = step;
	}

	public abstract List<m_ProofStep> getPredecessorSteps();

	public abstract String getProof();

	public abstract String getJustification();

	// END-ProofStep
	//
}
