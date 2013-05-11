package com.software.reuze;
//package aima.core.logic.fol.parsing;

import java.util.HashSet;
import java.util.Set;


/*import aima.core.logic.common.Lexer;
import aima.core.logic.common.LogicTokenTypes;
import aima.core.logic.common.Token;
import aima.core.logic.fol.Connectors;
import aima.core.logic.fol.Quantifiers;
import aima.core.logic.fol.domain.FOLDomain;*/

/**
 * @author Ravi Mohan
 * 
 */
public class mf_Lexer extends l_a_Lexer {
	private mf_Domain domain;
	private Set<String> connectors, quantifiers;

	public mf_Lexer(mf_Domain domain) {
		this.domain = domain;

		connectors = new HashSet<String>();
		connectors.add(mf_SymbolsConnectors.NOT);
		connectors.add(mf_SymbolsConnectors.AND);
		connectors.add(mf_SymbolsConnectors.OR);
		connectors.add(mf_SymbolsConnectors.IMPLIES);
		connectors.add(mf_SymbolsConnectors.BICOND);

		quantifiers = new HashSet<String>();
		quantifiers.add(mf_Quantifiers.FORALL);
		quantifiers.add(mf_Quantifiers.EXISTS);
	}

	public mf_Domain getFOLDomain() {
		return domain;
	}

	@Override
	public d_Token nextToken() {
		if (lookAhead(1) == '(') {
			consume();
			return new d_Token(ml_i_LogicTokenTypes.LPAREN, "(");

		} else if (lookAhead(1) == ')') {
			consume();
			return new d_Token(ml_i_LogicTokenTypes.RPAREN, ")");

		} else if (lookAhead(1) == ',') {
			consume();
			return new d_Token(ml_i_LogicTokenTypes.COMMA, ",");

		} else if (identifierDetected()) {
			// System.out.println("identifier detected");
			return identifier();
		} else if (Character.isWhitespace(lookAhead(1))) {
			consume();
			return nextToken();
		} else if (lookAhead(1) == (char) -1) {
			return new d_Token(ml_i_LogicTokenTypes.EOI, "EOI");
		} else {
			throw new RuntimeException("Lexing error on character "
					+ lookAhead(1));
		}
	}

	private d_Token identifier() {
		StringBuffer sbuf = new StringBuffer();
		while ((Character.isJavaIdentifierPart(lookAhead(1)))
				|| partOfConnector()) {
			sbuf.append(lookAhead(1));
			consume();
		}
		String readString = new String(sbuf);
		// System.out.println(readString);
		if (connectors.contains(readString)) {
			return new d_Token(ml_i_LogicTokenTypes.CONNECTOR, readString);
		} else if (quantifiers.contains(readString)) {
			return new d_Token(ml_i_LogicTokenTypes.QUANTIFIER, readString);
		} else if (domain.getPredicates().contains(readString)) {
			return new d_Token(ml_i_LogicTokenTypes.PREDICATE, readString);
		} else if (domain.getFunctions().contains(readString)) {
			return new d_Token(ml_i_LogicTokenTypes.FUNCTION, readString);
		} else if (domain.getConstants().contains(readString)) {
			return new d_Token(ml_i_LogicTokenTypes.CONSTANT, readString);
		} else if (isVariable(readString)) {
			return new d_Token(ml_i_LogicTokenTypes.VARIABLE, readString);
		} else if (readString.equals("=")) {
			return new d_Token(ml_i_LogicTokenTypes.EQUALS, readString);
		} else {
			throw new RuntimeException("Lexing error on character "
					+ lookAhead(1));
		}

	}

	private boolean isVariable(String s) {
		return (Character.isLowerCase(s.charAt(0)));
	}

	private boolean identifierDetected() {
		return (Character.isJavaIdentifierStart((char) lookAheadBuffer[0]))
				|| partOfConnector();
	}

	private boolean partOfConnector() {
		return (lookAhead(1) == '=') || (lookAhead(1) == '<')
				|| (lookAhead(1) == '>');
	}
}