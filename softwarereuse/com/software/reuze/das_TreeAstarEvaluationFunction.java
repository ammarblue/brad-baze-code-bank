package com.software.reuze;

import com.software.reuze.aa_TreeSearchNode;
import com.software.reuze.aa_TreeSearchPathCost;
import com.software.reuze.das_i_TreeSearchEvaluationFunction;
import com.software.reuze.m_i_HeuristicFunction;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 93.<br>
 * <br>
 * The most widely known form of best-first search is called A* search
 * (pronounced "A-star Search"). It evaluates nodes by combining g(n), the cost
 * to reach the node, and h(n), the cost to get from the node to the goal:<br>
 * 
 * <pre>
 *        f(n) = g(n) + h(n).
 * </pre>
 * 
 * @author Ciaran O'Reilly
 * @author Mike Stampone
 */
public class das_TreeAstarEvaluationFunction implements das_i_TreeSearchEvaluationFunction {

	private aa_TreeSearchPathCost gf = new aa_TreeSearchPathCost();
	private m_i_HeuristicFunction hf = null;

	public das_TreeAstarEvaluationFunction(m_i_HeuristicFunction hf) {
		this.hf = hf;
	}

	/**
	 * Returns <em>g(n)</em> the cost to reach the node, plus <em>h(n)</em> the
	 * heuristic cost to get from the specified node to the goal.
	 * 
	 * @param n
	 *            a node
	 * @return g(n) + h(n)
	 */
	public double f(aa_TreeSearchNode n) {
		// f(n) = g(n) + h(n)
		return gf.g(n) + hf.h(n.getState());
	}
}
