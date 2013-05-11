package reuze.test;
//package aima.test.core.unit.logic.propositional.visitors;

import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.software.reuze.mf_CNFClauseGatherer;
import com.software.reuze.mf_CNFTransformer;
import com.software.reuze.ml_Parser;
import com.software.reuze.ml_a_ParseTreeSentence;


/*import aima.core.logic.propositional.parsing.PEParser;
import aima.core.logic.propositional.parsing.ast.Sentence;
import aima.core.logic.propositional.visitors.CNFClauseGatherer;
import aima.core.logic.propositional.visitors.CNFTransformer;*/

/**
 * @author Ravi Mohan
 * 
 */
public class CNFClauseGathererTest {
	private mf_CNFClauseGatherer gatherer;

	private ml_Parser parser;

	@Before
	public void setUp() {
		parser = new ml_Parser();
		gatherer = new mf_CNFClauseGatherer();
	}

	@Test
	public void testSymbol() {
		ml_a_ParseTreeSentence simple = (ml_a_ParseTreeSentence) parser.parse("A");
		ml_a_ParseTreeSentence a = (ml_a_ParseTreeSentence) parser.parse("A");
		Set<ml_a_ParseTreeSentence> clauses = gatherer.getClausesFrom(simple);
		Assert.assertNotNull(clauses);
		Assert.assertEquals(1, clauses.size());
		Assert.assertTrue(clauses.contains(a));
	}

	@Test
	public void testNotSentence() {
		ml_a_ParseTreeSentence simple = (ml_a_ParseTreeSentence) parser.parse("(NOT A)");
		ml_a_ParseTreeSentence a = (ml_a_ParseTreeSentence) parser.parse("(NOT A)");
		Set<ml_a_ParseTreeSentence> clauses = gatherer.getClausesFrom(simple);
		Assert.assertNotNull(clauses);
		Assert.assertEquals(1, clauses.size());
		Assert.assertTrue(clauses.contains(a));
	}

	@Test
	public void testSimpleAndClause() {
		ml_a_ParseTreeSentence simple = (ml_a_ParseTreeSentence) parser.parse("(A AND B)");
		ml_a_ParseTreeSentence a = (ml_a_ParseTreeSentence) parser.parse("A");
		ml_a_ParseTreeSentence b = (ml_a_ParseTreeSentence) parser.parse("B");
		Set<ml_a_ParseTreeSentence> clauses = gatherer.getClausesFrom(simple);
		Assert.assertEquals(2, clauses.size());
		Assert.assertTrue(clauses.contains(a));
		Assert.assertTrue(clauses.contains(b));
	}

	@Test
	public void testMultiAndClause() {
		ml_a_ParseTreeSentence simple = (ml_a_ParseTreeSentence) parser.parse("((A AND B) AND C)");
		Set<ml_a_ParseTreeSentence> clauses = gatherer.getClausesFrom(simple);
		Assert.assertEquals(3, clauses.size());
		ml_a_ParseTreeSentence a = (ml_a_ParseTreeSentence) parser.parse("A");
		ml_a_ParseTreeSentence b = (ml_a_ParseTreeSentence) parser.parse("B");
		ml_a_ParseTreeSentence c = (ml_a_ParseTreeSentence) parser.parse("C");
		Assert.assertTrue(clauses.contains(a));
		Assert.assertTrue(clauses.contains(b));
		Assert.assertTrue(clauses.contains(c));
	}

	@Test
	public void testMultiAndClause2() {
		ml_a_ParseTreeSentence simple = (ml_a_ParseTreeSentence) parser.parse("(A AND (B AND C))");
		Set<ml_a_ParseTreeSentence> clauses = gatherer.getClausesFrom(simple);
		Assert.assertEquals(3, clauses.size());
		ml_a_ParseTreeSentence a = (ml_a_ParseTreeSentence) parser.parse("A");
		ml_a_ParseTreeSentence b = (ml_a_ParseTreeSentence) parser.parse("B");
		ml_a_ParseTreeSentence c = (ml_a_ParseTreeSentence) parser.parse("C");
		Assert.assertTrue(clauses.contains(a));
		Assert.assertTrue(clauses.contains(b));
		Assert.assertTrue(clauses.contains(c));
	}

	@Test
	public void testAimaExample() {
		ml_a_ParseTreeSentence aimaEg = (ml_a_ParseTreeSentence) parser.parse("( B11 <=> (P12 OR P21))");
		mf_CNFTransformer transformer = new mf_CNFTransformer();
		ml_a_ParseTreeSentence transformed = transformer.transform(aimaEg);
		Set<ml_a_ParseTreeSentence> clauses = gatherer.getClausesFrom(transformed);
		ml_a_ParseTreeSentence clause1 = (ml_a_ParseTreeSentence) parser.parse("( B11 OR  ( NOT P12 )  )");
		ml_a_ParseTreeSentence clause2 = (ml_a_ParseTreeSentence) parser.parse("( B11 OR  ( NOT P21 )  )");
		ml_a_ParseTreeSentence clause3 = (ml_a_ParseTreeSentence) parser
				.parse("(  ( NOT B11 )  OR  ( P12 OR P21 ) )");
		Assert.assertEquals(3, clauses.size());
		Assert.assertTrue(clauses.contains(clause1));
		Assert.assertTrue(clauses.contains(clause2));
		Assert.assertTrue(clauses.contains(clause3));
	}
}
