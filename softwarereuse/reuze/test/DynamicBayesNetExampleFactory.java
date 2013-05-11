package reuze.test;
//package aima.core.probability.example;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.software.reuze.mp_i_RandomVariable;
import com.software.reuze.mpb_Network;
import com.software.reuze.mpb_NetworkDynamic;
import com.software.reuze.mpb_NodeDiscreteFiniteFullCPT;
import com.software.reuze.mpb_i_NetworkDynamic;
import com.software.reuze.mpb_i_NodeDiscreteFinite;


/*import aima.core.probability.RandomVariable;
import aima.core.probability.bayes.DynamicBayesianNetwork;
import aima.core.probability.bayes.FiniteNode;
import aima.core.probability.bayes.impl.BayesNet;
import aima.core.probability.bayes.impl.DynamicBayesNet;
import aima.core.probability.bayes.impl.FullCPTNode;*/

/**
 * 
 * @author Ciaran O'Reilly
 *
 */
public class DynamicBayesNetExampleFactory {
	/**
	 * Return a Dynamic Bayesian Network of the Umbrella World Network.
	 * 
	 * @return a Dynamic Bayesian Network of the Umbrella World Network.
	 */
	public static mpb_i_NetworkDynamic getUmbrellaWorldNetwork() {
		mpb_i_NodeDiscreteFinite prior_rain_tm1 = new mpb_NodeDiscreteFiniteFullCPT(ExampleRV.RAIN_tm1_RV,
				new double[] { 0.5, 0.5 });

		mpb_Network priorNetwork = new mpb_Network(prior_rain_tm1);

		// Prior belief state
		mpb_i_NodeDiscreteFinite rain_tm1 = new mpb_NodeDiscreteFiniteFullCPT(ExampleRV.RAIN_tm1_RV,
				new double[] { 0.5, 0.5 });
		// Transition Model
		mpb_i_NodeDiscreteFinite rain_t = new mpb_NodeDiscreteFiniteFullCPT(ExampleRV.RAIN_t_RV, new double[] {
				// R_t-1 = true, R_t = true
				0.7,
				// R_t-1 = true, R_t = false
				0.3,
				// R_t-1 = false, R_t = true
				0.3,
				// R_t-1 = false, R_t = false
				0.7 }, rain_tm1);
		// Sensor Model
		@SuppressWarnings("unused")
		mpb_i_NodeDiscreteFinite umbrealla_t = new mpb_NodeDiscreteFiniteFullCPT(ExampleRV.UMBREALLA_t_RV,
				new double[] {
						// R_t = true, U_t = true
						0.9,
						// R_t = true, U_t = false
						0.1,
						// R_t = false, U_t = true
						0.2,
						// R_t = false, U_t = false
						0.8 }, rain_t);

		Map<mp_i_RandomVariable, mp_i_RandomVariable> X_0_to_X_1 = new HashMap<mp_i_RandomVariable, mp_i_RandomVariable>();
		X_0_to_X_1.put(ExampleRV.RAIN_tm1_RV, ExampleRV.RAIN_t_RV);
		Set<mp_i_RandomVariable> E_1 = new HashSet<mp_i_RandomVariable>();
		E_1.add(ExampleRV.UMBREALLA_t_RV);

		return new mpb_NetworkDynamic(priorNetwork, X_0_to_X_1, E_1, rain_tm1);
	}
}
