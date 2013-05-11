package com.software.reuze;
//package aima.core.logic.fol.inference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/*import aima.core.logic.fol.Connectors;
import aima.core.logic.fol.StandardizeApartInPlace;
import aima.core.logic.fol.SubstVisitor;
import aima.core.logic.fol.SubsumptionElimination;
import aima.core.logic.fol.Unifier;
import aima.core.logic.fol.inference.proof.Proof;
import aima.core.logic.fol.inference.proof.ProofFinal;
import aima.core.logic.fol.inference.proof.ProofStepChainCancellation;
import aima.core.logic.fol.inference.proof.ProofStepChainDropped;
import aima.core.logic.fol.inference.proof.ProofStepChainFromClause;
import aima.core.logic.fol.inference.proof.ProofStepChainReduction;
import aima.core.logic.fol.inference.proof.ProofStepGoal;
import aima.core.logic.fol.inference.trace.FOLModelEliminationTracer;
import aima.core.logic.fol.kb.FOLKnowledgeBase;
import aima.core.logic.fol.kb.data.Chain;
import aima.core.logic.fol.kb.data.Clause;
import aima.core.logic.fol.kb.data.Literal;
import aima.core.logic.fol.kb.data.ReducedLiteral;
import aima.core.logic.fol.parsing.ast.FOAtomicSentence;
import aima.core.logic.fol.parsing.ast.ConnectedSentence;
import aima.core.logic.fol.parsing.ast.NotSentence;
import aima.core.logic.fol.parsing.ast.FOSentence;
import aima.core.logic.fol.parsing.ast.Term;
import aima.core.logic.fol.parsing.ast.Variable;*/

/**
 * Based on lecture notes from:<br>
 * <a
 * href="http://logic.stanford.edu/classes/cs157/2008/lectures/lecture13.pdf">
 * http://logic.stanford.edu/classes/cs157/2008/lectures/lecture13.pdf</a>
 * 
 * @author Ciaran O'Reilly
 * 
 */
public class mf_InferenceProcedureModelElimination implements mf_i_InferenceProcedure {

	// Ten seconds is default maximum query time permitted
	private long maxQueryTime = 10 * 1000;
	//
	private mf_i_ModelEliminationTracer tracer = null;
	//
	private ml_Unifier unifier = new ml_Unifier();
	private mf_VisitorSubstitution substVisitor = new mf_VisitorSubstitution();

	public mf_InferenceProcedureModelElimination() {

	}

	public mf_InferenceProcedureModelElimination(long maxQueryTime) {
		setMaxQueryTime(maxQueryTime);
	}

	public mf_InferenceProcedureModelElimination(mf_i_ModelEliminationTracer tracer) {
		this.tracer = tracer;
	}

	public mf_InferenceProcedureModelElimination(mf_i_ModelEliminationTracer tracer,
			long maxQueryTime) {
		this.tracer = tracer;
		setMaxQueryTime(maxQueryTime);
	}

	public long getMaxQueryTime() {
		return maxQueryTime;
	}

	public void setMaxQueryTime(long maxQueryTime) {
		this.maxQueryTime = maxQueryTime;
	}

	//
	// START-InferenceProcedure

	public mf_i_InferenceResult ask(mf_KnowledgeBase kb, mf_i_Sentence query) {
		//
		// Get the background knowledge - are assuming this is satisfiable
		// as using Set of Support strategy.
		Set<mf_Clause> bgClauses = new LinkedHashSet<mf_Clause>(kb.getAllClauses());
		bgClauses.removeAll(mf_SubsumptionElimination
				.findSubsumedClauses(bgClauses));
		List<mf_ProofStepChain> background = createChainsFromClauses(bgClauses);

		// Collect the information necessary for constructing
		// an answer (supports use of answer literals).
		AnswerHandler ansHandler = new AnswerHandler(kb, query, maxQueryTime);

		IndexedFarParents ifps = new IndexedFarParents(
				ansHandler.getSetOfSupport(), background);

		// Iterative deepening to be used
		for (int maxDepth = 1; maxDepth < Integer.MAX_VALUE; maxDepth++) {
			// Track the depth actually reached
			ansHandler.resetMaxDepthReached();

			if (null != tracer) {
				tracer.reset();
			}

			for (mf_ProofStepChain nearParent : ansHandler.getSetOfSupport()) {
				recursiveDLS(maxDepth, 0, nearParent, ifps, ansHandler);
				if (ansHandler.isComplete()) {
					return ansHandler;
				}
			}
			// This means the search tree
			// has bottomed out (i.e. finite).
			// Return what I know based on exploring everything.
			if (ansHandler.getMaxDepthReached() < maxDepth) {
				return ansHandler;
			}
		}

		return ansHandler;
	}

	// END-InferenceProcedure
	//

	//
	// PRIVATE METHODS
	//
	private List<mf_ProofStepChain> createChainsFromClauses(Set<mf_Clause> clauses) {
		List<mf_ProofStepChain> chains = new ArrayList<mf_ProofStepChain>();

		for (mf_Clause c : clauses) {
			mf_ProofStepChain chn = new mf_ProofStepChain(c.getLiterals());
			chn.setProofStep(new mf_ProofStepChainFromClause(chn, c));
			chains.add(chn);
			chains.addAll(chn.getContrapositives());
		}

		return chains;
	}

	// Recursive Depth Limited Search
	private void recursiveDLS(int maxDepth, int currentDepth, mf_ProofStepChain nearParent,
			IndexedFarParents indexedFarParents, AnswerHandler ansHandler) {

		// Keep track of the maximum depth reached.
		ansHandler.updateMaxDepthReached(currentDepth);

		if (currentDepth == maxDepth) {
			return;
		}

		int noCandidateFarParents = indexedFarParents
				.getNumberCandidateFarParents(nearParent);
		if (null != tracer) {
			tracer.increment(currentDepth, noCandidateFarParents);
		}
		indexedFarParents.standardizeApart(nearParent);
		for (int farParentIdx = 0; farParentIdx < noCandidateFarParents; farParentIdx++) {
			// If have a complete answer, don't keep
			// checking candidate far parents
			if (ansHandler.isComplete()) {
				break;
			}

			// Reduction
			mf_ProofStepChain nextNearParent = indexedFarParents.attemptReduction(
					nearParent, farParentIdx);

			if (null == nextNearParent) {
				// Unable to remove the head via reduction
				continue;
			}

			// Handle Canceling and Dropping
			boolean cancelled = false;
			boolean dropped = false;
			do {
				cancelled = false;
				mf_ProofStepChain nextParent = null;
				while (nextNearParent != (nextParent = tryCancellation(nextNearParent))) {
					nextNearParent = nextParent;
					cancelled = true;
				}

				dropped = false;
				while (nextNearParent != (nextParent = tryDropping(nextNearParent))) {
					nextNearParent = nextParent;
					dropped = true;
				}
			} while (dropped || cancelled);

			// Check if have answer before
			// going to the next level
			if (!ansHandler.isAnswer(nextNearParent)) {
				// Keep track of the current # of
				// far parents that are possible for the next near parent.
				int noNextFarParents = indexedFarParents
						.getNumberFarParents(nextNearParent);
				// Add to indexed far parents
				nextNearParent = indexedFarParents.addToIndex(nextNearParent);

				// Check the next level
				recursiveDLS(maxDepth, currentDepth + 1, nextNearParent,
						indexedFarParents, ansHandler);

				// Reset the number of far parents possible
				// when recursing back up.
				indexedFarParents.resetNumberFarParentsTo(nextNearParent,
						noNextFarParents);
			}
		}
	}

	// Returns c if no cancellation occurred
	private mf_ProofStepChain tryCancellation(mf_ProofStepChain c) {
		mf_Literal head = c.getHead();
		if (null != head && !(head instanceof mf_LiteralReduced)) {
			for (mf_Literal l : c.getTail()) {
				if (l instanceof mf_LiteralReduced) {
					// if they can be resolved
					if (head.isNegativeLiteral() != l.isNegativeLiteral()) {
						Map<mf_NodeTermVariable, mf_i_NodeTerm> subst = unifier
								.unify(head.getAtomicSentence(),
										l.getAtomicSentence());
						if (null != subst) {
							// I have a cancellation
							// Need to apply subst to all of the
							// literals in the cancellation
							List<mf_Literal> cancLits = new ArrayList<mf_Literal>();
							for (mf_Literal lfc : c.getTail()) {
								mf_i_SentenceAtomic a = (mf_i_SentenceAtomic) substVisitor
										.subst(subst, lfc.getAtomicSentence());
								cancLits.add(lfc.newInstance(a));
							}
							mf_ProofStepChain cancellation = new mf_ProofStepChain(cancLits);
							cancellation
									.setProofStep(new mf_ProofStepChainCancellation(
											cancellation, c, subst));
							return cancellation;
						}
					}
				}
			}
		}
		return c;
	}

	// Returns c if no dropping occurred
	private mf_ProofStepChain tryDropping(mf_ProofStepChain c) {
		mf_Literal head = c.getHead();
		if (null != head && (head instanceof mf_LiteralReduced)) {
			mf_ProofStepChain dropped = new mf_ProofStepChain(c.getTail());
			dropped.setProofStep(new mf_ProofStepChainDropped(dropped, c));
			return dropped;
		}

		return c;
	}

	class AnswerHandler implements mf_i_InferenceResult {
		private mf_ProofStepChain answerChain = new mf_ProofStepChain();
		private Set<mf_NodeTermVariable> answerLiteralVariables;
		private List<mf_ProofStepChain> sos = null;
		private boolean complete = false;
		private long finishTime = 0L;
		private int maxDepthReached = 0;
		private List<mf_i_Proof> proofs = new ArrayList<mf_i_Proof>();
		private boolean timedOut = false;

		public AnswerHandler(mf_KnowledgeBase kb, mf_i_Sentence query,
				long maxQueryTime) {

			finishTime = System.currentTimeMillis() + maxQueryTime;

			mf_i_Sentence refutationQuery = new mf_SentenceNot(query);

			// Want to use an answer literal to pull
			// query variables where necessary
			mf_Literal answerLiteral = kb.createAnswerLiteral(refutationQuery);
			answerLiteralVariables = kb.collectAllVariables(answerLiteral
					.getAtomicSentence());

			// Create the Set of Support based on the Query.
			if (answerLiteralVariables.size() > 0) {
				mf_i_Sentence refutationQueryWithAnswer = new mf_SentenceConnected(
						mf_SymbolsConnectors.OR, refutationQuery, answerLiteral
								.getAtomicSentence().copy());

				sos = createChainsFromClauses(kb
						.convertToClauses(refutationQueryWithAnswer));

				answerChain.addLiteral(answerLiteral);
			} else {
				sos = createChainsFromClauses(kb
						.convertToClauses(refutationQuery));
			}

			for (mf_ProofStepChain s : sos) {
				s.setProofStep(new mf_ProofStepGoal(s));
			}
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

		public List<mf_ProofStepChain> getSetOfSupport() {
			return sos;
		}

		public boolean isComplete() {
			return complete;
		}

		public void resetMaxDepthReached() {
			maxDepthReached = 0;
		}

		public int getMaxDepthReached() {
			return maxDepthReached;
		}

		public void updateMaxDepthReached(int depth) {
			if (depth > maxDepthReached) {
				maxDepthReached = depth;
			}
		}

		public boolean isAnswer(mf_ProofStepChain nearParent) {
			boolean isAns = false;
			if (answerChain.isEmpty()) {
				if (nearParent.isEmpty()) {
					proofs.add(new mf_ProofFinal(nearParent.getProofStep(),
							new HashMap<mf_NodeTermVariable, mf_i_NodeTerm>()));
					complete = true;
					isAns = true;
				}
			} else {
				if (nearParent.isEmpty()) {
					// This should not happen
					// as added an answer literal to sos, which
					// implies the database (i.e. premises) are
					// unsatisfiable to begin with.
					throw new IllegalStateException(
							"Generated an empty chain while looking for an answer, implies original KB is unsatisfiable");
				}
				if (1 == nearParent.getNumberLiterals()
						&& nearParent
								.getHead()
								.getAtomicSentence()
								.getSymbolicName()
								.equals(answerChain.getHead()
										.getAtomicSentence().getSymbolicName())) {
					Map<mf_NodeTermVariable, mf_i_NodeTerm> answerBindings = new HashMap<mf_NodeTermVariable, mf_i_NodeTerm>();
					List<mf_i_NodeTerm> answerTerms = nearParent.getHead()
							.getAtomicSentence().getArgs();
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
						proofs.add(new mf_ProofFinal(nearParent.getProofStep(),
								answerBindings));
					}
					isAns = true;
				}
			}

			if (System.currentTimeMillis() > finishTime) {
				complete = true;
				// Indicate that I have run out of query time
				timedOut = true;
			}

			return isAns;
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

class IndexedFarParents {
	//
	private int saIdx = 0;
	private ml_Unifier unifier = new ml_Unifier();
	private mf_VisitorSubstitution substVisitor = new mf_VisitorSubstitution();
	//
	private Map<String, List<mf_ProofStepChain>> posHeads = new LinkedHashMap<String, List<mf_ProofStepChain>>();
	private Map<String, List<mf_ProofStepChain>> negHeads = new LinkedHashMap<String, List<mf_ProofStepChain>>();

	public IndexedFarParents(List<mf_ProofStepChain> sos, List<mf_ProofStepChain> background) {
		constructInternalDataStructures(sos, background);
	}

	public int getNumberFarParents(mf_ProofStepChain farParent) {
		mf_Literal head = farParent.getHead();

		Map<String, List<mf_ProofStepChain>> heads = null;
		if (head.isPositiveLiteral()) {
			heads = posHeads;
		} else {
			heads = negHeads;
		}
		String headKey = head.getAtomicSentence().getSymbolicName();

		List<mf_ProofStepChain> farParents = heads.get(headKey);
		if (null != farParents) {
			return farParents.size();
		}
		return 0;
	}

	public void resetNumberFarParentsTo(mf_ProofStepChain farParent, int toSize) {
		mf_Literal head = farParent.getHead();
		Map<String, List<mf_ProofStepChain>> heads = null;
		if (head.isPositiveLiteral()) {
			heads = posHeads;
		} else {
			heads = negHeads;
		}
		String key = head.getAtomicSentence().getSymbolicName();
		List<mf_ProofStepChain> farParents = heads.get(key);
		while (farParents.size() > toSize) {
			farParents.remove(farParents.size() - 1);
		}
	}

	public int getNumberCandidateFarParents(mf_ProofStepChain nearParent) {
		mf_Literal nearestHead = nearParent.getHead();

		Map<String, List<mf_ProofStepChain>> candidateHeads = null;
		if (nearestHead.isPositiveLiteral()) {
			candidateHeads = negHeads;
		} else {
			candidateHeads = posHeads;
		}

		String nearestKey = nearestHead.getAtomicSentence().getSymbolicName();

		List<mf_ProofStepChain> farParents = candidateHeads.get(nearestKey);
		if (null != farParents) {
			return farParents.size();
		}
		return 0;
	}

	public mf_ProofStepChain attemptReduction(mf_ProofStepChain nearParent, int farParentIndex) {
		mf_ProofStepChain nnpc = null;

		mf_Literal nearLiteral = nearParent.getHead();

		Map<String, List<mf_ProofStepChain>> candidateHeads = null;
		if (nearLiteral.isPositiveLiteral()) {
			candidateHeads = negHeads;
		} else {
			candidateHeads = posHeads;
		}

		mf_i_SentenceAtomic nearAtom = nearLiteral.getAtomicSentence();
		String nearestKey = nearAtom.getSymbolicName();
		List<mf_ProofStepChain> farParents = candidateHeads.get(nearestKey);
		if (null != farParents) {
			mf_ProofStepChain farParent = farParents.get(farParentIndex);
			standardizeApart(farParent);
			mf_Literal farLiteral = farParent.getHead();
			mf_i_SentenceAtomic farAtom = farLiteral.getAtomicSentence();
			Map<mf_NodeTermVariable, mf_i_NodeTerm> subst = unifier.unify(nearAtom, farAtom);

			// If I was able to unify with one
			// of the far heads
			if (null != subst) {
				// Want to always apply reduction uniformly
				mf_ProofStepChain topChain = farParent;
				mf_Literal botLit = nearLiteral;
				mf_ProofStepChain botChain = nearParent;

				// Need to apply subst to all of the
				// literals in the reduction
				List<mf_Literal> reduction = new ArrayList<mf_Literal>();
				for (mf_Literal l : topChain.getTail()) {
					mf_i_SentenceAtomic atom = (mf_i_SentenceAtomic) substVisitor.subst(
							subst, l.getAtomicSentence());
					reduction.add(l.newInstance(atom));
				}
				reduction.add(new mf_LiteralReduced((mf_i_SentenceAtomic) substVisitor
						.subst(subst, botLit.getAtomicSentence()), botLit
						.isNegativeLiteral()));
				for (mf_Literal l : botChain.getTail()) {
					mf_i_SentenceAtomic atom = (mf_i_SentenceAtomic) substVisitor.subst(
							subst, l.getAtomicSentence());
					reduction.add(l.newInstance(atom));
				}

				nnpc = new mf_ProofStepChain(reduction);
				nnpc.setProofStep(new mf_ProofStepChainReduction(nnpc, nearParent,
						farParent, subst));
			}
		}

		return nnpc;
	}

	public mf_ProofStepChain addToIndex(mf_ProofStepChain c) {
		mf_ProofStepChain added = null;
		mf_Literal head = c.getHead();
		if (null != head) {
			Map<String, List<mf_ProofStepChain>> toAddTo = null;
			if (head.isPositiveLiteral()) {
				toAddTo = posHeads;
			} else {
				toAddTo = negHeads;
			}

			String key = head.getAtomicSentence().getSymbolicName();
			List<mf_ProofStepChain> farParents = toAddTo.get(key);
			if (null == farParents) {
				farParents = new ArrayList<mf_ProofStepChain>();
				toAddTo.put(key, farParents);
			}

			added = c;
			farParents.add(added);
		}
		return added;
	}

	public void standardizeApart(mf_ProofStepChain c) {
		saIdx = mf_StandardizeApartInPlace.standardizeApart(c, saIdx);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("#");
		sb.append(posHeads.size());
		for (String key : posHeads.keySet()) {
			sb.append(",");
			sb.append(posHeads.get(key).size());
		}
		sb.append(" posHeads=");
		sb.append(posHeads.toString());
		sb.append("\n");
		sb.append("#");
		sb.append(negHeads.size());
		for (String key : negHeads.keySet()) {
			sb.append(",");
			sb.append(negHeads.get(key).size());
		}
		sb.append(" negHeads=");
		sb.append(negHeads.toString());

		return sb.toString();
	}

	//
	// PRIVATE METHODS
	//
	private void constructInternalDataStructures(List<mf_ProofStepChain> sos,
			List<mf_ProofStepChain> background) {
		List<mf_ProofStepChain> toIndex = new ArrayList<mf_ProofStepChain>();
		toIndex.addAll(sos);
		toIndex.addAll(background);

		for (mf_ProofStepChain c : toIndex) {
			addToIndex(c);
		}
	}
}