package reuze.test;
//package aima.test.core.unit.logic.propositional.parsing;

import org.junit.Assert;
import org.junit.Test;

import com.software.reuze.d_Token;
import com.software.reuze.ml_Lexer;
import com.software.reuze.ml_i_LogicTokenTypes;


//import aima.core.logic.common.LogicTokenTypes;
//import aima.core.logic.common.Token;
//import aima.core.logic.propositional.parsing.PELexer;

/**
 * @author Ravi Mohan
 * 
 */
public class PELexerTest {

	@Test
	public void testLexBasicExpression() {
		ml_Lexer pelexer = new ml_Lexer();
		pelexer.setInput("(P)");
		Assert.assertEquals(new d_Token(ml_i_LogicTokenTypes.LPAREN, "("),
				pelexer.nextToken());
		Assert.assertEquals(new d_Token(ml_i_LogicTokenTypes.SYMBOL, "P"),
				pelexer.nextToken());
		Assert.assertEquals(new d_Token(ml_i_LogicTokenTypes.RPAREN, ")"),
				pelexer.nextToken());

		Assert.assertEquals(new d_Token(ml_i_LogicTokenTypes.EOI, "EOI"),
				pelexer.nextToken());
	}

	@Test
	public void testLexNotExpression() {
		ml_Lexer pelexer = new ml_Lexer();
		pelexer.setInput("(NOT P)");
		Assert.assertEquals(new d_Token(ml_i_LogicTokenTypes.LPAREN, "("),
				pelexer.nextToken());
		Assert.assertEquals(new d_Token(ml_i_LogicTokenTypes.CONNECTOR, "NOT"),
				pelexer.nextToken());

		Assert.assertEquals(new d_Token(ml_i_LogicTokenTypes.SYMBOL, "P"),
				pelexer.nextToken());
		Assert.assertEquals(new d_Token(ml_i_LogicTokenTypes.RPAREN, ")"),
				pelexer.nextToken());

		Assert.assertEquals(new d_Token(ml_i_LogicTokenTypes.EOI, "EOI"),
				pelexer.nextToken());
	}

	@Test
	public void testLexImpliesExpression() {
		ml_Lexer pelexer = new ml_Lexer();
		pelexer.setInput("(P => Q)");
		Assert.assertEquals(new d_Token(ml_i_LogicTokenTypes.LPAREN, "("),
				pelexer.nextToken());
		Assert.assertEquals(new d_Token(ml_i_LogicTokenTypes.SYMBOL, "P"),
				pelexer.nextToken());
		Assert.assertEquals(new d_Token(ml_i_LogicTokenTypes.CONNECTOR, "=>"),
				pelexer.nextToken());
	}

	@Test
	public void testLexBiCOnditionalExpression() {
		ml_Lexer pelexer = new ml_Lexer();
		pelexer.setInput("(B11 <=> (P12 OR P21))");
		Assert.assertEquals(new d_Token(ml_i_LogicTokenTypes.LPAREN, "("),
				pelexer.nextToken());
		Assert.assertEquals(new d_Token(ml_i_LogicTokenTypes.SYMBOL, "B11"),
				pelexer.nextToken());
		Assert.assertEquals(new d_Token(ml_i_LogicTokenTypes.CONNECTOR, "<=>"),
				pelexer.nextToken());
	}
}
