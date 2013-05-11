package reuze.test;
//package aima.test.core.unit.logic.fol;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Assert;

import com.software.reuze.mf_KnowledgeBase;
import com.software.reuze.mf_KnowledgeBaseExamples;
import com.software.reuze.mf_NodeTermFunction;
import com.software.reuze.mf_NodeTermVariable;
import com.software.reuze.mf_Predicate;
import com.software.reuze.mf_SentenceAtomicTermEquality;
import com.software.reuze.mf_SentenceNot;
import com.software.reuze.mf_SymbolConstant;
import com.software.reuze.mf_i_InferenceProcedure;
import com.software.reuze.mf_i_InferenceResult;
import com.software.reuze.mf_i_NodeTerm;
import com.software.reuze.mf_i_Proof;


/*import aima.core.logic.fol.inference.InferenceProcedure;
import aima.core.logic.fol.inference.InferenceResult;
import aima.core.logic.fol.inference.proof.Proof;
import aima.core.logic.fol.kb.FOLKnowledgeBase;
import aima.core.logic.fol.kb.FOLKnowledgeBaseFactory;
import aima.core.logic.fol.parsing.ast.Constant;
import aima.core.logic.fol.parsing.ast.Function;
import aima.core.logic.fol.parsing.ast.NotSentence;
import aima.core.logic.fol.parsing.ast.Predicate;
import aima.core.logic.fol.parsing.ast.Term;
import aima.core.logic.fol.parsing.ast.TermEquality;
import aima.core.logic.fol.parsing.ast.Variable;*/

/**
 * @author Ciaran O'Reilly
 * 
 */
public abstract class CommonFOLInferenceProcedureTests {

	//
	// Protected Methods
	//
	protected void testDefiniteClauseKBKingsQueryCriminalXFalse(
			mf_i_InferenceProcedure infp) {
		mf_KnowledgeBase kkb = mf_KnowledgeBaseExamples
				.createKingsKnowledgeBase(infp);
		List<mf_i_NodeTerm> terms = new ArrayList<mf_i_NodeTerm>();
		terms.add(new mf_NodeTermVariable("x"));
		mf_Predicate query = new mf_Predicate("Criminal", terms);
		mf_i_InferenceResult answer = kkb.ask(query);
		Assert.assertTrue(null != answer);
		Assert.assertTrue(answer.isPossiblyFalse());
		Assert.assertFalse(answer.isTrue());
		Assert.assertFalse(answer.isUnknownDueToTimeout());
		Assert.assertFalse(answer.isPartialResultDueToTimeout());
		Assert.assertTrue(0 == answer.getProofs().size());
	}

	protected void testDefiniteClauseKBKingsQueryRichardEvilFalse(
			mf_i_InferenceProcedure infp) {
		mf_KnowledgeBase kkb = mf_KnowledgeBaseExamples
				.createKingsKnowledgeBase(infp);
		List<mf_i_NodeTerm> terms = new ArrayList<mf_i_NodeTerm>();
		terms.add(new mf_SymbolConstant("Richard"));
		mf_Predicate query = new mf_Predicate("Evil", terms);
		mf_i_InferenceResult answer = kkb.ask(query);
		Assert.assertTrue(null != answer);
		Assert.assertTrue(answer.isPossiblyFalse());
		Assert.assertFalse(answer.isTrue());
		Assert.assertFalse(answer.isUnknownDueToTimeout());
		Assert.assertFalse(answer.isPartialResultDueToTimeout());
		Assert.assertTrue(0 == answer.getProofs().size());
	}

	protected void testDefiniteClauseKBKingsQueryJohnEvilSucceeds(
			mf_i_InferenceProcedure infp) {
		mf_KnowledgeBase kkb = mf_KnowledgeBaseExamples
				.createKingsKnowledgeBase(infp);
		List<mf_i_NodeTerm> terms = new ArrayList<mf_i_NodeTerm>();
		terms.add(new mf_SymbolConstant("John"));
		mf_Predicate query = new mf_Predicate("Evil", terms);
		mf_i_InferenceResult answer = kkb.ask(query);

		Assert.assertTrue(null != answer);
		Assert.assertFalse(answer.isPossiblyFalse());
		Assert.assertTrue(answer.isTrue());
		Assert.assertFalse(answer.isUnknownDueToTimeout());
		Assert.assertFalse(answer.isPartialResultDueToTimeout());
		Assert.assertTrue(1 == answer.getProofs().size());
		Assert.assertTrue(0 == answer.getProofs().get(0).getAnswerBindings()
				.size());
	}

	protected void testDefiniteClauseKBKingsQueryEvilXReturnsJohnSucceeds(
			mf_i_InferenceProcedure infp) {
		mf_KnowledgeBase kkb = mf_KnowledgeBaseExamples
				.createKingsKnowledgeBase(infp);
		List<mf_i_NodeTerm> terms = new ArrayList<mf_i_NodeTerm>();
		terms.add(new mf_NodeTermVariable("x"));
		mf_Predicate query = new mf_Predicate("Evil", terms);
		mf_i_InferenceResult answer = kkb.ask(query);

		Assert.assertTrue(null != answer);
		Assert.assertFalse(answer.isPossiblyFalse());
		Assert.assertTrue(answer.isTrue());
		Assert.assertFalse(answer.isUnknownDueToTimeout());
		Assert.assertFalse(answer.isPartialResultDueToTimeout());
		Assert.assertTrue(1 == answer.getProofs().size());
		Assert.assertTrue(1 == answer.getProofs().get(0).getAnswerBindings()
				.size());
		Assert.assertEquals(new mf_SymbolConstant("John"), answer.getProofs().get(0)
				.getAnswerBindings().get(new mf_NodeTermVariable("x")));
	}

	protected void testDefiniteClauseKBKingsQueryKingXReturnsJohnAndRichardSucceeds(
			mf_i_InferenceProcedure infp) {
		mf_KnowledgeBase kkb = mf_KnowledgeBaseExamples
				.createKingsKnowledgeBase(infp);
		List<mf_i_NodeTerm> terms = new ArrayList<mf_i_NodeTerm>();
		terms.add(new mf_NodeTermVariable("x"));
		mf_Predicate query = new mf_Predicate("King", terms);
		mf_i_InferenceResult answer = kkb.ask(query);

		Assert.assertTrue(null != answer);
		Assert.assertFalse(answer.isPossiblyFalse());
		Assert.assertTrue(answer.isTrue());
		Assert.assertFalse(answer.isUnknownDueToTimeout());
		Assert.assertFalse(answer.isPartialResultDueToTimeout());
		Assert.assertTrue(2 == answer.getProofs().size());
		Assert.assertTrue(1 == answer.getProofs().get(0).getAnswerBindings()
				.size());
		Assert.assertTrue(1 == answer.getProofs().get(1).getAnswerBindings()
				.size());

		boolean gotJohn, gotRichard;
		gotJohn = gotRichard = false;
		mf_SymbolConstant cJohn = new mf_SymbolConstant("John");
		mf_SymbolConstant cRichard = new mf_SymbolConstant("Richard");
		for (mf_i_Proof p : answer.getProofs()) {
			Map<mf_NodeTermVariable, mf_i_NodeTerm> ans = p.getAnswerBindings();
			Assert.assertEquals(1, ans.size());
			if (cJohn.equals(ans.get(new mf_NodeTermVariable("x")))) {
				gotJohn = true;
			}
			if (cRichard.equals(ans.get(new mf_NodeTermVariable("x")))) {
				gotRichard = true;
			}
		}
		Assert.assertTrue(gotJohn);
		Assert.assertTrue(gotRichard);
	}

	protected void testDefiniteClauseKBWeaponsQueryCriminalXReturnsWestSucceeds(
			mf_i_InferenceProcedure infp) {
		mf_KnowledgeBase wkb = mf_KnowledgeBaseExamples
				.createWeaponsKnowledgeBase(infp);
		List<mf_i_NodeTerm> terms = new ArrayList<mf_i_NodeTerm>();
		terms.add(new mf_NodeTermVariable("x"));
		mf_Predicate query = new mf_Predicate("Criminal", terms);

		mf_i_InferenceResult answer = wkb.ask(query);

		Assert.assertTrue(null != answer);
		Assert.assertFalse(answer.isPossiblyFalse());
		Assert.assertTrue(answer.isTrue());
		Assert.assertFalse(answer.isUnknownDueToTimeout());
		Assert.assertFalse(answer.isPartialResultDueToTimeout());
		Assert.assertTrue(1 == answer.getProofs().size());
		Assert.assertTrue(1 == answer.getProofs().get(0).getAnswerBindings()
				.size());
		Assert.assertEquals(new mf_SymbolConstant("West"), answer.getProofs().get(0)
				.getAnswerBindings().get(new mf_NodeTermVariable("x")));
	}

	protected void testHornClauseKBRingOfThievesQuerySkisXReturnsNancyRedBertDrew(
			mf_i_InferenceProcedure infp) {
		mf_KnowledgeBase rotkb = mf_KnowledgeBaseExamples
				.createRingOfThievesKnowledgeBase(infp);
		List<mf_i_NodeTerm> terms = new ArrayList<mf_i_NodeTerm>();
		terms.add(new mf_NodeTermVariable("x"));
		mf_Predicate query = new mf_Predicate("Skis", terms);

		mf_i_InferenceResult answer = rotkb.ask(query);

		Assert.assertTrue(null != answer);
		Assert.assertFalse(answer.isPossiblyFalse());
		Assert.assertTrue(answer.isTrue());
		Assert.assertFalse(answer.isUnknownDueToTimeout());
		// DB can expand infinitely so is only partial.
		Assert.assertTrue(answer.isPartialResultDueToTimeout());
		Assert.assertEquals(4, answer.getProofs().size());
		Assert.assertEquals(1, answer.getProofs().get(0).getAnswerBindings()
				.size());
		Assert.assertEquals(1, answer.getProofs().get(1).getAnswerBindings()
				.size());
		Assert.assertEquals(1, answer.getProofs().get(2).getAnswerBindings()
				.size());
		Assert.assertEquals(1, answer.getProofs().get(3).getAnswerBindings()
				.size());

		List<mf_SymbolConstant> expected = new ArrayList<mf_SymbolConstant>();
		expected.add(new mf_SymbolConstant("Nancy"));
		expected.add(new mf_SymbolConstant("Red"));
		expected.add(new mf_SymbolConstant("Bert"));
		expected.add(new mf_SymbolConstant("Drew"));
		for (mf_i_Proof p : answer.getProofs()) {
			expected.remove(p.getAnswerBindings().get(new mf_NodeTermVariable("x")));
		}
		Assert.assertEquals(0, expected.size());
	}

	protected void testFullFOLKBLovesAnimalQueryKillsCuriosityTunaSucceeds(
			mf_i_InferenceProcedure infp, boolean expectedToTimeOut) {
		mf_KnowledgeBase akb = mf_KnowledgeBaseExamples
				.createLovesAnimalKnowledgeBase(infp);
		List<mf_i_NodeTerm> terms = new ArrayList<mf_i_NodeTerm>();
		terms.add(new mf_SymbolConstant("Curiosity"));
		terms.add(new mf_SymbolConstant("Tuna"));
		mf_Predicate query = new mf_Predicate("Kills", terms);

		mf_i_InferenceResult answer = akb.ask(query);
		Assert.assertTrue(null != answer);
		if (expectedToTimeOut) {
			Assert.assertFalse(answer.isPossiblyFalse());
			Assert.assertFalse(answer.isTrue());
			Assert.assertTrue(answer.isUnknownDueToTimeout());
			Assert.assertFalse(answer.isPartialResultDueToTimeout());
			Assert.assertTrue(0 == answer.getProofs().size());
		} else {
			Assert.assertFalse(answer.isPossiblyFalse());
			Assert.assertTrue(answer.isTrue());
			Assert.assertFalse(answer.isUnknownDueToTimeout());
			Assert.assertFalse(answer.isPartialResultDueToTimeout());
			Assert.assertTrue(1 == answer.getProofs().size());
			Assert.assertTrue(0 == answer.getProofs().get(0)
					.getAnswerBindings().size());
		}
	}

	protected void testFullFOLKBLovesAnimalQueryNotKillsJackTunaSucceeds(
			mf_i_InferenceProcedure infp, boolean expectedToTimeOut) {
		mf_KnowledgeBase akb = mf_KnowledgeBaseExamples
				.createLovesAnimalKnowledgeBase(infp);
		List<mf_i_NodeTerm> terms = new ArrayList<mf_i_NodeTerm>();
		terms.add(new mf_SymbolConstant("Jack"));
		terms.add(new mf_SymbolConstant("Tuna"));
		mf_SentenceNot query = new mf_SentenceNot(new mf_Predicate("Kills", terms));

		mf_i_InferenceResult answer = akb.ask(query);

		Assert.assertTrue(null != answer);
		if (expectedToTimeOut) {
			Assert.assertFalse(answer.isPossiblyFalse());
			Assert.assertFalse(answer.isTrue());
			Assert.assertTrue(answer.isUnknownDueToTimeout());
			Assert.assertFalse(answer.isPartialResultDueToTimeout());
			Assert.assertTrue(0 == answer.getProofs().size());
		} else {
			Assert.assertFalse(answer.isPossiblyFalse());
			Assert.assertTrue(answer.isTrue());
			Assert.assertFalse(answer.isUnknownDueToTimeout());
			Assert.assertFalse(answer.isPartialResultDueToTimeout());
			Assert.assertTrue(1 == answer.getProofs().size());
			Assert.assertTrue(0 == answer.getProofs().get(0)
					.getAnswerBindings().size());
		}
	}

	protected void testFullFOLKBLovesAnimalQueryKillsJackTunaFalse(
			mf_i_InferenceProcedure infp, boolean expectedToTimeOut) {
		mf_KnowledgeBase akb = mf_KnowledgeBaseExamples
				.createLovesAnimalKnowledgeBase(infp);
		List<mf_i_NodeTerm> terms = new ArrayList<mf_i_NodeTerm>();
		terms.add(new mf_SymbolConstant("Jack"));
		terms.add(new mf_SymbolConstant("Tuna"));
		mf_Predicate query = new mf_Predicate("Kills", terms);

		mf_i_InferenceResult answer = akb.ask(query);

		Assert.assertTrue(null != answer);
		if (expectedToTimeOut) {
			Assert.assertFalse(answer.isPossiblyFalse());
			Assert.assertFalse(answer.isTrue());
			Assert.assertTrue(answer.isUnknownDueToTimeout());
			Assert.assertFalse(answer.isPartialResultDueToTimeout());
			Assert.assertTrue(0 == answer.getProofs().size());
		} else {
			Assert.assertTrue(answer.isPossiblyFalse());
			Assert.assertFalse(answer.isTrue());
			Assert.assertFalse(answer.isUnknownDueToTimeout());
			Assert.assertFalse(answer.isPartialResultDueToTimeout());
			Assert.assertTrue(0 == answer.getProofs().size());
		}
	}

	protected void testEqualityAxiomsKBabcAEqualsCSucceeds(
			mf_i_InferenceProcedure infp) {
		mf_KnowledgeBase akb = mf_KnowledgeBaseExamples
				.createABCEqualityKnowledgeBase(infp, true);

		mf_SentenceAtomicTermEquality query = new mf_SentenceAtomicTermEquality(new mf_SymbolConstant("A"), new mf_SymbolConstant(
				"C"));

		mf_i_InferenceResult answer = akb.ask(query);

		Assert.assertTrue(null != answer);
		Assert.assertFalse(answer.isPossiblyFalse());
		Assert.assertTrue(answer.isTrue());
		Assert.assertFalse(answer.isUnknownDueToTimeout());
		Assert.assertFalse(answer.isPartialResultDueToTimeout());
		Assert.assertTrue(1 == answer.getProofs().size());
		Assert.assertTrue(0 == answer.getProofs().get(0).getAnswerBindings()
				.size());
	}

	protected void testEqualityAndSubstitutionAxiomsKBabcdFFASucceeds(
			mf_i_InferenceProcedure infp) {
		mf_KnowledgeBase akb = mf_KnowledgeBaseExamples
				.createABCDEqualityAndSubstitutionKnowledgeBase(infp, true);

		List<mf_i_NodeTerm> terms = new ArrayList<mf_i_NodeTerm>();
		terms.add(new mf_SymbolConstant("A"));
		mf_NodeTermFunction fa = new mf_NodeTermFunction("F", terms);
		terms = new ArrayList<mf_i_NodeTerm>();
		terms.add(fa);
		mf_SentenceAtomicTermEquality query = new mf_SentenceAtomicTermEquality(new mf_NodeTermFunction("F", terms),
				new mf_SymbolConstant("A"));

		mf_i_InferenceResult answer = akb.ask(query);

		Assert.assertTrue(null != answer);
		Assert.assertFalse(answer.isPossiblyFalse());
		Assert.assertTrue(answer.isTrue());
		Assert.assertFalse(answer.isUnknownDueToTimeout());
		Assert.assertFalse(answer.isPartialResultDueToTimeout());
		Assert.assertTrue(1 == answer.getProofs().size());
		Assert.assertTrue(0 == answer.getProofs().get(0).getAnswerBindings()
				.size());
	}

	protected void testEqualityAndSubstitutionAxiomsKBabcdPDSucceeds(
			mf_i_InferenceProcedure infp) {
		mf_KnowledgeBase akb = mf_KnowledgeBaseExamples
				.createABCDEqualityAndSubstitutionKnowledgeBase(infp, true);

		List<mf_i_NodeTerm> terms = new ArrayList<mf_i_NodeTerm>();
		terms.add(new mf_SymbolConstant("D"));
		mf_Predicate query = new mf_Predicate("P", terms);

		mf_i_InferenceResult answer = akb.ask(query);

		Assert.assertTrue(null != answer);
		Assert.assertFalse(answer.isPossiblyFalse());
		Assert.assertTrue(answer.isTrue());
		Assert.assertFalse(answer.isUnknownDueToTimeout());
		Assert.assertFalse(answer.isPartialResultDueToTimeout());
		Assert.assertTrue(1 == answer.getProofs().size());
		Assert.assertTrue(0 == answer.getProofs().get(0).getAnswerBindings()
				.size());
	}

	protected void testEqualityAndSubstitutionAxiomsKBabcdPFFASucceeds(
			mf_i_InferenceProcedure infp, boolean expectedToTimeOut) {
		mf_KnowledgeBase akb = mf_KnowledgeBaseExamples
				.createABCDEqualityAndSubstitutionKnowledgeBase(infp, true);

		List<mf_i_NodeTerm> terms = new ArrayList<mf_i_NodeTerm>();
		terms.add(new mf_SymbolConstant("A"));
		mf_NodeTermFunction fa = new mf_NodeTermFunction("F", terms);
		terms = new ArrayList<mf_i_NodeTerm>();
		terms.add(fa);
		mf_NodeTermFunction ffa = new mf_NodeTermFunction("F", terms);
		terms = new ArrayList<mf_i_NodeTerm>();
		terms.add(ffa);
		mf_Predicate query = new mf_Predicate("P", terms);

		mf_i_InferenceResult answer = akb.ask(query);

		if (expectedToTimeOut) {
			Assert.assertFalse(answer.isPossiblyFalse());
			Assert.assertFalse(answer.isTrue());
			Assert.assertTrue(answer.isUnknownDueToTimeout());
			Assert.assertFalse(answer.isPartialResultDueToTimeout());
			Assert.assertTrue(0 == answer.getProofs().size());
		} else {
			Assert.assertTrue(null != answer);
			Assert.assertFalse(answer.isPossiblyFalse());
			Assert.assertTrue(answer.isTrue());
			Assert.assertFalse(answer.isUnknownDueToTimeout());
			Assert.assertFalse(answer.isPartialResultDueToTimeout());
			Assert.assertTrue(1 == answer.getProofs().size());
			Assert.assertTrue(0 == answer.getProofs().get(0)
					.getAnswerBindings().size());
		}
	}

	protected void testEqualityNoAxiomsKBabcAEqualsCSucceeds(
			mf_i_InferenceProcedure infp, boolean expectedToFail) {
		mf_KnowledgeBase akb = mf_KnowledgeBaseExamples
				.createABCEqualityKnowledgeBase(infp, false);

		mf_SentenceAtomicTermEquality query = new mf_SentenceAtomicTermEquality(new mf_SymbolConstant("A"), new mf_SymbolConstant(
				"C"));

		mf_i_InferenceResult answer = akb.ask(query);

		Assert.assertTrue(null != answer);
		if (expectedToFail) {
			Assert.assertTrue(answer.isPossiblyFalse());
			Assert.assertFalse(answer.isTrue());
			Assert.assertFalse(answer.isUnknownDueToTimeout());
			Assert.assertFalse(answer.isPartialResultDueToTimeout());
			Assert.assertTrue(0 == answer.getProofs().size());
		} else {
			Assert.assertFalse(answer.isPossiblyFalse());
			Assert.assertTrue(answer.isTrue());
			Assert.assertFalse(answer.isUnknownDueToTimeout());
			Assert.assertFalse(answer.isPartialResultDueToTimeout());
			Assert.assertTrue(1 == answer.getProofs().size());
			Assert.assertTrue(0 == answer.getProofs().get(0)
					.getAnswerBindings().size());
		}
	}

	protected void testEqualityAndSubstitutionNoAxiomsKBabcdFFASucceeds(
			mf_i_InferenceProcedure infp, boolean expectedToFail) {
		mf_KnowledgeBase akb = mf_KnowledgeBaseExamples
				.createABCDEqualityAndSubstitutionKnowledgeBase(infp, false);

		List<mf_i_NodeTerm> terms = new ArrayList<mf_i_NodeTerm>();
		terms.add(new mf_SymbolConstant("A"));
		mf_NodeTermFunction fa = new mf_NodeTermFunction("F", terms);
		terms = new ArrayList<mf_i_NodeTerm>();
		terms.add(fa);
		mf_SentenceAtomicTermEquality query = new mf_SentenceAtomicTermEquality(new mf_NodeTermFunction("F", terms),
				new mf_SymbolConstant("A"));

		mf_i_InferenceResult answer = akb.ask(query);

		Assert.assertTrue(null != answer);
		if (expectedToFail) {
			Assert.assertTrue(answer.isPossiblyFalse());
			Assert.assertFalse(answer.isTrue());
			Assert.assertFalse(answer.isUnknownDueToTimeout());
			Assert.assertFalse(answer.isPartialResultDueToTimeout());
			Assert.assertTrue(0 == answer.getProofs().size());
		} else {
			Assert.assertFalse(answer.isPossiblyFalse());
			Assert.assertTrue(answer.isTrue());
			Assert.assertFalse(answer.isUnknownDueToTimeout());
			Assert.assertFalse(answer.isPartialResultDueToTimeout());
			Assert.assertTrue(1 == answer.getProofs().size());
			Assert.assertTrue(0 == answer.getProofs().get(0)
					.getAnswerBindings().size());
		}
	}

	protected void testEqualityAndSubstitutionNoAxiomsKBabcdPDSucceeds(
			mf_i_InferenceProcedure infp, boolean expectedToFail) {
		mf_KnowledgeBase akb = mf_KnowledgeBaseExamples
				.createABCDEqualityAndSubstitutionKnowledgeBase(infp, false);

		List<mf_i_NodeTerm> terms = new ArrayList<mf_i_NodeTerm>();
		terms.add(new mf_SymbolConstant("D"));
		mf_Predicate query = new mf_Predicate("P", terms);

		mf_i_InferenceResult answer = akb.ask(query);

		Assert.assertTrue(null != answer);
		if (expectedToFail) {
			Assert.assertTrue(answer.isPossiblyFalse());
			Assert.assertFalse(answer.isTrue());
			Assert.assertFalse(answer.isUnknownDueToTimeout());
			Assert.assertFalse(answer.isPartialResultDueToTimeout());
			Assert.assertTrue(0 == answer.getProofs().size());
		} else {
			Assert.assertFalse(answer.isPossiblyFalse());
			Assert.assertTrue(answer.isTrue());
			Assert.assertFalse(answer.isUnknownDueToTimeout());
			Assert.assertFalse(answer.isPartialResultDueToTimeout());
			Assert.assertTrue(1 == answer.getProofs().size());
			Assert.assertTrue(0 == answer.getProofs().get(0)
					.getAnswerBindings().size());
		}
	}

	protected void testEqualityAndSubstitutionNoAxiomsKBabcdPFFASucceeds(
			mf_i_InferenceProcedure infp, boolean expectedToFail) {
		mf_KnowledgeBase akb = mf_KnowledgeBaseExamples
				.createABCDEqualityAndSubstitutionKnowledgeBase(infp, false);

		List<mf_i_NodeTerm> terms = new ArrayList<mf_i_NodeTerm>();
		terms.add(new mf_SymbolConstant("A"));
		mf_NodeTermFunction fa = new mf_NodeTermFunction("F", terms);
		terms = new ArrayList<mf_i_NodeTerm>();
		terms.add(fa);
		mf_NodeTermFunction ffa = new mf_NodeTermFunction("F", terms);
		terms = new ArrayList<mf_i_NodeTerm>();
		terms.add(ffa);
		mf_Predicate query = new mf_Predicate("P", terms);

		mf_i_InferenceResult answer = akb.ask(query);

		Assert.assertTrue(null != answer);
		if (expectedToFail) {
			Assert.assertTrue(answer.isPossiblyFalse());
			Assert.assertFalse(answer.isTrue());
			Assert.assertFalse(answer.isUnknownDueToTimeout());
			Assert.assertFalse(answer.isPartialResultDueToTimeout());
			Assert.assertTrue(0 == answer.getProofs().size());
		} else {
			Assert.assertFalse(answer.isPossiblyFalse());
			Assert.assertTrue(answer.isTrue());
			Assert.assertFalse(answer.isUnknownDueToTimeout());
			Assert.assertFalse(answer.isPartialResultDueToTimeout());
			Assert.assertTrue(1 == answer.getProofs().size());
			Assert.assertTrue(0 == answer.getProofs().get(0)
					.getAnswerBindings().size());
		}
	}
}