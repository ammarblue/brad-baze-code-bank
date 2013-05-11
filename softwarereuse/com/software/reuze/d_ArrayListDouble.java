package com.software.reuze;
//package aima.core.learning.neural;

import java.util.ArrayList;

/**
 * Extends an ArrayList to simplify network input and output; with this class,
 * we avoid multi-dimensional arrays and simplify creation of network inputs.
 *
 * @author Andrew Brown
 */
public class d_ArrayListDouble extends ArrayList<Double> {

    /**
     * Difference between doubles under which they will still be considered
     * equal
     */
    public static double tolerance = 0.000001;

    /**
     * Constructor; pass elements as parameters @example DataList d = new
     * DataList(0.1, 0.2, 0.3, 0.4);
     *
     * @param set
     */
    public d_ArrayListDouble(Double... values) {
        for (Double d : values) {
            this.add(d);
        }
    }

    /**
     * Constructor
     *
     * @param set
     */
    public d_ArrayListDouble(double[] set) {
        for (double d : set) {
            this.add(new Double(d));
        }
    }

    /**
     * Constructor
     *
     * @param set
     */
    public d_ArrayListDouble(ArrayList<Double> set) {
        for (Double d : set) {
            this.add(d);
        }
    }

    /**
     * Constructor
     *
     * @param size
     */
    public d_ArrayListDouble(int size) {
        super(size);
    }
    //TODO should be moved out and this class deleted
    /**
     * Calculate error rate between two data sets
     *
     * @param actual
     * @param expected
     * @return
     * @throws SizeDifferenceException
     */
    public static double getErrorRate(d_ArrayListDouble actual, d_ArrayListDouble expected) throws RuntimeException {
        if (actual.size() != expected.size()) {
            throw new RuntimeException("To compare datasets, they must be the same size");
        }
        // see page 708: "define the error rate of actual hypothesis as the proportion of mistakes it makes"
        int errors = 0;
        for (int i = 0; i < actual.size(); i++) {
            if (Math.abs(actual.get(i) - expected.get(i)) < d_ArrayListDouble.tolerance) {
                errors++;
            }
        }
        // return
        return errors / actual.size();
    }

    /**
     * To Double Array
     *
     * @return
     */
    @Override
    public Double[] toArray() {
        return this.toArray(new Double[1]);
    }
}
