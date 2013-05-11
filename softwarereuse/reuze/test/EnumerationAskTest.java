package reuze.test;
//package aima.test.core.unit.probability.bayes.exact;

import org.junit.Before;

import com.software.reuze.mpb_InferenceEnumeration;


//import aima.core.probability.bayes.exact.EnumerationAsk;

/**
 * 
 * @author Ciaran O'Reilly
 * @author Ravi Mohan
 */
public class EnumerationAskTest extends BayesianInferenceTest {

	@Before
	public void setUp() {
		bayesInference = new mpb_InferenceEnumeration();
	}
}
