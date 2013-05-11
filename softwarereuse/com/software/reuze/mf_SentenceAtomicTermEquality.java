package com.software.reuze;
//package aima.core.logic.fol.parsing.ast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//import aima.core.logic.fol.parsing.FOLVisitor;

/**
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 */
public class mf_SentenceAtomicTermEquality implements mf_i_SentenceAtomic {
	private mf_i_NodeTerm term1, term2;
	private List<mf_i_NodeTerm> terms = new ArrayList<mf_i_NodeTerm>();
	private String stringRep = null;
	private int hashCode = 0;

	public static String getEqualitySynbol() {
		return "=";
	}

	public mf_SentenceAtomicTermEquality(mf_i_NodeTerm term1, mf_i_NodeTerm term2) {
		this.term1 = term1;
		this.term2 = term2;
		terms.add(term1);
		terms.add(term2);
	}

	public mf_i_NodeTerm getTerm1() {
		return term1;
	}

	public mf_i_NodeTerm getTerm2() {
		return term2;
	}

	//
	// START-AtomicSentence
	public String getSymbolicName() {
		return getEqualitySynbol();
	}

	public boolean isCompound() {
		return true;
	}

	public List<mf_i_NodeTerm> getArgs() {
		return Collections.unmodifiableList(terms);
	}

	public Object accept(mf_Visitor v, Object arg) {
		return v.visitTermEquality(this, arg);
	}

	public mf_SentenceAtomicTermEquality copy() {
		return new mf_SentenceAtomicTermEquality(term1.copy(), term2.copy());
	}

	// END-AtomicSentence
	//

	@Override
	public boolean equals(Object o) {

		if (this == o) {
			return true;
		}
		if ((o == null) || (this.getClass() != o.getClass())) {
			return false;
		}
		mf_SentenceAtomicTermEquality te = (mf_SentenceAtomicTermEquality) o;

		return te.getTerm1().equals(term1) && te.getTerm2().equals(term2);
	}

	@Override
	public int hashCode() {
		if (0 == hashCode) {
			hashCode = 17;
			hashCode = 37 * hashCode + getTerm1().hashCode();
			hashCode = 37 * hashCode + getTerm2().hashCode();
		}
		return hashCode;
	}

	@Override
	public String toString() {
		if (null == stringRep) {
			StringBuilder sb = new StringBuilder();
			sb.append(term1.toString());
			sb.append(" = ");
			sb.append(term2.toString());
			stringRep = sb.toString();
		}
		return stringRep;
	}
}