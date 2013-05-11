package com.software.reuze;
//package aima.core.logic.fol;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/*import aima.core.logic.fol.inference.proof.ProofStepRenaming;
import aima.core.logic.fol.kb.data.Chain;
import aima.core.logic.fol.kb.data.Clause;
import aima.core.logic.fol.kb.data.Literal;
import aima.core.logic.fol.parsing.ast.AtomicSentence;
import aima.core.logic.fol.parsing.ast.Sentence;
import aima.core.logic.fol.parsing.ast.Term;
import aima.core.logic.fol.parsing.ast.Variable;*/

/**
 * @author Ciaran O'Reilly
 * 
 */
public class mf_StandardizeApart {
	private mf_VariableCollector variableCollector = null;
	private mf_VisitorSubstitution substVisitor = null;

	public mf_StandardizeApart() {
		variableCollector = new mf_VariableCollector();
		substVisitor = new mf_VisitorSubstitution();
	}

	public mf_StandardizeApart(mf_VariableCollector variableCollector,
			mf_VisitorSubstitution substVisitor) {
		this.variableCollector = variableCollector;
		this.substVisitor = substVisitor;
	}

	// Note: see page 327.
	public mf_StandardizeApartResult standardizeApart(mf_i_Sentence sentence,
			mf_StandardizeApartIndexical standardizeApartIndexical) {
		Set<mf_NodeTermVariable> toRename = variableCollector
				.collectAllVariables(sentence);
		Map<mf_NodeTermVariable, mf_i_NodeTerm> renameSubstitution = new HashMap<mf_NodeTermVariable, mf_i_NodeTerm>();
		Map<mf_NodeTermVariable, mf_i_NodeTerm> reverseSubstitution = new HashMap<mf_NodeTermVariable, mf_i_NodeTerm>();

		for (mf_NodeTermVariable var : toRename) {
			mf_NodeTermVariable v = null;
			do {
				v = new mf_NodeTermVariable(standardizeApartIndexical.getPrefix()
						+ standardizeApartIndexical.getNextIndex());
				// Ensure the new variable name is not already
				// accidentally used in the sentence
			} while (toRename.contains(v));

			renameSubstitution.put(var, v);
			reverseSubstitution.put(v, var);
		}

		mf_i_Sentence standardized = substVisitor.subst(renameSubstitution,
				sentence);

		return new mf_StandardizeApartResult(sentence, standardized,
				renameSubstitution, reverseSubstitution);
	}

	public mf_Clause standardizeApart(mf_Clause clause,
			mf_StandardizeApartIndexical standardizeApartIndexical) {

		Set<mf_NodeTermVariable> toRename = variableCollector.collectAllVariables(clause);
		Map<mf_NodeTermVariable, mf_i_NodeTerm> renameSubstitution = new HashMap<mf_NodeTermVariable, mf_i_NodeTerm>();

		for (mf_NodeTermVariable var : toRename) {
			mf_NodeTermVariable v = null;
			do {
				v = new mf_NodeTermVariable(standardizeApartIndexical.getPrefix()
						+ standardizeApartIndexical.getNextIndex());
				// Ensure the new variable name is not already
				// accidentally used in the sentence
			} while (toRename.contains(v));

			renameSubstitution.put(var, v);
		}

		if (renameSubstitution.size() > 0) {
			List<mf_Literal> literals = new ArrayList<mf_Literal>();

			for (mf_Literal l : clause.getLiterals()) {
				literals.add(substVisitor.subst(renameSubstitution, l));
			}
			mf_Clause renamed = new mf_Clause(literals);
			renamed.setProofStep(new mf_ProofStepRenaming(renamed, clause
					.getProofStep()));
			return renamed;
		}

		return clause;
	}

	public mf_ProofStepChain standardizeApart(mf_ProofStepChain chain,
			mf_StandardizeApartIndexical standardizeApartIndexical) {

		Set<mf_NodeTermVariable> toRename = variableCollector.collectAllVariables(chain);
		Map<mf_NodeTermVariable, mf_i_NodeTerm> renameSubstitution = new HashMap<mf_NodeTermVariable, mf_i_NodeTerm>();

		for (mf_NodeTermVariable var : toRename) {
			mf_NodeTermVariable v = null;
			do {
				v = new mf_NodeTermVariable(standardizeApartIndexical.getPrefix()
						+ standardizeApartIndexical.getNextIndex());
				// Ensure the new variable name is not already
				// accidentally used in the sentence
			} while (toRename.contains(v));

			renameSubstitution.put(var, v);
		}

		if (renameSubstitution.size() > 0) {
			List<mf_Literal> lits = new ArrayList<mf_Literal>();

			for (mf_Literal l : chain.getLiterals()) {
				mf_i_SentenceAtomic atom = (mf_i_SentenceAtomic) substVisitor.subst(
						renameSubstitution, l.getAtomicSentence());
				lits.add(l.newInstance(atom));
			}

			mf_ProofStepChain renamed = new mf_ProofStepChain(lits);

			renamed.setProofStep(new mf_ProofStepRenaming(renamed, chain
					.getProofStep()));

			return renamed;
		}

		return chain;
	}

	public Map<mf_NodeTermVariable, mf_i_NodeTerm> standardizeApart(List<mf_Literal> l1Literals,
			List<mf_Literal> l2Literals,
			mf_StandardizeApartIndexical standardizeApartIndexical) {
		Set<mf_NodeTermVariable> toRename = new HashSet<mf_NodeTermVariable>();

		for (mf_Literal pl : l1Literals) {
			toRename.addAll(variableCollector.collectAllVariables(pl
					.getAtomicSentence()));
		}
		for (mf_Literal nl : l2Literals) {
			toRename.addAll(variableCollector.collectAllVariables(nl
					.getAtomicSentence()));
		}

		Map<mf_NodeTermVariable, mf_i_NodeTerm> renameSubstitution = new HashMap<mf_NodeTermVariable, mf_i_NodeTerm>();

		for (mf_NodeTermVariable var : toRename) {
			mf_NodeTermVariable v = null;
			do {
				v = new mf_NodeTermVariable(standardizeApartIndexical.getPrefix()
						+ standardizeApartIndexical.getNextIndex());
				// Ensure the new variable name is not already
				// accidentally used in the sentence
			} while (toRename.contains(v));

			renameSubstitution.put(var, v);
		}

		List<mf_Literal> posLits = new ArrayList<mf_Literal>();
		List<mf_Literal> negLits = new ArrayList<mf_Literal>();

		for (mf_Literal pl : l1Literals) {
			posLits.add(substVisitor.subst(renameSubstitution, pl));
		}
		for (mf_Literal nl : l2Literals) {
			negLits.add(substVisitor.subst(renameSubstitution, nl));
		}

		l1Literals.clear();
		l1Literals.addAll(posLits);
		l2Literals.clear();
		l2Literals.addAll(negLits);

		return renameSubstitution;
	}
}
