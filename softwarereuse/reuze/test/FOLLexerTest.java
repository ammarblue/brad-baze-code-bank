package reuze.test;
//package aima.test.core.unit.logic.fol.parsing;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.software.reuze.d_Token;
import com.software.reuze.mf_Domain;
import com.software.reuze.mf_Lexer;
import com.software.reuze.ml_i_LogicTokenTypes;


/*import aima.core.logic.common.LogicTokenTypes;
import aima.core.logic.common.Token;
import aima.core.logic.fol.domain.FOLDomain;
import aima.core.logic.fol.parsing.FOLLexer;*/

/**
 * @author Ravi Mohan
 * 
 */
public class FOLLexerTest {
	mf_Lexer lexer;

	@Before
	public void setUp() {
		mf_Domain domain = new mf_Domain();
		domain.addConstant("P");
		domain.addConstant("John");
		domain.addConstant("Saladin");
		domain.addFunction("LeftLeg");
		domain.addFunction("BrotherOf");
		domain.addFunction("EnemyOf");
		domain.addPredicate("HasColor");
		domain.addPredicate("King");
		lexer = new mf_Lexer(domain);
	}

	@Test
	public void testLexBasicExpression() {
		lexer.setInput("( P )");
		Assert.assertEquals(new d_Token(ml_i_LogicTokenTypes.LPAREN, "("),
				lexer.nextToken());
		Assert.assertEquals(new d_Token(ml_i_LogicTokenTypes.CONSTANT, "P"),
				lexer.nextToken());
		Assert.assertEquals(new d_Token(ml_i_LogicTokenTypes.RPAREN, ")"),
				lexer.nextToken());
		Assert.assertEquals(new d_Token(ml_i_LogicTokenTypes.EOI, "EOI"),
				lexer.nextToken());
	}

	@Test
	public void testConnectors() {
		lexer.setInput(" p  AND q");
		Assert.assertEquals(new d_Token(ml_i_LogicTokenTypes.VARIABLE, "p"),
				lexer.nextToken());
		Assert.assertEquals(new d_Token(ml_i_LogicTokenTypes.CONNECTOR, "AND"),
				lexer.nextToken());
		Assert.assertEquals(new d_Token(ml_i_LogicTokenTypes.VARIABLE, "q"),
				lexer.nextToken());
		Assert.assertEquals(new d_Token(ml_i_LogicTokenTypes.EOI, "EOI"),
				lexer.nextToken());
	}

	@Test
	public void testFunctions() {
		lexer.setInput(" LeftLeg(q)");
		Assert.assertEquals(new d_Token(ml_i_LogicTokenTypes.FUNCTION, "LeftLeg"),
				lexer.nextToken());
		Assert.assertEquals(new d_Token(ml_i_LogicTokenTypes.LPAREN, "("),
				lexer.nextToken());
		Assert.assertEquals(new d_Token(ml_i_LogicTokenTypes.VARIABLE, "q"),
				lexer.nextToken());
		Assert.assertEquals(new d_Token(ml_i_LogicTokenTypes.RPAREN, ")"),
				lexer.nextToken());
		Assert.assertEquals(new d_Token(ml_i_LogicTokenTypes.EOI, "EOI"),
				lexer.nextToken());
	}

	@Test
	public void testPredicate() {
		lexer.setInput(" HasColor(r)");
		Assert.assertEquals(new d_Token(ml_i_LogicTokenTypes.PREDICATE, "HasColor"),
				lexer.nextToken());
		Assert.assertEquals(new d_Token(ml_i_LogicTokenTypes.LPAREN, "("),
				lexer.nextToken());
		Assert.assertEquals(new d_Token(ml_i_LogicTokenTypes.VARIABLE, "r"),
				lexer.nextToken());
		Assert.assertEquals(new d_Token(ml_i_LogicTokenTypes.RPAREN, ")"),
				lexer.nextToken());
		Assert.assertEquals(new d_Token(ml_i_LogicTokenTypes.EOI, "EOI"),
				lexer.nextToken());
	}

	@Test
	public void testMultiArgPredicate() {
		lexer.setInput(" King(x,y)");
		Assert.assertEquals(new d_Token(ml_i_LogicTokenTypes.PREDICATE, "King"),
				lexer.nextToken());
		Assert.assertEquals(new d_Token(ml_i_LogicTokenTypes.LPAREN, "("),
				lexer.nextToken());
		Assert.assertEquals(new d_Token(ml_i_LogicTokenTypes.VARIABLE, "x"),
				lexer.nextToken());
		Assert.assertEquals(new d_Token(ml_i_LogicTokenTypes.COMMA, ","),
				lexer.nextToken());
		Assert.assertEquals(new d_Token(ml_i_LogicTokenTypes.VARIABLE, "y"),
				lexer.nextToken());
		Assert.assertEquals(new d_Token(ml_i_LogicTokenTypes.RPAREN, ")"),
				lexer.nextToken());
	}

	@Test
	public void testQuantifier() {
		lexer.setInput("FORALL x,y");
		Assert.assertEquals(new d_Token(ml_i_LogicTokenTypes.QUANTIFIER, "FORALL"),
				lexer.nextToken());
		Assert.assertEquals(new d_Token(ml_i_LogicTokenTypes.VARIABLE, "x"),
				lexer.nextToken());
		Assert.assertEquals(new d_Token(ml_i_LogicTokenTypes.COMMA, ","),
				lexer.nextToken());
		Assert.assertEquals(new d_Token(ml_i_LogicTokenTypes.VARIABLE, "y"),
				lexer.nextToken());
		Assert.assertEquals(new d_Token(ml_i_LogicTokenTypes.EOI, "EOI"),
				lexer.nextToken());
	}

	@Test
	public void testTermEquality() {
		lexer.setInput("BrotherOf(John) = EnemyOf(Saladin)");
		Assert.assertEquals(new d_Token(ml_i_LogicTokenTypes.FUNCTION, "BrotherOf"),
				lexer.nextToken());
		Assert.assertEquals(new d_Token(ml_i_LogicTokenTypes.LPAREN, "("),
				lexer.nextToken());
		Assert.assertEquals(new d_Token(ml_i_LogicTokenTypes.CONSTANT, "John"),
				lexer.nextToken());
		Assert.assertEquals(new d_Token(ml_i_LogicTokenTypes.RPAREN, ")"),
				lexer.nextToken());
		Assert.assertEquals(new d_Token(ml_i_LogicTokenTypes.EQUALS, "="),
				lexer.nextToken());
		Assert.assertEquals(new d_Token(ml_i_LogicTokenTypes.FUNCTION, "EnemyOf"),
				lexer.nextToken());
		Assert.assertEquals(new d_Token(ml_i_LogicTokenTypes.LPAREN, "("),
				lexer.nextToken());
		Assert.assertEquals(new d_Token(ml_i_LogicTokenTypes.CONSTANT, "Saladin"),
				lexer.nextToken());
		Assert.assertEquals(new d_Token(ml_i_LogicTokenTypes.RPAREN, ")"),
				lexer.nextToken());
	}
}
