package com.software.reuze;
//package aima.core.learning.learners;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Andrew Brown
 */
public class al_LearnerNeuralNetwork implements al_i_Learner {

    aln_NeuralNetwork network;
    double target_error_rate;

    /**
     * Constructor
     *
     * @param hypotheses
     * @param weights
     */
    public al_LearnerNeuralNetwork(int number_of_layers, int perceptrons_per_layer, aln_i_ActivationFunction function, double sensitivity, double target_error_rate) {
        this.network = new aln_NeuralNetwork(number_of_layers, perceptrons_per_layer, function, sensitivity);
        this.target_error_rate = target_error_rate;
    }

    /**
     * Train the network to classify inputs; inputs must be passed as a Double[]
     * in the "INPUT" attribute of each al_Example; likewise, the expected output
     * must be passed as a Double[] in the al_Example output.
     *
     * @param examples
     */
    @Override
    public void train(al_Examples examples) {
        // check
        al_Example first = examples.getExample(0);
        if (!(first.get("INPUT").getValue() instanceof Double[])) {
            throw new RuntimeException("NeuralNetworkLearner inputs must be passed as a Double[] in the \"INPUT\" attribute");
        }
        if (!(first.getOutput() instanceof Double[])) {
            throw new RuntimeException("NeuralNetworkLearner output must be set to a Double[]");
        }
        // convert example set
        ArrayList<Double>[] inputs = new ArrayList[examples.size()];
        ArrayList<Double>[] outputs = new ArrayList[examples.size()];
        for (int i = 0; i < examples.size(); i++) {
            inputs[i] = set((Double[]) examples.getExample(i).get("INPUT").getValue());
            outputs[i] = set((Double[]) examples.getExample(i).getOutput());
        }
        // train
        this.network.train(inputs, outputs, this.target_error_rate);
    }

    /**
     * Predict an outcome for this example; return the most common value seen
     * thus far
     *
     * @param e
     * @return
     */
    @Override
    public Double[] predict(al_Example e) {
        // check
        if (!(e.get("INPUT").getValue() instanceof Double[])) {
            throw new RuntimeException("NeuralNetworkLearner inputs must be passed as a Double[] in the \"INPUT\" attribute");
        }
        ArrayList<Double> in = set((Double[]) e.get("INPUT").getValue());
        // use network
        try {
            ArrayList<Double> out = this.network.use(in);
            return out.toArray(new Double[out.size()]);
        } catch (RuntimeException ex) {
            return null;
        }
    }
    private static ArrayList<Double> set(Double... values) {
    	ArrayList<Double> input = new ArrayList<Double>();
    	d_ArrayUtils.addArrayToCollection(input, values);
    	return input;
    }

    /**
     * Returns the accuracy of the neural network on the specified set of
     * examples
     *
     * @param test_set the test data set.
     * @return the accuracy of the hypothesis on the specified set of examples
     * as an array like [#correct, #incorrect]
     */
    @Override
    public int[] test(al_Examples test_set) {
        // check
        al_Example first = test_set.getExample(0);
        if (!(first.get("INPUT").getValue() instanceof Double[])) {
            throw new RuntimeException("NeuralNetworkLearner inputs must be passed as a Double[] in the \"INPUT\" attribute");
        }
        if (!(first.getOutput() instanceof Double[])) {
            throw new RuntimeException("NeuralNetworkLearner output must be set to a Double[]");
        }
        // test
        int[] results = new int[]{0, 0};
        for (al_Example e : test_set) {
            System.out.println(Arrays.toString(this.predict(e)));
            System.out.println(Arrays.toString((Double[]) e.getOutput()));
            if (Arrays.equals((Double[]) e.getOutput(), this.predict(e))) {
                results[0]++;
            } else {
                results[1]++;
            }
        }
        // return
        return results;
    }
}
