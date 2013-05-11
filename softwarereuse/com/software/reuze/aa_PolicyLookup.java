package com.software.reuze;
//package aima.core.probability.mdp.impl;

import java.util.HashMap;
import java.util.Map;


//import aima.core.agent.Action;
//import aima.core.probability.mdp.Policy;

/**
 * Default implementation of the Policy interface using an underlying Map to
 * look up an action associated with a state.
 * 
 * @param <S>
 *            the state type.
 * @param <A>
 *            the action type.
 * 
 * @author Ciaran O'Reilly
 */
public class aa_PolicyLookup<S, A extends aa_i_Action> implements aa_i_Policy<S, A> {
	private Map<S, A> policy = new HashMap<S, A>();

	public aa_PolicyLookup(Map<S, A> aPolicy) {
		policy.putAll(aPolicy);
	}

	//
	// START-Policy
	public A action(S s) {
		return policy.get(s);
	}

	// END-Policy
	//
}
