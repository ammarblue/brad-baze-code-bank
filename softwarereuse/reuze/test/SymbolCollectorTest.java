package reuze.test;
//package aima.test.core.unit.logic.propositional.visitors;

import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.software.reuze.ml_Parser;
import com.software.reuze.ml_SentenceAtomicSymbol;
import com.software.reuze.ml_SentenceVisitorSymbolCollector;
import com.software.reuze.ml_a_ParseTreeSentence;


/*import aima.core.logic.propositional.parsing.PEParser;
import aima.core.logic.propositional.parsing.ast.Sentence;
import aima.core.logic.propositional.parsing.ast.Symbol;
import aima.core.logic.propositional.visitors.SymbolCollector;*/

/**
 * @author Ravi Mohan
 * 
 */
public class SymbolCollectorTest {
	private ml_Parser parser;

	private ml_SentenceVisitorSymbolCollector collector;

	@Before
	public void setUp() {
		parser = new ml_Parser();
		collector = new ml_SentenceVisitorSymbolCollector();
	}

	@Test
	public void testCollectSymbolsFromComplexSentence() {
		ml_a_ParseTreeSentence sentence = (ml_a_ParseTreeSentence) parser
				.parse(" (  (  ( NOT B11 )  OR  ( P12 OR P21 ) ) AND  (  ( B11 OR  ( NOT P12 )  ) AND  ( B11 OR  ( NOT P21 )  ) ) )");
		Set<ml_SentenceAtomicSymbol> s = collector.getSymbolsIn(sentence);
		Assert.assertEquals(3, s.size());
		ml_a_ParseTreeSentence b11 = (ml_a_ParseTreeSentence) parser.parse("B11");
		ml_a_ParseTreeSentence p21 = (ml_a_ParseTreeSentence) parser.parse("P21");
		ml_a_ParseTreeSentence p12 = (ml_a_ParseTreeSentence) parser.parse("P12");
		Assert.assertTrue(s.contains(b11));
		Assert.assertTrue(s.contains(p21));
		Assert.assertTrue(s.contains(p12));
	}
}