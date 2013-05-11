package com.software.reuze;
//package aima.core.probability.hmm.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


/*import aima.core.probability.CategoricalDistribution;
import aima.core.probability.RandomVariable;
import aima.core.probability.domain.FiniteDomain;
import aima.core.probability.hmm.HiddenMarkovModel;
import aima.core.probability.proposition.AssignmentProposition;
import aima.core.probability.util.ProbabilityTable;
import aima.core.util.Util;
import aima.core.util.math.Matrix;*/

/**
 * Default implementation of the HiddenMarkovModel interface.
 * 
 * @author Ciaran O'Reilly
 * @author Ravi Mohan
 */
public class mp_HMM implements mp_i_HMM {
	private mp_i_RandomVariable stateVariable = null;
	private ml_i_DomainDiscreteFinite stateVariableDomain = null;
	private m_Matrix transitionModel = null;
	private Map<Object, m_Matrix> sensorModel = null;
	private m_Matrix prior = null;

	/**
	 * Instantiate a Hidden Markov Model.
	 * 
	 * @param stateVariable
	 *            the single discrete random variable used to describe the
	 *            process states 1,...,S.
	 * @param transitionModel
	 *            the transition model:<br>
	 *            <b>P</b>(X<sub>t</sub> | X<sub>t-1</sub>)<br>
	 *            is represented by an S * S matrix <b>T</b> where<br>
	 *            <b>T</b><sub>ij</sub> = P(X<sub>t</sub> = j | X<sub>t-1</sub>
	 *            = i).
	 * @param sensorModel
	 *            the sensor model in matrix form:<br>
	 *            P(e<sub>t</sub> | X<sub>t</sub> = i) for each state i. For
	 *            mathematical convenience we place each of these values into an
	 *            S * S diagonal matrix.
	 * @param prior
	 *            the prior distribution represented as a column vector in
	 *            Matrix form.
	 */
	public mp_HMM(mp_i_RandomVariable stateVariable, m_Matrix transitionModel,
			Map<Object, m_Matrix> sensorModel, m_Matrix prior) {
		if (!stateVariable.getDomain().isFinite()) {
			throw new IllegalArgumentException(
					"State Variable for HHM must be finite.");
		}
		this.stateVariable = stateVariable;
		stateVariableDomain = (ml_i_DomainDiscreteFinite) stateVariable.getDomain();
		if (transitionModel.getRowDimension() != transitionModel
				.getColumnDimension()) {
			throw new IllegalArgumentException(
					"Transition Model row and column dimensions must match.");
		}
		if (stateVariableDomain.size() != transitionModel.getRowDimension()) {
			throw new IllegalArgumentException(
					"Transition Model Matrix does not map correctly to the HMM's State Variable.");
		}
		this.transitionModel = transitionModel;
		for (m_Matrix smVal : sensorModel.values()) {
			if (smVal.getRowDimension() != smVal.getColumnDimension()) {
				throw new IllegalArgumentException(
						"Sensor Model row and column dimensions must match.");
			}
			if (stateVariableDomain.size() != smVal.getRowDimension()) {
				throw new IllegalArgumentException(
						"Sensor Model Matrix does not map correctly to the HMM's State Variable.");
			}
		}
		this.sensorModel = sensorModel;
		if (transitionModel.getRowDimension() != prior.getRowDimension()
				&& prior.getColumnDimension() != 1) {
			throw new IllegalArgumentException(
					"Prior is not of the correct dimensions.");
		}
		this.prior = prior;
	}

	//
	// START-HiddenMarkovModel
	public mp_i_RandomVariable getStateVariable() {
		return stateVariable;
	}

	public m_Matrix getTransitionModel() {
		return transitionModel;
	}

	public Map<Object, m_Matrix> getSensorModel() {
		return sensorModel;
	}

	public m_Matrix getPrior() {
		return prior;
	}

	public m_Matrix getEvidence(List<mp_PropositionTermOpsAssignment> evidence) {
		if (evidence.size() != 1) {
			throw new IllegalArgumentException(
					"Only a single evidence observation value should be provided.");
		}
		m_Matrix e = sensorModel.get(evidence.get(0).getValue());
		if (null == e) {
			throw new IllegalArgumentException(
					"Evidence does not map to sensor model.");
		}
		return e;
	}

	public m_Matrix createUnitMessage() {
		double[] values = new double[stateVariableDomain.size()];
		Arrays.fill(values, 1.0);
		return new m_Matrix(values, values.length);
	}

	public m_Matrix convert(mp_i_CategoricalDistributionIterator fromCD) {
		double[] values = fromCD.getValues();
		return new m_Matrix(values, values.length);
	}

	public mp_i_CategoricalDistributionIterator convert(m_Matrix fromMessage) {
		return new mp_ProbabilityTable(fromMessage.getRowPackedCopy(),
				stateVariable);
	}

	public List<mp_i_CategoricalDistributionIterator> convert(List<m_Matrix> matrixs) {
		List<mp_i_CategoricalDistributionIterator> cds = new ArrayList<mp_i_CategoricalDistributionIterator>();
		for (m_Matrix m : matrixs) {
			cds.add(convert(m));
		}
		return cds;
	}

	public m_Matrix normalize(m_Matrix m) {
		double[] values = m.getRowPackedCopy();
		return new m_Matrix(m_MathUtil2.normalize(values), values.length);
	}

	// END-HiddenMarkovModel
	//
}
