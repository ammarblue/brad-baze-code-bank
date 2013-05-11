package reuze.test;
//package aima.test.core.unit.logic.fol;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.software.reuze.mf_DomainExamples;
import com.software.reuze.mf_Parser;
import com.software.reuze.mf_Predicate;
import com.software.reuze.mf_VisitorPredicateCollector;
import com.software.reuze.mf_i_Sentence;


/*import aima.core.logic.fol.PredicateCollector;
import aima.core.logic.fol.domain.DomainFactory;
import aima.core.logic.fol.parsing.FOLParser;
import aima.core.logic.fol.parsing.ast.Predicate;
import aima.core.logic.fol.parsing.ast.Sentence;*/

/**
 * @author Ravi Mohan
 * 
 */
public class PredicateCollectorTest {
	mf_VisitorPredicateCollector collector;

	mf_Parser parser;

	@Before
	public void setUp() {
		collector = new mf_VisitorPredicateCollector();
		parser = new mf_Parser(mf_DomainExamples.weaponsDomain());
	}

	@Test
	public void testSimpleSentence() {
		mf_i_Sentence s = parser.parse("(Missile(x) => Weapon(x))");
		List<mf_Predicate> predicates = collector.getPredicates(s);
		Assert.assertNotNull(predicates);
	}
}
