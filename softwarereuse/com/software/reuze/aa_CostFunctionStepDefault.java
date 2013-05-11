package com.software.reuze;

import com.software.reuze.aa_i_Action;

/**
 * Returns one for every action.
 * 
 * @author Ravi Mohan
 */
public class aa_CostFunctionStepDefault implements aa_i_CostFunctionStep {

	public double c(Object stateFrom, aa_i_Action action, Object stateTo) {
		return 1;
	}
}