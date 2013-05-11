package com.software.reuze;
//package aima.core.probability.full;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;


/*import aima.core.probability.CategoricalDistribution;
import aima.core.probability.FiniteProbabilityModel;
import aima.core.probability.ProbabilityModel;
import aima.core.probability.RandomVariable;
import aima.core.probability.proposition.ConjunctiveProposition;
import aima.core.probability.proposition.Proposition;
import aima.core.probability.util.ProbUtil;
import aima.core.probability.util.ProbabilityTable;*/

/**
 * An implementation of the FiniteProbabilityModel API using a full joint
 * distribution as the underlying model.
 * 
 * @author Ciaran O'Reilly
 */
public class mp_ModelFiniteFullJoint implements mp_i_ModelFinite {

	private mp_ProbabilityTable distribution = null;
	private Set<mp_i_RandomVariable> representation = null;

	public mp_ModelFiniteFullJoint(double[] values, mp_i_RandomVariable... vars) {
		if (null == vars) {
			throw new IllegalArgumentException(
					"Random Variables describing the model's representation of the World need to be specified.");
		}

		distribution = new mp_ProbabilityTable(values, vars);

		representation = new LinkedHashSet<mp_i_RandomVariable>();
		for (int i = 0; i < vars.length; i++) {
			representation.add(vars[i]);
		}
		representation = Collections.unmodifiableSet(representation);
	}

	//
	// START-ProbabilityModel
	public boolean isValid() {
		// Handle rounding
		return Math.abs(1 - distribution.getSum()) <= mp_i_Model.DEFAULT_ROUNDING_THRESHOLD;
	}

	public double prior(mp_i_Proposition... phi) {
		return probabilityOf(mp_ProbUtil.constructConjunction(phi));
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

		return dAandB.divideBy(dEvidence);
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
			vars.toArray(distVars);

			final mp_ProbabilityTable ud = new mp_ProbabilityTable(distVars);
			final Object[] values = new Object[vars.size()];

			mp_ProbabilityTable.Iterator di = new mp_ProbabilityTable.Iterator() {

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

			distribution.iterateOverTable(di);

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

	//
	// PRIVATE METHODS
	//
	private double probabilityOf(final mp_i_Proposition phi) {
		final double[] probSum = new double[1];
		mp_ProbabilityTable.Iterator di = new mp_ProbabilityTable.Iterator() {
			public void iterate(Map<mp_i_RandomVariable, Object> possibleWorld,
					double probability) {
				if (phi.holds(possibleWorld)) {
					probSum[0] += probability;
				}
			}
		};

		distribution.iterateOverTable(di);

		return probSum[0];
	}
}
