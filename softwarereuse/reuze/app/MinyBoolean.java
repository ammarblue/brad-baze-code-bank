package reuze.app;

import reuze.app.appGUI.MinyValue;

public class MinyBoolean implements appGUI.MinyValue {
	/**
	 * 
	 */
	private final appGUI appGUI;
	public Boolean _v;

	MinyBoolean(appGUI appGUI) {
		this.appGUI = appGUI;
		_v = new Boolean(false);
	}

	MinyBoolean(appGUI appGUI, Boolean v) {
		this.appGUI = appGUI;
		_v = v;
	}

	Boolean getValue() {
		return _v;
	}

	void setValue(Boolean v) {
		_v = v;
	}

	public String getString() {
		return _v.toString();
	}
}