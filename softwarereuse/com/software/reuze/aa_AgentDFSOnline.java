package com.software.reuze;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.software.reuze.aa_ActionDynamicNoOp;
import com.software.reuze.aa_a_Agent;
import com.software.reuze.aa_i_Action;
import com.software.reuze.aa_i_Percept;
import com.software.reuze.aa_i_PerceptToStateFunction;
import com.software.reuze.d_Pair;
import com.software.reuze.da_HashMapTwoKey;


/**
 * Artificial Intelligence A Modern Approach (3rd Edition): Figure 4.21, page
 * 150.<br>
 * <br>
 * 
 * <pre>
 * function ONLINE-DFS-AGENT(s') returns an action
 *   inputs: s', a percept that identifies the current state
 *   persistent: result, a table, indexed by state and action, initially empty
 *               untried, a table that lists, for each state, the actions not yet tried
 *               unbacktracked, a table that lists, for each state, the backtracks not yet tried
 *               s, a, the previous state and action, initially null
 *    
 *   if GOAL-TEST(s') then return stop
 *   if s' is a new state (not in untried) then untried[s'] &lt;- ACTIONS(s')
 *   if s is not null then
 *       result[s, a] &lt;- s'
 *       add s to the front of the unbacktracked[s']
 *   if untried[s'] is empty then
 *       if unbacktracked[s'] is empty then return stop
 *       else a &lt;- an action b such that result[s', b] = POP(unbacktracked[s'])
 *   else a &lt;- POP(untried[s'])
 *   s &lt;- s'
 *   return a
 * </pre>
 * 
 * Figure 4.21 An online search agent that uses depth-first exploration. The
 * agent is applicable only in state spaces in which every action can be
 * "undone" by some other action.<br>
 * 
 * @author Ciaran O'Reilly
 * 
 */
public class aa_AgentDFSOnline extends aa_a_Agent {

	private aa_SearchProblemOnline problem;
	private aa_i_PerceptToStateFunction ptsFunction;
	// persistent: result, a table, indexed by state and action, initially empty
	private final da_HashMapTwoKey<Object, aa_i_Action, Object> result = new da_HashMapTwoKey<Object, aa_i_Action, Object>();
	// untried, a table that lists, for each state, the actions not yet tried
	private final Map<Object, List<aa_i_Action>> untried = new HashMap<Object, List<aa_i_Action>>();
	// unbacktracked, a table that lists,
	// for each state, the backtracks not yet tried
	private final Map<Object, List<Object>> unbacktracked = new HashMap<Object, List<Object>>();
	// s, a, the previous state and action, initially null
	private Object s = null;
	private aa_i_Action a = null;

	/**
	 * Constructs an online DFS agent with the specified search problem and
	 * percept to state function.
	 * 
	 * @param problem
	 *            an online search problem for this agent to solve
	 * @param ptsFunction
	 *            a function which returns the problem state associated with a
	 *            given Percept.
	 */
	public aa_AgentDFSOnline(aa_SearchProblemOnline problem,
			aa_i_PerceptToStateFunction ptsFunction) {
		setProblem(problem);
		setPerceptToStateFunction(ptsFunction);
	}

	/**
	 * Returns the search problem for this agent.
	 * 
	 * @return the search problem for this agent.
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
	 * Sets the percept to state functino of this agent.
	 * 
	 * @param ptsFunction
	 *            a function which returns the problem state associated with a
	 *            given Percept.
	 */
	public void setPerceptToStateFunction(aa_i_PerceptToStateFunction ptsFunction) {
		this.ptsFunction = ptsFunction;
	}

	// function ONLINE-DFS-AGENT(s') returns an action
	// inputs: s', a percept that identifies the current state
	@Override
	public aa_i_Action execute(aa_i_Percept psDelta) {
		Object sDelta = ptsFunction.getState(psDelta);
		// if GOAL-TEST(s') then return stop
		if (goalTest(sDelta)) {
			a = aa_ActionDynamicNoOp.NO_OP;
		} else {
			// if s' is a new state (not in untried) then untried[s'] <-
			// ACTIONS(s')
			if (!untried.containsKey(sDelta)) {
				untried.put(sDelta, actions(sDelta));
			}

			// if s is not null then do
			if (null != s) {
				// Note: If I've already seen the result of this
				// [s, a] then don't put it back on the unbacktracked
				// list otherwise you can keep oscillating
				// between the same states endlessly.
				if (!(sDelta.equals(result.get(s, a)))) {
					// result[s, a] <- s'
					result.put(s, a, sDelta);

					// Ensure the unbacktracked always has a list for s'
					if (!unbacktracked.containsKey(sDelta)) {
						unbacktracked.put(sDelta, new ArrayList<Object>());
					}

					// add s to the front of the unbacktracked[s']
					unbacktracked.get(sDelta).add(0, s);
				}
			}
			// if untried[s'] is empty then
			if (untried.get(sDelta).isEmpty()) {
				// if unbacktracked[s'] is empty then return stop
				if (unbacktracked.get(sDelta).isEmpty()) {
					a = aa_ActionDynamicNoOp.NO_OP;
				} else {
					// else a <- an action b such that result[s', b] =
					// POP(unbacktracked[s'])
					Object popped = unbacktracked.get(sDelta).remove(0);
					for (d_Pair<Object, aa_i_Action> sa : result.keySet()) {
						if (sa.getFirst().equals(sDelta)
								&& result.get(sa).equals(popped)) {
							a = sa.getSecond();
							break;
						}
					}
				}
			} else {
				// else a <- POP(untried[s'])
				a = untried.get(sDelta).remove(0);
			}
		}

		if (a.isNoOp()) {
			// I'm either at the Goal or can't get to it,
			// which in either case I'm finished so just die.
			setAlive(false);
		}

		// s <- s'
		s = sDelta;
		// return a
		return a;
	}

	//
	// PRIVATE METHODS
	//

	private void init() {
		setAlive(true);
		result.clear();
		untried.clear();
		unbacktracked.clear();
		s = null;
		a = null;
	}

	private boolean goalTest(Object state) {
		return getProblem().isGoalState(state);
	}

	private List<aa_i_Action> actions(Object state) {
		return new ArrayList<aa_i_Action>(problem.getActionsFunction()
				.actions(state));
	}
}
