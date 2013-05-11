package com.software.reuze;

import com.software.reuze.d_a_ObjectWithDynamicAttributes;

/**
 * Implementation of a NOT condition.
 * 
 * @author Ciaran O'Reilly
 * 
 */
public class aa_ActionRuleConditionNOT extends aa_a_ActionRuleCondition {
	private aa_a_ActionRuleCondition con;

	public aa_ActionRuleConditionNOT(aa_a_ActionRuleCondition con) {
		assert (null != con);

		this.con = con;
	}

	@Override
	public boolean evaluate(d_a_ObjectWithDynamicAttributes p) {
		return (!con.evaluate(p));
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		return sb.append("![").append(con).append("]").toString();
	}
}