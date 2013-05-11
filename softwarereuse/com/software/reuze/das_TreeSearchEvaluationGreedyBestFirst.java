package com.software.reuze;

import com.software.reuze.aa_TreeSearchNode;
import com.software.reuze.das_i_TreeSearchEvaluationFunction;
import com.software.reuze.m_i_HeuristicFunction;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 92.<br>
 * <br>
 * Greedy best-first search tries to expand the node that is closest to the
 * goal, on the grounds that this is likely to lead to a solution quickly. Thus,
 * it evaluates nodes by using just the heuristic function; that is, f(n) = h(n)
 * 
 * @author Ciaran O'Reilly
 * 
 */
public class das_TreeSearchEvaluationGreedyBestFirst implements das_i_TreeSearchEvaluationFunction {

	private m_i_HeuristicFunction hf = null;

	public das_TreeSearchEvaluationGreedyBestFirst(m_i_HeuristicFunction hf) {
		this.hf = hf;
	}

	public double f(aa_TreeSearchNode n) {
		// f(n) = h(n)
		return hf.h(n.getState());
	}
}
