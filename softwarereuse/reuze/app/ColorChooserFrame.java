package reuze.app;

import processing.core.PGraphics;
import processing.core.PImage;
import reuze.app.appGUI.FrameCreator;

public class ColorChooserFrame extends MinyFrame {
	/**
	 * 
	 */
	private final appGUI appGUI;
	private MinyColor _value;
	private FrameCreator _property;
	private PImage _imgChecker, _imgA, _imgR, _imgG, _imgB;
	private int _editing;
	private PGraphics _pg;

	ColorChooserFrame(appGUI appGUI, MinyGUI parent, String name,
			MinyColor value, FrameCreator property) {
		super(appGUI, parent, name);
		this.appGUI = appGUI;
		_value = value;
		_property = property;
		_editing = -1;
		setMoveable(true);
		_imgChecker = this.appGUI.createImage(256, 20, appGUI.RGB);
		_imgA = this.appGUI.createImage(256, 20, appGUI.ARGB);
		_imgR = this.appGUI.createImage(256, 20, appGUI.RGB);
		_imgG = this.appGUI.createImage(256, 20, appGUI.RGB);
		_imgB = this.appGUI.createImage(256, 20, appGUI.RGB);
		_pg = this.appGUI.createGraphics(256, 97, appGUI.JAVA2D);

		_imgChecker.loadPixels();
		for (int x = 0; x < 256; x++) {
			for (int y = 0; y < 20; y++) {
				boolean b = (x % 20 < 10);
				if (y % 20 < 10)
					b = !b;
				_imgChecker.pixels[y * 256 + x] = b ? this.appGUI.color(160)
						: this.appGUI.color(95);
			}
		}
		_imgChecker.updatePixels();

		computeColorBoxes();
	}

	void placeAt(int x, int y) {
		placeAt(x, y, 256 + 25, 105);
	}

	void computeColorBoxes() {
		int r, g, b; // ,a;
		int c = _value.getValue();
		/* a = c & 0xFF000000; */r = c & 0x00FF0000;
		g = c & 0x0000FF00;
		b = c & 0x000000FF;

		_imgR.loadPixels();
		for (int x = 0; x < 256; x++) {
			c = (x << 16) + g + b;
			for (int y = 0; y < 20; y++)
				_imgR.pixels[y * 256 + x] = c;
		}
		_imgR.updatePixels();

		_imgG.loadPixels();
		for (int x = 0; x < 256; x++) {
			c = r + (x << 8) + b;
			for (int y = 0; y < 20; y++)
				_imgG.pixels[y * 256 + x] = c;
		}
		_imgG.updatePixels();

		_imgB.loadPixels();
		for (int x = 0; x < 256; x++) {
			c = r + g + x;
			for (int y = 0; y < 20; y++)
				_imgB.pixels[y * 256 + x] = c;
		}
		_imgB.updatePixels();

		_imgA.loadPixels();
		for (int x = 0; x < 256; x++) {
			c = (x << 24) + r + g + b;
			for (int y = 0; y < 20; y++)
				_imgA.pixels[y * 256 + x] = c;
		}
		_imgA.updatePixels();
	}

	public void display() {
		super.display();
		Rect area = getClientArea();
		int r, g, b, a;
		int c = _value.getValue();
		a = (c >> 24) & 0xFF;
		r = (c >> 16) & 0xFF;
		g = (c >> 8) & 0xFF;
		b = c & 0xFF;
		// draw colors
		int x = area.x + 20, y = area.y + 5;
		this.appGUI.image(_imgR, x, y);
		y += 25;
		this.appGUI.image(_imgG, x, y);
		y += 25;
		this.appGUI.image(_imgB, x, y);
		y += 25;
		this.appGUI.image(_imgChecker, x, y);
		this.appGUI.image(_imgA, x, y);

		// line for each color value
		_pg.beginDraw();
		_pg.background(this.appGUI.color(255, 255, 255, 0));
		_pg.fill(-1, 0);
		_pg.noStroke();
		_pg.rect(0, 0, 256, 97);
		_pg.fill(this.appGUI.color(0));
		_pg.stroke(this.appGUI.color(255));
		_pg.triangle(r, 4, r - 4, 0, r + 4, 0);
		_pg.triangle(r, 17, r - 4, 21, r + 4, 21);
		_pg.triangle(g, 29, g - 4, 25, g + 4, 25);
		_pg.triangle(g, 42, g - 4, 46, g + 4, 46);
		_pg.triangle(b, 54, b - 4, 50, b + 4, 50);
		_pg.triangle(b, 67, b - 4, 71, b + 4, 71);
		_pg.triangle(a, 79, a - 4, 75, a + 4, 75);
		_pg.triangle(a, 92, a - 4, 96, a + 4, 96);
		_pg.endDraw();
		this.appGUI.image(_pg, area.x + 20, area.y + 4);

		// rectangles around each int area
		area.x += 19;
		area.w = 257;
		area.y += 4;
		area.h = 21;
		this.appGUI.noFill();
		this.appGUI.stroke(_parent.fg);
		this.appGUI.rect(area);
		area.y += 25;
		this.appGUI.rect(area);
		area.y += 25;
		this.appGUI.rect(area);
		area.y += 25;
		this.appGUI.rect(area);

		// text
		area = getClientArea();
		area.x += 5;
		area.y += 5;
		area.w = 20;
		area.h = 20;
		this.appGUI.noStroke();
		this.appGUI.fill(_parent.fg);
		this.appGUI.text("R", area);
		area.y += 25;
		this.appGUI.text("G", area);
		area.y += 25;
		this.appGUI.text("B", area);
		area.y += 25;
		this.appGUI.text("A", area);
	}

	void onClose() {
		_property.onCloseFrame(this);
	}

	public void onKeyPressed() {
		_parent.removeFrame(this);
	}

	public void onMousePressed() {
		super.onMousePressed();

		// int r,g,b,a;
		// int c = _value.getValue();
		// a = (c >> 24) & 0xFF; r = (c >> 16) & 0xFF; g = (c >> 8) & 0xFF; b =
		// c & 0xFF;

		Rect area = getClientArea();
		area.x += 20;
		area.y += 5;
		area.w = 256;
		area.h = 20;
		int v = appGUI.constrain(this.appGUI.mouseX - area.x, 0, 255);
		if (this.appGUI.overRect(area)) {
			_value.setRed(v);
			computeColorBoxes();
			_editing = 0;
			_parent.getLock(this);
			return;
		}

		area.y += 25;
		if (this.appGUI.overRect(area)) {
			_value.setGreen(v);
			computeColorBoxes();
			_editing = 1;
			_parent.getLock(this);
			return;
		}

		area.y += 25;
		if (this.appGUI.overRect(area)) {
			_value.setBlue(v);
			computeColorBoxes();
			_editing = 2;
			_parent.getLock(this);
			return;
		}

		area.y += 25;
		if (this.appGUI.overRect(area)) {
			_value.setAlpha(v);
			computeColorBoxes();
			_editing = 3;
			_parent.getLock(this);
			return;
		}
	}

	public void update() {
		super.update();

		if (_editing == -1)
			return;

		if (this.appGUI.mousePressed) {
			// int r,g,b,a;
			int x, v;
			// int c = _value.getValue();
			// a = (c >> 24) & 0xFF; r = (c >> 16) & 0xFF; g = (c >> 8) & 0xFF;
			// b = c & 0xFF;
			x = this.appGUI.mouseX - getClientArea().x - 20;
			v = appGUI.constrain(x, 0, 255);

			switch (_editing) {
			case 0:
				_value.setRed(v);
				computeColorBoxes();
				break;
			case 1:
				_value.setGreen(v);
				computeColorBoxes();
				break;
			case 2:
				_value.setBlue(v);
				computeColorBoxes();
				break;
			case 3:
				_value.setAlpha(v);
				computeColorBoxes();
				break;
			}
		} else {
			_editing = -1;
			_parent.releaseLock(this);
		}
	}
}