package reuze.test;
//package aima.test.core.unit.logic.fol.inference;

import org.junit.Ignore;
import org.junit.Test;

import com.software.reuze.mf_InferenceTFMResolution;


//import aima.core.logic.fol.inference.FOLTFMResolution;
//import aima.test.core.unit.logic.fol.CommonFOLInferenceProcedureTests;

/**
 * @author Ciaran O'Reilly
 * 
 */
public class FOLTFMResolutionTest extends CommonFOLInferenceProcedureTests {

	@Test
	public void testDefiniteClauseKBKingsQueryCriminalXFalse() {
		testDefiniteClauseKBKingsQueryCriminalXFalse(new mf_InferenceTFMResolution());
	}

	@Test
	public void testDefiniteClauseKBKingsQueryRichardEvilFalse() {
		testDefiniteClauseKBKingsQueryRichardEvilFalse(new mf_InferenceTFMResolution());
	}

	@Test
	public void testDefiniteClauseKBKingsQueryJohnEvilSucceeds() {
		testDefiniteClauseKBKingsQueryJohnEvilSucceeds(new mf_InferenceTFMResolution());
	}

	@Test
	public void testDefiniteClauseKBKingsQueryEvilXReturnsJohnSucceeds() {
		testDefiniteClauseKBKingsQueryEvilXReturnsJohnSucceeds(new mf_InferenceTFMResolution());
	}

	@Test
	public void testDefiniteClauseKBKingsQueryKingXReturnsJohnAndRichardSucceeds() {
		testDefiniteClauseKBKingsQueryKingXReturnsJohnAndRichardSucceeds(new mf_InferenceTFMResolution());
	}

	@Test
	public void testDefiniteClauseKBWeaponsQueryCriminalXReturnsWestSucceeds() {
		testDefiniteClauseKBWeaponsQueryCriminalXReturnsWestSucceeds(new mf_InferenceTFMResolution());
	}

	@Test
	public void testHornClauseKBRingOfThievesQuerySkisXReturnsNancyRedBertDrew() {
		// The clauses in this KB can keep creating resolvents infinitely,
		// therefore give it 20 seconds to find the 4 answers to this, should
		// be more than enough.
		testHornClauseKBRingOfThievesQuerySkisXReturnsNancyRedBertDrew(new mf_InferenceTFMResolution(
				40 * 1000));
	}

	@Test
	public void testFullFOLKBLovesAnimalQueryKillsCuriosityTunaSucceeds() {
		// 10 seconds should be more than plenty for this query to finish.
		testFullFOLKBLovesAnimalQueryKillsCuriosityTunaSucceeds(
				new mf_InferenceTFMResolution(10 * 1000), false);
	}

	@Test
	public void testFullFOLKBLovesAnimalQueryNotKillsJackTunaSucceeds() {
		// 10 seconds should be more than plenty for this query to finish.
		testFullFOLKBLovesAnimalQueryNotKillsJackTunaSucceeds(
				new mf_InferenceTFMResolution(10 * 1000), false);
	}

	@Test
	public void testFullFOLKBLovesAnimalQueryKillsJackTunaFalse() {
		// This query will not return using TFM as keep expanding
		// clauses through resolution for this KB.
		testFullFOLKBLovesAnimalQueryKillsJackTunaFalse(new mf_InferenceTFMResolution(
				10 * 1000), true);
	}

	@Test
	public void testEqualityAxiomsKBabcAEqualsCSucceeds() {
		testEqualityAxiomsKBabcAEqualsCSucceeds(new mf_InferenceTFMResolution(10 * 1000));
	}

	@Test
	public void testEqualityAndSubstitutionAxiomsKBabcdFFASucceeds() {
		testEqualityAndSubstitutionAxiomsKBabcdFFASucceeds(new mf_InferenceTFMResolution(
				40 * 1000));
	}

	// Note: Requires VM arguments to be:
	// -Xms256m -Xmx1024m
	// due to the amount of memory it uses.
	// Therefore, ignore by default as people don't normally set this.
	@Ignore
	@Test
	public void testEqualityAndSubstitutionAxiomsKBabcdPDSucceeds() {
		testEqualityAndSubstitutionAxiomsKBabcdPDSucceeds(new mf_InferenceTFMResolution(
				10 * 1000));
	}

	@Test
	public void testEqualityAndSubstitutionAxiomsKBabcdPFFASucceeds() {
		// TFM is unable to find the correct answer to this in a reasonable
		// amount of time for a JUnit test.
		testEqualityAndSubstitutionAxiomsKBabcdPFFASucceeds(
				new mf_InferenceTFMResolution(10 * 1000), true);
	}

	@Test
	public void testEqualityNoAxiomsKBabcAEqualsCSucceeds() {
		testEqualityNoAxiomsKBabcAEqualsCSucceeds(new mf_InferenceTFMResolution(
				10 * 1000), true);
	}

	@Test
	public void testEqualityAndSubstitutionNoAxiomsKBabcdFFASucceeds() {
		testEqualityAndSubstitutionNoAxiomsKBabcdFFASucceeds(
				new mf_InferenceTFMResolution(10 * 1000), true);
	}

	@Test
	public void testEqualityAndSubstitutionNoAxiomsKBabcdPDSucceeds() {
		testEqualityAndSubstitutionNoAxiomsKBabcdPDSucceeds(
				new mf_InferenceTFMResolution(10 * 1000), true);
	}

	@Test
	public void testEqualityAndSubstitutionNoAxiomsKBabcdPFFASucceeds() {
		testEqualityAndSubstitutionNoAxiomsKBabcdPFFASucceeds(
				new mf_InferenceTFMResolution(10 * 1000), true);
	}
}