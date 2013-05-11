package com.software.reuze;
//package aima.core.logic.fol.parsing;

import java.util.ArrayList;
import java.util.List;

/*import aima.core.logic.fol.parsing.ast.ConnectedSentence;
import aima.core.logic.fol.parsing.ast.Constant;
import aima.core.logic.fol.parsing.ast.Function;
import aima.core.logic.fol.parsing.ast.NotSentence;
import aima.core.logic.fol.parsing.ast.Predicate;
import aima.core.logic.fol.parsing.ast.QuantifiedSentence;
import aima.core.logic.fol.parsing.ast.Sentence;
import aima.core.logic.fol.parsing.ast.Term;
import aima.core.logic.fol.parsing.ast.TermEquality;
import aima.core.logic.fol.parsing.ast.Variable;*/

/**
 * @author Ravi Mohan
 * 
 */
public class mf_a_Visitor implements mf_Visitor {

	public mf_a_Visitor() {
	}

	protected mf_i_Sentence recreate(Object ast) {
		return ((mf_i_Sentence) ast).copy();
	}

	public Object visitVariable(mf_NodeTermVariable variable, Object arg) {
		return variable.copy();
	}

	public Object visitQuantifiedSentence(mf_SentenceQuantified sentence,
			Object arg) {
		List<mf_NodeTermVariable> variables = new ArrayList<mf_NodeTermVariable>();
		for (mf_NodeTermVariable var : sentence.getVariables()) {
			variables.add((mf_NodeTermVariable) var.accept(this, arg));
		}

		return new mf_SentenceQuantified(sentence.getQuantifier(), variables,
				(mf_i_Sentence) sentence.getQuantified().accept(this, arg));
	}

	public Object visitPredicate(mf_Predicate predicate, Object arg) {
		List<mf_i_NodeTerm> terms = predicate.getTerms();
		List<mf_i_NodeTerm> newTerms = new ArrayList<mf_i_NodeTerm>();
		for (int i = 0; i < terms.size(); i++) {
			mf_i_NodeTerm t = terms.get(i);
			mf_i_NodeTerm subsTerm = (mf_i_NodeTerm) t.accept(this, arg);
			newTerms.add(subsTerm);
		}
		return new mf_Predicate(predicate.getPredicateName(), newTerms);

	}

	public Object visitTermEquality(mf_SentenceAtomicTermEquality equality, Object arg) {
		mf_i_NodeTerm newTerm1 = (mf_i_NodeTerm) equality.getTerm1().accept(this, arg);
		mf_i_NodeTerm newTerm2 = (mf_i_NodeTerm) equality.getTerm2().accept(this, arg);
		return new mf_SentenceAtomicTermEquality(newTerm1, newTerm2);
	}

	public Object visitConstant(mf_SymbolConstant constant, Object arg) {
		return constant;
	}

	public Object visitFunction(mf_NodeTermFunction function, Object arg) {
		List<mf_i_NodeTerm> terms = function.getTerms();
		List<mf_i_NodeTerm> newTerms = new ArrayList<mf_i_NodeTerm>();
		for (int i = 0; i < terms.size(); i++) {
			mf_i_NodeTerm t = terms.get(i);
			mf_i_NodeTerm subsTerm = (mf_i_NodeTerm) t.accept(this, arg);
			newTerms.add(subsTerm);
		}
		return new mf_NodeTermFunction(function.getFunctionName(), newTerms);
	}

	public Object visitNotSentence(mf_SentenceNot sentence, Object arg) {
		return new mf_SentenceNot((mf_i_Sentence) sentence.getNegated().accept(this,
				arg));
	}

	public Object visitConnectedSentence(mf_SentenceConnected sentence, Object arg) {
		mf_i_Sentence substFirst = (mf_i_Sentence) sentence.getFirst().accept(this, arg);
		mf_i_Sentence substSecond = (mf_i_Sentence) sentence.getSecond()
				.accept(this, arg);
		return new mf_SentenceConnected(sentence.getConnector(), substFirst,
				substSecond);
	}
}