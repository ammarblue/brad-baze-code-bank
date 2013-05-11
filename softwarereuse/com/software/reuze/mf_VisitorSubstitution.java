package com.software.reuze;
//package aima.core.logic.fol;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/*import aima.core.logic.fol.kb.data.Literal;
import aima.core.logic.fol.parsing.AbstractFOLVisitor;
import aima.core.logic.fol.parsing.ast.AtomicSentence;
import aima.core.logic.fol.parsing.ast.Function;
import aima.core.logic.fol.parsing.ast.QuantifiedSentence;
import aima.core.logic.fol.parsing.ast.Sentence;
import aima.core.logic.fol.parsing.ast.Term;
import aima.core.logic.fol.parsing.ast.Variable;*/

/**
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 */
public class mf_VisitorSubstitution extends mf_a_Visitor {

	public mf_VisitorSubstitution() {
	}

	/**
	 * Note: Refer to Artificial Intelligence A Modern Approach (3rd Edition):
	 * page 323.
	 * 
	 * @param theta
	 *            a substitution.
	 * @param sentence
	 *            the substitution has been applied to.
	 * @return a new Sentence representing the result of applying the
	 *         substitution theta to aSentence.
	 * 
	 */
	public mf_i_Sentence subst(Map<mf_NodeTermVariable, mf_i_NodeTerm> theta, mf_i_Sentence sentence) {
		return (mf_i_Sentence) sentence.accept(this, theta);
	}

	public mf_i_NodeTerm subst(Map<mf_NodeTermVariable, mf_i_NodeTerm> theta, mf_i_NodeTerm aTerm) {
		return (mf_i_NodeTerm) aTerm.accept(this, theta);
	}

	public mf_NodeTermFunction subst(Map<mf_NodeTermVariable, mf_i_NodeTerm> theta, mf_NodeTermFunction function) {
		return (mf_NodeTermFunction) function.accept(this, theta);
	}

	public mf_Literal subst(Map<mf_NodeTermVariable, mf_i_NodeTerm> theta, mf_Literal literal) {
		return literal.newInstance((mf_i_SentenceAtomic) literal
				.getAtomicSentence().accept(this, theta));
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object visitVariable(mf_NodeTermVariable variable, Object arg) {
		Map<mf_NodeTermVariable, mf_i_NodeTerm> substitution = (Map<mf_NodeTermVariable, mf_i_NodeTerm>) arg;
		if (substitution.containsKey(variable)) {
			return substitution.get(variable).copy();
		}
		return variable.copy();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object visitQuantifiedSentence(mf_SentenceQuantified sentence,
			Object arg) {

		Map<mf_NodeTermVariable, mf_i_NodeTerm> substitution = (Map<mf_NodeTermVariable, mf_i_NodeTerm>) arg;

		mf_i_Sentence quantified = sentence.getQuantified();
		mf_i_Sentence quantifiedAfterSubs = (mf_i_Sentence) quantified.accept(this, arg);

		List<mf_NodeTermVariable> variables = new ArrayList<mf_NodeTermVariable>();
		for (mf_NodeTermVariable v : sentence.getVariables()) {
			mf_i_NodeTerm st = substitution.get(v);
			if (null != st) {
				if (st instanceof mf_NodeTermVariable) {
					// Only if it is a variable to I replace it, otherwise
					// I drop it.
					variables.add((mf_NodeTermVariable) st.copy());
				}
			} else {
				// No substitution for the quantified variable, so
				// keep it.
				variables.add(v.copy());
			}
		}

		// If not variables remaining on the quantifier, then drop it
		if (variables.size() == 0) {
			return quantifiedAfterSubs;
		}

		return new mf_SentenceQuantified(sentence.getQuantifier(), variables,
				quantifiedAfterSubs);
	}
}