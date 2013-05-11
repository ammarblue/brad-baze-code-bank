package com.software.reuze;

//package aima.core.probability;

//import aima.core.probability.proposition.Proposition;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 484.<br>
 * <br>
 * A probability model on a discrete, countable set of worlds. The proper
 * treatment of the continuous case brings in certain complications that are
 * less relevant for most purposes in AI.
 * 
 * @author Ciaran O'Reilly
 */
public interface mp_i_ModelFinite extends mp_i_Model {

	/**
	 * <b>P</b>(X,...)<br>
	 * 
	 * @param phi
	 *            the propositions of interest.
	 * @return all the possible values of the propositions &phi;. This is a
	 *         Vector of numbers, where we assume a predefined ordering of the
	 *         domain of the relevant random variables.
	 */
	mp_i_CategoricalDistributionIterator priorDistribution(mp_i_Proposition... phi);

	/**
	 * Get a conditional distribution. Example:<br>
	 * <br>
	 * <b>P</b>(X | Y) gives the values of P(X = x<sub>i</sub> | Y =
	 * y<sub>j</sub>) for each possible i, j pair.
	 * 
	 * @param phi
	 *            the proposition for which a probability distribution is to be
	 *            returned.
	 * @param evidence
	 *            information we already have.
	 * @return the conditional distribution for <b>P</b>(&phi; | evidence).
	 */
	mp_i_CategoricalDistributionIterator posteriorDistribution(mp_i_Proposition phi,
			mp_i_Proposition... evidence);

	/**
	 * Get a distribution on multiple variables. Example, the product rule:<br>
	 * <br>
	 * <b>P</b>(X, Y) gives the values of P(X = x<sub>i</sub> | Y =
	 * y<sub>j</sub>)P(Y = y<sub>j</sub>) for each possible i, j pair.
	 * 
	 * @param propositions
	 *            the propositions for which a joint probability distribution is
	 *            to be returned.
	 * @return the joint distribution for <b>P</b>(X, Y, ...).
	 */
	mp_i_CategoricalDistributionIterator jointDistribution(mp_i_Proposition... propositions);
}
