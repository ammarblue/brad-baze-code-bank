package reuze.test;
//package aima.test.core.unit.logic.fol;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.software.reuze.mf_DomainExamples;
import com.software.reuze.mf_NodeTermVariable;
import com.software.reuze.mf_Parser;
import com.software.reuze.mf_SymbolConstant;
import com.software.reuze.mf_VisitorSubstitution;
import com.software.reuze.mf_i_NodeTerm;
import com.software.reuze.mf_i_Sentence;


/*import aima.core.logic.fol.SubstVisitor;
import aima.core.logic.fol.domain.DomainFactory;
import aima.core.logic.fol.parsing.FOLParser;
import aima.core.logic.fol.parsing.ast.Constant;
import aima.core.logic.fol.parsing.ast.FOSentence;
import aima.core.logic.fol.parsing.ast.Term;
import aima.core.logic.fol.parsing.ast.Variable;*/

/**
 * @author Ravi Mohan
 * 
 */
public class SubstVisitorTest {

	private mf_Parser parser;
	private mf_VisitorSubstitution sv;

	@Before
	public void setUp() {
		parser = new mf_Parser(mf_DomainExamples.crusadesDomain());
		sv = new mf_VisitorSubstitution();
	}

	@Test
	public void testSubstSingleVariableSucceedsWithPredicate() {
		mf_i_Sentence beforeSubst = parser.parse("King(x)");
		mf_i_Sentence expectedAfterSubst = parser.parse(" King(John) ");
		mf_i_Sentence expectedAfterSubstCopy = expectedAfterSubst.copy();

		Assert.assertEquals(expectedAfterSubst, expectedAfterSubstCopy);
		Map<mf_NodeTermVariable, mf_i_NodeTerm> p = new LinkedHashMap<mf_NodeTermVariable, mf_i_NodeTerm>();
		p.put(new mf_NodeTermVariable("x"), new mf_SymbolConstant("John"));

		mf_i_Sentence afterSubst = sv.subst(p, beforeSubst);
		Assert.assertEquals(expectedAfterSubst, afterSubst);
		Assert.assertEquals(beforeSubst, parser.parse("King(x)"));
	}

	@Test
	public void testSubstSingleVariableFailsWithPredicate() {
		mf_i_Sentence beforeSubst = parser.parse("King(x)");
		mf_i_Sentence expectedAfterSubst = parser.parse(" King(x) ");

		Map<mf_NodeTermVariable, mf_i_NodeTerm> p = new LinkedHashMap<mf_NodeTermVariable, mf_i_NodeTerm>();
		p.put(new mf_NodeTermVariable("y"), new mf_SymbolConstant("John"));

		mf_i_Sentence afterSubst = sv.subst(p, beforeSubst);
		Assert.assertEquals(expectedAfterSubst, afterSubst);
		Assert.assertEquals(beforeSubst, parser.parse("King(x)"));
	}

	@Test
	public void testMultipleVariableSubstitutionWithPredicate() {
		mf_i_Sentence beforeSubst = parser.parse("King(x,y)");
		mf_i_Sentence expectedAfterSubst = parser.parse(" King(John ,England) ");

		Map<mf_NodeTermVariable, mf_i_NodeTerm> p = new LinkedHashMap<mf_NodeTermVariable, mf_i_NodeTerm>();
		p.put(new mf_NodeTermVariable("x"), new mf_SymbolConstant("John"));
		p.put(new mf_NodeTermVariable("y"), new mf_SymbolConstant("England"));

		mf_i_Sentence afterSubst = sv.subst(p, beforeSubst);
		Assert.assertEquals(expectedAfterSubst, afterSubst);
		Assert.assertEquals(beforeSubst, parser.parse("King(x,y)"));
	}

	@Test
	public void testMultipleVariablePartiallySucceedsWithPredicate() {
		mf_i_Sentence beforeSubst = parser.parse("King(x,y)");
		mf_i_Sentence expectedAfterSubst = parser.parse(" King(John ,y) ");

		Map<mf_NodeTermVariable, mf_i_NodeTerm> p = new LinkedHashMap<mf_NodeTermVariable, mf_i_NodeTerm>();
		p.put(new mf_NodeTermVariable("x"), new mf_SymbolConstant("John"));
		p.put(new mf_NodeTermVariable("z"), new mf_SymbolConstant("England"));

		mf_i_Sentence afterSubst = sv.subst(p, beforeSubst);
		Assert.assertEquals(expectedAfterSubst, afterSubst);
		Assert.assertEquals(beforeSubst, parser.parse("King(x,y)"));
	}

	@Test
	public void testSubstSingleVariableSucceedsWithTermEquality() {
		mf_i_Sentence beforeSubst = parser.parse("BrotherOf(x) = EnemyOf(y)");
		mf_i_Sentence expectedAfterSubst = parser
				.parse("BrotherOf(John) = EnemyOf(Saladin)");

		Map<mf_NodeTermVariable, mf_i_NodeTerm> p = new LinkedHashMap<mf_NodeTermVariable, mf_i_NodeTerm>();
		p.put(new mf_NodeTermVariable("x"), new mf_SymbolConstant("John"));
		p.put(new mf_NodeTermVariable("y"), new mf_SymbolConstant("Saladin"));

		mf_i_Sentence afterSubst = sv.subst(p, beforeSubst);
		Assert.assertEquals(expectedAfterSubst, afterSubst);
		Assert.assertEquals(beforeSubst,
				parser.parse("BrotherOf(x) = EnemyOf(y)"));
	}

	@Test
	public void testSubstSingleVariableSucceedsWithTermEquality2() {
		mf_i_Sentence beforeSubst = parser.parse("BrotherOf(John) = x)");
		mf_i_Sentence expectedAfterSubst = parser.parse("BrotherOf(John) = Richard");

		Map<mf_NodeTermVariable, mf_i_NodeTerm> p = new LinkedHashMap<mf_NodeTermVariable, mf_i_NodeTerm>();
		p.put(new mf_NodeTermVariable("x"), new mf_SymbolConstant("Richard"));
		p.put(new mf_NodeTermVariable("y"), new mf_SymbolConstant("Saladin"));

		mf_i_Sentence afterSubst = sv.subst(p, beforeSubst);
		Assert.assertEquals(expectedAfterSubst, afterSubst);
		Assert.assertEquals(parser.parse("BrotherOf(John) = x)"), beforeSubst);
	}

	@Test
	public void testSubstWithUniversalQuantifierAndSngleVariable() {
		mf_i_Sentence beforeSubst = parser.parse("FORALL x King(x))");
		mf_i_Sentence expectedAfterSubst = parser.parse("King(John)");

		Map<mf_NodeTermVariable, mf_i_NodeTerm> p = new LinkedHashMap<mf_NodeTermVariable, mf_i_NodeTerm>();
		p.put(new mf_NodeTermVariable("x"), new mf_SymbolConstant("John"));

		mf_i_Sentence afterSubst = sv.subst(p, beforeSubst);
		Assert.assertEquals(expectedAfterSubst, afterSubst);
		Assert.assertEquals(parser.parse("FORALL x King(x))"), beforeSubst);
	}

	@Test
	public void testSubstWithUniversalQuantifierAndZeroVariablesMatched() {
		mf_i_Sentence beforeSubst = parser.parse("FORALL x King(x))");
		mf_i_Sentence expectedAfterSubst = parser.parse("FORALL x King(x)");

		Map<mf_NodeTermVariable, mf_i_NodeTerm> p = new LinkedHashMap<mf_NodeTermVariable, mf_i_NodeTerm>();
		p.put(new mf_NodeTermVariable("y"), new mf_SymbolConstant("John"));

		mf_i_Sentence afterSubst = sv.subst(p, beforeSubst);
		Assert.assertEquals(expectedAfterSubst, afterSubst);
		Assert.assertEquals(parser.parse("FORALL x King(x))"), beforeSubst);
	}

	@Test
	public void testSubstWithUniversalQuantifierAndOneOfTwoVariablesMatched() {
		mf_i_Sentence beforeSubst = parser.parse("FORALL x,y King(x,y))");
		mf_i_Sentence expectedAfterSubst = parser.parse("FORALL x King(x,John)");

		Map<mf_NodeTermVariable, mf_i_NodeTerm> p = new LinkedHashMap<mf_NodeTermVariable, mf_i_NodeTerm>();
		p.put(new mf_NodeTermVariable("y"), new mf_SymbolConstant("John"));

		mf_i_Sentence afterSubst = sv.subst(p, beforeSubst);
		Assert.assertEquals(expectedAfterSubst, afterSubst);
		Assert.assertEquals(parser.parse("FORALL x,y King(x,y))"), beforeSubst);
	}

	@Test
	public void testSubstWithExistentialQuantifierAndSngleVariable() {
		mf_i_Sentence beforeSubst = parser.parse("EXISTS x King(x))");
		mf_i_Sentence expectedAfterSubst = parser.parse("King(John)");

		Map<mf_NodeTermVariable, mf_i_NodeTerm> p = new LinkedHashMap<mf_NodeTermVariable, mf_i_NodeTerm>();
		p.put(new mf_NodeTermVariable("x"), new mf_SymbolConstant("John"));

		mf_i_Sentence afterSubst = sv.subst(p, beforeSubst);

		Assert.assertEquals(expectedAfterSubst, afterSubst);
		Assert.assertEquals(parser.parse("EXISTS x King(x)"), beforeSubst);
	}

	@Test
	public void testSubstWithNOTSentenceAndSngleVariable() {
		mf_i_Sentence beforeSubst = parser.parse("NOT King(x))");
		mf_i_Sentence expectedAfterSubst = parser.parse("NOT King(John)");

		Map<mf_NodeTermVariable, mf_i_NodeTerm> p = new LinkedHashMap<mf_NodeTermVariable, mf_i_NodeTerm>();
		p.put(new mf_NodeTermVariable("x"), new mf_SymbolConstant("John"));

		mf_i_Sentence afterSubst = sv.subst(p, beforeSubst);
		Assert.assertEquals(expectedAfterSubst, afterSubst);
		Assert.assertEquals(parser.parse("NOT King(x))"), beforeSubst);
	}

	@Test
	public void testConnectiveANDSentenceAndSngleVariable() {
		mf_i_Sentence beforeSubst = parser
				.parse("EXISTS x ( King(x) AND BrotherOf(x) = EnemyOf(y) )");
		mf_i_Sentence expectedAfterSubst = parser
				.parse("( King(John) AND BrotherOf(John) = EnemyOf(Saladin) )");

		Map<mf_NodeTermVariable, mf_i_NodeTerm> p = new LinkedHashMap<mf_NodeTermVariable, mf_i_NodeTerm>();
		p.put(new mf_NodeTermVariable("x"), new mf_SymbolConstant("John"));
		p.put(new mf_NodeTermVariable("y"), new mf_SymbolConstant("Saladin"));

		mf_i_Sentence afterSubst = sv.subst(p, beforeSubst);
		Assert.assertEquals(expectedAfterSubst, afterSubst);
		Assert.assertEquals(parser
				.parse("EXISTS x ( King(x) AND BrotherOf(x) = EnemyOf(y) )"),
				beforeSubst);
	}

	@Test
	public void testParanthisedSingleVariable() {
		mf_i_Sentence beforeSubst = parser.parse("((( King(x))))");
		mf_i_Sentence expectedAfterSubst = parser.parse("King(John) ");

		Map<mf_NodeTermVariable, mf_i_NodeTerm> p = new LinkedHashMap<mf_NodeTermVariable, mf_i_NodeTerm>();
		p.put(new mf_NodeTermVariable("x"), new mf_SymbolConstant("John"));

		mf_i_Sentence afterSubst = sv.subst(p, beforeSubst);
		Assert.assertEquals(expectedAfterSubst, afterSubst);
		Assert.assertEquals(parser.parse("((( King(x))))"), beforeSubst);
	}
}