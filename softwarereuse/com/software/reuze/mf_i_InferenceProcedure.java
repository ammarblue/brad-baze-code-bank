package com.software.reuze;
//package aima.core.logic.fol.inference;

//import aima.core.logic.fol.kb.FOLKnowledgeBase;
//import aima.core.logic.fol.parsing.ast.Sentence;

/**
 * @author Ciaran O'Reilly
 * 
 */
public interface mf_i_InferenceProcedure {
	/**
	 * 
	 * @param kb
	 *            the knowledge base against which the query is to be made.
	 * @param query
	 *            the query to be answered.
	 * @return an InferenceResult.
	 */
	mf_i_InferenceResult ask(mf_KnowledgeBase kb, mf_i_Sentence query);
}
