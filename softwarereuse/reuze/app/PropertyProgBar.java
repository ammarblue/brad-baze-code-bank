package reuze.app;

import reuze.app.appGUI.MinyValue;

class PropertyProgBar extends PropertyPBar {
	/**
	 * 
	 */
	private final appGUI appGUI;
	private int _mini = 0;
	private int _maxi;

	PropertyProgBar(appGUI appGUI, String name, MinyInteger value, int maxi,
			int steps) {
		super(appGUI, name, maxi, steps);
		this.appGUI = appGUI;
		super._value = value;
		_maxi = maxi;
	}

	public MinyValue get() {
		return super._value;
	}

	float getPos() {
		return (super._value.getValue() - _mini) / (float) (_maxi - _mini);
	}

	void setPos(float v) {
	}

	public void update() {
		super.update();
	}
}