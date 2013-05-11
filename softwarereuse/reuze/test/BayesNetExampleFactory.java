package reuze.test;
import com.software.reuze.mpb_Network;
import com.software.reuze.mpb_NodeDiscreteFiniteFullCPT;
import com.software.reuze.mpb_i_Network;
import com.software.reuze.mpb_i_NodeDiscreteFinite;

//package aima.core.probability.example;

/*import aima.core.probability.bayes.BayesianNetwork;
import aima.core.probability.bayes.FiniteNode;
import aima.core.probability.bayes.impl.BayesNet;
import aima.core.probability.bayes.impl.FullCPTNode;*/

/**
 * 
 * @author Ciaran O'Reilly
 *
 */
public class BayesNetExampleFactory {
	public static mpb_i_Network construct2FairDiceNetwor() {
		mpb_i_NodeDiscreteFinite dice1 = new mpb_NodeDiscreteFiniteFullCPT(ExampleRV.DICE_1_RV, new double[] {
				1.0 / 6.0, 1.0 / 6.0, 1.0 / 6.0, 1.0 / 6.0, 1.0 / 6.0,
				1.0 / 6.0 });
		mpb_i_NodeDiscreteFinite dice2 = new mpb_NodeDiscreteFiniteFullCPT(ExampleRV.DICE_2_RV, new double[] {
				1.0 / 6.0, 1.0 / 6.0, 1.0 / 6.0, 1.0 / 6.0, 1.0 / 6.0,
				1.0 / 6.0 });

		return new mpb_Network(dice1, dice2);
	}

	public static mpb_i_Network constructToothacheCavityCatchNetwork() {
		mpb_i_NodeDiscreteFinite cavity = new mpb_NodeDiscreteFiniteFullCPT(ExampleRV.CAVITY_RV, new double[] {
				0.2, 0.8 });
		@SuppressWarnings("unused")
		mpb_i_NodeDiscreteFinite toothache = new mpb_NodeDiscreteFiniteFullCPT(ExampleRV.TOOTHACHE_RV,
				new double[] {
						// C=true, T=true
						0.6,
						// C=true, T=false
						0.4,
						// C=false, T=true
						0.1,
						// C=false, T=false
						0.9

				}, cavity);
		@SuppressWarnings("unused")
		mpb_i_NodeDiscreteFinite catchN = new mpb_NodeDiscreteFiniteFullCPT(ExampleRV.CATCH_RV, new double[] {
				// C=true, Catch=true
				0.9,
				// C=true, Catch=false
				0.1,
				// C=false, Catch=true
				0.2,
				// C=false, Catch=false
				0.8 }, cavity);

		return new mpb_Network(cavity);
	}

	public static mpb_i_Network constructToothacheCavityCatchWeatherNetwork() {
		mpb_i_NodeDiscreteFinite cavity = new mpb_NodeDiscreteFiniteFullCPT(ExampleRV.CAVITY_RV, new double[] {
				0.2, 0.8 });
		@SuppressWarnings("unused")
		mpb_i_NodeDiscreteFinite toothache = new mpb_NodeDiscreteFiniteFullCPT(ExampleRV.TOOTHACHE_RV,
				new double[] {
						// C=true, T=true
						0.6,
						// C=true, T=false
						0.4,
						// C=false, T=true
						0.1,
						// C=false, T=false
						0.9

				}, cavity);
		@SuppressWarnings("unused")
		mpb_i_NodeDiscreteFinite catchN = new mpb_NodeDiscreteFiniteFullCPT(ExampleRV.CATCH_RV, new double[] {
				// C=true, Catch=true
				0.9,
				// C=true, Catch=false
				0.1,
				// C=false, Catch=true
				0.2,
				// C=false, Catch=false
				0.8 }, cavity);
		mpb_i_NodeDiscreteFinite weather = new mpb_NodeDiscreteFiniteFullCPT(ExampleRV.WEATHER_RV,
				new double[] {
						// sunny
						0.6,
						// rain
						0.1,
						// cloudy
						0.29,
						// snow
						0.01 });

		return new mpb_Network(cavity, weather);
	}

	public static mpb_i_Network constructMeningitisStiffNeckNetwork() {
		mpb_i_NodeDiscreteFinite meningitis = new mpb_NodeDiscreteFiniteFullCPT(ExampleRV.MENINGITIS_RV,
				new double[] { 1.0 / 50000.0, 1.0 - (1.0 / 50000.0) });
		@SuppressWarnings("unused")
		mpb_i_NodeDiscreteFinite stiffneck = new mpb_NodeDiscreteFiniteFullCPT(ExampleRV.STIFF_NECK_RV,
				new double[] {
						// M=true, S=true
						0.7,
						// M=true, S=false
						0.3,
						// M=false, S=true
						0.009986199723994478,
						// M=false, S=false
						0.9900138002760055

				}, meningitis);
		return new mpb_Network(meningitis);
	}

	public static mpb_i_Network constructBurglaryAlarmNetwork() {
		mpb_i_NodeDiscreteFinite burglary = new mpb_NodeDiscreteFiniteFullCPT(ExampleRV.BURGLARY_RV,
				new double[] { 0.001, 0.999 });
		mpb_i_NodeDiscreteFinite earthquake = new mpb_NodeDiscreteFiniteFullCPT(ExampleRV.EARTHQUAKE_RV,
				new double[] { 0.002, 0.998 });
		mpb_i_NodeDiscreteFinite alarm = new mpb_NodeDiscreteFiniteFullCPT(ExampleRV.ALARM_RV, new double[] {
				// B=true, E=true, A=true
				0.95,
				// B=true, E=true, A=false
				0.05,
				// B=true, E=false, A=true
				0.94,
				// B=true, E=false, A=false
				0.06,
				// B=false, E=true, A=true
				0.29,
				// B=false, E=true, A=false
				0.71,
				// B=false, E=false, A=true
				0.001,
				// B=false, E=false, A=false
				0.999 }, burglary, earthquake);
		@SuppressWarnings("unused")
		mpb_i_NodeDiscreteFinite johnCalls = new mpb_NodeDiscreteFiniteFullCPT(ExampleRV.JOHN_CALLS_RV,
				new double[] {
						// A=true, J=true
						0.90,
						// A=true, J=false
						0.10,
						// A=false, J=true
						0.05,
						// A=false, J=false
						0.95 }, alarm);
		@SuppressWarnings("unused")
		mpb_i_NodeDiscreteFinite maryCalls = new mpb_NodeDiscreteFiniteFullCPT(ExampleRV.MARY_CALLS_RV,
				new double[] {
						// A=true, M=true
						0.70,
						// A=true, M=false
						0.30,
						// A=false, M=true
						0.01,
						// A=false, M=false
						0.99 }, alarm);

		return new mpb_Network(burglary, earthquake);
	}

	public static mpb_i_Network constructCloudySprinklerRainWetGrassNetwork() {
		mpb_i_NodeDiscreteFinite cloudy = new mpb_NodeDiscreteFiniteFullCPT(ExampleRV.CLOUDY_RV, new double[] {
				0.5, 0.5 });
		mpb_i_NodeDiscreteFinite sprinkler = new mpb_NodeDiscreteFiniteFullCPT(ExampleRV.SPRINKLER_RV,
				new double[] {
						// Cloudy=true, Sprinkler=true
						0.1,
						// Cloudy=true, Sprinkler=false
						0.9,
						// Cloudy=false, Sprinkler=true
						0.5,
						// Cloudy=false, Sprinkler=false
						0.5 }, cloudy);
		mpb_i_NodeDiscreteFinite rain = new mpb_NodeDiscreteFiniteFullCPT(ExampleRV.RAIN_RV, new double[] {
				// Cloudy=true, Rain=true
				0.8,
				// Cloudy=true, Rain=false
				0.2,
				// Cloudy=false, Rain=true
				0.2,
				// Cloudy=false, Rain=false
				0.8 }, cloudy);
		@SuppressWarnings("unused")
		mpb_i_NodeDiscreteFinite wetGrass = new mpb_NodeDiscreteFiniteFullCPT(ExampleRV.WET_GRASS_RV,
				new double[] {
						// Sprinkler=true, Rain=true, WetGrass=true
						.99,
						// Sprinkler=true, Rain=true, WetGrass=false
						.01,
						// Sprinkler=true, Rain=false, WetGrass=true
						.9,
						// Sprinkler=true, Rain=false, WetGrass=false
						.1,
						// Sprinkler=false, Rain=true, WetGrass=true
						.9,
						// Sprinkler=false, Rain=true, WetGrass=false
						.1,
						// Sprinkler=false, Rain=false, WetGrass=true
						0.0,
						// Sprinkler=false, Rain=false, WetGrass=false
						1.0 }, sprinkler, rain);

		return new mpb_Network(cloudy);
	}
}
