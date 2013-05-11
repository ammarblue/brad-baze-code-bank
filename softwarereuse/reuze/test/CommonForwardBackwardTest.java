package reuze.test;
//package aima.test.core.unit.probability.temporal;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;

import com.software.reuze.mp_ProbabilityTable;
import com.software.reuze.mp_PropositionTermOpsAssignment;
import com.software.reuze.mp_i_CategoricalDistributionIterator;
import com.software.reuze.mpt_InferenceBackwardStep;
import com.software.reuze.mpt_i_InferenceForwardBackward;
import com.software.reuze.mpt_i_InferenceForwardStep;


/*import aima.core.probability.CategoricalDistribution;
import aima.core.probability.example.ExampleRV;
import aima.core.probability.proposition.AssignmentProposition;
import aima.core.probability.temporal.BackwardStepInference;
import aima.core.probability.temporal.ForwardBackwardInference;
import aima.core.probability.temporal.ForwardStepInference;
import aima.core.probability.util.ProbabilityTable;*/

/**
 * 
 * @author Ciaran O'Reilly
 */
public abstract class CommonForwardBackwardTest {
	public static final double DELTA_THRESHOLD = 1e-3;

	//
	// PROTECTED METHODS
	//
	protected void testForwardStep_UmbrellaWorld(mpt_i_InferenceForwardStep uw) {
		// AIMA3e pg. 572
		// Day 0, no observations only the security guards prior beliefs
		// P(R<sub>0</sub>) = <0.5, 0.5>
		mp_i_CategoricalDistributionIterator prior = new mp_ProbabilityTable(new double[] {
				0.5, 0.5 }, ExampleRV.RAIN_t_RV);

		// Day 1, the umbrella appears, so U<sub>1</sub> = true.
		// &asymp; <0.818, 0.182>
		List<mp_PropositionTermOpsAssignment> e1 = new ArrayList<mp_PropositionTermOpsAssignment>();
		e1
				.add(new mp_PropositionTermOpsAssignment(ExampleRV.UMBREALLA_t_RV,
						Boolean.TRUE));
		mp_i_CategoricalDistributionIterator f1 = uw.forward(prior, e1);
		Assert.assertArrayEquals(new double[] { 0.818, 0.182 }, f1.getValues(),
				DELTA_THRESHOLD);

		// Day 2, the umbrella appears, so U<sub>2</sub> = true.
		// &asymp; <0.883, 0.117>
		List<mp_PropositionTermOpsAssignment> e2 = new ArrayList<mp_PropositionTermOpsAssignment>();
		e2
				.add(new mp_PropositionTermOpsAssignment(ExampleRV.UMBREALLA_t_RV,
						Boolean.TRUE));
		mp_i_CategoricalDistributionIterator f2 = uw.forward(f1, e2);
		Assert.assertArrayEquals(new double[] { 0.883, 0.117 }, f2.getValues(),
				DELTA_THRESHOLD);
	}

	protected void testBackwardStep_UmbrellaWorld(mpt_InferenceBackwardStep uw) {
		// AIMA3e pg. 575
		mp_i_CategoricalDistributionIterator b_kp2t = new mp_ProbabilityTable(new double[] {
				1.0, 1.0 }, ExampleRV.RAIN_t_RV);
		List<mp_PropositionTermOpsAssignment> e2 = new ArrayList<mp_PropositionTermOpsAssignment>();
		e2
				.add(new mp_PropositionTermOpsAssignment(ExampleRV.UMBREALLA_t_RV,
						Boolean.TRUE));
		mp_i_CategoricalDistributionIterator b1 = uw.backward(b_kp2t, e2);
		Assert.assertArrayEquals(new double[] { 0.69, 0.41 }, b1.getValues(),
				DELTA_THRESHOLD);
	}

	protected void testForwardBackward_UmbrellaWorld(mpt_i_InferenceForwardBackward uw) {
		// AIMA3e pg. 572
		// Day 0, no observations only the security guards prior beliefs
		// P(R<sub>0</sub>) = <0.5, 0.5>
		mp_i_CategoricalDistributionIterator prior = new mp_ProbabilityTable(new double[] {
				0.5, 0.5 }, ExampleRV.RAIN_t_RV);

		// Day 1
		List<List<mp_PropositionTermOpsAssignment>> evidence = new ArrayList<List<mp_PropositionTermOpsAssignment>>();
		List<mp_PropositionTermOpsAssignment> e1 = new ArrayList<mp_PropositionTermOpsAssignment>();
		e1
				.add(new mp_PropositionTermOpsAssignment(ExampleRV.UMBREALLA_t_RV,
						Boolean.TRUE));
		evidence.add(e1);

		List<mp_i_CategoricalDistributionIterator> smoothed = uw.forwardBackward(evidence,
				prior);

		Assert.assertEquals(1, smoothed.size());
		Assert.assertArrayEquals(new double[] { 0.818, 0.182 }, smoothed.get(0)
				.getValues(), DELTA_THRESHOLD);

		// Day 2
		List<mp_PropositionTermOpsAssignment> e2 = new ArrayList<mp_PropositionTermOpsAssignment>();
		e2
				.add(new mp_PropositionTermOpsAssignment(ExampleRV.UMBREALLA_t_RV,
						Boolean.TRUE));
		evidence.add(e2);

		smoothed = uw.forwardBackward(evidence, prior);

		Assert.assertEquals(2, smoothed.size());
		Assert.assertArrayEquals(new double[] { 0.883, 0.117 }, smoothed.get(0)
				.getValues(), DELTA_THRESHOLD);
		Assert.assertArrayEquals(new double[] { 0.883, 0.117 }, smoothed.get(1)
				.getValues(), DELTA_THRESHOLD);

		// Day 3
		List<mp_PropositionTermOpsAssignment> e3 = new ArrayList<mp_PropositionTermOpsAssignment>();
		e3.add(new mp_PropositionTermOpsAssignment(ExampleRV.UMBREALLA_t_RV,
				Boolean.FALSE));
		evidence.add(e3);

		smoothed = uw.forwardBackward(evidence, prior);

		Assert.assertEquals(3, smoothed.size());
		Assert.assertArrayEquals(new double[] { 0.861, 0.138 }, smoothed.get(0)
				.getValues(), DELTA_THRESHOLD);
		Assert.assertArrayEquals(new double[] { 0.799, 0.201 }, smoothed.get(1)
				.getValues(), DELTA_THRESHOLD);
		Assert.assertArrayEquals(new double[] { 0.190, 0.810 }, smoothed.get(2)
				.getValues(), DELTA_THRESHOLD);
	}
}
