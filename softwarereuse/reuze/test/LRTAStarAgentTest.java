package reuze.test;
//package aima.test.core.unit.search.online;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.software.reuze.a_GoalTestDefault;
import com.software.reuze.aa_ActionsFunctionMap;
import com.software.reuze.aa_AgentLRTAStar;
import com.software.reuze.aa_CostFunctionMapStep;
import com.software.reuze.aa_EnvironmentMap;
import com.software.reuze.aa_SearchProblemOnline;
import com.software.reuze.aa_i_Action;
import com.software.reuze.aa_i_Agent;
import com.software.reuze.aa_i_EnvironmentState;
import com.software.reuze.aa_i_EnvironmentView;
import com.software.reuze.d_MapExtendable;
import com.software.reuze.m_i_HeuristicFunction;


public class LRTAStarAgentTest {
	d_MapExtendable aMap;

	StringBuffer envChanges;

	m_i_HeuristicFunction hf;

	@Before
	public void setUp() {
		aMap = new d_MapExtendable();
		aMap.addBidirectionalLink("A", "B", 4.0);
		aMap.addBidirectionalLink("B", "C", 4.0);
		aMap.addBidirectionalLink("C", "D", 4.0);
		aMap.addBidirectionalLink("D", "E", 4.0);
		aMap.addBidirectionalLink("E", "F", 4.0);
		hf = new m_i_HeuristicFunction() {
			public double h(Object state) {
				return 1;
			}
		};

		envChanges = new StringBuffer();
	}

	@Test
	public void testAlreadyAtGoal() {
		aa_EnvironmentMap me = new aa_EnvironmentMap(aMap);
		aa_AgentLRTAStar agent = new aa_AgentLRTAStar(new aa_SearchProblemOnline(
				aa_ActionsFunctionMap.getActionsFunction(aMap),
				new a_GoalTestDefault("A"), new aa_CostFunctionMapStep(aMap)),
				aa_ActionsFunctionMap.getPerceptToStateFunction(), hf);
		me.addAgent(agent, "A");
		me.addEnvironmentView(new TestEnvironmentView());
		me.stepUntilDone();

		Assert.assertEquals("aa_i_Action[name==NoOp]->", envChanges.toString());
	}

	@Test
	public void testNormalSearch() {
		aa_EnvironmentMap me = new aa_EnvironmentMap(aMap);
		aa_AgentLRTAStar agent = new aa_AgentLRTAStar(new aa_SearchProblemOnline(
				aa_ActionsFunctionMap.getActionsFunction(aMap),
				new a_GoalTestDefault("F"), new aa_CostFunctionMapStep(aMap)),
				aa_ActionsFunctionMap.getPerceptToStateFunction(), hf);
		me.addAgent(agent, "A");
		me.addEnvironmentView(new TestEnvironmentView());
		me.stepUntilDone();

		Assert.assertEquals(
				"aa_i_Action[name==moveTo, location==B]->aa_i_Action[name==moveTo, location==A]->aa_i_Action[name==moveTo, location==B]->aa_i_Action[name==moveTo, location==C]->aa_i_Action[name==moveTo, location==B]->aa_i_Action[name==moveTo, location==C]->aa_i_Action[name==moveTo, location==D]->aa_i_Action[name==moveTo, location==C]->aa_i_Action[name==moveTo, location==D]->aa_i_Action[name==moveTo, location==E]->aa_i_Action[name==moveTo, location==D]->aa_i_Action[name==moveTo, location==E]->aa_i_Action[name==moveTo, location==F]->aa_i_Action[name==NoOp]->",
				envChanges.toString());
	}

	@Test
	public void testNoPath() {
		aa_EnvironmentMap me = new aa_EnvironmentMap(aMap);
		aa_AgentLRTAStar agent = new aa_AgentLRTAStar(new aa_SearchProblemOnline(
				aa_ActionsFunctionMap.getActionsFunction(aMap),
				new a_GoalTestDefault("G"), new aa_CostFunctionMapStep(aMap)),
				aa_ActionsFunctionMap.getPerceptToStateFunction(), hf);
		me.addAgent(agent, "A");
		me.addEnvironmentView(new TestEnvironmentView());
		// Note: Will search forever if no path is possible,
		// Therefore restrict the number of steps to something
		// reasonablbe, against which to test.
		me.step(14);

		Assert.assertEquals(
				"aa_i_Action[name==moveTo, location==B]->aa_i_Action[name==moveTo, location==A]->aa_i_Action[name==moveTo, location==B]->aa_i_Action[name==moveTo, location==C]->aa_i_Action[name==moveTo, location==B]->aa_i_Action[name==moveTo, location==C]->aa_i_Action[name==moveTo, location==D]->aa_i_Action[name==moveTo, location==C]->aa_i_Action[name==moveTo, location==D]->aa_i_Action[name==moveTo, location==E]->aa_i_Action[name==moveTo, location==D]->aa_i_Action[name==moveTo, location==E]->aa_i_Action[name==moveTo, location==F]->aa_i_Action[name==moveTo, location==E]->",
				envChanges.toString());
	}

	private class TestEnvironmentView implements aa_i_EnvironmentView {
		public void notify(String msg) {
			envChanges.append(msg).append("->");
		}

		public void agentAdded(aa_i_Agent agent, aa_i_EnvironmentState state) {
			// Nothing.
		}

		public void agentActed(aa_i_Agent agent, aa_i_Action action,
				aa_i_EnvironmentState state) {
			envChanges.append(action).append("->");
		}
	}
}
