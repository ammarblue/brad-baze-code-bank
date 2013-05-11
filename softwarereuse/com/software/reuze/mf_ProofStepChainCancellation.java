package com.software.reuze;
//package aima.core.logic.fol.inference.proof;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

//import aima.core.logic.fol.kb.data.Chain;
//import aima.core.logic.fol.parsing.ast.Term;
//import aima.core.logic.fol.parsing.ast.Variable;

/**
 * @author Ciaran O'Reilly
 * 
 */
public class mf_ProofStepChainCancellation extends mf_a_ProofStep {
	private List<m_ProofStep> predecessors = new ArrayList<m_ProofStep>();
	private mf_ProofStepChain cancellation = null;
	private mf_ProofStepChain cancellationOf = null;
	private Map<mf_NodeTermVariable, mf_i_NodeTerm> subst = null;

	public mf_ProofStepChainCancellation(mf_ProofStepChain cancellation, mf_ProofStepChain cancellationOf,
			Map<mf_NodeTermVariable, mf_i_NodeTerm> subst) {
		this.cancellation = cancellation;
		this.cancellationOf = cancellationOf;
		this.subst = subst;
		this.predecessors.add(cancellationOf.getProofStep());
	}

	//
	// START-ProofStep
	@Override
	public List<m_ProofStep> getPredecessorSteps() {
		return Collections.unmodifiableList(predecessors);
	}

	@Override
	public String getProof() {
		return cancellation.toString();
	}

	@Override
	public String getJustification() {
		return "Cancellation: " + cancellationOf.getProofStep().getStepNumber()
				+ " " + subst;
	}
	// END-ProofStep
	//
}
