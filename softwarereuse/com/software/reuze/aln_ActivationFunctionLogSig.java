package com.software.reuze;
//package aima.core.learning.neural;

import java.io.Serializable;

/**
 * @author Ravi Mohan
 * @author Andrew Brown
 */
public class aln_ActivationFunctionLogSig implements aln_i_ActivationFunction, Serializable {

    @Override
    public double activate(double parameter) {
        return 1.0 / (1.0 + Math.pow(Math.E, (-1.0 * parameter)));
    }

    @Override
    public double derivate(double parameter) {
        // parameter = induced field
        // e == activation
        double e = 1.0 / (1.0 + Math.pow(Math.E, (-1.0 * parameter)));
        return e * (1.0 - e);
    }
}
