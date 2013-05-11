package com.software.reuze;
//package aima.core.logic.fol.inference.proof;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

//import aima.core.logic.fol.parsing.ast.Term;
//import aima.core.logic.fol.parsing.ast.Variable;

/**
 * @author Ciaran O'Reilly
 * 
 */
public class mf_ProofFinal implements mf_i_Proof {
	private Map<mf_NodeTermVariable, mf_i_NodeTerm> answerBindings = new LinkedHashMap<mf_NodeTermVariable, mf_i_NodeTerm>();
	private m_ProofStep finalStep = null;
	private List<m_ProofStep> proofSteps = null;

	public mf_ProofFinal(m_ProofStep finalStep, Map<mf_NodeTermVariable, mf_i_NodeTerm> answerBindings) {
		this.finalStep = finalStep;
		this.answerBindings.putAll(answerBindings);
	}

	//
	// START-Proof
	public List<m_ProofStep> getSteps() {
		// Only calculate if the proof steps are actually requested.
		if (null == proofSteps) {
			calcualteProofSteps();
		}
		return proofSteps;
	}

	public Map<mf_NodeTermVariable, mf_i_NodeTerm> getAnswerBindings() {
		return answerBindings;
	}

	public void replaceAnswerBindings(Map<mf_NodeTermVariable, mf_i_NodeTerm> updatedBindings) {
		answerBindings.clear();
		answerBindings.putAll(updatedBindings);
	}

	// END-Proof
	//

	@Override
	public String toString() {
		return answerBindings.toString();
	}

	//
	// PRIVATE METHODS
	//
	private void calcualteProofSteps() {
		proofSteps = new ArrayList<m_ProofStep>();
		addToProofSteps(finalStep);

		// Move all premises to the front of the
		// list of steps
		int to = 0;
		for (int i = 0; i < proofSteps.size(); i++) {
			if (proofSteps.get(i) instanceof mf_ProofStepPremise) {
				m_ProofStep m = proofSteps.remove(i);
				proofSteps.add(to, m);
				to++;
			}
		}

		// Move the Goals after the premises
		for (int i = 0; i < proofSteps.size(); i++) {
			if (proofSteps.get(i) instanceof mf_ProofStepGoal) {
				m_ProofStep m = proofSteps.remove(i);
				proofSteps.add(to, m);
				to++;
			}
		}

		// Assign the step #s now that all the proof
		// steps have been unwound
		for (int i = 0; i < proofSteps.size(); i++) {
			proofSteps.get(i).setStepNumber(i + 1);
		}
	}

	private void addToProofSteps(m_ProofStep step) {
		if (!proofSteps.contains(step)) {
			proofSteps.add(0, step);
		} else {
			proofSteps.remove(step);
			proofSteps.add(0, step);
		}
		List<m_ProofStep> predecessors = step.getPredecessorSteps();
		for (int i = predecessors.size() - 1; i >= 0; i--) {
			addToProofSteps(predecessors.get(i));
		}
	}
}
