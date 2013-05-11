package com.software.reuze;
//package aima.core.logic.fol.inference;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/*import aima.core.logic.fol.inference.proof.Proof;
import aima.core.logic.fol.inference.proof.ProofFinal;
import aima.core.logic.fol.inference.proof.ProofStep;
import aima.core.logic.fol.inference.proof.ProofStepFoChAlreadyAFact;
import aima.core.logic.fol.inference.proof.ProofStepFoChAssertFact;
import aima.core.logic.fol.kb.FOLKnowledgeBase;
import aima.core.logic.fol.kb.data.Clause;
import aima.core.logic.fol.kb.data.Literal;
import aima.core.logic.fol.parsing.ast.FOAtomicSentence;
import aima.core.logic.fol.parsing.ast.NotSentence;
import aima.core.logic.fol.parsing.ast.FOSentence;
import aima.core.logic.fol.parsing.ast.Term;
import aima.core.logic.fol.parsing.ast.Variable;*/

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): Figure 9.3, page
 * 332.<br>
 * <br>
 * 
 * <pre>
 * function FOL-FC-ASK(KB, alpha) returns a substitution or false
 *   inputs: KB, the knowledge base, a set of first order definite clauses
 *           alpha, the query, an atomic sentence
 *   local variables: new, the new sentences inferred on each iteration
 *   
 *   repeat until new is empty
 *      new &lt;- {}
 *      for each rule in KB do
 *          (p1 &circ; ... &circ; pn =&gt; q) &lt;- STANDARDIZE-VARAIBLES(rule)
 *          for each theta such that SUBST(theta, p1 &circ; ... &circ; pn) = SUBST(theta, p'1 &circ; ... &circ; p'n)
 *                         for some p'1,...,p'n in KB
 *              q' &lt;- SUBST(theta, q)
 *              if q' does not unify with some sentence already in KB or new then
 *                   add q' to new
 *                   theta &lt;- UNIFY(q', alpha)
 *                   if theta is not fail then return theta
 *      add new to KB
 *   return false
 * </pre>
 * 
 * Figure 9.3 A conceptually straightforward, but very inefficient
 * forward-chaining algorithm. On each iteration, it adds to KB all the atomic
 * sentences that can be inferred in one step from the implication sentences and
 * the atomic sentences already in KB. The function STANDARDIZE-VARIABLES
 * replaces all variables in its arguments with new ones that have not been used
 * before.
 * 
 * @author Ciaran O'Reilly
 * 
 */
public class mf_InferenceProcedureFCAsk implements mf_i_InferenceProcedure {

	public mf_InferenceProcedureFCAsk() {
	}

	//
	// START-InferenceProcedure

	/**
	 * FOL-FC-ASK returns a substitution or false.
	 * 
	 * @param KB
	 *            the knowledge base, a set of first order definite clauses
	 * @param query
	 *            the query, an atomic sentence
	 * 
	 * @return a substitution or false
	 */
	public mf_i_InferenceResult ask(mf_KnowledgeBase KB, mf_i_Sentence query) {
		// Assertions on the type of queries this Inference procedure
		// supports
		if (!(query instanceof mf_i_SentenceAtomic)) {
			throw new IllegalArgumentException(
					"Only Atomic Queries are supported.");
		}

		FCAskAnswerHandler ansHandler = new FCAskAnswerHandler();

		mf_Literal alpha = new mf_Literal((mf_i_SentenceAtomic) query);

		// local variables: new, the new sentences inferred on each iteration
		List<mf_Literal> newSentences = new ArrayList<mf_Literal>();

		// Ensure query is not already a know fact before
		// attempting forward chaining.
		Set<Map<mf_NodeTermVariable, mf_i_NodeTerm>> answers = KB.fetch(alpha);
		if (answers.size() > 0) {
			ansHandler.addProofStep(new mf_ProofStepFoChAlreadyAFact(alpha));
			ansHandler.setAnswers(answers);
			return ansHandler;
		}

		// repeat until new is empty
		do {

			// new <- {}
			newSentences.clear();
			// for each rule in KB do
			// (p1 ^ ... ^ pn => q) <-STANDARDIZE-VARIABLES(rule)
			for (mf_Clause impl : KB.getAllDefiniteClauseImplications()) {
				impl = KB.standardizeApart(impl);
				// for each theta such that SUBST(theta, p1 ^ ... ^ pn) =
				// SUBST(theta, p'1 ^ ... ^ p'n)
				// --- for some p'1,...,p'n in KB
				for (Map<mf_NodeTermVariable, mf_i_NodeTerm> theta : KB.fetch(invert(impl
						.getNegativeLiterals()))) {
					// q' <- SUBST(theta, q)
					mf_Literal qDelta = KB.subst(theta, impl.getPositiveLiterals()
							.get(0));
					// if q' does not unify with some sentence already in KB or
					// new then do
					if (!KB.isRenaming(qDelta)
							&& !KB.isRenaming(qDelta, newSentences)) {
						// add q' to new
						newSentences.add(qDelta);
						ansHandler.addProofStep(impl, qDelta, theta);
						// theta <- UNIFY(q', alpha)
						theta = KB.unify(qDelta.getAtomicSentence(),
								alpha.getAtomicSentence());
						// if theta is not fail then return theta
						if (null != theta) {
							for (mf_Literal l : newSentences) {
								mf_i_Sentence s = null;
								if (l.isPositiveLiteral()) {
									s = l.getAtomicSentence();
								} else {
									s = new mf_SentenceNot(l.getAtomicSentence());
								}
								KB.tell(s);
							}
							ansHandler.setAnswers(KB.fetch(alpha));
							return ansHandler;
						}
					}
				}
			}
			// add new to KB
			for (mf_Literal l : newSentences) {
				mf_i_Sentence s = null;
				if (l.isPositiveLiteral()) {
					s = l.getAtomicSentence();
				} else {
					s = new mf_SentenceNot(l.getAtomicSentence());
				}
				KB.tell(s);
			}
		} while (newSentences.size() > 0);

		// return false
		return ansHandler;
	}

	// END-InferenceProcedure
	//

	//
	// PRIVATE METHODS
	//
	private List<mf_Literal> invert(List<mf_Literal> lits) {
		List<mf_Literal> invLits = new ArrayList<mf_Literal>();
		for (mf_Literal l : lits) {
			invLits.add(new mf_Literal(l.getAtomicSentence(), (l
					.isPositiveLiteral() ? true : false)));
		}
		return invLits;
	}

	class FCAskAnswerHandler implements mf_i_InferenceResult {

		private m_ProofStep stepFinal = null;
		private List<mf_i_Proof> proofs = new ArrayList<mf_i_Proof>();

		public FCAskAnswerHandler() {

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

		public void addProofStep(mf_Clause implication, mf_Literal fact,
				Map<mf_NodeTermVariable, mf_i_NodeTerm> bindings) {
			stepFinal = new mf_ProofStepFoChAssertFact(implication, fact,
					bindings, stepFinal);
		}

		public void addProofStep(m_ProofStep step) {
			stepFinal = step;
		}

		public void setAnswers(Set<Map<mf_NodeTermVariable, mf_i_NodeTerm>> answers) {
			for (Map<mf_NodeTermVariable, mf_i_NodeTerm> ans : answers) {
				proofs.add(new mf_ProofFinal(stepFinal, ans));
			}
		}
	}
}
