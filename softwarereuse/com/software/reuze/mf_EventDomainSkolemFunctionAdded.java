package com.software.reuze;
//package aima.core.logic.fol.domain;

/**
 * @author Ciaran O'Reilly
 * 
 */
public class mf_EventDomainSkolemFunctionAdded extends mf_a_EventDomain {

	private static final long serialVersionUID = 1L;

	private String skolemFunctionName;

	public mf_EventDomainSkolemFunctionAdded(Object source,
			String skolemFunctionName) {
		super(source);

		this.skolemFunctionName = skolemFunctionName;
	}

	public String getSkolemConstantName() {
		return skolemFunctionName;
	}

	@Override
	public void notifyListener(mf_i_DomainListener listener) {
		listener.skolemFunctionAdded(this);
	}
}
