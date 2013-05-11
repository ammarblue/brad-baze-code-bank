package com.software.reuze;
//package aima.core.logic.propositional.parsing;

import java.util.ArrayList;
import java.util.List;

/*import aima.core.logic.propositional.parsing.ast.BinarySentence;
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
public class ml_SentenceVisitOps implements ml_i_SentenceVisit {
	private ml_Parser parser = new ml_Parser();

	public Object visitSymbol(ml_SentenceAtomicSymbol s, Object arg) {
		return new ml_SentenceAtomicSymbol(s.getValue());
	}

	public Object visitTrueSentence(ml_SentenceAtomicTrue ts, Object arg) {
		return new ml_SentenceAtomicTrue();
	}

	public Object visitFalseSentence(ml_SentenceAtomicFalse fs, Object arg) {
		return new ml_SentenceAtomicFalse();
	}

	public Object visitNotSentence(ml_SentenceComplexUnary fs, Object arg) {
		return new ml_SentenceComplexUnary((ml_a_ParseTreeSentence) fs.getNegated().accept(this, arg));
	}

	public Object visitBinarySentence(ml_SentenceBinary fs, Object arg) {
		return new ml_SentenceBinary(fs.getOperator(), (ml_a_ParseTreeSentence) fs.getFirst()
				.accept(this, arg), (ml_a_ParseTreeSentence) fs.getSecond().accept(this, arg));
	}

	public Object visitMultiSentence(ml_SentenceComplexMulti fs, Object arg) {
		List<ml_a_ParseTreeSentence> terms = fs.getSentences();
		List<ml_a_ParseTreeSentence> newTerms = new ArrayList<ml_a_ParseTreeSentence>();
		for (int i = 0; i < terms.size(); i++) {
			ml_a_ParseTreeSentence s = (ml_a_ParseTreeSentence) terms.get(i);
			ml_a_ParseTreeSentence subsTerm = (ml_a_ParseTreeSentence) s.accept(this, arg);
			newTerms.add(subsTerm);
		}
		return new ml_SentenceComplexMulti(fs.getOperator(), newTerms);
	}

	protected ml_a_ParseTreeSentence recreate(Object ast) {
		return (ml_a_ParseTreeSentence) parser.parse(((ml_a_ParseTreeSentence) ast).toString());
	}
}