package com.software.reuze;
//package aima.core.logic.fol;

import java.util.LinkedHashSet;
import java.util.Set;

/*import aima.core.logic.fol.kb.data.Chain;
import aima.core.logic.fol.kb.data.Clause;
import aima.core.logic.fol.kb.data.Literal;
import aima.core.logic.fol.parsing.FOLVisitor;
import aima.core.logic.fol.parsing.ast.ConnectedSentence;
import aima.core.logic.fol.parsing.ast.Constant;
import aima.core.logic.fol.parsing.ast.Function;
import aima.core.logic.fol.parsing.ast.NotSentence;
import aima.core.logic.fol.parsing.ast.Predicate;
import aima.core.logic.fol.parsing.ast.QuantifiedSentence;
import aima.core.logic.fol.parsing.ast.FOSentence;
import aima.core.logic.fol.parsing.ast.Term;
import aima.core.logic.fol.parsing.ast.TermEquality;
import aima.core.logic.fol.parsing.ast.Variable;*/

/**
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 */
public class mf_VariableCollector implements mf_Visitor {

	public mf_VariableCollector() {
	}

	// Note: The set guarantees the order in which they were
	// found.
	public Set<mf_NodeTermVariable> collectAllVariables(mf_i_Sentence sentence) {
		Set<mf_NodeTermVariable> variables = new LinkedHashSet<mf_NodeTermVariable>();

		sentence.accept(this, variables);

		return variables;
	}

	public Set<mf_NodeTermVariable> collectAllVariables(mf_i_NodeTerm term) {
		Set<mf_NodeTermVariable> variables = new LinkedHashSet<mf_NodeTermVariable>();

		term.accept(this, variables);

		return variables;
	}

	public Set<mf_NodeTermVariable> collectAllVariables(mf_Clause clause) {
		Set<mf_NodeTermVariable> variables = new LinkedHashSet<mf_NodeTermVariable>();

		for (mf_Literal l : clause.getLiterals()) {
			l.getAtomicSentence().accept(this, variables);
		}

		return variables;
	}

	public Set<mf_NodeTermVariable> collectAllVariables(mf_ProofStepChain chain) {
		Set<mf_NodeTermVariable> variables = new LinkedHashSet<mf_NodeTermVariable>();

		for (mf_Literal l : chain.getLiterals()) {
			l.getAtomicSentence().accept(this, variables);
		}

		return variables;
	}

	@SuppressWarnings("unchecked")
	public Object visitVariable(mf_NodeTermVariable var, Object arg) {
		Set<mf_NodeTermVariable> variables = (Set<mf_NodeTermVariable>) arg;
		variables.add(var);
		return var;
	}

	@SuppressWarnings("unchecked")
	public Object visitQuantifiedSentence(mf_SentenceQuantified sentence,
			Object arg) {
		// Ensure I collect quantified variables too
		Set<mf_NodeTermVariable> variables = (Set<mf_NodeTermVariable>) arg;
		variables.addAll(sentence.getVariables());

		sentence.getQuantified().accept(this, arg);

		return sentence;
	}

	public Object visitPredicate(mf_Predicate predicate, Object arg) {
		for (mf_i_NodeTerm t : predicate.getTerms()) {
			t.accept(this, arg);
		}
		return predicate;
	}

	public Object visitTermEquality(mf_SentenceAtomicTermEquality equality, Object arg) {
		equality.getTerm1().accept(this, arg);
		equality.getTerm2().accept(this, arg);
		return equality;
	}

	public Object visitConstant(mf_SymbolConstant constant, Object arg) {
		return constant;
	}

	public Object visitFunction(mf_NodeTermFunction function, Object arg) {
		for (mf_i_NodeTerm t : function.getTerms()) {
			t.accept(this, arg);
		}
		return function;
	}

	public Object visitNotSentence(mf_SentenceNot sentence, Object arg) {
		sentence.getNegated().accept(this, arg);
		return sentence;
	}

	public Object visitConnectedSentence(mf_SentenceConnected sentence, Object arg) {
		sentence.getFirst().accept(this, arg);
		sentence.getSecond().accept(this, arg);
		return sentence;
	}
}