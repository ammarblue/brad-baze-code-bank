package com.software.reuze;
//package aima.core.logic.fol.inference;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/*import aima.core.logic.fol.SubstVisitor;
import aima.core.logic.fol.Unifier;
import aima.core.logic.fol.VariableCollector;
import aima.core.logic.fol.parsing.FOLVisitor;
import aima.core.logic.fol.parsing.ast.FOAtomicSentence;
import aima.core.logic.fol.parsing.ast.ConnectedSentence;
import aima.core.logic.fol.parsing.ast.Constant;
import aima.core.logic.fol.parsing.ast.Function;
import aima.core.logic.fol.parsing.ast.NotSentence;
import aima.core.logic.fol.parsing.ast.Predicate;
import aima.core.logic.fol.parsing.ast.QuantifiedSentence;
import aima.core.logic.fol.parsing.ast.Term;
import aima.core.logic.fol.parsing.ast.TermEquality;
import aima.core.logic.fol.parsing.ast.Variable;*/

/**
 * Abstract base class for Demodulation and Paramodulation algorithms.
 * 
 * @author Ciaran O'Reilly
 * 
 */
public abstract class mf_a_Modulation {
	//
	// PROTECTED ATTRIBUTES
	protected mf_VariableCollector variableCollector = new mf_VariableCollector();
	protected ml_Unifier unifier = new ml_Unifier();
	protected mf_VisitorSubstitution substVisitor = new mf_VisitorSubstitution();

	//
	// PROTECTED METODS
	//
	protected abstract boolean isValidMatch(mf_i_NodeTerm toMatch,
			Set<mf_NodeTermVariable> toMatchVariables, mf_i_NodeTerm possibleMatch,
			Map<mf_NodeTermVariable, mf_i_NodeTerm> substitution);

	protected IdentifyCandidateMatchingTerm getMatchingSubstitution(
			mf_i_NodeTerm toMatch, mf_i_SentenceAtomic expression) {

		IdentifyCandidateMatchingTerm icm = new IdentifyCandidateMatchingTerm(
				toMatch, expression);

		if (icm.isMatch()) {
			return icm;
		}

		// indicates no match
		return null;
	}

	protected class IdentifyCandidateMatchingTerm implements mf_Visitor {
		private mf_i_NodeTerm toMatch = null;
		private Set<mf_NodeTermVariable> toMatchVariables = null;
		private mf_i_NodeTerm matchingTerm = null;
		private Map<mf_NodeTermVariable, mf_i_NodeTerm> substitution = null;

		public IdentifyCandidateMatchingTerm(mf_i_NodeTerm toMatch,
				mf_i_SentenceAtomic expression) {
			this.toMatch = toMatch;
			this.toMatchVariables = variableCollector
					.collectAllVariables(toMatch);

			expression.accept(this, null);
		}

		public boolean isMatch() {
			return null != matchingTerm;
		}

		public mf_i_NodeTerm getMatchingTerm() {
			return matchingTerm;
		}

		public Map<mf_NodeTermVariable, mf_i_NodeTerm> getMatchingSubstitution() {
			return substitution;
		}

		//
		// START-FOLVisitor
		public Object visitPredicate(mf_Predicate p, Object arg) {
			for (mf_i_NodeTerm t : p.getArgs()) {
				// Finish processing if have found a match
				if (null != matchingTerm) {
					break;
				}
				t.accept(this, null);
			}
			return p;
		}

		public Object visitTermEquality(mf_SentenceAtomicTermEquality equality, Object arg) {
			for (mf_i_NodeTerm t : equality.getArgs()) {
				// Finish processing if have found a match
				if (null != matchingTerm) {
					break;
				}
				t.accept(this, null);
			}
			return equality;
		}

		public Object visitVariable(mf_NodeTermVariable variable, Object arg) {

			if (null != (substitution = unifier.unify(toMatch, variable))) {
				if (isValidMatch(toMatch, toMatchVariables, variable,
						substitution)) {
					matchingTerm = variable;
				}
			}

			return variable;
		}

		public Object visitConstant(mf_SymbolConstant constant, Object arg) {
			if (null != (substitution = unifier.unify(toMatch, constant))) {
				if (isValidMatch(toMatch, toMatchVariables, constant,
						substitution)) {
					matchingTerm = constant;
				}
			}

			return constant;
		}

		public Object visitFunction(mf_NodeTermFunction function, Object arg) {
			if (null != (substitution = unifier.unify(toMatch, function))) {
				if (isValidMatch(toMatch, toMatchVariables, function,
						substitution)) {
					matchingTerm = function;
				}
			}

			if (null == matchingTerm) {
				// Try the Function's arguments
				for (mf_i_NodeTerm t : function.getArgs()) {
					// Finish processing if have found a match
					if (null != matchingTerm) {
						break;
					}
					t.accept(this, null);
				}
			}

			return function;
		}

		public Object visitNotSentence(mf_SentenceNot sentence, Object arg) {
			throw new IllegalStateException(
					"visitNotSentence() should not be called.");
		}

		public Object visitConnectedSentence(mf_SentenceConnected sentence,
				Object arg) {
			throw new IllegalStateException(
					"visitConnectedSentence() should not be called.");
		}

		public Object visitQuantifiedSentence(mf_SentenceQuantified sentence,
				Object arg) {
			throw new IllegalStateException(
					"visitQuantifiedSentence() should not be called.");
		}

		// END-FOLVisitor
		//
	}

	protected class ReplaceMatchingTerm implements mf_Visitor {
		private mf_i_NodeTerm toReplace = null;
		private mf_i_NodeTerm replaceWith = null;
		private boolean replaced = false;

		public ReplaceMatchingTerm() {
		}

		public mf_i_SentenceAtomic replace(mf_i_SentenceAtomic expression,
				mf_i_NodeTerm toReplace, mf_i_NodeTerm replaceWith) {
			this.toReplace = toReplace;
			this.replaceWith = replaceWith;

			return (mf_i_SentenceAtomic) expression.accept(this, null);
		}

		//
		// START-FOLVisitor
		public Object visitPredicate(mf_Predicate p, Object arg) {
			List<mf_i_NodeTerm> newTerms = new ArrayList<mf_i_NodeTerm>();
			for (mf_i_NodeTerm t : p.getTerms()) {
				mf_i_NodeTerm subsTerm = (mf_i_NodeTerm) t.accept(this, arg);
				newTerms.add(subsTerm);
			}
			return new mf_Predicate(p.getPredicateName(), newTerms);
		}

		public Object visitTermEquality(mf_SentenceAtomicTermEquality equality, Object arg) {
			mf_i_NodeTerm newTerm1 = (mf_i_NodeTerm) equality.getTerm1().accept(this, arg);
			mf_i_NodeTerm newTerm2 = (mf_i_NodeTerm) equality.getTerm2().accept(this, arg);
			return new mf_SentenceAtomicTermEquality(newTerm1, newTerm2);
		}

		public Object visitVariable(mf_NodeTermVariable variable, Object arg) {
			if (!replaced) {
				if (toReplace.equals(variable)) {
					replaced = true;
					return replaceWith;
				}
			}
			return variable;
		}

		public Object visitConstant(mf_SymbolConstant constant, Object arg) {
			if (!replaced) {
				if (toReplace.equals(constant)) {
					replaced = true;
					return replaceWith;
				}
			}
			return constant;
		}

		public Object visitFunction(mf_NodeTermFunction function, Object arg) {
			if (!replaced) {
				if (toReplace.equals(function)) {
					replaced = true;
					return replaceWith;
				}
			}

			List<mf_i_NodeTerm> newTerms = new ArrayList<mf_i_NodeTerm>();
			for (mf_i_NodeTerm t : function.getTerms()) {
				mf_i_NodeTerm subsTerm = (mf_i_NodeTerm) t.accept(this, arg);
				newTerms.add(subsTerm);
			}
			return new mf_NodeTermFunction(function.getFunctionName(), newTerms);
		}

		public Object visitNotSentence(mf_SentenceNot sentence, Object arg) {
			throw new IllegalStateException(
					"visitNotSentence() should not be called.");
		}

		public Object visitConnectedSentence(mf_SentenceConnected sentence,
				Object arg) {
			throw new IllegalStateException(
					"visitConnectedSentence() should not be called.");
		}

		public Object visitQuantifiedSentence(mf_SentenceQuantified sentence,
				Object arg) {
			throw new IllegalStateException(
					"visitQuantifiedSentence() should not be called.");
		}

		// END-FOLVisitor
		//
	}
}
