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
public class mf_ProofStepClauseBinaryResolvent extends mf_a_ProofStep {
	private List<m_ProofStep> predecessors = new ArrayList<m_ProofStep>();
	private mf_Clause resolvent = null;
	private mf_Literal posLiteral = null;
	private mf_Literal negLiteral = null;
	private mf_Clause parent1, parent2 = null;
	private Map<mf_NodeTermVariable, mf_i_NodeTerm> subst = new LinkedHashMap<mf_NodeTermVariable, mf_i_NodeTerm>();
	private Map<mf_NodeTermVariable, mf_i_NodeTerm> renameSubst = new LinkedHashMap<mf_NodeTermVariable, mf_i_NodeTerm>();

	public mf_ProofStepClauseBinaryResolvent(mf_Clause resolvent, mf_Literal pl,
			mf_Literal nl, mf_Clause parent1, mf_Clause parent2,
			Map<mf_NodeTermVariable, mf_i_NodeTerm> subst, Map<mf_NodeTermVariable, mf_i_NodeTerm> renameSubst) {
		this.resolvent = resolvent;
		this.posLiteral = pl;
		this.negLiteral = nl;
		this.parent1 = parent1;
		this.parent2 = parent2;
		this.subst.putAll(subst);
		this.renameSubst.putAll(renameSubst);
		this.predecessors.add(parent1.getProofStep());
		this.predecessors.add(parent2.getProofStep());
	}

	//
	// START-ProofStep
	public List<m_ProofStep> getPredecessorSteps() {
		return Collections.unmodifiableList(predecessors);
	}

	public String getProof() {
		return resolvent.toString();
	}

	public String getJustification() {
		int lowStep = parent1.getProofStep().getStepNumber();
		int highStep = parent2.getProofStep().getStepNumber();

		if (lowStep > highStep) {
			lowStep = highStep;
			highStep = parent1.getProofStep().getStepNumber();
		}

		return "Resolution: " + lowStep + ", " + highStep + "  [" + posLiteral
				+ ", " + negLiteral + "], subst=" + subst + ", renaming="
				+ renameSubst;
	}
	// END-ProofStep
	//
}
