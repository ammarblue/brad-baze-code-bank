package reuze.test;
//package aima.test.core.unit.logic.fol.kb.data;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.software.reuze.mf_CNF;
import com.software.reuze.mf_CNFConverter;
import com.software.reuze.mf_Clause;
import com.software.reuze.mf_Domain;
import com.software.reuze.mf_DomainExamples;
import com.software.reuze.mf_KnowledgeBase;
import com.software.reuze.mf_Literal;
import com.software.reuze.mf_NodeTermFunction;
import com.software.reuze.mf_NodeTermVariable;
import com.software.reuze.mf_Parser;
import com.software.reuze.mf_Predicate;
import com.software.reuze.mf_StandardizeApartIndexicalFactory;
import com.software.reuze.mf_SymbolConstant;
import com.software.reuze.mf_i_NodeTerm;
import com.software.reuze.mf_i_Sentence;
import com.software.reuze.mf_i_SentenceAtomic;


/*import aima.core.logic.fol.CNFConverter;
import aima.core.logic.fol.StandardizeApartIndexicalFactory;
import aima.core.logic.fol.domain.DomainFactory;
import aima.core.logic.fol.domain.FOLDomain;
import aima.core.logic.fol.kb.FOLKnowledgeBase;
import aima.core.logic.fol.kb.data.CNF;
import aima.core.logic.fol.kb.data.Clause;
import aima.core.logic.fol.kb.data.Literal;
import aima.core.logic.fol.parsing.FOLParser;
import aima.core.logic.fol.parsing.ast.FOAtomicSentence;
import aima.core.logic.fol.parsing.ast.Constant;
import aima.core.logic.fol.parsing.ast.Function;
import aima.core.logic.fol.parsing.ast.Predicate;
import aima.core.logic.fol.parsing.ast.FOSentence;
import aima.core.logic.fol.parsing.ast.Term;
import aima.core.logic.fol.parsing.ast.Variable;*/

/**
 * @author Ciaran O'Reilly
 * 
 */
public class ClauseTest {

	@Before
	public void setUp() {
		mf_StandardizeApartIndexicalFactory.flush();
	}

	@Test
	public void testImmutable() {
		mf_Clause c = new mf_Clause();

		Assert.assertFalse(c.isImmutable());

		c.addNegativeLiteral(new mf_Predicate("Pred1", new ArrayList<mf_i_NodeTerm>()));
		c.addPositiveLiteral(new mf_Predicate("Pred2", new ArrayList<mf_i_NodeTerm>()));

		c.setImmutable();

		Assert.assertTrue(c.isImmutable());

		try {
			c.addNegativeLiteral(new mf_Predicate("Pred3", new ArrayList<mf_i_NodeTerm>()));

			Assert.fail("Should have thrown an IllegalStateException");
		} catch (IllegalStateException ise) {
			// Ok, Expected
		}

		try {
			c.addPositiveLiteral(new mf_Predicate("Pred3", new ArrayList<mf_i_NodeTerm>()));

			Assert.fail("Should have thrown an IllegalStateException");
		} catch (IllegalStateException ise) {
			// Ok, Expected
		}
	}

	@Test
	public void testIsEmpty() {
		mf_Clause c1 = new mf_Clause();
		Assert.assertTrue(c1.isEmpty());

		c1.addNegativeLiteral(new mf_Predicate("Pred1", new ArrayList<mf_i_NodeTerm>()));
		Assert.assertFalse(c1.isEmpty());

		mf_Clause c2 = new mf_Clause();
		Assert.assertTrue(c2.isEmpty());

		c2.addPositiveLiteral(new mf_Predicate("Pred1", new ArrayList<mf_i_NodeTerm>()));
		Assert.assertFalse(c2.isEmpty());

		mf_Clause c3 = new mf_Clause();
		Assert.assertTrue(c3.isEmpty());

		c3.addNegativeLiteral(new mf_Predicate("Pred1", new ArrayList<mf_i_NodeTerm>()));
		c3.addPositiveLiteral(new mf_Predicate("Pred1", new ArrayList<mf_i_NodeTerm>()));
		// Should be empty as they resolved with each other
		Assert.assertFalse(c3.isEmpty());

		c3.addNegativeLiteral(new mf_Predicate("Pred1", new ArrayList<mf_i_NodeTerm>()));
		c3.addPositiveLiteral(new mf_Predicate("Pred2", new ArrayList<mf_i_NodeTerm>()));
		Assert.assertFalse(c3.isEmpty());
	}

	@Test
	public void testIsHornClause() {
		mf_Clause c1 = new mf_Clause();
		Assert.assertFalse(c1.isHornClause());

		c1.addNegativeLiteral(new mf_Predicate("Pred1", new ArrayList<mf_i_NodeTerm>()));
		Assert.assertTrue(c1.isHornClause());

		c1.addPositiveLiteral(new mf_Predicate("Pred2", new ArrayList<mf_i_NodeTerm>()));
		Assert.assertTrue(c1.isHornClause());

		c1.addNegativeLiteral(new mf_Predicate("Pred3", new ArrayList<mf_i_NodeTerm>()));
		Assert.assertTrue(c1.isHornClause());
		c1.addNegativeLiteral(new mf_Predicate("Pred4", new ArrayList<mf_i_NodeTerm>()));
		Assert.assertTrue(c1.isHornClause());

		c1.addPositiveLiteral(new mf_Predicate("Pred5", new ArrayList<mf_i_NodeTerm>()));
		Assert.assertFalse(c1.isHornClause());
	}

	@Test
	public void testIsDefiniteClause() {
		mf_Clause c1 = new mf_Clause();
		Assert.assertFalse(c1.isDefiniteClause());

		c1.addNegativeLiteral(new mf_Predicate("Pred1", new ArrayList<mf_i_NodeTerm>()));
		Assert.assertFalse(c1.isDefiniteClause());

		c1.addPositiveLiteral(new mf_Predicate("Pred2", new ArrayList<mf_i_NodeTerm>()));
		Assert.assertTrue(c1.isDefiniteClause());

		c1.addNegativeLiteral(new mf_Predicate("Pred3", new ArrayList<mf_i_NodeTerm>()));
		Assert.assertTrue(c1.isDefiniteClause());
		c1.addNegativeLiteral(new mf_Predicate("Pred4", new ArrayList<mf_i_NodeTerm>()));
		Assert.assertTrue(c1.isDefiniteClause());

		c1.addPositiveLiteral(new mf_Predicate("Pred5", new ArrayList<mf_i_NodeTerm>()));
		Assert.assertFalse(c1.isDefiniteClause());
	}

	@Test
	public void testIsUnitClause() {
		mf_Clause c1 = new mf_Clause();
		Assert.assertFalse(c1.isUnitClause());

		c1.addPositiveLiteral(new mf_Predicate("Pred1", new ArrayList<mf_i_NodeTerm>()));
		Assert.assertTrue(c1.isUnitClause());

		c1.addPositiveLiteral(new mf_Predicate("Pred2", new ArrayList<mf_i_NodeTerm>()));
		Assert.assertFalse(c1.isUnitClause());

		c1 = new mf_Clause();
		Assert.assertFalse(c1.isUnitClause());

		c1.addPositiveLiteral(new mf_Predicate("Pred1", new ArrayList<mf_i_NodeTerm>()));
		Assert.assertTrue(c1.isUnitClause());

		c1.addNegativeLiteral(new mf_Predicate("Pred2", new ArrayList<mf_i_NodeTerm>()));
		Assert.assertFalse(c1.isUnitClause());

		c1 = new mf_Clause();
		Assert.assertFalse(c1.isUnitClause());

		c1.addNegativeLiteral(new mf_Predicate("Pred1", new ArrayList<mf_i_NodeTerm>()));
		Assert.assertTrue(c1.isUnitClause());

		c1.addPositiveLiteral(new mf_Predicate("Pred2", new ArrayList<mf_i_NodeTerm>()));
		Assert.assertFalse(c1.isUnitClause());
	}

	@Test
	public void testIsImplicationDefiniteClause() {
		mf_Clause c1 = new mf_Clause();
		Assert.assertFalse(c1.isImplicationDefiniteClause());

		c1.addPositiveLiteral(new mf_Predicate("Pred1", new ArrayList<mf_i_NodeTerm>()));
		Assert.assertFalse(c1.isImplicationDefiniteClause());

		c1.addNegativeLiteral(new mf_Predicate("Pred2", new ArrayList<mf_i_NodeTerm>()));
		Assert.assertTrue(c1.isImplicationDefiniteClause());
		c1.addNegativeLiteral(new mf_Predicate("Pred3", new ArrayList<mf_i_NodeTerm>()));
		Assert.assertTrue(c1.isImplicationDefiniteClause());

		c1.addPositiveLiteral(new mf_Predicate("Pred4", new ArrayList<mf_i_NodeTerm>()));
		Assert.assertFalse(c1.isImplicationDefiniteClause());
	}

	@Test
	public void testBinaryResolvents() {
		mf_Domain domain = new mf_Domain();
		domain.addPredicate("Pred1");
		domain.addPredicate("Pred2");
		domain.addPredicate("Pred3");
		domain.addPredicate("Pred4");

		mf_Clause c1 = new mf_Clause();

		// Ensure that resolving to self when empty returns an empty clause
		Assert.assertNotNull(c1.binaryResolvents(c1));
		Assert.assertEquals(1, c1.binaryResolvents(c1).size());
		Assert.assertTrue(c1.binaryResolvents(c1).iterator().next().isEmpty());

		// Check if resolve with self to an empty clause
		c1.addPositiveLiteral(new mf_Predicate("Pred1", new ArrayList<mf_i_NodeTerm>()));
		c1.addNegativeLiteral(new mf_Predicate("Pred1", new ArrayList<mf_i_NodeTerm>()));
		Assert.assertNotNull(c1.binaryResolvents(c1));
		Assert.assertEquals(1, c1.binaryResolvents(c1).size());
		// i.e. resolving a tautology with a tautology gives you
		// back a tautology.
		Assert.assertEquals("[~Pred1(), Pred1()]", c1.binaryResolvents(c1)
				.iterator().next().toString());

		// Check if try to resolve with self and no resolvents
		c1 = new mf_Clause();
		c1.addPositiveLiteral(new mf_Predicate("Pred1", new ArrayList<mf_i_NodeTerm>()));
		Assert.assertEquals(0, c1.binaryResolvents(c1).size());

		c1 = new mf_Clause();
		mf_Clause c2 = new mf_Clause();
		// Ensure that two empty clauses resolve to an empty clause
		Assert.assertNotNull(c1.binaryResolvents(c2));
		Assert.assertEquals(1, c1.binaryResolvents(c2).size());
		Assert.assertTrue(c1.binaryResolvents(c2).iterator().next().isEmpty());
		Assert.assertNotNull(c2.binaryResolvents(c1));
		Assert.assertEquals(1, c2.binaryResolvents(c1).size());
		Assert.assertTrue(c2.binaryResolvents(c1).iterator().next().isEmpty());

		// Enusre the two complementary clauses resolve
		// to the empty clause
		c1.addPositiveLiteral(new mf_Predicate("Pred1", new ArrayList<mf_i_NodeTerm>()));
		c2.addNegativeLiteral(new mf_Predicate("Pred1", new ArrayList<mf_i_NodeTerm>()));
		Assert.assertNotNull(c1.binaryResolvents(c2));
		Assert.assertEquals(1, c1.binaryResolvents(c2).size());
		Assert.assertTrue(c1.binaryResolvents(c2).iterator().next().isEmpty());
		Assert.assertNotNull(c2.binaryResolvents(c1));
		Assert.assertEquals(1, c2.binaryResolvents(c1).size());
		Assert.assertTrue(c2.binaryResolvents(c1).iterator().next().isEmpty());

		// Ensure that two clauses that have two complementaries
		// resolve with two resolvents
		c1.addPositiveLiteral(new mf_Predicate("Pred1", new ArrayList<mf_i_NodeTerm>()));
		c2.addNegativeLiteral(new mf_Predicate("Pred1", new ArrayList<mf_i_NodeTerm>()));
		c1.addPositiveLiteral(new mf_Predicate("Pred2", new ArrayList<mf_i_NodeTerm>()));
		c2.addNegativeLiteral(new mf_Predicate("Pred2", new ArrayList<mf_i_NodeTerm>()));
		Assert.assertNotNull(c1.binaryResolvents(c2));
		Assert.assertEquals(2, c1.binaryResolvents(c2).size());
		Assert.assertNotNull(c2.binaryResolvents(c1));
		Assert.assertEquals(2, c2.binaryResolvents(c1).size());

		// Ensure two clauses that factor are not
		// considered resolved
		c1 = new mf_Clause();
		c2 = new mf_Clause();
		c1.addPositiveLiteral(new mf_Predicate("Pred1", new ArrayList<mf_i_NodeTerm>()));
		c1.addPositiveLiteral(new mf_Predicate("Pred2", new ArrayList<mf_i_NodeTerm>()));
		c1.addNegativeLiteral(new mf_Predicate("Pred3", new ArrayList<mf_i_NodeTerm>()));
		c1.addNegativeLiteral(new mf_Predicate("Pred4", new ArrayList<mf_i_NodeTerm>()));
		c2.addPositiveLiteral(new mf_Predicate("Pred2", new ArrayList<mf_i_NodeTerm>()));
		c2.addNegativeLiteral(new mf_Predicate("Pred4", new ArrayList<mf_i_NodeTerm>()));
		Assert.assertNotNull(c1.binaryResolvents(c2));
		Assert.assertEquals(0, c1.binaryResolvents(c2).size());
		Assert.assertNotNull(c2.binaryResolvents(c1));
		Assert.assertEquals(0, c2.binaryResolvents(c1).size());

		// Ensure the resolvent is a subset of the originals
		c1 = new mf_Clause();
		c2 = new mf_Clause();
		c1.addPositiveLiteral(new mf_Predicate("Pred1", new ArrayList<mf_i_NodeTerm>()));
		c1.addNegativeLiteral(new mf_Predicate("Pred2", new ArrayList<mf_i_NodeTerm>()));
		c1.addNegativeLiteral(new mf_Predicate("Pred3", new ArrayList<mf_i_NodeTerm>()));
		c2.addPositiveLiteral(new mf_Predicate("Pred2", new ArrayList<mf_i_NodeTerm>()));
		Assert.assertNotNull(c1.binaryResolvents(c2));
		Assert.assertNotNull(c2.binaryResolvents(c1));
		Assert.assertEquals(1, c1.binaryResolvents(c2).iterator().next()
				.getNumberPositiveLiterals());
		Assert.assertEquals(1, c1.binaryResolvents(c2).iterator().next()
				.getNumberNegativeLiterals());
		Assert.assertEquals(1, c2.binaryResolvents(c1).iterator().next()
				.getNumberPositiveLiterals());
		Assert.assertEquals(1, c2.binaryResolvents(c1).iterator().next()
				.getNumberNegativeLiterals());
	}

	@Test
	public void testBinaryResolventsOrderDoesNotMatter() {
		// This is a regression test, to ensure
		// the ordering of resolvents does not matter.
		// If the order ends up mattering, then likely
		// a problem was introduced in the Clause class
		// unifier, or related class.

		// Set up the initial set of clauses based on the
		// loves animal domain as it contains functions
		// new clauses will always be created (i.e. is an
		// infinite universe of discourse).
		mf_KnowledgeBase kb = new mf_KnowledgeBase(
				mf_DomainExamples.lovesAnimalDomain());

		kb.tell("FORALL x (FORALL y (Animal(y) => Loves(x, y)) => EXISTS y Loves(y, x))");
		kb.tell("FORALL x (EXISTS y (Animal(y) AND Kills(x, y)) => FORALL z NOT(Loves(z, x)))");
		kb.tell("FORALL x (Animal(x) => Loves(Jack, x))");
		kb.tell("(Kills(Jack, Tuna) OR Kills(Curiosity, Tuna))");
		kb.tell("Cat(Tuna)");
		kb.tell("FORALL x (Cat(x) => Animal(x))");

		Set<mf_Clause> clauses = new LinkedHashSet<mf_Clause>();
		clauses.addAll(kb.getAllClauses());

		Set<mf_Clause> newClauses = new LinkedHashSet<mf_Clause>();
		long maxRunTime = 30 * 1000; // 30 seconds
		long finishTime = System.currentTimeMillis() + maxRunTime;
		do {
			clauses.addAll(newClauses);
			newClauses.clear();
			mf_Clause[] clausesA = new mf_Clause[clauses.size()];
			clauses.toArray(clausesA);
			for (int i = 0; i < clausesA.length; i++) {
				mf_Clause cI = clausesA[i];
				for (int j = 0; j < clausesA.length; j++) {
					mf_Clause cJ = clausesA[j];

					newClauses.addAll(cI.getFactors());
					newClauses.addAll(cJ.getFactors());

					Set<mf_Clause> cIresolvents = cI.binaryResolvents(cJ);
					Set<mf_Clause> cJresolvents = cJ.binaryResolvents(cI);
					if (!cIresolvents.equals(cJresolvents)) {
						System.err.println("cI=" + cI);
						System.err.println("cJ=" + cJ);
						System.err.println("cIR=" + cIresolvents);
						System.err.println("cJR=" + cJresolvents);
						Assert.fail("Ordering of binary resolvents has become important, which should not be the case");
					}

					for (mf_Clause r : cIresolvents) {
						newClauses.addAll(r.getFactors());
					}

					if (System.currentTimeMillis() > finishTime) {
						break;
					}
				}
				if (System.currentTimeMillis() > finishTime) {
					break;
				}
			}
		} while (System.currentTimeMillis() < finishTime);
	}

	@Test
	public void testEqualityBinaryResolvents() {
		mf_Domain domain = new mf_Domain();
		domain.addConstant("A");
		domain.addConstant("B");

		mf_Parser parser = new mf_Parser(domain);

		// B = A
		mf_Clause c1 = new mf_Clause();
		c1.addPositiveLiteral((mf_i_SentenceAtomic) parser.parse("B = A"));

		mf_Clause c2 = new mf_Clause();
		c2.addNegativeLiteral((mf_i_SentenceAtomic) parser.parse("B = A"));
		c2.addPositiveLiteral((mf_i_SentenceAtomic) parser.parse("B = A"));

		Set<mf_Clause> resolvents = c1.binaryResolvents(c2);

		Assert.assertEquals(1, resolvents.size());
		Assert.assertEquals("[[B = A]]", resolvents.toString());
	}

	@Test
	public void testHashCode() {
		mf_i_NodeTerm cons1 = new mf_SymbolConstant("C1");
		mf_i_NodeTerm cons2 = new mf_SymbolConstant("C2");
		mf_i_NodeTerm var1 = new mf_NodeTermVariable("v1");
		List<mf_i_NodeTerm> pts1 = new ArrayList<mf_i_NodeTerm>();
		List<mf_i_NodeTerm> pts2 = new ArrayList<mf_i_NodeTerm>();
		pts1.add(cons1);
		pts1.add(cons2);
		pts1.add(var1);
		pts2.add(cons2);
		pts2.add(cons1);
		pts2.add(var1);

		mf_Clause c1 = new mf_Clause();
		mf_Clause c2 = new mf_Clause();
		Assert.assertEquals(c1.hashCode(), c2.hashCode());

		c1.addNegativeLiteral(new mf_Predicate("Pred1", pts1));
		Assert.assertNotSame(c1.hashCode(), c2.hashCode());
		c2.addNegativeLiteral(new mf_Predicate("Pred1", pts1));
		Assert.assertEquals(c1.hashCode(), c2.hashCode());

		c1.addPositiveLiteral(new mf_Predicate("Pred1", pts1));
		Assert.assertNotSame(c1.hashCode(), c2.hashCode());
		c2.addPositiveLiteral(new mf_Predicate("Pred1", pts1));
		Assert.assertEquals(c1.hashCode(), c2.hashCode());
	}

	@Test
	public void testSimpleEquals() {
		mf_i_NodeTerm cons1 = new mf_SymbolConstant("C1");
		mf_i_NodeTerm cons2 = new mf_SymbolConstant("C2");
		mf_i_NodeTerm var1 = new mf_NodeTermVariable("v1");
		List<mf_i_NodeTerm> pts1 = new ArrayList<mf_i_NodeTerm>();
		List<mf_i_NodeTerm> pts2 = new ArrayList<mf_i_NodeTerm>();
		pts1.add(cons1);
		pts1.add(cons2);
		pts1.add(var1);
		pts2.add(cons2);
		pts2.add(cons1);
		pts2.add(var1);

		mf_Clause c1 = new mf_Clause();
		mf_Clause c2 = new mf_Clause();
		Assert.assertTrue(c1.equals(c1));
		Assert.assertTrue(c2.equals(c2));
		Assert.assertTrue(c1.equals(c2));
		Assert.assertTrue(c2.equals(c1));

		// Check negatives
		c1.addNegativeLiteral(new mf_Predicate("Pred1", pts1));
		Assert.assertFalse(c1.equals(c2));
		Assert.assertFalse(c2.equals(c1));
		c2.addNegativeLiteral(new mf_Predicate("Pred1", pts1));
		Assert.assertTrue(c1.equals(c2));
		Assert.assertTrue(c2.equals(c1));

		c1.addNegativeLiteral(new mf_Predicate("Pred2", pts2));
		Assert.assertFalse(c1.equals(c2));
		Assert.assertFalse(c2.equals(c1));
		c2.addNegativeLiteral(new mf_Predicate("Pred2", pts2));
		Assert.assertTrue(c1.equals(c2));
		Assert.assertTrue(c2.equals(c1));
		// Check same but added in different order
		c1.addNegativeLiteral(new mf_Predicate("Pred3", pts1));
		Assert.assertFalse(c1.equals(c2));
		Assert.assertFalse(c2.equals(c1));
		c1.addNegativeLiteral(new mf_Predicate("Pred4", pts1));
		Assert.assertFalse(c1.equals(c2));
		Assert.assertFalse(c2.equals(c1));
		c2.addNegativeLiteral(new mf_Predicate("Pred4", pts1));
		Assert.assertFalse(c1.equals(c2));
		Assert.assertFalse(c2.equals(c1));
		c2.addNegativeLiteral(new mf_Predicate("Pred3", pts1));
		Assert.assertTrue(c1.equals(c2));
		Assert.assertTrue(c2.equals(c1));

		// Check positives
		c1.addPositiveLiteral(new mf_Predicate("Pred1", pts1));
		Assert.assertFalse(c1.equals(c2));
		Assert.assertFalse(c2.equals(c1));
		c2.addPositiveLiteral(new mf_Predicate("Pred1", pts1));
		Assert.assertTrue(c1.equals(c2));
		Assert.assertTrue(c2.equals(c1));

		c1.addPositiveLiteral(new mf_Predicate("Pred2", pts2));
		Assert.assertFalse(c1.equals(c2));
		Assert.assertFalse(c2.equals(c1));
		c2.addPositiveLiteral(new mf_Predicate("Pred2", pts2));
		Assert.assertTrue(c1.equals(c2));
		Assert.assertTrue(c2.equals(c1));
		// Check same but added in different order
		c1.addPositiveLiteral(new mf_Predicate("Pred3", pts1));
		Assert.assertFalse(c1.equals(c2));
		Assert.assertFalse(c2.equals(c1));
		c1.addPositiveLiteral(new mf_Predicate("Pred4", pts1));
		Assert.assertFalse(c1.equals(c2));
		Assert.assertFalse(c2.equals(c1));
		c2.addPositiveLiteral(new mf_Predicate("Pred4", pts1));
		Assert.assertFalse(c1.equals(c2));
		Assert.assertFalse(c2.equals(c1));
		c2.addPositiveLiteral(new mf_Predicate("Pred3", pts1));
		Assert.assertTrue(c1.equals(c2));
		Assert.assertTrue(c2.equals(c1));
	}

	@Test
	public void testComplexEquals() {
		mf_Domain domain = new mf_Domain();
		domain.addConstant("A");
		domain.addConstant("B");
		domain.addConstant("C");
		domain.addConstant("D");
		domain.addPredicate("P");
		domain.addPredicate("Animal");
		domain.addPredicate("Kills");
		domain.addFunction("F");
		domain.addFunction("SF0");

		mf_Parser parser = new mf_Parser(domain);

		mf_CNFConverter cnfConverter = new mf_CNFConverter(parser);
		mf_i_Sentence s1 = parser.parse("((x1 = y1 AND y1 = z1) => x1 = z1)");
		mf_i_Sentence s2 = parser.parse("((x2 = y2 AND F(y2) = z2) => F(x2) = z2)");
		mf_CNF cnf1 = cnfConverter.convertToCNF(s1);
		mf_CNF cnf2 = cnfConverter.convertToCNF(s2);

		mf_Clause c1 = cnf1.getConjunctionOfClauses().get(0);
		mf_Clause c2 = cnf2.getConjunctionOfClauses().get(0);

		Assert.assertFalse(c1.equals(c2));

		s1 = parser.parse("((x1 = y1 AND y1 = z1) => x1 = z1)");
		s2 = parser.parse("((x2 = y2 AND y2 = z2) => x2 = z2)");
		cnf1 = cnfConverter.convertToCNF(s1);
		cnf2 = cnfConverter.convertToCNF(s2);

		c1 = cnf1.getConjunctionOfClauses().get(0);
		c2 = cnf2.getConjunctionOfClauses().get(0);

		Assert.assertTrue(c1.equals(c2));

		s1 = parser.parse("((x1 = y1 AND y1 = z1) => x1 = z1)");
		s2 = parser.parse("((y2 = z2 AND x2 = y2) => x2 = z2)");
		cnf1 = cnfConverter.convertToCNF(s1);
		cnf2 = cnfConverter.convertToCNF(s2);

		c1 = cnf1.getConjunctionOfClauses().get(0);
		c2 = cnf2.getConjunctionOfClauses().get(0);

		Assert.assertTrue(c1.equals(c2));

		s1 = parser.parse("(((x1 = y1 AND y1 = z1) AND z1 = r1) => x1 = r1)");
		s2 = parser.parse("(((x2 = y2 AND y2 = z2) AND z2 = r2) => x2 = r2)");
		cnf1 = cnfConverter.convertToCNF(s1);
		cnf2 = cnfConverter.convertToCNF(s2);

		c1 = cnf1.getConjunctionOfClauses().get(0);
		c2 = cnf2.getConjunctionOfClauses().get(0);

		Assert.assertTrue(c1.equals(c2));

		s1 = parser.parse("(((x1 = y1 AND y1 = z1) AND z1 = r1) => x1 = r1)");
		s2 = parser.parse("(((z2 = r2 AND y2 = z2) AND x2 = y2) => x2 = r2)");
		cnf1 = cnfConverter.convertToCNF(s1);
		cnf2 = cnfConverter.convertToCNF(s2);

		c1 = cnf1.getConjunctionOfClauses().get(0);
		c2 = cnf2.getConjunctionOfClauses().get(0);

		Assert.assertTrue(c1.equals(c2));

		s1 = parser.parse("(((x1 = y1 AND y1 = z1) AND z1 = r1) => x1 = r1)");
		s2 = parser.parse("(((x2 = y2 AND y2 = z2) AND z2 = y2) => x2 = r2)");
		cnf1 = cnfConverter.convertToCNF(s1);
		cnf2 = cnfConverter.convertToCNF(s2);

		c1 = cnf1.getConjunctionOfClauses().get(0);
		c2 = cnf2.getConjunctionOfClauses().get(0);

		Assert.assertFalse(c1.equals(c2));

		s1 = parser
				.parse("(((((x1 = y1 AND y1 = z1) AND z1 = r1) AND r1 = q1) AND q1 = s1) => x1 = r1)");
		s2 = parser
				.parse("(((((x2 = y2 AND y2 = z2) AND z2 = r2) AND r2 = q2) AND q2 = s2) => x2 = r2)");
		cnf1 = cnfConverter.convertToCNF(s1);
		cnf2 = cnfConverter.convertToCNF(s2);

		c1 = cnf1.getConjunctionOfClauses().get(0);
		c2 = cnf2.getConjunctionOfClauses().get(0);

		Assert.assertTrue(c1.equals(c2));

		s1 = parser
				.parse("((((NOT(Animal(c1920)) OR NOT(Animal(c1921))) OR NOT(Kills(c1922,c1920))) OR NOT(Kills(c1919,c1921))) OR NOT(Kills(SF0(c1922),SF0(c1919))))");
		s2 = parser
				.parse("((((NOT(Animal(c1929)) OR NOT(Animal(c1928))) OR NOT(Kills(c1927,c1929))) OR NOT(Kills(c1930,c1928))) OR NOT(Kills(SF0(c1930),SF0(c1927))))");
		cnf1 = cnfConverter.convertToCNF(s1);
		cnf2 = cnfConverter.convertToCNF(s2);

		c1 = cnf1.getConjunctionOfClauses().get(0);
		c2 = cnf2.getConjunctionOfClauses().get(0);

		Assert.assertTrue(c1.equals(c2));
	}

	@Test
	public void testNonTrivialFactors() {
		mf_Domain domain = new mf_Domain();
		domain.addConstant("A");
		domain.addConstant("B");
		domain.addFunction("F");
		domain.addFunction("G");
		domain.addFunction("H");
		domain.addPredicate("P");
		domain.addPredicate("Q");

		mf_Parser parser = new mf_Parser(domain);

		// p(x,y), q(a,b), p(b,a), q(y,x)
		mf_Clause c = new mf_Clause();
		c.addPositiveLiteral((mf_Predicate) parser.parse("P(x,y)"));
		c.addPositiveLiteral((mf_Predicate) parser.parse("Q(A,B)"));
		c.addNegativeLiteral((mf_Predicate) parser.parse("P(B,A)"));
		c.addPositiveLiteral((mf_Predicate) parser.parse("Q(y,x)"));

		Assert.assertEquals("[[~P(B,A), P(B,A), Q(A,B)]]", c
				.getNonTrivialFactors().toString());

		// p(x,y), q(a,b), p(b,a), q(y,x)
		c = new mf_Clause();
		c.addPositiveLiteral((mf_Predicate) parser.parse("P(x,y)"));
		c.addPositiveLiteral((mf_Predicate) parser.parse("Q(A,B)"));
		c.addNegativeLiteral((mf_Predicate) parser.parse("P(B,A)"));
		c.addNegativeLiteral((mf_Predicate) parser.parse("Q(y,x)"));

		Assert.assertEquals("[]", c.getNonTrivialFactors().toString());

		// p(x,f(y)), p(g(u),x), p(f(y),u)
		c = new mf_Clause();
		c.addPositiveLiteral((mf_Predicate) parser.parse("P(x,F(y))"));
		c.addPositiveLiteral((mf_Predicate) parser.parse("P(G(u),x)"));
		c.addPositiveLiteral((mf_Predicate) parser.parse("P(F(y),u)"));

		// Should be: [{P(F(c#),F(c#)),P(G(F(c#)),F(c#))}]
		c = c.getNonTrivialFactors().iterator().next();
		mf_Literal p = c.getPositiveLiterals().get(0);
		Assert.assertEquals("P", p.getAtomicSentence().getSymbolicName());
		mf_NodeTermFunction f = (mf_NodeTermFunction) p.getAtomicSentence().getArgs().get(0);
		Assert.assertEquals("F", f.getFunctionName());
		mf_NodeTermVariable v = (mf_NodeTermVariable) f.getTerms().get(0);
		f = (mf_NodeTermFunction) p.getAtomicSentence().getArgs().get(1);
		Assert.assertEquals("F", f.getFunctionName());
		Assert.assertEquals(v, f.getTerms().get(0));

		//
		p = c.getPositiveLiterals().get(1);
		f = (mf_NodeTermFunction) p.getAtomicSentence().getArgs().get(0);
		Assert.assertEquals("G", f.getFunctionName());
		f = (mf_NodeTermFunction) f.getTerms().get(0);
		Assert.assertEquals("F", f.getFunctionName());
		Assert.assertEquals(v, f.getTerms().get(0));
		f = (mf_NodeTermFunction) p.getAtomicSentence().getArgs().get(1);
		Assert.assertEquals("F", f.getFunctionName());
		Assert.assertEquals(v, f.getTerms().get(0));

		// p(g(x)), q(x), p(f(a)), p(x), p(g(f(x))), q(f(a))
		c = new mf_Clause();
		c.addPositiveLiteral((mf_Predicate) parser.parse("P(G(x))"));
		c.addPositiveLiteral((mf_Predicate) parser.parse("Q(x)"));
		c.addPositiveLiteral((mf_Predicate) parser.parse("P(F(A))"));
		c.addPositiveLiteral((mf_Predicate) parser.parse("P(x)"));
		c.addPositiveLiteral((mf_Predicate) parser.parse("P(G(F(x)))"));
		c.addPositiveLiteral((mf_Predicate) parser.parse("Q(F(A))"));

		Assert.assertEquals("[[P(F(A)), P(G(F(F(A)))), P(G(F(A))), Q(F(A))]]",
				c.getNonTrivialFactors().toString());
	}

	// Note: Tests derived from:
	// http://logic.stanford.edu/classes/cs157/2008/notes/chap09.pdf
	// page 16.
	@Test
	public void testIsTautology() {
		mf_Domain domain = new mf_Domain();
		domain.addConstant("A");
		domain.addPredicate("P");
		domain.addPredicate("Q");
		domain.addPredicate("R");
		domain.addFunction("F");

		mf_Parser parser = new mf_Parser(domain);

		// {p(f(a)),~p(f(a))}
		mf_Clause c = new mf_Clause();
		c.addPositiveLiteral((mf_Predicate) parser.parse("P(F(A))"));
		Assert.assertFalse(c.isTautology());
		c.addNegativeLiteral((mf_Predicate) parser.parse("P(F(A))"));
		Assert.assertTrue(c.isTautology());

		// {p(x),q(y),~q(y),r(z)}
		c = new mf_Clause();
		c.addPositiveLiteral((mf_Predicate) parser.parse("P(x)"));
		Assert.assertFalse(c.isTautology());
		c.addPositiveLiteral((mf_Predicate) parser.parse("Q(y)"));
		Assert.assertFalse(c.isTautology());
		c.addNegativeLiteral((mf_Predicate) parser.parse("Q(y)"));
		Assert.assertTrue(c.isTautology());
		c.addPositiveLiteral((mf_Predicate) parser.parse("R(z)"));
		Assert.assertTrue(c.isTautology());

		// {~p(a),p(x)}
		c = new mf_Clause();
		c.addNegativeLiteral((mf_Predicate) parser.parse("P(A)"));
		Assert.assertFalse(c.isTautology());
		c.addPositiveLiteral((mf_Predicate) parser.parse("P(x)"));
		Assert.assertFalse(c.isTautology());
	}

	// Note: Tests derived from:
	// http://logic.stanford.edu/classes/cs157/2008/lectures/lecture12.pdf
	// slides 17 and 18.
	@Test
	public void testSubsumes() {
		mf_Domain domain = new mf_Domain();
		domain.addConstant("A");
		domain.addConstant("B");
		domain.addConstant("C");
		domain.addConstant("D");
		domain.addConstant("E");
		domain.addConstant("F");
		domain.addConstant("G");
		domain.addConstant("H");
		domain.addConstant("I");
		domain.addConstant("J");
		domain.addPredicate("P");
		domain.addPredicate("Q");

		mf_Parser parser = new mf_Parser(domain);

		// Example
		// {~p(a,b),q(c)}
		mf_Clause psi = new mf_Clause();
		psi.addNegativeLiteral((mf_Predicate) parser.parse("P(A,B)"));
		psi.addPositiveLiteral((mf_Predicate) parser.parse("Q(C)"));
		// {~p(x,y)}
		mf_Clause phi = new mf_Clause();
		phi.addNegativeLiteral((mf_Predicate) parser.parse("P(x,y)"));

		Assert.assertTrue(phi.subsumes(psi));
		// Non-Example
		// {~p(x,b),q(x)}
		psi = new mf_Clause();
		psi.addNegativeLiteral((mf_Predicate) parser.parse("P(x,B)"));
		psi.addPositiveLiteral((mf_Predicate) parser.parse("Q(x)"));
		// {~p(a,y)}
		phi = new mf_Clause();
		phi.addNegativeLiteral((mf_Predicate) parser.parse("P(A,y)"));
		// Reason for Non-Example:
		// {p(b,b)}
		// {~q(b)}
		Assert.assertFalse(phi.subsumes(psi));

		//
		// Additional Examples

		// Non-Example
		// {~p(x,b),q(z)}
		psi = new mf_Clause();
		psi.addNegativeLiteral((mf_Predicate) parser.parse("P(x,B)"));
		psi.addPositiveLiteral((mf_Predicate) parser.parse("Q(z)"));
		// {~p(a,y)}
		phi = new mf_Clause();
		phi.addNegativeLiteral((mf_Predicate) parser.parse("P(A,y)"));

		Assert.assertFalse(phi.subsumes(psi));

		// Example
		// {~p(a,b),~p(w,z),q(c)}
		psi = new mf_Clause();
		psi.addNegativeLiteral((mf_Predicate) parser.parse("P(A,B)"));
		psi.addNegativeLiteral((mf_Predicate) parser.parse("P(w,z)"));
		psi.addPositiveLiteral((mf_Predicate) parser.parse("Q(C)"));
		// {~p(x,y),~p(a,b)}
		phi = new mf_Clause();
		phi.addNegativeLiteral((mf_Predicate) parser.parse("P(x,y)"));
		phi.addNegativeLiteral((mf_Predicate) parser.parse("P(A,B)"));

		Assert.assertTrue(phi.subsumes(psi));

		// Non-Example
		// {~p(v,b),~p(w,z),q(c)}
		psi = new mf_Clause();
		psi.addNegativeLiteral((mf_Predicate) parser.parse("P(v,B)"));
		psi.addNegativeLiteral((mf_Predicate) parser.parse("P(w,z)"));
		psi.addPositiveLiteral((mf_Predicate) parser.parse("Q(C)"));
		// {~p(x,y),~p(a,b)}
		phi = new mf_Clause();
		phi.addNegativeLiteral((mf_Predicate) parser.parse("P(x,y)"));
		phi.addNegativeLiteral((mf_Predicate) parser.parse("P(A,B)"));

		Assert.assertFalse(phi.subsumes(psi));

		// Example
		// {~p(a,b),~p(c,d),~p(e,f),~p(g,h),~p(i,j),q(c)}
		psi = new mf_Clause();
		psi.addNegativeLiteral((mf_Predicate) parser.parse("P(A,B)"));
		psi.addNegativeLiteral((mf_Predicate) parser.parse("P(C,D)"));
		psi.addNegativeLiteral((mf_Predicate) parser.parse("P(E,F)"));
		psi.addNegativeLiteral((mf_Predicate) parser.parse("P(G,H)"));
		psi.addNegativeLiteral((mf_Predicate) parser.parse("P(I,J)"));
		psi.addPositiveLiteral((mf_Predicate) parser.parse("Q(C)"));
		// {~p(i,j)}
		phi = new mf_Clause();
		phi.addNegativeLiteral((mf_Predicate) parser.parse("P(I,J)"));

		Assert.assertTrue(phi.subsumes(psi));

		// Example
		// {~p(a,b),~p(c,d),~p(e,f),q(c)}
		psi = new mf_Clause();
		psi.addNegativeLiteral((mf_Predicate) parser.parse("P(A,B)"));
		psi.addNegativeLiteral((mf_Predicate) parser.parse("P(C,D)"));
		psi.addNegativeLiteral((mf_Predicate) parser.parse("P(E,F)"));
		psi.addPositiveLiteral((mf_Predicate) parser.parse("Q(C)"));
		// {~p(e,f),~p(a,b),~p(c,d)}
		phi = new mf_Clause();
		phi.addNegativeLiteral((mf_Predicate) parser.parse("P(E,F)"));
		phi.addNegativeLiteral((mf_Predicate) parser.parse("P(A,B)"));
		phi.addNegativeLiteral((mf_Predicate) parser.parse("P(C,D)"));

		Assert.assertTrue(phi.subsumes(psi));

		// Example
		// {~p(a,b),~p(c,d),~p(e,f),~p(g,h),~p(i,j),q(c)}
		psi = new mf_Clause();
		psi.addNegativeLiteral((mf_Predicate) parser.parse("P(A,B)"));
		psi.addNegativeLiteral((mf_Predicate) parser.parse("P(C,D)"));
		psi.addNegativeLiteral((mf_Predicate) parser.parse("P(E,F)"));
		psi.addNegativeLiteral((mf_Predicate) parser.parse("P(G,H)"));
		psi.addNegativeLiteral((mf_Predicate) parser.parse("P(I,J)"));
		psi.addPositiveLiteral((mf_Predicate) parser.parse("Q(C)"));
		// {~p(i,j),~p(c,d)}
		phi = new mf_Clause();
		phi.addNegativeLiteral((mf_Predicate) parser.parse("P(I,J)"));
		phi.addNegativeLiteral((mf_Predicate) parser.parse("P(C,D)"));

		Assert.assertTrue(phi.subsumes(psi));

		// Non-Example
		// {~p(a,b),~p(x,d),~p(e,f),~p(g,h),~p(i,j),q(c)}
		psi = new mf_Clause();
		psi.addNegativeLiteral((mf_Predicate) parser.parse("P(A,B)"));
		psi.addNegativeLiteral((mf_Predicate) parser.parse("P(x,D)"));
		psi.addNegativeLiteral((mf_Predicate) parser.parse("P(E,F)"));
		psi.addNegativeLiteral((mf_Predicate) parser.parse("P(G,H)"));
		psi.addNegativeLiteral((mf_Predicate) parser.parse("P(I,J)"));
		psi.addPositiveLiteral((mf_Predicate) parser.parse("Q(C)"));
		// {~p(i,j),~p(c,d)}
		phi = new mf_Clause();
		phi.addNegativeLiteral((mf_Predicate) parser.parse("P(I,J)"));
		phi.addNegativeLiteral((mf_Predicate) parser.parse("P(C,D)"));

		Assert.assertFalse(phi.subsumes(psi));

		// Example
		// {~p(a,b),~p(c,d),~p(e,f),~p(g,h),~p(i,j),q(c)}
		psi = new mf_Clause();
		psi.addNegativeLiteral((mf_Predicate) parser.parse("P(A,B)"));
		psi.addNegativeLiteral((mf_Predicate) parser.parse("P(C,D)"));
		psi.addNegativeLiteral((mf_Predicate) parser.parse("P(E,F)"));
		psi.addNegativeLiteral((mf_Predicate) parser.parse("P(G,H)"));
		psi.addNegativeLiteral((mf_Predicate) parser.parse("P(I,J)"));
		psi.addPositiveLiteral((mf_Predicate) parser.parse("Q(C)"));
		// {~p(i,j),~p(a,x)}
		phi = new mf_Clause();
		phi.addNegativeLiteral((mf_Predicate) parser.parse("P(I,J)"));
		phi.addNegativeLiteral((mf_Predicate) parser.parse("P(A,x)"));

		Assert.assertTrue(phi.subsumes(psi));

		// Example
		// {~p(a,b),~p(c,d),~p(e,f),~p(g,h),~p(i,j),q(a,b),q(c,d),q(e,f)}
		psi = new mf_Clause();
		psi.addNegativeLiteral((mf_Predicate) parser.parse("P(A,B)"));
		psi.addNegativeLiteral((mf_Predicate) parser.parse("P(C,D)"));
		psi.addNegativeLiteral((mf_Predicate) parser.parse("P(E,F)"));
		psi.addNegativeLiteral((mf_Predicate) parser.parse("P(G,H)"));
		psi.addNegativeLiteral((mf_Predicate) parser.parse("P(I,J)"));
		psi.addPositiveLiteral((mf_Predicate) parser.parse("Q(A,B)"));
		psi.addPositiveLiteral((mf_Predicate) parser.parse("Q(C,D)"));
		psi.addPositiveLiteral((mf_Predicate) parser.parse("Q(E,F)"));
		// {~p(i,j),~p(a,b),q(e,f),q(a,b)}
		phi = new mf_Clause();
		phi.addNegativeLiteral((mf_Predicate) parser.parse("P(I,J)"));
		phi.addNegativeLiteral((mf_Predicate) parser.parse("P(A,B)"));
		phi.addPositiveLiteral((mf_Predicate) parser.parse("Q(E,F)"));
		phi.addPositiveLiteral((mf_Predicate) parser.parse("Q(A,B)"));

		Assert.assertTrue(phi.subsumes(psi));

		// Non-Example
		// {~p(a,b),~p(c,d),~p(e,f),~p(g,h),~p(i,j),q(a,b),q(c,d),q(e,f)}
		psi = new mf_Clause();
		psi.addNegativeLiteral((mf_Predicate) parser.parse("P(A,B)"));
		psi.addNegativeLiteral((mf_Predicate) parser.parse("P(C,D)"));
		psi.addNegativeLiteral((mf_Predicate) parser.parse("P(E,F)"));
		psi.addNegativeLiteral((mf_Predicate) parser.parse("P(G,H)"));
		psi.addNegativeLiteral((mf_Predicate) parser.parse("P(I,J)"));
		psi.addPositiveLiteral((mf_Predicate) parser.parse("Q(A,B)"));
		psi.addPositiveLiteral((mf_Predicate) parser.parse("Q(C,D)"));
		psi.addPositiveLiteral((mf_Predicate) parser.parse("Q(E,F)"));
		// {~p(i,j),~p(a,b),q(e,f),q(a,b)}
		phi = new mf_Clause();
		phi.addNegativeLiteral((mf_Predicate) parser.parse("P(I,J)"));
		phi.addNegativeLiteral((mf_Predicate) parser.parse("P(A,B)"));
		phi.addPositiveLiteral((mf_Predicate) parser.parse("Q(E,A)"));
		phi.addPositiveLiteral((mf_Predicate) parser.parse("Q(A,B)"));

		Assert.assertFalse(phi.subsumes(psi));
	}
}
