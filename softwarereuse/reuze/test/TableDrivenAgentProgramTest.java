package reuze.test;
//package aima.test.core.unit.agent.impl.aprog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.software.reuze.aa_ActionDynamic;
import com.software.reuze.aa_ActionDynamicNoOp;
import com.software.reuze.aa_AgentMock;
import com.software.reuze.aa_AgentProgramTableDriven;
import com.software.reuze.aa_PerceptWithDynamicAttributes;
import com.software.reuze.aa_a_Agent;
import com.software.reuze.aa_i_Action;
import com.software.reuze.aa_i_Percept;


/**
 * @author Ciaran O'Reilly
 * 
 */
public class TableDrivenAgentProgramTest {

	private static final aa_i_Action ACTION_1 = new aa_ActionDynamic("action1");
	private static final aa_i_Action ACTION_2 = new aa_ActionDynamic("action2");
	private static final aa_i_Action ACTION_3 = new aa_ActionDynamic("action3");

	private aa_a_Agent agent;

	@Before
	public void setUp() {
		Map<List<aa_i_Percept>, aa_i_Action> perceptSequenceActions = new HashMap<List<aa_i_Percept>, aa_i_Action>();
		perceptSequenceActions.put(createPerceptSequence(new aa_PerceptWithDynamicAttributes(
				"key1", "value1")), ACTION_1);
		perceptSequenceActions.put(
				createPerceptSequence(new aa_PerceptWithDynamicAttributes("key1", "value1"),
						new aa_PerceptWithDynamicAttributes("key1", "value2")), ACTION_2);
		perceptSequenceActions.put(
				createPerceptSequence(new aa_PerceptWithDynamicAttributes("key1", "value1"),
						new aa_PerceptWithDynamicAttributes("key1", "value2"),
						new aa_PerceptWithDynamicAttributes("key1", "value3")), ACTION_3);

		agent = new aa_AgentMock(new aa_AgentProgramTableDriven(
				perceptSequenceActions));
	}

	@Test
	public void testExistingSequences() {
		Assert.assertEquals(ACTION_1,
				agent.execute(new aa_PerceptWithDynamicAttributes("key1", "value1")));
		Assert.assertEquals(ACTION_2,
				agent.execute(new aa_PerceptWithDynamicAttributes("key1", "value2")));
		Assert.assertEquals(ACTION_3,
				agent.execute(new aa_PerceptWithDynamicAttributes("key1", "value3")));
	}

	@Test
	public void testNonExistingSequence() {
		Assert.assertEquals(ACTION_1,
				agent.execute(new aa_PerceptWithDynamicAttributes("key1", "value1")));
		Assert.assertEquals(aa_ActionDynamicNoOp.NO_OP,
				agent.execute(new aa_PerceptWithDynamicAttributes("key1", "value3")));
	}

	private static List<aa_i_Percept> createPerceptSequence(aa_i_Percept... percepts) {
		List<aa_i_Percept> perceptSequence = new ArrayList<aa_i_Percept>();

		for (aa_i_Percept p : percepts) {
			perceptSequence.add(p);
		}

		return perceptSequence;
	}
}
