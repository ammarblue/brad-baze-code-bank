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
public class mf_ProofStepClauseParamodulation extends mf_a_ProofStep {
	private List<m_ProofStep> predecessors = new ArrayList<m_ProofStep>();
	private mf_Clause paramodulated = null;
	private mf_Clause topClause = null;
	private mf_Clause equalityClause = null;
	private mf_SentenceAtomicTermEquality assertion = null;

	public mf_ProofStepClauseParamodulation(mf_Clause paramodulated,
			mf_Clause topClause, mf_Clause equalityClause, mf_SentenceAtomicTermEquality assertion) {
		this.paramodulated = paramodulated;
		this.topClause = topClause;
		this.equalityClause = equalityClause;
		this.assertion = assertion;
		this.predecessors.add(topClause.getProofStep());
		this.predecessors.add(equalityClause.getProofStep());
	}

	//
	// START-ProofStep
	@Override
	public List<m_ProofStep> getPredecessorSteps() {
		return Collections.unmodifiableList(predecessors);
	}

	@Override
	public String getProof() {
		return paramodulated.toString();
	}

	@Override
	public String getJustification() {
		return "Paramodulation: " + topClause.getProofStep().getStepNumber()
				+ ", " + equalityClause.getProofStep().getStepNumber() + ", ["
				+ assertion + "]";

	}
	// END-ProofStep
	//
}
