package com.software.reuze;

import com.software.reuze.d_Metrics;

/**
 * Artificial Intelligence A Modern Approach (3rd Ed.): Page 173.<br>
 * 
 * <pre>
 * <code>
 * function ALPHA-BETA-SEARCH(state) returns an action
 *   v = MAX-VALUE(state, -infinity, +infinity)
 *   return the action in ACTIONS(state) with value v
 *   
 * function MAX-VALUE(state, alpha, beta) returns a utility value
 *   if TERMINAL-TEST(state) then return UTILITY(state)
 *   v = -infinity
 *   for each a in ACTIONS(state) do
 *     v = MAX(v, MIN-VALUE(RESULT(s, a), alpha, beta))
 *     if v >= beta then return v
 *     alpha = MAX(alpha, v)
 *   return v
 *   
 * function MIN-VALUE(state, alpha, beta) returns a utility value
 *   if TERMINAL-TEST(state) then return UTILITY(state)
 *   v = infinity
 *   for each a in ACTIONS(state) do
 *     v = MIN(v, MAX-VALUE(RESULT(s,a), alpha, beta))
 *     if v <= alpha then return v
 *     beta = MIN(beta, v)
 *   return v
 * </code>
 * </pre>
 * !!!!RPC if UTILITY value is not normalized to 1.0 for a win, delete test in makeDecision()
 * Figure 5.7 The alpha-beta search algorithm. Notice that these routines are
 * the same as the MINIMAX functions in Figure 5.3, except for the two lines in
 * each of MIN-VALUE and MAX-VALUE that maintain alpha and beta (and the
 * bookkeeping to pass these parameters along).
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
public class dasg_SearchAlphaBeta<STATE extends Iterable<ACTION>, ACTION, PLAYER> implements
		dasg_i_SearchAdversarial<STATE, ACTION> {

	dg_i_Game<STATE, ACTION, PLAYER> game;
	private int expandedNodes;

	/** Creates a new search object for a given game. */
	public static <STATE extends Iterable<ACTION>, ACTION, PLAYER> dasg_SearchAlphaBeta<STATE, ACTION, PLAYER> createFor(
			dg_i_Game<STATE, ACTION, PLAYER> game) {
		return new dasg_SearchAlphaBeta<STATE, ACTION, PLAYER>(game);
	}

	public dasg_SearchAlphaBeta(dg_i_Game<STATE, ACTION, PLAYER> game) {
		this.game = game;
	}

	public ACTION makeDecision(STATE state) {
		expandedNodes = 0;
		ACTION result = null;
		double resultValue = Double.NEGATIVE_INFINITY;
		PLAYER player = game.getPlayer(state);
		for (ACTION action : state) {
			double value = minValue(game.getResult(state, action), player,
					Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY); //System.out.println(state+" "+action+" "+value);
			if (value > resultValue) {
				result = action;
				resultValue = value;
				if (value>=1.0) 
					break;  //RPC0912 assume 1 is guaranteed win, can be deleted, added for efficiency
			}
		}
		return result;
	}

	public double maxValue(STATE state, PLAYER player, double alpha, double beta) {
		expandedNodes++; //System.out.println(state+" "+player+" A="+alpha+" B="+beta);
		if (game.isTerminal(state))
			return game.getUtility(state, player);
		double value = Double.NEGATIVE_INFINITY;
		for (ACTION action : state) {
			value = Math.max(value, minValue( //
					game.getResult(state, action), player, alpha, beta));
			if (value >= beta)
				return value;
			alpha = Math.max(alpha, value);
		}
		return value;
	}

	public double minValue(STATE state, PLAYER player, double alpha, double beta) {
		expandedNodes++;
		if (game.isTerminal(state))
			return game.getUtility(state, player);
		double value = Double.POSITIVE_INFINITY;
		for (ACTION action : state) {
			value = Math.min(value, maxValue( //
					game.getResult(state, action), player, alpha, beta));
			if (value <= alpha)
				return value;
			beta = Math.min(beta, value);
		}
		return value;
	}

	public d_Metrics getMetrics() {
		d_Metrics result = new d_Metrics();
		result.set("expandedNodes", expandedNodes);
		return result;
	}
}
