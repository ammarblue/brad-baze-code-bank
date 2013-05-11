package com.software.reuze;
//package aima.core.logic.fol.parsing;

import java.util.ArrayList;
import java.util.List;


/*import aima.core.logic.common.LogicTokenTypes;
import aima.core.logic.common.Token;
import aima.core.logic.fol.domain.FOLDomain;
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
 * 
 */
public class mf_Parser {
	private mf_Lexer lexer;

	protected d_Token[] lookAheadBuffer;

	protected int lookAhead = 1;

	public mf_Parser(mf_Lexer lexer) {
		this.lexer = lexer;
		lookAheadBuffer = new d_Token[lookAhead];
	}

	public mf_Parser(mf_Domain domain) {
		this(new mf_Lexer(domain));
	}

	public mf_Domain getFOLDomain() {
		return lexer.getFOLDomain();
	}

	public mf_i_Sentence parse(String s) {
		setUpToParse(s);
		return parseSentence();
	}

	public void setUpToParse(String s) {
		lexer.clear();
		lookAheadBuffer = new d_Token[1];
		lexer.setInput(s);
		fillLookAheadBuffer();

	}

	private mf_i_NodeTerm parseTerm() {
		d_Token t = lookAhead(1);
		int tokenType = t.getType();
		if (tokenType == ml_i_LogicTokenTypes.CONSTANT) {
			return parseConstant();
		} else if (tokenType == ml_i_LogicTokenTypes.VARIABLE) {
			return parseVariable();
		} else if (tokenType == ml_i_LogicTokenTypes.FUNCTION) {
			return parseFunction();
		}

		else {
			return null;
		}
	}

	public mf_i_NodeTerm parseVariable() {
		d_Token t = lookAhead(1);
		String value = t.getText();
		consume();
		return new mf_NodeTermVariable(value);
	}

	public mf_i_NodeTerm parseConstant() {
		d_Token t = lookAhead(1);
		String value = t.getText();
		consume();
		return new mf_SymbolConstant(value);
	}

	public mf_i_NodeTerm parseFunction() {
		d_Token t = lookAhead(1);
		String functionName = t.getText();
		List<mf_i_NodeTerm> terms = processTerms();
		return new mf_NodeTermFunction(functionName, terms);
	}

	public mf_i_Sentence parsePredicate() {
		d_Token t = lookAhead(1);
		String predicateName = t.getText();
		List<mf_i_NodeTerm> terms = processTerms();
		return new mf_Predicate(predicateName, terms);
	}

	private List<mf_i_NodeTerm> processTerms() {
		consume();
		List<mf_i_NodeTerm> terms = new ArrayList<mf_i_NodeTerm>();
		match("(");
		mf_i_NodeTerm term = parseTerm();
		terms.add(term);

		while (lookAhead(1).getType() == ml_i_LogicTokenTypes.COMMA) {
			match(",");
			term = parseTerm();
			terms.add(term);
		}
		match(")");
		return terms;
	}

	public mf_i_Sentence parseTermEquality() {
		mf_i_NodeTerm term1 = parseTerm();
		match("=");
		// System.out.println("=");
		mf_i_NodeTerm term2 = parseTerm();
		return new mf_SentenceAtomicTermEquality(term1, term2);
	}

	public mf_i_Sentence parseNotSentence() {
		match("NOT");
		return new mf_SentenceNot(parseSentence());
	}

	//
	// PROTECTED METHODS
	//
	protected d_Token lookAhead(int i) {
		return lookAheadBuffer[i - 1];
	}

	protected void consume() {
		// System.out.println("consuming" +lookAheadBuffer[0].getText());
		loadNextTokenFromInput();
		// System.out.println("next token " +lookAheadBuffer[0].getText());
	}

	protected void loadNextTokenFromInput() {

		boolean eoiEncountered = false;
		for (int i = 0; i < lookAhead - 1; i++) {

			lookAheadBuffer[i] = lookAheadBuffer[i + 1];
			if (isEndOfInput(lookAheadBuffer[i])) {
				eoiEncountered = true;
				break;
			}
		}
		if (!eoiEncountered) {
			try {
				lookAheadBuffer[lookAhead - 1] = lexer.nextToken();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	protected boolean isEndOfInput(d_Token t) {
		return (t.getType() == ml_i_LogicTokenTypes.EOI);
	}

	protected void fillLookAheadBuffer() {
		for (int i = 0; i < lookAhead; i++) {
			lookAheadBuffer[i] = lexer.nextToken();
		}
	}

	protected void match(String terminalSymbol) {
		if (lookAhead(1).getText().equals(terminalSymbol)) {
			consume();
		} else {
			throw new RuntimeException(
					"Syntax error detected at match. Expected "
							+ terminalSymbol + " but got "
							+ lookAhead(1).getText());
		}

	}

	//
	// PRIVATE METHODS
	//

	private mf_i_Sentence parseSentence() {
		d_Token t = lookAhead(1);
		if (lParen(t)) {
			return parseParanthizedSentence();
		} else if ((lookAhead(1).getType() == ml_i_LogicTokenTypes.QUANTIFIER)) {

			return parseQuantifiedSentence();
		} else if (notToken(t)) {
			return parseNotSentence();
		} else if (predicate(t)) {
			return parsePredicate();
		} else if (term(t)) {
			return parseTermEquality();
		}

		throw new RuntimeException("parse failed with Token " + t.getText());
	}

	private mf_i_Sentence parseQuantifiedSentence() {
		String quantifier = lookAhead(1).getText();
		consume();
		List<mf_NodeTermVariable> variables = new ArrayList<mf_NodeTermVariable>();
		mf_NodeTermVariable var = (mf_NodeTermVariable) parseVariable();
		variables.add(var);
		while (lookAhead(1).getType() == ml_i_LogicTokenTypes.COMMA) {
			consume();
			var = (mf_NodeTermVariable) parseVariable();
			variables.add(var);
		}
		mf_i_Sentence sentence = parseSentence();
		return new mf_SentenceQuantified(quantifier, variables, sentence);
	}

	private mf_i_Sentence parseParanthizedSentence() {
		match("(");
		mf_i_Sentence sen = parseSentence();
		while (binaryConnector(lookAhead(1))) {
			String connector = lookAhead(1).getText();
			consume();
			mf_i_Sentence other = parseSentence();
			sen = new mf_SentenceConnected(connector, sen, other);
		}
		match(")");
		return sen; /* new ParanthizedSentence */

	}

	private boolean binaryConnector(d_Token t) {
		if ((t.getType() == ml_i_LogicTokenTypes.CONNECTOR)
				&& (!(t.getText().equals("NOT")))) {
			return true;
		} else {
			return false;
		}
	}

	private boolean lParen(d_Token t) {
		if (t.getType() == ml_i_LogicTokenTypes.LPAREN) {
			return true;
		} else {
			return false;
		}
	}

	private boolean term(d_Token t) {
		if ((t.getType() == ml_i_LogicTokenTypes.FUNCTION)
				|| (t.getType() == ml_i_LogicTokenTypes.CONSTANT)
				|| (t.getType() == ml_i_LogicTokenTypes.VARIABLE)) {
			return true;
		} else {
			return false;
		}

	}

	private boolean predicate(d_Token t) {
		if ((t.getType() == ml_i_LogicTokenTypes.PREDICATE)) {
			return true;
		} else {
			return false;
		}
	}

	private boolean notToken(d_Token t) {
		if ((t.getType() == ml_i_LogicTokenTypes.CONNECTOR)
				&& (t.getText().equals("NOT"))) {
			return true;
		} else {
			return false;
		}
	}
}