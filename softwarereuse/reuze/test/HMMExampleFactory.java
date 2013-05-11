package reuze.test;
//package aima.core.probability.example;

import java.util.HashMap;
import java.util.Map;

import com.software.reuze.m_Matrix;
import com.software.reuze.mp_HMM;
import com.software.reuze.mp_i_HMM;


//import aima.core.probability.hmm.HiddenMarkovModel;
//import aima.core.probability.hmm.impl.HMM;
//import aima.core.util.math.Matrix;

/**
 * 
 * @author Ciaran O'Reilly
 * @author Ravi Mohan
 * 
 */
public class HMMExampleFactory {

	public static mp_i_HMM getUmbrellaWorldModel() {
		m_Matrix transitionModel = new m_Matrix(new double[][] { { 0.7, 0.3 },
				{ 0.3, 0.7 } });
		Map<Object, m_Matrix> sensorModel = new HashMap<Object, m_Matrix>();
		sensorModel.put(Boolean.TRUE, new m_Matrix(new double[][] { { 0.9, 0.0 },
				{ 0.0, 0.2 } }));
		sensorModel.put(Boolean.FALSE, new m_Matrix(new double[][] {
				{ 0.1, 0.0 }, { 0.0, 0.8 } }));
		m_Matrix prior = new m_Matrix(new double[] { 0.5, 0.5 }, 2);
		return new mp_HMM(ExampleRV.RAIN_t_RV, transitionModel, sensorModel, prior);
	}
}
