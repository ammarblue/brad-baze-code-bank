package reuze.test;
//package aima.test.core.unit.probability.mdp;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.software.reuze.a_CellWorld;
import com.software.reuze.a_CellWorldAction;
import com.software.reuze.aa_PolicyEvaluationModified;
import com.software.reuze.aa_PolicyIteration;
import com.software.reuze.aa_i_Policy;
import com.software.reuze.ga_CellPositionAndContent;
import com.software.reuze.mp_i_MDP;


/*import aima.core.environment.cellworld.CellContent;
import aima.core.environment.cellworld.CellWorld;
import aima.core.environment.cellworld.CellWorldAction;
import aima.core.environment.cellworld.CellWorldFactory;
import aima.core.probability.example.MDPFactory;
import aima.core.probability.mdp.MarkovDecisionProcess;
import aima.core.probability.mdp.Policy;
import aima.core.probability.mdp.impl.ModifiedPolicyEvaluation;
import aima.core.probability.mdp.search.PolicyIteration;*/

/**
 * 
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 */
public class PolicyIterationTest {
	private a_CellWorld<Double> cw = null;
	private mp_i_MDP<ga_CellPositionAndContent<Double>, a_CellWorldAction> mdp = null;
	private aa_PolicyIteration<ga_CellPositionAndContent<Double>, a_CellWorldAction> pi = null;

	@Before
	public void setUp() {
		cw = CellWorldFactory.createCellWorldForFig17_1();
		mdp = MDPFactory.createMDPForFigure17_3(cw);
		pi = new aa_PolicyIteration<ga_CellPositionAndContent<Double>, a_CellWorldAction>(
				new aa_PolicyEvaluationModified<ga_CellPositionAndContent<Double>, a_CellWorldAction>(50, 1.0));
	}

	@Test
	public void testPolicyIterationForFig17_2() {

		// AIMA3e check with Figure 17.2 (a)
		aa_i_Policy<ga_CellPositionAndContent<Double>, a_CellWorldAction> policy = pi.policyIteration(mdp);

		Assert.assertEquals(a_CellWorldAction.Up,
				policy.action(cw.getCellAt(1, 1)));
		Assert.assertEquals(a_CellWorldAction.Up,
				policy.action(cw.getCellAt(1, 2)));
		Assert.assertEquals(a_CellWorldAction.Right,
				policy.action(cw.getCellAt(1, 3)));

		Assert.assertEquals(a_CellWorldAction.Left,
				policy.action(cw.getCellAt(2, 1)));
		Assert.assertEquals(a_CellWorldAction.Right,
				policy.action(cw.getCellAt(2, 3)));

		Assert.assertEquals(a_CellWorldAction.Left,
				policy.action(cw.getCellAt(3, 1)));
		Assert.assertEquals(a_CellWorldAction.Up,
				policy.action(cw.getCellAt(3, 2)));
		Assert.assertEquals(a_CellWorldAction.Right,
				policy.action(cw.getCellAt(3, 3)));

		Assert.assertEquals(a_CellWorldAction.Left,
				policy.action(cw.getCellAt(4, 1)));
		Assert.assertNull(policy.action(cw.getCellAt(4, 2)));
		Assert.assertNull(policy.action(cw.getCellAt(4, 3)));
	}
}
