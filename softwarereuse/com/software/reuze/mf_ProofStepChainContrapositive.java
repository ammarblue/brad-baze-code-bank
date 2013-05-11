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
public class mf_ProofStepChainContrapositive extends mf_a_ProofStep {
	private List<m_ProofStep> predecessors = new ArrayList<m_ProofStep>();
	private mf_ProofStepChain contrapositive = null;
	private mf_ProofStepChain contrapositiveOf = null;

	public mf_ProofStepChainContrapositive(mf_ProofStepChain contrapositive,
			mf_ProofStepChain contrapositiveOf) {
		this.contrapositive = contrapositive;
		this.contrapositiveOf = contrapositiveOf;
		this.predecessors.add(contrapositiveOf.getProofStep());
	}

	//
	// START-ProofStep
	@Override
	public List<m_ProofStep> getPredecessorSteps() {
		return Collections.unmodifiableList(predecessors);
	}

	@Override
	public String getProof() {
		return contrapositive.toString();
	}

	@Override
	public String getJustification() {
		return "Contrapositive: "
				+ contrapositiveOf.getProofStep().getStepNumber();
	}
	// END-ProofStep
	//
}
