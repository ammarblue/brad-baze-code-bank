package com.software.reuze;

import com.software.reuze.d_a_ObjectWithDynamicAttributes;

/**
 * Implementation of an EQUALity condition.
 * 
 * @author Ciaran O'Reilly
 * 
 */
public class aa_ActionRuleConditionEqual extends aa_a_ActionRuleCondition {
	private Object key;

	private Object value;

	public aa_ActionRuleConditionEqual(Object key, Object value) {
		assert (null != key);
		assert (null != value);

		this.key = key;
		this.value = value;
	}

	@Override
	public boolean evaluate(d_a_ObjectWithDynamicAttributes p) {
		return value.equals(p.getAttribute(key));
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		return sb.append(key).append("==").append(value).toString();
	}
}
