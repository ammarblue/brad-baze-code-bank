package com.software.reuze;
//package aima.core.learning.knowledge;

/*import aima.core.logic.fol.Connectors;
import aima.core.logic.fol.parsing.ast.ConnectedSentence;
import aima.core.logic.fol.parsing.ast.Predicate;
import aima.core.logic.fol.parsing.ast.QuantifiedSentence;
import aima.core.logic.fol.parsing.ast.FOSentence;
import aima.core.logic.fol.parsing.ast.Term;
import aima.core.logic.fol.parsing.ast.Variable;*/
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Hypothesis; per page 769, AIMAv3, should be in the form:
 * <pre>
 * FORALL v (Classification(v) &lt;=&gt;
 *  (Description1(v) AND Description2(v, Constant1))
 *  OR (Description1(v) AND Description3(v))
 *  OR ...
 * )
 * </pre>
 *
 * @author Ciaran O'Reilly
 * @author Andrew Brown
 */
public class mf_Hypothesis extends mf_SentenceQuantified {

    /**
     * Constructor
     *
     * @param classificationPredicate
     */
    public mf_Hypothesis(String classificationPredicate) {
        super("FORALL", buildVariables(), buildSentence(classificationPredicate));
    }

    /**
     * Return "r"; see page 769, AIMAv3
     *
     * @return
     */
    private static List<mf_NodeTermVariable> buildVariables() {
        ArrayList<mf_NodeTermVariable> variables = new ArrayList<mf_NodeTermVariable>();
        variables.add(new mf_NodeTermVariable("r"));
        return variables;
    }

    /**
     * Return starting sentence; e.g. "FORALL r WillWait(r) <=> true"
     *
     * @param predicate
     * @return
     */
    private static mf_i_Sentence buildSentence(String predicate) {
        // build predicate
        ArrayList<mf_i_NodeTerm> terms = new ArrayList<mf_i_NodeTerm>();
        terms.add(new mf_NodeTermVariable("r"));
        mf_Predicate p = new mf_Predicate(predicate, terms);
        // build sentence
        mf_SentenceConnected sentence = new mf_SentenceConnected(mf_SymbolsConnectors.BICOND, p, null); // @todo: this is probably incorrect
        // return
        return sentence;
    }
}
