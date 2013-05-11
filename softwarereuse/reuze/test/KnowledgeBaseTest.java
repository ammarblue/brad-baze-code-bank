package reuze.test;
//package aima.test.core.unit.logic.propositional.algorithms;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.software.reuze.ml_KnowledgeBase;


//import aima.core.logic.propositional.algorithms.KnowledgeBase;

/**
 * @author Ravi Mohan
 * 
 */
public class KnowledgeBaseTest {
	private ml_KnowledgeBase kb;

	@Before
	public void setUp() {
		kb = new ml_KnowledgeBase();
	}

	@Test
	public void testTellInsertsSentence() {
		kb.tell("(A AND B)");
		Assert.assertEquals(1, kb.size());
	}

	@Test
	public void testTellDoesNotInsertSameSentenceTwice() {
		kb.tell("(A AND B)");
		Assert.assertEquals(1, kb.size());
		kb.tell("(A AND B)");
		Assert.assertEquals(1, kb.size());
	}

	@Test
	public void testEmptyKnowledgeBaseIsAnEmptyString() {
		Assert.assertEquals("", kb.toString());
	}

	@Test
	public void testKnowledgeBaseWithOneSentenceToString() {
		kb.tell("(A AND B)");
		Assert.assertEquals(" ( A AND B )", kb.toString());
	}

	@Test
	public void testKnowledgeBaseWithTwoSentencesToString() {
		kb.tell("(A AND B)");
		kb.tell("(C AND D)");
		Assert.assertEquals(" (  ( A AND B ) AND  ( C AND D ) )", kb.toString());
	}

	@Test
	public void testKnowledgeBaseWithThreeSentencesToString() {
		kb.tell("(A AND B)");
		kb.tell("(C AND D)");
		kb.tell("(E AND F)");
		Assert.assertEquals(
				" (  (  ( A AND B ) AND  ( C AND D ) ) AND  ( E AND F ) )",
				kb.toString());
	}
}
