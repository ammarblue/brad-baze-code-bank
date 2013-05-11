package com.software.reuze;

import java.util.Collections;
import java.util.List;

import com.software.reuze.a_Problem;
import com.software.reuze.aa_TreeSearchNodeExpander;
import com.software.reuze.aa_i_Action;
import com.software.reuze.d_Metrics;
import com.software.reuze.das_i_SearchProblem;


/**
 * Artificial Intelligence A Modern Approach (3rd Edition): Figure 3.18, page
 * 89.<br>
 * <br>
 * 
 * <pre>
 * function ITERATIVE-DEEPENING-SEARCH(problem) returns a solution, or failure
 *   for depth = 0 to infinity  do
 *     result &lt;- DEPTH-LIMITED-SEARCH(problem, depth)
 *     if result != cutoff then return result
 * </pre>
 * 
 * Figure 3.18 The iterative deepening search algorithm, which repeatedly
 * applies depth-limited search with increasing limits. It terminates when a
 * solution is found or if the depth- limited search returns failure, meaning
 * that no solution exists.
 * 
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 */
public class das_SearchProblemIterativeDeepening extends aa_TreeSearchNodeExpander implements das_i_SearchProblem {
	public static final String PATH_COST = "pathCost";

	// Not infinity, but will do, :-)
	private final int infinity = Integer.MAX_VALUE;

	private final d_Metrics iterationMetrics;

	public das_SearchProblemIterativeDeepening() {
		iterationMetrics = new d_Metrics();
		iterationMetrics.set(METRIC_NODES_EXPANDED, 0);
		iterationMetrics.set(PATH_COST, 0);
	}

	// function ITERATIVE-DEEPENING-SEARCH(problem) returns a solution, or
	// failure
	public List<aa_i_Action> search(a_Problem p) throws Exception {
		iterationMetrics.set(METRIC_NODES_EXPANDED, 0);
		iterationMetrics.set(PATH_COST, 0);
		// for depth = 0 to infinity do
		for (int i = 0; i <= infinity; i++) {
			// result <- DEPTH-LIMITED-SEARCH(problem, depth)
			das_SearchProblemDepthLimited dls = new das_SearchProblemDepthLimited(i);
			List<aa_i_Action> result = dls.search(p);
			iterationMetrics.set(METRIC_NODES_EXPANDED,
					iterationMetrics.getInt(METRIC_NODES_EXPANDED)
							+ dls.getMetrics().getInt(METRIC_NODES_EXPANDED));
			// if result != cutoff then return result
			if (!dls.isCutOff(result)) {
				iterationMetrics.set(PATH_COST, dls.getPathCost());
				return result;
			}
		}
		return failure();
	}

	@Override
	public d_Metrics getMetrics() {
		return iterationMetrics;
	}

	//
	// PRIVATE METHODS
	//

	private List<aa_i_Action> failure() {
		return Collections.emptyList();
	}
}