package com.software.reuze;

import com.software.reuze.d_Metrics;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 169.<br>
 * 
 * <pre>
 * <code>
 * function MINIMAX-DECISION(state) returns an action
 *   return argmax_[a in ACTIONS(s)] MIN-VALUE(RESULT(state, a))
 * 
 * function MAX-VALUE(state) returns a utility value
 *   if TERMINAL-TEST(state) then return UTILITY(state)
 *   v = -infinity
 *   for each a in ACTIONS(state) do
 *     v = MAX(v, MIN-VALUE(RESULT(s, a)))
 *   return v
 * 
 * function MIN-VALUE(state) returns a utility value
 *   if TERMINAL-TEST(state) then return UTILITY(state)
 *     v = infinity
 *     for each a in ACTIONS(state) do
 *       v  = MIN(v, MAX-VALUE(RESULT(s, a)))
 *   return v
 * </code>
 * </pre>
 * 
 * Figure 5.3 An algorithm for calculating minimax decisions. It returns the
 * action corresponding to the best possible move, that is, the move that leads
 * to the outcome with the best utility, under the assumption that the opponent
 * plays to minimize utility. The functions MAX-VALUE and MIN-VALUE go through
 * the whole game tree, all the way to the leaves, to determine the backed-up
 * value of a state. The notation argmax_[a in S] f(a) computes the element a of
 * set S that has the maximum value of f(a).
 * 
 * 
 * @author Ruediger Lunde
 * 
 * @param <STATE>
 *            Type which is used for states in the game.
 * @param <ACTION>
 *            Type which is used for actions in the game.
 * @param <PLAYER>
 *            Type which is used for players in the game.
 */
public class dasg_SearchAdversarialMinimax<STATE extends Iterable<ACTION>, ACTION, PLAYER> implements
		dasg_i_SearchAdversarial<STATE, ACTION> {

	private dg_i_Game<STATE, ACTION, PLAYER> game;
	private int expandedNodes;

	/** Creates a new search object for a given game. */
	public static <STATE extends Iterable<ACTION>, ACTION, PLAYER> dasg_SearchAdversarialMinimax<STATE, ACTION, PLAYER> createFor(
			dg_i_Game<STATE, ACTION, PLAYER> game) {
		return new dasg_SearchAdversarialMinimax<STATE, ACTION, PLAYER>(game);
	}

	public dasg_SearchAdversarialMinimax(dg_i_Game<STATE, ACTION, PLAYER> game) {
		this.game = game;
	}

	public ACTION makeDecision(STATE state) {
		expandedNodes = 0;
		ACTION result = null;
		double resultValue = Double.NEGATIVE_INFINITY;
		PLAYER player = game.getPlayer(state);
		for (ACTION action : state) {
			double value = minValue(game.getResult(state, action), player);
			if (value > resultValue) {
				result = action;
				resultValue = value;
			}
		}
		return result;
	}

	public double maxValue(STATE state, PLAYER player) { // returns an utility
															// value
		expandedNodes++;
		if (game.isTerminal(state))
			return game.getUtility(state, player);
		double value = Double.NEGATIVE_INFINITY;
		for (ACTION action : state)
			value = Math.max(value,
					minValue(game.getResult(state, action), player));
		return value;
	}

	public double minValue(STATE state, PLAYER player) { // returns an utility
															// value
		expandedNodes++;
		if (game.isTerminal(state))
			return game.getUtility(state, player);
		double value = Double.POSITIVE_INFINITY;
		for (ACTION action : state)
			value = Math.min(value,
					maxValue(game.getResult(state, action), player));
		return value;
	}

	public d_Metrics getMetrics() {
		d_Metrics result = new d_Metrics();
		result.set("expandedNodes", expandedNodes);
		return result;
	}
}
