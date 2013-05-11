package com.software.reuze;
//package aima.core.logic.fol.inference.proof;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//import aima.core.logic.fol.kb.data.Chain;

/**
 * @author Ciaran O'Reilly
 * 
 */
public class mf_ProofStepChainDropped extends mf_a_ProofStep {
	private List<m_ProofStep> predecessors = new ArrayList<m_ProofStep>();
	private mf_ProofStepChain dropped = null;
	private mf_ProofStepChain droppedOff = null;

	public mf_ProofStepChainDropped(mf_ProofStepChain dropped, mf_ProofStepChain droppedOff) {
		this.dropped = dropped;
		this.droppedOff = droppedOff;
		this.predecessors.add(droppedOff.getProofStep());
	}

	//
	// START-ProofStep
	@Override
	public List<m_ProofStep> getPredecessorSteps() {
		return Collections.unmodifiableList(predecessors);
	}

	@Override
	public String getProof() {
		return dropped.toString();
	}

	@Override
	public String getJustification() {
		return "Dropped: " + droppedOff.getProofStep().getStepNumber();
	}
	// END-ProofStep
	//
}
