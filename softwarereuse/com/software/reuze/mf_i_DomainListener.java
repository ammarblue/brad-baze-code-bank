package com.software.reuze;
//package aima.core.logic.fol.domain;

/**
 * @author Ciaran O'Reilly
 * 
 */
public interface mf_i_DomainListener {
	void skolemConstantAdded(mf_EventDomainSkolemConstantAdded event);

	void skolemFunctionAdded(mf_EventDomainSkolemFunctionAdded event);

	void answerLiteralNameAdded(mf_EventDomainAnswerLiteralAdded event);
}
