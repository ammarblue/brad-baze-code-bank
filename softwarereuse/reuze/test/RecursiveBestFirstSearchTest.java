package reuze.test;
//package aima.test.core.unit.search.informed;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.software.reuze.aa_EnvironmentMap;
import com.software.reuze.aa_SimpleProblemSolvingMap;
import com.software.reuze.aa_TreeSearchBestFirstSearchRecursive;
import com.software.reuze.aa_i_Action;
import com.software.reuze.aa_i_Agent;
import com.software.reuze.aa_i_EnvironmentState;
import com.software.reuze.aa_i_EnvironmentView;
import com.software.reuze.d_MapRomaniaRoadsSimplified;
import com.software.reuze.d_i_Map;
import com.software.reuze.das_TreeAstarEvaluationFunction;
import com.software.reuze.ga_Point;
import com.software.reuze.m_i_HeuristicFunction;


/**
 * @author Ciaran O'Reilly
 * 
 */
public class RecursiveBestFirstSearchTest {

	StringBuffer envChanges;

	d_i_Map aMap;

	aa_TreeSearchBestFirstSearchRecursive recursiveBestFirstSearch;

	@Before
	public void setUp() {
		envChanges = new StringBuffer();

		aMap = new d_MapRomaniaRoadsSimplified();

		m_i_HeuristicFunction heuristicFunction = new m_i_HeuristicFunction() {
			public double h(Object state) {
				ga_Point pt1 = aMap.getPosition((String) state);
				ga_Point pt2 = aMap
						.getPosition(d_MapRomaniaRoadsSimplified.BUCHAREST);
				return pt1.distance(pt2);
			}
		};

		recursiveBestFirstSearch = new aa_TreeSearchBestFirstSearchRecursive(
				new das_TreeAstarEvaluationFunction(heuristicFunction));
	}

	@Test
	public void testStartingAtGoal() {
		aa_EnvironmentMap me = new aa_EnvironmentMap(aMap);
		aa_SimpleProblemSolvingMap ma = new aa_SimpleProblemSolvingMap(me.getMap(), me, recursiveBestFirstSearch,
				new String[] { d_MapRomaniaRoadsSimplified.BUCHAREST });

		me.addAgent(ma, d_MapRomaniaRoadsSimplified.BUCHAREST);
		me.addEnvironmentView(new TestEnvironmentView());
		me.stepUntilDone();

		Assert.assertEquals(
				"CurrentLocation=In(Bucharest), Goal=In(Bucharest):aa_i_Action[name==NoOp]:METRIC[pathCost]=0.0:METRIC[maxRecursiveDepth]=0:METRIC[nodesExpanded]=0:aa_i_Action[name==NoOp]:",
				envChanges.toString());
	}

	@Test
	public void testAIMA3eFigure3_27() {
		aa_EnvironmentMap me = new aa_EnvironmentMap(aMap);
		aa_SimpleProblemSolvingMap ma = new aa_SimpleProblemSolvingMap(me.getMap(), me, recursiveBestFirstSearch,
				new String[] { d_MapRomaniaRoadsSimplified.BUCHAREST });

		me.addAgent(ma, d_MapRomaniaRoadsSimplified.ARAD);
		me.addEnvironmentView(new TestEnvironmentView());
		me.stepUntilDone();

		Assert.assertEquals(
				"CurrentLocation=In(Arad), Goal=In(Bucharest):aa_i_Action[name==moveTo, location==Sibiu]:aa_i_Action[name==moveTo, location==RimnicuVilcea]:aa_i_Action[name==moveTo, location==Pitesti]:aa_i_Action[name==moveTo, location==Bucharest]:METRIC[pathCost]=418.0:METRIC[maxRecursiveDepth]=4:METRIC[nodesExpanded]=6:aa_i_Action[name==NoOp]:",
				envChanges.toString());
	}

	private class TestEnvironmentView implements aa_i_EnvironmentView {
		public void notify(String msg) {
			envChanges.append(msg).append(":");
		}

		public void agentAdded(aa_i_Agent agent, aa_i_EnvironmentState state) {
			// Nothing.
		}

		public void agentActed(aa_i_Agent agent, aa_i_Action action,
				aa_i_EnvironmentState state) {
			envChanges.append(action).append(":");
		}
	}
}
