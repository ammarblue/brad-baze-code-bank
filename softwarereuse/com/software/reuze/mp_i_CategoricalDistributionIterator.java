package com.software.reuze;
//package aima.core.probability;

import java.util.Map;


//import aima.core.probability.proposition.AssignmentProposition;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 487.<br>
 * <br>
 * A probability distribution for discrete random variables with a finite set of
 * values. <br>
 * <b>Note:</b> This definition corresponds to that given in AIMA3e pg. 487, for
 * a Probability Distribution.
 * 
 * @see <a href="http://en.wikipedia.org/wiki/Probability_distribution"
 *      >Probability Distribution</a>
 * 
 * @author Ciaran O'Reilly
 */
public interface mp_i_CategoricalDistributionIterator extends mp_i_ProbabilityMass {

	/**
	 * Interface to be implemented by an object/algorithm that wishes to iterate
	 * over the possible assignments for the random variables comprising this
	 * categorical distribution.
	 * 
	 * @see mp_i_CategoricalDistributionIterator#iterateOver(Iterator)
	 * @see mp_i_CategoricalDistributionIterator#iterateOver(Iterator,
	 *      mp_PropositionTermOpsAssignment...)
	 */
	public interface Iterator {
		/**
		 * Called for each possible assignment for the Random Variables
		 * comprising this CategoricalDistribution.
		 * 
		 * @param possibleAssignment
		 *            a possible assignment, &omega;, of variable/value pairs.
		 * @param probability
		 *            the probability associated with &omega;
		 */
		void iterate(Map<mp_i_RandomVariable, Object> possibleAssignment,
				double probability);
	}

	/**
	 * <b>Note:</b> Do not modify the double[] returned by this method directly.
	 * Instead use setValue() as this method is intended to be for read only
	 * purposes.
	 * 
	 * @return the double[] used to represent the CategoricalDistribution.
	 * 
	 * @see mp_i_CategoricalDistributionIterator#setValue(int, double)
	 */
	double[] getValues();

	/**
	 * Set the value at a specified index within the distribution.
	 * 
	 * @param idx
	 * @param value
	 */
	void setValue(int idx, double value);

	/**
	 * 
	 * @return the summation of all of the elements within the Distribution.
	 */
	double getSum();

	/**
	 * Normalize the values comprising this distribution.
	 * 
	 * @return this instance with its values normalized.
	 */
	mp_i_CategoricalDistributionIterator normalize();

	/**
	 * Retrieve the index into the CategoricalDistribution for the provided set
	 * of values for the random variables comprising the Distribution.
	 * 
	 * @param values
	 *            an ordered set of values for the random variables comprising
	 *            the Distribution (<b>Note:</b> the order must match the order
	 *            of the random variables describing the distribution)
	 * @return the index within the Distribution for the values specified.
	 * 
	 * @see mp_i_CategoricalDistributionIterator#getValues()
	 * @see mp_ProbabilityDistribution#getFor()
	 */
	int getIndex(Object... values);

	/**
	 * Get the marginal probability for the provided variables from this
	 * Distribution creating a new Distribution of the remaining variables with
	 * their values updated with the summed out random variables.<br>
	 * <br>
	 * see: AIMA3e page 492.<br>
	 * <br>
	 * 
	 * @param vars
	 *            the random variables to marginalize/sum out.
	 * @return a new Distribution containing any remaining random variables not
	 *         summed out and a new set of values updated with the summed out
	 *         values.
	 */
	mp_i_CategoricalDistributionIterator marginal(mp_i_RandomVariable... vars);

	/**
	 * Divide the dividend (this) CategoricalDistribution by the divisor to
	 * create a new CategoricalDistribution representing the quotient. The
	 * variables comprising the divisor distribution must be a subset of the
	 * dividend. However, ordering of variables does not matter as the quotient
	 * contains the same variables as the dividend and the internal
	 * implementation logic should handle iterating through the two
	 * distributions correctly, irrespective of the order of their variables.
	 * 
	 * @param divisor
	 * @return a new Distribution representing the quotient of the dividend
	 *         (this) divided by the divisor.
	 * @throws IllegalArgumentException
	 *             if the variables of the divisor distribution are not a subset
	 *             of the dividend.
	 */
	mp_i_CategoricalDistributionIterator divideBy(mp_i_CategoricalDistributionIterator divisor);

	/**
	 * Multiplication of this Distribution by a given multiplier, creating a new
	 * Distribution representing the product of the two. <b>Note:</b> Is
	 * equivalent to pointwise product calculation on factors.<br>
	 * <br>
	 * see: AIMA3e Figure 14.10 page 527.<br>
	 * <br>
	 * Note: Default Distribution multiplication is not commutative. The reason
	 * is because the order of the variables comprising a Distribution dictate
	 * the ordering of the values for that distribution. For example (the
	 * General case of Baye's rule, AIMA3e pg. 496), using this API method:<br>
	 * <br>
	 * <b>P</b>(Y | X) = (<b>P</b>(X | Y)<b>P</b>(Y))/<b>P</b>(X)<br>
	 * <br>
	 * is NOT true, due to multiplication of distributions not being
	 * commutative. However:<br>
	 * <br>
	 * <b>P</b>(Y | X) = (<b>P</b>(Y)<b>P</b>(X | Y))/<b>P</b>(X)<br>
	 * <br>
	 * is true, using this API.<br>
	 * <br>
	 * The default order of the variable of the Distribution returned is the
	 * order of the variables as they are seen, as read from the left to right
	 * term, for e.g.: <br>
	 * <br>
	 * <b>P</b>(Y)<b>P</b>(X | Y)<br>
	 * <br>
	 * would give a Distribution of the following form: <br>
	 * Y, X<br>
	 * <br>
	 * i.e. an ordered union of the variables from the two distributions. <br>
	 * To override the default order of the product use multiplyByPOS().
	 * 
	 * @param multiplier
	 * 
	 * @return a new Distribution representing the product of this and the
	 *         passed in multiplier. The order of the variables comprising the
	 *         product distribution is the ordered union of the left term (this)
	 *         and the right term (multiplier).
	 * 
	 * @see mp_i_CategoricalDistributionIterator#multiplyByPOS(mp_i_CategoricalDistributionIterator,
	 *      mp_i_RandomVariable...)
	 */
	mp_i_CategoricalDistributionIterator multiplyBy(mp_i_CategoricalDistributionIterator multiplier);

	/**
	 * Multiplication - Product Order Specified (POS). <b>Note:</b> Is
	 * equivalent to pointwise product calculation.<br>
	 * <br>
	 * see: AIMA3e Figure 14.10 page 527.<br>
	 * <br>
	 * Multiplication of this Distribution by a given multiplier, creating a new
	 * Distribution representing the product of the two. The order of the
	 * variables comprising the product will match those specified. For example
	 * (the General case of Baye's rule, AIMA3e pg. 496), using this API method:<br>
	 * <br>
	 * <b>P</b>(Y | X) = (<b>P</b>(X | Y)<b>P</b>(Y), [Y, X])/<b>P</b>(X)<br>
	 * <br>
	 * is true when the correct product order is specified.
	 * 
	 * @param multiplier
	 * @param prodVarOrder
	 *            the order the variables comprising the product are to be in.
	 * 
	 * @return a new Distribution representing the product of this and the
	 *         passed in multiplier. The order of the variables comprising the
	 *         product distribution is the order specified.
	 * 
	 * @see mp_i_CategoricalDistributionIterator#multiplyBy(mp_i_CategoricalDistributionIterator)
	 */
	mp_i_CategoricalDistributionIterator multiplyByPOS(mp_i_CategoricalDistributionIterator multiplier,
			mp_i_RandomVariable... prodVarOrder);

	/**
	 * Iterate over all the possible value assignments for the Random Variables
	 * comprising this CategoricalDistribution.
	 * 
	 * @param cdi
	 *            the CategoricalDistribution Iterator to iterate.
	 */
	void iterateOver(Iterator cdi);

	/**
	 * Iterate over all possible values assignments for the Random Variables
	 * comprising this CategoricalDistribution that are not in the fixed set of
	 * values. This allows you to iterate over a subset of possible
	 * combinations.
	 * 
	 * @param cdi
	 *            the CategoricalDistribution Iterator to iterate
	 * @param fixedValues
	 *            Fixed values for a subset of the Random Variables comprising
	 *            this CategoricalDistribution.
	 */
	void iterateOver(Iterator cdi, mp_PropositionTermOpsAssignment... fixedValues);
}
