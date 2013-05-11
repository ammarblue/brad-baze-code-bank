package reuze.test;
//package aima.test.core.unit.logic.fol;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.software.reuze.mf_Domain;
import com.software.reuze.mf_DomainExamples;
import com.software.reuze.mf_NodeTermFunction;
import com.software.reuze.mf_NodeTermVariable;
import com.software.reuze.mf_Parser;
import com.software.reuze.mf_Predicate;
import com.software.reuze.mf_SentenceAtomicTermEquality;
import com.software.reuze.mf_SymbolConstant;
import com.software.reuze.mf_i_NodeTerm;
import com.software.reuze.mf_i_Sentence;
import com.software.reuze.ml_Unifier;


/*import aima.core.logic.fol.Unifier;
import aima.core.logic.fol.domain.DomainFactory;
import aima.core.logic.fol.domain.FOLDomain;
import aima.core.logic.fol.parsing.FOLParser;
import aima.core.logic.fol.parsing.ast.Constant;
import aima.core.logic.fol.parsing.ast.Function;
import aima.core.logic.fol.parsing.ast.Predicate;
import aima.core.logic.fol.parsing.ast.FOSentence;
import aima.core.logic.fol.parsing.ast.Term;
import aima.core.logic.fol.parsing.ast.TermEquality;
import aima.core.logic.fol.parsing.ast.Variable;*/

/**
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 */
public class UnifierTest {

	private mf_Parser parser;
	private ml_Unifier unifier;
	private Map<mf_NodeTermVariable, mf_i_NodeTerm> theta;

	@Before
	public void setUp() {
		parser = new mf_Parser(mf_DomainExamples.knowsDomain());
		unifier = new ml_Unifier();
		theta = new Hashtable<mf_NodeTermVariable, mf_i_NodeTerm>();
	}

	@Test
	public void testFailureIfThetaisNull() {
		mf_NodeTermVariable var = new mf_NodeTermVariable("x");
		mf_i_Sentence sentence = parser.parse("Knows(x)");
		theta = null;
		Map<mf_NodeTermVariable, mf_i_NodeTerm> result = unifier.unify(var, sentence, theta);
		Assert.assertNull(result);
	}

	@Test
	public void testUnificationFailure() {
		mf_NodeTermVariable var = new mf_NodeTermVariable("x");
		mf_i_Sentence sentence = parser.parse("Knows(y)");
		theta = null;
		Map<mf_NodeTermVariable, mf_i_NodeTerm> result = unifier.unify(var, sentence, theta);
		Assert.assertNull(result);
	}

	@Test
	public void testThetaPassedBackIfXEqualsYBothVariables() {
		mf_NodeTermVariable var1 = new mf_NodeTermVariable("x");
		mf_NodeTermVariable var2 = new mf_NodeTermVariable("x");

		theta.put(new mf_NodeTermVariable("dummy"), new mf_NodeTermVariable("dummy"));
		Map<mf_NodeTermVariable, mf_i_NodeTerm> result = unifier.unify(var1, var2, theta);
		Assert.assertEquals(theta, result);
		Assert.assertEquals(1, theta.keySet().size());
		Assert.assertTrue(theta.containsKey(new mf_NodeTermVariable("dummy")));
	}

	@Test
	public void testVariableEqualsConstant() {
		mf_NodeTermVariable var1 = new mf_NodeTermVariable("x");
		mf_SymbolConstant constant = new mf_SymbolConstant("John");

		Map<mf_NodeTermVariable, mf_i_NodeTerm> result = unifier.unify(var1, constant, theta);
		Assert.assertEquals(theta, result);
		Assert.assertEquals(1, theta.keySet().size());
		Assert.assertTrue(theta.keySet().contains(var1));
		Assert.assertEquals(constant, theta.get(var1));
	}

	@Test
	public void testSimpleVariableUnification() {
		mf_NodeTermVariable var1 = new mf_NodeTermVariable("x");
		List<mf_i_NodeTerm> terms1 = new ArrayList<mf_i_NodeTerm>();
		terms1.add(var1);
		mf_Predicate p1 = new mf_Predicate("King", terms1); // King(x)

		List<mf_i_NodeTerm> terms2 = new ArrayList<mf_i_NodeTerm>();
		terms2.add(new mf_SymbolConstant("John"));
		mf_Predicate p2 = new mf_Predicate("King", terms2); // King(John)

		Map<mf_NodeTermVariable, mf_i_NodeTerm> result = unifier.unify(p1, p2, theta);
		Assert.assertEquals(theta, result);
		Assert.assertEquals(1, theta.keySet().size());
		Assert.assertTrue(theta.keySet().contains(new mf_NodeTermVariable("x"))); // x =
		Assert.assertEquals(new mf_SymbolConstant("John"), theta.get(var1)); // John
	}

	@Test
	public void testKnows1() {
		mf_i_Sentence query = parser.parse("Knows(John,x)");
		mf_i_Sentence johnKnowsJane = parser.parse("Knows(John,Jane)");
		Map<mf_NodeTermVariable, mf_i_NodeTerm> result = unifier.unify(query, johnKnowsJane, theta);
		Assert.assertEquals(theta, result);
		Assert.assertTrue(theta.keySet().contains(new mf_NodeTermVariable("x"))); // x =
		Assert.assertEquals(new mf_SymbolConstant("Jane"), theta.get(new mf_NodeTermVariable("x"))); // Jane
	}

	@Test
	public void testKnows2() {
		mf_i_Sentence query = parser.parse("Knows(John,x)");
		mf_i_Sentence johnKnowsJane = parser.parse("Knows(y,Bill)");
		Map<mf_NodeTermVariable, mf_i_NodeTerm> result = unifier.unify(query, johnKnowsJane, theta);

		Assert.assertEquals(2, result.size());

		Assert.assertEquals(new mf_SymbolConstant("Bill"), theta.get(new mf_NodeTermVariable("x"))); // x
		// =
		// Bill
		Assert.assertEquals(new mf_SymbolConstant("John"), theta.get(new mf_NodeTermVariable("y"))); // y
		// =
		// John
	}

	@Test
	public void testKnows3() {
		mf_i_Sentence query = parser.parse("Knows(John,x)");
		mf_i_Sentence johnKnowsJane = parser.parse("Knows(y,Mother(y))");
		Map<mf_NodeTermVariable, mf_i_NodeTerm> result = unifier.unify(query, johnKnowsJane, theta);

		Assert.assertEquals(2, result.size());

		List<mf_i_NodeTerm> terms = new ArrayList<mf_i_NodeTerm>();
		terms.add(new mf_SymbolConstant("John"));
		mf_NodeTermFunction mother = new mf_NodeTermFunction("Mother", terms);
		Assert.assertEquals(mother, theta.get(new mf_NodeTermVariable("x")));
		Assert.assertEquals(new mf_SymbolConstant("John"), theta.get(new mf_NodeTermVariable("y")));
	}

	@Test
	public void testKnows5() {
		mf_i_Sentence query = parser.parse("Knows(John,x)");
		mf_i_Sentence johnKnowsJane = parser.parse("Knows(y,z)");
		Map<mf_NodeTermVariable, mf_i_NodeTerm> result = unifier.unify(query, johnKnowsJane, theta);

		Assert.assertEquals(2, result.size());

		Assert.assertEquals(new mf_NodeTermVariable("z"), theta.get(new mf_NodeTermVariable("x"))); // x
		// =
		// z
		Assert.assertEquals(new mf_SymbolConstant("John"), theta.get(new mf_NodeTermVariable("y"))); // y
		// =
		// John
	}

	@Test
	public void testCascadedOccursCheck() {
		mf_Domain domain = new mf_Domain();
		domain.addPredicate("P");
		domain.addFunction("F");
		domain.addFunction("SF0");
		domain.addFunction("SF1");
		mf_Parser parser = new mf_Parser(domain);

		mf_i_Sentence s1 = parser.parse("P(SF1(v2),v2)");
		mf_i_Sentence s2 = parser.parse("P(v3,SF0(v3))");
		Map<mf_NodeTermVariable, mf_i_NodeTerm> result = unifier.unify(s1, s2);

		Assert.assertNull(result);

		s1 = parser.parse("P(v1,SF0(v1),SF0(v1),SF0(v1),SF0(v1))");
		s2 = parser.parse("P(v2,SF0(v2),v2,     v3,     v2)");
		result = unifier.unify(s1, s2);

		Assert.assertNull(result);

		s1 = parser
				.parse("P(v1,   F(v2),F(v2),F(v2),v1,      F(F(v1)),F(F(F(v1))),v2)");
		s2 = parser
				.parse("P(F(v3),v4,   v5,   v6,   F(F(v5)),v4,      F(v3),      F(F(v5)))");
		result = unifier.unify(s1, s2);

		Assert.assertNull(result);
	}

	/**
	 * From: TPTP:LCL418-1 Am performing an incorrect unification for:
	 * [is_a_theorem
	 * (equivalent(equivalent(c1744,c1743),equivalent(c1742,c1743))),
	 * is_a_theorem(equivalent(equivalent(c1752,c1751),c1752))]
	 * 
	 * which is giving the following substitution:
	 * 
	 * subst={c1744=equivalent(c1742,c1743), c1743=c1751,
	 * c1752=equivalent(c1742,c1751)}
	 * 
	 * which is incorrect as c1743 in the first function term needs to be c1751
	 * as this is the second substitution.
	 */
	@Test
	public void testBadCascadeSubstitution_LCL418_1() {
		mf_Domain domain = new mf_Domain();
		domain.addPredicate("ISATHEOREM");
		domain.addFunction("EQUIVALENT");
		mf_Parser parser = new mf_Parser(domain);

		mf_i_Sentence s1 = parser
				.parse("ISATHEOREM(EQUIVALENT(EQUIVALENT(c1744,c1743),EQUIVALENT(c1742,c1743)))");
		mf_i_Sentence s2 = parser
				.parse("ISATHEOREM(EQUIVALENT(EQUIVALENT(c1752,c1751),c1752))");
		Map<mf_NodeTermVariable, mf_i_NodeTerm> result = unifier.unify(s1, s2);

		Assert.assertEquals(
				"{c1744=EQUIVALENT(c1742,c1751), c1743=c1751, c1752=EQUIVALENT(c1742,c1751)}",
				result.toString());
	}

	@Test
	public void testAdditionalVariableMixtures() {
		mf_Domain domain = new mf_Domain();
		domain.addConstant("A");
		domain.addConstant("B");
		domain.addFunction("F");
		domain.addFunction("G");
		domain.addFunction("H");
		domain.addPredicate("P");

		mf_Parser parser = new mf_Parser(domain);

		// Test Cascade Substitutions handled correctly
		mf_i_Sentence s1 = parser.parse("P(z, x)");
		mf_i_Sentence s2 = parser.parse("P(x, a)");
		Map<mf_NodeTermVariable, mf_i_NodeTerm> result = unifier.unify(s1, s2);

		Assert.assertEquals("{z=a, x=a}", result.toString());

		s1 = parser.parse("P(x, z)");
		s2 = parser.parse("P(a, x)");
		result = unifier.unify(s1, s2);

		Assert.assertEquals("{x=a, z=a}", result.toString());

		s1 = parser.parse("P(w, w, w)");
		s2 = parser.parse("P(x, y, z)");
		result = unifier.unify(s1, s2);

		Assert.assertEquals("{w=z, x=z, y=z}", result.toString());

		s1 = parser.parse("P(x, y, z)");
		s2 = parser.parse("P(w, w, w)");
		result = unifier.unify(s1, s2);

		Assert.assertEquals("{x=w, y=w, z=w}", result.toString());

		s1 = parser.parse("P(x, B, F(y))");
		s2 = parser.parse("P(A, y, F(z))");
		result = unifier.unify(s1, s2);

		Assert.assertEquals("{x=A, y=B, z=B}", result.toString());

		s1 = parser.parse("P(F(x,B), G(y),         F(z,A))");
		s2 = parser.parse("P(y,      G(F(G(w),w)), F(w,z))");
		result = unifier.unify(s1, s2);

		Assert.assertNull(result);

		s1 = parser.parse("P(F(G(A)), x,    F(H(z,z)), H(y,    G(w)))");
		s2 = parser.parse("P(y,       G(z), F(v     ), H(F(w), x   ))");
		result = unifier.unify(s1, s2);

		Assert.assertEquals(
				"{y=F(G(A)), x=G(G(A)), v=H(G(A),G(A)), w=G(A), z=G(A)}",
				result.toString());
	}

	@Test
	public void testTermEquality() {
		mf_Domain domain = new mf_Domain();
		domain.addConstant("A");
		domain.addConstant("B");
		domain.addFunction("Plus");

		mf_Parser parser = new mf_Parser(domain);

		mf_SentenceAtomicTermEquality te1 = (mf_SentenceAtomicTermEquality) parser.parse("x = x");
		mf_SentenceAtomicTermEquality te2 = (mf_SentenceAtomicTermEquality) parser.parse("x = x");

		// Both term equalities the same,
		// should unify but no substitutions.
		Map<mf_NodeTermVariable, mf_i_NodeTerm> result = unifier.unify(te1, te2);

		Assert.assertNotNull(result);
		Assert.assertEquals(0, result.size());

		// Different variable names but should unify.
		te1 = (mf_SentenceAtomicTermEquality) parser.parse("x1 = x1");
		te2 = (mf_SentenceAtomicTermEquality) parser.parse("x2 = x2");

		result = unifier.unify(te1, te2);

		Assert.assertNotNull(result);
		Assert.assertEquals(1, result.size());
		Assert.assertEquals("{x1=x2}", result.toString());

		// Test simple unification with reflexivity axiom
		te1 = (mf_SentenceAtomicTermEquality) parser.parse("x1 = x1");
		te2 = (mf_SentenceAtomicTermEquality) parser.parse("Plus(A,B) = Plus(A,B)");

		result = unifier.unify(te1, te2);

		Assert.assertNotNull(result);

		Assert.assertEquals(1, result.size());
		Assert.assertEquals("{x1=Plus(A,B)}", result.toString());

		// Test more complex unification with reflexivity axiom
		te1 = (mf_SentenceAtomicTermEquality) parser.parse("x1 = x1");
		te2 = (mf_SentenceAtomicTermEquality) parser.parse("Plus(A,B) = Plus(A,z1)");

		result = unifier.unify(te1, te2);

		Assert.assertNotNull(result);

		Assert.assertEquals(2, result.size());
		Assert.assertEquals("{x1=Plus(A,B), z1=B}", result.toString());

		// Test reverse of previous unification with reflexivity axiom
		// Should still be the same.
		te1 = (mf_SentenceAtomicTermEquality) parser.parse("x1 = x1");
		te2 = (mf_SentenceAtomicTermEquality) parser.parse("Plus(A,z1) = Plus(A,B)");

		result = unifier.unify(te1, te2);

		Assert.assertNotNull(result);

		Assert.assertEquals(2, result.size());
		Assert.assertEquals("{x1=Plus(A,B), z1=B}", result.toString());

		// Test with nested terms
		te1 = (mf_SentenceAtomicTermEquality) parser
				.parse("Plus(Plus(Plus(A,B),B, A)) = Plus(Plus(Plus(A,B),B, A))");
		te2 = (mf_SentenceAtomicTermEquality) parser
				.parse("Plus(Plus(Plus(A,B),B, A)) = Plus(Plus(Plus(A,B),B, A))");

		result = unifier.unify(te1, te2);

		Assert.assertNotNull(result);

		Assert.assertEquals(0, result.size());

		// Simple term equality unification fails
		te1 = (mf_SentenceAtomicTermEquality) parser.parse("Plus(A,B) = Plus(B,A)");
		te2 = (mf_SentenceAtomicTermEquality) parser.parse("Plus(A,B) = Plus(A,B)");

		result = unifier.unify(te1, te2);

		Assert.assertNull(result);
	}

	@Test
	public void testNOTSentence() {
		mf_Domain domain = new mf_Domain();
		domain.addConstant("A");
		domain.addConstant("B");
		domain.addConstant("C");
		domain.addFunction("Plus");
		domain.addPredicate("P");

		mf_Parser parser = new mf_Parser(domain);

		mf_i_Sentence s1 = parser.parse("NOT(P(A))");
		mf_i_Sentence s2 = parser.parse("NOT(P(A))");

		Map<mf_NodeTermVariable, mf_i_NodeTerm> result = unifier.unify(s1, s2);

		Assert.assertNotNull(result);
		Assert.assertEquals(0, result.size());

		s1 = parser.parse("NOT(P(A))");
		s2 = parser.parse("NOT(P(B))");

		result = unifier.unify(s1, s2);

		Assert.assertNull(result);

		s1 = parser.parse("NOT(P(A))");
		s2 = parser.parse("NOT(P(x))");

		result = unifier.unify(s1, s2);

		Assert.assertNotNull(result);
		Assert.assertEquals(1, result.size());
		Assert.assertEquals(new mf_SymbolConstant("A"), result.get(new mf_NodeTermVariable("x")));
	}

	@Test
	public void testConnectedSentence() {
		mf_Domain domain = new mf_Domain();
		domain.addConstant("A");
		domain.addConstant("B");
		domain.addConstant("C");
		domain.addFunction("Plus");
		domain.addPredicate("P");

		mf_Parser parser = new mf_Parser(domain);

		mf_i_Sentence s1 = parser.parse("(P(A) AND P(B))");
		mf_i_Sentence s2 = parser.parse("(P(A) AND P(B))");

		Map<mf_NodeTermVariable, mf_i_NodeTerm> result = unifier.unify(s1, s2);

		Assert.assertNotNull(result);
		Assert.assertEquals(0, result.size());

		s1 = parser.parse("(P(A) AND P(B))");
		s2 = parser.parse("(P(A) AND P(C))");

		result = unifier.unify(s1, s2);

		Assert.assertNull(result);

		s1 = parser.parse("(P(A) AND P(B))");
		s2 = parser.parse("(P(A) AND P(x))");

		result = unifier.unify(s1, s2);

		Assert.assertNotNull(result);
		Assert.assertEquals(1, result.size());
		Assert.assertEquals(new mf_SymbolConstant("B"), result.get(new mf_NodeTermVariable("x")));

		s1 = parser.parse("(P(A) OR P(B))");
		s2 = parser.parse("(P(A) OR P(B))");

		result = unifier.unify(s1, s2);

		Assert.assertNotNull(result);
		Assert.assertEquals(0, result.size());

		s1 = parser.parse("(P(A) OR P(B))");
		s2 = parser.parse("(P(A) OR P(C))");

		result = unifier.unify(s1, s2);

		Assert.assertNull(result);

		s1 = parser.parse("(P(A) OR P(B))");
		s2 = parser.parse("(P(A) OR P(x))");

		result = unifier.unify(s1, s2);

		Assert.assertNotNull(result);
		Assert.assertEquals(1, result.size());
		Assert.assertEquals(new mf_SymbolConstant("B"), result.get(new mf_NodeTermVariable("x")));

		s1 = parser.parse("(P(A) => P(B))");
		s2 = parser.parse("(P(A) => P(B))");

		result = unifier.unify(s1, s2);

		Assert.assertNotNull(result);
		Assert.assertEquals(0, result.size());

		s1 = parser.parse("(P(A) => P(B))");
		s2 = parser.parse("(P(A) => P(C))");

		result = unifier.unify(s1, s2);

		Assert.assertNull(result);

		s1 = parser.parse("(P(A) => P(B))");
		s2 = parser.parse("(P(A) => P(x))");

		result = unifier.unify(s1, s2);

		Assert.assertNotNull(result);
		Assert.assertEquals(1, result.size());
		Assert.assertEquals(new mf_SymbolConstant("B"), result.get(new mf_NodeTermVariable("x")));

		s1 = parser.parse("(P(A) <=> P(B))");
		s2 = parser.parse("(P(A) <=> P(B))");

		result = unifier.unify(s1, s2);

		Assert.assertNotNull(result);
		Assert.assertEquals(0, result.size());

		s1 = parser.parse("(P(A) <=> P(B))");
		s2 = parser.parse("(P(A) <=> P(C))");

		result = unifier.unify(s1, s2);

		Assert.assertNull(result);

		s1 = parser.parse("(P(A) <=> P(B))");
		s2 = parser.parse("(P(A) <=> P(x))");

		result = unifier.unify(s1, s2);

		Assert.assertNotNull(result);
		Assert.assertEquals(1, result.size());
		Assert.assertEquals(new mf_SymbolConstant("B"), result.get(new mf_NodeTermVariable("x")));

		s1 = parser.parse("((P(A) AND P(B)) OR (P(C) => (P(A) <=> P(C))))");
		s2 = parser.parse("((P(A) AND P(B)) OR (P(C) => (P(A) <=> P(C))))");

		result = unifier.unify(s1, s2);

		Assert.assertNotNull(result);
		Assert.assertEquals(0, result.size());

		s1 = parser.parse("((P(A) AND P(B)) OR (P(C) => (P(A) <=> P(C))))");
		s2 = parser.parse("((P(A) AND P(B)) OR (P(C) => (P(A) <=> P(A))))");

		result = unifier.unify(s1, s2);

		Assert.assertNull(result);

		s1 = parser.parse("((P(A) AND P(B)) OR (P(C) => (P(A) <=> P(C))))");
		s2 = parser.parse("((P(A) AND P(B)) OR (P(C) => (P(A) <=> P(x))))");

		result = unifier.unify(s1, s2);

		Assert.assertNotNull(result);
		Assert.assertEquals(1, result.size());
		Assert.assertEquals(new mf_SymbolConstant("C"), result.get(new mf_NodeTermVariable("x")));
	}

	@Test
	public void testQuantifiedSentence() {
		mf_Domain domain = new mf_Domain();
		domain.addConstant("A");
		domain.addConstant("B");
		domain.addConstant("C");
		domain.addFunction("Plus");
		domain.addPredicate("P");

		mf_Parser parser = new mf_Parser(domain);

		mf_i_Sentence s1 = parser
				.parse("FORALL x,y ((P(x) AND P(A)) OR (P(A) => P(y)))");
		mf_i_Sentence s2 = parser
				.parse("FORALL x,y ((P(x) AND P(A)) OR (P(A) => P(y)))");

		Map<mf_NodeTermVariable, mf_i_NodeTerm> result = unifier.unify(s1, s2);

		Assert.assertNotNull(result);
		Assert.assertEquals(0, result.size());

		s1 = parser.parse("FORALL x,y ((P(x) AND P(A)) OR (P(A) => P(y)))");
		s2 = parser.parse("FORALL x   ((P(x) AND P(A)) OR (P(A) => P(y)))");

		result = unifier.unify(s1, s2);

		Assert.assertNull(result);

		s1 = parser.parse("FORALL x,y ((P(x) AND P(A)) OR (P(A) => P(y)))");
		s2 = parser.parse("FORALL x,y ((P(x) AND P(A)) OR (P(B) => P(y)))");

		result = unifier.unify(s1, s2);

		Assert.assertNull(result);

		s1 = parser.parse("FORALL x,y ((P(x) AND P(A)) OR (P(A) => P(y)))");
		s2 = parser.parse("FORALL x,y ((P(A) AND P(A)) OR (P(A) => P(y)))");

		result = unifier.unify(s1, s2);

		Assert.assertNotNull(result);
		Assert.assertEquals(1, result.size());
		Assert.assertEquals(new mf_SymbolConstant("A"), result.get(new mf_NodeTermVariable("x")));

		//
		s1 = parser.parse("EXISTS x,y ((P(x) AND P(A)) OR (P(A) => P(y)))");
		s2 = parser.parse("EXISTS x,y ((P(x) AND P(A)) OR (P(A) => P(y)))");

		result = unifier.unify(s1, s2);

		Assert.assertNotNull(result);
		Assert.assertEquals(0, result.size());

		s1 = parser.parse("EXISTS x,y ((P(x) AND P(A)) OR (P(A) => P(y)))");
		s2 = parser.parse("EXISTS x   ((P(x) AND P(A)) OR (P(A) => P(y)))");

		result = unifier.unify(s1, s2);

		Assert.assertNull(result);

		s1 = parser.parse("EXISTS x,y ((P(x) AND P(A)) OR (P(A) => P(y)))");
		s2 = parser.parse("EXISTS x,y ((P(x) AND P(A)) OR (P(B) => P(y)))");

		result = unifier.unify(s1, s2);

		Assert.assertNull(result);

		s1 = parser.parse("EXISTS x,y ((P(x) AND P(A)) OR (P(A) => P(y)))");
		s2 = parser.parse("EXISTS x,y ((P(A) AND P(A)) OR (P(A) => P(y)))");

		result = unifier.unify(s1, s2);

		Assert.assertNotNull(result);
		Assert.assertEquals(1, result.size());
		Assert.assertEquals(new mf_SymbolConstant("A"), result.get(new mf_NodeTermVariable("x")));
	}
}
