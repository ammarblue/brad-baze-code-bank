package com.software.reuze;
//package aima.core.probability.bayes.model;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;


/*import aima.core.probability.CategoricalDistribution;
import aima.core.probability.FiniteProbabilityModel;
import aima.core.probability.ProbabilityModel;
import aima.core.probability.RandomVariable;
import aima.core.probability.bayes.BayesInference;
import aima.core.probability.bayes.BayesianNetwork;
import aima.core.probability.bayes.exact.EnumerationAsk;
import aima.core.probability.proposition.AssignmentProposition;
import aima.core.probability.proposition.ConjunctiveProposition;
import aima.core.probability.proposition.Proposition;
import aima.core.probability.util.ProbUtil;
import aima.core.probability.util.ProbabilityTable;*/

/**
 * Very simple implementation of the FiniteProbabilityModel API using a Bayesian
 * Network, consisting of FiniteNodes, to represent the underlying model.<br>
 * <br>
 * <b>Note:</b> The implementation currently doesn't take advantage of the use
 * of evidence values when calculating posterior values using the provided
 * Bayesian Inference implementation (e.g enumerationAsk). Instead it simply
 * creates a joint distribution over the scope of the propositions (i.e. using
 * the inference implementation with just query variables corresponding to the
 * scope of the propositions) and then iterates over this to get the appropriate
 * probability values. A smarter version, in the general case, would need to
 * dynamically create deterministic nodes to represent the outcome of logical
 * and derived propositions (e.g. conjuncts and summations over variables) in
 * order to be able to correctly calculate using evidence values.
 * 
 * @author Ciaran O'Reilly
 */
public class mpb_ModelFinite implements mp_i_ModelFinite {

	private mpb_i_Network bayesNet = null;
	private Set<mp_i_RandomVariable> representation = new LinkedHashSet<mp_i_RandomVariable>();
	private mpb_i_Inference bayesInference = null;

	public mpb_ModelFinite(mpb_i_Network bn) {
		this(bn, new mpb_InferenceEnumeration());
	}

	public mpb_ModelFinite(mpb_i_Network bn, mpb_i_Inference bi) {
		if (null == bn) {
			throw new IllegalArgumentException(
					"Bayesian Network describing the model must be specified.");
		}
		this.bayesNet = bn;
		this.representation.addAll(bn.getVariablesInTopologicalOrder());
		setBayesInference(bi);
	}

	public mpb_i_Inference getBayesInference() {
		return bayesInference;
	}

	public void setBayesInference(mpb_i_Inference bi) {
		this.bayesInference = bi;
	}

	//
	// START-ProbabilityModel
	public boolean isValid() {
		// Handle rounding
		return Math.abs(1 - prior(representation
				.toArray(new mp_i_Proposition[representation.size()]))) <= mp_i_Model.DEFAULT_ROUNDING_THRESHOLD;
	}

	public double prior(mp_i_Proposition... phi) {
		// Calculating the prior, therefore no relevant evidence
		// just query over the scope of proposition phi in order
		// to get a joint distribution for these
		final mp_i_Proposition conjunct = mp_ProbUtil.constructConjunction(phi);
		mp_i_RandomVariable[] X = conjunct.getScope().toArray(
				new mp_i_RandomVariable[conjunct.getScope().size()]);
		mp_i_CategoricalDistributionIterator d = bayesInference.ask(X,
				new mp_PropositionTermOpsAssignment[0], bayesNet);

		// Then calculate the probability of the propositions phi
		// be seeing where they hold.
		final double[] probSum = new double[1];
		mp_i_CategoricalDistributionIterator.Iterator di = new mp_i_CategoricalDistributionIterator.Iterator() {
			public void iterate(Map<mp_i_RandomVariable, Object> possibleWorld,
					double probability) {
				if (conjunct.holds(possibleWorld)) {
					probSum[0] += probability;
				}
			}
		};
		d.iterateOver(di);

		return probSum[0];
	}

	public double posterior(mp_i_Proposition phi, mp_i_Proposition... evidence) {

		mp_i_Proposition conjEvidence = mp_ProbUtil.constructConjunction(evidence);

		// P(A | B) = P(A AND B)/P(B) - (13.3 AIMA3e)
		mp_i_Proposition aAndB = new mp_PropositionConjunctive(phi, conjEvidence);
		double probabilityOfEvidence = prior(conjEvidence);
		if (0 != probabilityOfEvidence) {
			return prior(aAndB) / probabilityOfEvidence;
		}

		return 0;
	}

	public Set<mp_i_RandomVariable> getRepresentation() {
		return representation;
	}

	// END-ProbabilityModel
	//

	//
	// START-FiniteProbabilityModel
	public mp_i_CategoricalDistributionIterator priorDistribution(mp_i_Proposition... phi) {
		return jointDistribution(phi);
	}

	public mp_i_CategoricalDistributionIterator posteriorDistribution(mp_i_Proposition phi,
			mp_i_Proposition... evidence) {

		mp_i_Proposition conjEvidence = mp_ProbUtil.constructConjunction(evidence);

		// P(A | B) = P(A AND B)/P(B) - (13.3 AIMA3e)
		mp_i_CategoricalDistributionIterator dAandB = jointDistribution(phi, conjEvidence);
		mp_i_CategoricalDistributionIterator dEvidence = jointDistribution(conjEvidence);

		mp_i_CategoricalDistributionIterator rVal = dAandB.divideBy(dEvidence);
		// Note: Need to ensure normalize() is called
		// in order to handle the case where an approximate
		// algorithm is used (i.e. won't evenly divide
		// as will have calculated on separate approximate
		// runs). However, this should only be done
		// if the all of the evidences scope are bound (if not
		// you are returning in essence a set of conditional
		// distributions, which you do not want normalized).
		boolean unboundEvidence = false;
		for (mp_i_Proposition e : evidence) {
			if (e.getUnboundScope().size() > 0) {
				unboundEvidence = true;
				break;
			}
		}
		if (!unboundEvidence) {
			rVal.normalize();
		}

		return rVal;
	}

	public mp_i_CategoricalDistributionIterator jointDistribution(
			mp_i_Proposition... propositions) {
		mp_ProbabilityTable d = null;
		final mp_i_Proposition conjProp = mp_ProbUtil
				.constructConjunction(propositions);
		final LinkedHashSet<mp_i_RandomVariable> vars = new LinkedHashSet<mp_i_RandomVariable>(
				conjProp.getUnboundScope());

		if (vars.size() > 0) {
			mp_i_RandomVariable[] distVars = new mp_i_RandomVariable[vars.size()];
			int i = 0;
			for (mp_i_RandomVariable rv : vars) {
				distVars[i] = rv;
				i++;
			}

			final mp_ProbabilityTable ud = new mp_ProbabilityTable(distVars);
			final Object[] values = new Object[vars.size()];

			mp_i_CategoricalDistributionIterator.Iterator di = new mp_i_CategoricalDistributionIterator.Iterator() {

				public void iterate(Map<mp_i_RandomVariable, Object> possibleWorld,
						double probability) {
					if (conjProp.holds(possibleWorld)) {
						int i = 0;
						for (mp_i_RandomVariable rv : vars) {
							values[i] = possibleWorld.get(rv);
							i++;
						}
						int dIdx = ud.getIndex(values);
						ud.setValue(dIdx, ud.getValues()[dIdx] + probability);
					}
				}
			};

			mp_i_RandomVariable[] X = conjProp.getScope().toArray(
					new mp_i_RandomVariable[conjProp.getScope().size()]);
			bayesInference.ask(X, new mp_PropositionTermOpsAssignment[0], bayesNet)
					.iterateOver(di);

			d = ud;
		} else {
			// No Unbound Variables, therefore just return
			// the singular probability related to the proposition.
			d = new mp_ProbabilityTable();
			d.setValue(0, prior(propositions));
		}
		return d;
	}

	// END-FiniteProbabilityModel
	//
}
