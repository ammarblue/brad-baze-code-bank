package reuze.app;

import reuze.app.appGUI.MinyValue;

public class MinyInteger implements appGUI.MinyValue {
	/**
	 * 
	 */
	private final appGUI appGUI;
	public Integer _v;

	MinyInteger(appGUI appGUI) {
		this.appGUI = appGUI;
		_v = new Integer(0);
	}

	MinyInteger(appGUI appGUI, Integer v) {
		this.appGUI = appGUI;
		_v = v;
	}

	Integer getValue() {
		return _v;
	}

	void setValue(Integer v) {
		_v = v;
	}

	public String getString() {
		return _v.toString();
	}
}