package com.software.reuze;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 127.<br>
 * <br>
 * Each state is rated by the objective function, or (in Genetic Algorithm
 * terminology) the fitness function. A fitness function should return higher
 * values for better states.
 * 
 * @author Ciaran O'Reilly
 * 
 */
public interface a_i_FitnessFunction {
	double getValue(String individual);
}
