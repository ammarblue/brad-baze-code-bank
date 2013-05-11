package reuze.test;
//package aima.test.core.unit.logic.propositional.visitors;

import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.software.reuze.ml_Parser;
import com.software.reuze.ml_SentenceAtomicSymbol;
import com.software.reuze.ml_SymbolClassifier;
import com.software.reuze.ml_a_ParseTreeSentence;


/*import aima.core.logic.propositional.parsing.PEParser;
import aima.core.logic.propositional.parsing.ast.Sentence;
import aima.core.logic.propositional.parsing.ast.Symbol;
import aima.core.logic.propositional.visitors.SymbolClassifier;*/

/**
 * @author Ravi Mohan
 * 
 */
public class SymbolClassifierTest {
	private ml_SymbolClassifier classifier;

	private ml_Parser parser;

	@Before
	public void setUp() {
		classifier = new ml_SymbolClassifier();
		parser = new ml_Parser();

	}

	@Test
	public void testSimpleNegativeSymbol() {
		ml_a_ParseTreeSentence sentence = (ml_a_ParseTreeSentence) parser.parse("(NOT B)");

		Set<ml_SentenceAtomicSymbol> neg = classifier.getNegativeSymbolsIn(sentence);
		Set<ml_SentenceAtomicSymbol> pos = classifier.getPositiveSymbolsIn(sentence);

		Set<ml_SentenceAtomicSymbol> pureNeg = classifier.getPureNegativeSymbolsIn(sentence);
		Set<ml_SentenceAtomicSymbol> purePos = classifier.getPurePositiveSymbolsIn(sentence);

		Set<ml_SentenceAtomicSymbol> pure = classifier.getPureSymbolsIn(sentence);
		Set<ml_SentenceAtomicSymbol> impure = classifier.getImpureSymbolsIn(sentence);

		ml_a_ParseTreeSentence b = (ml_a_ParseTreeSentence) parser.parse("B");

		Assert.assertEquals(1, neg.size());
		Assert.assertTrue(neg.contains(b));

		Assert.assertEquals(0, pos.size());

		Assert.assertEquals(1, pureNeg.size());
		Assert.assertTrue(pureNeg.contains(b));

		Assert.assertEquals(0, purePos.size());

		Assert.assertEquals(1, pure.size());
		Assert.assertTrue(pure.contains(b));

		Assert.assertEquals(0, impure.size());
	}

	@Test
	public void testSimplePositiveSymbol() {
		ml_a_ParseTreeSentence sentence = (ml_a_ParseTreeSentence) parser.parse("B");
		Set<ml_SentenceAtomicSymbol> neg = classifier.getNegativeSymbolsIn(sentence);
		Set<ml_SentenceAtomicSymbol> pos = classifier.getPositiveSymbolsIn(sentence);

		Set<ml_SentenceAtomicSymbol> pureNeg = classifier.getPureNegativeSymbolsIn(sentence);
		Set<ml_SentenceAtomicSymbol> purePos = classifier.getPurePositiveSymbolsIn(sentence);

		Set<ml_SentenceAtomicSymbol> pure = classifier.getPureSymbolsIn(sentence);
		Set<ml_SentenceAtomicSymbol> impure = classifier.getImpureSymbolsIn(sentence);

		Assert.assertEquals(0, neg.size());

		Assert.assertEquals(1, pos.size());
		ml_a_ParseTreeSentence b = (ml_a_ParseTreeSentence) parser.parse("B");
		Assert.assertTrue(pos.contains(b));

		Assert.assertEquals(1, purePos.size());
		Assert.assertTrue(purePos.contains(b));

		Assert.assertEquals(0, pureNeg.size());
		Assert.assertEquals(1, pure.size());

		Assert.assertTrue(pure.contains(b));

		Assert.assertEquals(0, impure.size());
	}

	@Test
	public void testSingleSymbolPositiveAndNegative() {
		ml_a_ParseTreeSentence sentence = (ml_a_ParseTreeSentence) parser.parse("(B AND (NOT B))");
		Set<ml_SentenceAtomicSymbol> neg = classifier.getNegativeSymbolsIn(sentence);
		Set<ml_SentenceAtomicSymbol> pos = classifier.getPositiveSymbolsIn(sentence);

		Set<ml_SentenceAtomicSymbol> pureNeg = classifier.getPureNegativeSymbolsIn(sentence);
		Set<ml_SentenceAtomicSymbol> purePos = classifier.getPurePositiveSymbolsIn(sentence);

		Set<ml_SentenceAtomicSymbol> pure = classifier.getPureSymbolsIn(sentence);
		Set<ml_SentenceAtomicSymbol> impure = classifier.getImpureSymbolsIn(sentence);

		ml_a_ParseTreeSentence b = (ml_a_ParseTreeSentence) parser.parse("B");

		Assert.assertEquals(1, neg.size());
		Assert.assertTrue(neg.contains(b));

		Assert.assertEquals(1, pos.size());
		Assert.assertTrue(pos.contains(b));

		Assert.assertEquals(0, pureNeg.size());
		Assert.assertEquals(0, purePos.size());
		Assert.assertEquals(0, pure.size());
		Assert.assertEquals(1, impure.size());
	}

	@Test
	public void testAIMA2eExample() {
		// 2nd Edition Pg 221
		ml_a_ParseTreeSentence sentence = (ml_a_ParseTreeSentence) parser
				.parse("(((A OR (NOT B)) AND ((NOT B) OR (NOT C))) AND (C OR A))");

		Set<ml_SentenceAtomicSymbol> neg = classifier.getNegativeSymbolsIn(sentence);
		Set<ml_SentenceAtomicSymbol> pos = classifier.getPositiveSymbolsIn(sentence);

		Set<ml_SentenceAtomicSymbol> pureNeg = classifier.getPureNegativeSymbolsIn(sentence);
		Set<ml_SentenceAtomicSymbol> purePos = classifier.getPurePositiveSymbolsIn(sentence);

		Set<ml_SentenceAtomicSymbol> pure = classifier.getPureSymbolsIn(sentence);
		Set<ml_SentenceAtomicSymbol> impure = classifier.getImpureSymbolsIn(sentence);

		ml_a_ParseTreeSentence a = (ml_a_ParseTreeSentence) parser.parse("A");
		ml_a_ParseTreeSentence b = (ml_a_ParseTreeSentence) parser.parse("B");
		ml_a_ParseTreeSentence c = (ml_a_ParseTreeSentence) parser.parse("C");

		Assert.assertEquals(2, neg.size());
		Assert.assertTrue(neg.contains(b));
		Assert.assertTrue(neg.contains(c));

		Assert.assertEquals(2, pos.size());
		Assert.assertTrue(pos.contains(a));
		Assert.assertTrue(pos.contains(c));

		Assert.assertEquals(1, pureNeg.size());
		Assert.assertTrue(pureNeg.contains(b));

		Assert.assertEquals(1, purePos.size());
		Assert.assertTrue(purePos.contains(a));

		Assert.assertEquals(2, pure.size());
		Assert.assertTrue(pure.contains(a));
		Assert.assertTrue(pure.contains(b));

		Assert.assertEquals(1, impure.size());
		Assert.assertTrue(impure.contains(c));
	}
}