package com.software.reuze;

public interface da_i_Validate {
	/* can be called multiple times
	 * if validated already, extra calls are ignored
	 * returns true if the data structure values are valid
	 * an implementation may choose to validate only on this call
	 * "extra" cab be used as a hint, for example, a class might correct invalid values
	 */
	boolean isValid(int extra);
}
