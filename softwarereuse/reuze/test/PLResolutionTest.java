package reuze.test;
//package aima.test.core.unit.logic.propositional.algorithms;

import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.software.reuze.ml_KnowledgeBase;
import com.software.reuze.ml_Parser;
import com.software.reuze.ml_Resolution;
import com.software.reuze.ml_SentenceAtomicSymbol;
import com.software.reuze.ml_a_ParseTreeSentence;


/*import aima.core.logic.propositional.algorithms.KnowledgeBase;
import aima.core.logic.propositional.algorithms.PLResolution;
import aima.core.logic.propositional.parsing.PEParser;
import aima.core.logic.propositional.parsing.ast.Sentence;
import aima.core.logic.propositional.parsing.ast.Symbol;*/

/**
 * @author Ravi Mohan
 * 
 */
public class PLResolutionTest {
	private ml_Resolution resolution;

	private ml_Parser parser;

	@Before
	public void setUp() {
		resolution = new ml_Resolution();
		parser = new ml_Parser();
	}

	@Test
	public void testPLResolveWithOneLiteralMatching() {
		ml_a_ParseTreeSentence one = (ml_a_ParseTreeSentence) parser.parse("(A OR B)");
		ml_a_ParseTreeSentence two = (ml_a_ParseTreeSentence) parser.parse("((NOT B) OR C)");
		ml_a_ParseTreeSentence expected = (ml_a_ParseTreeSentence) parser.parse("(A OR C)");
		Set<ml_a_ParseTreeSentence> resolvents = resolution.plResolve(one, two);
		Assert.assertEquals(1, resolvents.size());
		Assert.assertTrue(resolvents.contains(expected));
	}

	@Test
	public void testPLResolveWithNoLiteralMatching() {
		ml_a_ParseTreeSentence one = (ml_a_ParseTreeSentence) parser.parse("(A OR B)");
		ml_a_ParseTreeSentence two = (ml_a_ParseTreeSentence) parser.parse("(C OR D)");
		Set<ml_a_ParseTreeSentence> resolvents = resolution.plResolve(one, two);
		Assert.assertEquals(0, resolvents.size());
	}

	@Test
	public void testPLResolveWithOneLiteralSentencesMatching() {
		ml_a_ParseTreeSentence one = (ml_a_ParseTreeSentence) parser.parse("A");
		ml_a_ParseTreeSentence two = (ml_a_ParseTreeSentence) parser.parse("(NOT A)");
		// Sentence expected =(Sentence) parser.parse("(A OR C)");
		Set<ml_a_ParseTreeSentence> resolvents = resolution.plResolve(one, two);
		Assert.assertEquals(1, resolvents.size());
		Assert.assertTrue(resolvents.contains(new ml_SentenceAtomicSymbol("EMPTY_CLAUSE")));
	}

	@Test
	public void testPLResolveWithTwoLiteralsMatching() {
		ml_a_ParseTreeSentence one = (ml_a_ParseTreeSentence) parser.parse("((NOT P21) OR B11)");
		ml_a_ParseTreeSentence two = (ml_a_ParseTreeSentence) parser.parse("(((NOT B11) OR P21) OR P12)");
		ml_a_ParseTreeSentence expected1 = (ml_a_ParseTreeSentence) parser
				.parse("(  ( P12 OR P21 ) OR  ( NOT P21 )  )");
		ml_a_ParseTreeSentence expected2 = (ml_a_ParseTreeSentence) parser
				.parse("(  ( B11 OR P12 ) OR  ( NOT B11 )  )");
		Set<ml_a_ParseTreeSentence> resolvents = resolution.plResolve(one, two);

		Assert.assertEquals(2, resolvents.size());
		Assert.assertTrue(resolvents.contains(expected1));
		Assert.assertTrue(resolvents.contains(expected2));
	}

	@Test
	public void testPLResolve1() {
		boolean b = resolution.plResolution("((B11 =>  (NOT P11)) AND B11)",
				"(P11)");
		Assert.assertEquals(false, b);
	}

	@Test
	public void testPLResolve2() {
		boolean b = resolution.plResolution("(A AND B)", "B");
		Assert.assertEquals(true, b);
	}

	@Test
	public void testPLResolve3() {
		boolean b = resolution.plResolution("((B11 =>  (NOT P11)) AND B11)",
				"(NOT P11)");
		Assert.assertEquals(true, b);
	}

	@Test
	public void testPLResolve4() {
		boolean b = resolution.plResolution("(A OR B)", "B");
		Assert.assertEquals(false, b);
	}

	@Test
	public void testPLResolve5() {
		boolean b = resolution.plResolution("((B11 =>  (NOT P11)) AND B11)",
				"(NOT B11)");
		Assert.assertEquals(false, b);
	}

	@Test
	public void testMultipleClauseResolution() {
		// test (and fix) suggested by Huy Dinh. Thanks Huy!
		ml_Resolution plr = new ml_Resolution();
		ml_KnowledgeBase kb = new ml_KnowledgeBase();
		String fact = "((B11 <=> (P12 OR P21)) AND (NOT B11))";
		kb.tell(fact);
		plr.plResolution(kb, "(B)");
	}

	// public void testPLResolutionWithChadCarfBugReportData() {
	// commented out coz this needs a major fix wait for a rewrite
	// KnowledgeBase kb = new KnowledgeBase();
	// kb.tell("(B12 <=> (P11 OR (P13 OR (P22 OR P02))))");
	// kb.tell("(B21 <=> (P20 OR (P22 OR (P31 OR P11))))");
	// kb.tell("(B01 <=> (P00 OR (P02 OR P11)))");
	// kb.tell("(B10 <=> (P11 OR (P20 OR P00)))");
	// kb.tell("(NOT B21)");
	// kb.tell("(NOT B12)");
	// kb.tell("(B10)");
	// kb.tell("(B01)");
	// assertTrue(resolution.plResolution(kb.asSentence().toString(), "(P00)"));
	// //assertFalse(kb.askWithDpll("(NOT P00)"));
	//
	//
	// }
}