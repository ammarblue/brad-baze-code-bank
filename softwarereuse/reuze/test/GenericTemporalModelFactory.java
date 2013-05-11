package reuze.test;
//package aima.core.probability.example;

import java.util.HashMap;
import java.util.Map;

import com.software.reuze.mp_i_ModelFinite;
import com.software.reuze.mp_i_RandomVariable;
import com.software.reuze.mpb_ModelFinite;
import com.software.reuze.mpb_Network;
import com.software.reuze.mpb_NodeDiscreteFiniteFullCPT;
import com.software.reuze.mpb_i_NodeDiscreteFinite;


/*import aima.core.probability.FiniteProbabilityModel;
import aima.core.probability.RandomVariable;
import aima.core.probability.bayes.FiniteNode;
import aima.core.probability.bayes.impl.BayesNet;
import aima.core.probability.bayes.impl.FullCPTNode;
import aima.core.probability.bayes.model.FiniteBayesModel;*/

/**
 * 
 * @author Ciaran O'Reilly
 * 
 */
public class GenericTemporalModelFactory {

	public static mp_i_ModelFinite getUmbrellaWorldTransitionModel() {
		return getUmbrellaWorldModel();
	}

	public static mp_i_ModelFinite getUmbrellaWorldSensorModel() {
		return getUmbrellaWorldModel();
	}

	public static mp_i_ModelFinite getUmbrellaWorldModel() {
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

		return new mpb_ModelFinite(new mpb_Network(rain_tm1));
	}

	public static Map<mp_i_RandomVariable, mp_i_RandomVariable> getUmbrellaWorld_Xt_to_Xtm1_Map() {
		Map<mp_i_RandomVariable, mp_i_RandomVariable> tToTm1StateVarMap = new HashMap<mp_i_RandomVariable, mp_i_RandomVariable>();
		tToTm1StateVarMap.put(ExampleRV.RAIN_t_RV, ExampleRV.RAIN_tm1_RV);

		return tToTm1StateVarMap;
	}
}
