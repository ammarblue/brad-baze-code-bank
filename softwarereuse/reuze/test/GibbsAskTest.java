package reuze.test;
//package aima.test.core.unit.probability.bayes.approx;

import org.junit.Assert;
import org.junit.Test;

import com.software.reuze.m_RandomMock;
import com.software.reuze.mp_PropositionTermOpsAssignment;
import com.software.reuze.mp_i_Model;
import com.software.reuze.mp_i_RandomVariable;
import com.software.reuze.mpb_InferenceSampleGibbsAsk;
import com.software.reuze.mpb_i_Network;


/*import aima.core.probability.ProbabilityModel;
import aima.core.probability.RandomVariable;
import aima.core.probability.bayes.BayesianNetwork;
import aima.core.probability.bayes.approx.GibbsAsk;
import aima.core.probability.example.BayesNetExampleFactory;
import aima.core.probability.example.ExampleRV;
import aima.core.probability.proposition.AssignmentProposition;
import aima.core.util.MockRandomizer;*/

/**
 * 
 * @author Ciaran O'Reilly
 * @author Ravi Mohan
 */
public class GibbsAskTest {
	public static final double DELTA_THRESHOLD = mp_i_Model.DEFAULT_ROUNDING_THRESHOLD;

	@Test
	public void testGibbsAsk_basic() {
		mpb_i_Network bn = BayesNetExampleFactory
				.constructCloudySprinklerRainWetGrassNetwork();
		mp_PropositionTermOpsAssignment[] e = new mp_PropositionTermOpsAssignment[] { new mp_PropositionTermOpsAssignment(
				ExampleRV.SPRINKLER_RV, Boolean.TRUE) };
		m_RandomMock r = new m_RandomMock(new double[] { 0.5, 0.5, 0.5,
				0.5, 0.5, 0.5, 0.6, 0.5, 0.5, 0.6, 0.5, 0.5 });

		mpb_InferenceSampleGibbsAsk ga = new mpb_InferenceSampleGibbsAsk(r);

		double[] estimate = ga.gibbsAsk(
				new mp_i_RandomVariable[] { ExampleRV.RAIN_RV }, e, bn, 3)
				.getValues();

		Assert.assertArrayEquals(new double[] { 0.3333333333333333,
				0.6666666666666666 }, estimate, DELTA_THRESHOLD);
	}
}
