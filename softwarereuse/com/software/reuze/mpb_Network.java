package com.software.reuze;
//package aima.core.probability.bayes.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


//import aima.core.probability.RandomVariable;
//import aima.core.probability.bayes.BayesianNetwork;
//import aima.core.probability.bayes.Node;

/**
 * Default implementation of the BayesianNetwork interface.
 * 
 * @author Ciaran O'Reilly
 * @author Ravi Mohan
 */
public class mpb_Network implements mpb_i_Network {
	protected Set<mpb_i_Node> rootNodes = new LinkedHashSet<mpb_i_Node>();
	protected List<mp_i_RandomVariable> variables = new ArrayList<mp_i_RandomVariable>();
	protected Map<mp_i_RandomVariable, mpb_i_Node> varToNodeMap = new HashMap<mp_i_RandomVariable, mpb_i_Node>();

	public mpb_Network(mpb_i_Node... rootNodes) {
		if (null == rootNodes) {
			throw new IllegalArgumentException(
					"Root Nodes need to be specified.");
		}
		for (mpb_i_Node n : rootNodes) {
			this.rootNodes.add(n);
		}
		if (this.rootNodes.size() != rootNodes.length) {
			throw new IllegalArgumentException(
					"Duplicate Root Nodes Passed in.");
		}
		// Ensure is a DAG
		checkIsDAGAndCollectVariablesInTopologicalOrder();
		variables = Collections.unmodifiableList(variables);
	}

	//
	// START-BayesianNetwork
	public List<mp_i_RandomVariable> getVariablesInTopologicalOrder() {
		return variables;
	}

	public mpb_i_Node getNode(mp_i_RandomVariable rv) {
		return varToNodeMap.get(rv);
	}

	// END-BayesianNetwork
	//

	//
	// PRIVATE METHODS
	//
	private void checkIsDAGAndCollectVariablesInTopologicalOrder() {

		// Topological sort based on logic described at:
		// http://en.wikipedia.org/wiki/Topoligical_sorting
		Set<mpb_i_Node> seenAlready = new HashSet<mpb_i_Node>();
		Map<mpb_i_Node, List<mpb_i_Node>> incomingEdges = new HashMap<mpb_i_Node, List<mpb_i_Node>>();
		Set<mpb_i_Node> s = new LinkedHashSet<mpb_i_Node>();
		for (mpb_i_Node n : this.rootNodes) {
			walkNode(n, seenAlready, incomingEdges, s);
		}
		while (!s.isEmpty()) {
			mpb_i_Node n = s.iterator().next();
			s.remove(n);
			variables.add(n.getRandomVariable());
			varToNodeMap.put(n.getRandomVariable(), n);
			for (mpb_i_Node m : n.getChildren()) {
				List<mpb_i_Node> edges = incomingEdges.get(m);
				edges.remove(n);
				if (edges.isEmpty()) {
					s.add(m);
				}
			}
		}

		for (List<mpb_i_Node> edges : incomingEdges.values()) {
			if (!edges.isEmpty()) {
				throw new IllegalArgumentException(
						"Network contains at least one cycle in it, must be a DAG.");
			}
		}
	}

	private void walkNode(mpb_i_Node n, Set<mpb_i_Node> seenAlready,
			Map<mpb_i_Node, List<mpb_i_Node>> incomingEdges, Set<mpb_i_Node> rootNodes) {
		if (!seenAlready.contains(n)) {
			seenAlready.add(n);
			// Check if has no incoming edges
			if (n.isRoot()) {
				rootNodes.add(n);
			}
			incomingEdges.put(n, new ArrayList<mpb_i_Node>(n.getParents()));
			for (mpb_i_Node c : n.getChildren()) {
				walkNode(c, seenAlready, incomingEdges, rootNodes);
			}
		}
	}
}
