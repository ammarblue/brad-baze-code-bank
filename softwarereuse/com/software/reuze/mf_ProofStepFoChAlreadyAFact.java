package com.software.reuze;
//package aima.core.logic.fol.inference.proof;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//import aima.core.logic.fol.kb.data.Literal;

/**
 * @author Ciaran O'Reilly
 * 
 */
public class mf_ProofStepFoChAlreadyAFact extends mf_a_ProofStep {
	//
	private static final List<m_ProofStep> _noPredecessors = new ArrayList<m_ProofStep>();
	//
	private mf_Literal fact = null;

	public mf_ProofStepFoChAlreadyAFact(mf_Literal fact) {
		this.fact = fact;
	}

	//
	// START-ProofStep
	@Override
	public List<m_ProofStep> getPredecessorSteps() {
		return Collections.unmodifiableList(_noPredecessors);
	}

	@Override
	public String getProof() {
		return fact.toString();
	}

	@Override
	public String getJustification() {
		return "Already a known fact in the KB.";
	}
	// END-ProofStep
	//
}
