package reuze.app;

import reuze.app.appGUI.MinyValue;

public class MinyString implements appGUI.MinyValue {
	/**
	 * 
	 */
	private final appGUI appGUI;
	private String _v;

	MinyString(appGUI appGUI) {
		this.appGUI = appGUI;
		_v = new String("");
	}

	MinyString(appGUI appGUI, String v) {
		this.appGUI = appGUI;
		_v = v;
	}

	String getValue() {
		return _v;
	}

	void setValue(String v) {
		_v = v;
	}

	public String getString() {
		return _v;
	}
}