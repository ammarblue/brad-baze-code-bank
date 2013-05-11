package com.software.reuze;
//package aima.core.logic.propositional.algorithms;

import java.util.ArrayList;
import java.util.List;

//import aima.core.logic.propositional.parsing.PEParser;
//import aima.core.logic.propositional.parsing.ast.Sentence;
//import aima.core.logic.propositional.visitors.CNFTransformer;

/**
 * @author Ravi Mohan
 * @author Mike Stampone
 */
public class ml_KnowledgeBase {
	private List<ml_a_ParseTreeSentence> sentences;

	private ml_Parser parser;

	public ml_KnowledgeBase() {
		sentences = new ArrayList<ml_a_ParseTreeSentence>();
		parser = new ml_Parser();
	}

	/**
	 * Adds the specified sentence to the knowledge base.
	 * 
	 * @param aSentence
	 *            a fact to be added to the knowledge base.
	 */
	public void tell(String aSentence) {
		ml_a_ParseTreeSentence sentence = (ml_a_ParseTreeSentence) parser.parse(aSentence);
		if (!(sentences.contains(sentence))) {
			sentences.add(sentence);
		}
	}

	/**
	 * Each time the agent program is called, it TELLS the knowledge base what
	 * it perceives.
	 * 
	 * @param percepts
	 *            what the agent perceives
	 */
	public void tellAll(String[] percepts) {
		for (int i = 0; i < percepts.length; i++) {
			tell(percepts[i]);
		}

	}

	/**
	 * Returns the number of sentences in the knowledge base.
	 * 
	 * @return the number of sentences in the knowledge base.
	 */
	public int size() {
		return sentences.size();
	}

	/**
	 * Returns the list of sentences in the knowledge base chained together as a
	 * single sentence.
	 * 
	 * @return the list of sentences in the knowledge base chained together as a
	 *         single sentence.
	 */
	public ml_a_ParseTreeSentence asSentence() {
		return ml_LogicUtils.chainWith("AND", sentences);
	}

	/**
	 * Returns the answer to the specified question using the DPLL algorithm.
	 * 
	 * @param queryString
	 *            a question to ASK the knowledge base
	 * 
	 * @return the answer to the specified question using the DPLL algorithm.
	 */
	public boolean askWithDpll(String queryString) {
		ml_a_ParseTreeSentence query = null, cnfForm = null;
		try {
			// just a check to see that the query is well formed
			query = (ml_a_ParseTreeSentence) parser.parse(queryString);
		} catch (Exception e) {
			System.out.println("error parsing query" + e.getMessage());
		}

		ml_a_ParseTreeSentence kbSentence = asSentence();
		ml_a_ParseTreeSentence kbPlusQuery = null;
		if (kbSentence != null) {
			kbPlusQuery = (ml_a_ParseTreeSentence) parser.parse(" ( " + kbSentence.toString()
					+ " AND (NOT " + queryString + " ))");
		} else {
			kbPlusQuery = query;
		}
		try {
			cnfForm = new mf_CNFTransformer().transform(kbPlusQuery);
			// System.out.println(cnfForm.toString());
		} catch (Exception e) {
			System.out.println("error converting kb +  query to CNF"
					+ e.getMessage());

		}
		return !new ml_DPLL().dpllSatisfiable(cnfForm);
	}

	/**
	 * Returns the answer to the specified question using the TT-Entails
	 * algorithm.
	 * 
	 * @param queryString
	 *            a question to ASK the knowledge base
	 * 
	 * @return the answer to the specified question using the TT-Entails
	 *         algorithm.
	 */
	public boolean askWithTTEntails(String queryString) {

		return new ml_TTEntails().ttEntails(this, queryString);
	}

	@Override
	public String toString() {
		if (sentences.size() == 0) {
			return "";
		} else
			return asSentence().toString();
	}

	/**
	 * Returns the list of sentences in the knowledge base.
	 * 
	 * @return the list of sentences in the knowledge base.
	 */
	public List<ml_a_ParseTreeSentence> getSentences() {
		return sentences;
	}
}