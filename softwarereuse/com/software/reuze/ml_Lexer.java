package com.software.reuze;
//package aima.core.logic.propositional.parsing;

import java.util.HashSet;
import java.util.Set;

//import aima.core.logic.common.Lexer;
//import aima.core.logic.common.LogicTokenTypes;
//import aima.core.logic.common.Token;

/**
 * @author Ravi Mohan
 * @author Mike Stampone
 */
public class ml_Lexer extends l_a_Lexer {

	Set<String> connectors;

	public ml_Lexer() {
		connectors = new HashSet<String>();
		connectors.add("NOT");
		connectors.add("AND");
		connectors.add("OR");
		connectors.add("=>");
		connectors.add("<=>");
	}

	/**
	 * Constructs a propositional expression lexer with the specified character
	 * stream.
	 * 
	 * @param inputString
	 *            a sequence of characters to be converted into a sequence of
	 *            tokens.
	 */
	public ml_Lexer(String inputString) {
		this();
		setInput(inputString);
	}

	/**
	 * Returns the next token from the character stream.
	 * 
	 * @return the next token from the character stream.
	 */
	@Override
	public d_Token nextToken() {
		if (lookAhead(1) == '(') {
			consume();
			return new d_Token(ml_i_LogicTokenTypes.LPAREN, "(");

		} else if (lookAhead(1) == ')') {
			consume();
			return new d_Token(ml_i_LogicTokenTypes.RPAREN, ")");
		} else if (identifierDetected()) {
			return symbol();

		} else if (Character.isWhitespace(lookAhead(1))) {
			consume();
			return nextToken();
			// return whiteSpace();
		} else if (lookAhead(1) == (char) -1) {
			return new d_Token(ml_i_LogicTokenTypes.EOI, "EOI");
		} else {
			throw new RuntimeException("Lexing error on character "
					+ lookAhead(1));
		}
	}

	private boolean identifierDetected() {
		return (Character.isJavaIdentifierStart((char) lookAheadBuffer[0]))
				|| partOfConnector();
	}

	private boolean partOfConnector() {
		return (lookAhead(1) == '=') || (lookAhead(1) == '<')
				|| (lookAhead(1) == '>');
	}

	private d_Token symbol() {
		StringBuffer sbuf = new StringBuffer();
		while ((Character.isLetterOrDigit(lookAhead(1)))
				|| (lookAhead(1) == '=') || (lookAhead(1) == '<')
				|| (lookAhead(1) == '>')) {
			sbuf.append(lookAhead(1));
			consume();
		}
		String symbol = sbuf.toString();
		if (isConnector(symbol)) {
			return new d_Token(ml_i_LogicTokenTypes.CONNECTOR, sbuf.toString());
		} else if (symbol.equalsIgnoreCase("true")) {
			return new d_Token(ml_i_LogicTokenTypes.TRUE, "TRUE");
		} else if (symbol.equalsIgnoreCase("false")) {
			return new d_Token(ml_i_LogicTokenTypes.FALSE, "FALSE");
		} else {
			return new d_Token(ml_i_LogicTokenTypes.SYMBOL, sbuf.toString());
		}

	}

	@SuppressWarnings("unused")
	private d_Token connector() {
		StringBuffer sbuf = new StringBuffer();
		while (Character.isLetterOrDigit(lookAhead(1))) {
			sbuf.append(lookAhead(1));
			consume();
		}
		return new d_Token(ml_i_LogicTokenTypes.CONNECTOR, sbuf.toString());
	}

	@SuppressWarnings("unused")
	private d_Token whiteSpace() {
		StringBuffer sbuf = new StringBuffer();
		while (Character.isWhitespace(lookAhead(1))) {
			sbuf.append(lookAhead(1));
			consume();
		}
		return new d_Token(ml_i_LogicTokenTypes.WHITESPACE, sbuf.toString());

	}

	private boolean isConnector(String aSymbol) {
		return (connectors.contains(aSymbol));
	}
}