package reuze.test;
//package aima.test.core.unit.search.informed;

import org.junit.Assert;
import org.junit.Test;

import com.software.reuze.a_GoalTestDefault;
import com.software.reuze.a_Problem;
import com.software.reuze.aa_ActionsFunctionMap;
import com.software.reuze.aa_AgentSearch;
import com.software.reuze.aa_CostFunctionMapStep;
import com.software.reuze.aa_TreeSearch;
import com.software.reuze.apm_HeuristicAdaptableStraightLineDistance;
import com.software.reuze.d_MapRomaniaRoadsSimplified;
import com.software.reuze.d_i_Map;
import com.software.reuze.dag_GraphSearch;
import com.software.reuze.das_TreeBestFirstGreedy;
import com.software.reuze.das_i_SearchProblem;
import com.software.reuze.dg_EightPuzzleBoard;
import com.software.reuze.dg_EightPuzzleFunctionFactory;
import com.software.reuze.dg_EightPuzzleGoalTest;
import com.software.reuze.dg_EightPuzzleManhattanHeuristicFunction;


public class GreedyBestFirstSearchTest {

	@Test
	public void testGreedyBestFirstSearch() {
		try {
			// EightPuzzleBoard extreme = new EightPuzzleBoard(new int[]
			// {2,0,5,6,4,8,3,7,1});
			// EightPuzzleBoard extreme = new EightPuzzleBoard(new int[]
			// {0,8,7,6,5,4,3,2,1});
			dg_EightPuzzleBoard board = new dg_EightPuzzleBoard(new int[] { 7, 1, 8,
					0, 4, 6, 2, 3, 5 });

			a_Problem problem = new a_Problem(board,
					dg_EightPuzzleFunctionFactory.getActionsFunction(),
					dg_EightPuzzleFunctionFactory.getResultFunction(),
					new dg_EightPuzzleGoalTest());
			das_i_SearchProblem search = new das_TreeBestFirstGreedy(new dag_GraphSearch(),
					new dg_EightPuzzleManhattanHeuristicFunction());
			aa_AgentSearch agent = new aa_AgentSearch(problem, search);
			Assert.assertEquals(49, agent.getActions().size());
			Assert.assertEquals("197",
					agent.getInstrumentation().getProperty("nodesExpanded"));
			Assert.assertEquals("140",
					agent.getInstrumentation().getProperty("queueSize"));
			Assert.assertEquals("141",
					agent.getInstrumentation().getProperty("maxQueueSize"));
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Exception thrown.");
		}
	}

	@Test
	public void testAIMA3eFigure3_23() throws Exception {
		d_i_Map romaniaMap = new d_MapRomaniaRoadsSimplified();
		a_Problem problem = new a_Problem(d_MapRomaniaRoadsSimplified.ARAD,
				aa_ActionsFunctionMap.getActionsFunction(romaniaMap),
				aa_ActionsFunctionMap.getResultFunction(), new a_GoalTestDefault(
						d_MapRomaniaRoadsSimplified.BUCHAREST),
				new aa_CostFunctionMapStep(romaniaMap));

		das_i_SearchProblem search = new das_TreeBestFirstGreedy(new aa_TreeSearch(),
				new apm_HeuristicAdaptableStraightLineDistance(
						d_MapRomaniaRoadsSimplified.BUCHAREST, romaniaMap));
		aa_AgentSearch agent = new aa_AgentSearch(problem, search);
		Assert.assertEquals(
				"[aa_i_Action[name==moveTo, location==Sibiu], aa_i_Action[name==moveTo, location==Fagaras], aa_i_Action[name==moveTo, location==Bucharest]]",
				agent.getActions().toString());
		Assert.assertEquals(3, agent.getActions().size());
		Assert.assertEquals("3",
				agent.getInstrumentation().getProperty("nodesExpanded"));
		Assert.assertEquals("6",
				agent.getInstrumentation().getProperty("queueSize"));
		Assert.assertEquals("7",
				agent.getInstrumentation().getProperty("maxQueueSize"));
	}

	@Test
	public void testAIMA3eFigure3_23_using_GraphSearch() throws Exception {
		d_i_Map romaniaMap = new d_MapRomaniaRoadsSimplified();
		a_Problem problem = new a_Problem(d_MapRomaniaRoadsSimplified.ARAD,
				aa_ActionsFunctionMap.getActionsFunction(romaniaMap),
				aa_ActionsFunctionMap.getResultFunction(), new a_GoalTestDefault(
						d_MapRomaniaRoadsSimplified.BUCHAREST),
				new aa_CostFunctionMapStep(romaniaMap));

		das_i_SearchProblem search = new das_TreeBestFirstGreedy(new dag_GraphSearch(),
				new apm_HeuristicAdaptableStraightLineDistance(
						d_MapRomaniaRoadsSimplified.BUCHAREST, romaniaMap));
		aa_AgentSearch agent = new aa_AgentSearch(problem, search);
		Assert.assertEquals(
				"[aa_i_Action[name==moveTo, location==Sibiu], aa_i_Action[name==moveTo, location==Fagaras], aa_i_Action[name==moveTo, location==Bucharest]]",
				agent.getActions().toString());
		Assert.assertEquals(3, agent.getActions().size());
		Assert.assertEquals("3",
				agent.getInstrumentation().getProperty("nodesExpanded"));
		Assert.assertEquals("4",
				agent.getInstrumentation().getProperty("queueSize"));
		Assert.assertEquals("5",
				agent.getInstrumentation().getProperty("maxQueueSize"));
	}
}
