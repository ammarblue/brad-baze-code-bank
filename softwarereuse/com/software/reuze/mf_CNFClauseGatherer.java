package com.software.reuze;
//package aima.core.logic.propositional.visitors;

import java.util.HashSet;
import java.util.Set;

/*import aima.core.logic.propositional.parsing.ast.BinarySentence;
import aima.core.logic.propositional.parsing.ast.Sentence;
import aima.core.logic.propositional.parsing.ast.Symbol;
import aima.core.logic.propositional.parsing.ast.UnarySentence;*/

/**
 * @author Ravi Mohan
 * 
 */
public class mf_CNFClauseGatherer extends ml_SentenceVisitorBasic {
	ml_SentenceVisitOpsEvaluate detector;

	public mf_CNFClauseGatherer() {
		detector = new ml_SentenceVisitOpsEvaluate();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object visitBinarySentence(ml_SentenceBinary bs, Object args) {

		Set<ml_a_ParseTreeSentence> soFar = (Set<ml_a_ParseTreeSentence>) args;

		if (detector.containsEmbeddedAnd(bs)) {
			processSubTerm(bs.getSecond(), processSubTerm(bs.getFirst(), soFar));
		} else {
			soFar.add(bs);
		}

		return soFar;

	}

	@SuppressWarnings("unchecked")
	public Set<ml_a_ParseTreeSentence> getClausesFrom(ml_a_ParseTreeSentence sentence) {
		Set<ml_a_ParseTreeSentence> set = new HashSet<ml_a_ParseTreeSentence>();
		if (sentence instanceof ml_SentenceAtomicSymbol) {
			set.add(sentence);
		} else if (sentence instanceof ml_SentenceComplexUnary) {
			set.add(sentence);
		} else {
			set = (Set<ml_a_ParseTreeSentence>) sentence.accept(this, set);
		}
		return set;
	}

	//
	// PRIVATE METHODS
	//
	@SuppressWarnings("unchecked")
	private Set<ml_a_ParseTreeSentence> processSubTerm(ml_a_ParseTreeSentence s, Set<ml_a_ParseTreeSentence> soFar) {
		if (detector.containsEmbeddedAnd(s)) {
			return (Set<ml_a_ParseTreeSentence>) s.accept(this, soFar);
		} else {
			soFar.add(s);
			return soFar;
		}
	}
}