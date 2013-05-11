package reuze.test;
//package aima.test.core.unit.search.local;

import org.junit.Assert;
import org.junit.Test;

import com.software.reuze.aa_TreeSearchSimulatedAnnealing;


public class SimulatedAnnealingSearchTest {

	@Test
	public void testForGivenNegativeDeltaEProbabilityOfAcceptanceDecreasesWithDecreasingTemperature() {
		// this isn't very nice. the object's state is uninitialized but is ok
		// for this test.
		aa_TreeSearchSimulatedAnnealing search = new aa_TreeSearchSimulatedAnnealing(null);
		int deltaE = -1;
		double higherTemperature = 30.0;
		double lowerTemperature = 29.5;

		Assert.assertTrue(search.probabilityOfAcceptance(lowerTemperature,
				deltaE) < search.probabilityOfAcceptance(higherTemperature,
				deltaE));
	}

}
