package com.software.reuze;
//package aima.core.logic.fol.inference.proof;

import java.util.ArrayList;
import java.util.Collections;
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
public class mf_ProofStepFoChAssertFact extends mf_a_ProofStep {
	//
	private List<m_ProofStep> predecessors = new ArrayList<m_ProofStep>();
	//
	private mf_Clause implication = null;
	private mf_Literal fact = null;
	private Map<mf_NodeTermVariable, mf_i_NodeTerm> bindings = null;

	public mf_ProofStepFoChAssertFact(mf_Clause implication, mf_Literal fact,
			Map<mf_NodeTermVariable, mf_i_NodeTerm> bindings, m_ProofStep predecessor) {
		this.implication = implication;
		this.fact = fact;
		this.bindings = bindings;
		if (null != predecessor) {
			predecessors.add(predecessor);
		}
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
		List<mf_Literal> nLits = implication.getNegativeLiterals();
		for (int i = 0; i < implication.getNumberNegativeLiterals(); i++) {
			sb.append(nLits.get(i).getAtomicSentence());
			if (i != (implication.getNumberNegativeLiterals() - 1)) {
				sb.append(" AND ");
			}
		}
		sb.append(" => ");
		sb.append(implication.getPositiveLiterals().get(0));
		return sb.toString();
	}

	@Override
	public String getJustification() {
		return "Assert fact " + fact.toString() + ", " + bindings;
	}
	// END-ProofStep
	//
}
