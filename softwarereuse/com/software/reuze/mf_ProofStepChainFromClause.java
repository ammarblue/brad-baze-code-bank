package com.software.reuze;
//package aima.core.logic.fol.inference.proof;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//import aima.core.logic.fol.kb.data.Chain;
//import aima.core.logic.fol.kb.data.Clause;

/**
 * @author Ciaran O'Reilly
 * 
 */
public class mf_ProofStepChainFromClause extends mf_a_ProofStep {
	private List<m_ProofStep> predecessors = new ArrayList<m_ProofStep>();
	private mf_ProofStepChain chain = null;
	private mf_Clause fromClause = null;

	public mf_ProofStepChainFromClause(mf_ProofStepChain chain, mf_Clause fromClause) {
		this.chain = chain;
		this.fromClause = fromClause;
		this.predecessors.add(fromClause.getProofStep());
	}

	//
	// START-ProofStep
	@Override
	public List<m_ProofStep> getPredecessorSteps() {
		return Collections.unmodifiableList(predecessors);
	}

	@Override
	public String getProof() {
		return chain.toString();
	}

	@Override
	public String getJustification() {
		return "Chain from Clause: "
				+ fromClause.getProofStep().getStepNumber();
	}
	// END-ProofStep
	//
}
