package com.software.reuze;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.software.reuze.aa_i_Action;
import com.software.reuze.d_Metrics;

/**
 * @author Ravi Mohan
 * @author Mike Stampone
 */
public class aa_TreeSearchNodeExpander {
	public static final String METRIC_NODES_EXPANDED = "nodesExpanded";

	protected d_Metrics metrics;

	public aa_TreeSearchNodeExpander() {
		metrics = new d_Metrics();
	}

	/**
	 * Sets the nodes expanded metric to zero.
	 */
	public void clearInstrumentation() {
		metrics.set(METRIC_NODES_EXPANDED, 0);
	}

	/**
	 * Returns the number of nodes expanded so far.
	 * 
	 * @return the number of nodes expanded so far.
	 */
	public int getNodesExpanded() {
		return metrics.getInt(METRIC_NODES_EXPANDED);
	}

	/**
	 * Returns all the metrics of the node expander.
	 * 
	 * @return all the metrics of the node expander.
	 */
	public d_Metrics getMetrics() {
		return metrics;
	}

	/**
	 * Returns the children obtained from expanding the specified node in the
	 * specified problem.
	 * 
	 * @param node
	 *            the node to expand
	 * @param problem
	 *            the problem the specified node is within.
	 * 
	 * @return the children obtained from expanding the specified node in the
	 *         specified problem.
	 */
	public List<aa_TreeSearchNode> expandNode(aa_TreeSearchNode node, a_Problem problem) {
		List<aa_TreeSearchNode> childNodes = new ArrayList<aa_TreeSearchNode>();

		aa_i_ActionsFunction actionsFunction = problem.getActionsFunction();
		aa_i_ActionResultFunction resultFunction = problem.getResultFunction();
		aa_i_CostFunctionStep stepCostFunction = problem.getStepCostFunction();

		for (aa_i_Action action : (Set<aa_i_Action>)actionsFunction.actions(node.getState())) {
			Object successorState = resultFunction.result(node.getState(),
					action);

			double stepCost = stepCostFunction.c(node.getState(), action,
					successorState);
			childNodes.add(new aa_TreeSearchNode(successorState, node, action, stepCost));
		}
		metrics.set(METRIC_NODES_EXPANDED,
				metrics.getInt(METRIC_NODES_EXPANDED) + 1);

		return childNodes;
	}
}