package reuze.test;
//package aima.test.core.unit.logic.propositional.algorithms;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.software.reuze.d_ConvertListSet;
import com.software.reuze.mf_CNFClauseGatherer;
import com.software.reuze.mf_CNFTransformer;
import com.software.reuze.ml_DPLL;
import com.software.reuze.ml_KnowledgeBase;
import com.software.reuze.ml_Model;
import com.software.reuze.ml_Parser;
import com.software.reuze.ml_SentenceAtomicSymbol;
import com.software.reuze.ml_SentenceVisitorSymbolCollector;
import com.software.reuze.ml_a_ParseTreeSentence;


/*import aima.core.logic.propositional.algorithms.DPLL;
import aima.core.logic.propositional.algorithms.KnowledgeBase;
import aima.core.logic.propositional.algorithms.Model;
import aima.core.logic.propositional.parsing.PEParser;
import aima.core.logic.propositional.parsing.ast.Sentence;
import aima.core.logic.propositional.parsing.ast.Symbol;
import aima.core.logic.propositional.visitors.CNFClauseGatherer;
import aima.core.logic.propositional.visitors.CNFTransformer;
import aima.core.logic.propositional.visitors.SymbolCollector;
import aima.core.util.Converter;*/

/**
 * @author Ravi Mohan
 * 
 */
public class DPLLTest {

	private ml_DPLL dpll;

	private ml_Parser parser;

	@Before
	public void setUp() {
		parser = new ml_Parser();
		dpll = new ml_DPLL();
	}

	@Test
	public void testDPLLReturnsTrueWhenAllClausesTrueInModel() {
		ml_Model model = new ml_Model();
		model = model.extend(new ml_SentenceAtomicSymbol("A"), true).extend(new ml_SentenceAtomicSymbol("B"),
				true);
		ml_a_ParseTreeSentence sentence = (ml_a_ParseTreeSentence) parser.parse("((A AND B) AND (A OR B))");
		boolean satisfiable = dpll.dpllSatisfiable(sentence, model);
		Assert.assertEquals(true, satisfiable);
	}

	@Test
	public void testDPLLReturnsFalseWhenOneClauseFalseInModel() {
		ml_Model model = new ml_Model();
		model = model.extend(new ml_SentenceAtomicSymbol("A"), true).extend(new ml_SentenceAtomicSymbol("B"),
				false);
		ml_a_ParseTreeSentence sentence = (ml_a_ParseTreeSentence) parser.parse("((A OR B) AND (A => B))");
		boolean satisfiable = dpll.dpllSatisfiable(sentence, model);
		Assert.assertEquals(false, satisfiable);
	}

	@Test
	public void testDPLLFiltersClausesTheStatusOfWhichAreKnown() {
		ml_Model model = new ml_Model();
		model = model.extend(new ml_SentenceAtomicSymbol("A"), true).extend(new ml_SentenceAtomicSymbol("B"),
				true);
		ml_a_ParseTreeSentence sentence = (ml_a_ParseTreeSentence) parser
				.parse("((A AND B) AND (B AND C))");
		List<ml_a_ParseTreeSentence> clauseList = new d_ConvertListSet<ml_a_ParseTreeSentence>()
				.setToList(new mf_CNFClauseGatherer()
						.getClausesFrom(new mf_CNFTransformer()
								.transform(sentence)));
		List<ml_a_ParseTreeSentence> clausesWithNonTrueValues = dpll
				.clausesWithNonTrueValues(clauseList, model);
		Assert.assertEquals(1, clausesWithNonTrueValues.size());
		ml_a_ParseTreeSentence nonTrueClause = (ml_a_ParseTreeSentence) parser.parse("(B AND C)");
		clausesWithNonTrueValues.contains(nonTrueClause);
	}

	@Test
	public void testDPLLFilteringNonTrueClausesGivesNullWhenAllClausesAreKnown() {
		ml_Model model = new ml_Model();
		model = model.extend(new ml_SentenceAtomicSymbol("A"), true)
				.extend(new ml_SentenceAtomicSymbol("B"), true).extend(new ml_SentenceAtomicSymbol("C"), true);
		ml_a_ParseTreeSentence sentence = (ml_a_ParseTreeSentence) parser
				.parse("((A AND B) AND (B AND C))");
		List<ml_a_ParseTreeSentence> clauseList = new d_ConvertListSet<ml_a_ParseTreeSentence>()
				.setToList(new mf_CNFClauseGatherer()
						.getClausesFrom(new mf_CNFTransformer()
								.transform(sentence)));
		List<ml_a_ParseTreeSentence> clausesWithNonTrueValues = dpll
				.clausesWithNonTrueValues(clauseList, model);
		Assert.assertEquals(0, clausesWithNonTrueValues.size());
	}

	@Test
	public void testDPLLFindsPurePositiveSymbolsWhenTheyExist() {
		ml_Model model = new ml_Model();
		model = model.extend(new ml_SentenceAtomicSymbol("A"), true).extend(new ml_SentenceAtomicSymbol("B"),
				true);
		ml_a_ParseTreeSentence sentence = (ml_a_ParseTreeSentence) parser
				.parse("((A AND B) AND (B AND C))");
		List<ml_a_ParseTreeSentence> clauseList = new d_ConvertListSet<ml_a_ParseTreeSentence>()
				.setToList(new mf_CNFClauseGatherer()
						.getClausesFrom(new mf_CNFTransformer()
								.transform(sentence)));
		List<ml_SentenceAtomicSymbol> symbolList = new d_ConvertListSet<ml_SentenceAtomicSymbol>()
				.setToList(new ml_SentenceVisitorSymbolCollector().getSymbolsIn(sentence));

		ml_DPLL.SymbolValuePair sv = dpll.findPureSymbolValuePair(clauseList,
				model, symbolList);
		Assert.assertNotNull(sv);
		Assert.assertEquals(new ml_SentenceAtomicSymbol("C"), sv.symbol);
		Assert.assertEquals(new Boolean(true), sv.value);
	}

	@Test
	public void testDPLLFindsPureNegativeSymbolsWhenTheyExist() {
		ml_Model model = new ml_Model();
		model = model.extend(new ml_SentenceAtomicSymbol("A"), true).extend(new ml_SentenceAtomicSymbol("B"),
				true);
		ml_a_ParseTreeSentence sentence = (ml_a_ParseTreeSentence) parser
				.parse("((A AND B) AND ( B  AND (NOT C) ))");
		List<ml_a_ParseTreeSentence> clauseList = new d_ConvertListSet<ml_a_ParseTreeSentence>()
				.setToList(new mf_CNFClauseGatherer()
						.getClausesFrom(new mf_CNFTransformer()
								.transform(sentence)));
		List<ml_SentenceAtomicSymbol> symbolList = new d_ConvertListSet<ml_SentenceAtomicSymbol>()
				.setToList(new ml_SentenceVisitorSymbolCollector().getSymbolsIn(sentence));

		ml_DPLL.SymbolValuePair sv = dpll.findPureSymbolValuePair(clauseList,
				model, symbolList);
		Assert.assertNotNull(sv);
		Assert.assertEquals(new ml_SentenceAtomicSymbol("C"), sv.symbol);
		Assert.assertEquals(new Boolean(false), sv.value);
	}

	@Test
	public void testDPLLSucceedsWithAandNotA() {
		ml_a_ParseTreeSentence sentence = (ml_a_ParseTreeSentence) parser.parse("(A AND (NOT A))");
		boolean satisfiable = dpll.dpllSatisfiable(sentence);
		Assert.assertEquals(false, satisfiable);
	}

	@Test
	public void testDPLLSucceedsWithChadCarffsBugReport() {
		ml_KnowledgeBase kb = new ml_KnowledgeBase();
		kb.tell("(B12 <=> (P11 OR (P13 OR (P22 OR P02))))");
		kb.tell("(B21 <=> (P20 OR (P22 OR (P31 OR P11))))");
		kb.tell("(B01 <=> (P00 OR (P02 OR P11)))");
		kb.tell("(B10 <=> (P11 OR (P20 OR P00)))");
		kb.tell("(NOT B21)");
		kb.tell("(NOT B12)");
		kb.tell("(B10)");
		kb.tell("(B01)");
		Assert.assertTrue(kb.askWithDpll("(P00)"));
		Assert.assertFalse(kb.askWithDpll("(NOT P00)"));
	}

	@Test
	public void testDPLLSucceedsWithStackOverflowBugReport1() {
		ml_a_ParseTreeSentence sentence = (ml_a_ParseTreeSentence) parser
				.parse("((A OR (NOT A)) AND (A OR B))");
		Assert.assertTrue(dpll.dpllSatisfiable(sentence));
	}

	@Test
	public void testDPLLSucceedsWithChadCarffsBugReport2() {
		ml_KnowledgeBase kb = new ml_KnowledgeBase();
		kb.tell("(B10 <=> (P11 OR (P20 OR P00)))");
		kb.tell("(B01 <=> (P00 OR (P02 OR P11)))");
		kb.tell("(B21 <=> (P20 OR (P22 OR (P31 OR P11))))");
		kb.tell("(B12 <=> (P11 OR (P13 OR (P22 OR P02))))");
		kb.tell("(NOT B21)");
		kb.tell("(NOT B12)");
		kb.tell("(B10)");
		kb.tell("(B01)");
		Assert.assertTrue(kb.askWithDpll("(P00)"));
		Assert.assertFalse(kb.askWithDpll("(NOT P00)"));
	}

	@Test
	public void testIssue66() {
		// http://code.google.com/p/aima-java/issues/detail?id=66
		ml_Model model = new ml_Model();
		model = model.extend(new ml_SentenceAtomicSymbol("A"), false)
				.extend(new ml_SentenceAtomicSymbol("B"), false).extend(new ml_SentenceAtomicSymbol("C"), true);
		ml_a_ParseTreeSentence sentence = (ml_a_ParseTreeSentence) parser.parse("((A OR B) OR C)");
		Assert.assertTrue(dpll.dpllSatisfiable(sentence, model));
	}

	@Test
	public void testDoesNotKnow() {
		ml_KnowledgeBase kb = new ml_KnowledgeBase();
		kb.tell("A");

		Assert.assertFalse(kb.askWithDpll("B"));
		Assert.assertFalse(kb.askWithDpll("(NOT B)"));
	}
}
