package reuze.test;
//package aima.test.core.unit.logic.fol.inference;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.software.reuze.mf_Clause;
import com.software.reuze.mf_Demodulation;
import com.software.reuze.mf_Domain;
import com.software.reuze.mf_Literal;
import com.software.reuze.mf_Parser;
import com.software.reuze.mf_Predicate;
import com.software.reuze.mf_SentenceAtomicTermEquality;


/*import aima.core.logic.fol.domain.FOLDomain;
import aima.core.logic.fol.inference.Demodulation;
import aima.core.logic.fol.kb.data.Clause;
import aima.core.logic.fol.kb.data.Literal;
import aima.core.logic.fol.parsing.FOLParser;
import aima.core.logic.fol.parsing.ast.Predicate;
import aima.core.logic.fol.parsing.ast.TermEquality;*/

/**
 * @author Ciaran O'Reilly
 * 
 */
public class DemodulationTest {

	private mf_Demodulation demodulation = null;

	@Before
	public void setUp() {
		demodulation = new mf_Demodulation();
	}

	// Note: Based on:
	// http://logic.stanford.edu/classes/cs157/2008/lectures/lecture15.pdf
	// Slide 22.
	@Test
	public void testSimpleAtomicExamples() {
		mf_Domain domain = new mf_Domain();
		domain.addConstant("A");
		domain.addConstant("B");
		domain.addConstant("C");
		domain.addConstant("D");
		domain.addConstant("E");
		domain.addPredicate("P");
		domain.addFunction("F");
		domain.addFunction("G");
		domain.addFunction("H");
		domain.addFunction("J");

		mf_Parser parser = new mf_Parser(domain);

		mf_Predicate expression = (mf_Predicate) parser
				.parse("P(A,F(B,G(A,H(B)),C),D)");
		mf_SentenceAtomicTermEquality assertion = (mf_SentenceAtomicTermEquality) parser.parse("B = E");

		mf_Predicate altExpression = (mf_Predicate) demodulation.apply(assertion,
				expression);

		Assert.assertFalse(expression.equals(altExpression));
		Assert.assertEquals("P(A,F(E,G(A,H(B)),C),D)", altExpression.toString());

		altExpression = (mf_Predicate) demodulation
				.apply(assertion, altExpression);

		Assert.assertEquals("P(A,F(E,G(A,H(E)),C),D)", altExpression.toString());

		assertion = (mf_SentenceAtomicTermEquality) parser.parse("G(x,y) = J(x)");

		altExpression = (mf_Predicate) demodulation.apply(assertion, expression);

		Assert.assertEquals("P(A,F(B,J(A),C),D)", altExpression.toString());
	}

	// Note: Based on:
	// http://logic.stanford.edu/classes/cs157/2008/lectures/lecture15.pdf
	// Slide 23.
	@Test
	public void testSimpleAtomicNonExample() {
		mf_Domain domain = new mf_Domain();
		domain.addConstant("A");
		domain.addConstant("B");
		domain.addConstant("C");
		domain.addConstant("D");
		domain.addConstant("E");
		domain.addPredicate("P");
		domain.addFunction("F");
		domain.addFunction("G");
		domain.addFunction("H");
		domain.addFunction("J");

		mf_Parser parser = new mf_Parser(domain);

		mf_Predicate expression = (mf_Predicate) parser.parse("P(A,G(x,B),C)");
		mf_SentenceAtomicTermEquality assertion = (mf_SentenceAtomicTermEquality) parser.parse("G(A,y) = J(y)");

		mf_Predicate altExpression = (mf_Predicate) demodulation.apply(assertion,
				expression);

		Assert.assertNull(altExpression);
	}

	@Test
	public void testSimpleClauseExamples() {
		mf_Domain domain = new mf_Domain();
		domain.addConstant("A");
		domain.addConstant("B");
		domain.addConstant("C");
		domain.addConstant("D");
		domain.addConstant("E");
		domain.addPredicate("P");
		domain.addPredicate("Q");
		domain.addPredicate("W");
		domain.addFunction("F");
		domain.addFunction("G");
		domain.addFunction("H");
		domain.addFunction("J");

		mf_Parser parser = new mf_Parser(domain);

		List<mf_Literal> lits = new ArrayList<mf_Literal>();
		mf_Predicate p1 = (mf_Predicate) parser.parse("Q(z, G(D,B))");
		mf_Predicate p2 = (mf_Predicate) parser.parse("P(x, G(A,C))");
		mf_Predicate p3 = (mf_Predicate) parser.parse("W(z,x,u,w,y)");
		lits.add(new mf_Literal(p1));
		lits.add(new mf_Literal(p2));
		lits.add(new mf_Literal(p3));

		mf_Clause clExpression = new mf_Clause(lits);

		mf_SentenceAtomicTermEquality assertion = (mf_SentenceAtomicTermEquality) parser.parse("G(x,y) = x");

		mf_Clause altClExpression = demodulation.apply(assertion, clExpression);

		Assert.assertEquals("[P(x,G(A,C)), Q(z,D), W(z,x,u,w,y)]",
				altClExpression.toString());

		altClExpression = demodulation.apply(assertion, altClExpression);

		Assert.assertEquals("[P(x,A), Q(z,D), W(z,x,u,w,y)]",
				altClExpression.toString());
	}

	@Test
	public void testSimpleClauseNonExample() {
		mf_Domain domain = new mf_Domain();
		domain.addConstant("A");
		domain.addConstant("B");
		domain.addConstant("C");
		domain.addPredicate("P");
		domain.addFunction("F");

		mf_Parser parser = new mf_Parser(domain);

		List<mf_Literal> lits = new ArrayList<mf_Literal>();
		mf_Predicate p1 = (mf_Predicate) parser.parse("P(y, F(A,y))");
		lits.add(new mf_Literal(p1));

		mf_Clause clExpression = new mf_Clause(lits);

		mf_SentenceAtomicTermEquality assertion = (mf_SentenceAtomicTermEquality) parser.parse("F(x,B) = C");

		mf_Clause altClExpression = demodulation.apply(assertion, clExpression);

		Assert.assertNull(altClExpression);
	}

	@Test
	public void testBypassReflexivityAxiom() {
		mf_Domain domain = new mf_Domain();
		domain.addConstant("A");
		domain.addConstant("B");
		domain.addConstant("C");
		domain.addPredicate("P");
		domain.addFunction("F");

		mf_Parser parser = new mf_Parser(domain);

		List<mf_Literal> lits = new ArrayList<mf_Literal>();
		mf_Predicate p1 = (mf_Predicate) parser.parse("P(y, F(A,y))");
		lits.add(new mf_Literal(p1));

		mf_Clause clExpression = new mf_Clause(lits);

		mf_SentenceAtomicTermEquality assertion = (mf_SentenceAtomicTermEquality) parser.parse("x = x");

		mf_Clause altClExpression = demodulation.apply(assertion, clExpression);

		Assert.assertNull(altClExpression);
	}
}
