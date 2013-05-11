package com.software.reuze;
import com.software.reuze.aa_i_Percept;

/**
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 * @author Mike Stampone
 */
public class aa_PerceptWithDynamicAttributes extends d_a_ObjectWithDynamicAttributes implements
		aa_i_Percept {
	public aa_PerceptWithDynamicAttributes() {

	}

	@Override
	public String describeType() {
		return aa_i_Percept.class.getSimpleName();
	}

	/**
	 * Constructs a DynamicPercept with one attribute
	 * 
	 * @param key1
	 *            the attribute key
	 * @param value1
	 *            the attribute value
	 */
	public aa_PerceptWithDynamicAttributes(Object key1, Object value1) {
		setAttribute(key1, value1);
	}

	/**
	 * Constructs a DynamicPercept with two attributes
	 * 
	 * @param key1
	 *            the first attribute key
	 * @param value1
	 *            the first attribute value
	 * @param key2
	 *            the second attribute key
	 * @param value2
	 *            the second attribute value
	 */
	public aa_PerceptWithDynamicAttributes(Object key1, Object value1, Object key2, Object value2) {
		setAttribute(key1, value1);
		setAttribute(key2, value2);
	}

	/**
	 * Constructs a DynamicPercept with an array of attributes
	 * 
	 * @param keys
	 *            the array of attribute keys
	 * @param values
	 *            the array of attribute values
	 */
	public aa_PerceptWithDynamicAttributes(Object[] keys, Object[] values) {
		assert (keys.length == values.length);

		for (int i = 0; i < keys.length; i++) {
			setAttribute(keys[i], values[i]);
		}
	}
}