package reuze.test;
//package aima.test.core.unit.agent.impl.aprog.simplerule;

import org.junit.Assert;
import org.junit.Test;

import com.software.reuze.aa_ActionDynamic;
import com.software.reuze.aa_ActionRule;
import com.software.reuze.aa_ActionRuleConditionAND;
import com.software.reuze.aa_ActionRuleConditionEqual;
import com.software.reuze.aa_ActionRuleConditionNOT;
import com.software.reuze.aa_ActionRuleConditionOR;
import com.software.reuze.aa_PerceptWithDynamicAttributes;
import com.software.reuze.aa_i_Action;


/**
 * @author Ciaran O'Reilly
 * 
 */
public class RuleTest {

	private static final aa_i_Action ACTION_INITIATE_BRAKING = new aa_ActionDynamic(
			"initiate-braking");
	private static final aa_i_Action ACTION_EMERGENCY_BRAKING = new aa_ActionDynamic(
			"emergency-braking");
	//
	private static final String ATTRIBUTE_CAR_IN_FRONT_IS_BRAKING = "car-in-front-is-braking";
	private static final String ATTRIBUTE_CAR_IN_FRONT_IS_INDICATING = "car-in-front-is-indicating";
	private static final String ATTRIBUTE_CAR_IN_FRONT_TIRES_SMOKING = "car-in-front-tires-smoking";

	@Test
	public void testEQUALRule() {
		aa_ActionRule r = new aa_ActionRule(new aa_ActionRuleConditionEqual(ATTRIBUTE_CAR_IN_FRONT_IS_BRAKING,
				true), ACTION_INITIATE_BRAKING);
System.out.println(ACTION_INITIATE_BRAKING);
		Assert.assertEquals(ACTION_INITIATE_BRAKING, r.getAction());

		Assert.assertEquals(
				"if car-in-front-is-braking==true then aa_i_Action[name==initiate-braking].",
				r.toString());

		Assert.assertEquals(true, r.evaluate(new aa_PerceptWithDynamicAttributes(
				ATTRIBUTE_CAR_IN_FRONT_IS_BRAKING, true)));

		Assert.assertEquals(false, r.evaluate(new aa_PerceptWithDynamicAttributes(
				ATTRIBUTE_CAR_IN_FRONT_IS_BRAKING, false)));

		Assert.assertEquals(false, r.evaluate(new aa_PerceptWithDynamicAttributes(
				ATTRIBUTE_CAR_IN_FRONT_IS_INDICATING, true)));
	}

	@Test
	public void testNOTRule() {
		aa_ActionRule r = new aa_ActionRule(new aa_ActionRuleConditionNOT(new aa_ActionRuleConditionEqual(
				ATTRIBUTE_CAR_IN_FRONT_IS_BRAKING, true)),
				ACTION_INITIATE_BRAKING);

		Assert.assertEquals(ACTION_INITIATE_BRAKING, r.getAction());

		Assert.assertEquals(
				"if ![car-in-front-is-braking==true] then aa_i_Action[name==initiate-braking].",
				r.toString());

		Assert.assertEquals(false, r.evaluate(new aa_PerceptWithDynamicAttributes(
				ATTRIBUTE_CAR_IN_FRONT_IS_BRAKING, true)));

		Assert.assertEquals(true, r.evaluate(new aa_PerceptWithDynamicAttributes(
				ATTRIBUTE_CAR_IN_FRONT_IS_BRAKING, false)));

		Assert.assertEquals(true, r.evaluate(new aa_PerceptWithDynamicAttributes(
				ATTRIBUTE_CAR_IN_FRONT_IS_INDICATING, true)));
	}

	@Test
	public void testANDRule() {
		aa_ActionRule r = new aa_ActionRule(new aa_ActionRuleConditionAND(new aa_ActionRuleConditionEqual(
				ATTRIBUTE_CAR_IN_FRONT_IS_BRAKING, true), new aa_ActionRuleConditionEqual(
				ATTRIBUTE_CAR_IN_FRONT_TIRES_SMOKING, true)),
				ACTION_EMERGENCY_BRAKING);

		Assert.assertEquals(ACTION_EMERGENCY_BRAKING, r.getAction());

		Assert.assertEquals(
				"if [car-in-front-is-braking==true && car-in-front-tires-smoking==true] then aa_i_Action[name==emergency-braking].",
				r.toString());

		Assert.assertEquals(false, r.evaluate(new aa_PerceptWithDynamicAttributes(
				ATTRIBUTE_CAR_IN_FRONT_IS_BRAKING, true)));

		Assert.assertEquals(false, r.evaluate(new aa_PerceptWithDynamicAttributes(
				ATTRIBUTE_CAR_IN_FRONT_TIRES_SMOKING, true)));

		Assert.assertEquals(true, r.evaluate(new aa_PerceptWithDynamicAttributes(
				ATTRIBUTE_CAR_IN_FRONT_IS_BRAKING, true,
				ATTRIBUTE_CAR_IN_FRONT_TIRES_SMOKING, true)));

		Assert.assertEquals(false, r.evaluate(new aa_PerceptWithDynamicAttributes(
				ATTRIBUTE_CAR_IN_FRONT_IS_BRAKING, false,
				ATTRIBUTE_CAR_IN_FRONT_TIRES_SMOKING, true)));

		Assert.assertEquals(false, r.evaluate(new aa_PerceptWithDynamicAttributes(
				ATTRIBUTE_CAR_IN_FRONT_IS_BRAKING, true,
				ATTRIBUTE_CAR_IN_FRONT_TIRES_SMOKING, false)));
	}

	@Test
	public void testORRule() {
		aa_ActionRule r = new aa_ActionRule(new aa_ActionRuleConditionOR(new aa_ActionRuleConditionEqual(
				ATTRIBUTE_CAR_IN_FRONT_IS_BRAKING, true), new aa_ActionRuleConditionEqual(
				ATTRIBUTE_CAR_IN_FRONT_TIRES_SMOKING, true)),
				ACTION_EMERGENCY_BRAKING);

		Assert.assertEquals(ACTION_EMERGENCY_BRAKING, r.getAction());

		Assert.assertEquals( //TODO, do not use class name, change to Action
				"if [car-in-front-is-braking==true || car-in-front-tires-smoking==true] then aa_i_Action[name==emergency-braking].",
				r.toString());

		Assert.assertEquals(true, r.evaluate(new aa_PerceptWithDynamicAttributes(
				ATTRIBUTE_CAR_IN_FRONT_IS_BRAKING, true)));

		Assert.assertEquals(true, r.evaluate(new aa_PerceptWithDynamicAttributes(
				ATTRIBUTE_CAR_IN_FRONT_TIRES_SMOKING, true)));

		Assert.assertEquals(true, r.evaluate(new aa_PerceptWithDynamicAttributes(
				ATTRIBUTE_CAR_IN_FRONT_IS_BRAKING, true,
				ATTRIBUTE_CAR_IN_FRONT_TIRES_SMOKING, true)));

		Assert.assertEquals(true, r.evaluate(new aa_PerceptWithDynamicAttributes(
				ATTRIBUTE_CAR_IN_FRONT_IS_BRAKING, false,
				ATTRIBUTE_CAR_IN_FRONT_TIRES_SMOKING, true)));

		Assert.assertEquals(true, r.evaluate(new aa_PerceptWithDynamicAttributes(
				ATTRIBUTE_CAR_IN_FRONT_IS_BRAKING, true,
				ATTRIBUTE_CAR_IN_FRONT_TIRES_SMOKING, false)));

		Assert.assertEquals(false, r.evaluate(new aa_PerceptWithDynamicAttributes(
				ATTRIBUTE_CAR_IN_FRONT_IS_BRAKING, false,
				ATTRIBUTE_CAR_IN_FRONT_TIRES_SMOKING, false)));
	}
}