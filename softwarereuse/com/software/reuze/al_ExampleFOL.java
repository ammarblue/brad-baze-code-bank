package com.software.reuze;
//package aima.core.learning.knowledge;

/*import aima.core.learning.framework.Attribute;
import aima.core.learning.framework.Example;
import aima.core.logic.fol.Connectors;
import aima.core.logic.fol.parsing.ast.*;*/
import java.util.ArrayList;
import java.util.List;


/**
 * Extends Example to work with CURRENT-BEST-LEARNING (page 771, AIMAv3). This
 * class converts the attributes in an Example to Predicates in a first order
 * logic sentence.
 *
 * @author Ciaran O'Reilly
 * @author Andrew Brown
 */
public class al_ExampleFOL extends al_Example {

    public String classificationPredicate = "UnknownClassifier";

    /**
     * Returns the constant representing this example; uses hashCode() as it is
     * the most convenient pattern in Java for uniquely representing this
     * object.
     *
     * @return
     */
    public mf_SymbolConstant toConstant() {
        return new mf_SymbolConstant("X_" + this.hashCode());
    }

    /**
     * Returns the classification sentence for this example; e.g.
     * "WillWait(X_83478)" or "NOT WillWait(X_83478)".
     *
     * @param classificationPredicate
     * @return
     */
    public mf_i_Sentence toClassification() {
        List<mf_i_NodeTerm> terms = new ArrayList<mf_i_NodeTerm>();
        terms.add(this.toConstant());
        mf_i_Sentence classification = new mf_Predicate(this.classificationPredicate, terms);
        if (this.getOutput() == null) {
            classification = new mf_SentenceNot(classification);
        }
        return classification;
    }

    /**
     * Returns the Example as a first order logic sentence; e.g.
     * "Patrons(X_1234, Full) AND NOT Hungry(X_1234)".
     *
     * @return
     */
    public mf_i_Sentence toDescription() {
        // collect predicates for this example
        List<mf_i_Sentence> predicates = new ArrayList<mf_i_Sentence>();
        for (d_PropertyListPair a : this.getAttributes()) {
            // create predicate terms
            mf_i_Sentence predicate = null;
            List<mf_i_NodeTerm> terms = new ArrayList<mf_i_NodeTerm>();
            terms.add(this.toConstant());
            // single value predicate
            if (a.getValue() instanceof Boolean) {
                boolean value = (Boolean) a.getValue();
                if (value) {
                    // do nothing; e.g. Predicate(X_1385)
                    predicate = new mf_Predicate(a.getName(), terms);
                } else {
                    // create not-sentence
                    predicate = new mf_Predicate(a.getName(), terms);
                    predicate = new mf_SentenceNot(predicate);
                }
            } // multi-value predicate
            else {
                mf_SymbolConstant value = new mf_SymbolConstant(a.getValue().toString());
                terms.add(value);
                predicate = new mf_Predicate(a.getName(), terms);
            }
            // add predicate
            predicates.add(predicate);
        }
        // compile as ConnectedSentence
        mf_i_Sentence output = null;
        if (predicates.size() > 1) {
            output = new mf_SentenceConnected(mf_SymbolsConnectors.AND, predicates.get(0), predicates.get(1));
            for (int i = 2; i < predicates.size(); i++) {
                output = new mf_SentenceConnected(mf_SymbolsConnectors.AND, output, predicates.get(i));
            }
        } else {
            output = predicates.get(0);
        }
        // return
        return output;
    }
}
