package com.software.reuze;
//package aima.core.logic.fol.kb.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Conjunctive Normal Form (CNF) : a conjunction of clauses, where each clause
 * is a disjunction of literals.
 * 
 * @author Ciaran O'Reilly
 * 
 */
public class mf_CNF {

	private List<mf_Clause> conjunctionOfClauses = new ArrayList<mf_Clause>();

	public mf_CNF(List<mf_Clause> conjunctionOfClauses) {
		this.conjunctionOfClauses.addAll(conjunctionOfClauses);
	}

	public int getNumberOfClauses() {
		return conjunctionOfClauses.size();
	}

	public List<mf_Clause> getConjunctionOfClauses() {
		return Collections.unmodifiableList(conjunctionOfClauses);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < conjunctionOfClauses.size(); i++) {
			if (i > 0) {
				sb.append(",");
			}
			sb.append(conjunctionOfClauses.get(i).toString());
		}

		return sb.toString();
	}
}
