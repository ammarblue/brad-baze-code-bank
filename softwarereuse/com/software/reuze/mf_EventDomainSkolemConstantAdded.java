package com.software.reuze;
//package aima.core.logic.fol.domain;

/**
 * @author Ciaran O'Reilly
 * 
 */
public class mf_EventDomainSkolemConstantAdded extends mf_a_EventDomain {

	private static final long serialVersionUID = 1L;

	private String skolemConstantName;

	public mf_EventDomainSkolemConstantAdded(Object source,
			String skolemConstantName) {
		super(source);

		this.skolemConstantName = skolemConstantName;
	}

	public String getSkolemConstantName() {
		return skolemConstantName;
	}

	@Override
	public void notifyListener(mf_i_DomainListener listener) {
		listener.skolemConstantAdded(this);
	}
}
