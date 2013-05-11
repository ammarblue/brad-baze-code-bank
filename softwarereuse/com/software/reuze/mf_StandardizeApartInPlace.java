package com.software.reuze;
//package aima.core.logic.fol;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
 * @author Ciaran O'Reilly
 * 
 */
public class mf_StandardizeApartInPlace {
	//
	private static CollectAllVariables _collectAllVariables = new CollectAllVariables();

	public static int standardizeApart(mf_ProofStepChain c, int saIdx) {
		List<mf_NodeTermVariable> variables = new ArrayList<mf_NodeTermVariable>();
		for (mf_Literal l : c.getLiterals()) {
			collectAllVariables(l.getAtomicSentence(), variables);
		}

		return standardizeApart(variables, c, saIdx);
	}

	public static int standardizeApart(mf_Clause c, int saIdx) {
		List<mf_NodeTermVariable> variables = new ArrayList<mf_NodeTermVariable>();
		for (mf_Literal l : c.getLiterals()) {
			collectAllVariables(l.getAtomicSentence(), variables);
		}

		return standardizeApart(variables, c, saIdx);
	}

	//
	// PRIVATE METHODS
	//
	private static int standardizeApart(List<mf_NodeTermVariable> variables, Object expr,
			int saIdx) {
		Map<String, Integer> indexicals = new HashMap<String, Integer>();
		for (mf_NodeTermVariable v : variables) {
			if (!indexicals.containsKey(v.getIndexedValue())) {
				indexicals.put(v.getIndexedValue(), saIdx++);
			}
		}
		for (mf_NodeTermVariable v : variables) {
			Integer i = indexicals.get(v.getIndexedValue());
			if (null == i) {
				throw new RuntimeException("ERROR: duplicate var=" + v
						+ ", expr=" + expr);
			} else {
				v.setIndexical(i);
			}
		}

		return saIdx;
	}

	private static void collectAllVariables(mf_i_Sentence s, List<mf_NodeTermVariable> vars) {
		s.accept(_collectAllVariables, vars);
	}
}

class CollectAllVariables implements mf_Visitor {
	public CollectAllVariables() {

	}

	@SuppressWarnings("unchecked")
	public Object visitVariable(mf_NodeTermVariable var, Object arg) {
		List<mf_NodeTermVariable> variables = (List<mf_NodeTermVariable>) arg;
		variables.add(var);
		return var;
	}

	@SuppressWarnings("unchecked")
	public Object visitQuantifiedSentence(mf_SentenceQuantified sentence,
			Object arg) {
		// Ensure I collect quantified variables too
		List<mf_NodeTermVariable> variables = (List<mf_NodeTermVariable>) arg;
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
