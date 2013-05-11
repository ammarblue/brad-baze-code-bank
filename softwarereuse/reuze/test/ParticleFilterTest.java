package reuze.test;
//package aima.test.core.unit.probability.bayes.approx;

import org.junit.Assert;
import org.junit.Test;

import com.software.reuze.m_RandomMock;
import com.software.reuze.mp_PropositionTermOpsAssignment;
import com.software.reuze.mpb_ParticleFiltering;


/*import aima.core.probability.bayes.approx.ParticleFiltering;
import aima.core.probability.example.DynamicBayesNetExampleFactory;
import aima.core.probability.example.ExampleRV;
import aima.core.probability.proposition.AssignmentProposition;
import aima.core.util.MockRandomizer;*/

/**
 * @author Ciaran O'Reilly
 * @author Ravi Mohan
 */
public class ParticleFilterTest {

	@Test
	public void test_AIMA3e_Fig15_18() {
		m_RandomMock mr = new m_RandomMock(new double[] {
				// Prior Sample:
				// 8 with Rain_t-1=true from prior distribution
				0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5,
				// 2 with Rain_t-1=false from prior distribution
				0.6, 0.6,
				// (a) Propagate 6 samples Rain_t=true
				0.7, 0.7, 0.7, 0.7, 0.7, 0.7,
				// 4 samples Rain_t=false
				0.71, 0.71, 0.31, 0.31,
				// (b) Weight should be for first 6 samples:
				// Rain_t-1=true, Rain_t=true, Umbrella_t=false = 0.1
				// Next 2 samples:
				// Rain_t-1=true, Rain_t=false, Umbrealla_t=false= 0.8
				// Final 2 samples:
				// Rain_t-1=false, Rain_t=false, Umbrella_t=false = 0.8
				// gives W[] =
				// [0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.8, 0.8, 0.8, 0.8]
				// normalized =
				// [0.026, ...., 0.211, ....] is approx. 0.156 = true
				// the remainder is false
				// (c) Resample 2 Rain_t=true, 8 Rain_t=false
				0.15, 0.15, 0.2, 0.2, 0.2, 0.2, 0.2, 0.2, 0.2, 0.2,
				//
				// Next Sample:
				// (a) Propagate 1 samples Rain_t=true
				0.7,
				// 9 samples Rain_t=false
				0.71, 0.31, 0.31, 0.31, 0.31, 0.31, 0.31, 0.31, 0.31,
				// (c) resample 1 Rain_t=true, 9 Rain_t=false
				0.0001, 0.2, 0.2, 0.2, 0.2, 0.2, 0.2, 0.2, 0.2, 0.2 });

		int N = 10;
		mpb_ParticleFiltering pf = new mpb_ParticleFiltering(N,
				DynamicBayesNetExampleFactory.getUmbrellaWorldNetwork(), mr);

		mp_PropositionTermOpsAssignment[] e = new mp_PropositionTermOpsAssignment[] { new mp_PropositionTermOpsAssignment(
				ExampleRV.UMBREALLA_t_RV, false) };

		mp_PropositionTermOpsAssignment[][] S = pf.particleFiltering(e);

		Assert.assertEquals(N, S.length);
		for (int i = 0; i < N; i++) {
			Assert.assertEquals(1, S[i].length);
			mp_PropositionTermOpsAssignment ap = S[i][0];
			Assert.assertEquals(ExampleRV.RAIN_t_RV, ap.getTermVariable());
			if (i < 2) {
				Assert.assertEquals(true, ap.getValue());
			} else {
				Assert.assertEquals(false, ap.getValue());
			}
		}

		// Generate next sample to ensure everything roles forward ok
		// in this case with prefixed probabilities only expect 1 Rain_t=true
		S = pf.particleFiltering(e);
		Assert.assertEquals(N, S.length);
		for (int i = 0; i < N; i++) {
			Assert.assertEquals(1, S[i].length);
			mp_PropositionTermOpsAssignment ap = S[i][0];
			Assert.assertEquals(ExampleRV.RAIN_t_RV, ap.getTermVariable());
			if (i < 1) {
				Assert.assertEquals(true, ap.getValue());
			} else {
				Assert.assertEquals(false, ap.getValue());
			}
		}
	}
}
