package com.software.reuze;
//package aima.core.logic.propositional.parsing;

/*import aima.core.logic.common.Visitor;
import aima.core.logic.propositional.parsing.ast.BinarySentence;
import aima.core.logic.propositional.parsing.ast.FalseSentence;
import aima.core.logic.propositional.parsing.ast.MultiSentence;
import aima.core.logic.propositional.parsing.ast.Symbol;
import aima.core.logic.propositional.parsing.ast.TrueSentence;
import aima.core.logic.propositional.parsing.ast.UnarySentence;*/

/**
 * @author Ravi Mohan
 * 
 */
public interface ml_i_SentenceVisit extends a_i_Visitor {
	public Object visitSymbol(ml_SentenceAtomicSymbol s, Object arg);

	public Object visitTrueSentence(ml_SentenceAtomicTrue ts, Object arg);

	public Object visitFalseSentence(ml_SentenceAtomicFalse fs, Object arg);

	public Object visitNotSentence(ml_SentenceComplexUnary fs, Object arg);

	public Object visitBinarySentence(ml_SentenceBinary fs, Object arg);

	public Object visitMultiSentence(ml_SentenceComplexMulti fs, Object arg);
}