package com.software.reuze;
//package aima.core.logic.propositional.algorithms;

import java.util.List;
import java.util.Set;


/*import aima.core.logic.propositional.parsing.PEParser;
import aima.core.logic.propositional.parsing.ast.Sentence;
import aima.core.logic.propositional.parsing.ast.Symbol;
import aima.core.logic.propositional.visitors.SymbolCollector;
import aima.core.util.Converter;
import aima.core.util.SetOps;
import aima.core.util.Util;*/

/**
 * @author Ravi Mohan
 * @author Mike Stampone
 */
public class ml_TTEntails {

	/**
	 * Returns the answer to the specified question using the TT-Entails
	 * algorithm.
	 * 
	 * @param kb
	 *            a knowledge base to ASK
	 * @param alpha
	 *            a question to ASK the knowledge base
	 * 
	 * @return the answer to the specified question using the TT-Entails
	 *         algorithm.
	 */
	public boolean ttEntails(ml_KnowledgeBase kb, String alpha) {
		ml_a_ParseTreeSentence kbSentence = kb.asSentence();
		ml_a_ParseTreeSentence querySentence = (ml_a_ParseTreeSentence) new ml_Parser().parse(alpha);
		ml_SentenceVisitorSymbolCollector collector = new ml_SentenceVisitorSymbolCollector();
		Set<ml_SentenceAtomicSymbol> kbSymbols = collector.getSymbolsIn(kbSentence);
		Set<ml_SentenceAtomicSymbol> querySymbols = collector.getSymbolsIn(querySentence);
		Set<ml_SentenceAtomicSymbol> symbols = da_SetOps.union(kbSymbols, querySymbols);
		List<ml_SentenceAtomicSymbol> symbolList = new d_ConvertListSet<ml_SentenceAtomicSymbol>().setToList(symbols);
		return ttCheckAll(kbSentence, querySentence, symbolList, new ml_Model());
	}

	public boolean ttCheckAll(ml_a_ParseTreeSentence kbSentence, ml_a_ParseTreeSentence querySentence,
			List<ml_SentenceAtomicSymbol> symbols, ml_Model model) {
		if (symbols.isEmpty()) {
			if (model.isTrue(kbSentence)) {
				// System.out.println("#");
				return model.isTrue(querySentence);
			} else {
				// System.out.println("0");
				return true;
			}
		} else {
			ml_SentenceAtomicSymbol symbol = m_MathUtil2.first(symbols);
			List<ml_SentenceAtomicSymbol> rest = m_MathUtil2.rest(symbols);

			ml_Model trueModel = model.extend(new ml_SentenceAtomicSymbol(symbol.getValue()), true);
			ml_Model falseModel = model.extend(new ml_SentenceAtomicSymbol(symbol.getValue()),
					false);
			return (ttCheckAll(kbSentence, querySentence, rest, trueModel) && (ttCheckAll(
					kbSentence, querySentence, rest, falseModel)));
		}
	}
}