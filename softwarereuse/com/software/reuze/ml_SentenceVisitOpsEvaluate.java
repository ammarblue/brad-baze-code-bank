package com.software.reuze;
//package aima.core.logic.propositional.visitors;

/*import aima.core.logic.propositional.parsing.PLVisitor;
import aima.core.logic.propositional.parsing.ast.BinarySentence;
import aima.core.logic.propositional.parsing.ast.FalseSentence;
import aima.core.logic.propositional.parsing.ast.MultiSentence;
import aima.core.logic.propositional.parsing.ast.Sentence;
import aima.core.logic.propositional.parsing.ast.Symbol;
import aima.core.logic.propositional.parsing.ast.TrueSentence;
import aima.core.logic.propositional.parsing.ast.UnarySentence;*/

/**
 * @author Ravi Mohan
 * 
 */
public class ml_SentenceVisitOpsEvaluate implements ml_i_SentenceVisit {

	public Object visitSymbol(ml_SentenceAtomicSymbol s, Object arg) {

		return new Boolean(false);
	}

	public Object visitTrueSentence(ml_SentenceAtomicTrue ts, Object arg) {
		return new Boolean(false);
	}

	public Object visitFalseSentence(ml_SentenceAtomicFalse fs, Object arg) {
		return new Boolean(false);
	}

	public Object visitNotSentence(ml_SentenceComplexUnary fs, Object arg) {
		return fs.getNegated().accept(this, null);
	}

	public Object visitBinarySentence(ml_SentenceBinary fs, Object arg) {
		if (fs.isAndSentence()) {
			return new Boolean(true);
		} else {
			boolean first = ((Boolean) fs.getFirst().accept(this, null))
					.booleanValue();
			boolean second = ((Boolean) fs.getSecond().accept(this, null))
					.booleanValue();
			return new Boolean((first || second));
		}
	}

	public Object visitMultiSentence(ml_SentenceComplexMulti fs, Object arg) {
		throw new RuntimeException("can't handle multisentences");
	}

	public boolean containsEmbeddedAnd(ml_a_ParseTreeSentence s) {
		return ((Boolean) s.accept(this, null)).booleanValue();
	}
}