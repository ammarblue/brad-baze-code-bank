package com.software.reuze;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.software.reuze.a_Problem;
import com.software.reuze.a_Utils;
import com.software.reuze.aa_SearchUtils;
import com.software.reuze.aa_TreeSearchNode;
import com.software.reuze.aa_TreeSearchNodeExpander;
import com.software.reuze.aa_i_Action;
import com.software.reuze.das_i_SearchProblem;
import com.software.reuze.lc_ThreadCancelable;
import com.software.reuze.m_i_HeuristicFunction;


/**
 * Artificial Intelligence A Modern Approach (3rd Edition): Figure 4.5, page
 * 126.<br>
 * <br>
 * 
 * <pre>
 * function SIMULATED-ANNEALING(problem, schedule) returns a solution state
 *                    
 *   current &lt;- MAKE-NODE(problem.INITIAL-STATE)
 *   for t = 1 to INFINITY do
 *     T &lt;- schedule(t)
 *     if T = 0 then return current
 *     next &lt;- a randomly selected successor of current
 *     /\E &lt;- next.VALUE - current.value
 *     if /\E &gt; 0 then current &lt;- next
 *     else current &lt;- next only with probability e&circ;(/\E/T)
 * </pre>
 * 
 * Figure 4.5 The simulated annealing search algorithm, a version of stochastic
 * hill climbing where some downhill moves are allowed. Downhill moves are
 * accepted readily early in the annealing schedule and then less often as time
 * goes on. The schedule input determines the value of the temperature T as a
 * function of time.
 * 
 * @author Ravi Mohan
 * @author Mike Stampone
 */
public class aa_TreeSearchSimulatedAnnealing extends aa_TreeSearchNodeExpander implements das_i_SearchProblem {

	public enum SearchOutcome {
		FAILURE, SOLUTION_FOUND
	};

	private final m_i_HeuristicFunction hf;
	private final aa_SearchScheduler scheduler;

	private SearchOutcome outcome = SearchOutcome.FAILURE;

	private Object lastState = null;

	/**
	 * Constructs a simulated annealing search from the specified heuristic
	 * function and a default scheduler.
	 * 
	 * @param hf
	 *            a heuristic function
	 */
	public aa_TreeSearchSimulatedAnnealing(m_i_HeuristicFunction hf) {
		this.hf = hf;
		this.scheduler = new aa_SearchScheduler();
	}

	/**
	 * Constructs a simulated annealing search from the specified heuristic
	 * function and scheduler.
	 * 
	 * @param hf
	 *            a heuristic function
	 * @param scheduler
	 *            a mapping from time to "temperature"
	 */
	public aa_TreeSearchSimulatedAnnealing(m_i_HeuristicFunction hf, aa_SearchScheduler scheduler) {
		this.hf = hf;
		this.scheduler = scheduler;
	}

	// function SIMULATED-ANNEALING(problem, schedule) returns a solution state
	public List<aa_i_Action> search(a_Problem p) throws Exception {
		clearInstrumentation();
		outcome = SearchOutcome.FAILURE;
		lastState = null;
		// current <- MAKE-NODE(problem.INITIAL-STATE)
		aa_TreeSearchNode current = new aa_TreeSearchNode(p.getInitialState());
		aa_TreeSearchNode next = null;
		List<aa_i_Action> ret = new ArrayList<aa_i_Action>();
		// for t = 1 to INFINITY do
		int timeStep = 0;
		while (!lc_ThreadCancelable.currIsCanceled()) {
			// temperature <- schedule(t)
			double temperature = scheduler.getTemp(timeStep);
			timeStep++;
			// if temperature = 0 then return current
			if (temperature == 0.0) {
				if (aa_SearchUtils.isGoalState(p, current)) {
					outcome = SearchOutcome.SOLUTION_FOUND;
				}
				ret = aa_SearchUtils.actionsFromNodes(current.getPathFromRoot());
				lastState = current.getState();
				break;
			}

			List<aa_TreeSearchNode> children = expandNode(current, p);
			if (children.size() > 0) {
				// next <- a randomly selected successor of current
				next = a_Utils.selectRandomlyFromList(children);
				// /\E <- next.VALUE - current.value
				double deltaE = getValue(p, next) - getValue(p, current);

				if (shouldAccept(temperature, deltaE)) {
					current = next;
				}
			}
		}

		return ret;
	}

	/**
	 * Returns <em>e</em><sup>&delta<em>E / T</em></sup>
	 * 
	 * @param temperature
	 *            <em>T</em>, a "temperature" controlling the probability of
	 *            downward steps
	 * @param deltaE
	 *            VALUE[<em>next</em>] - VALUE[<em>current</em>]
	 * @return <em>e</em><sup>&delta<em>E / T</em></sup>
	 */
	public double probabilityOfAcceptance(double temperature, double deltaE) {
		return Math.exp(deltaE / temperature);
	}

	public SearchOutcome getOutcome() {
		return outcome;
	}

	/**
	 * Returns the last state from which the simulated annealing search found a
	 * solution state.
	 * 
	 * @return the last state from which the simulated annealing search found a
	 *         solution state.
	 */
	public Object getLastSearchState() {
		return lastState;
	}

	//
	// PRIVATE METHODS
	//

	// if /\E > 0 then current <- next
	// else current <- next only with probability e^(/\E/T)
	private boolean shouldAccept(double temperature, double deltaE) {
		return (deltaE > 0.0)
				|| (new Random().nextDouble() <= probabilityOfAcceptance(
						temperature, deltaE));
	}

	private double getValue(a_Problem p, aa_TreeSearchNode n) {
		// assumption greater heuristic value =>
		// HIGHER on hill; 0 == goal state;
		// SA deals with gradient DESCENT
		return -1 * hf.h(n.getState());
	}
}