package com.software.reuze;
//package aima.core.logic.fol.kb.data;

//import aima.core.logic.fol.parsing.ast.AtomicSentence;

/**
 * @see <a
 *      href="http://logic.stanford.edu/classes/cs157/2008/lectures/lecture13.pdf"
 *      >Reduced Literal</a>
 * 
 * @author Ciaran O'Reilly
 * 
 */
public class mf_LiteralReduced extends mf_Literal {
	private String strRep = null;

	public mf_LiteralReduced(mf_i_SentenceAtomic atom) {
		super(atom);
	}

	public mf_LiteralReduced(mf_i_SentenceAtomic atom, boolean negated) {
		super(atom, negated);
	}

	@Override
	public mf_Literal newInstance(mf_i_SentenceAtomic atom) {
		return new mf_LiteralReduced(atom, isNegativeLiteral());
	}

	@Override
	public String toString() {
		if (null == strRep) {
			StringBuilder sb = new StringBuilder();
			sb.append("[");
			if (isNegativeLiteral()) {
				sb.append("~");
			}
			sb.append(getAtomicSentence().toString());
			sb.append("]");
			strRep = sb.toString();
		}

		return strRep;
	}
}
