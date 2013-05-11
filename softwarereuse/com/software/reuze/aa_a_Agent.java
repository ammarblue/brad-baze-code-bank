package com.software.reuze;

//package aima.core.agent.impl;

/*import aima.core.agent.Action;
import aima.core.agent.Agent;
import aima.core.agent.AgentProgram;
import aima.core.agent.Percept;*/

/**
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 * @author Mike Stampone
 */
public abstract class aa_a_Agent implements aa_i_Agent {

	protected aa_i_AgentProgram program;
	private boolean alive = true;

	public aa_a_Agent() {

	}

	/**
	 * Constructs an Agent with the specified AgentProgram.
	 * 
	 * @param aProgram
	 *            the Agent's program, which maps any given percept sequences to
	 *            an action.
	 */
	public aa_a_Agent(aa_i_AgentProgram aProgram) {
		program = aProgram;
	}

	//
	// START-Agent
	public aa_i_Action execute(aa_i_Percept p) {
		if (null != program) {
			return program.execute(p);
		}
		return aa_ActionDynamicNoOp.NO_OP;
	}

	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	// END-Agent
	//
}