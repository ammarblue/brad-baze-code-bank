package reuze.test;
//package aima.test.core.unit.probability.hmm.exact;

import org.junit.Before;
import org.junit.Test;

import com.software.reuze.mp_HMMInferenceForwardBackwardConstantSpace;


//import aima.core.probability.example.HMMExampleFactory;
//import aima.core.probability.hmm.exact.HMMForwardBackwardConstantSpace;
//import aima.test.core.unit.probability.temporal.CommonForwardBackwardTest;

/**
 * 
 * @author Ciaran O'Reilly
 *
 */
public class HMMForwardBackwardConstantSpaceTest extends
		CommonForwardBackwardTest {

	//
	private mp_HMMInferenceForwardBackwardConstantSpace uw = null;

	@Before
	public void setUp() {
		uw = new mp_HMMInferenceForwardBackwardConstantSpace(
				HMMExampleFactory.getUmbrellaWorldModel());
	}

	@Test
	public void testForwardBackward_UmbrellaWorld() {
		super.testForwardBackward_UmbrellaWorld(uw);
	}
}
