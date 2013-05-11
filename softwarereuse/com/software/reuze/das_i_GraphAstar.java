package com.software.reuze;

import com.software.reuze.dag_GraphNode;

/**
 * Interface for all A* heuristic classes
 * 
 * @see		das_GraphAstarCrowFlight
 * @see		das_GraphAstarManhattan
 * 
 * @author Peter Lager
 */
public interface das_i_GraphAstar {

	/**
	 * Estimate the cost between the node and the target.
	 */
	public double getCost(dag_GraphNode node, dag_GraphNode target);
	
}
