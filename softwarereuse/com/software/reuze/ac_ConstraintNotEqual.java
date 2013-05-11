package com.software.reuze;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a binary constraint which forbids equal values.
 * 
 * @author Ruediger Lunde
 */
public class ac_ConstraintNotEqual implements ac_i_Constraint {

	private mf_NodeTermVariable var1;
	private mf_NodeTermVariable var2;
	private List<mf_NodeTermVariable> scope;

	public ac_ConstraintNotEqual(mf_NodeTermVariable var1, mf_NodeTermVariable var2) {
		this.var1 = var1;
		this.var2 = var2;
		scope = new ArrayList<mf_NodeTermVariable>(2);
		scope.add(var1);
		scope.add(var2);
	}

	public List<mf_NodeTermVariable> getScope() {
		return scope;
	}

	public boolean isSatisfiedWith(ac_OpsAssignmentVariables assignment) {
		Object value1 = assignment.getAssignment(var1);
		return value1 == null || !value1.equals(assignment.getAssignment(var2));
	}
}
