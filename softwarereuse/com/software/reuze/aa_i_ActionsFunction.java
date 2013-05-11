package com.software.reuze;
//package aima.core.probability.mdp;

import java.util.Set;

//import aima.core.agent.Action;

/**
 * An interface for MDP action functions.
 * 
 * @param <S>
 *            the state type.
 * @param <A>
 *            the action type.
 * 
 * @author Ciaran O'Reilly
 * @author Ravi Mohan
 */
public interface aa_i_ActionsFunction<S, A extends aa_i_Action> {
	/**
	 * Get the set of actions for state s.
	 * 
	 * @param s
	 *            the state.
	 * @return the set of actions for state s.
	 */
	Set<A> actions(S s);
}
