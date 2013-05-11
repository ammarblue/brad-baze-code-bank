package com.software.reuze;

import java.util.List;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): Figure 3.7, page 77.<br>
 * <br>
 * 
 * <pre>
 * function TREE-SEARCH(problem) returns a solution, or failure
 *   initialize the frontier using the initial state of the problem
 *   loop do
 *     if the frontier is empty then return failure
 *     choose a leaf node and remove it from the frontier
 *     if the node contains a goal state then return the corresponding solution
 *     expand the chosen node, adding the resulting nodes to the frontier
 * </pre>
 * 
 * Figure 3.7 An informal description of the general tree-search algorithm.
 * 
 * @author Ravi Mohan
 * 
 */
public class aa_TreeSearch extends aa_TreeSearchNodeExpanderQueue {

	@Override
	public List<aa_TreeSearchNode> getResultingNodesToAddToFrontier(aa_TreeSearchNode nodeToExpand,
			a_Problem problem) {
		// expand the chosen node, adding the resulting nodes to the frontier
		return expandNode(nodeToExpand, problem);
	}
}