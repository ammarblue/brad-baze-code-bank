package com.software.reuze;
//package aima.core.probability.temporal;

import java.util.List;


//import aima.core.probability.CategoricalDistribution;
//import aima.core.probability.proposition.AssignmentProposition;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 576.<br>
 * <br>
 * 
 * Generic interface for calling different implementations of the
 * forward-backward algorithm for smoothing: computing posterior probabilities
 * of a sequence of states given a sequence of observations.
 * 
 * 
 * @author Ciaran O'Reilly
 * 
 */
public interface mpt_i_InferenceForwardBackward extends mpt_i_InferenceForwardStep,
		mpt_InferenceBackwardStep {

	/**
	 * The forward-backward algorithm for smoothing: computing posterior
	 * probabilities of a sequence of states given a sequence of observations.
	 * 
	 * @param ev
	 *            a vector of evidence values for steps 1,...,t
	 * @param prior
	 *            the prior distribution on the initial state,
	 *            <b>P</b>(X<sub>0</sub>)
	 * @return a vector of smoothed estimates for steps 1,...,t
	 */
	List<mp_i_CategoricalDistributionIterator> forwardBackward(
			List<List<mp_PropositionTermOpsAssignment>> ev, mp_i_CategoricalDistributionIterator prior);
}
