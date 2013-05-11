package reuze.app;

import reuze.app.appGUI.MinyValue;

public class PropertyEditInteger extends PropertyEdit {
	/**
	 * 
	 */
	private final appGUI appGUI;
	private MinyInteger _value;

	PropertyEditInteger(appGUI appGUI, String name, MinyInteger value) {
		super(appGUI, name);
		this.appGUI = appGUI;
		_value = value;
	}

	PropertyEditInteger(appGUI appGUI, String name) {
		super(appGUI, name);
		this.appGUI = appGUI;
		_value = new MinyInteger(this.appGUI);
	}

	public MinyValue get() {
		return _value;
	}

	boolean validate(String test) {
		if (test.length() == 0)
			return true;
		if (test.equals("-"))
			return true;
		try {
			Integer.parseInt(test);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	void saveValue(String val) {
		if (val.length() == 0)
			_value.setValue(0);
		else
			try {
				_value.setValue(Integer.parseInt(val));
			} catch (NumberFormatException e) {
			}
	}

	String getValue() {
		return _value.getValue().toString();
	}
}