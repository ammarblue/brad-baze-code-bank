package reuze.test;
//package aima.test.core.unit.probability.bayes.model;

import org.junit.Test;

import com.software.reuze.mpb_InferenceEliminationAsk;
import com.software.reuze.mpb_InferenceEnumeration;
import com.software.reuze.mpb_ModelFinite;
import com.software.reuze.mpb_i_Inference;


/*import aima.core.probability.bayes.BayesInference;
import aima.core.probability.bayes.exact.EliminationAsk;
import aima.core.probability.bayes.exact.EnumerationAsk;
import aima.core.probability.bayes.model.FiniteBayesModel;
import aima.core.probability.example.BayesNetExampleFactory;
import aima.test.core.unit.probability.CommonFiniteProbabilityModelTests;*/

public class FiniteBayesModelTest extends CommonFiniteProbabilityModelTests {

	//
	// ProbabilityModel Tests
	@Test
	public void test_RollingPairFairDiceModel() {
		for (mpb_i_Inference bi : getBayesInferenceImplementations()) {
			test_RollingPairFairDiceModel(new mpb_ModelFinite(
					BayesNetExampleFactory.construct2FairDiceNetwor(), bi));
		}
	}

	@Test
	public void test_ToothacheCavityCatchModel() {
		for (mpb_i_Inference bi : getBayesInferenceImplementations()) {
			test_ToothacheCavityCatchModel(new mpb_ModelFinite(
					BayesNetExampleFactory
							.constructToothacheCavityCatchNetwork(),
					bi));
		}
	}

	@Test
	public void test_ToothacheCavityCatchWeatherModel() {
		for (mpb_i_Inference bi : getBayesInferenceImplementations()) {
			test_ToothacheCavityCatchWeatherModel(new mpb_ModelFinite(
					BayesNetExampleFactory
							.constructToothacheCavityCatchWeatherNetwork(),
					bi));
		}
	}

	@Test
	public void test_MeningitisStiffNeckModel() {
		for (mpb_i_Inference bi : getBayesInferenceImplementations()) {
			test_MeningitisStiffNeckModel(new mpb_ModelFinite(
					BayesNetExampleFactory
							.constructMeningitisStiffNeckNetwork(),
					bi));
		}
	}

	@Test
	public void test_BurglaryAlarmModel() {
		for (mpb_i_Inference bi : getBayesInferenceImplementations()) {
			test_BurglaryAlarmModel(new mpb_ModelFinite(
					BayesNetExampleFactory.constructBurglaryAlarmNetwork(), bi));
		}
	}

	//
	// FiniteProbabilityModel Tests
	@Test
	public void test_RollingPairFairDiceModel_Distributions() {
		for (mpb_i_Inference bi : getBayesInferenceImplementations()) {
			test_RollingPairFairDiceModel_Distributions(new mpb_ModelFinite(
					BayesNetExampleFactory.construct2FairDiceNetwor(), bi));
		}
	}

	@Test
	public void test_ToothacheCavityCatchModel_Distributions() {
		for (mpb_i_Inference bi : getBayesInferenceImplementations()) {
			test_ToothacheCavityCatchModel_Distributions(new mpb_ModelFinite(
					BayesNetExampleFactory
							.constructToothacheCavityCatchNetwork(),
					bi));
		}
	}

	@Test
	public void test_ToothacheCavityCatchWeatherModel_Distributions() {
		for (mpb_i_Inference bi : getBayesInferenceImplementations()) {
			test_ToothacheCavityCatchWeatherModel_Distributions(new mpb_ModelFinite(
					BayesNetExampleFactory
							.constructToothacheCavityCatchWeatherNetwork(),
					bi));
		}
	}

	@Test
	public void test_MeningitisStiffNeckModel_Distributions() {
		for (mpb_i_Inference bi : getBayesInferenceImplementations()) {
			test_MeningitisStiffNeckModel_Distributions(new mpb_ModelFinite(
					BayesNetExampleFactory
							.constructMeningitisStiffNeckNetwork(),
					bi));
		}
	}

	@Test
	public void test_BurglaryAlarmModel_Distributions() {
		for (mpb_i_Inference bi : getBayesInferenceImplementations()) {
			test_BurglaryAlarmModel_Distributions(new mpb_ModelFinite(
					BayesNetExampleFactory.constructBurglaryAlarmNetwork(), bi));
		}
	}

	//
	// PRIVATE METHODS
	//
	private mpb_i_Inference[] getBayesInferenceImplementations() {
		return new mpb_i_Inference[] { new mpb_InferenceEnumeration(),
				new mpb_InferenceEliminationAsk() };
	}
}
