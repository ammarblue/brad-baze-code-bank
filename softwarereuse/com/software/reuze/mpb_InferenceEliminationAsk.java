package com.software.reuze;
//package aima.core.probability.bayes.exact;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/*import aima.core.probability.CategoricalDistribution;
import aima.core.probability.Factor;
import aima.core.probability.RandomVariable;
import aima.core.probability.bayes.BayesInference;
import aima.core.probability.bayes.BayesianNetwork;
import aima.core.probability.bayes.FiniteNode;
import aima.core.probability.bayes.Node;
import aima.core.probability.proposition.AssignmentProposition;
import aima.core.probability.util.ProbabilityTable;*/

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): Figure 14.11, page
 * 528.<br>
 * <br>
 * 
 * <pre>
 * function ELIMINATION-ASK(X, e, bn) returns a distribution over X
 *   inputs: X, the query variable
 *           e, observed values for variables E
 *           bn, a Bayesian network specifying joint distribution P(X<sub>1</sub>, ..., X<sub>n</sub>)
 *   
 *   factors <- []
 *   for each var in ORDER(bn.VARS) do
 *       factors <- [MAKE-FACTOR(var, e) | factors]
 *       if var is hidden variable the factors <- SUM-OUT(var, factors)
 *   return NORMALIZE(POINTWISE-PRODUCT(factors))
 * </pre>
 * 
 * Figure 14.11 The variable elimination algorithm for inference in Bayesian
 * networks. <br>
 * <br>
 * <b>Note:</b> The implementation has been extended to handle queries with
 * multiple variables. <br>
 * 
 * @author Ciaran O'Reilly
 */
public class mpb_InferenceEliminationAsk implements mpb_i_Inference {
	//
	private static final mp_ProbabilityTable _identity = new mp_ProbabilityTable(
			new double[] { 1.0 });

	public mpb_InferenceEliminationAsk() {

	}

	// function ELIMINATION-ASK(X, e, bn) returns a distribution over X
	/**
	 * The ELIMINATION-ASK algorithm in Figure 14.11.
	 * 
	 * @param X
	 *            the query variables.
	 * @param e
	 *            observed values for variables E.
	 * @param bn
	 *            a Bayes net with variables {X} &cup; E &cup; Y /* Y = hidden
	 *            variables //
	 * @return a distribution over the query variables.
	 */
	public mp_i_CategoricalDistributionIterator eliminationAsk(final mp_i_RandomVariable[] X,
			final mp_PropositionTermOpsAssignment[] e, final mpb_i_Network bn) {

		Set<mp_i_RandomVariable> hidden = new HashSet<mp_i_RandomVariable>();
		List<mp_i_RandomVariable> VARS = new ArrayList<mp_i_RandomVariable>();
		calculateVariables(X, e, bn, hidden, VARS);

		// factors <- []
		List<mp_Factor> factors = new ArrayList<mp_Factor>();
		// for each var in ORDER(bn.VARS) do
		for (mp_i_RandomVariable var : order(bn, VARS)) {
			// factors <- [MAKE-FACTOR(var, e) | factors]
			factors.add(0, makeFactor(var, e, bn));
			// if var is hidden variable then factors <- SUM-OUT(var, factors)
			if (hidden.contains(var)) {
				factors = sumOut(var, factors, bn);
			}
		}
		// return NORMALIZE(POINTWISE-PRODUCT(factors))
		mp_Factor product = pointwiseProduct(factors);
		// Note: Want to ensure the order of the product matches the
		// query variables
		return ((mp_ProbabilityTable) product.pointwiseProductPOS(_identity, X))
				.normalize();
	}

	//
	// START-BayesInference
	public mp_i_CategoricalDistributionIterator ask(final mp_i_RandomVariable[] X,
			final mp_PropositionTermOpsAssignment[] observedEvidence,
			final mpb_i_Network bn) {
		return this.eliminationAsk(X, observedEvidence, bn);
	}

	// END-BayesInference
	//

	//
	// PROTECTED METHODS
	//
	/**
	 * <b>Note:</b>Override this method for a more efficient implementation as
	 * outlined in AIMA3e pgs. 527-28. Calculate the hidden variables from the
	 * Bayesian Network. The default implementation does not perform any of
	 * these.<br>
	 * <br>
	 * Two calcuations to be performed here in order to optimize iteration over
	 * the Bayesian Network:<br>
	 * 1. Calculate the hidden variables to be enumerated over. An optimization
	 * (AIMA3e pg. 528) is to remove 'every variable that is not an ancestor of
	 * a query variable or evidence variable as it is irrelevant to the query'
	 * (i.e. sums to 1). 2. The subset of variables from the Bayesian Network to
	 * be retained after irrelevant hidden variables have been removed.
	 * 
	 * @param X
	 *            the query variables.
	 * @param e
	 *            observed values for variables E.
	 * @param bn
	 *            a Bayes net with variables {X} &cup; E &cup; Y /* Y = hidden
	 *            variables //
	 * @param hidden
	 *            to be populated with the relevant hidden variables Y.
	 * @param bnVARS
	 *            to be populated with the subset of the random variables
	 *            comprising the Bayesian Network with any irrelevant hidden
	 *            variables removed.
	 */
	protected void calculateVariables(final mp_i_RandomVariable[] X,
			final mp_PropositionTermOpsAssignment[] e, final mpb_i_Network bn,
			Set<mp_i_RandomVariable> hidden, Collection<mp_i_RandomVariable> bnVARS) {

		bnVARS.addAll(bn.getVariablesInTopologicalOrder());
		hidden.addAll(bnVARS);

		for (mp_i_RandomVariable x : X) {
			hidden.remove(x);
		}
		for (mp_PropositionTermOpsAssignment ap : e) {
			hidden.removeAll(ap.getScope());
		}

		return;
	}

	/**
	 * <b>Note:</b>Override this method for a more efficient implementation as
	 * outlined in AIMA3e pgs. 527-28. The default implementation does not
	 * perform any of these.<br>
	 * 
	 * @param bn
	 *            the Bayesian Network over which the query is being made. Note,
	 *            is necessary to provide this in order to be able to determine
	 *            the dependencies between variables.
	 * @param vars
	 *            a subset of the RandomVariables making up the Bayesian
	 *            Network, with any irrelevant hidden variables alreay removed.
	 * @return a possibly opimal ordering for the random variables to be
	 *         iterated over by the algorithm. For example, one fairly effective
	 *         ordering is a greedy one: eliminate whichever variable minimizes
	 *         the size of the next factor to be constructed.
	 */
	protected List<mp_i_RandomVariable> order(mpb_i_Network bn,
			Collection<mp_i_RandomVariable> vars) {
		// Note: Trivial Approach:
		// For simplicity just return in the reverse order received,
		// i.e. received will be the default topological order for
		// the Bayesian Network and we want to ensure the network
		// is iterated from bottom up to ensure when hidden variables
		// are come across all the factors dependent on them have
		// been seen so far.
		List<mp_i_RandomVariable> order = new ArrayList<mp_i_RandomVariable>(vars);
		Collections.reverse(order);

		return order;
	}

	//
	// PRIVATE METHODS
	//
	private mp_Factor makeFactor(mp_i_RandomVariable var, mp_PropositionTermOpsAssignment[] e,
			mpb_i_Network bn) {

		mpb_i_Node n = bn.getNode(var);
		if (!(n instanceof mpb_i_NodeDiscreteFinite)) {
			throw new IllegalArgumentException(
					"Elimination-Ask only works with finite Nodes.");
		}
		mpb_i_NodeDiscreteFinite fn = (mpb_i_NodeDiscreteFinite) n;
		List<mp_PropositionTermOpsAssignment> evidence = new ArrayList<mp_PropositionTermOpsAssignment>();
		for (mp_PropositionTermOpsAssignment ap : e) {
			if (fn.getCPT().contains(ap.getTermVariable())) {
				evidence.add(ap);
			}
		}

		return fn.getCPT().getFactorFor(
				evidence.toArray(new mp_PropositionTermOpsAssignment[evidence.size()]));
	}

	private List<mp_Factor> sumOut(mp_i_RandomVariable var, List<mp_Factor> factors,
			mpb_i_Network bn) {
		List<mp_Factor> summedOutFactors = new ArrayList<mp_Factor>();
		List<mp_Factor> toMultiply = new ArrayList<mp_Factor>();
		for (mp_Factor f : factors) {
			if (f.contains(var)) {
				toMultiply.add(f);
			} else {
				// This factor does not contain the variable
				// so no need to sum out - see AIMA3e pg. 527.
				summedOutFactors.add(f);
			}
		}

		summedOutFactors.add(pointwiseProduct(toMultiply).sumOut(var));

		return summedOutFactors;
	}

	private mp_Factor pointwiseProduct(List<mp_Factor> factors) {

		mp_Factor product = factors.get(0);
		for (int i = 1; i < factors.size(); i++) {
			product = product.pointwiseProduct(factors.get(i));
		}

		return product;
	}
}