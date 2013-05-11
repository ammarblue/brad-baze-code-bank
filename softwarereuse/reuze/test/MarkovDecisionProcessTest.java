package reuze.test;
//package aima.test.core.unit.probability.mdp;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.software.reuze.a_CellWorld;
import com.software.reuze.a_CellWorldAction;
import com.software.reuze.ga_CellPositionAndContent;
import com.software.reuze.mp_i_MDP;


/*import aima.core.environment.cellworld.CellContent;
import aima.core.environment.cellworld.CellWorld;
import aima.core.environment.cellworld.CellWorldAction;
import aima.core.environment.cellworld.CellWorldFactory;
import aima.core.probability.example.MDPFactory;
import aima.core.probability.mdp.MarkovDecisionProcess;*/

/**
 * 
 * @author Ciaran O'Reilly
 * @author Ravi Mohan
 *
 */
public class MarkovDecisionProcessTest {
	public static final double DELTA_THRESHOLD = 1e-3;

	private a_CellWorld<Double> cw = null;
	private mp_i_MDP<ga_CellPositionAndContent<Double>, a_CellWorldAction> mdp = null;

	@Before
	public void setUp() {
		cw = CellWorldFactory.createCellWorldForFig17_1();
		mdp = MDPFactory.createMDPForFigure17_3(cw);
	}

	@Test
	public void testActions() {
		// Ensure all actions can be performed in each cell
		// except for the terminal states.
		for (ga_CellPositionAndContent<Double> s : cw.getCells()) {
			if (4 == s.getX() && (3 == s.getY() || 2 == s.getY())) {
				Assert.assertEquals(0, mdp.actions(s).size());
			} else {
				Assert.assertEquals(5, mdp.actions(s).size());
			}
		}
	}

	@Test
	public void testMDPTransitionModel() {
		Assert.assertEquals(0.8, mdp.transitionProbability(cw.getCellAt(1, 2),
				cw.getCellAt(1, 1), a_CellWorldAction.Up), DELTA_THRESHOLD);
		Assert.assertEquals(0.1, mdp.transitionProbability(cw.getCellAt(1, 1),
				cw.getCellAt(1, 1), a_CellWorldAction.Up), DELTA_THRESHOLD);
		Assert.assertEquals(0.1, mdp.transitionProbability(cw.getCellAt(2, 1),
				cw.getCellAt(1, 1), a_CellWorldAction.Up), DELTA_THRESHOLD);
		Assert.assertEquals(0.0, mdp.transitionProbability(cw.getCellAt(1, 3),
				cw.getCellAt(1, 1), a_CellWorldAction.Up), DELTA_THRESHOLD);

		Assert.assertEquals(0.9, mdp.transitionProbability(cw.getCellAt(1, 1),
				cw.getCellAt(1, 1), a_CellWorldAction.Down), DELTA_THRESHOLD);
		Assert.assertEquals(0.1, mdp.transitionProbability(cw.getCellAt(2, 1),
				cw.getCellAt(1, 1), a_CellWorldAction.Down), DELTA_THRESHOLD);
		Assert.assertEquals(0.0, mdp.transitionProbability(cw.getCellAt(3, 1),
				cw.getCellAt(1, 1), a_CellWorldAction.Down), DELTA_THRESHOLD);
		Assert.assertEquals(0.0, mdp.transitionProbability(cw.getCellAt(1, 2),
				cw.getCellAt(1, 1), a_CellWorldAction.Down), DELTA_THRESHOLD);

		Assert.assertEquals(0.9, mdp.transitionProbability(cw.getCellAt(1, 1),
				cw.getCellAt(1, 1), a_CellWorldAction.Left), DELTA_THRESHOLD);
		Assert.assertEquals(0.0, mdp.transitionProbability(cw.getCellAt(2, 1),
				cw.getCellAt(1, 1), a_CellWorldAction.Left), DELTA_THRESHOLD);
		Assert.assertEquals(0.0, mdp.transitionProbability(cw.getCellAt(3, 1),
				cw.getCellAt(1, 1), a_CellWorldAction.Left), DELTA_THRESHOLD);
		Assert.assertEquals(0.1, mdp.transitionProbability(cw.getCellAt(1, 2),
				cw.getCellAt(1, 1), a_CellWorldAction.Left), DELTA_THRESHOLD);

		Assert.assertEquals(0.8, mdp.transitionProbability(cw.getCellAt(2, 1),
				cw.getCellAt(1, 1), a_CellWorldAction.Right), DELTA_THRESHOLD);
		Assert.assertEquals(0.1, mdp.transitionProbability(cw.getCellAt(1, 1),
				cw.getCellAt(1, 1), a_CellWorldAction.Right), DELTA_THRESHOLD);
		Assert.assertEquals(0.1, mdp.transitionProbability(cw.getCellAt(1, 2),
				cw.getCellAt(1, 1), a_CellWorldAction.Right), DELTA_THRESHOLD);
		Assert.assertEquals(0.0, mdp.transitionProbability(cw.getCellAt(1, 3),
				cw.getCellAt(1, 1), a_CellWorldAction.Right), DELTA_THRESHOLD);
	}

	@Test
	public void testRewardFunction() {
		// Ensure all actions can be performed in each cell.
		for (ga_CellPositionAndContent<Double> s : cw.getCells()) {
			if (4 == s.getX() && 3 == s.getY()) {
				Assert.assertEquals(1.0, mdp.reward(s), DELTA_THRESHOLD);
			} else if (4 == s.getX() && 2 == s.getY()) {
				Assert.assertEquals(-1.0, mdp.reward(s), DELTA_THRESHOLD);
			} else {
				Assert.assertEquals(-0.04, mdp.reward(s), DELTA_THRESHOLD);
			}
		}
	}
}
