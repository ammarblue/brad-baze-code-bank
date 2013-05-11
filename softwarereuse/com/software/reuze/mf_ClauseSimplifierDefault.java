package com.software.reuze;
//package aima.core.logic.fol.inference.otter.defaultimpl;

import java.util.ArrayList;
import java.util.List;

/*import aima.core.logic.fol.inference.Demodulation;
import aima.core.logic.fol.inference.otter.ClauseSimplifier;
import aima.core.logic.fol.kb.data.Clause;
import aima.core.logic.fol.parsing.ast.TermEquality;*/

/**
 * @author Ciaran O'Reilly
 * 
 */
public class mf_ClauseSimplifierDefault implements mf_i_ClauseSimplifier {

	private mf_Demodulation demodulation = new mf_Demodulation();
	private List<mf_SentenceAtomicTermEquality> rewrites = new ArrayList<mf_SentenceAtomicTermEquality>();

	public mf_ClauseSimplifierDefault() {

	}

	public mf_ClauseSimplifierDefault(List<mf_SentenceAtomicTermEquality> rewrites) {
		this.rewrites.addAll(rewrites);
	}

	//
	// START-ClauseSimplifier
	public mf_Clause simplify(mf_Clause c) {
		mf_Clause simplified = c;

		// Apply each of the rewrite rules to
		// the clause
		for (mf_SentenceAtomicTermEquality te : rewrites) {
			mf_Clause dc = simplified;
			// Keep applying the rewrite as many times as it
			// can be applied before moving on to the next one.
			while (null != (dc = demodulation.apply(te, dc))) {
				simplified = dc;
			}
		}

		return simplified;
	}

	// END-ClauseSimplifier
	//
}
