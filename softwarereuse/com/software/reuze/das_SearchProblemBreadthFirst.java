package com.software.reuze;

import java.util.List;

import com.software.reuze.a_Problem;
import com.software.reuze.aa_TreeSearchNodeExpanderQueue;
import com.software.reuze.aa_i_Action;
import com.software.reuze.d_Metrics;
import com.software.reuze.d_QueueFIFO;
import com.software.reuze.dag_GraphSearch;
import com.software.reuze.das_i_SearchProblem;
import com.software.reuze.mpb_i_Node;


/**
 * Artificial Intelligence A Modern Approach (3rd Edition): Figure 3.11, page
 * 82.<br>
 * <br>
 * 
 * <pre>
 * function BREADTH-FIRST-SEARCH(problem) returns a solution, or failure
 *   node &lt;- a node with STATE = problem.INITIAL-STATE, PATH-COST=0
 *   if problem.GOAL-TEST(node.STATE) then return SOLUTION(node)
 *   frontier &lt;- a FIFO queue with node as the only element
 *   explored &lt;- an empty set
 *   loop do
 *      if EMPTY?(frontier) then return failure
 *      node &lt;- POP(frontier) // chooses the shallowest node in frontier
 *      add node.STATE to explored
 *      for each action in problem.ACTIONS(node.STATE) do
 *          child &lt;- CHILD-NODE(problem, node, action)
 *          if child.STATE is not in explored or frontier then
 *              if problem.GOAL-TEST(child.STATE) then return SOLUTION(child)
 *              frontier &lt;- INSERT(child, frontier)
 * </pre>
 * 
 * Figure 3.11 Breadth-first search on a graph.<br>
 * <br>
 * <b>Note:</b> Supports both Tree and Graph based versions by assigning an
 * instance of TreeSearch or GraphSearch to its constructor.
 * 
 * @author Ciaran O'Reilly
 */
public class das_SearchProblemBreadthFirst implements das_i_SearchProblem {

	private final aa_TreeSearchNodeExpanderQueue search;

	public das_SearchProblemBreadthFirst() {
		this(new dag_GraphSearch());
	}

	public das_SearchProblemBreadthFirst(aa_TreeSearchNodeExpanderQueue search) {
		// Goal test is to be applied to each node when it is generated
		// rather than when it is selected for expansion.
		search.setCheckGoalBeforeAddingToFrontier(true);
		this.search = search;
	}

	public List<aa_i_Action> search(a_Problem p) {
		return search.search(p, new d_QueueFIFO<aa_TreeSearchNode>());
	}

	public d_Metrics getMetrics() {
		return search.getMetrics();
	}
}