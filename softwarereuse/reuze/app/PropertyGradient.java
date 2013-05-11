package reuze.app;

import java.io.IOException;
import java.io.StringWriter;

import com.software.reuze.ff_XMLWriter;

import processing.core.PGraphics;
import processing.core.PImage;

public class PropertyGradient extends Property {
	/**
	 * 
	 */
	private final appGUI appGUI;
	private ColorGradient _value;
	private PImage _imgChecker;
	private PGraphics _gradient;
	private GradientFrame _frame;

	PropertyGradient(appGUI appGUI, String name, ColorGradient value) {
		super(appGUI, name);
		this.appGUI = appGUI;
		_value = value;
		int h = getHeight() - 3;
		_imgChecker = this.appGUI.createImage(h, h, appGUI.RGB);
		_imgChecker.loadPixels();
		for (int x = 0; x < h; x++) {
			for (int y = 0; y < h; y++) {
				boolean b = (x % h < h / 2);
				if (y % h < h / 2)
					b = !b;
				_imgChecker.pixels[y * h + x] = b ? this.appGUI.color(160)
						: this.appGUI.color(95);
			}
		}
		_imgChecker.updatePixels();
	}

	Rect getBox() {
		return new Rect(this.appGUI, (int) (_x + _w * 0.4 + 3), _y + 1,
				(int) (_w * 0.6 - 8), 18);
	}

	public void onMousePressed() {
		if (this.appGUI.overRect(getBox())) {
			if (_frame == null) {
				_frame = new GradientFrame(this.appGUI, _parent, _name, _value,
						this);
				_frame.placeAt(this.appGUI.mouseX, this.appGUI.mouseY);
				_parent.addFrame(_frame);
			}
			_parent.changeFocus(_frame);
			_parent.putFrameOnTop(_frame);
		}
	}

	void updateGradient() {
		Rect r = getBox();
		r.x++;
		r.w--;
		r.y++;
		r.h--;
		if (_gradient == null || _gradient.width != r.w
				|| _gradient.height != r.h)
			_gradient = this.appGUI.createGraphics(r.w, r.h, appGUI.JAVA2D);
		_gradient.beginDraw();
		_gradient.background(this.appGUI.color(255, 255, 255, 0));
		int h = getHeight() - 3;
		int x;
		for (x = 0; x + h < r.w; x += h)
			_gradient.image(_imgChecker, x, 0);
		int w = r.w - x;
		_gradient.copy(_imgChecker, 0, 0, w, h, x, 0, w, h);

		int[] c = _value.getTable(r.w);
		for (x = 0; x < r.w; x++) {
			_gradient.stroke(c[x]);
			_gradient.line(x, 0, x, r.h);
		}
		_gradient.endDraw();
	}

	public void display(PGraphics pg, int y) {
		super.display(pg, y);

		Rect r = getBox();
		r.x -= _x;
		r.y += y - _y;
		pg.noFill();
		pg.stroke(_parent.fg);
		this.appGUI.rect(pg, r);

		if (_gradient == null || r.w - 1 != _gradient.width)
			updateGradient();

		pg.noFill();
		pg.stroke(_parent.fg);
		this.appGUI.rect(pg, r);
		pg.image(_gradient, r.x + 1, r.y + 1);
	}

	void onCloseFrame(MinyFrame frame) {
		_frame = null;
		updateGradient();
	}

	public ff_XMLWriter writeXML(ff_XMLWriter xml, int i) {

		try {
			xml.element(String.valueOf(i))
					.attribute("type", "PropertyGradient")
					.element("name", _name).pop();
		} catch (IOException e) {
			System.out.println("Error: PropertyGradient.writeXML()");
			e.printStackTrace();
		}

		return xml;
	}
}