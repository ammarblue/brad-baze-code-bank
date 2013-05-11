package com.software.reuze;

//package aima.core.agent;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): Figure 2.1, page 35.<br>
 * 
 * Figure 2.1 Agents interact with environments through sensors and actuators.
 * 
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 */
public interface aa_i_Agent extends aa_i_EnvironmentObject {
	/**
	 * Call the Agent's program, which maps any given percept sequences to an
	 * action.
	 * 
	 * @param percept
	 *            The current percept of a sequence perceived by the Agent.
	 * @return the Action to be taken in response to the currently perceived
	 *         percept.
	 */
	aa_i_Action execute(aa_i_Percept percept);

	/**
	 * Life-cycle indicator as to the liveness of an Agent.
	 * 
	 * @return true if the Agent is to be considered alive, false otherwise.
	 */
	boolean isAlive();

	/**
	 * Set the current liveness of the Agent.
	 * 
	 * @param alive
	 *            set to true if the Agent is to be considered alive, false
	 *            otherwise.
	 */
	void setAlive(boolean alive);
}
