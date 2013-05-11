package com.software.reuze;

import com.software.reuze.aa_ActionDynamic;

/**
 * A NoOp action that indicates a CutOff has occurred in a search. Used
 * primarily by DepthLimited and IterativeDeepening search routines.
 * 
 * @author Ciaran O'Reilly
 */
public class aa_ActionDynamicCutOffIndicator extends aa_ActionDynamic {
	public static final aa_ActionDynamicCutOffIndicator CUT_OFF = new aa_ActionDynamicCutOffIndicator();

	//
	// START-Action
	public boolean isNoOp() {
		return true;
	}

	// END-Action
	//

	private aa_ActionDynamicCutOffIndicator() {
		super("CutOff");
	}
}