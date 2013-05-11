package com.software.reuze;
//package aima.core.learning.neural;

import java.io.Serializable;

/**
 * @author Ravi Mohan
 * @author Andrew Brown
 */
public class aln_ActivationFunctionPureLinear implements aln_i_ActivationFunction, Serializable {

    @Override
    public double activate(double parameter) {
        return parameter;
    }

    @Override
    public double derivate(double parameter) {
        return 1;
    }
}
