package reuze.test;
//package aima.test.core.unit.logic.fol.inference;

import org.junit.Test;

import com.software.reuze.mf_InferenceProcedureFCAsk;


//import aima.core.logic.fol.inference.FOLFCAsk;
//import aima.test.core.unit.logic.fol.CommonFOLInferenceProcedureTests;

/**
 * @author Ciaran O'Reilly
 * 
 */
public class FOLFCAskTest extends CommonFOLInferenceProcedureTests {

	@Test
	public void testDefiniteClauseKBKingsQueryCriminalXFalse() {
		testDefiniteClauseKBKingsQueryCriminalXFalse(new mf_InferenceProcedureFCAsk());
	}

	@Test
	public void testDefiniteClauseKBKingsQueryRichardEvilFalse() {
		testDefiniteClauseKBKingsQueryRichardEvilFalse(new mf_InferenceProcedureFCAsk());
	}

	@Test
	public void testDefiniteClauseKBKingsQueryJohnEvilSucceeds() {
		testDefiniteClauseKBKingsQueryJohnEvilSucceeds(new mf_InferenceProcedureFCAsk());
	}

	@Test
	public void testDefiniteClauseKBKingsQueryEvilXReturnsJohnSucceeds() {
		testDefiniteClauseKBKingsQueryEvilXReturnsJohnSucceeds(new mf_InferenceProcedureFCAsk());
	}

	@Test
	public void testDefiniteClauseKBKingsQueryKingXReturnsJohnAndRichardSucceeds() {
		testDefiniteClauseKBKingsQueryKingXReturnsJohnAndRichardSucceeds(new mf_InferenceProcedureFCAsk());
	}

	@Test
	public void testDefiniteClauseKBWeaponsQueryCriminalXReturnsWestSucceeds() {
		testDefiniteClauseKBWeaponsQueryCriminalXReturnsWestSucceeds(new mf_InferenceProcedureFCAsk());
	}
}