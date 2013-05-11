package com.software.reuze;
import com.software.reuze.aa_TreeSearchNode;
/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 92.<br>
 * <br>
 * The evaluation function is construed as a cost estimate, so the node with the
 * lowest evaluation is expanded first.
 * 
 * @author Ciaran O'Reilly
 * 
 */
public interface das_i_TreeSearchEvaluationFunction {
	double f(aa_TreeSearchNode n);
}
