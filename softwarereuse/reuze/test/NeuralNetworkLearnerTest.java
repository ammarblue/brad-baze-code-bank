package reuze.test;
//package aima.test.core.unit.learning.learners;

import java.io.IOException;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Assert.*;
import org.junit.Test;

import com.software.reuze.al_Example;
import com.software.reuze.al_Examples;
import com.software.reuze.al_LearnerNeuralNetwork;
import com.software.reuze.aln_ActivationFunctionHardLimit;
import com.software.reuze.d_PropertyListPair;
import com.software.reuze.m_MathUtil2;

/**
 * NeuralNetworkLearner
 *
 * @author Andrew Brown
 */
public class NeuralNetworkLearnerTest extends TestCase {

    al_Examples irisData;
    al_LearnerNeuralNetwork learner;

    public NeuralNetworkLearnerTest() {
        // get iris data
        al_Examples _irisData = null;
        try {
            _irisData = DataSetTest.loadIrisData();
        } catch (IOException e) {
            Assert.fail("Could not load iris data from URL.");
        }
        // normalize
        int size = _irisData.size();
        double[] sepalLength = new double[size];
        double[] sepalWidth = new double[size];
        double[] petalLength = new double[size];
        double[] petalWidth = new double[size];
        for (int i = 0; i < _irisData.size(); i++) {
            sepalLength[i] = (Double) _irisData.getExample(i).get("sepal-length").getValue();
            sepalWidth[i] = (Double) _irisData.getExample(i).get("sepal-width").getValue();
            petalLength[i] = (Double) _irisData.getExample(i).get("petal-length").getValue();
            petalWidth[i] = (Double) _irisData.getExample(i).get("petal-width").getValue();
        }
        sepalLength = m_MathUtil2.normalize(sepalLength);
        sepalWidth = m_MathUtil2.normalize(sepalWidth);
        petalLength = m_MathUtil2.normalize(petalLength);
        petalWidth = m_MathUtil2.normalize(petalWidth);
        // setup iris data     
        this.irisData = new al_Examples();
        for (int i = 0; i < _irisData.size(); i++) {
            al_Example e = new al_Example();
            // set input
            Double[] input = {sepalLength[i], sepalWidth[i], petalLength[i], petalWidth[i]};
            // set output
            Double[] output = {0.0, 0.0, 0.0, 0.0};
            if (_irisData.getExample(i).getOutput().equals("setosa")) {
                output = new Double[]{1.0, 0.0, 0.0, 0.0};
            } else if (_irisData.getExample(i).getOutput().equals("versicolor")) {
                output = new Double[]{0.0, 1.0, 0.0, 0.0};
            } else if (_irisData.getExample(i).getOutput().equals("virginica")) {
                output = new Double[]{0.0, 0.0, 1.0, 0.0};
            }
            // create example
            e.add(new d_PropertyListPair<Double[]>("INPUT", input));
            e.setOutput(output);
            this.irisData.add(e);
        }

        // setup decision list learner
        this.learner = new al_LearnerNeuralNetwork(3, 4, new aln_ActivationFunctionHardLimit(), 0.1, 0.95);
    }

    /**
     * Trains a decision list with the restaurant data and tests its
     * effectiveness
     */
    @Test
    public void testTrain() {
        // train
        this.learner.train(irisData);
        // test
        int[] results = learner.test(irisData);
        double proportionCorrect = results[0] / irisData.size();
        System.out.println(proportionCorrect);
        Assert.assertTrue(proportionCorrect > 0.95);
    }
}