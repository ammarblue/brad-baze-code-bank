package com.software.reuze;
//package aima.core.probability.bayes.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


/*import aima.core.probability.RandomVariable;
import aima.core.probability.bayes.BayesianNetwork;
import aima.core.probability.bayes.DynamicBayesianNetwork;
import aima.core.probability.bayes.Node;
import aima.core.util.SetOps;*/

/**
 * Default implementation of the DynamicBayesianNetwork interface.
 * 
 * @author Ciaran O'Reilly
 */
public class mpb_NetworkDynamic extends mpb_Network implements mpb_i_NetworkDynamic {

	private Set<mp_i_RandomVariable> X_0 = new LinkedHashSet<mp_i_RandomVariable>();
	private Set<mp_i_RandomVariable> X_1 = new LinkedHashSet<mp_i_RandomVariable>();
	private Set<mp_i_RandomVariable> E_1 = new LinkedHashSet<mp_i_RandomVariable>();
	private Map<mp_i_RandomVariable, mp_i_RandomVariable> X_0_to_X_1 = new LinkedHashMap<mp_i_RandomVariable, mp_i_RandomVariable>();
	private Map<mp_i_RandomVariable, mp_i_RandomVariable> X_1_to_X_0 = new LinkedHashMap<mp_i_RandomVariable, mp_i_RandomVariable>();
	private mpb_i_Network priorNetwork = null;
	private List<mp_i_RandomVariable> X_1_VariablesInTopologicalOrder = new ArrayList<mp_i_RandomVariable>();

	public mpb_NetworkDynamic(mpb_i_Network priorNetwork,
			Map<mp_i_RandomVariable, mp_i_RandomVariable> X_0_to_X_1,
			Set<mp_i_RandomVariable> E_1, mpb_i_Node... rootNodes) {
		super(rootNodes);

		for (Map.Entry<mp_i_RandomVariable, mp_i_RandomVariable> x0_x1 : X_0_to_X_1
				.entrySet()) {
			mp_i_RandomVariable x0 = x0_x1.getKey();
			mp_i_RandomVariable x1 = x0_x1.getValue();
			this.X_0.add(x0);
			this.X_1.add(x1);
			this.X_0_to_X_1.put(x0, x1);
			this.X_1_to_X_0.put(x1, x0);
		}
		this.E_1.addAll(E_1);

		// Assert the X_0, X_1, and E_1 sets are of expected sizes
		Set<mp_i_RandomVariable> combined = new LinkedHashSet<mp_i_RandomVariable>();
		combined.addAll(X_0);
		combined.addAll(X_1);
		combined.addAll(E_1);
		if (da_SetOps.difference(varToNodeMap.keySet(), combined).size() != 0) {
			throw new IllegalArgumentException(
					"X_0, X_1, and E_1 do not map correctly to the Nodes describing this Dynamic Bayesian Network.");
		}
		this.priorNetwork = priorNetwork;

		X_1_VariablesInTopologicalOrder
				.addAll(getVariablesInTopologicalOrder());
		X_1_VariablesInTopologicalOrder.removeAll(X_0);
		X_1_VariablesInTopologicalOrder.removeAll(E_1);
	}

	//
	// START-DynamicBayesianNetwork
	public mpb_i_Network getPriorNetwork() {
		return priorNetwork;
	}

	public Set<mp_i_RandomVariable> getX_0() {
		return X_0;
	}

	public Set<mp_i_RandomVariable> getX_1() {
		return X_1;
	}

	public List<mp_i_RandomVariable> getX_1_VariablesInTopologicalOrder() {
		return X_1_VariablesInTopologicalOrder;
	}

	public Map<mp_i_RandomVariable, mp_i_RandomVariable> getX_0_to_X_1() {
		return X_0_to_X_1;
	}

	public Map<mp_i_RandomVariable, mp_i_RandomVariable> getX_1_to_X_0() {
		return X_1_to_X_0;
	}

	public Set<mp_i_RandomVariable> getE_1() {
		return E_1;
	}

	// END-DynamicBayesianNetwork
	//

	//
	// PRIVATE METHODS
	//
}
