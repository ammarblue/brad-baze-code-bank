package com.software.reuze;
//package aima.core.learning.neural;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Neural network layer
 *
 * @author Andrew Brown
 */
public class aln_Layer implements Iterable<aln_Perceptron>, Serializable {

    /**
     * List of perceptrons in this layer
     */
    public ArrayList<aln_Perceptron> perceptrons;

    /**
     * Constructor
     *
     * @param count
     * @param g
     */
    public aln_Layer(int count, aln_i_ActivationFunction g, double sensitivity) {
        // setup
        this.perceptrons = new ArrayList<aln_Perceptron>();
        // create perceptrons
        for (int i = 0; i < count; i++) {
            this.perceptrons.add(new aln_Perceptron(g, sensitivity));
        }
    }

    /**
     * Connects a layer to another layer; every perceptron is connected to every
     * perceptron in the next layer
     *
     * @param downstream
     */
    public void connectTo(aln_Layer downstream) {
        for (aln_Perceptron a : this.perceptrons) {
            for (aln_Perceptron b : downstream.perceptrons) {
                a.addOutput(b);
            }
        }
    }

    /**
     * Sends initial input data into the network
     *
     * @param input
     * @throws SizeDifferenceException
     */
    public void in(ArrayList<Double> input) throws RuntimeException {
        if (input.size() != this.size()) {
            throw new RuntimeException("Dataset size (" + input.size() + ") and Layer size (" + this.size() + ") do not match");
        }
        // send to perceptrons
        for (int i = 0; i < this.perceptrons.size(); i++) {
            this.perceptrons.get(i).in(input.get(i));
        }
    }

    /**
     * Receives final output data from the network
     *
     * @return
     */
    public ArrayList<Double> out() {
    	ArrayList<Double> output = new ArrayList<Double>(this.perceptrons.size());
        // wait until all processing is complete
        boolean complete;
        do {
            complete = true;
            for (int i = 0; i < this.perceptrons.size(); i++) {
                if (!this.perceptrons.get(i).isComplete()) {
                    complete = false;
                }
            }
        } while (!complete);
        // get values
        for (aln_Perceptron p : this.perceptrons) {
            output.add(p.result);
        }
        // return
        return output;
    }

    /**
     * Back-propagates errors from the given correct result
     *
     * @param expected_output
     * @throws SizeDifferenceException a
     */
    public void backpropagate(ArrayList<Double> expected_output) throws RuntimeException {
        if (expected_output.size() != this.size()) {
            throw new RuntimeException("Data size (" + expected_output.size() + ") and Layer size (" + this.size() + ") do not match");
        }
        // send to upstream perceptrons
        for (int i = 0; i < this.perceptrons.size(); i++) {
            this.perceptrons.get(i).backpropagate_in(expected_output.get(i));
        }
    }

    /**
     * Returns the number of perceptrons in this layer
     *
     * @return
     */
    public int size() {
        return this.perceptrons.size();
    }

    /**
     * Makes the layer Iterable
     *
     * @return
     */
    @Override
    public LayerIterator iterator() {
        return new LayerIterator();
    }

    /**
     * Iterator for the layer
     */
    private class LayerIterator implements Iterator<aln_Perceptron> {

        /**
         * Tracks the location in the list
         */
        private int index = 0;

        /**
         * Checks whether the list is empty or ended
         *
         * @return
         */
        @Override
        public boolean hasNext() {
            return (this.index < aln_Layer.this.perceptrons.size());
        }

        /**
         * Returns the next element
         *
         * @return
         */
        @Override
        public aln_Perceptron next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            aln_Perceptron next = aln_Layer.this.perceptrons.get(this.index);
            this.index++;
            return next;
        }

        /**
         * Removes an element; not supported in this implementation
         */
        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}