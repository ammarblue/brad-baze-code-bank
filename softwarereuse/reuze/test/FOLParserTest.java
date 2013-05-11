package reuze.test;
//package aima.test.core.unit.logic.fol.parsing;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.software.reuze.mf_Domain;
import com.software.reuze.mf_DomainExamples;
import com.software.reuze.mf_Lexer;
import com.software.reuze.mf_NodeTermFunction;
import com.software.reuze.mf_NodeTermVariable;
import com.software.reuze.mf_Parser;
import com.software.reuze.mf_Predicate;
import com.software.reuze.mf_SentenceAtomicTermEquality;
import com.software.reuze.mf_SentenceConnected;
import com.software.reuze.mf_SentenceNot;
import com.software.reuze.mf_SentenceQuantified;
import com.software.reuze.mf_SymbolConstant;
import com.software.reuze.mf_i_NodeTerm;
import com.software.reuze.mf_i_Sentence;


/*import aima.core.logic.fol.domain.DomainFactory;
import aima.core.logic.fol.domain.FOLDomain;
import aima.core.logic.fol.parsing.FOLLexer;
import aima.core.logic.fol.parsing.FOLParser;
import aima.core.logic.fol.parsing.ast.ConnectedSentence;
import aima.core.logic.fol.parsing.ast.Constant;
import aima.core.logic.fol.parsing.ast.Function;
import aima.core.logic.fol.parsing.ast.NotSentence;
import aima.core.logic.fol.parsing.ast.Predicate;
import aima.core.logic.fol.parsing.ast.QuantifiedSentence;
import aima.core.logic.fol.parsing.ast.FOSentence;
import aima.core.logic.fol.parsing.ast.Term;
import aima.core.logic.fol.parsing.ast.TermEquality;
import aima.core.logic.fol.parsing.ast.Variable;*/

/**
 * @author Ravi Mohan
 * 
 */
public class FOLParserTest {
	mf_Lexer lexer;

	mf_Parser parser;

	@Before
	public void setUp() {
		mf_Domain domain = mf_DomainExamples.crusadesDomain();

		lexer = new mf_Lexer(domain);
		parser = new mf_Parser(lexer);
	}

	@Test
	public void testParseSimpleVariable() {
		parser.setUpToParse("x");
		mf_i_NodeTerm v = parser.parseVariable();
		Assert.assertEquals(v, new mf_NodeTermVariable("x"));
	}

	@Test
	public void testParseIndexedVariable() {
		parser.setUpToParse("x1");
		mf_i_NodeTerm v = parser.parseVariable();
		Assert.assertEquals(v, new mf_NodeTermVariable("x1"));
	}

	@Test(expected = RuntimeException.class)
	public void testNotAllowedParseLeadingIndexedVariable() {
		parser.setUpToParse("1x");
		parser.parseVariable();
	}

	@Test
	public void testParseSimpleConstant() {
		parser.setUpToParse("John");
		mf_i_NodeTerm c = parser.parseConstant();
		Assert.assertEquals(c, new mf_SymbolConstant("John"));
	}

	@Test
	public void testParseFunction() {
		parser.setUpToParse("BrotherOf(John)");
		mf_i_NodeTerm f = parser.parseFunction();
		Assert.assertEquals(f, getBrotherOfFunction(new mf_SymbolConstant("John")));
	}

	@Test
	public void testParseMultiArityFunction() {
		parser.setUpToParse("LegsOf(John,Saladin,Richard)");
		mf_i_NodeTerm f = parser.parseFunction();
		Assert.assertEquals(f, getLegsOfFunction());
		Assert.assertEquals(3, ((mf_NodeTermFunction) f).getTerms().size());
	}

	@Test
	public void testPredicate() {
		// parser.setUpToParse("King(John)");
		mf_Predicate p = (mf_Predicate) parser.parse("King(John)");
		Assert.assertEquals(p, getKingPredicate(new mf_SymbolConstant("John")));
	}

	@Test
	public void testTermEquality() {
		try {
			mf_SentenceAtomicTermEquality te = (mf_SentenceAtomicTermEquality) parser
					.parse("BrotherOf(John) = EnemyOf(Saladin)");
			Assert.assertEquals(te, new mf_SentenceAtomicTermEquality(
					getBrotherOfFunction(new mf_SymbolConstant("John")),
					getEnemyOfFunction()));
		} catch (RuntimeException e) {
			Assert.fail("RuntimeException thrown");
		}
	}

	@Test
	public void testTermEquality2() {
		try {
			mf_SentenceAtomicTermEquality te = (mf_SentenceAtomicTermEquality) parser
					.parse("BrotherOf(John) = x)");
			Assert.assertEquals(te, new mf_SentenceAtomicTermEquality(
					getBrotherOfFunction(new mf_SymbolConstant("John")), new mf_NodeTermVariable(
							"x")));
		} catch (RuntimeException e) {
			Assert.fail("RuntimeException thrown");
		}
	}

	@Test
	public void testNotSentence() {
		mf_SentenceNot ns = (mf_SentenceNot) parser
				.parse("NOT BrotherOf(John) = EnemyOf(Saladin)");
		Assert.assertEquals(ns.getNegated(), new mf_SentenceAtomicTermEquality(
				getBrotherOfFunction(new mf_SymbolConstant("John")),
				getEnemyOfFunction()));
	}

	@Test
	public void testSimpleParanthizedSentence() {
		mf_i_Sentence ps = parser.parse("(NOT King(John))");
		Assert.assertEquals(ps, new mf_SentenceNot(getKingPredicate(new mf_SymbolConstant(
				"John"))));
	}

	@Test
	public void testExtraParanthizedSentence() {
		mf_i_Sentence ps = parser.parse("(((NOT King(John))))");
		Assert.assertEquals(ps, new mf_SentenceNot(getKingPredicate(new mf_SymbolConstant(
				"John"))));
	}

	@Test
	public void testParseComplexParanthizedSentence() {
		mf_i_Sentence ps = parser.parse("(NOT BrotherOf(John) = EnemyOf(Saladin))");
		Assert.assertEquals(ps, new mf_SentenceNot(new mf_SentenceAtomicTermEquality(
				getBrotherOfFunction(new mf_SymbolConstant("John")),
				getEnemyOfFunction())));
	}

	@Test
	public void testParseSimpleConnectedSentence() {
		mf_i_Sentence ps = parser.parse("(King(John) AND NOT King(Richard))");

		Assert.assertEquals(ps, new mf_SentenceConnected("AND",
				getKingPredicate(new mf_SymbolConstant("John")), new mf_SentenceNot(
						getKingPredicate(new mf_SymbolConstant("Richard")))));

		ps = parser.parse("(King(John) AND King(Saladin))");
		Assert.assertEquals(ps, new mf_SentenceConnected("AND",
				getKingPredicate(new mf_SymbolConstant("John")),
				getKingPredicate(new mf_SymbolConstant("Saladin"))));
	}

	@Test
	public void testComplexConnectedSentence1() {
		mf_i_Sentence ps = parser
				.parse("((King(John) AND NOT King(Richard)) OR King(Saladin))");

		Assert.assertEquals(ps, new mf_SentenceConnected("OR",
				new mf_SentenceConnected("AND", getKingPredicate(new mf_SymbolConstant(
						"John")), new mf_SentenceNot(
						getKingPredicate(new mf_SymbolConstant("Richard")))),
				getKingPredicate(new mf_SymbolConstant("Saladin"))));
	}

	@Test
	public void testQuantifiedSentenceWithSingleVariable() {
		mf_i_Sentence qs = parser.parse("FORALL x  King(x)");
		List<mf_NodeTermVariable> vars = new ArrayList<mf_NodeTermVariable>();
		vars.add(new mf_NodeTermVariable("x"));
		Assert.assertEquals(qs, new mf_SentenceQuantified("FORALL", vars,
				getKingPredicate(new mf_NodeTermVariable("x"))));
	}

	@Test
	public void testQuantifiedSentenceWithTwoVariables() {
		mf_i_Sentence qs = parser
				.parse("EXISTS x,y  (King(x) AND BrotherOf(x) = y)");
		List<mf_NodeTermVariable> vars = new ArrayList<mf_NodeTermVariable>();
		vars.add(new mf_NodeTermVariable("x"));
		vars.add(new mf_NodeTermVariable("y"));
		mf_SentenceConnected cse = new mf_SentenceConnected("AND",
				getKingPredicate(new mf_NodeTermVariable("x")), new mf_SentenceAtomicTermEquality(
						getBrotherOfFunction(new mf_NodeTermVariable("x")), new mf_NodeTermVariable(
								"y")));
		Assert.assertEquals(qs, new mf_SentenceQuantified("EXISTS", vars, cse));
	}

	@Test
	public void testQuantifiedSentenceWithPathologicalParanthising() {
		mf_i_Sentence qs = parser
				.parse("(( (EXISTS x,y  (King(x) AND (BrotherOf(x) = y)) ) ))");
		List<mf_NodeTermVariable> vars = new ArrayList<mf_NodeTermVariable>();
		vars.add(new mf_NodeTermVariable("x"));
		vars.add(new mf_NodeTermVariable("y"));
		mf_SentenceConnected cse = new mf_SentenceConnected("AND",
				getKingPredicate(new mf_NodeTermVariable("x")), new mf_SentenceAtomicTermEquality(
						getBrotherOfFunction(new mf_NodeTermVariable("x")), new mf_NodeTermVariable(
								"y")));
		Assert.assertEquals(qs, new mf_SentenceQuantified("EXISTS", vars, cse));
	}

	@Test
	public void testParseMultiArityFunctionEquality() {
		parser.setUpToParse("LegsOf(John,Saladin,Richard)");
		mf_i_NodeTerm f = parser.parseFunction();

		parser.setUpToParse("LegsOf(John,Saladin,Richard)");
		mf_i_NodeTerm f2 = parser.parseFunction();
		Assert.assertEquals(f, f2);
		Assert.assertEquals(3, ((mf_NodeTermFunction) f).getTerms().size());
	}

	@Test
	public void testConnectedImplication() {
		parser = new mf_Parser(mf_DomainExamples.weaponsDomain());
		parser.parse("((Missile(m) AND Owns(Nono,m)) => Sells(West , m ,Nono))");
	}

	//
	// PRIVATE METHODS
	//
	private mf_NodeTermFunction getBrotherOfFunction(mf_i_NodeTerm t) {
		List<mf_i_NodeTerm> l = new ArrayList<mf_i_NodeTerm>();
		l.add(t);
		return new mf_NodeTermFunction("BrotherOf", l);
	}

	private mf_NodeTermFunction getEnemyOfFunction() {
		List<mf_i_NodeTerm> l = new ArrayList<mf_i_NodeTerm>();
		l.add(new mf_SymbolConstant("Saladin"));
		return new mf_NodeTermFunction("EnemyOf", l);
	}

	private mf_NodeTermFunction getLegsOfFunction() {
		List<mf_i_NodeTerm> l = new ArrayList<mf_i_NodeTerm>();
		l.add(new mf_SymbolConstant("John"));
		l.add(new mf_SymbolConstant("Saladin"));
		l.add(new mf_SymbolConstant("Richard"));
		return new mf_NodeTermFunction("LegsOf", l);
	}

	private mf_Predicate getKingPredicate(mf_i_NodeTerm t) {
		List<mf_i_NodeTerm> l = new ArrayList<mf_i_NodeTerm>();
		l.add(t);
		return new mf_Predicate("King", l);
	}
}