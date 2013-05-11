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
public class mf_ProofStepChainReduction extends mf_a_ProofStep {
	private List<m_ProofStep> predecessors = new ArrayList<m_ProofStep>();
	private mf_ProofStepChain reduction = null;
	private mf_ProofStepChain nearParent, farParent = null;
	private Map<mf_NodeTermVariable, mf_i_NodeTerm> subst = null;

	public mf_ProofStepChainReduction(mf_ProofStepChain reduction, mf_ProofStepChain nearParent,
			mf_ProofStepChain farParent, Map<mf_NodeTermVariable, mf_i_NodeTerm> subst) {
		this.reduction = reduction;
		this.nearParent = nearParent;
		this.farParent = farParent;
		this.subst = subst;
		this.predecessors.add(farParent.getProofStep());
		this.predecessors.add(nearParent.getProofStep());
	}

	//
	// START-ProofStep
	@Override
	public List<m_ProofStep> getPredecessorSteps() {
		return Collections.unmodifiableList(predecessors);
	}

	@Override
	public String getProof() {
		return reduction.toString();
	}

	@Override
	public String getJustification() {
		return "Reduction: " + nearParent.getProofStep().getStepNumber() + ","
				+ farParent.getProofStep().getStepNumber() + " " + subst;
	}
	// END-ProofStep
	//
}
