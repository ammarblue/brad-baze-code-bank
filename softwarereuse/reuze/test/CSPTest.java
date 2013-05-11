package reuze.test;
//package aima.test.core.unit.search.csp;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.software.reuze.ac_CSP;
import com.software.reuze.ac_ConstraintNotEqual;
import com.software.reuze.ac_Domain;
import com.software.reuze.ac_i_Constraint;
import com.software.reuze.mf_NodeTermVariable;


/**
 * @author Ruediger Lunde
 */
public class CSPTest {
	private static final mf_NodeTermVariable X = new mf_NodeTermVariable("x");
	private static final mf_NodeTermVariable Y = new mf_NodeTermVariable("y");
	private static final mf_NodeTermVariable Z = new mf_NodeTermVariable("z");

	private static final ac_i_Constraint C1 = new ac_ConstraintNotEqual(X, Y);
	private static final ac_i_Constraint C2 = new ac_ConstraintNotEqual(X, Y);

	private ac_Domain colors;
	private ac_Domain animals;

	private List<mf_NodeTermVariable> variables;

	@Before
	public void setUp() {
		variables = new ArrayList<mf_NodeTermVariable>();
		variables.add(X);
		variables.add(Y);
		variables.add(Z);
		colors = new ac_Domain(new Object[] { "red", "green", "blue" });
		animals = new ac_Domain(new Object[] { "cat", "dog" });
	}

	@Test
	public void testConstraintNetwork() {
		ac_CSP csp = new ac_CSP(variables);
		csp.addConstraint(C1);
		csp.addConstraint(C2);
		Assert.assertNotNull(csp.getConstraints());
		Assert.assertEquals(2, csp.getConstraints().size());
		Assert.assertNotNull(csp.getConstraints(X));
		Assert.assertEquals(2, csp.getConstraints(X).size());
		Assert.assertNotNull(csp.getConstraints(Y));
		Assert.assertEquals(2, csp.getConstraints(Y).size());
		Assert.assertNotNull(csp.getConstraints(Z));
		Assert.assertEquals(0, csp.getConstraints(Z).size());
	}

	@Test
	public void testDomainChanges() {
		ac_Domain colors2 = new ac_Domain(colors.asList());
		Assert.assertEquals(colors, colors2);

		ac_CSP csp = new ac_CSP(variables);
		csp.addConstraint(C1);
		Assert.assertNotNull(csp.getDomain(X));
		Assert.assertEquals(0, csp.getDomain(X).size());
		Assert.assertNotNull(csp.getConstraints(X));

		csp.setDomain(X, colors);
		Assert.assertEquals(colors, csp.getDomain(X));
		Assert.assertEquals(3, csp.getDomain(X).size());
		Assert.assertEquals("red", csp.getDomain(X).get(0));

		ac_CSP cspCopy = csp.copyDomains();
		Assert.assertNotNull(cspCopy.getDomain(X));
		Assert.assertEquals(3, cspCopy.getDomain(X).size());
		Assert.assertEquals("red", cspCopy.getDomain(X).get(0));
		Assert.assertNotNull(cspCopy.getDomain(Y));
		Assert.assertEquals(0, cspCopy.getDomain(Y).size());
		Assert.assertNotNull(cspCopy.getConstraints(X));
		Assert.assertEquals(C1, cspCopy.getConstraints(X).get(0));

		cspCopy.removeValueFromDomain(X, "red");
		Assert.assertEquals(2, cspCopy.getDomain(X).size());
		Assert.assertEquals("green", cspCopy.getDomain(X).get(0));
		Assert.assertEquals(3, csp.getDomain(X).size());
		Assert.assertEquals("red", csp.getDomain(X).get(0));

		cspCopy.setDomain(X, animals);
		Assert.assertEquals(2, cspCopy.getDomain(X).size());
		Assert.assertEquals("cat", cspCopy.getDomain(X).get(0));
		Assert.assertEquals(3, csp.getDomain(X).size());
		Assert.assertEquals("red", csp.getDomain(X).get(0));
	}
}
