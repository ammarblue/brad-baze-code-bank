package com.software.reuze;

import com.software.reuze.aa_TreeSearchNodeExpanderQueue;
import com.software.reuze.m_i_HeuristicFunction;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 92.<br>
 * <br>
 * Greedy best-first search tries to expand the node that is closest to the
 * goal, on the grounds that this is likely to lead to a solution quickly. Thus,
 * it evaluates nodes by using just the heuristic function; that is, f(n) = h(n)
 * 
 * @author Ravi Mohan
 * @author Mike Stampone
 */
public class das_TreeBestFirstGreedy extends das_TreeBestFirst {

	/**
	 * Constructs a greedy best-first search from the specified search problem
	 * and heuristic function.
	 * 
	 * @param search
	 *            a search problem
	 * @param hf
	 *            a heuristic function <em>h(n)</em>, which estimates the
	 *            cheapest path from the state at node <em>n</em> to a goal
	 *            state.
	 */
	public das_TreeBestFirstGreedy(aa_TreeSearchNodeExpanderQueue search, m_i_HeuristicFunction hf) {
		super(search, new das_TreeSearchEvaluationGreedyBestFirst(hf));
	}
}