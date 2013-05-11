package reuze.app;

import java.io.IOException;
import java.io.StringWriter;

import com.software.reuze.ff_XMLWriter;

import processing.core.PGraphics;
import reuze.app.appGUI.ButtonCallback;
import reuze.app.appGUI.MinyValue;

public class PropertyButton extends Property {
	/**
	 * 
	 */
	private final appGUI appGUI;
	public ButtonCallback _callback;
	public boolean _pressed;

	PropertyButton(appGUI appGUI, String name, ButtonCallback callback) {
		super(appGUI, name);
		this.appGUI = appGUI;
		_callback = callback;
		_pressed = false;
	}

	public MinyValue get() {
		return new MinyBoolean(this.appGUI, _pressed);
	}

	Rect getBox() {
		return new Rect(this.appGUI, (int) (_x + _w * 0.2), _y + 1,
				(int) (_w * 0.6), 18);
	}

	public void onMousePressed() {
		if (this.appGUI.overRect(getBox()))
			_pressed = true;
	}

	public void update() {
		if (_pressed && !this.appGUI.mousePressed) {
			_pressed = false;
			if (this.appGUI.overRect(getBox()))
				_callback.onButtonPressed();
		}
	}

	public void display(PGraphics pg, int y) {
		pg.stroke(_parent.fg);
		Rect b = getBox();
		b.x -= _x;
		b.y += y - _y;
		if (_pressed && this.appGUI.overRect(getBox()))
			pg.strokeWeight(4);
		pg.noFill();
		this.appGUI.rect(pg, b);
		pg.strokeWeight(1);
		b.grow(-1);
		pg.textAlign(appGUI.CENTER, appGUI.TOP);
		// pg.fill(_parent.fg);
		this.appGUI.text(pg, _name, b);
	}

	public ff_XMLWriter writeXML(ff_XMLWriter xml, int i) {

		try {
			xml.element(String.valueOf(i)).attribute("type", "PropertyButton")
					.element("name", _name).pop();
		} catch (IOException e) {
			System.out.println("Error: PropertyButton.writeXML()");
			e.printStackTrace();
		}

		return xml;
	}
}