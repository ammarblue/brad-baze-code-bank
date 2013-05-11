package com.software.reuze;
import com.software.reuze.mp_Factor;

//package aima.core.probability.bayes;

//import aima.core.probability.CategoricalDistribution;
//import aima.core.probability.Factor;
//import aima.core.probability.proposition.AssignmentProposition;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 512.<br>
 * <br>
 * A Conditional Probability Table, or CPT, can be used for representing
 * conditional probabilities for discrete (finite) random variables. Each row in
 * a CPT contains the conditional probability of each node value for a
 * <b>conditioning case</b>.
 * 
 * @author Ciaran O'Reilly
 */
public interface mp_i_DistributionConditionalTable extends
		mp_i_DistributionConditional {

	mp_i_CategoricalDistributionIterator getConditioningCase(Object... parentValues);

	mp_i_CategoricalDistributionIterator getConditioningCase(
			mp_PropositionTermOpsAssignment... parentValues);

	/**
	 * Construct a Factor consisting of the Random Variables from the
	 * Conditional Probability Table that are not part of the evidence (see
	 * AIMA3e pg. 524).
	 * 
	 * @param evidence
	 * @return a Factor for the Random Variables from the Conditional
	 *         Probability Table that are not part of the evidence.
	 */
	mp_Factor getFactorFor(mp_PropositionTermOpsAssignment... evidence);
}
