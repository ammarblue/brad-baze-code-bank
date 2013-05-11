package com.software.reuze;

//package aima.core.probability.bayes.impl;

/*import aima.core.probability.RandomVariable;
import aima.core.probability.bayes.ConditionalProbabilityDistribution;
import aima.core.probability.bayes.ConditionalProbabilityTable;
import aima.core.probability.bayes.FiniteNode;
import aima.core.probability.bayes.Node;*/

/**
 * Default implementation of the FiniteNode interface that uses a fully
 * specified Conditional Probability Table to represent the Node's conditional
 * distribution.
 * 
 * @author Ciaran O'Reilly
 * 
 */
public class mpb_NodeDiscreteFiniteFullCPT extends mp_a_Node implements mpb_i_NodeDiscreteFinite {
	private mp_i_DistributionConditionalTable cpt = null;

	public mpb_NodeDiscreteFiniteFullCPT(mp_i_RandomVariable var, double[] distribution) {
		this(var, distribution, (mpb_i_Node[]) null);
	}

	public mpb_NodeDiscreteFiniteFullCPT(mp_i_RandomVariable var, double[] values, mpb_i_Node... parents) {
		super(var, parents);

		mp_i_RandomVariable[] conditionedOn = new mp_i_RandomVariable[getParents().size()];
		int i = 0;
		for (mpb_i_Node p : getParents()) {
			conditionedOn[i++] = p.getRandomVariable();
		}

		cpt = new mp_DistributionConditionalTable(var, values, conditionedOn);
	}

	//
	// START-Node
	public mp_i_DistributionConditional getCPD() {
		return getCPT();
	}

	// END-Node
	//

	//
	// START-FiniteNode

	public mp_i_DistributionConditionalTable getCPT() {
		return cpt;
	}

	// END-FiniteNode
	//
}
