package com.software.reuze;
//package aima.core.logic.fol;

import java.util.ArrayList;
import java.util.List;

/*import aima.core.logic.fol.parsing.FOLVisitor;
import aima.core.logic.fol.parsing.ast.ConnectedSentence;
import aima.core.logic.fol.parsing.ast.Constant;
import aima.core.logic.fol.parsing.ast.Function;
import aima.core.logic.fol.parsing.ast.NotSentence;
import aima.core.logic.fol.parsing.ast.Predicate;
import aima.core.logic.fol.parsing.ast.QuantifiedSentence;
import aima.core.logic.fol.parsing.ast.Sentence;
import aima.core.logic.fol.parsing.ast.TermEquality;
import aima.core.logic.fol.parsing.ast.Variable;*/

/**
 * @author Ravi Mohan
 * 
 */
public class mf_VisitorPredicateCollector implements mf_Visitor {

	public mf_VisitorPredicateCollector() {

	}

	@SuppressWarnings("unchecked")
	public List<mf_Predicate> getPredicates(mf_i_Sentence s) {
		return (List<mf_Predicate>) s.accept(this, new ArrayList<mf_Predicate>());
	}

	@SuppressWarnings("unchecked")
	public Object visitPredicate(mf_Predicate p, Object arg) {
		List<mf_Predicate> predicates = (List<mf_Predicate>) arg;
		predicates.add(p);
		return predicates;
	}

	public Object visitTermEquality(mf_SentenceAtomicTermEquality equality, Object arg) {
		return arg;
	}

	public Object visitVariable(mf_NodeTermVariable variable, Object arg) {
		return arg;
	}

	public Object visitConstant(mf_SymbolConstant constant, Object arg) {
		return arg;
	}

	public Object visitFunction(mf_NodeTermFunction function, Object arg) {
		return arg;
	}

	public Object visitNotSentence(mf_SentenceNot sentence, Object arg) {
		sentence.getNegated().accept(this, arg);
		return arg;
	}

	public Object visitConnectedSentence(mf_SentenceConnected sentence, Object arg) {
		sentence.getFirst().accept(this, arg);
		sentence.getSecond().accept(this, arg);
		return arg;
	}

	public Object visitQuantifiedSentence(mf_SentenceQuantified sentence,
			Object arg) {
		sentence.getQuantified().accept(this, arg);
		return arg;
	}
}