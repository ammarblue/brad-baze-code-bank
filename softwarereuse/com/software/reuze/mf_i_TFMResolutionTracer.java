package com.software.reuze;
//package aima.core.logic.fol.inference.trace;

import java.util.Set;

//import aima.core.logic.fol.inference.InferenceResult;
//import aima.core.logic.fol.kb.data.Clause;

/**
 * @author Ciaran O'Reilly
 * 
 */
public interface mf_i_TFMResolutionTracer {
	void stepStartWhile(Set<mf_Clause> clauses, int totalNoClauses,
			int totalNoNewCandidateClauses);

	void stepOuterFor(mf_Clause i);

	void stepInnerFor(mf_Clause i, mf_Clause j);

	void stepResolved(mf_Clause iFactor, mf_Clause jFactor, Set<mf_Clause> resolvents);

	void stepFinished(Set<mf_Clause> clauses, mf_i_InferenceResult result);
}
