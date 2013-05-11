package reuze.app;

import java.io.IOException;
import java.io.StringWriter;

import com.software.reuze.ff_XMLWriter;

import processing.core.PGraphics;
import processing.core.PImage;
import reuze.app.appGUI.FrameCreator;
import reuze.app.appGUI.MinyValue;

public class PropertyColorChooser extends Property implements
		appGUI.FrameCreator {
	/**
	 * 
	 */
	private final appGUI appGUI;
	private MinyColor _value;
	private PImage _imgChecker;
	private ColorChooserFrame _frame;

	public PropertyColorChooser(appGUI appGUI, String name, MinyColor value) {
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

	public PropertyColorChooser(appGUI appGUI, String name) {
		this(appGUI, name, new MinyColor(appGUI));
	}

	public MinyValue get() {
		return _value;
	}

	public Rect getBox() {
		return new Rect(this.appGUI, (int) (_x + _w * 0.4 + 3), _y + 1,
				(int) (_w * 0.6 - 8), 18);
	}

	public void onMousePressed() {
		if (this.appGUI.overRect(getBox())) {
			if (_frame == null) {
				_frame = new ColorChooserFrame(this.appGUI, _parent, _name,
						_value, this);
				_frame.placeAt(this.appGUI.mouseX, this.appGUI.mouseY);
				_parent.addFrame(_frame);
			}
			_parent.changeFocus(_frame);
			_parent.putFrameOnTop(_frame);
		}
	}

	public void display(PGraphics pg, int y) {
		super.display(pg, y);
		Rect r = getBox();
		r.x -= _x;
		r.y += y - _y;
		int h = getHeight() - 3;
		int x;
		for (x = 1; x + h < r.w; x += h)
			pg.image(_imgChecker, r.x + x, r.y + 1);
		int w = r.w - x;
		pg.copy(_imgChecker, 0, 0, w, h, r.x + x, r.y + 1, w, h);

		pg.noFill();
		pg.stroke(_parent.fg);
		this.appGUI.rect(pg, r);

		// Bug if calling fill(color) if alpha = 0
		// fill(_value.getValue());
		pg.fill(_value.getRed(), _value.getGreen(), _value.getBlue(),
				_value.getAlpha());
		pg.noStroke();
		r.x++;
		r.y++;
		r.w--;
		r.h--;
		this.appGUI.rect(pg, r);
	}

	public void onCloseFrame(MinyFrame frame) {
		_frame = null;
	}

	public ff_XMLWriter writeXML(ff_XMLWriter xml, int i) {
		try {
			xml.element(String.valueOf(i))
					.attribute("type", "PropertyColorChooser")
					.element("name", _name).element("_value", _value._v).pop();
		} catch (IOException e) {
			System.out.println("Error: PropertyColorChooser.writeXML()");
			e.printStackTrace();
		}

		return xml;
	}
}