package reuze.app;

import java.io.IOException;
import java.io.StringWriter;

import com.software.reuze.ff_XMLWriter;

import processing.core.PGraphics;
import reuze.app.appGUI.MinyValue;

public class PropertyList extends Property {
	/**
	 * 
	 */
	private final appGUI appGUI;
	private MinyInteger _value;
	private String[] _choices;
	private String rawchoices;

	PropertyList(appGUI appGUI, String name, MinyInteger value, String choices) {
		super(appGUI, name);
		this.appGUI = appGUI;
		_value = value;
		rawchoices = choices;
		_choices = appGUI.split(choices, ';');
	}

	PropertyList(appGUI appGUI, String name, String choices) {
		super(appGUI, name);
		this.appGUI = appGUI;
		_value = new MinyInteger(this.appGUI);
		_choices = appGUI.split(choices, ';');
	}

	Rect getBox() {
		return new Rect(this.appGUI, (int) (_x + _w * 0.4 + 3), _y + 2,
				(int) (_w * 0.6 - 8), 18);
	}

	public MinyValue get() {
		return _value;
	}

	public void onMousePressed() {
		if (this.appGUI.overRect(getBox())) {
			Rect p = _parent.getRect();
			Rect b = getBox();
			boolean below = true;
			if (b.y + b.h + (b.h - 1) * _choices.length > p.y + p.h)
				below = false;

			Rect r = new Rect(this.appGUI, b.x, b.y
					+ (below ? b.h : -(b.h - 2) * _choices.length - 2), b.w, 2
					+ (b.h - 2) * (_choices.length));
			PropertyListFrame frame = new PropertyListFrame(this.appGUI,
					_parent, _name, _value, _choices);
			frame.setRect(r);
			_parent.addFrame(frame);
			_parent.changeFocus(frame);
		}
	}

	public void display(PGraphics pg, int y) {
		super.display(pg, y);
		// pg.fill(_parent.bg);
		// pg.noStroke();
		// pg.rect(_w*0.4f + 3, y, _w *0.6f - 8, 20);
		pg.noFill();
		pg.stroke(_parent.fg);
		Rect b = getBox();
		b.x -= _x;
		b.y += y - _y;
		b.w -= 14;
		this.appGUI.rect(pg, b);
		b.grow(-1);
		pg.fill(_parent.fg);
		pg.textAlign(appGUI.LEFT, appGUI.CENTER);
		this.appGUI.text(pg, _choices[(int) _value.getValue()], b);

		b = getBox();
		b.x -= _x;
		b.y += y - _y;
		b.x += b.w - 14;
		b.w = 14;
		pg.noFill();
		this.appGUI.rect(pg, b);
		pg.line(b.x + 3, b.y + 3, b.x + b.w / 2, b.y + b.h - 4);
		pg.line(b.x + b.w / 2, b.y + b.h - 4, b.x + b.w - 3, b.y + 3);
	}

	public ff_XMLWriter writeXML(ff_XMLWriter xml, int i) {

		try {
			xml.element(String.valueOf(i)).attribute("type", "PropertyList")
					.element("name", _name).element("_value", _value._v)
					.element("rawchoices", rawchoices).pop();
		} catch (IOException e) {
			System.out.println("Error: PropertyList.writeXML()");
			e.printStackTrace();
		}

		return xml;
	}
}