package com.software.reuze;
//package aima.core.probability.bayes.approx;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;


/*import aima.core.probability.CategoricalDistribution;
import aima.core.probability.RandomVariable;
import aima.core.probability.bayes.BayesianNetwork;
import aima.core.probability.proposition.AssignmentProposition;
import aima.core.probability.util.ProbUtil;
import aima.core.probability.util.ProbabilityTable;
import aima.core.util.JavaRandomizer;
import aima.core.util.Randomizer;
import aima.core.util.datastructure.TwoTuple;*/

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 534.<br>
 * <br>
 * 
 * <pre>
 * function LIKELIHOOD-WEIGHTING(X, e, bn, N) returns an estimate of <b>P</b>(X|e)
 *   inputs: X, the query variable
 *           e, observed values for variables E
 *           bn, a Bayesian network specifying joint distribution <b>P</b>(X<sub>1</sub>,...,X<sub>n</sub>)
 *           N, the total number of samples to be generated
 *   local variables: W, a vector of weighted counts for each value of X, initially zero
 *   
 *   for j = 1 to N do
 *       <b>x</b>,w <- WEIGHTED-SAMPLE(bn,e)
 *       W[x] <- W[x] + w where x is the value of X in <b>x</b>
 *   return NORMALIZE(W)
 * --------------------------------------------------------------------------------------
 * function WEIGHTED-SAMPLE(bn, e) returns an event and a weight
 *   
 *    w <- 1; <b>x</b> <- an event with n elements initialized from e
 *    foreach variable X<sub>i</sub> in X<sub>1</sub>,...,X<sub>n</sub> do
 *        if X<sub>i</sub> is an evidence variable with value x<sub>i</sub> in e
 *            then w <- w * P(X<sub>i</sub> = x<sub>i</sub> | parents(X<sub>i</sub>))
 *            else <b>x</b>[i] <- a random sample from <b>P</b>(X<sub>i</sub> | parents(X<sub>i</sub>))
 *    return <b>x</b>, w
 * </pre>
 * 
 * Figure 14.15 The likelihood-weighting algorithm for inference in Bayesian
 * networks. In WEIGHTED-SAMPLE, each nonevidence variable is sampled according
 * to the conditional distribution given the values already sampled for the
 * variable's parents, while a weight is accumulated based on the likelihood for
 * each evidence variable.<br>
 * <br>
 * <b>Note:</b> The implementation has been extended to handle queries with
 * multiple variables. <br>
 * 
 * @author Ciaran O'Reilly
 * @author Ravi Mohan
 */
public class mpb_InferenceSampleLikelihoodWeighting implements mpb_i_InferenceSample {
	private m_i_Random randomizer = null;

	public mpb_InferenceSampleLikelihoodWeighting() {
		this(new m_RandomJava(new Random()));
	}

	public mpb_InferenceSampleLikelihoodWeighting(m_i_Random r) {
		this.randomizer = r;
	}

	// function LIKELIHOOD-WEIGHTING(X, e, bn, N) returns an estimate of
	// <b>P</b>(X|e)
	/**
	 * The LIKELIHOOD-WEIGHTING algorithm in Figure 14.15. For answering queries
	 * given evidence in a Bayesian Network.
	 * 
	 * @param X
	 *            the query variables
	 * @param e
	 *            observed values for variables E
	 * @param bn
	 *            a Bayesian network specifying joint distribution
	 *            <b>P</b>(X<sub>1</sub>,...,X<sub>n</sub>)
	 * @param N
	 *            the total number of samples to be generated
	 * @return an estimate of <b>P</b>(X|e)
	 */
	public mp_i_CategoricalDistributionIterator likelihoodWeighting(mp_i_RandomVariable[] X,
			mp_PropositionTermOpsAssignment[] e, mpb_i_Network bn, int N) {
		// local variables: W, a vector of weighted counts for each value of X,
		// initially zero
		double[] W = new double[mp_ProbUtil
				.expectedSizeOfCategoricalDistribution(X)];

		// for j = 1 to N do
		for (int j = 0; j < N; j++) {
			// <b>x</b>,w <- WEIGHTED-SAMPLE(bn,e)
			d_TwoTuple<Map<mp_i_RandomVariable, Object>, Double> x_w = weightedSample(bn,
					e);
			// W[x] <- W[x] + w where x is the value of X in <b>x</b>
			W[mp_ProbUtil.indexOf(X, x_w.getFirst())] += x_w.getSecond();
		}
		// return NORMALIZE(W)
		return new mp_ProbabilityTable(W, X).normalize();
	}

	// function WEIGHTED-SAMPLE(bn, e) returns an event and a weight
	/**
	 * The WEIGHTED-SAMPLE function in Figure 14.15.
	 * 
	 * @param e
	 *            observed values for variables E
	 * @param bn
	 *            a Bayesian network specifying joint distribution
	 *            <b>P</b>(X<sub>1</sub>,...,X<sub>n</sub>)
	 * @return return <b>x</b>, w - an event with its associated weight.
	 */
	public d_TwoTuple<Map<mp_i_RandomVariable, Object>, Double> weightedSample(
			mpb_i_Network bn, mp_PropositionTermOpsAssignment[] e) {
		// w <- 1;
		double w = 1.0;
		// <b>x</b> <- an event with n elements initialized from e
		Map<mp_i_RandomVariable, Object> x = new LinkedHashMap<mp_i_RandomVariable, Object>();
		for (mp_PropositionTermOpsAssignment ap : e) {
			x.put(ap.getTermVariable(), ap.getValue());
		}

		// foreach variable X<sub>i</sub> in X<sub>1</sub>,...,X<sub>n</sub> do
		for (mp_i_RandomVariable Xi : bn.getVariablesInTopologicalOrder()) {
			// if X<sub>i</sub> is an evidence variable with value x<sub>i</sub>
			// in e
			if (x.containsKey(Xi)) {
				// then w <- w * P(X<sub>i</sub> = x<sub>i</sub> |
				// parents(X<sub>i</sub>))
				w *= bn.getNode(Xi)
						.getCPD()
						.getValue(
								mp_ProbUtil.getEventValuesForXiGivenParents(
										bn.getNode(Xi), x));
			} else {
				// else <b>x</b>[i] <- a random sample from
				// <b>P</b>(X<sub>i</sub> | parents(X<sub>i</sub>))
				x.put(Xi, mp_ProbUtil.randomSample(bn.getNode(Xi), x, randomizer));
			}
		}
		// return <b>x</b>, w
		return new d_TwoTuple<Map<mp_i_RandomVariable, Object>, Double>(x, w);
	}

	//
	// START-BayesSampleInference
	public mp_i_CategoricalDistributionIterator ask(final mp_i_RandomVariable[] X,
			final mp_PropositionTermOpsAssignment[] observedEvidence,
			final mpb_i_Network bn, int N) {
		return likelihoodWeighting(X, observedEvidence, bn, N);
	}

	// END-BayesSampleInference
	//
}
