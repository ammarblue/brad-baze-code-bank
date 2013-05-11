package com.software.reuze;

//package aima.core.probability.bayes;

//import aima.core.probability.CategoricalDistribution;
//import aima.core.probability.RandomVariable;
//import aima.core.probability.proposition.AssignmentProposition;

/**
 * General interface to be implemented by Bayesian Inference algorithms.
 * 
 * @author Ciaran O'Reilly
 */
public interface mpb_i_Inference {
	/**
	 * @param X
	 *            the query variables.
	 * @param observedEvidence
	 *            observed values for variables E.
	 * @param bn
	 *            a Bayes net with variables {X} &cup; E &cup; Y /* Y = hidden
	 *            variables
	 * @return a distribution over the query variables.
	 */
	mp_i_CategoricalDistributionIterator ask(final mp_i_RandomVariable[] X,
			final mp_PropositionTermOpsAssignment[] observedEvidence,
			final mpb_i_Network bn);
}
