package com.software.reuze;
//package aima.core.logic.fol.parsing;

/*import aima.core.logic.fol.parsing.ast.ConnectedSentence;
import aima.core.logic.fol.parsing.ast.Constant;
import aima.core.logic.fol.parsing.ast.Function;
import aima.core.logic.fol.parsing.ast.NotSentence;
import aima.core.logic.fol.parsing.ast.Predicate;
import aima.core.logic.fol.parsing.ast.QuantifiedSentence;
import aima.core.logic.fol.parsing.ast.TermEquality;
import aima.core.logic.fol.parsing.ast.Variable;*/

/**
 * @author Ravi Mohan
 * 
 */
public interface mf_Visitor {
	public Object visitPredicate(mf_Predicate p, Object arg);

	public Object visitTermEquality(mf_SentenceAtomicTermEquality equality, Object arg);

	public Object visitVariable(mf_NodeTermVariable variable, Object arg);

	public Object visitConstant(mf_SymbolConstant constant, Object arg);

	public Object visitFunction(mf_NodeTermFunction function, Object arg);

	public Object visitNotSentence(mf_SentenceNot sentence, Object arg);

	public Object visitConnectedSentence(mf_SentenceConnected sentence, Object arg);

	public Object visitQuantifiedSentence(mf_SentenceQuantified sentence,
			Object arg);
}
