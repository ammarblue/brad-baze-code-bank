package reuze.app;

import reuze.app.appGUI.MinyValue;

public class PropertySliderInteger extends PropertySlider {
	/**
	 * 
	 */
	private final appGUI appGUI;
	private MinyInteger _value;
	private int _mini, _maxi;

	PropertySliderInteger(appGUI appGUI, String name, MinyInteger value,
			int mini, int maxi) {
		super(appGUI, name);
		this.appGUI = appGUI;
		_value = value;
		_mini = mini;
		_maxi = maxi;
	}

	PropertySliderInteger(appGUI appGUI, String name, int mini, int maxi) {
		super(appGUI, name);
		this.appGUI = appGUI;
		_value = new MinyInteger(this.appGUI, mini);
		_mini = mini;
		_maxi = maxi;
	}

	public MinyValue get() {
		return _value;
	}

	float getPos() {
		return (_value.getValue() - _mini) / (float) (_maxi - _mini);
	}

	void setPos(float v) {
		_value.setValue(appGUI.round(appGUI.constrain(_mini + v
				* (_maxi - _mini), _mini, _maxi)));
	}

	public void update() {
		super.update();
		_value.setValue(appGUI.constrain(_value.getValue(), _mini, _maxi));
	}
}