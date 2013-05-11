package reuze.test;
//package aima.test.core.unit.logic.propositional.visitors;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.software.reuze.mf_CNFTransformer;
import com.software.reuze.ml_Parser;
import com.software.reuze.ml_a_ParseTreeSentence;


//import aima.core.logic.propositional.parsing.PEParser;
//import aima.core.logic.propositional.parsing.ast.Sentence;
//import aima.core.logic.propositional.visitors.CNFTransformer;

/**
 * @author Ravi Mohan
 * 
 */
public class CNFTransformerTest {
	private ml_Parser parser = new ml_Parser();

	private mf_CNFTransformer transformer;

	@Before
	public void setUp() {
		transformer = new mf_CNFTransformer();
	}

	@Test
	public void testSymbolTransform() {
		ml_a_ParseTreeSentence symbol = (ml_a_ParseTreeSentence) parser.parse("A");
		ml_a_ParseTreeSentence transformed = transformer.transform(symbol);
		Assert.assertEquals("A", transformed.toString());
	}

	@Test
	public void testBasicSentenceTransformation() {
		ml_a_ParseTreeSentence and = (ml_a_ParseTreeSentence) parser.parse("(A AND B)");
		ml_a_ParseTreeSentence transformedAnd = transformer.transform(and);
		Assert.assertEquals(and.toString(), transformedAnd.toString());

		ml_a_ParseTreeSentence or = (ml_a_ParseTreeSentence) parser.parse("(A OR B)");
		ml_a_ParseTreeSentence transformedOr = transformer.transform(or);
		Assert.assertEquals(or.toString(), transformedOr.toString());

		ml_a_ParseTreeSentence not = (ml_a_ParseTreeSentence) parser.parse("(NOT C)");
		ml_a_ParseTreeSentence transformedNot = transformer.transform(not);
		Assert.assertEquals(not.toString(), transformedNot.toString());
	}

	@Test
	public void testImplicationTransformation() {
		ml_a_ParseTreeSentence impl = (ml_a_ParseTreeSentence) parser.parse("(A => B)");
		ml_a_ParseTreeSentence expected = (ml_a_ParseTreeSentence) parser.parse("((NOT A) OR B)");
		ml_a_ParseTreeSentence transformedImpl = transformer.transform(impl);
		Assert.assertEquals(expected.toString(), transformedImpl.toString());
	}

	@Test
	public void testBiConditionalTransformation() {
		ml_a_ParseTreeSentence bic = (ml_a_ParseTreeSentence) parser.parse("(A <=> B)");
		ml_a_ParseTreeSentence expected = (ml_a_ParseTreeSentence) parser
				.parse("(((NOT A) OR B)  AND ((NOT B) OR A)) ");
		ml_a_ParseTreeSentence transformedBic = transformer.transform(bic);
		Assert.assertEquals(expected.toString(), transformedBic.toString());
	}

	@Test
	public void testTwoSuccessiveNotsTransformation() {
		ml_a_ParseTreeSentence twoNots = (ml_a_ParseTreeSentence) parser.parse("(NOT (NOT A))");
		ml_a_ParseTreeSentence expected = (ml_a_ParseTreeSentence) parser.parse("A");
		ml_a_ParseTreeSentence transformed = transformer.transform(twoNots);
		Assert.assertEquals(expected.toString(), transformed.toString());
	}

	@Test
	public void testThreeSuccessiveNotsTransformation() {
		ml_a_ParseTreeSentence threeNots = (ml_a_ParseTreeSentence) parser.parse("(NOT (NOT (NOT A)))");
		ml_a_ParseTreeSentence expected = (ml_a_ParseTreeSentence) parser.parse("(NOT A)");
		ml_a_ParseTreeSentence transformed = transformer.transform(threeNots);
		Assert.assertEquals(expected.toString(), transformed.toString());
	}

	@Test
	public void testFourSuccessiveNotsTransformation() {
		ml_a_ParseTreeSentence fourNots = (ml_a_ParseTreeSentence) parser
				.parse("(NOT (NOT (NOT (NOT A))))");
		ml_a_ParseTreeSentence expected = (ml_a_ParseTreeSentence) parser.parse("A");
		ml_a_ParseTreeSentence transformed = transformer.transform(fourNots);
		Assert.assertEquals(expected.toString(), transformed.toString());
	}

	@Test
	public void testDeMorgan1() {
		ml_a_ParseTreeSentence dm = (ml_a_ParseTreeSentence) parser.parse("(NOT (A AND B))");
		ml_a_ParseTreeSentence expected = (ml_a_ParseTreeSentence) parser.parse("((NOT A) OR (NOT B))");
		ml_a_ParseTreeSentence transformed = transformer.transform(dm);
		Assert.assertEquals(expected.toString(), transformed.toString());
	}

	@Test
	public void testDeMorgan2() {
		ml_a_ParseTreeSentence dm = (ml_a_ParseTreeSentence) parser.parse("(NOT (A OR B))");
		ml_a_ParseTreeSentence expected = (ml_a_ParseTreeSentence) parser.parse("((NOT A) AND (NOT B))");
		ml_a_ParseTreeSentence transformed = transformer.transform(dm);
		Assert.assertEquals(expected.toString(), transformed.toString());
	}

	@Test
	public void testOrDistribution1() {
		ml_a_ParseTreeSentence or = (ml_a_ParseTreeSentence) parser.parse("((A AND B) OR C)");
		ml_a_ParseTreeSentence expected = (ml_a_ParseTreeSentence) parser.parse("((C OR A) AND (C OR B))");
		ml_a_ParseTreeSentence transformed = transformer.transform(or);
		Assert.assertEquals(expected.toString(), transformed.toString());
	}

	@Test
	public void testOrDistribution2() {
		ml_a_ParseTreeSentence or = (ml_a_ParseTreeSentence) parser.parse("(A OR (B AND C))");
		ml_a_ParseTreeSentence expected = (ml_a_ParseTreeSentence) parser.parse("((A OR B) AND (A OR C))");
		ml_a_ParseTreeSentence transformed = transformer.transform(or);
		Assert.assertEquals(expected.toString(), transformed.toString());
	}

	@Test
	public void testAimaExample() {
		ml_a_ParseTreeSentence aimaEg = (ml_a_ParseTreeSentence) parser.parse("( B11 <=> (P12 OR P21))");
		ml_a_ParseTreeSentence expected = (ml_a_ParseTreeSentence) parser
				.parse(" (  (  ( NOT B11 )  OR  ( P12 OR P21 ) ) AND  (  ( B11 OR  ( NOT P12 )  ) AND  ( B11 OR  ( NOT P21 )  ) ) )");
		ml_a_ParseTreeSentence transformed = transformer.transform(aimaEg);
		Assert.assertEquals(expected.toString(), transformed.toString());
	}
}
