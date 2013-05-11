package com.software.reuze;

import com.software.reuze.d_a_ObjectWithDynamicAttributes;

/**
 * Implementation of an AND condition.
 * 
 * @author Ciaran O'Reilly
 * 
 */
public class aa_ActionRuleConditionAND extends aa_a_ActionRuleCondition {
	private aa_a_ActionRuleCondition left;

	private aa_a_ActionRuleCondition right;

	public aa_ActionRuleConditionAND(aa_a_ActionRuleCondition leftCon, aa_a_ActionRuleCondition rightCon) {
		assert (null != leftCon);
		assert (null != rightCon);

		left = leftCon;
		right = rightCon;
	}

	@Override
	public boolean evaluate(d_a_ObjectWithDynamicAttributes p) {
		return (left.evaluate(p) && right.evaluate(p));
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		return sb.append("[").append(left).append(" && ").append(right)
				.append("]").toString();
	}
}
