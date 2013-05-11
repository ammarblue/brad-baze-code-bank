package reuze.test;
//package aima.test.core.unit.learning.neural;

import java.io.File;
import java.util.ArrayList;

import junit.framework.Assert;
import junit.framework.TestCase;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import com.software.reuze.aln_ActivationFunctionHardLimit;
import com.software.reuze.aln_NeuralNetwork;
import com.software.reuze.d_ArrayUtils;

/**
 * Tests neural network functionality
 *
 * @author andrew
 */
public class NeuralNetworkTest extends TestCase {

    aln_NeuralNetwork n;
    private static ArrayList<Double> set(Double... values) {
    	ArrayList<Double> input = new ArrayList<Double>();
    	d_ArrayUtils.addArrayToCollection(input, values);
    	return input;
    }
    @Before
    public void setUp() {
        this.n = new aln_NeuralNetwork(3, 20, new aln_ActivationFunctionHardLimit(), 1.0);
    }

    /**
     * Example use of aln_NeuralNetwork
     */
    @Test
    public void testUse() throws Exception {
        ArrayList<Double> input = set(0.0, 0.1, 0.2, 0.3, 0.4, 0.5,
                0.6, 0.7, 0.8, 0.9, 0.10, 0.11, 0.12, 0.13, 0.14,
                0.15, 0.16, 0.17, 0.18, 0.19);
        // use
        ArrayList<Double> result = this.n.use(input);
        // test
        for (Double d : result) {
            Assert.assertTrue(d == 1.0 || d == 0.0);
        }
    }

    /**
     * Tests saving to file
     */
    @Test
    public void testSave() throws Exception {
        File file = new File("test.txt");
        file.createNewFile();
        aln_NeuralNetwork.save(file, this.n);
        System.out.println("Bytes written: " + file.length());
        if (file.length() < 10) {
            fail("No file data written");
        }
        file.delete();
    }

    /**
     * Tests loading from file
     */
    @Test
    public void testLoad() throws Exception {
        File file = new File("test2.txt");
        file.createNewFile();
        aln_NeuralNetwork.save(file, this.n);
        aln_NeuralNetwork actual = aln_NeuralNetwork.load(file);
        assertEquals(this.n.layers.size(), actual.layers.size());
        assertEquals(this.n.layers.get(0).perceptrons.get(0).weights,
                actual.layers.get(0).perceptrons.get(0).weights);
        file.delete();
    }

    /**
     * Demonstrates the functionality of aln_NeuralNetwork by implementing the
     * example two-bit adder from page 729, AIMA 3ed.
     *
     * @throws SizeDifferenceException
     * @throws WrongSizeException
     */
    @Test
    public void testBackPropagationAdder() throws RuntimeException {
        // setup
        aln_NeuralNetwork adder = new aln_NeuralNetwork(3, 2, new aln_ActivationFunctionHardLimit(), 1.0);
        ArrayList<Double>[] inputs = new ArrayList[4];
        inputs[0]=set(0.0,0.0);
        inputs[1]=set(0.0,1.0);
        inputs[2]=set(1.0,0.0);
        inputs[3]=set(1.0,1.0);
        ArrayList<Double>[] outputs = new ArrayList[4];
        outputs[0]=set(0.0,0.0);
        outputs[1]=set(0.0,1.0);
        outputs[2]=set(1.0,0.0);
        outputs[3]=set(1.0,1.0);
        // train
        int iterations = adder.train(inputs, outputs, 0.01);
        // test
        double expected = 0.01;
        double actual = adder.test(inputs, outputs);
        System.out.println("Error rate: " + actual + " after " + iterations + " iterations");
        if (actual > expected) {
            fail("Error rate " + actual + " must be less than " + expected);
        }
    }
}
