package reuze.test;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import reuze.pending.dg_NQueensFunctionFactory;

import com.software.reuze.a_GoalTestDefault;
import com.software.reuze.a_Problem;
import com.software.reuze.aa_ActionsFunctionMap;
import com.software.reuze.aa_AgentSearch;
import com.software.reuze.aa_CostFunctionMapStep;
import com.software.reuze.aa_TreeSearchNodeExpanderQueue;
import com.software.reuze.aa_i_Action;
import com.software.reuze.d_MapExtendable;
import com.software.reuze.d_MapRomaniaRoadsSimplified;
import com.software.reuze.d_i_Map;
import com.software.reuze.das_SearchProblemPriorityUniformCost;
import com.software.reuze.das_i_SearchProblem;
import com.software.reuze.dg_NQueensBoard;
import com.software.reuze.dg_NQueensGoalTest;

/*import aima.core.environment.nqueens.NQueensBoard;
import aima.core.environment.nqueens.NQueensFunctionFactory;
import aima.core.environment.nqueens.NQueensGoalTest;*/

/**
 * @author Ciaran O'Reilly
 * 
 */
public class UniformCostSearchTest {

	@Test
	public void testUniformCostSuccesfulSearch() throws Exception {
		a_Problem problem = new a_Problem(new dg_NQueensBoard(8),
				dg_NQueensFunctionFactory.getIActionsFunction(),
				dg_NQueensFunctionFactory.getResultFunction(),
				new dg_NQueensGoalTest());
		das_i_SearchProblem search = new das_SearchProblemPriorityUniformCost();
		aa_AgentSearch agent = new aa_AgentSearch(problem, search);

		List<aa_i_Action> actions = agent.getActions();

		Assert.assertEquals(8, actions.size());

		Assert.assertEquals("1965",
				agent.getInstrumentation().getProperty("nodesExpanded"));

		Assert.assertEquals("8.0",
				agent.getInstrumentation().getProperty("pathCost"));
	}

	@Test
	public void testUniformCostUnSuccesfulSearch() throws Exception {
		a_Problem problem = new a_Problem(new dg_NQueensBoard(3),
				dg_NQueensFunctionFactory.getIActionsFunction(),
				dg_NQueensFunctionFactory.getResultFunction(),
				new dg_NQueensGoalTest());
		das_i_SearchProblem search = new das_SearchProblemPriorityUniformCost();
		aa_AgentSearch agent = new aa_AgentSearch(problem, search);

		List<aa_i_Action> actions = agent.getActions();

		Assert.assertEquals(0, actions.size());

		Assert.assertEquals("6",
				agent.getInstrumentation().getProperty("nodesExpanded"));

		// Will be 0 as did not reach goal state.
		Assert.assertEquals("0",
				agent.getInstrumentation().getProperty("pathCost"));
	}

	@Test
	public void testAIMA3eFigure3_15() throws Exception {
		d_i_Map romaniaMap = new d_MapRomaniaRoadsSimplified();
		a_Problem problem = new a_Problem(d_MapRomaniaRoadsSimplified.SIBIU,
				aa_ActionsFunctionMap.getActionsFunction(romaniaMap),
				aa_ActionsFunctionMap.getResultFunction(), new a_GoalTestDefault(
						d_MapRomaniaRoadsSimplified.BUCHAREST),
				new aa_CostFunctionMapStep(romaniaMap));

		das_i_SearchProblem search = new das_SearchProblemPriorityUniformCost();
		aa_AgentSearch agent = new aa_AgentSearch(problem, search);

		List<aa_i_Action> actions = agent.getActions();

		Assert.assertEquals(
				"[aa_i_Action[name==moveTo, location==RimnicuVilcea], aa_i_Action[name==moveTo, location==Pitesti], aa_i_Action[name==moveTo, location==Bucharest]]",
				actions.toString());
		Assert.assertEquals("278.0",
				search.getMetrics().get(aa_TreeSearchNodeExpanderQueue.METRIC_PATH_COST));
	}

	@Test
	public void testCheckFrontierPathCost() throws Exception {
		d_MapExtendable map = new d_MapExtendable();
		map.addBidirectionalLink("start", "b", 2.5);
		map.addBidirectionalLink("start", "c", 1.0);
		map.addBidirectionalLink("b", "d", 2.0);
		map.addBidirectionalLink("c", "d", 4.0);
		map.addBidirectionalLink("c", "e", 1.0);
		map.addBidirectionalLink("d", "goal", 1.0);
		map.addBidirectionalLink("e", "goal", 5.0);
		a_Problem problem = new a_Problem("start",
				aa_ActionsFunctionMap.getActionsFunction(map),
				aa_ActionsFunctionMap.getResultFunction(), new a_GoalTestDefault(
						"goal"), new aa_CostFunctionMapStep(map));

		das_i_SearchProblem search = new das_SearchProblemPriorityUniformCost();
		aa_AgentSearch agent = new aa_AgentSearch(problem, search);

		List<aa_i_Action> actions = agent.getActions();

		Assert.assertEquals(
				"[aa_i_Action[name==moveTo, location==b], aa_i_Action[name==moveTo, location==d], aa_i_Action[name==moveTo, location==goal]]",
				actions.toString());
		Assert.assertEquals("5.5",
				search.getMetrics().get(aa_TreeSearchNodeExpanderQueue.METRIC_PATH_COST));
	}
}
