package com.software.reuze;
//package aima.core.logic.fol.inference.proof;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//import aima.core.logic.fol.kb.data.Clause;
//import aima.core.logic.fol.parsing.ast.TermEquality;

/**
 * @author Ciaran O'Reilly
 * 
 */
public class mf_ProofStepClauseDemodulation extends mf_a_ProofStep {
	private List<m_ProofStep> predecessors = new ArrayList<m_ProofStep>();
	private mf_Clause demodulated = null;
	private mf_Clause origClause = null;
	private mf_SentenceAtomicTermEquality assertion = null;

	public mf_ProofStepClauseDemodulation(mf_Clause demodulated, mf_Clause origClause,
			mf_SentenceAtomicTermEquality assertion) {
		this.demodulated = demodulated;
		this.origClause = origClause;
		this.assertion = assertion;
		this.predecessors.add(origClause.getProofStep());
	}

	//
	// START-ProofStep
	@Override
	public List<m_ProofStep> getPredecessorSteps() {
		return Collections.unmodifiableList(predecessors);
	}

	@Override
	public String getProof() {
		return demodulated.toString();
	}

	@Override
	public String getJustification() {
		return "Demodulation: " + origClause.getProofStep().getStepNumber()
				+ ", [" + assertion + "]";
	}
	// END-ProofStep
	//
}
