package com.software.reuze;

import com.software.reuze.aa_i_Action;
import com.software.reuze.aa_i_CostFunctionStep;

/**
 * Implementation of StepCostFunction interface that uses the distance between
 * locations to calculate the cost in addition to a constant cost, so that it
 * may be used in conjunction with a Uniform-cost search.
 * 
 * @author Ciaran O'Reilly
 * 
 */
public class aa_CostFunctionMapStep implements aa_i_CostFunctionStep {
	private d_i_Map map = null;

	//
	// Used by Uniform-cost search to ensure every step is greater than or equal
	// to some small positive constant
	private static double constantCost = 1.0;

	public aa_CostFunctionMapStep(d_i_Map map) {
		this.map = map;
	}

	//
	// START-StepCostFunction
	public double c(Object fromCurrentState, aa_i_Action action, Object toNextState) {

		String fromLoc = fromCurrentState.toString();
		String toLoc = toNextState.toString();

		Double distance = map.getDistance(fromLoc, toLoc);

		if (distance == null || distance <= 0) {
			return constantCost;
		}

		return new Double(distance);
	}

	// END-StepCostFunction
	//
}
