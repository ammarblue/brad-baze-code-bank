package com.software.reuze;
//package aima.core.logic.fol.domain;

/**
 * @author Ciaran O'Reilly
 * 
 */
public class mf_EventDomainAnswerLiteralAdded extends mf_a_EventDomain {

	private static final long serialVersionUID = 1L;

	private String answerLiteralName;

	public mf_EventDomainAnswerLiteralAdded(Object source,
			String answerLiteralName) {
		super(source);

		this.answerLiteralName = answerLiteralName;
	}

	public String getAnswerLiteralNameName() {
		return answerLiteralName;
	}

	@Override
	public void notifyListener(mf_i_DomainListener listener) {
		listener.answerLiteralNameAdded(this);
	}
}
