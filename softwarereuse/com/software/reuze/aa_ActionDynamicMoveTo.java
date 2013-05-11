package com.software.reuze;

import com.software.reuze.aa_ActionDynamic;

public class aa_ActionDynamicMoveTo extends aa_ActionDynamic {
	public static final String ATTRIBUTE_MOVE_TO_LOCATION = "location";

	public aa_ActionDynamicMoveTo(String location) {
		super("moveTo");
		setAttribute(ATTRIBUTE_MOVE_TO_LOCATION, location);
	}

	public String getToLocation() {
		return (String) getAttribute(ATTRIBUTE_MOVE_TO_LOCATION);
	}
}
