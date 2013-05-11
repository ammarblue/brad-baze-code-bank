package com.software.reuze;

import com.software.reuze.aa_i_Action;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 68.<br>
 * <br>
 * The <b>step cost</b> of taking action a in state s to reach state s' is
 * denoted by c(s, a, s').
 * 
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 */
public interface aa_i_CostFunctionStep {
	/**
	 * Calculate the step cost of taking action a in state s to reach state s'.
	 * 
	 * @param s
	 *            the state from which action a is to be performed.
	 * @param a
	 *            the action to be taken.
	 * 
	 * @param sDelta
	 *            the state reached by taking the action.
	 * @return the cost of taking action a in state s to reach state s'.
	 */
	double c(Object s, aa_i_Action a, Object sDelta);
}