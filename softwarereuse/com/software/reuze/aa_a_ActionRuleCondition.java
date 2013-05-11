package com.software.reuze;

import com.software.reuze.d_a_ObjectWithDynamicAttributes;

/**
 * Base abstract class for describing conditions.
 * 
 * @author Ciaran O'Reilly
 * 
 */
public abstract class aa_a_ActionRuleCondition {
	public abstract boolean evaluate(d_a_ObjectWithDynamicAttributes p);

	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof aa_a_ActionRuleCondition)) {
			return super.equals(o);
		}
		return (toString().equals(((aa_a_ActionRuleCondition) o).toString()));
	}

	@Override
	public int hashCode() {
		return toString().hashCode();
	}
}
