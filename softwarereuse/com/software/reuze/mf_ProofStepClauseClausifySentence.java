package com.software.reuze;
//package aima.core.logic.fol.inference.proof;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//import aima.core.logic.fol.kb.data.Clause;
//import aima.core.logic.fol.parsing.ast.Sentence;

/**
 * @author Ciaran O'Reilly
 * 
 */
public class mf_ProofStepClauseClausifySentence extends mf_a_ProofStep {
	private List<m_ProofStep> predecessors = new ArrayList<m_ProofStep>();
	private mf_Clause clausified = null;

	public mf_ProofStepClauseClausifySentence(mf_Clause clausified,
			mf_i_Sentence origSentence) {
		this.clausified = clausified;
		this.predecessors.add(new mf_ProofStepPremise(origSentence));
	}

	//
	// START-ProofStep
	@Override
	public List<m_ProofStep> getPredecessorSteps() {
		return Collections.unmodifiableList(predecessors);
	}

	@Override
	public String getProof() {
		return clausified.toString();
	}

	@Override
	public String getJustification() {
		return "Clausified " + predecessors.get(0).getStepNumber();
	}
	// END-ProofStep
	//
}
