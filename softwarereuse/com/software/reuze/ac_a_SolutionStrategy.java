package com.software.reuze;

import java.util.ArrayList;
import java.util.List;

/**
 * Base class for CSP solver implementations. Solving a CSP means finding an
 * assignment, which is consistent and complete with respect to a CSP. This
 * abstract class provides the central interface method and additionally an
 * implementation of an observer mechanism.
 * 
 * @author Ruediger Lunde
 * @author Mike Stampone
 */
public abstract class ac_a_SolutionStrategy {
	List<ac_i_StateListener> listeners = new ArrayList<ac_i_StateListener>();

	/**
	 * Adds a CSP state listener to the solution strategy.
	 * 
	 * @param listener
	 *            a listener which follows the progress of the solution strategy
	 *            step-by-step.
	 */
	public void addCSPStateListener(ac_i_StateListener listener) {
		listeners.add(listener);
	}

	/**
	 * Removes a CSP listener from the solution strategy.
	 * 
	 * @param listener
	 *            the listener to remove
	 */
	public void removeCSPStateListener(ac_i_StateListener listener) {
		listeners.remove(listener);
	}

	protected void fireStateChanged(ac_CSP csp) {
		for (ac_i_StateListener listener : listeners)
			listener.stateChanged(csp.copyDomains());
	}

	protected void fireStateChanged(ac_OpsAssignmentVariables assignment, ac_CSP csp) {
		for (ac_i_StateListener listener : listeners)
			listener.stateChanged(assignment.copy(), csp.copyDomains());
	}

	/**
	 * Returns a solution to the specified CSP, which specifies values for all
	 * the variables such that the constraints are satisfied.
	 * 
	 * @param csp
	 *            a CSP to solve
	 * 
	 * @return a solution to the specified CSP, which specifies values for all
	 *         the variables such that the constraints are satisfied.
	 */
	public abstract ac_OpsAssignmentVariables solve(ac_CSP csp);
}
