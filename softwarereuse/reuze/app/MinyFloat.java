package reuze.app;

import reuze.app.appGUI.MinyValue;

public class MinyFloat implements appGUI.MinyValue {
	/**
	 * 
	 */
	private final appGUI appGUI;
	public Float _v;

	MinyFloat(appGUI appGUI) {
		this.appGUI = appGUI;
		_v = new Float(0);
	}

	MinyFloat(appGUI appGUI, Float v) {
		this.appGUI = appGUI;
		_v = v;
	}

	Float getValue() {
		return _v;
	}

	void setValue(Float v) {
		_v = v;
	}

	public String getString() {
		return _v.toString();
	}
}