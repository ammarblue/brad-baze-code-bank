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
public class mf_ProofStepClauseFactor extends mf_a_ProofStep {
	private List<m_ProofStep> predecessors = new ArrayList<m_ProofStep>();
	private mf_Clause factor = null;
	private mf_Clause factorOf = null;
	private mf_Literal lx = null;
	private mf_Literal ly = null;
	private Map<mf_NodeTermVariable, mf_i_NodeTerm> subst = new LinkedHashMap<mf_NodeTermVariable, mf_i_NodeTerm>();
	private Map<mf_NodeTermVariable, mf_i_NodeTerm> renameSubst = new LinkedHashMap<mf_NodeTermVariable, mf_i_NodeTerm>();

	public mf_ProofStepClauseFactor(mf_Clause factor, mf_Clause factorOf, mf_Literal lx,
			mf_Literal ly, Map<mf_NodeTermVariable, mf_i_NodeTerm> subst,
			Map<mf_NodeTermVariable, mf_i_NodeTerm> renameSubst) {
		this.factor = factor;
		this.factorOf = factorOf;
		this.lx = lx;
		this.ly = ly;
		this.subst.putAll(subst);
		this.renameSubst.putAll(renameSubst);
		this.predecessors.add(factorOf.getProofStep());
	}

	//
	// START-ProofStep
	public List<m_ProofStep> getPredecessorSteps() {
		return Collections.unmodifiableList(predecessors);
	}

	public String getProof() {
		return factor.toString();
	}

	public String getJustification() {
		return "Factor of " + factorOf.getProofStep().getStepNumber() + "  ["
				+ lx + ", " + ly + "], subst=" + subst + ", renaming="
				+ renameSubst;
	}
	// END-ProofStep
	//
}
