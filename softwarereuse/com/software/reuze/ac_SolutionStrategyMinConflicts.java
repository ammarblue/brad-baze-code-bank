package com.software.reuze;

import java.util.ArrayList;
import java.util.List;

import com.software.reuze.a_Utils;


/**
 * Artificial Intelligence A Modern Approach (3rd Ed.): Figure 6.8, Page 221.<br>
 * <br>
 * 
 * <pre>
 * <code>
 * function MIN-CONFLICTS(csp, max-steps) returns a solution or failure
 *    inputs: csp, a constraint satisfaction problem
 *            max-steps, the number of steps allowed before giving up
 *    current = an initial complete assignment for csp
 *    for i = 1 to max steps do
 *       if current is a solution for csp then return current
 *       var = a randomly chosen conflicted variable from csp.VARIABLES
 *       value = the value v for var that minimizes CONFLICTS(var, v, current, csp)
 *       set var = value in current
 *    return failure
 * </code>
 * </pre>
 * 
 * Figure 6.8 The MIN-CONFLICTS algorithm for solving CSPs by local search. The
 * initial state may be chosen randomly or by a greedy assignment process that
 * chooses a minimal-conflict value for each variable in turn. The CONFLICTS
 * function counts the number of constraints violated by a particular value,
 * given the rest of the current assignment.
 * 
 * @author Ruediger Lunde
 * @author Mike Stampone
 */
public class ac_SolutionStrategyMinConflicts extends ac_a_SolutionStrategy {
	private int maxSteps;

	/**
	 * Constructs a min-conflicts strategy with a given number of steps allowed
	 * before giving up.
	 * 
	 * @param maxSteps
	 *            the number of steps allowed before giving up
	 */
	public ac_SolutionStrategyMinConflicts(int maxSteps) {
		this.maxSteps = maxSteps;
	}

	public ac_OpsAssignmentVariables solve(ac_CSP csp) {
		ac_OpsAssignmentVariables assignment = generateRandomAssignment(csp);
		fireStateChanged(assignment, csp);
		for (int i = 0; i < maxSteps; i++) {
			if (assignment.isSolution(csp)) {
				return assignment;
			} else {
				List<mf_NodeTermVariable> vars = getConflictedVariables(assignment, csp);
				mf_NodeTermVariable var = a_Utils.selectRandomlyFromList(vars);
				Object value = getMinConflictValueFor(var, assignment, csp);
				assignment.setAssignment(var, value);
				fireStateChanged(assignment, csp);
			}
		}
		return null;
	}

	private ac_OpsAssignmentVariables generateRandomAssignment(ac_CSP csp) {
		ac_OpsAssignmentVariables assignment = new ac_OpsAssignmentVariables();
		for (mf_NodeTermVariable var : csp.getVariables()) {
			Object randomValue = a_Utils.selectRandomlyFromList(csp.getDomain(var)
					.asList());
			assignment.setAssignment(var, randomValue);
		}
		return assignment;
	}

	private List<mf_NodeTermVariable> getConflictedVariables(ac_OpsAssignmentVariables assignment, ac_CSP csp) {
		List<mf_NodeTermVariable> result = new ArrayList<mf_NodeTermVariable>();
		for (ac_i_Constraint constraint : csp.getConstraints()) {
			if (!constraint.isSatisfiedWith(assignment))
				for (mf_NodeTermVariable var : constraint.getScope())
					if (!result.contains(var))
						result.add(var);
		}
		return result;
	}

	private Object getMinConflictValueFor(mf_NodeTermVariable var, ac_OpsAssignmentVariables assignment,
			ac_CSP csp) {
		List<ac_i_Constraint> constraints = csp.getConstraints(var);
		ac_OpsAssignmentVariables duplicate = assignment.copy();
		int minConflict = Integer.MAX_VALUE;
		List<Object> resultCandidates = new ArrayList<Object>();
		for (Object value : csp.getDomain(var)) {
			duplicate.setAssignment(var, value);
			int currConflict = countConflicts(duplicate, constraints);
			if (currConflict <= minConflict) {
				if (currConflict < minConflict) {
					resultCandidates.clear();
					minConflict = currConflict;
				}
				resultCandidates.add(value);
			}
		}
		if (!resultCandidates.isEmpty())
			return a_Utils.selectRandomlyFromList(resultCandidates);
		else
			return null;
	}

	private int countConflicts(ac_OpsAssignmentVariables assignment,
			List<ac_i_Constraint> constraints) {
		int result = 0;
		for (ac_i_Constraint constraint : constraints)
			if (!constraint.isSatisfiedWith(assignment))
				result++;
		return result;
	}
}
