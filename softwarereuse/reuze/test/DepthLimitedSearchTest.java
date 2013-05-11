package reuze.test;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import reuze.pending.dg_NQueensFunctionFactory;

import com.software.reuze.a_Problem;
import com.software.reuze.aa_AgentSearch;
import com.software.reuze.aa_i_Action;
import com.software.reuze.das_SearchProblemDepthLimited;
import com.software.reuze.das_i_SearchProblem;
import com.software.reuze.dg_NQueensBoard;
import com.software.reuze.dg_NQueensGoalTest;

/*import aima.core.environment.nqueens.NQueensBoard;
import aima.core.environment.nqueens.NQueensFunctionFactory;
import aima.core.environment.nqueens.NQueensGoalTest;*/

public class DepthLimitedSearchTest {

	@Test
	public void testSuccesfulDepthLimitedSearch() throws Exception {
		a_Problem problem = new a_Problem(new dg_NQueensBoard(8),
				dg_NQueensFunctionFactory.getIActionsFunction(),
				dg_NQueensFunctionFactory.getResultFunction(),
				new dg_NQueensGoalTest());
		das_i_SearchProblem search = new das_SearchProblemDepthLimited(8);
		aa_AgentSearch agent = new aa_AgentSearch(problem, search);
		List<aa_i_Action> actions = agent.getActions();
		assertCorrectPlacement(actions);
		Assert.assertEquals("113",
				agent.getInstrumentation().getProperty("nodesExpanded"));
	}

	@Test
	public void testCutOff() throws Exception {
		a_Problem problem = new a_Problem(new dg_NQueensBoard(8),
				dg_NQueensFunctionFactory.getIActionsFunction(),
				dg_NQueensFunctionFactory.getResultFunction(),
				new dg_NQueensGoalTest());
		das_SearchProblemDepthLimited search = new das_SearchProblemDepthLimited(1);
		aa_AgentSearch agent = new aa_AgentSearch(problem, search);
		List<aa_i_Action> actions = agent.getActions();
		Assert.assertEquals(true, search.isCutOff(actions));
	}

	@Test
	public void testFailure() throws Exception {
		a_Problem problem = new a_Problem(new dg_NQueensBoard(3),
				dg_NQueensFunctionFactory.getIActionsFunction(),
				dg_NQueensFunctionFactory.getResultFunction(),
				new dg_NQueensGoalTest());
		das_SearchProblemDepthLimited search = new das_SearchProblemDepthLimited(5);
		aa_AgentSearch agent = new aa_AgentSearch(problem, search);
		List<aa_i_Action> actions = agent.getActions();
		Assert.assertEquals(true, search.isFailure(actions));
	}

	//
	// PRIVATE METHODS
	//
	private void assertCorrectPlacement(List<aa_i_Action> actions) {
		Assert.assertEquals(8, actions.size());
		Assert.assertEquals(
				"aa_i_Action[name==placeQueenAt, location== ( 0 , 0 ) ]", actions
						.get(0).toString());
		Assert.assertEquals(
				"aa_i_Action[name==placeQueenAt, location== ( 1 , 4 ) ]", actions
						.get(1).toString());
		Assert.assertEquals(
				"aa_i_Action[name==placeQueenAt, location== ( 2 , 7 ) ]", actions
						.get(2).toString());
		Assert.assertEquals(
				"aa_i_Action[name==placeQueenAt, location== ( 3 , 5 ) ]", actions
						.get(3).toString());
		Assert.assertEquals(
				"aa_i_Action[name==placeQueenAt, location== ( 4 , 2 ) ]", actions
						.get(4).toString());
		Assert.assertEquals(
				"aa_i_Action[name==placeQueenAt, location== ( 5 , 6 ) ]", actions
						.get(5).toString());
		Assert.assertEquals(
				"aa_i_Action[name==placeQueenAt, location== ( 6 , 1 ) ]", actions
						.get(6).toString());
		Assert.assertEquals(
				"aa_i_Action[name==placeQueenAt, location== ( 7 , 3 ) ]", actions
						.get(7).toString());
	}
}