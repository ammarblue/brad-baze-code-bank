package com.software.reuze;
//package aima.core.logic.fol.inference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/*import aima.core.logic.fol.Connectors;
import aima.core.logic.fol.inference.proof.Proof;
import aima.core.logic.fol.inference.proof.ProofFinal;
import aima.core.logic.fol.inference.proof.ProofStepGoal;
import aima.core.logic.fol.inference.trace.FOLTFMResolutionTracer;
import aima.core.logic.fol.kb.FOLKnowledgeBase;
import aima.core.logic.fol.kb.data.Clause;
import aima.core.logic.fol.kb.data.Literal;
import aima.core.logic.fol.parsing.ast.ConnectedSentence;
import aima.core.logic.fol.parsing.ast.NotSentence;
import aima.core.logic.fol.parsing.ast.FOSentence;
import aima.core.logic.fol.parsing.ast.Term;
import aima.core.logic.fol.parsing.ast.Variable;*/

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 347.<br>
 * <br>
 * The algorithmic approach is identical to the propositional case, described in
 * Figure 7.12.<br>
 * <br>
 * However, this implementation will use the T)wo F)inger M)ethod for looking
 * for resolvents between clauses, which is very inefficient.<br>
 * <br>
 * see:<br>
 * <a
 * href="http://logic.stanford.edu/classes/cs157/2008/lectures/lecture04.pdf">
 * http://logic.stanford.edu/classes/cs157/2008/lectures/lecture04.pdf</a>,
 * slide 21 for the propositional case. In addition, an Answer literal will be
 * used so that queries with Variables may be answered (see pg. 350 of AIMA3e).
 * 
 * @author Ciaran O'Reilly
 * 
 */
public class mf_InferenceTFMResolution implements mf_i_InferenceProcedure {

	private long maxQueryTime = 10 * 1000;

	private mf_i_TFMResolutionTracer tracer = null;

	public mf_InferenceTFMResolution() {

	}

	public mf_InferenceTFMResolution(long maxQueryTime) {
		setMaxQueryTime(maxQueryTime);
	}

	public mf_InferenceTFMResolution(mf_i_TFMResolutionTracer tracer) {
		setTracer(tracer);
	}

	public long getMaxQueryTime() {
		return maxQueryTime;
	}

	public void setMaxQueryTime(long maxQueryTime) {
		this.maxQueryTime = maxQueryTime;
	}

	public mf_i_TFMResolutionTracer getTracer() {
		return tracer;
	}

	public void setTracer(mf_i_TFMResolutionTracer tracer) {
		this.tracer = tracer;
	}

	//
	// START-InferenceProcedure
	public mf_i_InferenceResult ask(mf_KnowledgeBase KB, mf_i_Sentence alpha) {

		// clauses <- the set of clauses in CNF representation of KB ^ ~alpha
		Set<mf_Clause> clauses = new LinkedHashSet<mf_Clause>();
		for (mf_Clause c : KB.getAllClauses()) {
			c = KB.standardizeApart(c);
			c.setStandardizedApartCheckNotRequired();
			clauses.addAll(c.getFactors());
		}
		mf_i_Sentence notAlpha = new mf_SentenceNot(alpha);
		// Want to use an answer literal to pull
		// query variables where necessary
		mf_Literal answerLiteral = KB.createAnswerLiteral(notAlpha);
		Set<mf_NodeTermVariable> answerLiteralVariables = KB
				.collectAllVariables(answerLiteral.getAtomicSentence());
		mf_Clause answerClause = new mf_Clause();

		if (answerLiteralVariables.size() > 0) {
			mf_i_Sentence notAlphaWithAnswer = new mf_SentenceConnected(mf_SymbolsConnectors.OR,
					notAlpha, answerLiteral.getAtomicSentence());
			for (mf_Clause c : KB.convertToClauses(notAlphaWithAnswer)) {
				c = KB.standardizeApart(c);
				c.setProofStep(new mf_ProofStepGoal(c));
				c.setStandardizedApartCheckNotRequired();
				clauses.addAll(c.getFactors());
			}

			answerClause.addLiteral(answerLiteral);
		} else {
			for (mf_Clause c : KB.convertToClauses(notAlpha)) {
				c = KB.standardizeApart(c);
				c.setProofStep(new mf_ProofStepGoal(c));
				c.setStandardizedApartCheckNotRequired();
				clauses.addAll(c.getFactors());
			}
		}

		TFMAnswerHandler ansHandler = new TFMAnswerHandler(answerLiteral,
				answerLiteralVariables, answerClause, maxQueryTime);

		// new <- {}
		Set<mf_Clause> newClauses = new LinkedHashSet<mf_Clause>();
		Set<mf_Clause> toAdd = new LinkedHashSet<mf_Clause>();
		// loop do
		int noOfPrevClauses = clauses.size();
		do {
			if (null != tracer) {
				tracer.stepStartWhile(clauses, clauses.size(),
						newClauses.size());
			}

			newClauses.clear();

			// for each Ci, Cj in clauses do
			mf_Clause[] clausesA = new mf_Clause[clauses.size()];
			clauses.toArray(clausesA);
			// Basically, using the simple T)wo F)inger M)ethod here.
			for (int i = 0; i < clausesA.length; i++) {
				mf_Clause cI = clausesA[i];
				if (null != tracer) {
					tracer.stepOuterFor(cI);
				}
				for (int j = i; j < clausesA.length; j++) {
					mf_Clause cJ = clausesA[j];

					if (null != tracer) {
						tracer.stepInnerFor(cI, cJ);
					}

					// resolvent <- FOL-RESOLVE(Ci, Cj)
					Set<mf_Clause> resolvents = cI.binaryResolvents(cJ);

					if (resolvents.size() > 0) {
						toAdd.clear();
						// new <- new <UNION> resolvent
						for (mf_Clause rc : resolvents) {
							toAdd.addAll(rc.getFactors());
						}

						if (null != tracer) {
							tracer.stepResolved(cI, cJ, toAdd);
						}

						ansHandler.checkForPossibleAnswers(toAdd);

						if (ansHandler.isComplete()) {
							break;
						}

						newClauses.addAll(toAdd);
					}

					if (ansHandler.isComplete()) {
						break;
					}
				}
				if (ansHandler.isComplete()) {
					break;
				}
			}

			noOfPrevClauses = clauses.size();

			// clauses <- clauses <UNION> new
			clauses.addAll(newClauses);

			if (ansHandler.isComplete()) {
				break;
			}

			// if new is a <SUBSET> of clauses then finished
			// searching for an answer
			// (i.e. when they were added the # clauses
			// did not increase).
		} while (noOfPrevClauses < clauses.size());

		if (null != tracer) {
			tracer.stepFinished(clauses, ansHandler);
		}

		return ansHandler;
	}

	// END-InferenceProcedure
	//

	//
	// PRIVATE METHODS
	//
	class TFMAnswerHandler implements mf_i_InferenceResult {
		private mf_Literal answerLiteral = null;
		private Set<mf_NodeTermVariable> answerLiteralVariables = null;
		private mf_Clause answerClause = null;
		private long finishTime = 0L;
		private boolean complete = false;
		private List<mf_i_Proof> proofs = new ArrayList<mf_i_Proof>();
		private boolean timedOut = false;

		public TFMAnswerHandler(mf_Literal answerLiteral,
				Set<mf_NodeTermVariable> answerLiteralVariables, mf_Clause answerClause,
				long maxQueryTime) {
			this.answerLiteral = answerLiteral;
			this.answerLiteralVariables = answerLiteralVariables;
			this.answerClause = answerClause;
			//
			this.finishTime = System.currentTimeMillis() + maxQueryTime;
		}

		//
		// START-InferenceResult
		public boolean isPossiblyFalse() {
			return !timedOut && proofs.size() == 0;
		}

		public boolean isTrue() {
			return proofs.size() > 0;
		}

		public boolean isUnknownDueToTimeout() {
			return timedOut && proofs.size() == 0;
		}

		public boolean isPartialResultDueToTimeout() {
			return timedOut && proofs.size() > 0;
		}

		public List<mf_i_Proof> getProofs() {
			return proofs;
		}

		// END-InferenceResult
		//

		public boolean isComplete() {
			return complete;
		}

		private void checkForPossibleAnswers(Set<mf_Clause> resolvents) {
			// If no bindings being looked for, then
			// is just a true false query.
			for (mf_Clause aClause : resolvents) {
				if (answerClause.isEmpty()) {
					if (aClause.isEmpty()) {
						proofs.add(new mf_ProofFinal(aClause.getProofStep(),
								new HashMap<mf_NodeTermVariable, mf_i_NodeTerm>()));
						complete = true;
					}
				} else {
					if (aClause.isEmpty()) {
						// This should not happen
						// as added an answer literal, which
						// implies the database (i.e. premises) are
						// unsatisfiable to begin with.
						throw new IllegalStateException(
								"Generated an empty clause while looking for an answer, implies original KB is unsatisfiable");
					}

					if (aClause.isUnitClause()
							&& aClause.isDefiniteClause()
							&& aClause
									.getPositiveLiterals()
									.get(0)
									.getAtomicSentence()
									.getSymbolicName()
									.equals(answerLiteral.getAtomicSentence()
											.getSymbolicName())) {
						Map<mf_NodeTermVariable, mf_i_NodeTerm> answerBindings = new HashMap<mf_NodeTermVariable, mf_i_NodeTerm>();
						List<mf_i_NodeTerm> answerTerms = aClause.getPositiveLiterals()
								.get(0).getAtomicSentence().getArgs();
						int idx = 0;
						for (mf_NodeTermVariable v : answerLiteralVariables) {
							answerBindings.put(v, answerTerms.get(idx));
							idx++;
						}
						boolean addNewAnswer = true;
						for (mf_i_Proof p : proofs) {
							if (p.getAnswerBindings().equals(answerBindings)) {
								addNewAnswer = false;
								break;
							}
						}
						if (addNewAnswer) {
							proofs.add(new mf_ProofFinal(aClause.getProofStep(),
									answerBindings));
						}
					}
				}

				if (System.currentTimeMillis() > finishTime) {
					complete = true;
					// Indicate that I have run out of query time
					timedOut = true;
				}
			}
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append("isComplete=" + complete);
			sb.append("\n");
			sb.append("result=" + proofs);
			return sb.toString();
		}
	}
}
