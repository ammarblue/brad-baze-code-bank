package com.software.reuze;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

/**
 * An assignment assigns values to some or all variables of a CSP.
 * 
 * @author Ruediger Lunde
 */
public class ac_OpsAssignmentVariables {
	/**
	 * Contains all assigned variables. Positions reflect the the order in which
	 * the variables were assigned to values.
	 */
	List<mf_NodeTermVariable> variables;
	/** Maps variables to their assigned values. */
	Hashtable<mf_NodeTermVariable, Object> variableToValue;

	public ac_OpsAssignmentVariables() {
		variables = new ArrayList<mf_NodeTermVariable>();
		variableToValue = new Hashtable<mf_NodeTermVariable, Object>();
	}

	public List<mf_NodeTermVariable> getVariables() {
		return Collections.unmodifiableList(variables);
	}

	public Object getAssignment(mf_NodeTermVariable var) {
		return variableToValue.get(var);
	}

	public void setAssignment(mf_NodeTermVariable var, Object value) {
		if (!variableToValue.containsKey(var))
			variables.add(var);
		variableToValue.put(var, value);
	}

	public void removeAssignment(mf_NodeTermVariable var) {
		if (hasAssignmentFor(var)) {
			variables.remove(var);
			variableToValue.remove(var);
		}
	}

	public boolean hasAssignmentFor(mf_NodeTermVariable var) {
		return variableToValue.get(var) != null;
	}

	/**
	 * Returns true if this assignment does not violate any constraints of
	 * <code>constraints</code>.
	 */
	public boolean isConsistent(List<ac_i_Constraint> constraints) {
		for (ac_i_Constraint cons : constraints)
			if (!cons.isSatisfiedWith(this))
				return false;
		return true;
	}

	/**
	 * Returns true if this assignment assigns values to every variable of
	 * <code>vars</code>.
	 */
	public boolean isComplete(List<mf_NodeTermVariable> vars) {
		for (mf_NodeTermVariable var : vars) {
			if (!hasAssignmentFor(var))
				return false;
		}
		return true;
	}

	/**
	 * Returns true if this assignment assigns values to every variable of
	 * <code>vars</code>.
	 */
	public boolean isComplete(mf_NodeTermVariable[] vars) {
		for (mf_NodeTermVariable var : vars) {
			if (!hasAssignmentFor(var))
				return false;
		}
		return true;
	}

	/**
	 * Returns true if this assignment is consistent as well as complete with
	 * respect to the given CSP.
	 */
	public boolean isSolution(ac_CSP csp) {
		return isConsistent(csp.getConstraints())
				&& isComplete(csp.getVariables());
	}

	public ac_OpsAssignmentVariables copy() {
		ac_OpsAssignmentVariables copy = new ac_OpsAssignmentVariables();
		for (mf_NodeTermVariable var : variables) {
			copy.setAssignment(var, variableToValue.get(var));
		}
		return copy;
	}

	@Override
	public String toString() {
		boolean comma = false;
		StringBuffer result = new StringBuffer("{");
		for (mf_NodeTermVariable var : variables) {
			if (comma)
				result.append(", ");
			result.append(var + "=" + variableToValue.get(var));
			comma = true;
		}
		result.append("}");
		return result.toString();
	}
}