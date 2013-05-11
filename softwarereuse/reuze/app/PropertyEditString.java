package reuze.app;

import reuze.app.appGUI.MinyValue;

public class PropertyEditString extends PropertyEdit {
	/**
	 * 
	 */
	private final appGUI appGUI;
	private MinyString _value;

	PropertyEditString(appGUI appGUI, String name, MinyString value) {
		super(appGUI, name);
		this.appGUI = appGUI;
		_value = value;
	}

	PropertyEditString(appGUI appGUI, String name) {
		super(appGUI, name);
		this.appGUI = appGUI;
		_value = new MinyString(this.appGUI);
	}

	public MinyValue get() {
		return _value;
	}

	boolean validate(String test) {
		return this.appGUI.textWidth(test) < getBox().w - 2;
	}

	void saveValue(String val) {
		_value.setValue(val);
	}

	String getValue() {
		return _value.getValue();
	}
}