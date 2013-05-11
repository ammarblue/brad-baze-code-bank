package reuze.app;

import java.io.IOException;
import java.io.StringWriter;

import com.software.reuze.ff_XMLWriter;

import reuze.app.appGUI.MinyValue;

public class PropertyEditFloat extends PropertyEdit {
	/**
	 * 
	 */
	private final appGUI appGUI;
	private MinyFloat _value;

	PropertyEditFloat(appGUI appGUI, String name, MinyFloat value) {
		super(appGUI, name);
		this.appGUI = appGUI;
		_value = value;
	}

	PropertyEditFloat(appGUI appGUI, String name) {
		super(appGUI, name);
		this.appGUI = appGUI;
		_value = new MinyFloat(this.appGUI);
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
			if (test.substring(test.length() - 1).compareToIgnoreCase("e") == 0) {
				Float.parseFloat(test.substring(0, test.length() - 1));
				return true;
			}

			Float.parseFloat(test);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	void saveValue(String val) {
		if (val.length() == 0)
			_value.setValue(0.0f);
		else
			try {
				_value.setValue(Float.parseFloat(val));
			} catch (NumberFormatException e) {
			}
	}

	String getValue() {
		return _value.getValue().toString();
	}

	public ff_XMLWriter writeXML(ff_XMLWriter xml, int i) {

		try {
			xml.element(String.valueOf(i))
					.attribute("type", "PropertyEditFloat")
					.element("name", _name).element("_value", _value._v).pop();
		} catch (IOException e) {
			System.out.println("Error: PropertyEditFloat.writeXML()");
			e.printStackTrace();
		}

		return xml;
	}
}