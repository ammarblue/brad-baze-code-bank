package com.software.reuze;
//package aima.core.learning.learners;

/*import aima.core.learning.framework.Attribute;
import aima.core.learning.framework.DataSet;
import aima.core.learning.framework.Example;
import aima.core.learning.framework.Learner;
import aima.core.learning.inductive.DecisionTree;
import aima.core.learning.inductive.DecisionTreeLeaf;*/
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Uses DECISION-TREE-LEARNING from page 702, AIMAv3, to induce decision trees
 * (see DecisionTree).
 *
 * @author Ravi Mohan
 * @author Mike Stampone
 * @author Andrew Brown
 */
public class al_LearnerDecisionTree implements al_i_Learner {

    protected al_DecisionTreeBoolean trainedTree;

    /**
     * Constructor
     */
    public al_LearnerDecisionTree() {
    }

    /**
     * Constructor: used when testing a non-induced tree (i.e. for testing)
     *
     * @param tree
     */
    public al_LearnerDecisionTree(al_DecisionTreeBoolean tree) {
        this.trainedTree = tree;
    }

    /**
     * Return the decision tree
     *
     * @return
     */
    public al_DecisionTreeBoolean getDecisionTree() {
        return trainedTree;
    }

    /**
     * Implements DECISION-TREE-LEARNING function from page 702 of AIMAv3:
     *
     * <pre><code>
     * function DECISION-TREE-LEARNING(examples, attributes, parent_examples) returns a tree
     *  if examples is empty then return PLURALITY-VALUE(parent_examples)
     *  else if all examples have the same classification then return the classification
     *  else if attributes is empty then return PLURALITY-VALUE(examples)
     *  else
     *      A = argmax_(a|attributes) IMPORTANCE(a, examples)
     *      tree = a new decision tree with root test A
     *      for each value v_k of A do
     *          exs = {e : e|examples and e.A = v_k}
     *          subtree = DECISION-TREE-LEARNING(exs, attributes - A, examples)
     *          add a branch to tree with label (A = v_k) and subtree subtree
     *      return tree
     * </code></pre>
     *
     * @param ds
     * @param attributeNames
     * @param defaultTree
     * @return
     */
    private al_DecisionTreeBoolean decisionTreeLearning(al_Examples examples, List<String> attributeNames, al_Examples parent_examples) {
        if (examples.size() == 0) {
            return new al_DecisionTreeBooleanLeaf(this.getPluralityValue(parent_examples));
        }
        if (this.allExamplesHaveSameClassification(examples)) {
            return new al_DecisionTreeBooleanLeaf(examples.getExample(0).getOutput());
        }
        if (attributeNames.isEmpty()) {
            return new al_DecisionTreeBooleanLeaf(this.getPluralityValue(examples));
        }
        // A <- argmax_(a in attributes) IMPORTANCE(a, examples)
        d_PropertyListPair A = new d_PropertyListPair(this.getMostImportantAttribute(attributeNames, examples), null);
        al_DecisionTreeBoolean tree = new al_DecisionTreeBoolean(A);
        for (Object value : examples.getValuesOf(A.getName())) {
            al_Examples exs = examples.find(A.getName(), value);
            attributeNames.remove(A.getName()); // attributes - A
            al_DecisionTreeBoolean subtree = this.decisionTreeLearning(exs, attributeNames, examples);
            tree.addBranch(value, subtree);
        }
        return tree;
    }

    /**
     * Return plurality classification, "the most common output value among a
     * set of examples, breaking ties randomly" page 702, AIMAv3. Uses
     * MajorityLearner
     *
     * @param examples
     * @return
     */
    private al_DecisionTreeBooleanLeaf getPluralityValue(al_Examples examples) {
        al_i_Learner learner = new al_LearnerMajority();
        learner.train(examples);
        return new al_DecisionTreeBooleanLeaf(learner.predict(null));
    }

    /**
     * Find the most important attribute; this method corresponds to "argmax_(a
     * in attributes) IMPORTANCE(a, examples)" on page 702, AIMAv3. IMPORTANCE
     * is implemented as the information gain of the attribute, see page 703.
     *
     * @param examples
     * @param attributeNames
     * @return
     */
    public String getMostImportantAttribute(List<String> attributeNames, al_Examples examples) {
        double greatestGain = 0.0;
        String attributeWithGreatestGain = attributeNames.get(0);
        for (String attributeName : attributeNames) {
            // IMPORTANCE(a, examples) is implemented as the information gain of the attribute, page 703
            double gain = examples.getInformationGainOf(attributeName);
            if (gain > greatestGain) {
                greatestGain = gain;
                attributeWithGreatestGain = attributeName;
            }
        }
        return attributeWithGreatestGain;
    }

    /**
     * Test the example set for classification; if all examples have the same
     * output, return true.
     *
     * @param examples
     * @return
     */
    private boolean allExamplesHaveSameClassification(al_Examples examples) {
        Object classification = examples.getExample(0).getOutput();
        Iterator<al_Example> iter = examples.iterator();
        while (iter.hasNext()) {
            al_Example element = iter.next();
            if (!element.getOutput().equals(classification)) {
                return false;
            }

        }
        return true;
    }

    /**
     * Induces the decision tree from the specified set of examples
     *
     * @param examples a set of examples for constructing the decision tree
     */
    public void train(al_Examples examples) {
        // get attribute names
        ArrayList<String> attributes = new ArrayList<String>();
        for (d_PropertyListPair a : examples.getExample(0).getAttributes()) {
            attributes.add(a.getName());
        }
        // train
        this.trainedTree = decisionTreeLearning(examples, attributes, null);
    }

    /**
     * Predict a result using the trained decision tree
     *
     * @param e
     * @return
     */
    public <T> T predict(al_Example e) {
        if (this.trainedTree == null) {
            throw new RuntimeException("DecisionTreeLearner has not yet been trained with an example set.");
        }
        return (T) this.trainedTree.predict(e);
    }

    /**
     * Return the accuracy of the decision tree on the specified set of
     * examples
     *
     * @param examples
     * @return
     */
    public int[] test(al_Examples examples) {
        int[] results = new int[]{0, 0};
        for (al_Example e : examples) {
            if (e.getOutput().equals(this.trainedTree.predict(e))) {
                results[0]++;
            } else {
                results[1]++;
            }
        }
        return results;
    }
}
