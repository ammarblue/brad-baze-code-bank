package com.software.reuze;

import com.software.reuze.aa_i_Action;
import com.software.reuze.d_a_ObjectWithDynamicAttributes;

/**
 * A simple implementation of a "condition-action rule".
 * 
 * @author Ciaran O'Reilly
 * @author Mike Stampone
 */
public class aa_ActionRule {
	private aa_a_ActionRuleCondition con;

	private aa_i_Action action;

	/**
	 * Constructs a condition-action rule.
	 * 
	 * @param con
	 *            a condition
	 * @param action
	 *            an action
	 */
	public aa_ActionRule(aa_a_ActionRuleCondition con, aa_i_Action action) {
		assert (null != con);
		assert (null != action);

		this.con = con;
		this.action = action;
	}

	public boolean evaluate(d_a_ObjectWithDynamicAttributes p) {
		return (con.evaluate(p));
	}

	/**
	 * Returns the action of this condition-action rule.
	 * 
	 * @return the action of this condition-action rule.
	 */
	public aa_i_Action getAction() {
		return action;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof aa_ActionRule)) {
			return super.equals(o);
		}
		return (toString().equals(((aa_ActionRule) o).toString()));
	}

	@Override
	public int hashCode() {
		return toString().hashCode();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		return sb.append("if ").append(con).append(" then ").append(action)
				.append(".").toString();
	}
}
