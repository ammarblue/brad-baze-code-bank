package com.software.reuze;
//package aima.core.logic.propositional.algorithms;

import java.util.List;

//import aima.core.logic.propositional.parsing.ast.BinarySentence;
//import aima.core.logic.propositional.parsing.ast.Sentence;

/**
 * @author Ravi Mohan
 * 
 */
public class ml_LogicUtils {

	public static ml_a_ParseTreeSentence chainWith(String connector, List<ml_a_ParseTreeSentence> sentences) {
		if (sentences.size() == 0) {
			return null;
		} else if (sentences.size() == 1) {
			return sentences.get(0);
		} else {
			ml_a_ParseTreeSentence soFar = sentences.get(0);
			for (int i = 1; i < sentences.size(); i++) {
				ml_a_ParseTreeSentence next = sentences.get(i);
				soFar = new ml_SentenceBinary(connector, soFar, next);
			}
			return soFar;
		}
	}
}