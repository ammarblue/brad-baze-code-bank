package reuze.test;
//package aima.test.core.unit.logic.fol.inference;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.software.reuze.mf_Clause;
import com.software.reuze.mf_Domain;
import com.software.reuze.mf_Literal;
import com.software.reuze.mf_Paramodulation;
import com.software.reuze.mf_Parser;
import com.software.reuze.mf_i_SentenceAtomic;


/*import aima.core.logic.fol.domain.FOLDomain;
import aima.core.logic.fol.inference.Paramodulation;
import aima.core.logic.fol.kb.data.Clause;
import aima.core.logic.fol.kb.data.Literal;
import aima.core.logic.fol.parsing.FOLParser;
import aima.core.logic.fol.parsing.ast.FOAtomicSentence;*/

/**
 * @author Ciaran O'Reilly
 * 
 */
public class ParamodulationTest {

	private mf_Paramodulation paramodulation = null;

	@Before
	public void setUp() {
		paramodulation = new mf_Paramodulation();
	}

	// Note: Based on:
	// http://logic.stanford.edu/classes/cs157/2008/lectures/lecture15.pdf
	// Slide 31.
	@Test
	public void testSimpleExample() {
		mf_Domain domain = new mf_Domain();
		domain.addConstant("A");
		domain.addConstant("B");
		domain.addPredicate("P");
		domain.addPredicate("Q");
		domain.addPredicate("R");
		domain.addFunction("F");

		mf_Parser parser = new mf_Parser(domain);

		List<mf_Literal> lits = new ArrayList<mf_Literal>();
		mf_i_SentenceAtomic a1 = (mf_i_SentenceAtomic) parser.parse("P(F(x,B),x)");
		mf_i_SentenceAtomic a2 = (mf_i_SentenceAtomic) parser.parse("Q(x)");
		lits.add(new mf_Literal(a1));
		lits.add(new mf_Literal(a2));

		mf_Clause c1 = new mf_Clause(lits);

		lits.clear();
		a1 = (mf_i_SentenceAtomic) parser.parse("F(A,y) = y");
		a2 = (mf_i_SentenceAtomic) parser.parse("R(y)");
		lits.add(new mf_Literal(a1));
		lits.add(new mf_Literal(a2));

		mf_Clause c2 = new mf_Clause(lits);

		Set<mf_Clause> paras = paramodulation.apply(c1, c2);
		Assert.assertEquals(2, paras.size());

		Iterator<mf_Clause> it = paras.iterator();
		Assert.assertEquals("[P(B,A), Q(A), R(B)]", it.next().toString());
		Assert.assertEquals("[P(F(A,F(x,B)),x), Q(x), R(F(x,B))]", it.next()
				.toString());
	}

	@Test
	public void testMultipleTermEqualitiesInBothClausesExample() {
		mf_Domain domain = new mf_Domain();
		domain.addConstant("A");
		domain.addConstant("B");
		domain.addConstant("C");
		domain.addConstant("D");
		domain.addPredicate("P");
		domain.addPredicate("Q");
		domain.addPredicate("R");
		domain.addFunction("F");

		mf_Parser parser = new mf_Parser(domain);

		List<mf_Literal> lits = new ArrayList<mf_Literal>();
		mf_i_SentenceAtomic a1 = (mf_i_SentenceAtomic) parser.parse("F(C,x) = D");
		mf_i_SentenceAtomic a2 = (mf_i_SentenceAtomic) parser.parse("A = D");
		mf_i_SentenceAtomic a3 = (mf_i_SentenceAtomic) parser.parse("P(F(x,B),x)");
		mf_i_SentenceAtomic a4 = (mf_i_SentenceAtomic) parser.parse("Q(x)");
		mf_i_SentenceAtomic a5 = (mf_i_SentenceAtomic) parser.parse("R(C)");
		lits.add(new mf_Literal(a1));
		lits.add(new mf_Literal(a2));
		lits.add(new mf_Literal(a3));
		lits.add(new mf_Literal(a4));
		lits.add(new mf_Literal(a5));

		mf_Clause c1 = new mf_Clause(lits);

		lits.clear();
		a1 = (mf_i_SentenceAtomic) parser.parse("F(A,y) = y");
		a2 = (mf_i_SentenceAtomic) parser.parse("F(B,y) = C");
		a3 = (mf_i_SentenceAtomic) parser.parse("R(y)");
		a4 = (mf_i_SentenceAtomic) parser.parse("R(A)");
		lits.add(new mf_Literal(a1));
		lits.add(new mf_Literal(a2));
		lits.add(new mf_Literal(a3));
		lits.add(new mf_Literal(a4));

		mf_Clause c2 = new mf_Clause(lits);

		Set<mf_Clause> paras = paramodulation.apply(c1, c2);
		Assert.assertEquals(5, paras.size());

		Iterator<mf_Clause> it = paras.iterator();
		Assert.assertEquals(
				"[F(B,B) = C, F(C,A) = D, A = D, P(B,A), Q(A), R(A), R(B), R(C)]",
				it.next().toString());
		Assert.assertEquals(
				"[F(A,F(C,x)) = D, F(B,F(C,x)) = C, A = D, P(F(x,B),x), Q(x), R(F(C,x)), R(A), R(C)]",
				it.next().toString());
		Assert.assertEquals(
				"[F(A,B) = B, F(C,B) = D, A = D, P(C,B), Q(B), R(A), R(B), R(C)]",
				it.next().toString());
		Assert.assertEquals(
				"[F(F(B,y),x) = D, F(A,y) = y, A = D, P(F(x,B),x), Q(x), R(y), R(A), R(C)]",
				it.next().toString());
		Assert.assertEquals(
				"[F(B,y) = C, F(C,x) = D, F(D,y) = y, P(F(x,B),x), Q(x), R(y), R(A), R(C)]",
				it.next().toString());
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
		mf_i_SentenceAtomic a1 = (mf_i_SentenceAtomic) parser.parse("P(y, F(A,y))");
		lits.add(new mf_Literal(a1));

		mf_Clause c1 = new mf_Clause(lits);

		lits.clear();
		a1 = (mf_i_SentenceAtomic) parser.parse("x = x");
		lits.add(new mf_Literal(a1));

		mf_Clause c2 = new mf_Clause(lits);

		Set<mf_Clause> paras = paramodulation.apply(c1, c2);
		Assert.assertEquals(0, paras.size());
	}

	@Test
	public void testNegativeTermEquality() {
		mf_Domain domain = new mf_Domain();
		domain.addConstant("A");
		domain.addConstant("B");
		domain.addConstant("C");
		domain.addPredicate("P");
		domain.addFunction("F");

		mf_Parser parser = new mf_Parser(domain);

		List<mf_Literal> lits = new ArrayList<mf_Literal>();
		mf_i_SentenceAtomic a1 = (mf_i_SentenceAtomic) parser.parse("P(y, F(A,y))");
		lits.add(new mf_Literal(a1));

		mf_Clause c1 = new mf_Clause(lits);

		lits.clear();
		a1 = (mf_i_SentenceAtomic) parser.parse("F(x,B) = x");
		lits.add(new mf_Literal(a1, true));

		mf_Clause c2 = new mf_Clause(lits);

		Set<mf_Clause> paras = paramodulation.apply(c1, c2);
		Assert.assertEquals(0, paras.size());
	}
}
