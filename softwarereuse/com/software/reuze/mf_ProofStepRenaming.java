package com.software.reuze;
//package aima.core.logic.fol.inference.proof;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Ciaran O'Reilly
 * 
 */
public class mf_ProofStepRenaming extends mf_a_ProofStep {
	private List<m_ProofStep> predecessors = new ArrayList<m_ProofStep>();
	private Object proof = "";

	public mf_ProofStepRenaming(Object proof, m_ProofStep predecessor) {
		this.proof = proof;
		this.predecessors.add(predecessor);
	}

	//
	// START-ProofStep
	@Override
	public List<m_ProofStep> getPredecessorSteps() {
		return Collections.unmodifiableList(predecessors);
	}

	@Override
	public String getProof() {
		return proof.toString();
	}

	@Override
	public String getJustification() {
		return "Renaming of " + predecessors.get(0).getStepNumber();
	}
	// END-ProofStep
	//
}
