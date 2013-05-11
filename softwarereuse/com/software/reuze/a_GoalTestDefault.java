package com.software.reuze;

/**
 * Checks whether a given state equals an explicitly specified goal state.
 * 
 * @author Ruediger Lunde
 */
public class a_GoalTestDefault implements a_i_GoalTest {
	private Object goalState;

	public a_GoalTestDefault(Object goalState) {
		this.goalState = goalState;
	}

	public boolean isGoalState(Object state) {
		return goalState.equals(state);
	}
}
