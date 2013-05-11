package com.software.reuze;
//package aima.core.learning.learners;

/*import aima.core.learning.framework.Attribute;
import aima.core.learning.framework.DataSet;
import aima.core.learning.framework.Example;
import aima.core.learning.inductive.DecisionTree;*/
import java.util.HashMap;

/**
 * Used by ADA-BOOST as a hypothesis, page 750, AIMAv3.
 *
 * @author Ravi Mohan
 * @author Andrew Brown
 */
public class al_LearnerDecisionTreeStump extends al_LearnerDecisionTree {
    
    /**
     * Constructor
     */
    public al_LearnerDecisionTreeStump(){
        super();
    }

    /**
     * Train the stump; these stumps branch to all possible values for a
     * randomly chosen attribute; the leaves are set as the most common output
     * found for the branch's attribute value; uses MajorityLearner
     *
     * @param examples
     */
    @Override
    public void train(al_Examples examples) {
        // randomly select attribute
        int attributeSpace = examples.getExample(0).getAttributes().length;
        int chosenAttribute = (int) Math.floor(Math.random() * attributeSpace);
        // randomly select example to train to
        int exampleSpace = examples.size();
        int chosenExample = (int) Math.floor(Math.random() * exampleSpace);
        // create tree
        al_Example e = examples.getExample(chosenExample);
        d_PropertyListPair a = e.getAttributes()[chosenAttribute].clone();
        a.setValue(null);
        this.trainedTree = new al_DecisionTreeBoolean(a);
        // create leaves
        HashMap<Object, al_Examples> leafSets = examples.splitBy(a.getName());
        for(Object attributeValue : leafSets.keySet()){
            al_LearnerMajority ml = new al_LearnerMajority();
            ml.train(leafSets.get(attributeValue));
            this.trainedTree.addLeaf(attributeValue, ml.result);
        }
    }
}
