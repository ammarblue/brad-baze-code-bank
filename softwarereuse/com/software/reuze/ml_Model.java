package com.software.reuze;
//package aima.core.logic.propositional.algorithms;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/*import aima.core.logic.propositional.parsing.PLVisitor;
import aima.core.logic.propositional.parsing.ast.BinarySentence;
import aima.core.logic.propositional.parsing.ast.FalseSentence;
import aima.core.logic.propositional.parsing.ast.MultiSentence;
import aima.core.logic.propositional.parsing.ast.Sentence;
import aima.core.logic.propositional.parsing.ast.Symbol;
import aima.core.logic.propositional.parsing.ast.TrueSentence;
import aima.core.logic.propositional.parsing.ast.UnarySentence;*/

/**
 * @author Ravi Mohan
 * 
 */
public class ml_Model implements ml_i_SentenceVisit {

	private HashMap<ml_SentenceAtomicSymbol, Boolean> h = new HashMap<ml_SentenceAtomicSymbol, Boolean>();

	public ml_Model() {

	}

	public Boolean getStatus(ml_SentenceAtomicSymbol symbol) {
		return h.get(symbol);
	}

	public boolean isTrue(ml_SentenceAtomicSymbol symbol) {
		return Boolean.TRUE.equals(h.get(symbol));
	}

	public boolean isFalse(ml_SentenceAtomicSymbol symbol) {
		return Boolean.FALSE.equals(h.get(symbol));
	}

	public ml_Model extend(ml_SentenceAtomicSymbol symbol, boolean b) {
		ml_Model m = new ml_Model();
		m.h.putAll(this.h);
		m.h.put(symbol, b);
		return m;
	}

	public boolean isTrue(ml_a_ParseTreeSentence clause) {
		return Boolean.TRUE.equals(clause.accept(this, null));
	}

	public boolean isFalse(ml_a_ParseTreeSentence clause) {
		return Boolean.FALSE.equals(clause.accept(this, null));
	}

	public boolean isUnknown(ml_a_ParseTreeSentence clause) {
		return null == clause.accept(this, null);
	}

	public ml_Model flip(ml_SentenceAtomicSymbol s) {
		if (isTrue(s)) {
			return extend(s, false);
		}
		if (isFalse(s)) {
			return extend(s, true);
		}
		return this;
	}

	public Set<ml_SentenceAtomicSymbol> getAssignedSymbols() {
		return Collections.unmodifiableSet(h.keySet());
	}

	public void print() {
		for (Map.Entry<ml_SentenceAtomicSymbol, Boolean> e : h.entrySet()) {
			System.out.print(e.getKey() + " = " + e.getValue() + " ");
		}
		System.out.println();
	}

	@Override
	public String toString() {
		return h.toString();
	}

	//
	// START-PLVisitor
	public Object visitSymbol(ml_SentenceAtomicSymbol s, Object arg) {
		return getStatus(s);
	}

	public Object visitTrueSentence(ml_SentenceAtomicTrue ts, Object arg) {
		return Boolean.TRUE;
	}

	public Object visitFalseSentence(ml_SentenceAtomicFalse fs, Object arg) {
		return Boolean.FALSE;
	}

	public Object visitNotSentence(ml_SentenceComplexUnary fs, Object arg) {
		Object negatedValue = fs.getNegated().accept(this, null);
		if (negatedValue != null) {
			return new Boolean(!((Boolean) negatedValue).booleanValue());
		} else {
			return null;
		}
	}

	public Object visitBinarySentence(ml_SentenceBinary bs, Object arg) {
		Boolean firstValue = (Boolean) bs.getFirst().accept(this, null);
		Boolean secondValue = (Boolean) bs.getSecond().accept(this, null);
		if ((firstValue == null) || (secondValue == null)) {
			// strictly not true for or/and
			// -FIX later
			return null;
		} else {
			String operator = bs.getOperator();
			if (operator.equals("AND")) {
				return firstValue && secondValue;
			} else if (operator.equals("OR")) {
				return firstValue || secondValue;
			} else if (operator.equals("=>")) {
				return !(firstValue && !secondValue);
			} else if (operator.equals("<=>")) {
				return firstValue.equals(secondValue);
			}
			return null;
		}
	}

	public Object visitMultiSentence(ml_SentenceComplexMulti fs, Object argd) {
		// TODO remove this?
		return null;
	}
	// END-PLVisitor
	//
}