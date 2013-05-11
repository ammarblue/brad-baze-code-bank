package reuze.test;
//package aima.test.core.unit.probability.bayes.approx;

import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.software.reuze.m_RandomMock;
import com.software.reuze.mp_i_RandomVariable;
import com.software.reuze.mpb_PriorSample;
import com.software.reuze.mpb_i_Network;


/*import aima.core.probability.RandomVariable;
import aima.core.probability.bayes.BayesianNetwork;
import aima.core.probability.bayes.approx.PriorSample;
import aima.core.probability.example.BayesNetExampleFactory;
import aima.core.probability.example.ExampleRV;
import aima.core.util.MockRandomizer;*/

/**
 * 
 * @author Ciaran O'Reilly
 * @author Ravi Mohan
 */
public class PriorSampleTest {

	@Test
	public void testPriorSample_basic() {
		// AIMA3e pg. 530
		mpb_i_Network bn = BayesNetExampleFactory
				.constructCloudySprinklerRainWetGrassNetwork();
		m_RandomMock r = new m_RandomMock(
				new double[] { 0.5, 0.5, 0.5, 0.5 });

		mpb_PriorSample ps = new mpb_PriorSample(r);
		Map<mp_i_RandomVariable, Object> event = ps.priorSample(bn);

		Assert.assertEquals(4, event.keySet().size());
		Assert.assertEquals(Boolean.TRUE, event.get(ExampleRV.CLOUDY_RV));
		Assert.assertEquals(Boolean.FALSE, event.get(ExampleRV.SPRINKLER_RV));
		Assert.assertEquals(Boolean.TRUE, event.get(ExampleRV.RAIN_RV));
		Assert.assertEquals(Boolean.TRUE, event.get(ExampleRV.WET_GRASS_RV));
	}
}
