package reuze.app;

import reuze.app.appGUI.ButtonCallback;

public class TestButton implements appGUI.ButtonCallback {
	/**
	 * 
	 */
	private final appGUI appGUI;

	/**
	 * @param appGUI
	 */
	TestButton(appGUI appGUI) {
		this.appGUI = appGUI;
	}

	public void onButtonPressed() {
		this.appGUI.time = 0;
		this.appGUI.running.setValue(true);
	}
}