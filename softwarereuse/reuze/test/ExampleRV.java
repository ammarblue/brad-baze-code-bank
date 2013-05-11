package reuze.test;
import com.software.reuze.mp_DomainFiniteBoolean;
import com.software.reuze.mp_DomainFiniteInteger;
import com.software.reuze.mp_DomainTokensArbitrary;
import com.software.reuze.mp_RandomVariable;

/*package aima.core.probability.example;

import aima.core.probability.domain.ArbitraryTokenDomain;
import aima.core.probability.domain.BooleanDomain;
import aima.core.probability.domain.FiniteIntegerDomain;
import aima.core.probability.util.RandVar;*/

/**
 * Predefined example Random Variables from AIMA3e.
 * 
 * @author Ciaran O'Reilly
 */
public class ExampleRV {
	//
	public static final mp_RandomVariable DICE_1_RV = new mp_RandomVariable("Dice1",
			new mp_DomainFiniteInteger(1, 2, 3, 4, 5, 6));
	public static final mp_RandomVariable DICE_2_RV = new mp_RandomVariable("Dice2",
			new mp_DomainFiniteInteger(1, 2, 3, 4, 5, 6));
	//
	public static final mp_RandomVariable TOOTHACHE_RV = new mp_RandomVariable("Toothache",
			new mp_DomainFiniteBoolean());
	public static final mp_RandomVariable CAVITY_RV = new mp_RandomVariable("Cavity",
			new mp_DomainFiniteBoolean());
	public static final mp_RandomVariable CATCH_RV = new mp_RandomVariable("Catch",
			new mp_DomainFiniteBoolean());
	//
	public static final mp_RandomVariable WEATHER_RV = new mp_RandomVariable("Weather",
			new mp_DomainTokensArbitrary("sunny", "rain", "cloudy", "snow"));
	//
	public static final mp_RandomVariable MENINGITIS_RV = new mp_RandomVariable("Meningitis",
			new mp_DomainFiniteBoolean());
	public static final mp_RandomVariable STIFF_NECK_RV = new mp_RandomVariable("StiffNeck",
			new mp_DomainFiniteBoolean());
	//
	public static final mp_RandomVariable BURGLARY_RV = new mp_RandomVariable("Burglary",
			new mp_DomainFiniteBoolean());
	public static final mp_RandomVariable EARTHQUAKE_RV = new mp_RandomVariable("Earthquake",
			new mp_DomainFiniteBoolean());
	public static final mp_RandomVariable ALARM_RV = new mp_RandomVariable("Alarm",
			new mp_DomainFiniteBoolean());
	public static final mp_RandomVariable JOHN_CALLS_RV = new mp_RandomVariable("JohnCalls",
			new mp_DomainFiniteBoolean());
	public static final mp_RandomVariable MARY_CALLS_RV = new mp_RandomVariable("MaryCalls",
			new mp_DomainFiniteBoolean());
	//
	public static final mp_RandomVariable CLOUDY_RV = new mp_RandomVariable("Cloudy",
			new mp_DomainFiniteBoolean());
	public static final mp_RandomVariable SPRINKLER_RV = new mp_RandomVariable("Sprinkler",
			new mp_DomainFiniteBoolean());
	public static final mp_RandomVariable RAIN_RV = new mp_RandomVariable("Rain",
			new mp_DomainFiniteBoolean());
	public static final mp_RandomVariable WET_GRASS_RV = new mp_RandomVariable("WetGrass",
			new mp_DomainFiniteBoolean());
	//
	public static final mp_RandomVariable RAIN_tm1_RV = new mp_RandomVariable("Rain_t-1",
			new mp_DomainFiniteBoolean());
	public static final mp_RandomVariable RAIN_t_RV = new mp_RandomVariable("Rain_t",
			new mp_DomainFiniteBoolean());
	public static final mp_RandomVariable UMBREALLA_t_RV = new mp_RandomVariable("Umbrella_t",
			new mp_DomainFiniteBoolean());
}
