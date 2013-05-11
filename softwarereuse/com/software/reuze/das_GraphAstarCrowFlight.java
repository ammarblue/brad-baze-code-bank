package com.software.reuze;

import com.software.reuze.dag_GraphNode;

/**
 * This class is used to calculate the heuristic estimated-cost-to-goal. <br>
 * 
 * It estimates the cost to goal as the Euclidean (as the crow flies) distance 
 * between the current node and the goal. <br>
 * 
 * It is also possible to apply a scaling factor to the heuristic. <br>
 * 
 * @author Peter Lager
 *
 */
public class das_GraphAstarCrowFlight implements das_i_GraphAstar {

	private double factor = 1.0;

	/**
	 * Will use a factor of 1.0 to calculate the estimated cost 
	 * between nodes
	 */
	public das_GraphAstarCrowFlight() {
		factor = 1.0;
	}

	/**
	 * Create the heuristic.
	 * @param factor scaling factor
	 */
	public das_GraphAstarCrowFlight(double factor) {
		this.factor = factor;
	}

	/**
	 * Estimate the cost between the node and the target.
	 */
	public double getCost(dag_GraphNode node, dag_GraphNode target) {
		double dx = target.x - node.x;
		double dy = target.y - node.y;
		double dz = target.z - node.z;
		
		return factor * Math.sqrt(dx*dx + dy*dy + dz*dz);
	}

}
