package com.software.reuze;

import com.software.reuze.aa_i_Percept;

/**
 * This interface is to define how to Map a Percept to a State representation
 * for a problem solver within a specific environment. This arises in the
 * description of the Online Search algorithms from Chapter 4.
 * 
 * @author Ciaran O'Reilly
 * 
 */
public interface aa_i_PerceptToStateFunction {

	/**
	 * Get the problem state associated with a Percept.
	 * 
	 * @param p
	 *            the percept to be transformed to a problem state.
	 * @return a problem state derived from the Percept p.
	 */
	Object getState(aa_i_Percept p);
}
