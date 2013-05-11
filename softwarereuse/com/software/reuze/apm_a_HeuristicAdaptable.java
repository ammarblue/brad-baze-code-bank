package com.software.reuze;

import com.software.reuze.m_i_HeuristicFunction;

/**
 * This class extends heuristic functions in two ways: It maintains a goal and a
 * map to estimate distance to goal for states in route planning problems, and
 * it provides a method to adapt to different goals.
 * 
 * @author Ruediger Lunde
 */
public abstract class apm_a_HeuristicAdaptable implements m_i_HeuristicFunction,
		Cloneable {
	/** The Current Goal. */
	protected Object goal;
	/** The map to be used for distance to goal estimates. */
	protected d_i_Map map;

	/**
	 * Modifies goal and map information and returns the modified heuristic
	 * function.
	 */
	public apm_a_HeuristicAdaptable adaptToGoal(Object goal, d_i_Map map) {
		this.goal = goal;
		this.map = map;
		return this;
	}

	// when sub-classing: Don't forget to implement the most important method
	// public double h(Object state)
}
