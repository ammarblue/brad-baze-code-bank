package com.software.reuze;
//package aima.core.logic.fol.inference.proof;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/*import aima.core.logic.fol.kb.data.Clause;
import aima.core.logic.fol.kb.data.Literal;
import aima.core.logic.fol.parsing.ast.Term;
import aima.core.logic.fol.parsing.ast.Variable;*/

/**
 * @author Ciaran O'Reilly
 * 
 */
public class mf_ProofStepBwChGoal extends mf_a_ProofStep {
	//
	private List<m_ProofStep> predecessors = new ArrayList<m_ProofStep>();
	//
	private mf_Clause toProve = null;
	private mf_Literal currentGoal = null;
	private Map<mf_NodeTermVariable, mf_i_NodeTerm> bindings = new LinkedHashMap<mf_NodeTermVariable, mf_i_NodeTerm>();

	public mf_ProofStepBwChGoal(mf_Clause toProve, mf_Literal currentGoal,
			Map<mf_NodeTermVariable, mf_i_NodeTerm> bindings) {
		this.toProve = toProve;
		this.currentGoal = currentGoal;
		this.bindings.putAll(bindings);
	}

	public Map<mf_NodeTermVariable, mf_i_NodeTerm> getBindings() {
		return bindings;
	}

	public void setPredecessor(m_ProofStep predecessor) {
		predecessors.clear();
		predecessors.add(predecessor);
	}

	//
	// START-ProofStep
	@Override
	public List<m_ProofStep> getPredecessorSteps() {
		return Collections.unmodifiableList(predecessors);
	}

	@Override
	public String getProof() {
		StringBuilder sb = new StringBuilder();
		List<mf_Literal> nLits = toProve.getNegativeLiterals();
		for (int i = 0; i < toProve.getNumberNegativeLiterals(); i++) {
			sb.append(nLits.get(i).getAtomicSentence());
			if (i != (toProve.getNumberNegativeLiterals() - 1)) {
				sb.append(" AND ");
			}
		}
		if (toProve.getNumberNegativeLiterals() > 0) {
			sb.append(" => ");
		}
		sb.append(toProve.getPositiveLiterals().get(0));
		return sb.toString();
	}

	@Override
	public String getJustification() {
		return "Current Goal " + currentGoal.getAtomicSentence().toString()
				+ ", " + bindings;
	}
	// END-ProofStep
	//
}
