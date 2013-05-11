package com.software.reuze;

/**
 * Interface which allows interested clients to register at a solution strategy
 * and follow their progress step by step.
 * 
 * @author Ruediger Lunde
 */
public interface ac_i_StateListener {
	/** Informs about changed assignments. */
	void stateChanged(ac_OpsAssignmentVariables assignment, ac_CSP csp);

	/** Informs about changed domains (inferences). */
	void stateChanged(ac_CSP csp);
}
