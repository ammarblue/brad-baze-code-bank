package reuze.app;

import java.io.IOException;
import java.io.StringWriter;

import com.software.reuze.ff_XMLWriter;

import reuze.app.appGUI.MinyValue;

public class PropertySliderFloat extends PropertySlider {
	/**
	 * 
	 */
	private final appGUI appGUI;
	private MinyFloat _value;
	private float _mini, _maxi;

	PropertySliderFloat(appGUI appGUI, String name, MinyFloat value,
			float mini, float maxi) {
		super(appGUI, name);
		this.appGUI = appGUI;
		_value = value;
		_mini = mini;
		_maxi = maxi;
	}

	PropertySliderFloat(appGUI appGUI, String name, float mini, float maxi) {
		super(appGUI, name);
		this.appGUI = appGUI;
		_value = new MinyFloat(this.appGUI, mini);
		_mini = mini;
		_maxi = maxi;
	}

	public MinyValue get() {
		return _value;
	}

	float getPos() {
		return (_value.getValue() - _mini) / (_maxi - _mini);
	}

	void setPos(float v) {
		_value.setValue(appGUI.constrain(_mini + v * (_maxi - _mini), _mini,
				_maxi));
	}

	public void update() {
		super.update();
		_value.setValue(appGUI.constrain(_value.getValue(), _mini, _maxi));
	}

	public ff_XMLWriter writeXML(ff_XMLWriter xml, int i) {

		try {
			xml.element(String.valueOf(i))
					.attribute("type", "PropertySliderFloat")
					.element("name", _name).element("_value", _value._v)
					.element("_mini", _mini).element("_maxi", _maxi).pop();
		} catch (IOException e) {
			System.out.println("Error: PropertySliderFloat.writeXML()");
			e.printStackTrace();
		}

		return xml;
	}
}