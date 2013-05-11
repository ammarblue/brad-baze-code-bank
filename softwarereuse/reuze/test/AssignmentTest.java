package reuze.test;
//package aima.test.core.unit.search.csp;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.software.reuze.ac_OpsAssignmentVariables;
import com.software.reuze.mf_NodeTermVariable;


/**
 * @author Ravi Mohan
 * 
 */
public class AssignmentTest {
	private static final mf_NodeTermVariable X = new mf_NodeTermVariable("x");
	private static final mf_NodeTermVariable Y = new mf_NodeTermVariable("y");

	private List<mf_NodeTermVariable> variables;
	private ac_OpsAssignmentVariables assignment;

	@Before
	public void setUp() {
		variables = new ArrayList<mf_NodeTermVariable>();
		variables.add(X);
		variables.add(Y);
		assignment = new ac_OpsAssignmentVariables();
	}

	@Test
	public void testAssignmentCompletion() {
		Assert.assertFalse(assignment.isComplete(variables));
		assignment.setAssignment(X, "Ravi");
		Assert.assertFalse(assignment.isComplete(variables));
		assignment.setAssignment(Y, "AIMA");
		Assert.assertTrue(assignment.isComplete(variables));
		assignment.removeAssignment(X);
		Assert.assertFalse(assignment.isComplete(variables));
	}

	// @Test
	// public void testAssignmentDefaultVariableSelection() {
	// Assert.assertEquals(X, assignment.selectFirstUnassignedVariable(csp));
	// assignment.setAssignment(X, "Ravi");
	// Assert.assertEquals(Y, assignment.selectFirstUnassignedVariable(csp));
	// assignment.setAssignment(Y, "AIMA");
	// Assert.assertEquals(null, assignment.selectFirstUnassignedVariable(csp));
	// }
}