package reuze.app;

import reuze.app.appGUI.MinyValue;

public class MinyColor implements appGUI.MinyValue {
	/**
	 * 
	 */
	private final appGUI appGUI;
	public int _v;

	MinyColor(appGUI appGUI) {
		this.appGUI = appGUI;
		_v = -1;
	}

	MinyColor(appGUI appGUI, int v) {
		this.appGUI = appGUI;
		_v = v;
	}

	int getValue() {
		return _v;
	}

	void setValue(int v) {
		_v = v;
	}

	public String getString() {
		return "0x" + appGUI.hex(_v);
	}

	int getAlpha() {
		return (_v >> 24) & 0xFF;
	}

	int getRed() {
		return (_v >> 16) & 0xFF;
	}

	int getGreen() {
		return (_v >> 8) & 0xFF;
	}

	int getBlue() {
		return _v & 0xFF;
	}

	void setAlpha(int a) {
		_v = (_v & 0x00FFFFFF) + ((a & 0xFF) << 24);
	}

	void setRed(int r) {
		_v = (_v & 0xFF00FFFF) + ((r & 0xFF) << 16);
	}

	void setGreen(int g) {
		_v = (_v & 0xFFFF00FF) + ((g & 0xFF) << 8);
	}

	void setBlue(int b) {
		_v = (_v & 0xFFFFFF00) + (b & 0xFF);
	}
}