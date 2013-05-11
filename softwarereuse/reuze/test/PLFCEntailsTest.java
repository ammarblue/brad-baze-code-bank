package reuze.test;
//package aima.test.core.unit.logic.propositional.algorithms;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.software.reuze.ml_KnowledgeBase;
import com.software.reuze.ml_PLFCEntails;
import com.software.reuze.ml_Parser;


//import aima.core.logic.propositional.algorithms.KnowledgeBase;
//import aima.core.logic.propositional.algorithms.PLFCEntails;
//import aima.core.logic.propositional.parsing.PEParser;

/**
 * @author Ravi Mohan
 * 
 */
public class PLFCEntailsTest {
	ml_Parser parser;

	ml_PLFCEntails plfce;

	@Before
	public void setUp() {
		plfce = new ml_PLFCEntails();
	}

	@Test
	public void testAIMAExample() {
		ml_KnowledgeBase kb = new ml_KnowledgeBase();
		kb.tell(" (P => Q)");
		kb.tell("((L AND M) => P)");
		kb.tell("((B AND L) => M)");
		kb.tell("( (A AND P) => L)");
		kb.tell("((A AND B) => L)");
		kb.tell("(A)");
		kb.tell("(B)");

		Assert.assertEquals(true, plfce.plfcEntails(kb, "Q"));
	}
}