package com.software.reuze;
//package aima.core.learning.learners;

/*import aima.core.learning.framework.DataSet;
import aima.core.learning.framework.Example;
import aima.core.learning.framework.Learner;
import aima.core.util.Util;*/
import java.util.Arrays;


/**
 * Implements ADABOOST algorithm from page 751, AIMAv3.
 *
 * @author Ravi Mohan
 * @author Andrew Brown
 */
public class al_LearnerAdaBoost implements al_i_Learner {

    private Class<? extends al_i_Learner> learnerType;
    private int learnerCount;
    private al_LearnerWeightedMajority ensemble;

    /**
     * Constructor; uses page 750 defaults
     */
    public al_LearnerAdaBoost() {
        this.learnerType = al_LearnerDecisionTreeStump.class;
        this.learnerCount = 5;
    }

    /**
     * Constructor
     *
     * @param learnerType
     * @param learnerCount
     */
    public al_LearnerAdaBoost(Class<? extends al_i_Learner> learnerType, int learnerCount) {
        this.learnerType = learnerType;
        this.learnerCount = learnerCount;
    }

    /**
     * Return ensemble
     *
     * @return
     */
    public al_LearnerWeightedMajority getEnsemble() {
        return this.ensemble;
    }

    /**
     * Train the learners according to page 751, AIMAv3.
     * <pre><code>
     * function ADABOOST(examples, L, K) returns a weighted-majority hypothesis
     *  inputs: examples, a set of N labeled examples (x_1, y_1), ..., (x_N, y_N)
     *      L, a learning algorithm
     *      K, the number of hypothesis in the ensemble
     *  local variables: w, a vector of N example weights, initially 1/N
     *      h, a vector of K hypotheses
     *      z, a vector of K hypothesis weights
     *
     *  for k = 1 to K do
     *      h[k] &lt;- L(examples, w)
     *      error &lt;- 0
     *      for j = 1 to N do
     *          if h[k](x_j) != y_j then error &lt;- error + w[j]
     *      for j = 1 to N do
     *          if h[k](x_j) = y_j then w[j] &lt;- w[j] * error/(1 - error)
     *      w &lt;- NORMALIZE(w)
     *      z[k] &lt;- log (1-error)/error
     *  return WEIGHTED-MAJORITY(h, z)
     * </code></pre>
     *
     * @param examples
     */
    public al_LearnerWeightedMajority adaBoost(al_Examples examples, Class<? extends al_i_Learner> L, int K) {
        // initialize local variables
        int N = examples.size();
        double[] w = new double[N];
        Arrays.fill(w, 1.0/N);  //RPC1012-changed from 1, above comment says 1/N
        al_i_Learner[] h = new al_i_Learner[K];
        try {
            for(int k=0; k < K; k++){
                h[k] = L.getConstructor().newInstance();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        double[] z = new double[K];
        // for each K
        for (int k = 0; k < K; k++) {
            // @todo h[k] <- L(examples, w); w not really implemented here; each Learner will be identical
            // @todo try WeightedLearner, where we combine Learner and a weight set for examples; or try a replicated training set, page 749
            h[k].train(examples); 
            double error = 0;
            for (int j = 0; j < N; j++) {
                Object x_j = h[k].predict(examples.getExample(j));
                Object y_j = examples.getExample(j).getOutput();
                if (!x_j.equals(y_j)) {
                    error = error + w[j];
                }
            }
            for (int j = 0; j < N; j++) {
                Object x_j = h[k].predict(examples.getExample(j));
                Object y_j = examples.getExample(j).getOutput();
                if (x_j.equals(y_j)) {
                    w[j] = w[j] * (error / (1 - error));
                }
            }
            w = m_MathUtil2.normalize(w);
            z[k] = Math.log((1 - error) / error);
        }
        // return
        return new al_LearnerWeightedMajority(h, z);
    }

    /**
     * Train this learning ensemble
     *
     * @param examples
     */
    public void train(al_Examples examples) {
        this.ensemble = this.adaBoost(examples, this.learnerType, this.learnerCount);
    }

    /**
     * Predict a result based on the trained ensemble
     *
     * @param e
     * @return
     */
    public <T> T predict(al_Example e) {
        if (this.ensemble == null) {
            throw new RuntimeException("AdaBoostLearner has not yet been trained.");
        }
        return (T) this.ensemble.predict(e);
    }

    /**
     * Return the accuracy of the decision tree on the specified set of examples
     *
     * @param examples
     * @return
     */
    public int[] test(al_Examples examples) {
        int[] results = new int[]{0, 0};
        for (al_Example e : examples) {
        	String s=(String)e.getOutput(), t=(String)this.predict(e);
            if (e.getOutput().equals(this.predict(e))) {
                results[0]++;
            } else {
                results[1]++;
            }
        }
        return results;
    }
}
