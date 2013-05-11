package com.software.reuze;

import java.util.ArrayList;
import java.util.List;

import com.software.reuze.a_Problem;
import com.software.reuze.aa_SearchUtils;
import com.software.reuze.aa_TreeSearchNode;
import com.software.reuze.aa_TreeSearchNodeExpander;
import com.software.reuze.aa_i_Action;
import com.software.reuze.das_i_SearchProblem;
import com.software.reuze.lc_ThreadCancelable;
import com.software.reuze.m_i_HeuristicFunction;


/**
 * Artificial Intelligence A Modern Approach (3rd Edition): Figure 4.2, page
 * 122.<br>
 * <br>
 * 
 * <pre>
 * function HILL-CLIMBING(problem) returns a state that is a local maximum
 *                    
 *   current &lt;- MAKE-NODE(problem.INITIAL-STATE)
 *   loop do
 *     neighbor &lt;- a highest-valued successor of current
 *     if neighbor.VALUE &lt;= current.VALUE then return current.STATE
 *     current &lt;- neighbor
 * </pre>
 * 
 * Figure 4.2 The hill-climbing search algorithm, which is the most basic local
 * search technique. At each step the current node is replaced by the best
 * neighbor; in this version, that means the neighbor with the highest VALUE,
 * but if a heuristic cost estimate h is used, we would find the neighbor with
 * the lowest h.
 * 
 * @author Ravi Mohan
 * @author Mike Stampone
 */
public class das_SearchProblemHillClimbing extends aa_TreeSearchNodeExpander implements das_i_SearchProblem {

	public enum SearchOutcome {
		FAILURE, SOLUTION_FOUND
	};

	private m_i_HeuristicFunction hf = null;

	private SearchOutcome outcome = SearchOutcome.FAILURE;

	private Object lastState = null;

	/**
	 * Constructs a hill-climbing search from the specified heuristic function.
	 * 
	 * @param hf
	 *            a heuristic function
	 */
	public das_SearchProblemHillClimbing(m_i_HeuristicFunction hf) {
		this.hf = hf;
	}

	/**
	 * Returns a list of actions to the local maximum if the local maximum was
	 * found, a list containing a single NoOp Action if already at the local
	 * maximum, or an empty list if the search was canceled by the user.
	 * 
	 * @param p
	 *            the search problem
	 * 
	 * @return a list of actions to the local maximum if the local maximum was
	 *         found, a list containing a single NoOp Action if already at the
	 *         local maximum, or an empty list if the search was canceled by the
	 *         user.
	 */
	// function HILL-CLIMBING(problem) returns a state that is a local maximum
	public List<aa_i_Action> search(a_Problem p) throws Exception {
		clearInstrumentation();
		outcome = SearchOutcome.FAILURE;
		lastState = null;
		// current <- MAKE-NODE(problem.INITIAL-STATE)
		aa_TreeSearchNode current = new aa_TreeSearchNode(p.getInitialState());
		aa_TreeSearchNode neighbor = null;
		// loop do
		while (!lc_ThreadCancelable.currIsCanceled()) {
			List<aa_TreeSearchNode> children = expandNode(current, p);
			// neighbor <- a highest-valued successor of current
			neighbor = getHighestValuedNodeFrom(children, p);

			// if neighbor.VALUE <= current.VALUE then return current.STATE
			if ((neighbor == null) || (getValue(neighbor) <= getValue(current))) {
				if (aa_SearchUtils.isGoalState(p, current)) {
					outcome = SearchOutcome.SOLUTION_FOUND;
				}
				lastState = current.getState();
				return aa_SearchUtils.actionsFromNodes(current.getPathFromRoot());
			}
			// current <- neighbor
			current = neighbor;
		}
		return new ArrayList<aa_i_Action>();
	}

	/**
	 * Returns SOLUTION_FOUND if the local maximum is a goal state, or FAILURE
	 * if the local maximum is not a goal state.
	 * 
	 * @return SOLUTION_FOUND if the local maximum is a goal state, or FAILURE
	 *         if the local maximum is not a goal state.
	 */
	public SearchOutcome getOutcome() {
		return outcome;
	}

	/**
	 * Returns the last state from which the hill climbing search found the
	 * local maximum.
	 * 
	 * @return the last state from which the hill climbing search found the
	 *         local maximum.
	 */
	public Object getLastSearchState() {
		return lastState;
	}

	//
	// PRIVATE METHODS
	//

	private aa_TreeSearchNode getHighestValuedNodeFrom(List<aa_TreeSearchNode> children, a_Problem p) {
		double highestValue = Double.NEGATIVE_INFINITY;
		aa_TreeSearchNode nodeWithHighestValue = null;
		for (int i = 0; i < children.size(); i++) {
			aa_TreeSearchNode child = (aa_TreeSearchNode) children.get(i);
			double value = getValue(child);
			if (value > highestValue) {
				highestValue = value;
				nodeWithHighestValue = child;
			}
		}
		return nodeWithHighestValue;
	}

	private double getValue(aa_TreeSearchNode n) {
		// assumption greater heuristic value =>
		// HIGHER on hill; 0 == goal state;
		return -1 * hf.h(n.getState());
	}
}