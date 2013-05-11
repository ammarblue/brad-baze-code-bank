package com.software.reuze;
//package aima.core.agent.impl;

/**
 * @author Ciaran O'Reilly
 */
public class aa_ActionDynamicNoOp extends aa_ActionDynamic {

	public static final aa_ActionDynamicNoOp NO_OP = new aa_ActionDynamicNoOp();

	//
	// START-Action
	public boolean isNoOp() {
		return true;
	}

	// END-Action
	//

	private aa_ActionDynamicNoOp() {
		super("NoOp");
	}
}
