package com.software.reuze;

import com.software.reuze.a_GoalTestDefault;
import com.software.reuze.a_Problem;
import com.software.reuze.apm_i_ProblemBidirectional;

/**
 * @author Ciaran O'Reilly
 * 
 */
public class apm_ProblemBidirectional extends a_Problem implements
		apm_i_ProblemBidirectional {

	d_i_Map map;

	a_Problem reverseProblem;

	public apm_ProblemBidirectional(d_i_Map map, String initialState,
			String goalState) {
		super(initialState, aa_ActionsFunctionMap.getActionsFunction(map),
				aa_ActionsFunctionMap.getResultFunction(), new a_GoalTestDefault(
						goalState), new aa_CostFunctionMapStep(map));

		this.map = map;

		reverseProblem = new a_Problem(goalState,
				aa_ActionsFunctionMap.getActionsFunction(map),
				aa_ActionsFunctionMap.getResultFunction(), new a_GoalTestDefault(
						initialState), new aa_CostFunctionMapStep(map));
	}

	//
	// START Interface BidrectionalProblem
	public a_Problem getOriginalProblem() {
		return this;
	}

	public a_Problem getReverseProblem() {
		return reverseProblem;
	}
	// END Interface BirectionalProblem
	//
}
