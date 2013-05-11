package reuze.app;

import java.io.IOException;
import java.io.StringWriter;

import com.software.reuze.ff_XMLWriter;

import processing.core.PGraphics;
import reuze.app.appGUI.MinyValue;
import reuze.app.appGUI.MinyWidget;

public class Property implements appGUI.MinyWidget {
	/**
	 * 
	 */
	private final appGUI appGUI;
	protected MinyGUI _parent;
	public String _name;
	protected int _x, _y, _w;
	protected boolean _hasFocus;

	Property(appGUI appGUI, String name) {
		this.appGUI = appGUI;
		_name = name;
		_hasFocus = false;
	}

	void add(MinyGUI parent) {
		_parent = parent;
		_parent.addProperty(this);
	}

	void setPosition(int x, int y, int w) {
		_x = x;
		_y = y;
		_w = w;
	}

	public int getHeight() {
		return 20;
	}

	public Rect getRect() {
		return new Rect(this.appGUI, _x, _y, _w, getHeight());
	}

	public MinyValue get() {
		return new MinyString(this.appGUI, "");
	}

	public void update() {
	}

	public void display(PGraphics pg, int y) {
		pg.textAlign(appGUI.LEFT, appGUI.CENTER);
		pg.fill(_parent.fg);
		pg.text(_name, 5, y, _w * 0.4f - 7, getHeight());
	}

	public void getFocus() {
		_hasFocus = true;
	}

	public void lostFocus() {
		_hasFocus = false;
	}

	public void onMousePressed() {
	}

	public void onKeyPressed() {
	}

	public ff_XMLWriter writeXML(ff_XMLWriter xml, int i) {

		try {
			xml.element(String.valueOf(i)).attribute("type", "Property")
					.element("name", _name).pop();
		} catch (IOException e) {
			System.out.println("Error: Property.writeXML()");
			e.printStackTrace();
		}

		return xml;
	}
}