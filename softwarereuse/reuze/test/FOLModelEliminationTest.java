package reuze.test;
//package aima.test.core.unit.logic.fol.inference;

import org.junit.Test;

import com.software.reuze.mf_InferenceProcedureModelElimination;


//import aima.core.logic.fol.inference.FOLModelElimination;
//import aima.test.core.unit.logic.fol.CommonFOLInferenceProcedureTests;

/**
 * @author Ciaran O'Reilly
 * 
 */
public class FOLModelEliminationTest extends CommonFOLInferenceProcedureTests {

	@Test
	public void testDefiniteClauseKBKingsQueryCriminalXFalse() {
		testDefiniteClauseKBKingsQueryCriminalXFalse(new mf_InferenceProcedureModelElimination());
	}

	@Test
	public void testDefiniteClauseKBKingsQueryRichardEvilFalse() {
		testDefiniteClauseKBKingsQueryRichardEvilFalse(new mf_InferenceProcedureModelElimination());
	}

	@Test
	public void testDefiniteClauseKBKingsQueryJohnEvilSucceeds() {
		testDefiniteClauseKBKingsQueryJohnEvilSucceeds(new mf_InferenceProcedureModelElimination());
	}

	@Test
	public void testDefiniteClauseKBKingsQueryEvilXReturnsJohnSucceeds() {
		testDefiniteClauseKBKingsQueryEvilXReturnsJohnSucceeds(new mf_InferenceProcedureModelElimination());
	}

	@Test
	public void testDefiniteClauseKBKingsQueryKingXReturnsJohnAndRichardSucceeds() {
		testDefiniteClauseKBKingsQueryKingXReturnsJohnAndRichardSucceeds(new mf_InferenceProcedureModelElimination());
	}

	@Test
	public void testDefiniteClauseKBWeaponsQueryCriminalXReturnsWestSucceeds() {
		testDefiniteClauseKBWeaponsQueryCriminalXReturnsWestSucceeds(new mf_InferenceProcedureModelElimination());
	}

	@Test
	public void testHornClauseKBRingOfThievesQuerySkisXReturnsNancyRedBertDrew() {
		// This KB ends up being infinite when resolving, however 2
		// seconds is more than enough to extract the 4 answers
		// that are expected
		testHornClauseKBRingOfThievesQuerySkisXReturnsNancyRedBertDrew(new mf_InferenceProcedureModelElimination(
				2 * 1000));
	}

	@Test
	public void testFullFOLKBLovesAnimalQueryKillsCuriosityTunaSucceeds() {
		testFullFOLKBLovesAnimalQueryKillsCuriosityTunaSucceeds(
				new mf_InferenceProcedureModelElimination(), false);
	}

	@Test
	public void testFullFOLKBLovesAnimalQueryNotKillsJackTunaSucceeds() {
		testFullFOLKBLovesAnimalQueryNotKillsJackTunaSucceeds(
				new mf_InferenceProcedureModelElimination(), false);
	}

	@Test
	public void testFullFOLKBLovesAnimalQueryKillsJackTunaFalse() {
		// Note: While the KB expands infinitely, the answer
		// search for this bottoms out indicating the
		// KB does not entail the fact.
		testFullFOLKBLovesAnimalQueryKillsJackTunaFalse(
				new mf_InferenceProcedureModelElimination(), false);
	}

	@Test
	public void testEqualityAxiomsKBabcAEqualsCSucceeds() {
		testEqualityAxiomsKBabcAEqualsCSucceeds(new mf_InferenceProcedureModelElimination());
	}

	@Test
	public void testEqualityAndSubstitutionAxiomsKBabcdFFASucceeds() {
		testEqualityAndSubstitutionAxiomsKBabcdFFASucceeds(new mf_InferenceProcedureModelElimination());
	}

	@Test
	public void testEqualityAndSubstitutionAxiomsKBabcdPDSucceeds() {
		testEqualityAndSubstitutionAxiomsKBabcdPDSucceeds(new mf_InferenceProcedureModelElimination());
	}

	@Test
	public void testEqualityAndSubstitutionAxiomsKBabcdPFFASucceeds() {
		testEqualityAndSubstitutionAxiomsKBabcdPFFASucceeds(
				new mf_InferenceProcedureModelElimination(), false);
	}

	@Test
	public void testEqualityNoAxiomsKBabcAEqualsCSucceeds() {
		testEqualityNoAxiomsKBabcAEqualsCSucceeds(new mf_InferenceProcedureModelElimination(),
				true);
	}

	@Test
	public void testEqualityAndSubstitutionNoAxiomsKBabcdFFASucceeds() {
		testEqualityAndSubstitutionNoAxiomsKBabcdFFASucceeds(
				new mf_InferenceProcedureModelElimination(), true);
	}

	@Test
	public void testEqualityAndSubstitutionNoAxiomsKBabcdPDSucceeds() {
		testEqualityAndSubstitutionNoAxiomsKBabcdPDSucceeds(
				new mf_InferenceProcedureModelElimination(), true);
	}

	@Test
	public void testEqualityAndSubstitutionNoAxiomsKBabcdPFFASucceeds() {
		testEqualityAndSubstitutionNoAxiomsKBabcdPFFASucceeds(
				new mf_InferenceProcedureModelElimination(), true);
	}
}