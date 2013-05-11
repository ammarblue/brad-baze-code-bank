package com.software.reuze;

import com.software.reuze.aa_i_State;

/**
 * @author Ciaran O'Reilly
 */
public class aa_StateWithDynamicAttributes extends d_a_ObjectWithDynamicAttributes implements aa_i_State {
	public aa_StateWithDynamicAttributes() {

	}

	@Override
	public String describeType() {
		return aa_i_State.class.getSimpleName();
	}
}