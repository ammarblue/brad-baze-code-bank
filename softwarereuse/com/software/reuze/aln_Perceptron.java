package com.software.reuze;
//package aima.core.learning.neural;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Neural network perceptron
 *
 * @author Andrew Brown
 */
public class aln_Perceptron implements Iterable, Serializable {

    public ArrayList<aln_Perceptron> inputs;
    public ArrayList<aln_Perceptron> outputs;
    public HashMap<aln_Perceptron, Double> weights;
    public HashMap<aln_Perceptron, Double> activation;
    public HashMap<aln_Perceptron, Double> error;
    public aln_i_ActivationFunction function;
    public double bias;
    public double result;
    public double sensitivity;
    public byte state;

    /**
     * Constructor
     *
     * @param g
     */
    public aln_Perceptron(aln_i_ActivationFunction g, double sensitivity) {
        this.inputs = new ArrayList<aln_Perceptron>();
        this.outputs = new ArrayList<aln_Perceptron>();
        this.weights = new HashMap<aln_Perceptron, Double>();
        this.activation = new HashMap<aln_Perceptron, Double>();
        this.error = new HashMap<aln_Perceptron, Double>();
        this.function = g;
        this.bias = 0.0d;
        this.sensitivity = sensitivity;
        this.state = aln_NeuralNetwork.WAITING;
    }

    /**
     * Constructor
     */
    public aln_Perceptron() {
        this.inputs = new ArrayList<aln_Perceptron>();
        this.outputs = new ArrayList<aln_Perceptron>();
        this.weights = new HashMap<aln_Perceptron, Double>();
        this.activation = new HashMap<aln_Perceptron, Double>();
        this.error = new HashMap<aln_Perceptron, Double>();
        this.bias = 0.0d;
        this.sensitivity = 1.0d;
        this.state = aln_NeuralNetwork.WAITING;
    }

    /**
     * Adds input perceptron with a random weight
     *
     * @param p
     */
    public void addInput(aln_Perceptron p) {
        double weight = Math.random() - 0.5;
        this.addInput(p, weight);
    }

    /**
     * Adds input perceptron
     *
     * @param p
     * @param weight
     */
    public void addInput(aln_Perceptron p, double weight) {
        this.weights.put(p, weight);
        this.inputs.add(p);
        // bi-directional connection
        p.outputs.add(this);
    }

    /**
     * Adds output perceptron
     *
     * @param p
     */
    public void addOutput(aln_Perceptron p) {
        this.outputs.add(p);
        // bi-directional connection
        p.weights.put(this, Math.random() - 0.5);
        p.inputs.add(this);
    }

    /**
     * Receives inputs from the governing application; implies there are no
     * input perceptrons to this one; makes this perceptron an input node
     *
     * @param d an input value
     */
    public void in(double d) throws RuntimeException {
        // warning
        if (d > 1.0 || d < 0.0) {
            throw new RuntimeException("Perceptron inputs must fall between 0.0 and 1.0");
        }
        // change state
        this.state = aln_NeuralNetwork.RECEIVING;
        // debug
        if (aln_NeuralNetwork.DEBUG) {
            System.out.println("Application -[" + d + "]-> " + this);
        }
        // create stub perceptron if necessary
        this.out(d);
    }

    /**
     * Accepts inputs from an upstream perceptron
     *
     * @param p
     * @param d
     */
    public void in(aln_Perceptron p, double d) {
        // change state
        this.state = aln_NeuralNetwork.RECEIVING;
        // debug
        if (aln_NeuralNetwork.DEBUG) {
            System.out.println(p + " -[" + d + "]-> " + this);
        }
        // input
        this.activation.put(p, d);
        // send downstream once full
        if (this.activation.size() == this.inputs.size()) {
            // TODO: perhaps just test for activation here, not wait for all inputs to come in; this might be necessary in recurrent networks
            this.out();
        }
    }

    /**
     * Send immediately, without activation function, the passed value; used for
     * sending data from governing application; must only be used if this
     * perceptron is an input node
     *
     * @param d
     */
    public void out(double d) {
        // change state
        this.state = aln_NeuralNetwork.SENDING;
        // set result
        this.result = d;
        // send to outputs
        for (aln_Perceptron output : this.outputs) {
            output.in(this, this.result);
        }
        // change state
        this.state = aln_NeuralNetwork.WAITING;
    }

    /**
     * Consumes input values and produces output signal to downstream
     * perceptrons.
     */
    public void out() {
        // change state
        this.state = aln_NeuralNetwork.SENDING;
        // sum inputs
        double sum = 0.0;
        for (aln_Perceptron p : this.inputs) {
            sum += this.weights.get(p) * this.activation.get(p);
        }
        // add bias
        sum += this.bias;
        // get activation function
        this.result = this.function.activate(sum);
        // debug
        if (aln_NeuralNetwork.DEBUG) {
            System.out.println(this + " = [" + this.result + "]");
        }
        // send result to output perceptrons
        for (aln_Perceptron p : this.outputs) {
            p.in(this, this.result);
        }
        // change state
        this.state = aln_NeuralNetwork.WAITING;
    }

    /**
     * Receives errors (w_i,j * delta_j) from downstream perceptrons and stores
     * them until a backpropagation can be made. Please note differences from
     * AIMA 18.24, p.734, which assumes a fully-connected network and the use of
     * vectors.
     *
     * @param p
     * @param error
     */
    public void backpropagate_in(aln_Perceptron p, double error) {
        // save delta
        this.error.put(p, error);
        // send upstream once full
        if (this.error.size() == this.outputs.size()) {
            this.backpropagate_out();
        }
    }

    /**
     * Receives the correct training result from the application and calculates
     * the error, backpropagating it to upstream perceptrons. Please note
     * differences from AIMA 18.24, p.734, which assumes a fully-connected
     * network and the use of vectors.
     *
     * @param d correct result from example set
     */
    public void backpropagate_in(double y) {
        // calculate delta
        double _error = y - this.result; // y_j - a_j
        this.error.put(null, _error);
        // send upstream immediately
        this.backpropagate_out();
    }

    /**
     * Sends backpropagation results upstream, modifying each perceptrons
     * weights on the way. Please note differences from AIMA 18.24, p.734, which
     * assumes a fully-connected network and the use of vectors.
     */
    public void backpropagate_out() {
        // change state
        this.state = aln_NeuralNetwork.TRAINING;
        // calculate activation (in_i = sum(a_i))
        double activation_sum = 0.0;
        if (this.inputs.size() > 0) {
            // case: inputs from upstream
            for (aln_Perceptron p : this.inputs) {
                activation_sum += this.activation.get(p);
            }
        } else {
            // case: input from application
            activation_sum = this.result;
        }
        // calculate delta
        double delta = 0.0;
        // case: many outputs, sum error
        if (this.outputs.size() > 0) {
            double error_sum = 0.0;
            for (aln_Perceptron p : this.outputs) {
                error_sum += this.error.get(p); // sum w_i,j * delta_j
            }
            delta = this.function.derivate(activation_sum) * error_sum; // TODO: check this, g'(in_i) * sum(w_i,j * delta_j)
        } // case: output node, get error delta
        else {
            delta = this.function.derivate(activation_sum) * this.error.get(null);
        }
        // send upstream and modify weights
        for (aln_Perceptron p : this.inputs) {
            // send upstream
            double d = this.weights.get(p) * delta; // send w_i,j * delta_j
            p.backpropagate_in(this, d);
            // debug
            if (aln_NeuralNetwork.DEBUG) {
                System.out.println(p + " <-[" + d + "]- " + this);
            }
            // modify weights
            double new_weight = this.weights.get(p) + this.sensitivity * this.activation.get(p) * delta; // calculate w_i,j + alpha * a_i * delta_j
            this.weights.put(p, new_weight);
        }
        // change state
        this.state = aln_NeuralNetwork.WAITING;
    }

    /**
     * Tests whether this perceptron has sent out an activation value
     *
     * @return
     */
    public boolean isComplete() {
        return (this.state == aln_NeuralNetwork.WAITING);
    }

    /**
     * Returns string representation of a perceptron
     */
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("Perceptron@");
        s.append(Integer.toHexString(this.hashCode()));
        return s.toString();
    }

    /**
     * Prints debugging information for this perceptron
     */
    public void debug() {
        StringBuilder s = new StringBuilder("Debugging ");
        s.append(this.toString());
        // inputs
        s.append("\n  Inputs:\n");
        for (aln_Perceptron p : this.inputs) {
            s.append("    ");
            s.append(p);
            if (this.activation.containsKey(p)) {
                s.append(" sent ");
                s.append(this.activation.get(p));
            }
            if (this.weights.containsKey(p)) {
                s.append(" to weight ");
                s.append(this.weights.get(p));
            }
            s.append("\n");
        }
        // outputs
        s.append("  Result:\n");
        s.append("   ");
        s.append(this.result);
        // print
        System.out.println(s.toString());
    }

    /**
     * Makes the perceptron's inputs iterable
     *
     * @return
     */
    @Override
    public InputIterator iterator() {
        return new InputIterator();
    }

    /**
     * Iterator for this perceptron; iterates over input perceptrons
     */
    private class InputIterator implements Iterator<aln_Perceptron> {

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
            return (this.index < aln_Perceptron.this.inputs.size());
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
            aln_Perceptron next = aln_Perceptron.this.inputs.get(this.index);
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
