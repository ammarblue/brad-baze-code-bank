package com.software.reuze;

//package aima.core.probability.bayes.approx;

/*import aima.core.probability.CategoricalDistribution;
import aima.core.probability.RandomVariable;
import aima.core.probability.bayes.BayesianNetwork;
import aima.core.probability.proposition.AssignmentProposition;*/

/**
 * General interface to be implemented by approximate Bayesian Inference
 * algorithms.
 * 
 * @author Ciaran O'Reilly
 */
public interface mpb_i_InferenceSample {
	/**
	 * @param X
	 *            the query variables.
	 * @param observedEvidence
	 *            observed values for variables E.
	 * @param bn
	 *            a Bayes net with variables {X} &cup; E &cup; Y /* Y = hidden
	 *            variables
	 * @param N
	 *            the total number of samples to be generated
	 * @return an estimate of <b>P</b>(X|e).
	 */
	mp_i_CategoricalDistributionIterator ask(final mp_i_RandomVariable[] X,
			final mp_PropositionTermOpsAssignment[] observedEvidence,
			final mpb_i_Network bn, int N);
}
