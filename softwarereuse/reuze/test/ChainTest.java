package reuze.test;
//package aima.test.core.unit.logic.fol.kb.data;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.software.reuze.mf_Literal;
import com.software.reuze.mf_Predicate;
import com.software.reuze.mf_ProofStepChain;
import com.software.reuze.mf_i_NodeTerm;


/*import aima.core.logic.fol.kb.data.Chain;
import aima.core.logic.fol.kb.data.Literal;
import aima.core.logic.fol.parsing.ast.Predicate;
import aima.core.logic.fol.parsing.ast.Term;*/

/**
 * @author Ciaran O'Reilly
 * 
 */
public class ChainTest {

	@Test
	public void testIsEmpty() {
		mf_ProofStepChain c = new mf_ProofStepChain();

		Assert.assertTrue(c.isEmpty());

		c.addLiteral(new mf_Literal(new mf_Predicate("P", new ArrayList<mf_i_NodeTerm>())));

		Assert.assertFalse(c.isEmpty());

		List<mf_Literal> lits = new ArrayList<mf_Literal>();

		lits.add(new mf_Literal(new mf_Predicate("P", new ArrayList<mf_i_NodeTerm>())));

		c = new mf_ProofStepChain(lits);

		Assert.assertFalse(c.isEmpty());
	}

	@Test
	public void testContrapositives() {
		List<mf_ProofStepChain> conts;
		mf_Literal p = new mf_Literal(new mf_Predicate("P", new ArrayList<mf_i_NodeTerm>()));
		mf_Literal notq = new mf_Literal(new mf_Predicate("Q", new ArrayList<mf_i_NodeTerm>()),
				true);
		mf_Literal notr = new mf_Literal(new mf_Predicate("R", new ArrayList<mf_i_NodeTerm>()),
				true);

		mf_ProofStepChain c = new mf_ProofStepChain();

		conts = c.getContrapositives();
		Assert.assertEquals(0, conts.size());

		c.addLiteral(p);
		conts = c.getContrapositives();
		Assert.assertEquals(0, conts.size());

		c.addLiteral(notq);
		conts = c.getContrapositives();
		Assert.assertEquals(1, conts.size());
		Assert.assertEquals("<~Q(),P()>", conts.get(0).toString());

		c.addLiteral(notr);
		conts = c.getContrapositives();
		Assert.assertEquals(2, conts.size());
		Assert.assertEquals("<~Q(),P(),~R()>", conts.get(0).toString());
		Assert.assertEquals("<~R(),P(),~Q()>", conts.get(1).toString());
	}
}
