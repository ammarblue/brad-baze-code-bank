package com.software.reuze;

import java.util.Collections;
import java.util.List;

import com.software.reuze.aa_i_Action;
import com.software.reuze.d_Queue;
import com.software.reuze.lc_ThreadCancelable;


/**
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 * @author Mike Stampone
 */
public abstract class aa_TreeSearchNodeExpanderQueue extends aa_TreeSearchNodeExpander {
	public static final String METRIC_QUEUE_SIZE = "queueSize";

	public static final String METRIC_MAX_QUEUE_SIZE = "maxQueueSize";

	public static final String METRIC_PATH_COST = "pathCost";

	//
	//
	private d_i_Queue<aa_TreeSearchNode> frontier = null;
	private boolean checkGoalBeforeAddingToFrontier = false;

	public boolean isFailure(List<aa_i_Action> result) {
		return 0 == result.size();
	}

	/**
	 * Returns a list of actions to the goal if the goal was found, a list
	 * containing a single NoOp Action if already at the goal, or an empty list
	 * if the goal could not be found.
	 * 
	 * @param problem
	 *            the search problem
	 * @param frontier
	 *            the collection of nodes that are waiting to be expanded
	 * 
	 * @return a list of actions to the goal if the goal was found, a list
	 *         containing a single NoOp Action if already at the goal, or an
	 *         empty list if the goal could not be found.
	 */
	public List<aa_i_Action> search(a_Problem problem, d_i_Queue<aa_TreeSearchNode> frontier) {
		this.frontier = frontier;

		clearInstrumentation();
		// initialize the frontier using the initial state of the problem
		aa_TreeSearchNode root = new aa_TreeSearchNode(problem.getInitialState());
		if (isCheckGoalBeforeAddingToFrontier()) {
			if (aa_SearchUtils.isGoalState(problem, root)) {
				return aa_SearchUtils.actionsFromNodes(root.getPathFromRoot());
			}
		}
		frontier.insert(root);
		setQueueSize(frontier.size());
		while (!(frontier.isEmpty()) && !lc_ThreadCancelable.currIsCanceled()) {
			// choose a leaf node and remove it from the frontier
			aa_TreeSearchNode nodeToExpand = popNodeFromFrontier();
			setQueueSize(frontier.size());
			// Only need to check the nodeToExpand if have not already
			// checked before adding to the frontier
			if (!isCheckGoalBeforeAddingToFrontier()) {
				// if the node contains a goal state then return the
				// corresponding solution
				if (aa_SearchUtils.isGoalState(problem, nodeToExpand)) {
					setPathCost(nodeToExpand.getPathCost());
					return aa_SearchUtils.actionsFromNodes(nodeToExpand
							.getPathFromRoot());
				}
			}
			// expand the chosen node, adding the resulting nodes to the
			// frontier
			for (aa_TreeSearchNode fn : getResultingNodesToAddToFrontier(nodeToExpand,
					problem)) {
				if (isCheckGoalBeforeAddingToFrontier()) {
					if (aa_SearchUtils.isGoalState(problem, fn)) {
						setPathCost(fn.getPathCost());
						return aa_SearchUtils.actionsFromNodes(fn
								.getPathFromRoot());
					}
				}
				frontier.insert(fn);
			}
			setQueueSize(frontier.size());
		}
		// if the frontier is empty then return failure
		return failure();
	}

	public boolean isCheckGoalBeforeAddingToFrontier() {
		return checkGoalBeforeAddingToFrontier;
	}

	public void setCheckGoalBeforeAddingToFrontier(
			boolean checkGoalBeforeAddingToFrontier) {
		this.checkGoalBeforeAddingToFrontier = checkGoalBeforeAddingToFrontier;
	}

	/**
	 * Removes and returns the node at the head of the frontier.
	 * 
	 * @return the node at the head of the frontier.
	 */
	public aa_TreeSearchNode popNodeFromFrontier() {
		return frontier.pop();
	}

	public boolean removeNodeFromFrontier(aa_TreeSearchNode toRemove) {
		return frontier.remove(toRemove);
	}

	public abstract List<aa_TreeSearchNode> getResultingNodesToAddToFrontier(
			aa_TreeSearchNode nodeToExpand, a_Problem p);

	@Override
	public void clearInstrumentation() {
		super.clearInstrumentation();
		metrics.set(METRIC_QUEUE_SIZE, 0);
		metrics.set(METRIC_MAX_QUEUE_SIZE, 0);
		metrics.set(METRIC_PATH_COST, 0);
	}

	public int getQueueSize() {
		return metrics.getInt("queueSize");
	}

	public void setQueueSize(int queueSize) {

		metrics.set(METRIC_QUEUE_SIZE, queueSize);
		int maxQSize = metrics.getInt(METRIC_MAX_QUEUE_SIZE);
		if (queueSize > maxQSize) {
			metrics.set(METRIC_MAX_QUEUE_SIZE, queueSize);
		}
	}

	public int getMaxQueueSize() {
		return metrics.getInt(METRIC_MAX_QUEUE_SIZE);
	}

	public double getPathCost() {
		return metrics.getDouble(METRIC_PATH_COST);
	}

	public void setPathCost(Double pathCost) {
		metrics.set(METRIC_PATH_COST, pathCost);
	}

	//
	// PRIVATE METHODS
	//
	private List<aa_i_Action> failure() {
		return Collections.emptyList();
	}
}