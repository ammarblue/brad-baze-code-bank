package com.software.reuze;
//package aima.core.logic.propositional.parsing;

import java.util.ArrayList;
import java.util.List;

/*import aima.core.logic.common.LogicTokenTypes;
import aima.core.logic.common.ParseTreeNode;
import aima.core.logic.common.Parser;
import aima.core.logic.common.Token;
import aima.core.logic.propositional.parsing.ast.AtomicSentence;
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
public class ml_Parser extends l_Parser {

	public ml_Parser() {
		lookAheadBuffer = new d_Token[lookAhead];
	}

	@Override
	public l_ParseTreeNode parse(String inputString) {
		lexer = new ml_Lexer(inputString);
		fillLookAheadBuffer();
		return parseSentence();
	}

	private ml_SentenceAtomicTrue parseTrue() {
		consume();
		return new ml_SentenceAtomicTrue();
	}

	private ml_SentenceAtomicFalse parseFalse() {
		consume();
		return new ml_SentenceAtomicFalse();
	}

	private ml_SentenceAtomicSymbol parseSymbol() {
		String sym = lookAhead(1).getText();
		consume();
		return new ml_SentenceAtomicSymbol(sym);
	}

	private ml_a_SentenceAtomic parseAtomicSentence() {
		d_Token t = lookAhead(1);
		if (t.getType() == ml_i_LogicTokenTypes.TRUE) {
			return parseTrue();
		} else if (t.getType() == ml_i_LogicTokenTypes.FALSE) {
			return parseFalse();
		} else if (t.getType() == ml_i_LogicTokenTypes.SYMBOL) {
			return parseSymbol();
		} else {
			throw new RuntimeException(
					"Error in parseAtomicSentence with Token " + lookAhead(1));
		}
	}

	private ml_SentenceComplexUnary parseNotSentence() {
		match("NOT");
		ml_a_ParseTreeSentence sen = parseSentence();
		return new ml_SentenceComplexUnary(sen);
	}

	private ml_SentenceComplexMulti parseMultiSentence() {
		consume();
		String connector = lookAhead(1).getText();
		consume();
		List<ml_a_ParseTreeSentence> sentences = new ArrayList<ml_a_ParseTreeSentence>();
		while (lookAhead(1).getType() != ml_i_LogicTokenTypes.RPAREN) {
			ml_a_ParseTreeSentence sen = parseSentence();
			// consume();
			sentences.add(sen);
		}
		match(")");
		return new ml_SentenceComplexMulti(connector, sentences);
	}

	private ml_a_ParseTreeSentence parseSentence() {
		if (detectAtomicSentence()) {
			return parseAtomicSentence();
		} else if (detectBracket()) {
			return parseBracketedSentence();
		} else if (detectNOT()) {
			return parseNotSentence();
		} else {

			throw new RuntimeException("Parser Error Token = " + lookAhead(1));
		}
	}

	private boolean detectNOT() {
		return (lookAhead(1).getType() == ml_i_LogicTokenTypes.CONNECTOR)
				&& (lookAhead(1).getText().equals("NOT"));
	}

	private ml_a_ParseTreeSentence parseBracketedSentence() {

		if (detectMultiOperator()) {
			return parseMultiSentence();
		} else {
			match("(");
			ml_a_ParseTreeSentence one = parseSentence();
			if (lookAhead(1).getType() == ml_i_LogicTokenTypes.RPAREN) {
				match(")");
				return one;
			} else if ((lookAhead(1).getType() == ml_i_LogicTokenTypes.CONNECTOR)
					&& (!(lookAhead(1).getText().equals("Not")))) {
				String connector = lookAhead(1).getText();
				consume(); // connector
				ml_a_ParseTreeSentence two = parseSentence();
				match(")");
				return new ml_SentenceBinary(connector, one, two);
			}

		}
		throw new RuntimeException(
				" Runtime Exception at Bracketed Expression with token "
						+ lookAhead(1));
	}

	private boolean detectMultiOperator() {
		return (lookAhead(1).getType() == ml_i_LogicTokenTypes.LPAREN)
				&& ((lookAhead(2).getText().equals("AND")) || (lookAhead(2)
						.getText().equals("OR")));
	}

	private boolean detectBracket() {
		return lookAhead(1).getType() == ml_i_LogicTokenTypes.LPAREN;
	}

	private boolean detectAtomicSentence() {
		int type = lookAhead(1).getType();
		return (type == ml_i_LogicTokenTypes.TRUE)
				|| (type == ml_i_LogicTokenTypes.FALSE)
				|| (type == ml_i_LogicTokenTypes.SYMBOL);
	}
}