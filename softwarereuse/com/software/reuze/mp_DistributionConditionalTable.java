package com.software.reuze;
//package aima.core.probability.bayes.impl;


import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.software.reuze.ml_i_DomainDiscreteFinite;
import com.software.reuze.mp_Factor;


/*import aima.core.probability.CategoricalDistribution;
import aima.core.probability.Factor;
import aima.core.probability.ProbabilityModel;
import aima.core.probability.RandomVariable;
import aima.core.probability.bayes.ConditionalProbabilityTable;
import aima.core.probability.domain.FiniteDomain;
import aima.core.probability.proposition.AssignmentProposition;
import aima.core.probability.util.ProbUtil;
import aima.core.probability.util.ProbabilityTable;*/

/**
 * Default implementation of the ConditionalProbabilityTable interface.
 * 
 * @author Ciaran O'Reilly
 * 
 */
public class mp_DistributionConditionalTable implements mp_i_DistributionConditionalTable {
	private mp_i_RandomVariable on = null;
	private LinkedHashSet<mp_i_RandomVariable> parents = new LinkedHashSet<mp_i_RandomVariable>();
	private mp_ProbabilityTable table = null;
	private List<Object> onDomain = new ArrayList<Object>();

	public mp_DistributionConditionalTable(mp_i_RandomVariable on, double[] values,
			mp_i_RandomVariable... conditionedOn) {
		this.on = on;
		if (null == conditionedOn) {
			conditionedOn = new mp_i_RandomVariable[0];
		}
		mp_i_RandomVariable[] tableVars = new mp_i_RandomVariable[conditionedOn.length + 1];
		for (int i = 0; i < conditionedOn.length; i++) {
			tableVars[i] = conditionedOn[i];
			parents.add(conditionedOn[i]);
		}
		tableVars[conditionedOn.length] = on;
		table = new mp_ProbabilityTable(values, tableVars);
		onDomain.addAll(((ml_i_DomainDiscreteFinite) on.getDomain()).getPossibleValues());

		checkEachRowTotalsOne();
	}

	public double probabilityFor(final Object... values) {
		return table.getValue(values);
	}

	//
	// START-ConditionalProbabilityDistribution


	public mp_i_RandomVariable getOn() {
		return on;
	}

	public Set<mp_i_RandomVariable> getParents() {
		return parents;
	}

	public Set<mp_i_RandomVariable> getFor() {
		return table.getFor();
	}

	public boolean contains(mp_i_RandomVariable rv) {
		return table.contains(rv);
	}

	public double getValue(Object... eventValues) {
		return table.getValue(eventValues);
	}

	public double getValue(mp_PropositionTermOpsAssignment... eventValues) {
		return table.getValue(eventValues);
	}

	public Object getSample(double probabilityChoice, Object... parentValues) {
		return mp_ProbUtil.sample(probabilityChoice, on,
				getConditioningCase(parentValues).getValues());
	}

	public Object getSample(double probabilityChoice,
			mp_PropositionTermOpsAssignment... parentValues) {
		return mp_ProbUtil.sample(probabilityChoice, on,
				getConditioningCase(parentValues).getValues());
	}

	// END-ConditionalProbabilityDistribution
	//

	//
	// START-ConditionalProbabilityTable
	public mp_i_CategoricalDistributionIterator getConditioningCase(Object... parentValues) {
		if (parentValues.length != parents.size()) {
			throw new IllegalArgumentException(
					"The number of parent value arguments ["
							+ parentValues.length
							+ "] is not equal to the number of parents ["
							+ parents.size() + "] for this CPT.");
		}
		mp_PropositionTermOpsAssignment[] aps = new mp_PropositionTermOpsAssignment[parentValues.length];
		int idx = 0;
		for (mp_i_RandomVariable parentRV : parents) {
			aps[idx] = new mp_PropositionTermOpsAssignment(parentRV, parentValues[idx]);
			idx++;
		}

		return getConditioningCase(aps);
	}

	public mp_i_CategoricalDistributionIterator getConditioningCase(
			mp_PropositionTermOpsAssignment... parentValues) {
		if (parentValues.length != parents.size()) {
			throw new IllegalArgumentException(
					"The number of parent value arguments ["
							+ parentValues.length
							+ "] is not equal to the number of parents ["
							+ parents.size() + "] for this CPT.");
		}
		final mp_ProbabilityTable cc = new mp_ProbabilityTable(getOn());
		mp_ProbabilityTable.Iterator pti = new mp_ProbabilityTable.Iterator() {
			private int idx = 0;

			public void iterate(Map<mp_i_RandomVariable, Object> possibleAssignment,
					double probability) {
				cc.getValues()[idx] = probability;
				idx++;
			}
		};
		table.iterateOverTable(pti, parentValues);

		return cc;
	}

	public mp_Factor getFactorFor(final mp_PropositionTermOpsAssignment... evidence) {
		Set<mp_i_RandomVariable> fofVars = new LinkedHashSet<mp_i_RandomVariable>(
				table.getFor());
		for (mp_PropositionTermOpsAssignment ap : evidence) {
			fofVars.remove(ap.getTermVariable());
		}
		final mp_ProbabilityTable fof = new mp_ProbabilityTable(fofVars);
		// Otherwise need to iterate through the table for the
		// non evidence variables.
		final Object[] termValues = new Object[fofVars.size()];
		mp_ProbabilityTable.Iterator di = new mp_ProbabilityTable.Iterator() {
			public void iterate(Map<mp_i_RandomVariable, Object> possibleWorld,
					double probability) {
				if (0 == termValues.length) {
					fof.getValues()[0] += probability;
				} else {
					int i = 0;
					for (mp_i_RandomVariable rv : fof.getFor()) {
						termValues[i] = possibleWorld.get(rv);
						i++;
					}
					fof.getValues()[fof.getIndex(termValues)] += probability;
				}
			}
		};
		table.iterateOverTable(di, evidence);

		return fof;
	}

	// END-ConditionalProbabilityTable
	//

	//
	// PRIVATE METHODS
	//
	private void checkEachRowTotalsOne() {
		mp_ProbabilityTable.Iterator di = new mp_ProbabilityTable.Iterator() {
			private int rowSize = onDomain.size();
			private int iterateCnt = 0;
			private double rowProb = 0;

			public void iterate(Map<mp_i_RandomVariable, Object> possibleWorld,
					double probability) {
				iterateCnt++;
				rowProb += probability;
				if (iterateCnt % rowSize == 0) {
					if (Math.abs(1 - rowProb) > mp_i_Model.DEFAULT_ROUNDING_THRESHOLD) {
						throw new IllegalArgumentException("Row "
								+ (iterateCnt / rowSize)
								+ " of CPT does not sum to 1.0.");
					}
					rowProb = 0;
				}
			}
		};

		table.iterateOverTable(di);
	}
}
