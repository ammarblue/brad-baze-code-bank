package com.software.reuze;
//package aima.core.logic.fol.kb.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

//import aima.core.logic.fol.inference.proof.ProofStep;
//import aima.core.logic.fol.inference.proof.ProofStepChainContrapositive;
//import aima.core.logic.fol.inference.proof.ProofStepPremise;

/**
 * 
 * A Chain is a sequence of literals (while a clause is a set) - order is
 * important for a chain.
 * 
 * @see <a
 *      href="http://logic.stanford.edu/classes/cs157/2008/lectures/lecture13.pdf"
 *      >Chain</a>
 * 
 * @author Ciaran O'Reilly
 * 
 */
public class mf_ProofStepChain {
	private static List<mf_Literal> _emptyLiteralsList = Collections
			.unmodifiableList(new ArrayList<mf_Literal>());
	//
	private List<mf_Literal> literals = new ArrayList<mf_Literal>();
	private m_ProofStep proofStep = null;

	public mf_ProofStepChain() {
		// i.e. the empty chain
	}

	public mf_ProofStepChain(List<mf_Literal> literals) {
		this.literals.addAll(literals);
	}

	public mf_ProofStepChain(Set<mf_Literal> literals) {
		this.literals.addAll(literals);
	}

	public m_ProofStep getProofStep() {
		if (null == proofStep) {
			// Assume was a premise
			proofStep = new mf_ProofStepPremise(this);
		}
		return proofStep;
	}

	public void setProofStep(m_ProofStep proofStep) {
		this.proofStep = proofStep;
	}

	public boolean isEmpty() {
		return literals.size() == 0;
	}

	public void addLiteral(mf_Literal literal) {
		literals.add(literal);
	}

	public mf_Literal getHead() {
		if (0 == literals.size()) {
			return null;
		}
		return literals.get(0);
	}

	public List<mf_Literal> getTail() {
		if (0 == literals.size()) {
			return _emptyLiteralsList;
		}
		return Collections
				.unmodifiableList(literals.subList(1, literals.size()));
	}

	public int getNumberLiterals() {
		return literals.size();
	}

	public List<mf_Literal> getLiterals() {
		return Collections.unmodifiableList(literals);
	}

	/**
	 * A contrapositive of a chain is a permutation in which a different literal
	 * is placed at the front. The contrapositives of a chain are logically
	 * equivalent to the original chain.
	 * 
	 * @return a list of contrapositives for this chain.
	 */
	public List<mf_ProofStepChain> getContrapositives() {
		List<mf_ProofStepChain> contrapositives = new ArrayList<mf_ProofStepChain>();
		List<mf_Literal> lits = new ArrayList<mf_Literal>();

		for (int i = 1; i < literals.size(); i++) {
			lits.clear();
			lits.add(literals.get(i));
			lits.addAll(literals.subList(0, i));
			lits.addAll(literals.subList(i + 1, literals.size()));
			mf_ProofStepChain cont = new mf_ProofStepChain(lits);
			cont.setProofStep(new mf_ProofStepChainContrapositive(cont, this));
			contrapositives.add(cont);
		}

		return contrapositives;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("<");

		for (int i = 0; i < literals.size(); i++) {
			if (i > 0) {
				sb.append(",");
			}
			sb.append(literals.get(i).toString());
		}

		sb.append(">");

		return sb.toString();
	}
}
