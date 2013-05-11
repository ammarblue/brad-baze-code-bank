package com.software.reuze;
//package aima.core.probability.bayes;

import java.util.List;
import java.util.Map;
import java.util.Set;


//import aima.core.probability.RandomVariable;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 590.<br>
 * <br>
 * A <b>dynamic Bayesian network</b>, or <b>DBN</b>, is a Bayesian network that
 * represents a temporal probability model. In general, each slice of a DBN can
 * have any number of state variables <b>X</b><sub>t</sub< and evidence
 * variables <b>E</b><sub>t</sub>. For simplicity, we assume that the variables
 * and their links are exactly replicated from slice to slice and that the DBN
 * represents a first-order Markov process, so that each variable can have
 * parents only in its own slice or the immediately preceding slice.
 * 
 * @author Ciaran O'Reilly
 * 
 */
public interface mpb_i_NetworkDynamic extends mpb_i_Network {

	/**
	 * 
	 * @return a Bayesian Network containing just the nodes representing the
	 *         prior distribution (layer 0) of the dynamic bayesian network.
	 */
	mpb_i_Network getPriorNetwork();

	/**
	 * 
	 * @return the set of state variables representing the prior distribution.
	 */
	Set<mp_i_RandomVariable> getX_0();

	/**
	 * 
	 * @return the set of state variables representing the first posterior slice
	 *         of the DBN. This along with <b>X</b><sub>0</sub> should represent
	 *         the transition model <b>P</b>(<b>X</b><sub>1</sub> |
	 *         <b>X</b><sub>0</sub>).
	 */
	Set<mp_i_RandomVariable> getX_1();

	/**
	 * 
	 * @return the X_1 variables in topological order.
	 */
	List<mp_i_RandomVariable> getX_1_VariablesInTopologicalOrder();

	/**
	 * 
	 * @return a Map indicating equivalent variables between X<sub>0</sub> and
	 *         X<sub>1</sub>.
	 */
	Map<mp_i_RandomVariable, mp_i_RandomVariable> getX_0_to_X_1();

	/**
	 * 
	 * @return a Map indicating equivalent variables between X<sub>1</sub> and
	 *         X<sub>0</sub>.
	 */
	Map<mp_i_RandomVariable, mp_i_RandomVariable> getX_1_to_X_0();

	/**
	 * 
	 * @return the set of state variables representing the evidence variables
	 *         for the DBN. This along with <b>X</b><sub>1</sub> should
	 *         represent the sensor model <b>P</b>(<b>E</b><sub>1</sub> |
	 *         <b>X</b><sub>1</sub>).
	 */
	Set<mp_i_RandomVariable> getE_1();
}
