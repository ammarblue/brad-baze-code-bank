package com.software.reuze;
//package aima.core.logic.fol.inference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/*import aima.core.logic.fol.Connectors;
import aima.core.logic.fol.SubsumptionElimination;
import aima.core.logic.fol.inference.otter.ClauseFilter;
import aima.core.logic.fol.inference.otter.ClauseSimplifier;
import aima.core.logic.fol.inference.otter.LightestClauseHeuristic;
import aima.core.logic.fol.inference.otter.defaultimpl.DefaultClauseFilter;
import aima.core.logic.fol.inference.otter.defaultimpl.DefaultClauseSimplifier;
import aima.core.logic.fol.inference.otter.defaultimpl.DefaultLightestClauseHeuristic;
import aima.core.logic.fol.inference.proof.Proof;
import aima.core.logic.fol.inference.proof.ProofFinal;
import aima.core.logic.fol.inference.proof.ProofStepGoal;
import aima.core.logic.fol.kb.FOLKnowledgeBase;
import aima.core.logic.fol.kb.data.Clause;
import aima.core.logic.fol.kb.data.Literal;
import aima.core.logic.fol.parsing.ast.ConnectedSentence;
import aima.core.logic.fol.parsing.ast.NotSentence;
import aima.core.logic.fol.parsing.ast.FOSentence;
import aima.core.logic.fol.parsing.ast.Term;
import aima.core.logic.fol.parsing.ast.TermEquality;
import aima.core.logic.fol.parsing.ast.Variable;*/

/**
 * Artificial Intelligence A Modern Approach (2nd Edition): Figure 9.14, page
 * 307.<br>
 * <br>
 * 
 * <pre>
 * procedure OTTER(sos, usable)
 *   inputs: sos, a set of support-clauses defining the problem (a global variable)
 *   usable, background knowledge potentially relevant to the problem
 *   
 *   repeat
 *      clause <- the lightest member of sos
 *      move clause from sos to usable
 *      PROCESS(INFER(clause, usable), sos)
 *   until sos = [] or a refutation has been found
 * 
 * --------------------------------------------------------------------------------
 * 
 * function INFER(clause, usable) returns clauses
 *   
 *   resolve clause with each member of usable
 *   return the resulting clauses after applying filter
 *   
 * --------------------------------------------------------------------------------
 * 
 * procedure PROCESS(clauses, sos)
 * 
 *   for each clause in clauses do
 *       clause <- SIMPLIFY(clause)
 *       merge identical literals
 *       discard clause if it is a tautology
 *       sos <- [clause | sos]
 *       if clause has no literals then a refutation has been found
 *       if clause has one literal then look for unit refutation
 * </pre>
 * 
 * Figure 9.14 Sketch of the OTTER theorem prover. Heuristic control is applied
 * in the selection of the "lightest" clause and in the FILTER function that
 * eliminates uninteresting clauses from consideration.<br>
 * <br>
 * <b>Note:</b> The original implementation of OTTER has been retired but its
 * successor, <b>Prover9</b>, can be found at:<br>
 * <a href="http://www.prover9.org/">http://www.prover9.org/</a><br>
 * or<br>
 * <a href="http://www.cs.unm.edu/~mccune/mace4/">http://www.cs.unm.edu/~mccune/
 * mace4/</a><br>
 * Should you wish to play with a mature implementation of a theorem prover :-)<br>
 * <br>
 * For lots of interesting problems to play with, see <b>The TPTP Problem
 * Library for Automated Theorem Proving</b>:<br>
 * <a href="http://www.cs.miami.edu/~tptp/">http://www.cs.miami.edu/~tptp/</a><br>
 * 
 * @author Ciaran O'Reilly
 * 
 */
public class mf_OTTERLikeTheoremProver implements mf_i_InferenceProcedure {
	//
	// Ten seconds is default maximum query time permitted
	private long maxQueryTime = 10 * 1000;
	private boolean useParamodulation = true;
	private mf_InferenceOtterHeuristicLightestClause lightestClauseHeuristic = new mf_InferenceOtterHeuristicLightestClauseDefault();
	private mf_i_ClauseFilter clauseFilter = new mf_ClauseFilterDefault();
	private mf_i_ClauseSimplifier clauseSimplifier = new mf_ClauseSimplifierDefault();
	//
	private mf_Paramodulation paramodulation = new mf_Paramodulation();

	public mf_OTTERLikeTheoremProver() {

	}

	public mf_OTTERLikeTheoremProver(long maxQueryTime) {
		setMaxQueryTime(maxQueryTime);
	}

	public mf_OTTERLikeTheoremProver(boolean useParamodulation) {
		setUseParamodulation(useParamodulation);
	}

	public mf_OTTERLikeTheoremProver(long maxQueryTime,
			boolean useParamodulation) {
		setMaxQueryTime(maxQueryTime);
		setUseParamodulation(useParamodulation);
	}

	public long getMaxQueryTime() {
		return maxQueryTime;
	}

	public void setMaxQueryTime(long maxQueryTime) {
		this.maxQueryTime = maxQueryTime;
	}

	public boolean isUseParamodulation() {
		return useParamodulation;
	}

	public void setUseParamodulation(boolean useParamodulation) {
		this.useParamodulation = useParamodulation;
	}

	public mf_InferenceOtterHeuristicLightestClause getLightestClauseHeuristic() {
		return lightestClauseHeuristic;
	}

	public void setLightestClauseHeuristic(
			mf_InferenceOtterHeuristicLightestClause lightestClauseHeuristic) {
		this.lightestClauseHeuristic = lightestClauseHeuristic;
	}

	public mf_i_ClauseFilter getClauseFilter() {
		return clauseFilter;
	}

	public void setClauseFilter(mf_i_ClauseFilter clauseFilter) {
		this.clauseFilter = clauseFilter;
	}

	public mf_i_ClauseSimplifier getClauseSimplifier() {
		return clauseSimplifier;
	}

	public void setClauseSimplifier(mf_i_ClauseSimplifier clauseSimplifier) {
		this.clauseSimplifier = clauseSimplifier;
	}

	//
	// START-InferenceProcedure
	public mf_i_InferenceResult ask(mf_KnowledgeBase KB, mf_i_Sentence alpha) {
		Set<mf_Clause> sos = new HashSet<mf_Clause>();
		Set<mf_Clause> usable = new HashSet<mf_Clause>();

		// Usable set will be the set of clauses in the KB,
		// are assuming this is satisfiable as using the
		// Set of Support strategy.
		for (mf_Clause c : KB.getAllClauses()) {
			c = KB.standardizeApart(c);
			c.setStandardizedApartCheckNotRequired();
			usable.addAll(c.getFactors());
		}

		// Ensure reflexivity axiom is added to usable if using paramodulation.
		if (isUseParamodulation()) {
			// Reflexivity Axiom: x = x
			mf_SentenceAtomicTermEquality reflexivityAxiom = new mf_SentenceAtomicTermEquality(new mf_NodeTermVariable("x"),
					new mf_NodeTermVariable("x"));
			mf_Clause reflexivityClause = new mf_Clause();
			reflexivityClause.addLiteral(new mf_Literal(reflexivityAxiom));
			reflexivityClause = KB.standardizeApart(reflexivityClause);
			reflexivityClause.setStandardizedApartCheckNotRequired();
			usable.add(reflexivityClause);
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
				sos.addAll(c.getFactors());
			}

			answerClause.addLiteral(answerLiteral);
		} else {
			for (mf_Clause c : KB.convertToClauses(notAlpha)) {
				c = KB.standardizeApart(c);
				c.setProofStep(new mf_ProofStepGoal(c));
				c.setStandardizedApartCheckNotRequired();
				sos.addAll(c.getFactors());
			}
		}

		// Ensure all subsumed clauses are removed
		usable.removeAll(mf_SubsumptionElimination.findSubsumedClauses(usable));
		sos.removeAll(mf_SubsumptionElimination.findSubsumedClauses(sos));

		OTTERAnswerHandler ansHandler = new OTTERAnswerHandler(answerLiteral,
				answerLiteralVariables, answerClause, maxQueryTime);

		IndexedClauses idxdClauses = new IndexedClauses(
				getLightestClauseHeuristic(), sos, usable);

		return otter(ansHandler, idxdClauses, sos, usable);
	}

	// END-InferenceProcedure
	//

	/**
	 * <pre>
	 * procedure OTTER(sos, usable) 
	 *   inputs: sos, a set of support-clauses defining the problem (a global variable) 
	 *   usable, background knowledge potentially relevant to the problem
	 * </pre>
	 */
	private mf_i_InferenceResult otter(OTTERAnswerHandler ansHandler,
			IndexedClauses idxdClauses, Set<mf_Clause> sos, Set<mf_Clause> usable) {

		getLightestClauseHeuristic().initialSOS(sos);

		// * repeat
		do {
			// * clause <- the lightest member of sos
			mf_Clause clause = getLightestClauseHeuristic().getLightestClause();
			if (null != clause) {
				// * move clause from sos to usable
				sos.remove(clause);
				getLightestClauseHeuristic().removedClauseFromSOS(clause);
				usable.add(clause);
				// * PROCESS(INFER(clause, usable), sos)
				process(ansHandler, idxdClauses, infer(clause, usable), sos,
						usable);
			}

			// * until sos = [] or a refutation has been found
		} while (sos.size() != 0 && !ansHandler.isComplete());

		return ansHandler;
	}

	/**
	 * <pre>
	 * function INFER(clause, usable) returns clauses
	 */
	private Set<mf_Clause> infer(mf_Clause clause, Set<mf_Clause> usable) {
		Set<mf_Clause> resultingClauses = new LinkedHashSet<mf_Clause>();

		// * resolve clause with each member of usable
		for (mf_Clause c : usable) {
			Set<mf_Clause> resolvents = clause.binaryResolvents(c);
			for (mf_Clause rc : resolvents) {
				resultingClauses.add(rc);
			}

			// if using paramodulation to handle equality
			if (isUseParamodulation()) {
				Set<mf_Clause> paras = paramodulation.apply(clause, c, true);
				for (mf_Clause p : paras) {
					resultingClauses.add(p);
				}
			}
		}

		// * return the resulting clauses after applying filter
		return getClauseFilter().filter(resultingClauses);
	}

	// procedure PROCESS(clauses, sos)
	private void process(OTTERAnswerHandler ansHandler,
			IndexedClauses idxdClauses, Set<mf_Clause> clauses, Set<mf_Clause> sos,
			Set<mf_Clause> usable) {

		// * for each clause in clauses do
		for (mf_Clause clause : clauses) {
			// * clause <- SIMPLIFY(clause)
			clause = getClauseSimplifier().simplify(clause);

			// * merge identical literals
			// Note: Not required as handled by Clause Implementation
			// which keeps literals within a Set, so no duplicates
			// will exist.

			// * discard clause if it is a tautology
			if (clause.isTautology()) {
				continue;
			}

			// * if clause has no literals then a refutation has been found
			// or if it just contains the answer literal.
			if (!ansHandler.isAnswer(clause)) {
				// * sos <- [clause | sos]
				// This check ensure duplicate clauses are not
				// introduced which will cause the
				// LightestClauseHeuristic to loop continuously
				// on the same pair of objects.
				if (!sos.contains(clause) && !usable.contains(clause)) {
					for (mf_Clause ac : clause.getFactors()) {
						if (!sos.contains(ac) && !usable.contains(ac)) {
							idxdClauses.addClause(ac, sos, usable);

							// * if clause has one literal then look for unit
							// refutation
							lookForUnitRefutation(ansHandler, idxdClauses, ac,
									sos, usable);
						}
					}
				}
			}

			if (ansHandler.isComplete()) {
				break;
			}
		}
	}

	private void lookForUnitRefutation(OTTERAnswerHandler ansHandler,
			IndexedClauses idxdClauses, mf_Clause clause, Set<mf_Clause> sos,
			Set<mf_Clause> usable) {

		Set<mf_Clause> toCheck = new LinkedHashSet<mf_Clause>();

		if (ansHandler.isCheckForUnitRefutation(clause)) {
			for (mf_Clause s : sos) {
				if (s.isUnitClause()) {
					toCheck.add(s);
				}
			}
			for (mf_Clause u : usable) {
				if (u.isUnitClause()) {
					toCheck.add(u);
				}
			}
		}

		if (toCheck.size() > 0) {
			toCheck = infer(clause, toCheck);
			for (mf_Clause t : toCheck) {
				// * clause <- SIMPLIFY(clause)
				t = getClauseSimplifier().simplify(t);

				// * discard clause if it is a tautology
				if (t.isTautology()) {
					continue;
				}

				// * if clause has no literals then a refutation has been found
				// or if it just contains the answer literal.
				if (!ansHandler.isAnswer(t)) {
					// * sos <- [clause | sos]
					// This check ensure duplicate clauses are not
					// introduced which will cause the
					// LightestClauseHeuristic to loop continuously
					// on the same pair of objects.
					if (!sos.contains(t) && !usable.contains(t)) {
						idxdClauses.addClause(t, sos, usable);
					}
				}

				if (ansHandler.isComplete()) {
					break;
				}
			}
		}
	}

	// This is a simple indexing on the clauses to support
	// more efficient forward and backward subsumption testing.
	class IndexedClauses {
		private mf_InferenceOtterHeuristicLightestClause lightestClauseHeuristic = null;
		// Group the clauses by their # of literals.
		private Map<Integer, Set<mf_Clause>> clausesGroupedBySize = new HashMap<Integer, Set<mf_Clause>>();
		// Keep track of the min and max # of literals.
		private int minNoLiterals = Integer.MAX_VALUE;
		private int maxNoLiterals = 0;

		public IndexedClauses(mf_InferenceOtterHeuristicLightestClause lightestClauseHeuristic,
				Set<mf_Clause> sos, Set<mf_Clause> usable) {
			this.lightestClauseHeuristic = lightestClauseHeuristic;
			for (mf_Clause c : sos) {
				indexClause(c);
			}
			for (mf_Clause c : usable) {
				indexClause(c);
			}
		}

		public void addClause(mf_Clause c, Set<mf_Clause> sos, Set<mf_Clause> usable) {
			// Perform forward subsumption elimination
			boolean addToSOS = true;
			for (int i = minNoLiterals; i < c.getNumberLiterals(); i++) {
				Set<mf_Clause> fs = clausesGroupedBySize.get(i);
				if (null != fs) {
					for (mf_Clause s : fs) {
						if (s.subsumes(c)) {
							addToSOS = false;
							break;
						}
					}
				}
				if (!addToSOS) {
					break;
				}
			}

			if (addToSOS) {
				sos.add(c);
				lightestClauseHeuristic.addedClauseToSOS(c);
				indexClause(c);
				// Have added clause, therefore
				// perform backward subsumption elimination
				Set<mf_Clause> subsumed = new HashSet<mf_Clause>();
				for (int i = c.getNumberLiterals() + 1; i <= maxNoLiterals; i++) {
					subsumed.clear();
					Set<mf_Clause> bs = clausesGroupedBySize.get(i);
					if (null != bs) {
						for (mf_Clause s : bs) {
							if (c.subsumes(s)) {
								subsumed.add(s);
								if (sos.contains(s)) {
									sos.remove(s);
									lightestClauseHeuristic
											.removedClauseFromSOS(s);
								}
								usable.remove(s);
							}
						}
						bs.removeAll(subsumed);
					}
				}
			}
		}

		//
		// PRIVATE METHODS
		//
		private void indexClause(mf_Clause c) {
			int size = c.getNumberLiterals();
			if (size < minNoLiterals) {
				minNoLiterals = size;
			}
			if (size > maxNoLiterals) {
				maxNoLiterals = size;
			}
			Set<mf_Clause> cforsize = clausesGroupedBySize.get(size);
			if (null == cforsize) {
				cforsize = new HashSet<mf_Clause>();
				clausesGroupedBySize.put(size, cforsize);
			}
			cforsize.add(c);
		}
	}

	class OTTERAnswerHandler implements mf_i_InferenceResult {
		private mf_Literal answerLiteral = null;
		private Set<mf_NodeTermVariable> answerLiteralVariables = null;
		private mf_Clause answerClause = null;
		private long finishTime = 0L;
		private boolean complete = false;
		private List<mf_i_Proof> proofs = new ArrayList<mf_i_Proof>();
		private boolean timedOut = false;

		public OTTERAnswerHandler(mf_Literal answerLiteral,
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

		public boolean isLookingForAnswerLiteral() {
			return !answerClause.isEmpty();
		}

		public boolean isCheckForUnitRefutation(mf_Clause clause) {

			if (isLookingForAnswerLiteral()) {
				if (2 == clause.getNumberLiterals()) {
					for (mf_Literal t : clause.getLiterals()) {
						if (t.getAtomicSentence()
								.getSymbolicName()
								.equals(answerLiteral.getAtomicSentence()
										.getSymbolicName())) {
							return true;
						}
					}
				}
			} else {
				return clause.isUnitClause();
			}

			return false;
		}

		public boolean isAnswer(mf_Clause clause) {
			boolean isAns = false;

			if (answerClause.isEmpty()) {
				if (clause.isEmpty()) {
					proofs.add(new mf_ProofFinal(clause.getProofStep(),
							new HashMap<mf_NodeTermVariable, mf_i_NodeTerm>()));
					complete = true;
					isAns = true;
				}
			} else {
				if (clause.isEmpty()) {
					// This should not happen
					// as added an answer literal to sos, which
					// implies the database (i.e. premises) are
					// unsatisfiable to begin with.
					throw new IllegalStateException(
							"Generated an empty clause while looking for an answer, implies original KB or usable is unsatisfiable");
				}

				if (clause.isUnitClause()
						&& clause.isDefiniteClause()
						&& clause
								.getPositiveLiterals()
								.get(0)
								.getAtomicSentence()
								.getSymbolicName()
								.equals(answerLiteral.getAtomicSentence()
										.getSymbolicName())) {
					Map<mf_NodeTermVariable, mf_i_NodeTerm> answerBindings = new HashMap<mf_NodeTermVariable, mf_i_NodeTerm>();
					List<mf_i_NodeTerm> answerTerms = clause.getPositiveLiterals()
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
						proofs.add(new mf_ProofFinal(clause.getProofStep(),
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
