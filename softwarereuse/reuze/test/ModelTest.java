package reuze.test;
//package aima.test.core.unit.logic.propositional.algorithms;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.software.reuze.ml_Model;
import com.software.reuze.ml_Parser;
import com.software.reuze.ml_SentenceAtomicSymbol;
import com.software.reuze.ml_a_ParseTreeSentence;


/*import aima.core.logic.propositional.algorithms.Model;
import aima.core.logic.propositional.parsing.PEParser;
import aima.core.logic.propositional.parsing.ast.Sentence;
import aima.core.logic.propositional.parsing.ast.Symbol;*/

/**
 * @author Ravi Mohan
 * 
 */
public class ModelTest {
	private ml_Model m;

	private ml_Parser parser;

	ml_a_ParseTreeSentence trueSentence, falseSentence, andSentence, orSentence,
			impliedSentence, biConditionalSentence;

	@Before
	public void setUp() {
		parser = new ml_Parser();
		trueSentence = (ml_a_ParseTreeSentence) parser.parse("true");
		falseSentence = (ml_a_ParseTreeSentence) parser.parse("false");
		andSentence = (ml_a_ParseTreeSentence) parser.parse("(P  AND  Q)");
		orSentence = (ml_a_ParseTreeSentence) parser.parse("(P  OR  Q)");
		impliedSentence = (ml_a_ParseTreeSentence) parser.parse("(P  =>  Q)");
		biConditionalSentence = (ml_a_ParseTreeSentence) parser.parse("(P  <=>  Q)");
		m = new ml_Model();
	}

	@Test
	public void testEmptyModel() {
		Assert.assertEquals(null, m.getStatus(new ml_SentenceAtomicSymbol("P")));
		Assert.assertEquals(true, m.isUnknown(new ml_SentenceAtomicSymbol("P")));
	}

	@Test
	public void testExtendModel() {
		String p = "P";
		m = m.extend(new ml_SentenceAtomicSymbol(p), true);
		Assert.assertEquals(Boolean.TRUE, m.getStatus(new ml_SentenceAtomicSymbol("P")));
	}

	@Test
	public void testTrueFalseEvaluation() {
		Assert.assertEquals(true, m.isTrue(trueSentence));
		Assert.assertEquals(false, m.isFalse(trueSentence));
		Assert.assertEquals(false, m.isTrue(falseSentence));
		Assert.assertEquals(true, m.isFalse(falseSentence));
	}

	@Test
	public void testSentenceStatusWhenPTrueAndQTrue() {
		String p = "P";
		String q = "Q";
		m = m.extend(new ml_SentenceAtomicSymbol(p), true);
		m = m.extend(new ml_SentenceAtomicSymbol(q), true);
		Assert.assertEquals(true, m.isTrue(andSentence));
		Assert.assertEquals(true, m.isTrue(orSentence));
		Assert.assertEquals(true, m.isTrue(impliedSentence));
		Assert.assertEquals(true, m.isTrue(biConditionalSentence));
	}

	@Test
	public void testSentenceStatusWhenPFalseAndQFalse() {
		String p = "P";
		String q = "Q";
		m = m.extend(new ml_SentenceAtomicSymbol(p), false);
		m = m.extend(new ml_SentenceAtomicSymbol(q), false);
		Assert.assertEquals(true, m.isFalse(andSentence));
		Assert.assertEquals(true, m.isFalse(orSentence));
		Assert.assertEquals(true, m.isTrue(impliedSentence));
		Assert.assertEquals(true, m.isTrue(biConditionalSentence));
	}

	@Test
	public void testSentenceStatusWhenPTrueAndQFalse() {
		String p = "P";
		String q = "Q";
		m = m.extend(new ml_SentenceAtomicSymbol(p), true);
		m = m.extend(new ml_SentenceAtomicSymbol(q), false);
		Assert.assertEquals(true, m.isFalse(andSentence));
		Assert.assertEquals(true, m.isTrue(orSentence));
		Assert.assertEquals(true, m.isFalse(impliedSentence));
		Assert.assertEquals(true, m.isFalse(biConditionalSentence));
	}

	@Test
	public void testSentenceStatusWhenPFalseAndQTrue() {
		String p = "P";
		String q = "Q";
		m = m.extend(new ml_SentenceAtomicSymbol(p), false);
		m = m.extend(new ml_SentenceAtomicSymbol(q), true);
		Assert.assertEquals(true, m.isFalse(andSentence));
		Assert.assertEquals(true, m.isTrue(orSentence));
		Assert.assertEquals(true, m.isTrue(impliedSentence));
		Assert.assertEquals(true, m.isFalse(biConditionalSentence));
	}

	@Test
	public void testComplexSentence() {
		String p = "P";
		String q = "Q";
		m = m.extend(new ml_SentenceAtomicSymbol(p), true);
		m = m.extend(new ml_SentenceAtomicSymbol(q), false);
		ml_a_ParseTreeSentence sent = (ml_a_ParseTreeSentence) parser.parse("((P OR Q) AND  (P => Q))");
		Assert.assertFalse(m.isTrue(sent));
		Assert.assertTrue(m.isFalse(sent));
		ml_a_ParseTreeSentence sent2 = (ml_a_ParseTreeSentence) parser.parse("((P OR Q) AND  (Q))");
		Assert.assertFalse(m.isTrue(sent2));
		Assert.assertTrue(m.isFalse(sent2));
	}
}