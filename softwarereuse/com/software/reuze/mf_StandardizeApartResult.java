package com.software.reuze;
//package aima.core.logic.fol;

import java.util.Map;

//import aima.core.logic.fol.parsing.ast.Sentence;
//import aima.core.logic.fol.parsing.ast.Term;
//import aima.core.logic.fol.parsing.ast.Variable;

/**
 * @author Ciaran O'Reilly
 * 
 */
public class mf_StandardizeApartResult {
	private mf_i_Sentence originalSentence = null;
	private mf_i_Sentence standardized = null;
	private Map<mf_NodeTermVariable, mf_i_NodeTerm> forwardSubstitution = null;
	private Map<mf_NodeTermVariable, mf_i_NodeTerm> reverseSubstitution = null;

	public mf_StandardizeApartResult(mf_i_Sentence originalSentence,
			mf_i_Sentence standardized, Map<mf_NodeTermVariable, mf_i_NodeTerm> forwardSubstitution,
			Map<mf_NodeTermVariable, mf_i_NodeTerm> reverseSubstitution) {
		this.originalSentence = originalSentence;
		this.standardized = standardized;
		this.forwardSubstitution = forwardSubstitution;
		this.reverseSubstitution = reverseSubstitution;
	}

	public mf_i_Sentence getOriginalSentence() {
		return originalSentence;
	}

	public mf_i_Sentence getStandardized() {
		return standardized;
	}

	public Map<mf_NodeTermVariable, mf_i_NodeTerm> getForwardSubstitution() {
		return forwardSubstitution;
	}

	public Map<mf_NodeTermVariable, mf_i_NodeTerm> getReverseSubstitution() {
		return reverseSubstitution;
	}
}
