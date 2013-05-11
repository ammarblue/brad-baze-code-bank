package com.software.reuze;
//package aima.core.logic.fol.inference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*import aima.core.logic.fol.inference.proof.Proof;
import aima.core.logic.fol.inference.proof.ProofFinal;
import aima.core.logic.fol.inference.proof.ProofStepBwChGoal;
import aima.core.logic.fol.kb.FOLKnowledgeBase;
import aima.core.logic.fol.kb.data.Clause;
import aima.core.logic.fol.kb.data.Literal;
import aima.core.logic.fol.parsing.ast.FOAtomicSentence;
import aima.core.logic.fol.parsing.ast.Sentence;
import aima.core.logic.fol.parsing.ast.Term;
import aima.core.logic.fol.parsing.ast.Variable;*/

/**
 * Artificial Intelligence A Modern Approach (2nd Edition): Figure 9.6, page
 * 288.<br>
 * <br>
 * 
 * <pre>
 * function FOL-BC-ASK(KB, goals, theta) returns a set of substitutions
 *   input: KB, a knowledge base
 *          goals, a list of conjuncts forming a query (theta already applied)
 *          theta, the current substitution, initially the empty substitution {}
 *   local variables: answers, a set of substitutions, initially empty
 *   
 *   if goals is empty then return {theta}
 *   qDelta &lt;- SUBST(theta, FIRST(goals))
 *   for each sentence r in KB where STANDARDIZE-APART(r) = (p1 &circ; ... &circ; pn =&gt; q)
 *          and thetaDelta &lt;- UNIFY(q, qDelta) succeeds
 *       new_goals &lt;- [p1,...,pn|REST(goals)]
 *       answers &lt;- FOL-BC-ASK(KB, new_goals, COMPOSE(thetaDelta, theta)) U answers
 *   return answers
 * </pre>
 * 
 * Figure 9.6 A simple backward-chaining algorithm.
 * 
 * @author Ciaran O'Reilly
 * @author Mike Stampone
 */
public class mf_InferenceProcedureBCAsk implements mf_i_InferenceProcedure {

	public mf_InferenceProcedureBCAsk() {

	}

	//
	// START-InferenceProcedure
	/**
	 * Returns a set of substitutions
	 * 
	 * @param KB
	 *            a knowledge base
	 * @param query
	 *            goals, a list of conjuncts forming a query
	 * 
	 * @return a set of substitutions
	 */
	public mf_i_InferenceResult ask(mf_KnowledgeBase KB, mf_i_Sentence query) {
		// Assertions on the type queries this Inference procedure
		// supports
		if (!(query instanceof mf_i_SentenceAtomic)) {
			throw new IllegalArgumentException(
					"Only Atomic Queries are supported.");
		}

		List<mf_Literal> goals = new ArrayList<mf_Literal>();
		goals.add(new mf_Literal((mf_i_SentenceAtomic) query));

		BCAskAnswerHandler ansHandler = new BCAskAnswerHandler();

		List<List<mf_ProofStepBwChGoal>> allProofSteps = folbcask(KB, ansHandler,
				goals, new HashMap<mf_NodeTermVariable, mf_i_NodeTerm>());

		ansHandler.setAllProofSteps(allProofSteps);

		return ansHandler;
	}

	// END-InferenceProcedure
	//

	//
	// PRIVATE METHODS
	//

	/**
	 * <code>
	 * function FOL-BC-ASK(KB, goals, theta) returns a set of substitutions
	 *   input: KB, a knowledge base
	 *          goals, a list of conjuncts forming a query (theta already applied)
	 *          theta, the current substitution, initially the empty substitution {}
	 * </code>
	 */
	private List<List<mf_ProofStepBwChGoal>> folbcask(mf_KnowledgeBase KB,
			BCAskAnswerHandler ansHandler, List<mf_Literal> goals,
			Map<mf_NodeTermVariable, mf_i_NodeTerm> theta) {
		List<List<mf_ProofStepBwChGoal>> thisLevelProofSteps = new ArrayList<List<mf_ProofStepBwChGoal>>();
		// local variables: answers, a set of substitutions, initially empty

		// if goals is empty then return {theta}
		if (goals.isEmpty()) {
			thisLevelProofSteps.add(new ArrayList<mf_ProofStepBwChGoal>());
			return thisLevelProofSteps;
		}

		// qDelta <- SUBST(theta, FIRST(goals))
		mf_Literal qDelta = KB.subst(theta, goals.get(0));

		// for each sentence r in KB where
		// STANDARDIZE-APART(r) = (p1 ^ ... ^ pn => q)
		for (mf_Clause r : KB.getAllDefiniteClauses()) {
			r = KB.standardizeApart(r);
			// and thetaDelta <- UNIFY(q, qDelta) succeeds
			Map<mf_NodeTermVariable, mf_i_NodeTerm> thetaDelta = KB.unify(r.getPositiveLiterals()
					.get(0).getAtomicSentence(), qDelta.getAtomicSentence());
			if (null != thetaDelta) {
				// new_goals <- [p1,...,pn|REST(goals)]
				List<mf_Literal> newGoals = new ArrayList<mf_Literal>(
						r.getNegativeLiterals());
				newGoals.addAll(goals.subList(1, goals.size()));
				// answers <- FOL-BC-ASK(KB, new_goals, COMPOSE(thetaDelta,
				// theta)) U answers
				Map<mf_NodeTermVariable, mf_i_NodeTerm> composed = compose(KB, thetaDelta, theta);
				List<List<mf_ProofStepBwChGoal>> lowerLevelProofSteps = folbcask(
						KB, ansHandler, newGoals, composed);

				ansHandler.addProofStep(lowerLevelProofSteps, r, qDelta,
						composed);

				thisLevelProofSteps.addAll(lowerLevelProofSteps);
			}
		}

		// return answers
		return thisLevelProofSteps;
	}

	// Artificial Intelligence A Modern Approach (2nd Edition): page 288.
	// COMPOSE(delta, tau) is the substitution whose effect is identical to
	// the effect of applying each substitution in turn. That is,
	// SUBST(COMPOSE(theta1, theta2), p) = SUBST(theta2, SUBST(theta1, p))
	private Map<mf_NodeTermVariable, mf_i_NodeTerm> compose(mf_KnowledgeBase KB,
			Map<mf_NodeTermVariable, mf_i_NodeTerm> theta1, Map<mf_NodeTermVariable, mf_i_NodeTerm> theta2) {
		Map<mf_NodeTermVariable, mf_i_NodeTerm> composed = new HashMap<mf_NodeTermVariable, mf_i_NodeTerm>();

		// So that it behaves like:
		// SUBST(theta2, SUBST(theta1, p))
		// There are two steps involved here.
		// See: http://logic.stanford.edu/classes/cs157/2008/notes/chap09.pdf
		// for a detailed discussion:

		// 1. Apply theta2 to the range of theta1.
		for (mf_NodeTermVariable v : theta1.keySet()) {
			composed.put(v, KB.subst(theta2, theta1.get(v)));
		}

		// 2. Adjoin to delta all pairs from tau with different
		// domain variables.
		for (mf_NodeTermVariable v : theta2.keySet()) {
			if (!theta1.containsKey(v)) {
				composed.put(v, theta2.get(v));
			}
		}

		return cascadeSubstitutions(KB, composed);
	}

	// See:
	// http://logic.stanford.edu/classes/cs157/2008/miscellaneous/faq.html#jump165
	// for need for this.
	private Map<mf_NodeTermVariable, mf_i_NodeTerm> cascadeSubstitutions(mf_KnowledgeBase KB,
			Map<mf_NodeTermVariable, mf_i_NodeTerm> theta) {
		for (mf_NodeTermVariable v : theta.keySet()) {
			mf_i_NodeTerm t = theta.get(v);
			theta.put(v, KB.subst(theta, t));
		}

		return theta;
	}

	class BCAskAnswerHandler implements mf_i_InferenceResult {

		private List<mf_i_Proof> proofs = new ArrayList<mf_i_Proof>();

		public BCAskAnswerHandler() {

		}

		//
		// START-InferenceResult
		public boolean isPossiblyFalse() {
			return proofs.size() == 0;
		}

		public boolean isTrue() {
			return proofs.size() > 0;
		}

		public boolean isUnknownDueToTimeout() {
			return false;
		}

		public boolean isPartialResultDueToTimeout() {
			return false;
		}

		public List<mf_i_Proof> getProofs() {
			return proofs;
		}

		// END-InferenceResult
		//

		public void setAllProofSteps(List<List<mf_ProofStepBwChGoal>> allProofSteps) {
			for (List<mf_ProofStepBwChGoal> steps : allProofSteps) {
				mf_ProofStepBwChGoal lastStep = steps.get(steps.size() - 1);
				Map<mf_NodeTermVariable, mf_i_NodeTerm> theta = lastStep.getBindings();
				proofs.add(new mf_ProofFinal(lastStep, theta));
			}
		}

		public void addProofStep(
				List<List<mf_ProofStepBwChGoal>> currentLevelProofSteps,
				mf_Clause toProve, mf_Literal currentGoal,
				Map<mf_NodeTermVariable, mf_i_NodeTerm> bindings) {

			if (currentLevelProofSteps.size() > 0) {
				mf_ProofStepBwChGoal predecessor = new mf_ProofStepBwChGoal(toProve,
						currentGoal, bindings);
				for (List<mf_ProofStepBwChGoal> steps : currentLevelProofSteps) {
					if (steps.size() > 0) {
						steps.get(0).setPredecessor(predecessor);
					}
					steps.add(0, predecessor);
				}
			}
		}
	}
}
