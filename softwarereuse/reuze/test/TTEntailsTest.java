package reuze.test;
//package aima.test.core.unit.logic.propositional.algorithms;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.software.reuze.ml_KnowledgeBase;
import com.software.reuze.ml_Model;
import com.software.reuze.ml_SentenceAtomicSymbol;
import com.software.reuze.ml_TTEntails;
import com.software.reuze.ml_a_ParseTreeSentence;


/*import aima.core.logic.propositional.algorithms.KnowledgeBase;
import aima.core.logic.propositional.algorithms.Model;
import aima.core.logic.propositional.algorithms.TTEntails;
import aima.core.logic.propositional.parsing.ast.Sentence;
import aima.core.logic.propositional.parsing.ast.Symbol;*/

/**
 * @author Ravi Mohan
 * 
 */
public class TTEntailsTest {
	ml_TTEntails tte;

	ml_KnowledgeBase kb;

	@Before
	public void setUp() {
		tte = new ml_TTEntails();
		kb = new ml_KnowledgeBase();
	}

	@Test
	public void testSimpleSentence1() {
		kb.tell("(A AND B)");
		Assert.assertEquals(true, kb.askWithTTEntails("A"));
	}

	@Test
	public void testSimpleSentence2() {
		kb.tell("(A OR B)");
		Assert.assertEquals(false, kb.askWithTTEntails("A"));
	}

	@Test
	public void testSimpleSentence3() {
		kb.tell("((A => B) AND A)");
		Assert.assertEquals(true, kb.askWithTTEntails("B"));
	}

	@Test
	public void testSimpleSentence4() {
		kb.tell("((A => B) AND B)");
		Assert.assertEquals(false, kb.askWithTTEntails("A"));
	}

	@Test
	public void testSimpleSentence5() {
		kb.tell("A");
		Assert.assertEquals(false, kb.askWithTTEntails("NOT A"));
	}

	@Test
	public void testSUnkownSymbol() {
		kb.tell("((A => B) AND B)");
		Assert.assertEquals(false, kb.askWithTTEntails("X"));
	}

	@Test
	public void testSimpleSentence6() {
		kb.tell("NOT A");
		Assert.assertEquals(false, kb.askWithTTEntails("A"));
	}

	@Test
	public void testNewAIMAExample() {
		kb.tell("(NOT P11)");
		kb.tell("(B11 <=> (P12 OR P21))");
		kb.tell("(B21 <=> ((P11 OR P22) OR P31))");
		kb.tell("(NOT B11)");
		kb.tell("(B21)");

		Assert.assertEquals(true, kb.askWithTTEntails("NOT P12"));
		Assert.assertEquals(false, kb.askWithTTEntails("P22"));
	}

	@Test
	public void testTTEntailsSucceedsWithChadCarffsBugReport() {
		ml_KnowledgeBase kb = new ml_KnowledgeBase();
		kb.tell("(B12 <=> (P11 OR (P13 OR (P22 OR P02))))");
		kb.tell("(B21 <=> (P20 OR (P22 OR (P31 OR P11))))");
		kb.tell("(B01 <=> (P00 OR (P02 OR P11)))");
		kb.tell("(B10 <=> (P11 OR (P20 OR P00)))");
		kb.tell("(NOT B21)");
		kb.tell("(NOT B12)");
		kb.tell("(B10)");
		kb.tell("(B01)");

		Assert.assertTrue(kb.askWithTTEntails("(P00)"));
		Assert.assertFalse(kb.askWithTTEntails("(NOT P00)"));
	}

	@Test
	public void testDoesNotKnow() {
		ml_KnowledgeBase kb = new ml_KnowledgeBase();
		kb.tell("A");
		Assert.assertFalse(kb.askWithTTEntails("B"));
		Assert.assertFalse(kb.askWithTTEntails("(NOT B)"));
	}

	// public void testTTEntailsSucceedsWithCStackOverFlowBugReport() {
	// KnowledgeBase kb = new KnowledgeBase();
	//
	// assertTrue(kb.askWithTTEntails("((A OR (NOT A)) AND (A OR B))"));
	// }

	@Test
	public void testModelEvaluation() {
		kb.tell("(NOT P11)");
		kb.tell("(B11 <=> (P12 OR P21))");
		kb.tell("(B21 <=> ((P11 OR P22) OR P31))");
		kb.tell("(NOT B11)");
		kb.tell("(B21)");

		ml_Model model = new ml_Model();
		model = model.extend(new ml_SentenceAtomicSymbol("B11"), false);
		model = model.extend(new ml_SentenceAtomicSymbol("B21"), true);
		model = model.extend(new ml_SentenceAtomicSymbol("P11"), false);
		model = model.extend(new ml_SentenceAtomicSymbol("P12"), false);
		model = model.extend(new ml_SentenceAtomicSymbol("P21"), false);
		model = model.extend(new ml_SentenceAtomicSymbol("P22"), false);
		model = model.extend(new ml_SentenceAtomicSymbol("P31"), true);

		ml_a_ParseTreeSentence kbs = kb.asSentence();
		Assert.assertEquals(true, model.isTrue(kbs));
	}
}