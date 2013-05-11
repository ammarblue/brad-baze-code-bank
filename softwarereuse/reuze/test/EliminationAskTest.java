package reuze.test;
//package aima.test.core.unit.probability.bayes.exact;

import org.junit.Before;

import com.software.reuze.mpb_InferenceEliminationAsk;


//import aima.core.probability.bayes.exact.EliminationAsk;

/**
 * 
 * @author Ciaran O'Reilly
 */
public class EliminationAskTest extends BayesianInferenceTest {

	@Before
	public void setUp() {
		bayesInference = new mpb_InferenceEliminationAsk();
	}
}
