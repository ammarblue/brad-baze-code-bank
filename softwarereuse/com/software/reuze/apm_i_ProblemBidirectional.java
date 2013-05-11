package com.software.reuze;
import com.software.reuze.a_Problem;
/**
 * An interface describing a problem that can be tackled from both directions at
 * once (i.e InitialState<->Goal).
 * 
 * @author Ciaran O'Reilly
 * 
 */
public interface apm_i_ProblemBidirectional {
	a_Problem getOriginalProblem();

	a_Problem getReverseProblem();
}
