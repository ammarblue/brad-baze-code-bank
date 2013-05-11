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
public class mf_SentenceQuantified implements mf_i_Sentence {
	private String quantifier;
	private List<mf_NodeTermVariable> variables = new ArrayList<mf_NodeTermVariable>();
	private mf_i_Sentence quantified;
	private List<mf_i_Node> args = new ArrayList<mf_i_Node>();
	private String stringRep = null;
	private int hashCode = 0;

	public mf_SentenceQuantified(String quantifier, List<mf_NodeTermVariable> variables,
			mf_i_Sentence quantified) {
		this.quantifier = quantifier;
		this.variables.addAll(variables);
		this.quantified = quantified;
		this.args.addAll(variables);
		this.args.add(quantified);
	}

	public String getQuantifier() {
		return quantifier;
	}

	public List<mf_NodeTermVariable> getVariables() {
		return Collections.unmodifiableList(variables);
	}

	public mf_i_Sentence getQuantified() {
		return quantified;
	}

	//
	// START-Sentence
	public String getSymbolicName() {
		return getQuantifier();
	}

	public boolean isCompound() {
		return true;
	}

	public List<mf_i_Node> getArgs() {
		return Collections.unmodifiableList(args);
	}

	public Object accept(mf_Visitor v, Object arg) {
		return v.visitQuantifiedSentence(this, arg);
	}

	public mf_SentenceQuantified copy() {
		List<mf_NodeTermVariable> copyVars = new ArrayList<mf_NodeTermVariable>();
		for (mf_NodeTermVariable v : variables) {
			copyVars.add(v.copy());
		}
		return new mf_SentenceQuantified(quantifier, copyVars, quantified.copy());
	}

	// END-Sentence
	//

	@Override
	public boolean equals(Object o) {

		if (this == o) {
			return true;
		}
		if ((o == null) || (this.getClass() != o.getClass())) {
			return false;
		}
		mf_SentenceQuantified cs = (mf_SentenceQuantified) o;
		return cs.quantifier.equals(quantifier)
				&& cs.variables.equals(variables)
				&& cs.quantified.equals(quantified);
	}

	@Override
	public int hashCode() {
		if (0 == hashCode) {
			hashCode = 17;
			hashCode = 37 * hashCode + quantifier.hashCode();
			for (mf_NodeTermVariable v : variables) {
				hashCode = 37 * hashCode + v.hashCode();
			}
			hashCode = hashCode * 37 + quantified.hashCode();
		}
		return hashCode;
	}

	@Override
	public String toString() {
		if (null == stringRep) {
			StringBuilder sb = new StringBuilder();
			sb.append(quantifier);
			sb.append(" ");
			for (mf_NodeTermVariable v : variables) {
				sb.append(v.toString());
				sb.append(" ");
			}
			sb.append(quantified.toString());
			stringRep = sb.toString();
		}
		return stringRep;
	}
}