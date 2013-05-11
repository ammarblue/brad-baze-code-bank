package com.software.reuze;

import java.util.List;

import com.software.reuze.aa_i_Action;


/**
 * @author Ravi Mohan
 * @author Mike Stampone
 */
public interface das_i_SearchProblem {

	/**
	 * Returns a list of actions to the goal if the goal was found, a list
	 * containing a single NoOp Action if already at the goal, or an empty list
	 * if the goal could not be found.
	 * 
	 * @param p
	 *            the search problem
	 * 
	 * @return a list of actions to the goal if the goal was found, a list
	 *         containing a single NoOp Action if already at the goal, or an
	 *         empty list if the goal could not be found.
	 */
	List<aa_i_Action> search(a_Problem p) throws Exception;

	/**
	 * Returns all the metrics of the search.
	 * 
	 * @return all the metrics of the search.
	 */
	d_Metrics getMetrics();
}