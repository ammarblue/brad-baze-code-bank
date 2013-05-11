package com.software.reuze;
//package aima.core.probability.mdp.impl;

import java.util.Set;


/*import aima.core.agent.Action;
import aima.core.probability.mdp.ActionsFunction;
import aima.core.probability.mdp.MarkovDecisionProcess;
import aima.core.probability.mdp.RewardFunction;
import aima.core.probability.mdp.TransitionProbabilityFunction;*/

/**
 * Default implementation of the MarkovDecisionProcess<S, A> interface.
 * 
 * @param <S>
 *            the state type.
 * @param <A>
 *            the action type.
 * 
 * @author Ciaran O'Reilly
 * @author Ravi Mohan
 */
public class mp_MDP<S, A extends aa_i_Action> implements mp_i_MDP<S, A> {
	private Set<S> states = null;
	private S initialState = null;
	private aa_i_ActionsFunction<S, A> actionsFunction = null;
	private mp_i_MDPTransitionProbabilityFunction<S, A> transitionProbabilityFunction = null;
	private mp_i_MDPRewardFunction<S> rewardFunction = null;

	public mp_MDP(Set<S> states, S initialState,
			aa_i_ActionsFunction<S, A> actionsFunction,
			mp_i_MDPTransitionProbabilityFunction<S, A> transitionProbabilityFunction,
			mp_i_MDPRewardFunction<S> rewardFunction) {
		this.states = states;
		this.initialState = initialState;
		this.actionsFunction = actionsFunction;
		this.transitionProbabilityFunction = transitionProbabilityFunction;
		this.rewardFunction = rewardFunction;
	}

	//
	// START-MarkovDecisionProcess
	public Set<S> states() {
		return states;
	}

	public S getInitialState() {
		return initialState;
	}

	public Set<A> actions(S s) {
		return actionsFunction.actions(s);
	}

	public double transitionProbability(S sDelta, S s, A a) {
		return transitionProbabilityFunction.probability(sDelta, s, a);
	}

	public double reward(S s) {
		return rewardFunction.reward(s);
	}

	// END-MarkovDecisionProcess
	//
}
