package com.software.reuze;
//package aima.core.probability.bayes.impl;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;


//import aima.core.probability.RandomVariable;
//import aima.core.probability.bayes.ConditionalProbabilityDistribution;
//import aima.core.probability.bayes.Node;

/**
 * Abstract base implementation of the Node interface.
 * 
 * @author Ciaran O'Reilly
 * @author Ravi Mohan
 */
public abstract class mp_a_Node implements mpb_i_Node {
	private mp_i_RandomVariable variable = null;
	private Set<mpb_i_Node> parents = null;
	private Set<mpb_i_Node> children = null;

	public mp_a_Node(mp_i_RandomVariable var) {
		this(var, (mpb_i_Node[]) null);
	}

	public mp_a_Node(mp_i_RandomVariable var, mpb_i_Node... parents) {
		if (null == var) {
			throw new IllegalArgumentException(
					"Random Variable for Node must be specified.");
		}
		this.variable = var;
		this.parents = new LinkedHashSet<mpb_i_Node>();
		if (null != parents) {
			for (mpb_i_Node p : parents) {
				((mp_a_Node) p).addChild(this);
				this.parents.add(p);
			}
		}
		this.parents = Collections.unmodifiableSet(this.parents);
		this.children = Collections.unmodifiableSet(new LinkedHashSet<mpb_i_Node>());
	}

	//
	// START-Node
	public mp_i_RandomVariable getRandomVariable() {
		return variable;
	}

	public boolean isRoot() {
		return 0 == getParents().size();
	}

	public Set<mpb_i_Node> getParents() {
		return parents;
	}

	public Set<mpb_i_Node> getChildren() {
		return children;
	}

	public Set<mpb_i_Node> getMarkovBlanket() {
		LinkedHashSet<mpb_i_Node> mb = new LinkedHashSet<mpb_i_Node>();
		// Given its parents,
		mb.addAll(getParents());
		// children,
		mb.addAll(getChildren());
		// and children's parents
		for (mpb_i_Node cn : getChildren()) {
			mb.addAll(cn.getParents());
		}

		return mb;
	}

	public abstract mp_i_DistributionConditional getCPD();

	// END-Node
	//

	@Override
	public String toString() {
		return getRandomVariable().getName();
	}

	@Override
	public boolean equals(Object o) {
		if (null == o) {
			return false;
		}
		if (o == this) {
			return true;
		}

		if (o instanceof mpb_i_Node) {
			mpb_i_Node n = (mpb_i_Node) o;

			return getRandomVariable().equals(n.getRandomVariable());
		}

		return false;
	}

	@Override
	public int hashCode() {
		return variable.hashCode();
	}

	//
	// PROTECTED METHODS
	//
	protected void addChild(mpb_i_Node childNode) {
		children = new LinkedHashSet<mpb_i_Node>(children);

		children.add(childNode);

		children = Collections.unmodifiableSet(children);
	}
}
