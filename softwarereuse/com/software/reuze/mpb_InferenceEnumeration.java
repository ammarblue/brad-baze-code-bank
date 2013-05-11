package com.software.reuze;
//package aima.core.probability.bayes.exact;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/*import aima.core.probability.CategoricalDistribution;
import aima.core.probability.RandomVariable;
import aima.core.probability.bayes.BayesInference;
import aima.core.probability.bayes.BayesianNetwork;
import aima.core.probability.bayes.FiniteNode;
import aima.core.probability.bayes.Node;
import aima.core.probability.domain.FiniteDomain;
import aima.core.probability.proposition.AssignmentProposition;
import aima.core.probability.util.ProbabilityTable;
import aima.core.util.Util;*/

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): Figure 14.9, page
 * 525.<br>
 * <br>
 * 
 * <pre>
 * function ENUMERATION-ASK(X, e, bn) returns a distribution over X
 *   inputs: X, the query variable
 *           e, observed values for variables E
 *           bn, a Bayes net with variables {X} &cup; E &cup; Y /* Y = hidden variables //
 *           
 *   Q(X) <- a distribution over X, initially empty
 *   for each value x<sub>i</sub> of X do
 *       Q(x<sub>i</sub>) <- ENUMERATE-ALL(bn.VARS, e<sub>x<sub>i</sub></sub>)
 *          where e<sub>x<sub>i</sub></sub> is e extended with X = x<sub>i</sub>
 *   return NORMALIZE(Q(X))
 *   
 * ---------------------------------------------------------------------------------------------------
 * 
 * function ENUMERATE-ALL(vars, e) returns a real number
 *   if EMPTY?(vars) then return 1.0
 *   Y <- FIRST(vars)
 *   if Y has value y in e
 *       then return P(y | parents(Y)) * ENUMERATE-ALL(REST(vars), e)
 *       else return &sum;<sub>y</sub> P(y | parents(Y)) * ENUMERATE-ALL(REST(vars), e<sub>y</sub>)
 *           where e<sub>y</sub> is e extended with Y = y
 * </pre>
 * 
 * Figure 14.9 The enumeration algorithm for answering queries on Bayesian
 * networks. <br>
 * <br>
 * <b>Note:</b> The implementation has been extended to handle queries with
 * multiple variables. <br>
 * 
 * @author Ciaran O'Reilly
 */
public class mpb_InferenceEnumeration implements mpb_i_Inference {

	public mpb_InferenceEnumeration() {

	}

	// function ENUMERATION-ASK(X, e, bn) returns a distribution over X
	/**
	 * The ENUMERATION-ASK algorithm in Figure 14.9 evaluates expression trees
	 * (Figure 14.8) using depth-first recursion.
	 * 
	 * @param X
	 *            the query variables.
	 * @param observedEvidence
	 *            observed values for variables E.
	 * @param bn
	 *            a Bayes net with variables {X} &cup; E &cup; Y /* Y = hidden
	 *            variables //
	 * @return a distribution over the query variables.
	 */
	public mp_i_CategoricalDistributionIterator enumerationAsk(final mp_i_RandomVariable[] X,
			final mp_PropositionTermOpsAssignment[] observedEvidence,
			final mpb_i_Network bn) {

		// Q(X) <- a distribution over X, initially empty
		final mp_ProbabilityTable Q = new mp_ProbabilityTable(X);
		final ObservedEvidence e = new ObservedEvidence(X, observedEvidence, bn);
		// for each value x<sub>i</sub> of X do
		mp_ProbabilityTable.Iterator di = new mp_ProbabilityTable.Iterator() {
			int cnt = 0;

			/**
			 * <pre>
			 * Q(x<sub>i</sub>) <- ENUMERATE-ALL(bn.VARS, e<sub>x<sub>i</sub></sub>)
			 *   where e<sub>x<sub>i</sub></sub> is e extended with X = x<sub>i</sub>
			 * </pre>
			 */
			public void iterate(Map<mp_i_RandomVariable, Object> possibleWorld,
					double probability) {
				for (int i = 0; i < X.length; i++) {
					e.setExtendedValue(X[i], possibleWorld.get(X[i]));
				}
				Q.setValue(cnt,
						enumerateAll(bn.getVariablesInTopologicalOrder(), e));
				cnt++;
			}
		};
		Q.iterateOverTable(di);

		// return NORMALIZE(Q(X))
		return Q.normalize();
	}

	//
	// START-BayesInference
	public mp_i_CategoricalDistributionIterator ask(final mp_i_RandomVariable[] X,
			final mp_PropositionTermOpsAssignment[] observedEvidence,
			final mpb_i_Network bn) {
		return this.enumerationAsk(X, observedEvidence, bn);
	}

	// END-BayesInference
	//

	//
	// PROTECTED METHODS
	//
	// function ENUMERATE-ALL(vars, e) returns a real number
	protected double enumerateAll(List<mp_i_RandomVariable> vars, ObservedEvidence e) {
		// if EMPTY?(vars) then return 1.0
		if (0 == vars.size()) {
			return 1;
		}
		// Y <- FIRST(vars)
		mp_i_RandomVariable Y = m_MathUtil2.first(vars);
		// if Y has value y in e
		if (e.containsValue(Y)) {
			// then return P(y | parents(Y)) * ENUMERATE-ALL(REST(vars), e)
			return e.posteriorForParents(Y) * enumerateAll(m_MathUtil2.rest(vars), e);
		}
		/**
		 * <pre>
		 *  else return &sum;<sub>y</sub> P(y | parents(Y)) * ENUMERATE-ALL(REST(vars), e<sub>y</sub>)
		 *       where e<sub>y</sub> is e extended with Y = y
		 * </pre>
		 */
		double sum = 0;
		for (Object y : ((ml_i_DomainDiscreteFinite) Y.getDomain()).getPossibleValues()) {
			e.setExtendedValue(Y, y);
			sum += e.posteriorForParents(Y) * enumerateAll(m_MathUtil2.rest(vars), e);
		}

		return sum;
	}

	protected class ObservedEvidence {
		private mpb_i_Network bn = null;
		private Object[] extendedValues = null;
		private int hiddenStart = 0;
		private int extendedIdx = 0;
		private mp_i_RandomVariable[] var = null;
		private Map<mp_i_RandomVariable, Integer> varIdxs = new HashMap<mp_i_RandomVariable, Integer>();

		public ObservedEvidence(mp_i_RandomVariable[] queryVariables,
				mp_PropositionTermOpsAssignment[] e, mpb_i_Network bn) {
			this.bn = bn;

			int maxSize = bn.getVariablesInTopologicalOrder().size();
			extendedValues = new Object[maxSize];
			var = new mp_i_RandomVariable[maxSize];
			// query variables go first
			int idx = 0;
			for (int i = 0; i < queryVariables.length; i++) {
				var[idx] = queryVariables[i];
				varIdxs.put(var[idx], idx);
				idx++;
			}
			// initial evidence variables go next
			for (int i = 0; i < e.length; i++) {
				var[idx] = e[i].getTermVariable();
				varIdxs.put(var[idx], idx);
				extendedValues[idx] = e[i].getValue();
				idx++;
			}
			extendedIdx = idx - 1;
			hiddenStart = idx;
			// the remaining slots are left open for the hidden variables
			for (mp_i_RandomVariable rv : bn.getVariablesInTopologicalOrder()) {
				if (!varIdxs.containsKey(rv)) {
					var[idx] = rv;
					varIdxs.put(var[idx], idx);
					idx++;
				}
			}
		}

		public void setExtendedValue(mp_i_RandomVariable rv, Object value) {
			int idx = varIdxs.get(rv);
			extendedValues[idx] = value;
			if (idx >= hiddenStart) {
				extendedIdx = idx;
			} else {
				extendedIdx = hiddenStart - 1;
			}
		}

		public boolean containsValue(mp_i_RandomVariable rv) {
			return varIdxs.get(rv) <= extendedIdx;
		}

		public double posteriorForParents(mp_i_RandomVariable rv) {
			mpb_i_Node n = bn.getNode(rv);
			if (!(n instanceof mpb_i_NodeDiscreteFinite)) {
				throw new IllegalArgumentException(
						"Enumeration-Ask only works with finite Nodes.");
			}
			mpb_i_NodeDiscreteFinite fn = (mpb_i_NodeDiscreteFinite) n;
			Object[] vals = new Object[1 + fn.getParents().size()];
			int idx = 0;
			for (mpb_i_Node pn : n.getParents()) {
				vals[idx] = extendedValues[varIdxs.get(pn.getRandomVariable())];
				idx++;
			}
			vals[idx] = extendedValues[varIdxs.get(rv)];

			return fn.getCPT().getValue(vals);
		}
	}

	//
	// PRIVATE METHODS
	//
}