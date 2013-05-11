package reuze.test;
//package aima.test.core.unit.logic.fol.inference;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.software.reuze.mf_CNF;
import com.software.reuze.mf_CNFConverter;
import com.software.reuze.mf_Clause;
import com.software.reuze.mf_ClauseSimplifierDefault;
import com.software.reuze.mf_Domain;
import com.software.reuze.mf_KnowledgeBase;
import com.software.reuze.mf_OTTERLikeTheoremProver;
import com.software.reuze.mf_Parser;
import com.software.reuze.mf_SentenceAtomicTermEquality;
import com.software.reuze.mf_i_InferenceResult;
import com.software.reuze.mf_i_Sentence;


/*import aima.core.logic.fol.CNFConverter;
import aima.core.logic.fol.domain.FOLDomain;
import aima.core.logic.fol.inference.FOLOTTERLikeTheoremProver;
import aima.core.logic.fol.inference.InferenceResult;
import aima.core.logic.fol.inference.otter.defaultimpl.DefaultClauseSimplifier;
import aima.core.logic.fol.kb.FOLKnowledgeBase;
import aima.core.logic.fol.kb.data.CNF;
import aima.core.logic.fol.kb.data.Clause;
import aima.core.logic.fol.parsing.FOLParser;
import aima.core.logic.fol.parsing.ast.FOSentence;
import aima.core.logic.fol.parsing.ast.TermEquality;
import aima.test.core.unit.logic.fol.CommonFOLInferenceProcedureTests;*/

/**
 * @author Ciaran O'Reilly
 * 
 */
public class FOLOTTERLikeTheoremProverTest extends
		CommonFOLInferenceProcedureTests {

	@Test
	public void testDefaultClauseSimplifier() {
		mf_Domain domain = new mf_Domain();
		domain.addConstant("ZERO");
		domain.addConstant("ONE");
		domain.addPredicate("P");
		domain.addFunction("Plus");
		domain.addFunction("Power");

		mf_Parser parser = new mf_Parser(domain);

		List<mf_SentenceAtomicTermEquality> rewrites = new ArrayList<mf_SentenceAtomicTermEquality>();
		rewrites.add((mf_SentenceAtomicTermEquality) parser.parse("Plus(x, ZERO) = x"));
		rewrites.add((mf_SentenceAtomicTermEquality) parser.parse("Plus(ZERO, x) = x"));
		rewrites.add((mf_SentenceAtomicTermEquality) parser.parse("Power(x, ONE) = x"));
		rewrites.add((mf_SentenceAtomicTermEquality) parser.parse("Power(x, ZERO) = ONE"));
		mf_ClauseSimplifierDefault simplifier = new mf_ClauseSimplifierDefault(
				rewrites);

		mf_i_Sentence s1 = parser
				.parse("((P(Plus(y,ZERO),Plus(ZERO,y)) OR P(Power(y, ONE),Power(y,ZERO))) OR P(Power(y,ZERO),Plus(y,ZERO)))");

		mf_CNFConverter cnfConverter = new mf_CNFConverter(parser);

		mf_CNF cnf = cnfConverter.convertToCNF(s1);

		Assert.assertEquals(1, cnf.getNumberOfClauses());

		mf_Clause simplified = simplifier.simplify(cnf.getConjunctionOfClauses()
				.get(0));

		Assert.assertEquals("[P(y,y), P(y,ONE), P(ONE,y)]",
				simplified.toString());
	}

	// This tests to ensure the OTTERLike theorem prover
	// uses subsumption correctly so that it exhausts
	// its search space.
	@Test
	public void testExhaustsSearchSpace() {
		// Taken from AIMA pg 679
		mf_Domain domain = new mf_Domain();
		domain.addPredicate("alternate");
		domain.addPredicate("bar");
		domain.addPredicate("fri_sat");
		domain.addPredicate("hungry");
		domain.addPredicate("patrons");
		domain.addPredicate("price");
		domain.addPredicate("raining");
		domain.addPredicate("reservation");
		domain.addPredicate("type");
		domain.addPredicate("wait_estimate");
		domain.addPredicate("will_wait");
		domain.addConstant("Some");
		domain.addConstant("Full");
		domain.addConstant("French");
		domain.addConstant("Thai");
		domain.addConstant("Burger");
		domain.addConstant("$");
		domain.addConstant("_30_60");
		domain.addConstant("X0");
		mf_Parser parser = new mf_Parser(domain);

		// The hypothesis
		String c1 = "patrons(v,Some)";
		String c2 = "patrons(v,Full) AND (hungry(v) AND type(v,French))";
		String c3 = "patrons(v,Full) AND (hungry(v) AND (type(v,Thai) AND fri_sat(v)))";
		String c4 = "patrons(v,Full) AND (hungry(v) AND type(v,Burger))";
		String sh = "FORALL v (will_wait(v) <=> (" + c1 + " OR (" + c2
				+ " OR (" + c3 + " OR (" + c4 + ")))))";

		mf_i_Sentence hypothesis = parser.parse(sh);
		mf_i_Sentence desc = parser
				.parse("(((((((((alternate(X0) AND NOT(bar(X0))) AND NOT(fri_sat(X0))) AND hungry(X0)) AND patrons(X0,Full)) AND price(X0,$)) AND NOT(raining(X0))) AND NOT(reservation(X0))) AND type(X0,Thai)) AND wait_estimate(X0,_30_60))");
		mf_i_Sentence classification = parser.parse("will_wait(X0)");

		mf_KnowledgeBase kb = new mf_KnowledgeBase(domain,
				new mf_OTTERLikeTheoremProver(false));

		kb.tell(hypothesis);
		kb.tell(desc);

		mf_i_InferenceResult ir = kb.ask(classification);

		Assert.assertFalse(ir.isTrue());
		Assert.assertTrue(ir.isPossiblyFalse());
		Assert.assertFalse(ir.isUnknownDueToTimeout());
		Assert.assertFalse(ir.isPartialResultDueToTimeout());
		Assert.assertEquals(0, ir.getProofs().size());
	}

	@Test
	public void testDefiniteClauseKBKingsQueryCriminalXFalse() {
		testDefiniteClauseKBKingsQueryCriminalXFalse(new mf_OTTERLikeTheoremProver(
				false));
	}

	@Test
	public void testDefiniteClauseKBKingsQueryRichardEvilFalse() {
		testDefiniteClauseKBKingsQueryRichardEvilFalse(new mf_OTTERLikeTheoremProver(
				false));
	}

	@Test
	public void testDefiniteClauseKBKingsQueryJohnEvilSucceeds() {
		testDefiniteClauseKBKingsQueryJohnEvilSucceeds(new mf_OTTERLikeTheoremProver(
				false));
	}

	@Test
	public void testDefiniteClauseKBKingsQueryEvilXReturnsJohnSucceeds() {
		testDefiniteClauseKBKingsQueryEvilXReturnsJohnSucceeds(new mf_OTTERLikeTheoremProver(
				false));
	}

	@Test
	public void testDefiniteClauseKBKingsQueryKingXReturnsJohnAndRichardSucceeds() {
		testDefiniteClauseKBKingsQueryKingXReturnsJohnAndRichardSucceeds(new mf_OTTERLikeTheoremProver(
				false));
	}

	@Test
	public void testDefiniteClauseKBWeaponsQueryCriminalXReturnsWestSucceeds() {
		testDefiniteClauseKBWeaponsQueryCriminalXReturnsWestSucceeds(new mf_OTTERLikeTheoremProver(
				false));
	}

	@Test
	public void testHornClauseKBRingOfThievesQuerySkisXReturnsNancyRedBertDrew() {
		// This KB ends up being infinite when resolving, however 2
		// seconds is more than enough to extract the 4 answers
		// that are expected
		testHornClauseKBRingOfThievesQuerySkisXReturnsNancyRedBertDrew(new mf_OTTERLikeTheoremProver(
				2 * 1000, false));
	}

	@Test
	public void testFullFOLKBLovesAnimalQueryKillsCuriosityTunaSucceeds() {
		testFullFOLKBLovesAnimalQueryKillsCuriosityTunaSucceeds(
				new mf_OTTERLikeTheoremProver(false), false);
	}

	@Test
	public void testFullFOLKBLovesAnimalQueryNotKillsJackTunaSucceeds() {
		testFullFOLKBLovesAnimalQueryNotKillsJackTunaSucceeds(
				new mf_OTTERLikeTheoremProver(false), false);
	}

	@Test
	public void testFullFOLKBLovesAnimalQueryKillsJackTunaFalse() {
		// This query will not return using OTTER Like resolution
		// as keep expanding clauses through resolution for this KB.
		testFullFOLKBLovesAnimalQueryKillsJackTunaFalse(
				new mf_OTTERLikeTheoremProver(false), true);
	}

	@Test
	public void testEqualityAxiomsKBabcAEqualsCSucceeds() {
		testEqualityAxiomsKBabcAEqualsCSucceeds(new mf_OTTERLikeTheoremProver(
				false));
	}

	@Test
	public void testEqualityAndSubstitutionAxiomsKBabcdFFASucceeds() {
		testEqualityAndSubstitutionAxiomsKBabcdFFASucceeds(new mf_OTTERLikeTheoremProver(
				false));
	}

	@Test
	public void testEqualityAndSubstitutionAxiomsKBabcdPDSucceeds() {
		testEqualityAndSubstitutionAxiomsKBabcdPDSucceeds(new mf_OTTERLikeTheoremProver(
				false));
	}

	@Test
	public void testEqualityAndSubstitutionAxiomsKBabcdPFFASucceeds() {
		testEqualityAndSubstitutionAxiomsKBabcdPFFASucceeds(
				new mf_OTTERLikeTheoremProver(false), false);
	}

	@Test
	public void testEqualityNoAxiomsKBabcAEqualsCSucceeds() {
		testEqualityNoAxiomsKBabcAEqualsCSucceeds(
				new mf_OTTERLikeTheoremProver(true), false);
	}

	@Test
	public void testEqualityAndSubstitutionNoAxiomsKBabcdFFASucceeds() {
		testEqualityAndSubstitutionNoAxiomsKBabcdFFASucceeds(
				new mf_OTTERLikeTheoremProver(true), false);
	}

	@Test
	public void testEqualityAndSubstitutionNoAxiomsKBabcdPDSucceeds() {
		testEqualityAndSubstitutionNoAxiomsKBabcdPDSucceeds(
				new mf_OTTERLikeTheoremProver(true), false);
	}

	@Test
	public void testEqualityAndSubstitutionNoAxiomsKBabcdPFFASucceeds() {
		testEqualityAndSubstitutionNoAxiomsKBabcdPFFASucceeds(
				new mf_OTTERLikeTheoremProver(true), false);
	}
}