package com.software.reuze;
//package aima.core.logic.fol.inference.proof;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Ciaran O'Reilly
 * 
 */
public class mf_ProofStepPremise extends mf_a_ProofStep {
	//
	private static final List<m_ProofStep> _noPredecessors = new ArrayList<m_ProofStep>();
	//
	private Object proof = "";

	public mf_ProofStepPremise(Object proof) {
		this.proof = proof;
	}

	//
	// START-ProofStep
	@Override
	public List<m_ProofStep> getPredecessorSteps() {
		return Collections.unmodifiableList(_noPredecessors);
	}

	@Override
	public String getProof() {
		return proof.toString();
	}

	@Override
	public String getJustification() {
		return "Premise";
	}
	// END-ProofStep
	//
}
