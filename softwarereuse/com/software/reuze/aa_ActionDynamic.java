package com.software.reuze;

//package aima.core.agent.impl;

//import aima.core.agent.Action;

/**
 * @author Ciaran O'Reilly
 * @author Mike Stampone
 */
public class aa_ActionDynamic extends d_a_ObjectWithDynamicAttributes implements
		aa_i_Action {
	public static final String ATTRIBUTE_NAME = "name";

	//

	public aa_ActionDynamic(String name) {
		this.setAttribute(ATTRIBUTE_NAME, name);
	}

	/**
	 * Returns the value of the name attribute.
	 * 
	 * @return the value of the name attribute.
	 */
	public String getName() {
		return (String) getAttribute(ATTRIBUTE_NAME);
	}

	//
	// START-Action
	public boolean isNoOp() {
		return false;
	}

	// END-Action
	//

	@Override
	public String describeType() {
		return aa_i_Action.class.getSimpleName();
	}
}