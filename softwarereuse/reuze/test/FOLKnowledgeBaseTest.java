package reuze.test;
//package aima.test.core.unit.logic.fol.kb;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.software.reuze.mf_Clause;
import com.software.reuze.mf_DomainExamples;
import com.software.reuze.mf_KnowledgeBase;
import com.software.reuze.mf_Literal;
import com.software.reuze.mf_NodeTermVariable;
import com.software.reuze.mf_Predicate;
import com.software.reuze.mf_StandardizeApartIndexicalFactory;
import com.software.reuze.mf_i_NodeTerm;


/*import aima.core.logic.fol.StandardizeApartIndexicalFactory;
import aima.core.logic.fol.domain.DomainFactory;
import aima.core.logic.fol.kb.FOLKnowledgeBase;
import aima.core.logic.fol.kb.data.Clause;
import aima.core.logic.fol.kb.data.Literal;
import aima.core.logic.fol.parsing.ast.Predicate;
import aima.core.logic.fol.parsing.ast.Term;
import aima.core.logic.fol.parsing.ast.Variable;*/

/**
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 * 
 */
public class FOLKnowledgeBaseTest {

	private mf_KnowledgeBase weaponsKB, kingsKB;

	@Before
	public void setUp() {
		mf_StandardizeApartIndexicalFactory.flush();

		weaponsKB = new mf_KnowledgeBase(mf_DomainExamples.weaponsDomain());

		kingsKB = new mf_KnowledgeBase(mf_DomainExamples.kingsDomain());
	}

	@Test
	public void testAddRuleAndFact() {
		weaponsKB.tell("(Missile(x) => Weapon(x))");
		Assert.assertEquals(1, weaponsKB.getNumberRules());
		weaponsKB.tell("American(West)");
		Assert.assertEquals(1, weaponsKB.getNumberRules());
		Assert.assertEquals(1, weaponsKB.getNumberFacts());
	}

	@Test
	public void testAddComplexRule() {
		weaponsKB
				.tell("( (((American(x) AND Weapon(y)) AND Sells(x,y,z)) AND Hostile(z)) => Criminal(x))");
		Assert.assertEquals(1, weaponsKB.getNumberRules());
		weaponsKB.tell("American(West)");
		Assert.assertEquals(1, weaponsKB.getNumberRules());

		List<mf_i_NodeTerm> terms = new ArrayList<mf_i_NodeTerm>();
		terms.add(new mf_NodeTermVariable("v0"));

		mf_Clause dcRule = weaponsKB.getAllDefiniteClauseImplications().get(0);
		Assert.assertNotNull(dcRule);
		Assert.assertEquals(true, dcRule.isImplicationDefiniteClause());
		Assert.assertEquals(new mf_Literal(new mf_Predicate("Criminal", terms)),
				dcRule.getPositiveLiterals().get(0));
	}

	@Test
	public void testFactNotAddedWhenAlreadyPresent() {
		kingsKB.tell("((King(x) AND Greedy(x)) => Evil(x))");
		kingsKB.tell("King(John)");
		kingsKB.tell("King(Richard)");
		kingsKB.tell("Greedy(John)");

		Assert.assertEquals(1, kingsKB.getNumberRules());
		Assert.assertEquals(3, kingsKB.getNumberFacts());

		kingsKB.tell("King(John)");
		kingsKB.tell("King(Richard)");
		kingsKB.tell("Greedy(John)");

		Assert.assertEquals(1, kingsKB.getNumberRules());
		Assert.assertEquals(3, kingsKB.getNumberFacts());

		kingsKB.tell("(((King(John))))");
		kingsKB.tell("(((King(Richard))))");
		kingsKB.tell("(((Greedy(John))))");

		Assert.assertEquals(1, kingsKB.getNumberRules());
		Assert.assertEquals(3, kingsKB.getNumberFacts());
	}
}