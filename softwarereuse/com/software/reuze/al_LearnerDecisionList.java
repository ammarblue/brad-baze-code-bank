package com.software.reuze;
//package aima.core.learning.learners;

/*import aima.core.learning.framework.Attribute;
import aima.core.learning.framework.DataSet;
import aima.core.learning.framework.Example;
import aima.core.learning.framework.Learner;
import aima.core.learning.inductive.DecisionList;
import aima.core.learning.inductive.DecisionListTest;*/
import java.util.HashSet;

/**
 * Uses DECISION-LIST-LEARNING algorithm, page 717, AIMAv3, to build a series of
 * tests (DecisionListTest); the chain of tests is used to predict outcomes for
 * new examples.
 *
 * @author Ravi Mohan
 * @author Mike Stampone
 * @author Andrew Brown
 */
public class al_LearnerDecisionList implements al_i_Learner {

    private al_DecisionListTests decisionList;
    private HashSet<d_PropertyListPair> possibleAttributes;

    /**
     * Constructor
     */
    public al_LearnerDecisionList() {
    }

    /**
     * Return the decision list for this learner
     *
     * @return
     */
    public al_DecisionListTests getDecisionList() {
        return this.decisionList;
    }

    /**
     * Implement DECISION-LIST-LEARNING, page 717, AIMAv3 <br/>
     * <pre>
     * function DECISION-LIST-LEARNING(examples) returns a decision list, or failure
     * if examples is empty then return the trivial decision list "No"
     * t &lt;- a test that matches a nonempty subset examples_t of examples, such that the members of examples_t are all positive or negative
     * if there is no such t then return failure
     * if the examples in examples_t are positive then o &lt- Yes else o &lt;- No
     * return a decision list with initial test t and outcome o and remaining tests given by DECISION-LIST-LEARNING(examples - examples_t)
     * </pre>
     *
     * @param examples
     * @return
     */
    public al_DecisionListTests decisionListLearning(al_Examples examples) {
        if (examples.size() == 0) {
            return new al_DecisionListTests();
        }
        // find test that returns largest list of positive or negative results
        al_DecisionListTest t = this.getLargestClassifyingTest(examples);
        // check t
        if (t == null) {
            return new al_DecisionListTests();
        }
        //
        al_Examples matched = t.getMatchingExamples(examples);
        // set positive
        if (matched.examples.get(0).getOutput() != null) {
            t.setOutput(matched.examples.get(0).getOutput());
        } // set negative
        else {
            t.setOutput(null);
        }
        // return
        al_DecisionListTests list = new al_DecisionListTests();
        list.add(t);
        return list.mergeWith(this.decisionListLearning(t.getNonMatchingExamples(examples)));
    }

    /**
     * Return first test that classifies a subset of examples returning the same
     * value
     * @todo examine 2-, 3-, k-attribute tests
     *
     * @param examples
     * @return
     */
    private al_DecisionListTest getLargestClassifyingTest(al_Examples examples) {
        // attempt all single attribute tests
        for (d_PropertyListPair a : examples.getPossibleAttributes()) {
            al_DecisionListTest test = new al_DecisionListTest();
            test.add(a);
            al_Examples matched = test.getMatchingExamples(examples);
            if (matched.size() > 0 && this.allExamplesHaveSameOutput(matched)) {
                test.setOutput(matched.getExample(0).getOutput());
                return test;
            }
        }
        // return
        return null;
    }

    /**
     * Test whether all of the examples in an example set return the same output
     *
     * @param matched
     * @return
     */
    private boolean allExamplesHaveSameOutput(al_Examples matched) {
        Object value = matched.getExample(0).getOutput();
        for (al_Example e : matched.examples) {
            if (!e.getOutput().equals(value)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Trains the decision list given a set of examples
     *
     * @param examples
     */
    public void train(al_Examples examples) {
        this.decisionList = this.decisionListLearning(examples);
    }

    /**
     * Predict an outcome for the given example
     *
     * @param e
     * @return the example's output value, or null
     */
    public Object predict(al_Example e) {
        if (this.decisionList == null) {
            throw new RuntimeException("DecisionListLearner has not yet been trained with a data set.");
        }
        // predict and return
        return this.decisionList.predict(e);
    }

    /**
     * Returns the accuracy of the hypothesis on the specified set of examples
     *
     * @param examples the test data set.
     * @return the accuracy of the hypothesis on the specified set of examples
     * as an array like [#correct, #incorrect]
     */
    public int[] test(al_Examples examples) {
        int[] results = new int[]{0, 0};
        for (al_Example e : examples) {
            if (e.getOutput().equals(this.decisionList.predict(e))) {
                results[0]++;
            } else {
                results[1]++;
            }
        }
        return results;
    }
}