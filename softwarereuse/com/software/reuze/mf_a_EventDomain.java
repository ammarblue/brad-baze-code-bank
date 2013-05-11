package com.software.reuze;
//package aima.core.logic.fol.domain;

import java.util.EventObject;

/**
 * @author Ciaran O'Reilly
 * 
 */
public abstract class mf_a_EventDomain extends EventObject {

	private static final long serialVersionUID = 1L;

	public mf_a_EventDomain(Object source) {
		super(source);
	}

	public abstract void notifyListener(mf_i_DomainListener listener);
}
