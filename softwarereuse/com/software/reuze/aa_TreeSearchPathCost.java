package com.software.reuze;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 78.<br>
 * <br>
 * 
 * @author Ciaran O'Reilly
 * 
 */
public class aa_TreeSearchPathCost {
	public aa_TreeSearchPathCost() {
	}

	/**
	 * 
	 * @param n
	 * @return the cost, traditionally denoted by g(n), of the path from the
	 *         initial state to the node, as indicated by the parent pointers.
	 */
	public double g(aa_TreeSearchNode n) {
		return n.getPathCost();
	}
}
