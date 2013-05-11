package reuze.app;

import processing.core.PGraphics;
import reuze.app.appGUI.FrameCreator;

public class GradientFrame extends MinyFrame implements appGUI.FrameCreator {
	/**
	 * 
	 */
	private final appGUI appGUI;
	private ColorGradient _value;
	private MinyColor _color;
	private PropertyGradient _property;
	private ColorChooserFrame _frame;
	private PGraphics _gradient;
	private int _editingPos, _editingColor, _selected;

	GradientFrame(appGUI appGUI, MinyGUI parent, String name,
			ColorGradient value, PropertyGradient property) {
		super(appGUI, parent, name);
		this.appGUI = appGUI;
		_value = value;
		_property = property;
		_color = new MinyColor(this.appGUI, this.appGUI.color(0));
		_editingPos = _editingColor = _selected = -1;

		setMoveable(true);
		setResizeable(true);
		_minW = 200;
		if (appGUI._imgChecker == null) {
			appGUI._imgChecker = this.appGUI.createImage(100, 30, appGUI.RGB);
			appGUI._imgChecker.loadPixels();
			for (int x = 0; x < appGUI._imgChecker.width; x++) {
				for (int y = 0; y < appGUI._imgChecker.height; y++) {
					boolean b = (x % 20 < 10);
					if (y % 20 < 10)
						b = !b;
					appGUI._imgChecker.pixels[y * appGUI._imgChecker.width + x] = b ? this.appGUI
							.color(160) : this.appGUI.color(95);
				}
			}
			appGUI._imgChecker.updatePixels();
		}
		if (appGUI._imgSmallChecker == null) {
			appGUI._imgSmallChecker = this.appGUI.createImage(15, 15,
					appGUI.RGB);
			appGUI._imgSmallChecker.loadPixels();
			for (int x = 0; x < appGUI._imgSmallChecker.width; x++) {
				for (int y = 0; y < appGUI._imgSmallChecker.height; y++) {
					boolean b = (x < 8);
					if (y < 8)
						b = !b;
					appGUI._imgSmallChecker.pixels[y
							* appGUI._imgSmallChecker.width + x] = b ? this.appGUI
							.color(160) : this.appGUI.color(95);
				}
			}
			appGUI._imgSmallChecker.updatePixels();
		}
	}

	void placeAt(int x, int y) {
		placeAt(x, y, 350, 105);
		updateGradient();
	}

	void onClose() {
		_property.onCloseFrame(this);
	}

	void onResize() {
		updateGradient();
	}

	Rect getBox() {
		Rect r = getClientArea();
		r.x += 10;
		r.w -= 20;
		r.y += 5;
		r.h = 30;
		return r;
	}

	void updateGradient() {
		Rect r = getBox();
		_gradient = this.appGUI.createGraphics(r.w, r.h, appGUI.JAVA2D);
		_gradient.beginDraw();
		_gradient.background(this.appGUI.color(255, 255, 255, 0));
		// checker background
		for (int x = 0; x < r.w; x += appGUI._imgChecker.width)
			_gradient.image(appGUI._imgChecker, x, 0);

		// color gradient
		int[] v = _value.getTable(r.w);
		for (int x = 0; x < r.w; x++) {
			_gradient.stroke(v[x]);
			_gradient.line(x, 0, x, 40);
		}
		_gradient.endDraw();
	}

	public void update() {
		super.update();
		_box.h = 105;

		if (_parent.hasLock(this)) {
			if (!this.appGUI.mousePressed) {
				_parent.releaseLock(this);
				// if(_editingPos != -1) updateGradient();
				_editingPos = -1;
			} else if (_editingPos != -1
					&& this.appGUI.pmouseX != this.appGUI.mouseX) {
				Rect r = getClientArea();
				// int w = getBox().w;
				float p = appGUI.constrain(
						appGUI.map(this.appGUI.mouseX - r.x, 0, r.w, 0, 1), 0,
						1);
				if (p == 0 && _editingPos != 0) {
					if (_frame != null)
						_parent.removeFrame(_frame);
					_value.remove(_editingPos);
					_selected = -1;
					_editingPos = -1;
				} else if (_editingPos != 0)
					_value.setPosition(_editingPos, p);
				updateGradient();
			}
		}
	}

	public void onMousePressed() {
		super.onMousePressed();
		_selected = -1;

		Rect r = getBox();
		if (this.appGUI.overRect(r)) {
			// if(_parent.getModCtrl())
			{
				float p = appGUI.constrain(
						appGUI.map(this.appGUI.mouseX - r.x, 0, r.w, 0, 1), 0,
						1);
				int c = _value.get(p);
				_value.add(p, c);
				updateGradient();
			}
		}

		for (int i = 0; i < _value.size(); i++) {
			int x = r.x
					+ appGUI.floor(appGUI.map(_value.getPosition(i), 0, 1, 0,
							r.w - 1));
			int y = r.y + r.h;
			if (this.appGUI.overRect(x - 8, y + 24, 16, 16)) {
				_color.setValue(_value.getValue(i));
				if (_frame == null) {
					_frame = new ColorChooserFrame(this.appGUI, _parent, _name
							+ " (" + i + ")", _color, this);
					_frame.placeAt(this.appGUI.mouseX, this.appGUI.mouseY);
					_parent.addFrame(_frame);
				} else if (_editingColor != i) {
					_frame.computeColorBoxes();
					_frame._name = _name + " (" + i + ")";
				}
				_editingColor = i;
				_parent.changeFocus(_frame);
				_parent.putFrameOnTop(_frame);
				return;
			}

			if (this.appGUI.overRect(x - 8, y, 16, 24)) {
				_selected = i;
				_editingPos = i;
				_parent.getLock(this);
				return;
			}
		}
	}

	/*
	 * public void onKeyPressed() { super.onKeyPressed();
	 * 
	 * if(_selected != -1 && (keyCode == DELETE || keyCode == BACKSPACE)) //key
	 * == CODED && { if(_frame != null && _editingColor == _selected)
	 * _parent.removeFrame(_frame); _value.remove(_selected); _selected = -1;
	 * updateGradient(); } }
	 */

	public void display() {
		super.display();

		Rect r = getBox();
		if (_editingColor != -1
				&& _color.getValue() != _value.getValue(_editingColor)) {
			_value.setValue(_editingColor, _color.getValue());
			updateGradient();
		} else if (_gradient == null || _gradient.width != r.w
				|| _gradient.height != r.h)
			updateGradient();

		int w = r.w;
		this.appGUI.image(_gradient, r.x, r.y);
		this.appGUI.stroke(_parent.fg);
		this.appGUI.noFill();
		r.x--;
		r.y--;
		r.w++;
		r.h++;
		this.appGUI.rect(r);

		r = getClientArea();
		for (int i = 0; i < _value.size(); i++) {
			int x = r.x
					+ 10
					+ appGUI.floor(appGUI.map(_value.getPosition(i), 0, 1, 0,
							w - 1));
			int y = r.y + 35;

			if (_selected == i)
				this.appGUI.fill(_parent.selectColor);
			else
				this.appGUI.noFill();

			// selection handles
			this.appGUI.beginShape();
			this.appGUI.vertex(x, y);
			this.appGUI.vertex(x + 8, y + 16);
			this.appGUI.vertex(x + 8, y + 24);
			this.appGUI.vertex(x - 8, y + 24);
			this.appGUI.vertex(x - 8, y + 16);
			this.appGUI.vertex(x, y);
			this.appGUI.endShape();

			// color squares
			this.appGUI.image(appGUI._imgSmallChecker, x - 7, y + 25);
			// copy(_imgChecker, 0, 0, 20, 20, x-7, y+25, 15, 15);
			this.appGUI.fill(_value.getValue(i));
			this.appGUI.rect(x - 8, y + 24, 16, 16);
		}
	}

	public void onCloseFrame(MinyFrame frame) {
		_editingColor = -1;
		_frame = null;
	}
}