package com.software.reuze;

//package aima.core.probability.bayes;

/**
 * A node over a Random Variable that has a finite countable domain.
 * 
 * @author Ciaran O'Reilly
 * 
 */
public interface mpb_i_NodeDiscreteFinite extends mpb_i_NodeDiscrete {

	/**
	 * 
	 * @return the Conditional Probability Table detailing the finite set of
	 *         probabilities for this Node.
	 */
	mp_i_DistributionConditionalTable getCPT();
}
