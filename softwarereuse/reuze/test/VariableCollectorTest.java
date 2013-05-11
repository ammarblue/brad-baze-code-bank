package reuze.test;
//package aima.test.core.unit.logic.fol;

import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.software.reuze.mf_DomainExamples;
import com.software.reuze.mf_NodeTermVariable;
import com.software.reuze.mf_Parser;
import com.software.reuze.mf_VariableCollector;


/*import aima.core.logic.fol.VariableCollector;
import aima.core.logic.fol.domain.DomainFactory;
import aima.core.logic.fol.parsing.FOLParser;
import aima.core.logic.fol.parsing.ast.Variable;*/

/**
 * @author Ravi Mohan
 * 
 */
public class VariableCollectorTest {

	mf_Parser parser;

	mf_VariableCollector vc;

	@Before
	public void setUp() {
		parser = new mf_Parser(mf_DomainExamples.crusadesDomain());
		vc = new mf_VariableCollector();
	}

	@Test
	public void testSimplepredicate() {
		Set<mf_NodeTermVariable> variables = vc.collectAllVariables(parser
				.parse("King(x)"));
		Assert.assertEquals(1, variables.size());
		Assert.assertTrue(variables.contains(new mf_NodeTermVariable("x")));
	}

	@Test
	public void testMultipleVariables() {
		Set<mf_NodeTermVariable> variables = vc.collectAllVariables(parser
				.parse("BrotherOf(x) = EnemyOf(y)"));
		Assert.assertEquals(2, variables.size());
		Assert.assertTrue(variables.contains(new mf_NodeTermVariable("x")));
		Assert.assertTrue(variables.contains(new mf_NodeTermVariable("y")));
	}

	@Test
	public void testQuantifiedVariables() {
		// Note: Should collect quantified variables
		// even if not mentioned in clause.
		Set<mf_NodeTermVariable> variables = vc.collectAllVariables(parser
				.parse("FORALL x,y,z (BrotherOf(x) = EnemyOf(y))"));
		Assert.assertEquals(3, variables.size());
		Assert.assertTrue(variables.contains(new mf_NodeTermVariable("x")));
		Assert.assertTrue(variables.contains(new mf_NodeTermVariable("y")));
		Assert.assertTrue(variables.contains(new mf_NodeTermVariable("z")));
	}
}
