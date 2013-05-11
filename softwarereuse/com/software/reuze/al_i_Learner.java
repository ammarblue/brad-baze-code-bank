package com.software.reuze;
//package aima.core.learning.framework;

/**
 * Describes a model for learning based on a set of examples; see page 693-694
 * in AIMAv3 for further explanation. This model does not specify the types 
 * of feedback (un-supervised, reinforcement, supervised) used to train the 
 * learner.
 * @todo should implement aima.core.agent.Agent
 * @author Ravi Mohan
 * @author Andrew Brown
 */
public interface al_i_Learner {

    /**
     * Trains the learner given a set of examples
     * @param training_set the training data set 
     */
    void train(al_Examples training_set);

    /**
     * Returns the outcome predicted for the specified example
     * @param example an example
     * @return the outcome predicted for the specified example
     */
    <E> E predict(al_Example example);

    /**
     * Returns the accuracy of the hypothesis on the specified set of examples
     * @param test_set the test data set.
     * @return the accuracy of the hypothesis on the specified set of examples as an array like [#correct, #incorrect]
     */
    int[] test(al_Examples test_set);
}
