package com.software.reuze;

import java.util.HashMap;
import java.util.Set;

import com.software.reuze.aa_ActionDynamicNoOp;
import com.software.reuze.aa_a_Agent;
import com.software.reuze.aa_i_Action;
import com.software.reuze.aa_i_Percept;
import com.software.reuze.aa_i_PerceptToStateFunction;
import com.software.reuze.da_HashMapTwoKey;
import com.software.reuze.m_i_HeuristicFunction;


/**
 * Artificial Intelligence A Modern Approach 3rdd Edition): Figure 4.24, page
 * 152.<br>
 * <br>
 * 
 * <pre>
 * function LRTA*-AGENT(s') returns an action
 *   inputs: s', a percept that identifies the current state
 *   persistent: result, a table, indexed by state and action, initially empty
 *               H, a table of cost estimates indexed by state, initially empty
 *               s, a, the previous state and action, initially null
 *           
 *   if GOAL-TEST(s') then return stop
 *   if s' is a new state (not in H) then H[s'] &lt;- h(s')
 *   if s is not null
 *     result[s, a] &lt;- s'
 *     H[s] &lt;-        min LRTA*-COST(s, b, result[s, b], H)
 *             b (element of) ACTIONS(s)
 *   a &lt;- an action b in ACTIONS(s') that minimizes LRTA*-COST(s', b, result[s', b], H)
 *   s &lt;- s'
 *   return a
 *   
 * function LRTA*-COST(s, a, s', H) returns a cost estimate
 *   if s' is undefined then return h(s)
 *   else return c(s, a, s') + H[s']
 * </pre>
 * 
 * Figure 4.24 LRTA*-AGENT selects an action according to the value of
 * neighboring states, which are updated as the agent moves about the state
 * space.<br>
 * <br>
 * <b>Note:</b> This algorithm fails to exit if the goal does not exist (e.g.
 * A<->B Goal=X), this could be an issue with the implementation. Comments
 * welcome.
 * 
 * @author Ciaran O'Reilly
 * @author Mike Stampone
 */
public class aa_AgentLRTAStar extends aa_a_Agent {

	private aa_SearchProblemOnline problem;
	private aa_i_PerceptToStateFunction ptsFunction;
	private m_i_HeuristicFunction hf;
	// persistent: result, a table, indexed by state and action, initially empty
	private final da_HashMapTwoKey<Object, aa_i_Action, Object> result = new da_HashMapTwoKey<Object, aa_i_Action, Object>();
	// H, a table of cost estimates indexed by state, initially empty
	private final HashMap<Object, Double> H = new HashMap<Object, Double>();
	// s, a, the previous state and action, initially null
	private Object s = null;
	private aa_i_Action a = null;

	/**
	 * Constructs a LRTA* agent with the specified search problem, percept to
	 * state function, and heuristic function.
	 * 
	 * @param problem
	 *            an online search problem for this agent to solve.
	 * @param ptsFunction
	 *            a function which returns the problem state associated with a
	 *            given Percept.
	 * @param hf
	 *            heuristic function <em>h(n)</em>, which estimates the cost of
	 *            the cheapest path from the state at node <em>n</em> to a goal
	 *            state.
	 */
	public aa_AgentLRTAStar(aa_SearchProblemOnline problem,
			aa_i_PerceptToStateFunction ptsFunction, m_i_HeuristicFunction hf) {
		setProblem(problem);
		setPerceptToStateFunction(ptsFunction);
		setHeuristicFunction(hf);
	}

	/**
	 * Returns the search problem of this agent.
	 * 
	 * @return the search problem of this agent.
	 */
	public aa_SearchProblemOnline getProblem() {
		return problem;
	}

	/**
	 * Sets the search problem for this agent to solve.
	 * 
	 * @param problem
	 *            the search problem for this agent to solve.
	 */
	public void setProblem(aa_SearchProblemOnline problem) {
		this.problem = problem;
		init();
	}

	/**
	 * Returns the percept to state function of this agent.
	 * 
	 * @return the percept to state function of this agent.
	 */
	public aa_i_PerceptToStateFunction getPerceptToStateFunction() {
		return ptsFunction;
	}

	/**
	 * Sets the percept to state function of this agent.
	 * 
	 * @param ptsFunction
	 *            a function which returns the problem state associated with a
	 *            given Percept.
	 */
	public void setPerceptToStateFunction(aa_i_PerceptToStateFunction ptsFunction) {
		this.ptsFunction = ptsFunction;
	}

	/**
	 * Returns the heuristic function of this agent.
	 */
	public m_i_HeuristicFunction getHeuristicFunction() {
		return hf;
	}

	/**
	 * Sets the heuristic function of this agent.
	 * 
	 * @param hf
	 *            heuristic function <em>h(n)</em>, which estimates the cost of
	 *            the cheapest path from the state at node <em>n</em> to a goal
	 *            state.
	 */
	public void setHeuristicFunction(m_i_HeuristicFunction hf) {
		this.hf = hf;
	}

	// function LRTA*-AGENT(s') returns an action
	// inputs: s', a percept that identifies the current state
	@Override
	public aa_i_Action execute(aa_i_Percept psDelta) {
		Object sDelta = ptsFunction.getState(psDelta);
		// if GOAL-TEST(s') then return stop
		if (goalTest(sDelta)) {
			a = aa_ActionDynamicNoOp.NO_OP;
		} else {
			// if s' is a new state (not in H) then H[s'] <- h(s')
			if (!H.containsKey(sDelta)) {
				H.put(sDelta, getHeuristicFunction().h(sDelta));
			}
			// if s is not null
			if (null != s) {
				// result[s, a] <- s'
				result.put(s, a, sDelta);

				// H[s] <- min LRTA*-COST(s, b, result[s, b], H)
				// b (element of) ACTIONS(s)
				double min = Double.MAX_VALUE;
				for (aa_i_Action b : actions(s)) {
					double cost = lrtaCost(s, b, result.get(s, b));
					if (cost < min) {
						min = cost;
					}
				}
				H.put(s, min);
			}
			// a <- an action b in ACTIONS(s') that minimizes LRTA*-COST(s', b,
			// result[s', b], H)
			double min = Double.MAX_VALUE;
			// Just in case no actions
			a = aa_ActionDynamicNoOp.NO_OP;
			for (aa_i_Action b : actions(sDelta)) {
				double cost = lrtaCost(sDelta, b, result.get(sDelta, b));
				if (cost < min) {
					min = cost;
					a = b;
				}
			}
		}

		// s <- s'
		s = sDelta;

		if (a.isNoOp()) {
			// I'm either at the Goal or can't get to it,
			// which in either case I'm finished so just die.
			setAlive(false);
		}
		// return a
		return a;
	}

	//
	// PRIVATE METHODS
	//
	private void init() {
		setAlive(true);
		result.clear();
		H.clear();
		s = null;
		a = null;
	}

	private boolean goalTest(Object state) {
		return getProblem().isGoalState(state);
	}

	// function LRTA*-COST(s, a, s', H) returns a cost estimate
	private double lrtaCost(Object s, aa_i_Action action, Object sDelta) {
		// if s' is undefined then return h(s)
		if (null == sDelta) {
			return getHeuristicFunction().h(s);
		}
		// else return c(s, a, s') + H[s']
		return getProblem().getStepCostFunction().c(s, action, sDelta)
				+ H.get(sDelta);
	}

	private Set<aa_i_Action> actions(Object state) {
		return problem.getActionsFunction().actions(state);
	}
}
