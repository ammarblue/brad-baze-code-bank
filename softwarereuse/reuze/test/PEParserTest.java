package reuze.test;
//package aima.test.core.unit.logic.propositional.parsing;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.software.reuze.ml_Parser;
import com.software.reuze.ml_SentenceAtomicFalse;
import com.software.reuze.ml_SentenceAtomicSymbol;
import com.software.reuze.ml_SentenceAtomicTrue;
import com.software.reuze.ml_SentenceBinary;
import com.software.reuze.ml_SentenceComplexMulti;
import com.software.reuze.ml_SentenceComplexUnary;
import com.software.reuze.ml_a_SentenceAtomic;


/*import aima.core.logic.propositional.parsing.PEParser;
import aima.core.logic.propositional.parsing.ast.AtomicSentence;
import aima.core.logic.propositional.parsing.ast.BinarySentence;
import aima.core.logic.propositional.parsing.ast.FalseSentence;
import aima.core.logic.propositional.parsing.ast.MultiSentence;
import aima.core.logic.propositional.parsing.ast.Symbol;
import aima.core.logic.propositional.parsing.ast.TrueSentence;
import aima.core.logic.propositional.parsing.ast.UnarySentence;*/

/**
 * @author Ravi Mohan
 * 
 */
public class PEParserTest {
	private ml_Parser parser;

	@Before
	public void setUp() {
		parser = new ml_Parser();
	}

	@Test
	public void testAtomicSentenceTrueParse() {
		ml_a_SentenceAtomic sen = (ml_a_SentenceAtomic) parser.parse("true");
		Assert.assertEquals(ml_SentenceAtomicTrue.class, sen.getClass());
		sen = (ml_a_SentenceAtomic) parser.parse("(true)");
		Assert.assertEquals(ml_SentenceAtomicTrue.class, sen.getClass());
		sen = (ml_a_SentenceAtomic) parser.parse("((true))");
		Assert.assertEquals(ml_SentenceAtomicTrue.class, sen.getClass());
	}

	@Test
	public void testAtomicSentenceFalseParse() {
		ml_a_SentenceAtomic sen = (ml_a_SentenceAtomic) parser.parse("faLse");
		Assert.assertEquals(ml_SentenceAtomicFalse.class, sen.getClass());
	}

	@Test
	public void testAtomicSentenceSymbolParse() {
		ml_a_SentenceAtomic sen = (ml_a_SentenceAtomic) parser.parse("AIMA");
		Assert.assertEquals(ml_SentenceAtomicSymbol.class, sen.getClass());
	}

	@Test
	public void testNotSentenceParse() {
		ml_SentenceComplexUnary sen = (ml_SentenceComplexUnary) parser.parse("NOT AIMA");
		Assert.assertEquals(ml_SentenceComplexUnary.class, sen.getClass());
	}

	@Test
	public void testBinarySentenceParse() {
		ml_SentenceBinary sen = (ml_SentenceBinary) parser
				.parse("(PETER  AND  NORVIG)");
		Assert.assertEquals(ml_SentenceBinary.class, sen.getClass());
	}

	@Test
	public void testMultiSentenceAndParse() {
		ml_SentenceComplexMulti sen = (ml_SentenceComplexMulti) parser
				.parse("(AND  NORVIG AIMA LISP)");
		Assert.assertEquals(ml_SentenceComplexMulti.class, sen.getClass());
	}

	@Test
	public void testMultiSentenceOrParse() {
		ml_SentenceComplexMulti sen = (ml_SentenceComplexMulti) parser
				.parse("(OR  NORVIG AIMA LISP)");
		Assert.assertEquals(ml_SentenceComplexMulti.class, sen.getClass());
	}

	@Test
	public void testMultiSentenceBracketedParse() {
		ml_SentenceComplexMulti sen = (ml_SentenceComplexMulti) parser
				.parse("((OR  NORVIG AIMA LISP))");
		Assert.assertEquals(ml_SentenceComplexMulti.class, sen.getClass());
	}

	@Test
	public void testComplexSentenceParse() {
		ml_SentenceBinary sen = (ml_SentenceBinary) parser
				.parse("((OR  NORVIG AIMA LISP) AND TRUE)");
		Assert.assertEquals(ml_SentenceBinary.class, sen.getClass());

		sen = (ml_SentenceBinary) parser
				.parse("((OR  NORVIG AIMA LISP) AND (((LISP => COOL))))");
		Assert.assertEquals(ml_SentenceBinary.class, sen.getClass());
		Assert.assertEquals(
				" ( ( OR NORVIG AIMA LISP  )  AND  ( LISP => COOL ) )",
				sen.toString());

		String s = "((NOT (P AND Q ))  AND ((NOT (R AND S))))";
		sen = (ml_SentenceBinary) parser.parse(s);
		Assert.assertEquals(
				" (  ( NOT  ( P AND Q ) )  AND  ( NOT  ( R AND S ) )  )",
				sen.toString());

		s = "((P AND Q) OR (S AND T))";
		sen = (ml_SentenceBinary) parser.parse(s);
		Assert.assertEquals(" (  ( P AND Q ) OR  ( S AND T ) )", sen.toString());
		Assert.assertEquals("OR", sen.getOperator());

		s = "(NOT ((P AND Q) => (S AND T)))";
		ml_SentenceComplexUnary nsen = (ml_SentenceComplexUnary) parser.parse(s);
		// assertEquals("=>",sen.getOperator());
		s = "(NOT (P <=> (S AND T)))";
		nsen = (ml_SentenceComplexUnary) parser.parse(s);
		Assert.assertEquals(" ( NOT  ( P <=>  ( S AND T ) ) ) ",
				nsen.toString());

		s = "(P <=> (S AND T))";
		sen = (ml_SentenceBinary) parser.parse(s);

		s = "(P => Q)";
		sen = (ml_SentenceBinary) parser.parse(s);

		s = "((P AND Q) => R)";
		sen = (ml_SentenceBinary) parser.parse(s);
	}
}
