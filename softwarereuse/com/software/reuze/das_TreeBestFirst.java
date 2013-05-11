package com.software.reuze;

import java.util.Comparator;

import com.software.reuze.aa_TreeSearchNode;
import com.software.reuze.aa_TreeSearchNodeExpanderQueue;
import com.software.reuze.das_SearchProblemPriority;
import com.software.reuze.das_i_TreeSearchEvaluationFunction;


/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 92.<br>
 * <br>
 * Best-first search is an instance of the general TREE-SEARCH or GRAPH-SEARCH
 * algorithm in which a node is selected for expansion based on an evaluation
 * function, f(n). The evaluation function is construed as a cost estimate, so
 * the node with the lowest evaluation is expanded first. The implementation of
 * best-first graph search is identical to that for uniform-cost search (Figure
 * 3.14), except for the use of f instead of g to order the priority queue.
 * 
 * @author Ciaran O'Reilly
 * @author Mike Stampone
 * @author Ruediger Lunde
 */
public class das_TreeBestFirst extends das_SearchProblemPriority {

	/**
	 * Constructs a best first search from a specified search problem and
	 * evaluation function.
	 * 
	 * @param search
	 *            a search problem
	 * @param ef
	 *            an evaluation function, which returns a number purporting to
	 *            describe the desirability (or lack thereof) of expanding a
	 *            node
	 */
	public das_TreeBestFirst(aa_TreeSearchNodeExpanderQueue search, das_i_TreeSearchEvaluationFunction ef) {
		super(search, createComparator(ef));
	}

	private static Comparator<aa_TreeSearchNode> createComparator(final das_i_TreeSearchEvaluationFunction ef) {
		return new Comparator<aa_TreeSearchNode>() {
			public int compare(aa_TreeSearchNode n1, aa_TreeSearchNode n2) {
				Double f1 = ef.f(n1);
				Double f2 = ef.f(n2);
				return f1.compareTo(f2);
			}
		};
	}
}
