package com.software.reuze;
//package aima.core.logic.fol.parsing.ast;

import java.util.List;

/**
 * @author Ciaran O'Reilly
 * 
 */
public interface mf_i_SentenceAtomic extends mf_i_Sentence {
	List<mf_i_NodeTerm> getArgs();

	mf_i_SentenceAtomic copy();
}
