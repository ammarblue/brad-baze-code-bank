
import processing.core.*;
import java.io.*;
import java.util.*;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;

public class appGUI extends PApplet {
	/* OpenProcessing Tweak of *@*http://www.openprocessing.org/sketch/11032*@* */
	/*
	 * !do not delete the line above, required for linking your tweak if you
	 * re-upload
	 */
	/*
	 * TinyGUI by Christophe Guebert A small example to illustrate the MinyGUI
	 * Library. Now with color chooser, gradient and graph editors ! (it's click
	 * in color area to add a new point to gradient, drag point to far left to
	 * delete, point 0 cannot be moved/deleted) Sorry for the lack of comments
	 * in the code...
	 */

	MinyGUI gui;

	float time, lastTime;
	MinyBoolean running;
	MinyInteger speed;
	MinyFloat stWidth;
	// BWB032513
	MinyInteger rSize;
	MinyInteger time2;
	// END_BWB
	MinyString timeCaption;
	MinyColor borderColor;

	InterpolatedFloat rotation;
	ColorGradient gradient;

	public void mousePressed() {
		gui.onMousePressed();
	}

	public void keyPressed() {
		gui.onKeyPressed();
	}

	public void keyReleased() {
		gui.onKeyReleased();
	}

	PImage img;

	public void setup() {
		size(600, 400);
		smooth();
		img = loadImage("../data/mearth.jpg");
		time = 0;
		running = new MinyBoolean(true);
		this.speed = new MinyInteger(2);
		timeCaption = new MinyString("0.0");
		stWidth = new MinyFloat(2.0f);
		// BWB032513
		rSize = new MinyInteger(1);
		time2=new MinyInteger();
		// END_BWB
		// BWB032513
		MinyInteger testint = new MinyInteger(1);
		// END_BWB
		borderColor = new MinyColor(color(192));
		rotation = new InterpolatedFloat(1);
		rotation.add(0, 0);
		rotation.add(1.5f, -PI);
		rotation.add(3.0f, PI);
		rotation.add(4.0f, 2 * PI);
		rotation.add(6.0f, 0);

		gradient = new ColorGradient(color(0));
		gradient.add(0, color(0, 1));
		gradient.add(0.25f, color(255, 0, 0));
		gradient.add(0.5f, color(0, 255, 0));
		gradient.add(0.75f, color(0, 0, 255));
		gradient.add(1, color(255));

		gui = new MinyGUI(0, 0, 200, height);// height=400
		new PropertyButton("Start", new TestButton()).add(gui);
		new PropertyButton("Tester", new TestButton()).add(gui);
		new PropertyButtonImage("../data/particle.png", new TestButton())
				.add(gui);
		new PropertyCheckBox("Running", running).add(gui);
		new PropertyList("Speed", speed, "slowest;slow;normal;fast;fastest")
				.add(gui);
		new PropertyDisplay("Time", timeCaption).add(gui);
		new PropertyEditFloat("Border width", stWidth).add(gui);
		// BWB032513
		new PropertySliderInteger("Rect size", rSize, 1, 10).add(gui);
		// END_BWB
		// BWB032513
		new PropertyProgBar("Size", rSize, 10,1).add(gui);
		new PropertyProgBar("Time", time2,20,2).add(gui);
		// END_BWB
		new PropertyColorChooser("Fill color", borderColor).add(gui);
		new PropertyGraph("Rotation", rotation).add(gui);
		new PropertyGradient("Gradient", gradient).add(gui);

		gui.fg = color(0);
		gui.bg = color(255);
		gui.selectColor = color(196);
		for (Property p : gui)
			System.out.println(p.get().getString());
		// gui.drawBackground=false; //uncomment for image background
	}

	class TestButton implements ButtonCallback {
		public void onButtonPressed() {
			time = 0;
			running.setValue(true);
		}
	}

	public void draw() {
		background(0);

		float t = millis() / 1000.0f;
		float dt = t - lastTime;
		dt *= pow(2, speed.getValue() - 2);
		if (running.getValue())
			time += dt;

		float m = rotation.getXMax();
		if (m == 0)
			m = 1;
		if (time > m)
			running.setValue(false);
		lastTime = t;
		timeCaption.setValue(nf(time, 0, 2));
		time2.setValue((int)time);

		pushMatrix();// layer controel
		translate(400, 200);
		rotate(rotation.get(time));
		scale(rSize.getValue());
		float f = stWidth.getValue();
		if (f < 0)
			f = 0;
		if (f > 20)
			f = 20;
		strokeWeight(f);
		stroke(borderColor.getValue());
		fill(gradient.get(time / m));
		rect(-50, -30, 100, 60);
		popMatrix();
		image(img, 0, 0, 200, height);
		gui.display();// sart the gui
	}

	// ---------------------------------------------------
	class ColorGradient {
		class VarKey {
			float position;
			int value;
		}

		private int _defaultValue;
		private boolean _useLookupTable;
		private int[] _lookupTable;
		private int _interpolationMethod;

		ArrayList<VarKey> keysList;

		ColorGradient(int defaultValue) {
			_defaultValue = defaultValue;
			_useLookupTable = false;
			keysList = new ArrayList<VarKey>();
			_interpolationMethod = 0;
		}

		int getInterpolation() {
			return _interpolationMethod;
		}

		void setInterpolation(int i) {
			_interpolationMethod = i % 5;
		}

		int size() {
			return keysList.size();
		}

		void clear() {
			keysList.clear();
			update();
		}

		void remove(int index) {
			keysList.remove(index);
			update();
		}

		int add(float pos, int val) {
			pos = constrain(pos, 0, 1);

			VarKey newVar = new VarKey();
			newVar.position = pos;
			newVar.value = val;

			int size = keysList.size();
			for (int i = 0; i < size; i++) {
				VarKey v = (VarKey) keysList.get(i);
				if (v.position == pos) // don't add, just update
				{
					v.value = val;
					update();
					return i;
				}
			}

			keysList.add(newVar);
			update();
			return size;
		}

		void set(int index, float pos, int val) {
			pos = constrain(pos, 0, 1);
			VarKey var = (VarKey) keysList.get(index);
			var.position = pos;
			var.value = val;
			update();
		}

		void setValue(int index, int val) {
			((VarKey) keysList.get(index)).value = val;
			update();
		}

		void setPosition(int index, float pos) {
			pos = constrain(pos, 0, 1);
			((VarKey) keysList.get(index)).position = pos;
			update();
		}

		float getPosition(int index) {
			return ((VarKey) keysList.get(index)).position;
		}

		int getValue(int index) {
			return ((VarKey) keysList.get(index)).value;
		}

		int getPrev(float pos) {
			int size = keysList.size();
			int best = -1;
			float bestPos = pos;
			for (int i = 0; i < size; i++) {
				float p = getPosition(i);
				if (p < pos && (bestPos <= p || bestPos == pos)) {
					bestPos = p;
					best = i;
				}
			}

			return best;
		}

		int getPrev(int index) {
			return getPrev(getPosition(index));
		}

		int getNext(float pos) {
			int size = keysList.size();
			int best = -1;
			float bestPos = pos;
			for (int i = 0; i < size; i++) {
				float p = getPosition(i);
				if (p > pos && (bestPos >= p || bestPos == pos)) {
					bestPos = p;
					best = i;
				}
			}

			return best;
		}

		int getNext(int index) {
			return getNext(getPosition(index));
		}

		int get(float pos) {
			int size = keysList.size();
			if (size == 0)
				return _defaultValue;
			if (size == 1)
				return ((VarKey) keysList.get(0)).value;

			if (_useLookupTable) {
				int i = constrain(floor(pos * _lookupTable.length), 0,
						_lookupTable.length - 1);
				return _lookupTable[i];
			}

			TreeMap<Float, Integer> sorted = getSortedMap();

			if (_interpolationMethod != 4 && sorted.containsKey(pos))
				return (Integer) sorted.get(pos);

			SortedMap<Float, Integer> left, right;
			left = sorted.headMap(pos);
			if (left.size() == 0)
				return (Integer) sorted.get(sorted.firstKey());
			right = sorted.tailMap(pos);
			if (right.size() == 0)
				return (Integer) sorted.get(sorted.lastKey());

			float leftKey, rightKey;
			leftKey = (Float) left.lastKey();
			rightKey = (Float) right.firstKey();

			float amt = (pos - leftKey) / (rightKey - leftKey);

			int leftValue, rightValue;
			leftValue = (Integer) sorted.get(leftKey);
			rightValue = (Integer) sorted.get(rightKey);

			switch (_interpolationMethod) {
			case 0:
			default:
				return lerpColor(leftValue, rightValue, (1 - cos(amt * PI)) / 2);
			case 1:
				return lerpColor(leftValue, rightValue, amt);
			case 2:
				return rightValue;
			case 3:
				return leftValue;
			case 4:
				return amt < 0.5 ? leftValue : rightValue;
				// return (leftValue+rightValue)/2;
			}
		}

		// prepare a lookup table used in following calls of "get"
		void useLookupTable(int size) {
			_lookupTable = new int[size];
			_useLookupTable = true;
			update();
		}

		// compute a lookup table, not kept
		int[] getTable(int size) {
			int[] v = new int[size];
			boolean b = _useLookupTable;
			_useLookupTable = false;
			for (int i = 0; i < size; i++) {
				float p = (float) i / (size - 1);
				v[i] = get(p);
			}
			_useLookupTable = b;
			return v;
		}

		private TreeMap<Float, Integer> getSortedMap() {
			int size = keysList.size();
			if (size == 0)
				return null;
			TreeMap<Float, Integer> mp = new TreeMap<Float, Integer>();

			for (int i = 0; i < size; i++) {
				VarKey v = (VarKey) keysList.get(i);
				mp.put(v.position, v.value);
			}

			return mp;
		}

		private void update() {
			if (!_useLookupTable)
				return;
			_useLookupTable = false;
			for (int i = 0; i < _lookupTable.length; i++) {
				float p = (float) i / (_lookupTable.length - 1);
				_lookupTable[i] = get(p);
			}
			_useLookupTable = true;
		}
	}

	static PImage _imgChecker, _imgSmallChecker;

	class GradientFrame extends MinyFrame implements FrameCreator {
		private ColorGradient _value;
		private MinyColor _color;
		private PropertyGradient _property;
		private ColorChooserFrame _frame;
		private PGraphics _gradient;
		private int _editingPos, _editingColor, _selected;

		GradientFrame(MinyGUI parent, String name, ColorGradient value,
				PropertyGradient property) {
			super(parent, name);
			_value = value;
			_property = property;
			_color = new MinyColor(color(0));
			_editingPos = _editingColor = _selected = -1;

			setMoveable(true);
			setResizeable(true);
			_minW = 200;
			if (_imgChecker == null) {
				_imgChecker = createImage(100, 30, RGB);
				_imgChecker.loadPixels();
				for (int x = 0; x < _imgChecker.width; x++) {
					for (int y = 0; y < _imgChecker.height; y++) {
						boolean b = (x % 20 < 10);
						if (y % 20 < 10)
							b = !b;
						_imgChecker.pixels[y * _imgChecker.width + x] = b ? color(160)
								: color(95);
					}
				}
				_imgChecker.updatePixels();
			}
			if (_imgSmallChecker == null) {
				_imgSmallChecker = createImage(15, 15, RGB);
				_imgSmallChecker.loadPixels();
				for (int x = 0; x < _imgSmallChecker.width; x++) {
					for (int y = 0; y < _imgSmallChecker.height; y++) {
						boolean b = (x < 8);
						if (y < 8)
							b = !b;
						_imgSmallChecker.pixels[y * _imgSmallChecker.width + x] = b ? color(160)
								: color(95);
					}
				}
				_imgSmallChecker.updatePixels();
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
			_gradient = createGraphics(r.w, r.h, JAVA2D);
			_gradient.beginDraw();
			_gradient.background(color(255, 255, 255, 0));
			// checker background
			for (int x = 0; x < r.w; x += _imgChecker.width)
				_gradient.image(_imgChecker, x, 0);

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
				if (!mousePressed) {
					_parent.releaseLock(this);
					// if(_editingPos != -1) updateGradient();
					_editingPos = -1;
				} else if (_editingPos != -1 && pmouseX != mouseX) {
					Rect r = getClientArea();
					// int w = getBox().w;
					float p = constrain(map(mouseX - r.x, 0, r.w, 0, 1), 0, 1);
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
			if (overRect(r)) {
				// if(_parent.getModCtrl())
				{
					float p = constrain(map(mouseX - r.x, 0, r.w, 0, 1), 0, 1);
					int c = _value.get(p);
					_value.add(p, c);
					updateGradient();
				}
			}

			for (int i = 0; i < _value.size(); i++) {
				int x = r.x
						+ floor(map(_value.getPosition(i), 0, 1, 0, r.w - 1));
				int y = r.y + r.h;
				if (overRect(x - 8, y + 24, 16, 16)) {
					_color.setValue(_value.getValue(i));
					if (_frame == null) {
						_frame = new ColorChooserFrame(_parent, _name + " ("
								+ i + ")", _color, this);
						_frame.placeAt(mouseX, mouseY);
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

				if (overRect(x - 8, y, 16, 24)) {
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
		 * if(_selected != -1 && (keyCode == DELETE || keyCode == BACKSPACE))
		 * //key == CODED && { if(_frame != null && _editingColor == _selected)
		 * _parent.removeFrame(_frame); _value.remove(_selected); _selected =
		 * -1; updateGradient(); } }
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
			image(_gradient, r.x, r.y);
			stroke(_parent.fg);
			noFill();
			r.x--;
			r.y--;
			r.w++;
			r.h++;
			rect(r);

			r = getClientArea();
			for (int i = 0; i < _value.size(); i++) {
				int x = r.x + 10
						+ floor(map(_value.getPosition(i), 0, 1, 0, w - 1));
				int y = r.y + 35;

				if (_selected == i)
					fill(_parent.selectColor);
				else
					noFill();

				// selection handles
				beginShape();
				vertex(x, y);
				vertex(x + 8, y + 16);
				vertex(x + 8, y + 24);
				vertex(x - 8, y + 24);
				vertex(x - 8, y + 16);
				vertex(x, y);
				endShape();

				// color squares
				image(_imgSmallChecker, x - 7, y + 25);
				// copy(_imgChecker, 0, 0, 20, 20, x-7, y+25, 15, 15);
				fill(_value.getValue(i));
				rect(x - 8, y + 24, 16, 16);
			}
		}

		public void onCloseFrame(MinyFrame frame) {
			_editingColor = -1;
			_frame = null;
		}
	}

	class PropertyGradient extends Property {
		private ColorGradient _value;
		private PImage _imgChecker;
		private PGraphics _gradient;
		private GradientFrame _frame;

		PropertyGradient(String name, ColorGradient value) {
			super(name);
			_value = value;
			int h = getHeight() - 3;
			_imgChecker = createImage(h, h, RGB);
			_imgChecker.loadPixels();
			for (int x = 0; x < h; x++) {
				for (int y = 0; y < h; y++) {
					boolean b = (x % h < h / 2);
					if (y % h < h / 2)
						b = !b;
					_imgChecker.pixels[y * h + x] = b ? color(160) : color(95);
				}
			}
			_imgChecker.updatePixels();
		}

		Rect getBox() {
			return new Rect((int) (_x + _w * 0.4 + 3), _y + 1,
					(int) (_w * 0.6 - 8), 18);
		}

		public void onMousePressed() {
			if (overRect(getBox())) {
				if (_frame == null) {
					_frame = new GradientFrame(_parent, _name, _value, this);
					_frame.placeAt(mouseX, mouseY);
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
				_gradient = createGraphics(r.w, r.h, JAVA2D);
			_gradient.beginDraw();
			_gradient.background(color(255, 255, 255, 0));
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
			rect(pg, r);

			if (_gradient == null || r.w - 1 != _gradient.width)
				updateGradient();

			pg.noFill();
			pg.stroke(_parent.fg);
			rect(pg, r);
			pg.image(_gradient, r.x + 1, r.y + 1);
		}

		void onCloseFrame(MinyFrame frame) {
			_frame = null;
			updateGradient();
		}
	}

	// ---------------------------------------------------
	class InterpolatedFloat {
		class VarKey {
			float position, value;
		}

		private float _defaultValue;
		private boolean _useLookupTable;
		private float _lookupMin, _lookupMax;
		private float[] _lookupTable;
		private float _XMin, _XMax, _YMin, _YMax;
		private boolean _limitXMin, _limitXMax, _limitYMin, _limitYMax;
		private int _interpolationMethod;

		ArrayList<VarKey> keysList;

		InterpolatedFloat(float defaultValue) {
			_defaultValue = defaultValue;
			_useLookupTable = false;
			keysList = new ArrayList<VarKey>();
			_interpolationMethod = 0;

			_XMin = 0;
			_YMin = 0;
			_XMax = 1;
			_YMax = 1;
			_limitXMin = _limitXMax = _limitYMin = _limitYMax = false;
		}

		void setXMin(float v) {
			_limitXMin = true;
			_XMin = v;
		}

		boolean limitXMin() {
			return _limitXMin;
		}

		float getXMin() {
			return _XMin;
		}

		void setXMax(float v) {
			_limitXMax = true;
			_XMax = v;
		}

		boolean limitXMax() {
			return _limitXMax;
		}

		float getXMax() {
			return _XMax;
		}

		void setYMin(float v) {
			_limitYMin = true;
			_YMin = v;
		}

		boolean limitYMin() {
			return _limitYMin;
		}

		float getYMin() {
			return _YMin;
		}

		void setYMax(float v) {
			_limitYMax = true;
			_YMax = v;
		}

		boolean limitYMax() {
			return _limitYMax;
		}

		float getYMax() {
			return _YMax;
		}

		int getInterpolation() {
			return _interpolationMethod;
		}

		void setInterpolation(int i) {
			_interpolationMethod = i % 5;
		}

		int size() {
			return keysList.size();
		}

		void clear() {
			keysList.clear();
			update();
		}

		void remove(int index) {
			keysList.remove(index);
			update();
		}

		int add(float pos, float val) {
			if (_limitXMin)
				pos = max(_XMin, pos);
			if (_limitXMax)
				pos = min(_XMax, pos);
			if (_limitYMin)
				val = max(_YMin, val);
			if (_limitYMax)
				val = min(_YMax, val);

			VarKey newVar = new VarKey();
			newVar.position = pos;
			newVar.value = val;

			int size = keysList.size();
			for (int i = 0; i < size; i++) {
				VarKey v = (VarKey) keysList.get(i);
				if (v.position == pos) // don't add, just update
				{
					v.value = val;
					update();
					return i;
				}
			}

			keysList.add(newVar);
			update();
			return size;
		}

		void set(int index, float pos, float val) {
			VarKey var = (VarKey) keysList.get(index);
			if (_limitXMin)
				pos = max(_XMin, pos);
			if (_limitXMax)
				pos = min(_XMax, pos);
			if (_limitYMin)
				val = max(_YMin, val);
			if (_limitYMax)
				val = min(_YMin, val);
			var.position = pos;
			var.value = val;
			update();
		}

		void setValue(int index, float val) {
			if (_limitYMin)
				val = max(_YMin, val);
			if (_limitYMax)
				val = min(_YMin, val);
			((VarKey) keysList.get(index)).value = val;
			update();
		}

		void setPosition(int index, float pos) {
			if (_limitXMin)
				pos = max(_XMin, pos);
			if (_limitXMax)
				pos = min(_XMax, pos);
			((VarKey) keysList.get(index)).position = pos;
			update();
		}

		float getPosition(int index) {
			return ((VarKey) keysList.get(index)).position;
		}

		float getValue(int index) {
			return ((VarKey) keysList.get(index)).value;
		}

		int getPrev(float pos) {
			int size = keysList.size();
			int best = -1;
			float bestPos = pos;
			for (int i = 0; i < size; i++) {
				float p = getPosition(i);
				if (p < pos && (bestPos <= p || bestPos == pos)) {
					bestPos = p;
					best = i;
				}
			}

			return best;
		}

		int getPrev(int index) {
			return getPrev(getPosition(index));
		}

		int getNext(float pos) {
			int size = keysList.size();
			int best = -1;
			float bestPos = pos;
			for (int i = 0; i < size; i++) {
				float p = getPosition(i);
				if (p > pos && (bestPos >= p || bestPos == pos)) {
					bestPos = p;
					best = i;
				}
			}

			return best;
		}

		int getNext(int index) {
			return getNext(getPosition(index));
		}

		float get(float pos) {
			int size = keysList.size();
			if (size == 0)
				return _defaultValue;
			if (size == 1)
				return ((VarKey) keysList.get(0)).value;

			if (_useLookupTable) {
				float p = map(pos, _lookupMin, _lookupMax, 0, 1)
						* _lookupTable.length;
				int i = constrain(floor(p), 0, _lookupTable.length - 1);
				return _lookupTable[i];
			}

			TreeMap<Float, Float> sorted = getSortedMap();

			if (_interpolationMethod != 4 && sorted.containsKey(pos))
				return (Float) sorted.get(pos);

			SortedMap<Float, Float> left, right;
			left = sorted.headMap(pos);
			if (left.size() == 0)
				return (Float) sorted.get(sorted.firstKey());
			right = sorted.tailMap(pos);
			if (right.size() == 0)
				return (Float) sorted.get(sorted.lastKey());

			float leftKey, rightKey;
			leftKey = (Float) left.lastKey();
			rightKey = (Float) right.firstKey();

			float amt = (pos - leftKey) / (rightKey - leftKey);

			float leftValue, rightValue;
			leftValue = (Float) sorted.get(leftKey);
			rightValue = (Float) sorted.get(rightKey);

			switch (_interpolationMethod) {
			case 0:
			default:
				return lerp(leftValue, rightValue, (1 - cos(amt * PI)) / 2);
			case 1:
				return lerp(leftValue, rightValue, amt);
			case 2:
				return rightValue;
			case 3:
				return leftValue;
			case 4:
				return amt < 0.5 ? leftValue : rightValue;
				// return (leftValue+rightValue)/2;
			}
		}

		// prepare a lookup table used in following calls of "get"
		void useLookupTable(int size, float from, float to) {
			_lookupMin = from;
			_lookupMax = to;
			_lookupTable = new float[size];
			_useLookupTable = true;
			update();
		}

		void useLookupTable(int size) {
			useLookupTable(size, _XMin, _XMax);
		}

		// compute a lookup table, not kept
		float[] getTable(int size, float from, float to) {
			float[] v = new float[size];
			boolean b = _useLookupTable;
			_useLookupTable = false;
			for (int i = 0; i < size; i++) {
				float p = map(i, 0, size - 1, from, to);
				v[i] = get(p);
			}
			_useLookupTable = b;
			return v;
		}

		float[] getTable(int size) {
			return getTable(size, _XMin, _XMax);
		}

		private TreeMap<Float, Float> getSortedMap() {
			int size = keysList.size();
			if (size == 0)
				return null;
			TreeMap<Float, Float> mp = new TreeMap<Float, Float>();

			for (int i = 0; i < size; i++) {
				VarKey v = (VarKey) keysList.get(i);
				mp.put(v.position, v.value);
			}

			return mp;
		}

		private void update() {
			updateLimits();
			if (!_useLookupTable)
				return;
			_useLookupTable = false;
			for (int i = 0; i < _lookupTable.length; i++) {
				float p = map(i, 0, _lookupTable.length - 1, _lookupMin,
						_lookupMax);
				_lookupTable[i] = get(p);
			}
			_useLookupTable = true;
		}

		private void updateLimits() {
			float tXMin = _XMin, tXMax = _XMax, tYMin = _YMin, tYMax = _YMax;
			int size = keysList.size();
			if (size <= 1) {
				if (size == 0) {
					tXMin = 0;
					tXMax = 1;
				} else {
					float p = getPosition(0);
					tXMin = p - 0.5f;
					tXMax = p + 0.5f;
				}

				float v = getValue(0);
				tYMin = v - 0.5f;
				tYMax = v + 0.5f;
			} else {
				tXMin = tXMax = getPosition(0);
				tYMin = tYMax = getValue(0);
				for (int i = 1; i < size; i++) {
					float p = getPosition(i);
					if (p < tXMin)
						tXMin = p;
					else if (p > tXMax)
						tXMax = p;

					float v = getValue(i);
					if (v < tYMin)
						tYMin = v;
					else if (v > tYMax)
						tYMax = v;
				}
			}

			if (!_limitXMin)
				_XMin = tXMin;
			if (!_limitXMax)
				_XMax = tXMax;
			if (!_limitYMin)
				_YMin = tYMin;
			if (!_limitYMax)
				_YMax = tYMax;
		}
	}

	class GraphFrame extends MinyFrame {
		private int _selected, _over;
		private InterpolatedFloat _value;
		private boolean _drawGrid;
		private float _minX, _maxX, _minY, _maxY;
		private final float bigIncrement = 0.1f, smallIncrement = 0.01f;
		private float[] _graphValues;
		private FrameCreator _property;

		GraphFrame(MinyGUI parent, String name, InterpolatedFloat value,
				FrameCreator property) {
			super(parent, name);
			_value = value;
			_property = property;
			_selected = _over = -1;
			_drawGrid = false;
			setMoveable(true);
			setResizeable(true);
			_minW = 300;
			_minH = 200;
			_minX = _value.getXMin();
			_maxX = _value.getXMax();
			_minY = _value.getYMin();
			_maxY = _value.getYMax();
		}

		void placeAt(int x, int y) {
			placeAt(x, y, 350, 250);
			updateGraph();
		}

		void onClose() {
			_property.onCloseFrame(this);
		}

		void onResize() {
			updateGraph();
		}

		Rect getGraphArea() {
			Rect r = getClientArea();
			r.x += 40;
			r.w -= 45;
			r.y += 10;
			r.h -= 40;
			return r;
		}

		void setGrid(boolean b) {
			_drawGrid = b;
		}

		void updateGraph() {
			Rect r = getGraphArea();
			_graphValues = _value.getTable(r.w, _minX, _maxX);
			for (int x = 0; x < _graphValues.length; x++)
				_graphValues[x] = map(_graphValues[x], _minY, _maxY, r.h, 0);
		}

		void drawAxis() {
			Rect r = getGraphArea();
			stroke(_parent.fg);
			fill(_parent.fg);
			NumberFormat formatter = new DecimalFormat("0.##E0");
			int alpha = (_parent.fg >> 24) & 0xFF;
			alpha = alpha / 3;
			int gridColor = _parent.fg & 0x00FFFFFF + (alpha << 24);

			// Positions
			textAlign(CENTER);
			float l = log(_maxX - _minX) / log(10);
			l = l < 0 ? -ceil(abs(l)) : floor(l);
			float xn = pow(10, l);
			int nb = ceil((_maxX - _minX) * 1.001f / xn);

			if (nb <= 2)
				xn /= 5;
			else if (nb < 5)
				xn /= 2;
			else if (nb > 10)
				xn *= 2;

			float x1 = ceil(_minX / xn) * xn;
			float x2 = floor(_maxX / xn) * xn;
			nb = ceil((x2 - x1) / xn) + 1;

			for (int i = 0; i < nb; i++) {
				float x = x1 + xn * i;
				if (x > _maxX)
					break;
				float sx = r.x + map(x, _minX, _maxX, 0, r.w);
				if (_drawGrid) {
					stroke(gridColor);
					line(sx, r.y, sx, r.y + r.h);
				}
				stroke(_parent.fg);
				line(sx, r.y + r.h, sx, r.y + r.h + 5);
				String caption = nf(x, 0, 0);
				// pushMatrix();
				// rotate(90);
				if (caption.length() > 4)
					caption = formatter.format(x);
				text(caption, sx - 25, r.y + r.h + 10, 50, 20);
				// popMatrix();
			}

			// Values
			l = log(_maxY - _minY) / log(10);
			l = l < 0 ? -ceil(abs(l)) : floor(l);
			float yn = pow(10, l);
			nb = ceil((_maxY - _minY) * 1.001f / yn);

			if (nb <= 2)
				yn /= 5;
			else if (nb < 5)
				yn /= 2;
			else if (nb > 10)
				yn *= 2;

			float y1 = ceil(_minY / yn) * yn;
			float y2 = floor(_maxY / yn) * yn;
			nb = ceil((y2 - y1) / yn) + 1;

			Rect area = getClientArea();
			for (int i = 0; i < nb; i++) {
				float y = y1 + yn * i;
				if (y > _maxY)
					break;
				float sy = r.y + map(y, _minY, _maxY, r.h, 0);
				if (_drawGrid) {
					stroke(gridColor);
					line(r.x, sy, r.x + r.w, sy);
				}
				stroke(_parent.fg);
				line(r.x - 5, sy, r.x, sy);
				String caption = nf(y, 0, 0);
				if (caption.length() > 4) {
					caption = formatter.format(y);
					textAlign(LEFT);
					text(caption, area.x, sy - 10, 100, 20);
				} else {
					textAlign(RIGHT);
					text(caption, area.x, sy - 10, r.x - area.x - 10, 20);
				}
			}

			textAlign(LEFT);
		}

		public void display() {
			super.display();

			noFill();
			stroke(_parent.fg);
			Rect r = getGraphArea();
			rect(r);

			float lastY = r.y + _graphValues[0];
			for (int x = 1; x < r.w; x++) {
				float y = r.y + _graphValues[x];
				line(r.x + x - 1, lastY, r.x + x, y);
				lastY = y;
			}

			drawAxis();

			// Draw selection handles
			for (int i = 0; i < _value.size(); i++) {
				float x = r.x
						+ map(_value.getPosition(i), _minX, _maxX, 0, r.w);
				float y = r.y + map(_value.getValue(i), _minY, _maxY, r.h, 0);
				if (_selected == i)
					fill(_parent.selectColor);
				else
					fill(_parent.bg);
				ellipse(x, y, 6, 6);
			}
		}

		public void update() {
			super.update();

			Rect r = getGraphArea();
			if (mousePressed) {
				if (_selected != -1) {
					if (pmouseX != mouseX) {
						if (mouseX < r.x && !_value.limitXMin()) {
							float t = max(
									0,
									map(pmouseX - mouseX, 0, r.w, 0, _maxX
											- _minX));
							if (t > 0) {
								float v = _value.getPosition(_selected) - t;
								_value.setPosition(_selected, v);
								_minX = v;
							}
						} else if (mouseX > r.x + r.w && !_value.limitXMax()) {
							float t = max(
									0,
									map(mouseX - pmouseX, 0, r.w, 0, _maxX
											- _minX));
							if (t > 0) {
								float v = _value.getPosition(_selected) + t;
								_value.setPosition(_selected, v);
								_maxX = v;
							}
						} else {
							float p = constrain(
									map(mouseX, r.x, r.x + r.w, _minX, _maxX),
									_minX, _maxX);
							_value.setPosition(_selected, p);
						}
						updateGraph();
					}
					if (pmouseY != mouseY) {
						if (mouseY < r.y && !_value.limitYMax()) {
							float t = max(
									0,
									map(pmouseY - mouseY, 0, r.h, 0, _maxY
											- _minY));
							if (t > 0) {
								float v = _value.getValue(_selected) + t;
								_value.setValue(_selected, v);
								_maxY = v;
							}
						} else if (mouseY > r.y + r.h && !_value.limitYMin()) {
							float t = max(
									0,
									map(mouseY - pmouseY, 0, r.h, 0, _maxY
											- _minY));
							if (t > 0) {
								float v = _value.getValue(_selected) - t;
								_value.setValue(_selected, v);
								_minY = v;
							}
						} else {
							float v = constrain(
									map(mouseY, r.y + r.h, r.y, _minY, _maxY),
									_minY, _maxY);
							_value.setValue(_selected, v);
						}
						updateGraph();
					}
				}
			} else {
				_over = -1;
				for (int i = 0; i < _value.size(); i++) {
					float x = r.x
							+ map(_value.getPosition(i), _minX, _maxX, 0, r.w);
					float y = r.y
							+ map(_value.getValue(i), _minY, _maxY, r.h, 0);
					if (dist(mouseX, mouseY, x, y) < 5) {
						_over = i;
						break;
					}
				}
			}
		}

		public void onMousePressed() {
			super.onMousePressed();
			_selected = -1;
			if (_over != -1) {
				_selected = _over;
				return;
			}

			if (_parent.getModCtrl()) {
				Rect r = getGraphArea();
				float p = constrain(map(mouseX, r.x, r.x + r.w, _minX, _maxX),
						_minX, _maxX);
				float v = constrain(map(mouseY, r.y + r.h, r.y, _minY, _maxY),
						_minY, _maxY);
				_selected = _value.add(p, v);
				updateGraph();
			}
		}

		public void onKeyPressed() {
			super.onKeyPressed();
			switch (key) {
			case CODED:
				switch (keyCode) {
				case UP:
					if (_selected != -1) {
						float v = _value.getValue(_selected);
						if (_parent.getModShift())
							v += (_maxY - _minY) * bigIncrement;
						else
							v += (_maxY - _minY) * smallIncrement;
						if (v > _maxY && !_value.limitYMax()) {
							_maxY = v;
							_value.setValue(_selected, v);
						} else
							_value.setValue(_selected, min(v, _maxY));
						updateGraph();
					}
					break;
				case DOWN:
					if (_selected != -1) {
						float v = _value.getValue(_selected);
						if (_parent.getModShift())
							v -= (_maxY - _minY) * bigIncrement;
						else
							v -= (_maxY - _minY) * smallIncrement;
						if (v < _minY && !_value.limitYMin()) {
							_minY = v;
							_value.setValue(_selected, v);
						} else
							_value.setValue(_selected, max(v, _minY));
						updateGraph();
					}
					break;
				case LEFT:
					if (_selected != -1) {
						float p = _value.getPosition(_selected);
						if (_parent.getModShift())
							p -= (_maxX - _minX) * bigIncrement;
						else
							p -= (_maxX - _minX) * smallIncrement;
						if (p < _minX && !_value.limitXMin()) {
							_minX = p;
							_value.setPosition(_selected, p);
						} else
							_value.setPosition(_selected, max(p, _minX));
						updateGraph();
					}
					break;
				case RIGHT:
					if (_selected != -1) {
						float p = _value.getPosition(_selected);
						if (_parent.getModShift())
							p += (_maxX - _minX) * bigIncrement;
						else
							p += (_maxX - _minX) * smallIncrement;
						if (p > _maxX && !_value.limitXMax()) {
							_maxX = p;
							_value.setPosition(_selected, p);
						} else
							_value.setPosition(_selected, min(p, _maxX));
						updateGraph();
					}
					break;
				}
				break;
			case TAB:
				if (_parent.getModShift()) {
					if (_selected != -1) {
						int temp = _value.getPrev(_selected);
						if (temp != -1)
							_selected = temp;
					} else
						_selected = _value.getPrev(_maxX + 1.0f);
				} else {
					if (_selected != -1) {
						int temp = _value.getNext(_selected);
						if (temp != -1)
							_selected = temp;
					} else
						_selected = _value.getNext(_minX - 1.0f);
				}
				break;
			case DELETE:
				if (_selected != -1) {
					float p = _value.getPosition(_selected);
					_value.remove(_selected);
					_selected = _value.getNext(p);
					updateGraph();
				}
				break;
			case BACKSPACE:
				if (_selected != -1) {
					float p = _value.getPosition(_selected);
					_value.remove(_selected);
					_selected = _value.getPrev(p);
					updateGraph();
				}
				break;
			case ' ':
				_minX = _value.getXMin();
				_maxX = _value.getXMax();
				_minY = _value.getYMin();
				_maxY = _value.getYMax();
				updateGraph();
				break;
			case 'g':
			case 'G':
				_drawGrid = !_drawGrid;
				break;
			case 'n':
			case 'N':
				_selected = _value
						.add((_maxX + _minX) / 2, (_maxY + _minY) / 2);
				updateGraph();
				break;
			case 'i':
			case 'I':
				_value.setInterpolation(_value.getInterpolation() + 1);
				updateGraph();
				break;
			case 'q':
			case 'Q':
				_parent.removeFrame(this);
				break;
			}
		}
	}

	class PropertyGraph extends Property implements FrameCreator {
		private InterpolatedFloat _value;
		private PGraphics _graph;
		private GraphFrame _frame;

		PropertyGraph(String name, InterpolatedFloat value) {
			super(name);
			_value = value;
		}

		void updateGraph() {
			Rect r = getBox();
			r.x++;
			r.w--;
			r.y++;
			r.h--;
			if (_graph == null || _graph.width != r.w || _graph.height != r.h)
				_graph = createGraphics(r.w, r.h, JAVA2D);
			_graph.beginDraw();
			_graph.background(color(255, 255, 255, 0));
			_graph.fill(-1);
			_graph.rect(0, 0, r.w, r.h);
			_graph.stroke(_parent.fg);
			float[] v = _value.getTable(r.w);
			float YMin = _value.getYMin(), YMax = _value.getYMax();
			float lastY = map(v[0], YMin, YMax, r.h - 1, 0);
			for (int x = 1; x < r.w; x++) {
				float y = map(v[x], YMin, YMax, r.h - 1, 0);
				_graph.line(x - 1, lastY, x, y);
				lastY = y;
			}
			_graph.endDraw();
		}

		public int getHeight() {
			return 40;
		}

		Rect getBox() {
			return new Rect((int) (_x + _w * 0.4 + 3), _y + 1,
					(int) (_w * 0.6 - 8), getHeight() - 2);
		}

		public void onMousePressed() {
			if (overRect(getBox())) {
				if (_frame == null) {
					_frame = new GraphFrame(_parent, _name, _value, this);
					_frame.placeAt(mouseX, mouseY);
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
			if (_graph == null || r.w - 1 != _graph.width
					|| _parent.fg != _graph.strokeColor)
				updateGraph();

			pg.noFill();
			pg.stroke(_parent.fg);
			rect(pg, r);
			pg.image(_graph, r.x + 1, r.y + 1);
		}

		public void onCloseFrame(MinyFrame frame) {
			_frame = null;
			updateGraph();
		}
	}

	// ---------------------------------------------------
	class MinyGUI implements Iterable<Property> {
		int bg, fg, selectColor;
		boolean drawBackground;

		private Rect _area;
		private int _totalH;
		private ArrayList<Property> properties;
		private ArrayList<MinyFrame> frames;
		private MinyWidget locked, focus;
		private VScrollbar scrollbar;
		private boolean _modAlt, _modShift, _modCtrl, _useScrollbar;
		private PGraphics _drawingSurface;

		MinyGUI(Rect r) {
			_area = r;

			_totalH = 0;

			locked = null;
			focus = null;
			bg = color(128);
			fg = color(0);
			selectColor = color(96);

			drawBackground = true; // TODO why does this exist? the image
									// overwrites

			properties = new ArrayList<Property>();
			frames = new ArrayList<MinyFrame>();

			_modAlt = _modShift = _modCtrl = _useScrollbar = false;
			scrollbar = new VScrollbar(this, new Rect(_area.x + _area.w - 15,
					_area.y, 14, _area.h - 1));
		}

		MinyGUI(int x, int y, int w, int h) {// 0,0,200,400
			this(new Rect(x, y, w, h));
		}

		void setPosition(Rect r) {
			_area = r;
		}

		void setPosition(int x, int y, int w, int h) {
			_area = new Rect(x, y, w, h);
		}

		private void update() {
			if (_useScrollbar)
				scrollbar.update();

			if (locked != null)
				locked.update();

			if (locked == null) {
				if (overRect(_area)) {
					for (int i = 0; i < properties.size(); i++)
						((Property) properties.get(i)).update();
				}

				for (int i = 0; i < frames.size(); i++)
					((MinyFrame) frames.get(i)).update();
			}
		}

		void getLock(MinyWidget p) {// if no lock, set lock to p
			if (locked == null)
				locked = p;
		}

		boolean hasLock(MinyWidget p) {// if you have lock
			return (locked == p);
		}

		boolean isLocked() {// is there a lock
			return locked != null;
		}

		void releaseLock(MinyWidget p) {// unlock
			if (locked == p)
				locked = null;
		}

		void changeFocus(MinyWidget p) {
			if (focus == p)
				return;
			if (locked != null && locked != p)
				return;

			if (focus != null)
				focus.lostFocus();
			focus = p;
			focus.getFocus();
		}

		void display() {
			update();

			noTint();
			imageMode(CORNERS);
			strokeWeight(1);

			if (drawBackground) {
				noStroke();
				fill(bg);
				rect(_area);
			}

			_useScrollbar = false;
			_totalH = 0;
			for (int i = 0; i < properties.size(); i++) {
				Property p = (Property) properties.get(i);
				_totalH += p.getHeight();
			}

			if (_totalH > _area.h)
				_useScrollbar = true;

			if (_drawingSurface == null || _drawingSurface.width != _area.w
					|| _drawingSurface.height != _area.h)
				_drawingSurface = createGraphics(_area.w, _area.h, JAVA2D);
			_drawingSurface.beginDraw();
			_drawingSurface.background(color(255, 255, 255, 0));
			float y = 0;
			int w = _area.w;
			if (_useScrollbar) {
				scrollbar.setPosition(new Rect(_area.x + _area.w - 15, _area.y,
						14, _area.h - 1));
				scrollbar.display(_drawingSurface, 0);
				y = min(0, -(_totalH - _area.h) * scrollbar.pos);
				w -= 15;
			}

			for (int i = 0; i < properties.size(); i++) {
				Property p = (Property) properties.get(i);
				// Rect r = p.getRect();
				p.setPosition(_area.x, _area.y + floor(y), w);
				int h = p.getHeight();
				if (y + h > 0 && y < _area.h)
					p.display(_drawingSurface, floor(y));
				y += h;
			}

			_drawingSurface.endDraw();
			image(_drawingSurface, _area.x, _area.y);

			for (int i = 0; i < frames.size(); i++)
				((MinyFrame) frames.get(i)).display();
		}

		void onMousePressed() {
			// if(mouseButton != LEFT) return;

			if (locked != null) {
				if (overRect(locked.getRect())) {
					locked.onMousePressed();
					return;
				} else {
					locked.lostFocus();
					if (focus == locked)
						focus = null;
					locked = null;
				}
			}

			for (int i = frames.size() - 1; i >= 0; i--) {
				MinyFrame f = (MinyFrame) frames.get(i);
				if (overRect(f.getRect())) {
					if (focus != f) {
						if (focus != null)
							focus.lostFocus();
						focus = f;
						f.getFocus();
						putFrameOnTop(f);
					}
					f.onMousePressed();
					return;
				}
			}

			if (focus != null) {
				if (overRect(focus.getRect())) {
					focus.onMousePressed();
					return;
				} else {
					focus.lostFocus();
					focus = null;
				}
			}

			if (!overRect(_area))
				return;

			if (_useScrollbar && overRect(scrollbar.getRect()))
				scrollbar.onMousePressed();

			for (int i = 0; i < properties.size(); i++) {
				Property p = (Property) properties.get(i);
				if (overRect(p.getRect())) {
					focus = p;
					p.getFocus();
					p.onMousePressed();
					return;
				}
			}
		}

		Rect getRect() {
			return _area;
		}

		void onKeyPressed() {
			/*
			 * if(key == CODED) { switch(keyCode) { case ALT: _modAlt = true;
			 * break; case SHIFT: _modShift = true; break; case CONTROL:
			 * _modCtrl = true; break; } }
			 */

			if (locked != null)
				locked.onKeyPressed();
			else if (focus != null)
				focus.onKeyPressed();
		}

		void onKeyReleased() {
			/*
			 * if(key == CODED) { switch(keyCode) { case ALT: _modAlt = false;
			 * break; case SHIFT: _modShift = false; break; case CONTROL:
			 * _modCtrl = false; break; } }
			 */
		}

		boolean getModShift() {
			return _modShift;
		}

		boolean getModCtrl() {
			return _modCtrl;
		}

		boolean getModAlt() {
			return _modAlt;
		}

		void addProperty(Property p) {
			properties.add(p);// add the new property to the arraylist
			if (_useScrollbar)// if there is a scroll bar then change size to
								// fit
				p.setPosition(_area.x, _area.y + _totalH, _area.w - 15);
			else
				p.setPosition(_area.x, _area.y + _totalH, _area.w);// the y will
																	// be set to
																	// end of
																	// list
			_totalH += p.getHeight();// add height to fit new property
		}

		/*
		 * void addButton(String name, ButtonCallback callback) {
		 * addProperty(new PropertyButton(this, name, callback)); }
		 * 
		 * void addDisplay(String name, MinyValue value) { addProperty(new
		 * PropertyDisplay(this, name, value)); }
		 * 
		 * void addEditBox(String name, MinyString value) { addProperty(new
		 * PropertyEditString(this, name, value)); }
		 * 
		 * void addEditBox(String name, MinyInteger value) { addProperty(new
		 * PropertyEditInteger(this, name, value)); }
		 * 
		 * void addEditBox(String name, MinyFloat value) { addProperty(new
		 * PropertyEditFloat(this, name, value)); }
		 * 
		 * void addSlider(String name, MinyInteger value, int mini, int maxi) {
		 * addProperty(new PropertySliderInteger(this, name, value, mini,
		 * maxi)); }
		 * 
		 * void addSlider(String name, MinyFloat value, float mini, float maxi)
		 * { addProperty(new PropertySliderFloat(this, name, value, mini,
		 * maxi)); }
		 */

		/*
		 * void addCheckBox(String name, MinyBoolean value) { addProperty(new
		 * PropertyCheckBox(this, name, value)); }
		 * 
		 * void addList(String name, MinyInteger value, String choices) {
		 * addProperty(new PropertyList(this, name, value, choices)); }
		 */

		/*
		 * void addColorChooser(String name, MinyColor value) { addProperty(new
		 * PropertyColorChooser(this, name, value)); }
		 * 
		 * void addGraph(String name, InterpolatedFloat value) { addProperty(new
		 * PropertyGraph(this, name, value)); }
		 * 
		 * void addGradient(String name, ColorGradient value) { addProperty(new
		 * PropertyGradient(this, name, value)); }
		 */

		void addFrame(MinyFrame frame) {
			frames.add(frame);
		}

		void removeFrame(MinyFrame frame) {
			frame.onClose();
			for (int i = 0; i < frames.size(); i++) {
				if (frames.get(i) == frame) {
					frames.remove(i);
					if (focus == frame)
						focus = null;
					if (locked == frame)
						locked = null;
					return;
				}
			}
		}

		void putFrameOnTop(MinyFrame frame) {
			for (int i = 0; i < frames.size(); i++) {
				if (frames.get(i) == frame) {
					frames.remove(i);
					break;
				}
			}
			frames.add(frame);
		}

		@Override
		public Iterator<Property> iterator() {
			return properties.iterator();
		}
	}

	interface ButtonCallback {
		void onButtonPressed();
	}

	interface MinyWidget {
		Rect getRect();

		void update();

		void getFocus();

		void lostFocus();

		void onMousePressed();

		void onKeyPressed();
	}

	class Property implements MinyWidget {
		protected MinyGUI _parent;
		String _name;
		protected int _x, _y, _w; // property position on field
		protected boolean _hasFocus;

		Property(String name) {
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
			return new Rect(_x, _y, _w, getHeight());
		}

		public MinyValue get() {
			return new MinyString("");
		}

		public void update() {
		}

		public void display(PGraphics pg, int y) {
			pg.textAlign(LEFT, CENTER);
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
	}

	class MinyFrame implements MinyWidget {
		protected Rect _box;
		protected MinyGUI _parent;
		protected int _minW, _minH;
		private boolean _moveable, _resizeable, _hasFocus, _movingFrame;
		private Rect _saved;
		private int _resizing, _clickX, _clickY;
		String _name;

		MinyFrame(MinyGUI parent, String name) {
			_parent = parent;
			_name = name;
			_moveable = _resizeable = _hasFocus = _movingFrame = false;
			_resizing = -1;
			_minW = 80;
			_minH = 60;
		}

		void onClose() {
		}

		void onResize() {
		}

		void setRect(Rect r) {
			_box = r;
		}

		public Rect getRect() {
			return _box;
		}

		void setMoveable(boolean b) {
			_moveable = b;
		}

		void setResizeable(boolean b) {
			_resizeable = b;
		}

		void setClientAreaSize(int w, int h) {
			_box.w = w + 2;
			_box.h = h + 2;
			if (_moveable)
				_box.h += 18;
			if (_resizeable) {
				_box.w += 6;
				_box.h += 6;
			}

			validatePosition();
		}

		Rect getClientArea() {
			Rect r = new Rect(_box);
			r.grow(-1);
			if (_resizeable)
				r.grow(-3);
			if (_moveable) {
				r.y += 18;
				r.h -= 18;
			}
			return r;
		}

		void placeAt(int x, int y, int w, int h) {
			w = w + 2;
			h = h + 2;
			if (_moveable)
				h += 18;
			if (_resizeable) {
				w += 6;
				h += 6;
			}

			if (x + w > width - 5)
				x = width - w - 5;
			if (y + h > height - 5)
				y = height - h - 5;

			_box = new Rect(x, y, w, h);
			validatePosition();
		}

		void validatePosition() {
			_box.w = constrain(_box.w, _minW, width - 1);
			_box.h = constrain(_box.h, _minH, height - 1);
			_box.x = max(0, _box.x);
			_box.y = max(0, _box.y);
			if (_box.x + _box.w + 1 > width)
				_box.x = width - _box.w - 1;
			if (_box.y + _box.h + 1 > height)
				_box.y = height - _box.h - 1;
		}

		public void update() {
			if (!_parent.hasLock(this)) {
				if (_resizeable) {
					Rect r = new Rect(_box);
					r.grow(-4);
					/*
					 * if(overRect(_box) && !overRect(r)) cursor(CROSS); else
					 * cursor(ARROW);
					 */
				}
				return;
			}

			if (mousePressed) {
				int oldW = _box.w, oldH = _box.h;
				if (_resizing != -1 && (pmouseX != mouseX || pmouseY != mouseY)) {
					if ((_resizing & 1) != 0) {
						_box.x = min(_saved.x + _saved.w - _minW, _saved.x
								+ mouseX - _clickX);
						_box.w = max(_minW, _saved.w - mouseX + _clickX);
					} else if ((_resizing & 2) != 0) {
						_box.x = _saved.x;
						_box.w = max(_minW, _saved.w + mouseX - _clickX);
					}
					if ((_resizing & 4) != 0) {
						_box.y = min(_saved.y + _saved.h - _minH, _saved.y
								+ mouseY - _clickY);
						_box.h = max(_minH, _saved.h - mouseY + _clickY);
					} else if ((_resizing & 8) != 0) {
						_box.y = _saved.y;
						_box.h = max(_minH, _saved.h + mouseY - _clickY);
					}
				}

				if (_movingFrame) {
					_box.x = _saved.x + mouseX - _clickX;
					_box.y = _saved.y + mouseY - _clickY;
				}

				validatePosition();
				if (_box.w != oldW || _box.h != oldH)
					onResize();
			} else if (_resizing != -1 || _movingFrame) {
				_parent.releaseLock(this);
				_resizing = -1;
				_movingFrame = false;
				// cursor(ARROW);
			}
		}

		public void display() {
			fill(_parent.bg);
			stroke(_parent.fg);
			Rect r = new Rect(_box);
			if (_resizeable && _moveable) {
				noFill();
				rect(r);
				r.grow(-1);
				stroke(_parent.bg);
				rect(r);
				r.grow(-1);
				rect(r);
				r.grow(-1);
				r.y += 18;
				r.h -= 18;
				stroke(_parent.fg);
				fill(_parent.bg);
				rect(r);
			} else if (_resizeable) {
				rect(r);
				r = new Rect(_box);
				r.grow(-3);
				noFill();
				rect(r);
			} else if (_moveable) {
				r.y += 18;
				r.h -= 18;
				rect(r);
			} else
				rect(r);

			if (_moveable) {
				r = new Rect(_box);
				if (_resizeable)
					r.grow(-3);
				r.h = 18;

				r.y++;
				r.h--;
				r.w -= r.h;
				noStroke();
				if (_hasFocus)
					fill(_parent.selectColor);
				else
					fill(_parent.bg);
				rect(r);

				r.y--;
				r.w += r.h;
				r.h++;
				noFill();
				stroke(_parent.fg);
				rect(r);

				fill(_parent.fg);
				noStroke();
				r.x += 5;
				r.w -= 10;
				textAlign(LEFT);
				text(_name, r);

				r.x = _box.x + _box.w - r.h;
				if (_resizeable)
					r.x -= 3;
				r.w = r.h;
				noFill();
				if (_hasFocus)
					stroke(_parent.selectColor);
				else
					stroke(_parent.bg);
				r.grow(-1);
				rect(r);
				r.grow(-1);
				rect(r);
				r.grow(-1);
				rect(r);

				fill(_parent.bg);
				stroke(_parent.fg);
				rect(r);
				noFill();
				r.grow(-2);
				line(r.x, r.y, r.x + r.w, r.y + r.h);
				line(r.x + r.h, r.y, r.x, r.y + r.h);
			}
		}

		public void getFocus() {
			_hasFocus = true;
		}

		public void lostFocus() {
			_hasFocus = false;
		}

		public void onMousePressed() {
			_clickX = mouseX;
			_clickY = mouseY;
			_saved = new Rect(_box);
			if (_resizeable) {
				Rect r = new Rect(_box);
				r.grow(-4);
				if (overRect(_box) && !overRect(r)) {
					_parent.getLock(this);
					// cursor(CROSS);
					_resizing = 0;
					if (mouseX - _box.x < 4)
						_resizing += 1;
					if (_box.x + _box.w - mouseX < 4)
						_resizing += 2;
					if (mouseY - _box.y < 4)
						_resizing += 4;
					if (_box.y + _box.h - mouseY < 4)
						_resizing += 8;
					return;
				}
			}

			if (_moveable) {
				Rect r = new Rect(_box);
				if (_resizeable)
					r.grow(-3);
				r.h = 18;

				Rect r2 = new Rect(r);
				r2.x = _box.x + _box.w - r2.h;
				if (_resizeable)
					r2.x -= 3;
				r2.w = r2.h;
				r2.grow(-3);
				if (overRect(r2)) {
					_parent.removeFrame(this);
					return;
				}

				if (overRect(r)) {
					// cursor(MOVE);
					_movingFrame = true;
					_parent.getLock(this);
					return;
				}
			}
		}

		public void onKeyPressed() {
		}
	}

	interface FrameCreator {
		void onCloseFrame(MinyFrame frame);
	}

	class PropertyButton extends Property {
		public ButtonCallback _callback;
		public boolean _pressed;

		PropertyButton(String name, ButtonCallback callback) {
			super(name);
			_callback = callback;
			_pressed = false;
		}

		public MinyValue get() {
			return new MinyBoolean(_pressed);
		}

		Rect getBox() {
			return new Rect((int) (_x + _w * 0.2), _y + 1, (int) (_w * 0.6), 18);
		}

		public void onMousePressed() {
			if (overRect(getBox()))
				_pressed = true;
		}

		public void update() {
			if (_pressed && !mousePressed) {
				_pressed = false;
				if (overRect(getBox()))
					_callback.onButtonPressed();
			}
		}

		public void display(PGraphics pg, int y) {
			pg.stroke(_parent.fg);
			Rect b = getBox();
			b.x -= _x;
			b.y += y - _y;
			if (_pressed && overRect(getBox()))
				pg.strokeWeight(4);
			pg.noFill();
			rect(pg, b);
			pg.strokeWeight(1);
			b.grow(-1);
			pg.textAlign(CENTER, TOP);
			// pg.fill(_parent.fg);
			text(pg, _name, b);
		}
	}

	class PropertyButtonImage extends PropertyButton {
		PImage img;

		PropertyButtonImage(String name, ButtonCallback callback) {
			super(name, callback);
			img = loadImage(name);
		}

		Rect getBox() {
			return new Rect((int) (_x + _w * 0.2), _y + 1, (int) (_w * 0.6), 18);
		}

		public void display(PGraphics pg, int y) {
			pg.stroke(_parent.fg);
			Rect b = getBox();
			pg.image(img, b.x, b.y, b.w, b.h);
			b.x -= _x;
			b.y += y - _y;
			if (_pressed && overRect(getBox()))
				pg.strokeWeight(4);
			pg.noFill();
			rect(pg, b);
			pg.strokeWeight(1);
		}
	}

	class PropertyDisplay extends Property {
		private MinyValue _value;

		PropertyDisplay(String name, MinyValue value) {
			super(name);
			_value = value;
		}

		PropertyDisplay(String name) {
			super(name);
		}

		public MinyValue get() {
			return _value;
		}

		public void display(PGraphics pg, int y) {
			super.display(pg, y); // displays the label
			if (_value != null) {
				pg.noFill();
				pg.stroke(_parent.fg);
				pg.text(_value.getString(), _w * 0.4f + 3, y, _w * 0.6f - 8, 20);
			}
		}
	}

	class PropertyEdit extends Property {
		private int cursorPos, cursorTime, selectionStart, selectionEnd;
		private boolean cursorOn, selectioning;
		private String editText;

		PropertyEdit(String name) {
			super(name);
			cursorPos = 0;
			cursorTime = 0;
			cursorOn = true;
			selectioning = false;
			selectionStart = -1;
			editText = new String();
		}

		Rect getBox() {
			return new Rect((int) (_x + _w * 0.4 + 3), _y + 1,
					(int) (_w * 0.6 - 8), 18);
		}

		int findCursorPos() {
			float tc = mouseX - (int) (_x + _w * 0.4 + 4);
			int closestPos = editText.length();
			float closestDist = _w;
			for (int i = editText.length(); i >= 0; i--) {
				float tw = textWidth(editText.substring(0, i));
				float d = abs(tc - tw);
				if (d < closestDist) {
					closestDist = d;
					closestPos = i;
				} else
					break;
			}
			return closestPos;
		}

		public void onMousePressed() {
			if (overRect(getBox())) {
				if (!_parent.hasLock(this)) {
					_parent.getLock(this);
					cursorTime = millis() + 500;
					cursorOn = true;
					editText = getValue();
				}

				cursorPos = findCursorPos();
				selectioning = true;
				selectionStart = cursorPos;
			}
		}

		public void onKeyPressed() {
			switch (key) {
			case CODED:
				switch (keyCode) {
				case LEFT:
					if (_parent.getModShift()) {
						if (selectionStart != -1) {
							if (cursorPos == selectionStart)
								selectionStart = max(selectionStart - 1, 0);
							else if (cursorPos == selectionEnd)
								selectionEnd = max(selectionEnd - 1, 0);
							cursorPos = max(cursorPos - 1, 0);
						} else if (cursorPos > 0) {
							selectionEnd = cursorPos;
							cursorPos--;
							selectionStart = cursorPos;
						}
					} else if (selectionStart != -1) {
						cursorPos = selectionStart;
						selectionStart = selectionEnd = -1;
					} else
						cursorPos = max(cursorPos - 1, 0);
					break;
				case RIGHT:
					if (_parent.getModShift()) {
						if (selectionStart != -1) {
							if (cursorPos == selectionStart)
								selectionStart = min(selectionStart + 1,
										editText.length());
							else if (cursorPos == selectionEnd)
								selectionEnd = min(selectionEnd + 1,
										editText.length());
							cursorPos = min(cursorPos + 1, editText.length());
						} else if (cursorPos < editText.length() - 1) {
							selectionStart = cursorPos;
							cursorPos++;
							selectionEnd = cursorPos;
						}
					} else if (selectionStart != -1) {
						cursorPos = selectionEnd;
						selectionStart = selectionEnd = -1;
					} else
						cursorPos = min(cursorPos + 1, editText.length());
					break;
				}
				break;
			case RETURN:
			case ENTER:
				lostFocus();
				break;
			case DELETE:
				if (!selectioning && (selectionStart != -1)) {
					editText = editText.substring(0, selectionStart)
							+ editText.substring(selectionEnd);
					cursorPos = selectionStart;
					selectionStart = selectionEnd = -1;
				} else if (cursorPos < editText.length())
					editText = editText.substring(0, cursorPos)
							+ editText.substring(cursorPos + 1);
				break;
			case BACKSPACE:
				if (!selectioning && (selectionStart != -1)) {
					editText = editText.substring(0, selectionStart)
							+ editText.substring(selectionEnd);
					cursorPos = selectionStart;
					selectionStart = selectionEnd = -1;
				} else if (cursorPos > 0) {
					editText = editText.substring(0, cursorPos - 1)
							+ editText.substring(cursorPos);
					cursorPos--;
				}
				break;
			default:
				if (_parent.getModCtrl() || _parent.getModAlt())
					break;
				if (!selectioning && (selectionStart != -1)) {
					editText = editText.substring(0, selectionStart)
							+ editText.substring(selectionEnd);
					cursorPos = selectionStart;
					selectionStart = selectionEnd = -1;
				}
				String tempText = editText.substring(0, cursorPos) + key
						+ editText.substring(cursorPos);
				if (validate(tempText)) {
					editText = tempText;
					cursorPos++;
				}
				break;
			}
		}

		boolean validate(String test) {
			return true;
		}

		void saveValue(String val) {
		}

		String getValue() {
			return "";
		}

		public void lostFocus() {
			if (validate(editText))
				saveValue(editText);
			cursorPos = 0;
			cursorOn = true;
			selectioning = false;
			// cursor(ARROW);
			_parent.releaseLock(this);
		}

		public void update() {
			if (!_parent.hasLock(this))
				return;

			if (millis() > cursorTime) {
				cursorOn = !cursorOn;
				cursorTime = millis() + 500;
			}

			if (!mousePressed) {
				selectioning = false;
				if (selectionStart != selectionEnd) {
					int start = min(selectionStart, selectionEnd);
					int end = max(selectionStart, selectionEnd);
					selectionStart = start;
					selectionEnd = end;
				} else
					selectionStart = selectionEnd = -1;
			}

			/*
			 * if(overRect(getBox())) cursor(TEXT); else cursor(ARROW);
			 */

			if (selectioning)
				cursorPos = selectionEnd = findCursorPos();
		}

		public void display(PGraphics pg, int y) {
			super.display(pg, y);

			pg.noFill();
			pg.stroke(_parent.fg);
			Rect b = getBox();
			b.y += y - _y;
			b.x -= _x;
			if (_parent.hasLock(this))
				pg.strokeWeight(2);
			rect(pg, b);
			pg.strokeWeight(1);
			b.grow(-1);
			pg.textAlign(LEFT, CENTER);
			if (_parent.hasLock(this)) {
				if (selectionStart != selectionEnd) {
					float tw1, tw2, tw;
					tw1 = textWidth(editText.substring(0, selectionStart));
					tw2 = textWidth(editText.substring(0, selectionEnd));
					tw = tw2 - tw1;

					pg.fill(_parent.selectColor);
					pg.noStroke();
					pg.rect(b.x + tw1, b.y + 1, tw, b.h - 2);
					pg.noFill();
				}

				pg.fill(_parent.fg);
				text(pg, editText, b);
				pg.stroke(_parent.fg);
				if (cursorOn) {
					float tw = textWidth(editText.substring(0, cursorPos));
					pg.line(_w * 0.4f + 4 + tw, y + 4, _w * 0.4f + 4 + tw,
							y + 17);
				}
			} else
				text(pg, getValue(), b);
		}
	}

	class PropertyEditString extends PropertyEdit {
		private MinyString _value;

		PropertyEditString(String name, MinyString value) {
			super(name);
			_value = value;
		}

		PropertyEditString(String name) {
			super(name);
			_value = new MinyString();
		}

		public MinyValue get() {
			return _value;
		}

		boolean validate(String test) {
			return textWidth(test) < getBox().w - 2;
		}

		void saveValue(String val) {
			_value.setValue(val);
		}

		String getValue() {
			return _value.getValue();
		}
	}

	class PropertyEditInteger extends PropertyEdit {
		private MinyInteger _value;

		PropertyEditInteger(String name, MinyInteger value) {
			super(name);
			_value = value;
		}

		PropertyEditInteger(String name) {
			super(name);
			_value = new MinyInteger();
		}

		public MinyValue get() {
			return _value;
		}

		boolean validate(String test) {
			if (test.length() == 0)
				return true;
			if (test.equals("-"))
				return true;
			try {
				Integer.parseInt(test);
				return true;
			} catch (NumberFormatException e) {
				return false;
			}
		}

		void saveValue(String val) {
			if (val.length() == 0)
				_value.setValue(0);
			else
				try {
					_value.setValue(Integer.parseInt(val));
				} catch (NumberFormatException e) {
				}
		}

		String getValue() {
			return _value.getValue().toString();
		}
	}

	class PropertyEditFloat extends PropertyEdit {
		private MinyFloat _value;

		PropertyEditFloat(String name, MinyFloat value) {
			super(name);
			_value = value;
		}

		PropertyEditFloat(String name) {
			super(name);
			_value = new MinyFloat();
		}

		public MinyValue get() {
			return _value;
		}

		boolean validate(String test) {
			if (test.length() == 0)
				return true;
			if (test.equals("-"))
				return true;
			try {
				if (test.substring(test.length() - 1).compareToIgnoreCase("e") == 0) {
					Float.parseFloat(test.substring(0, test.length() - 1));
					return true;
				}

				Float.parseFloat(test);
				return true;
			} catch (NumberFormatException e) {
				return false;
			}
		}

		void saveValue(String val) {
			if (val.length() == 0)
				_value.setValue(0.0f);
			else
				try {
					_value.setValue(Float.parseFloat(val));
				} catch (NumberFormatException e) {
				}
		}

		String getValue() {
			return _value.getValue().toString();
		}
	}

	class PropertySlider extends Property {
		private boolean _over;

		PropertySlider(String name) {
			super(name);
		}

		float getPos() {
			return 0.0f;
		}

		void setPos(float v) {
		}

		private Rect getBox() {
			float fpos = (_x + _w * 0.4f + 8) + (_w * 0.6f - 18) * getPos();
			return new Rect((int) fpos - 5, _y + 8, 10, 10);
		}

		public void update() {
			if (mousePressed) {
				if (_over)
					_parent.getLock(this);
			} else
				_parent.releaseLock(this);

			if (_parent.hasLock(this)) {
				float t = mouseX - (_x + _w * 0.4f + 8);
				t /= _w * 0.6 - 18;
				setPos(t);
			} else
				_over = overRect(getBox()) && !mousePressed;
		}

		public void onMousePressed() {
			if (overRect(getBox())) {
				_over = true;
				_parent.getLock(this);
			} else if (overRect((int) (_x + _w * 0.4 + 3), _y + 8, _x + _w - 5,
					_y + 15)) {
				float t = mouseX - (_x + _w * 0.4f + 8);
				t /= _w * 0.6 - 18;
				setPos(t);
			}
		}

		public void display(PGraphics pg, int y) {
			super.display(pg, y);
			// pg.fill(_parent.bg);
			// pg.noStroke();
			// pg.rect(_w*0.4f + 3, y, _w *0.6f - 8, 20);
			pg.stroke(_parent.fg);
			pg.line(_w * 0.4f + 3, y + 13, _w - 5, y + 13);
			if (_over || _parent.hasLock(this))
				pg.strokeWeight(2);
			Rect b = getBox();
			b.x -= _x;
			b.y += y - _y;
			rect(pg, b);
			pg.strokeWeight(1);
		}
	}

	class PropertySliderInteger extends PropertySlider {
		private MinyInteger _value;
		private int _mini, _maxi;

		PropertySliderInteger(String name, MinyInteger value, int mini, int maxi) {
			super(name);
			_value = value;
			_mini = mini;
			_maxi = maxi;
		}

		PropertySliderInteger(String name, int mini, int maxi) {
			super(name);
			_value = new MinyInteger(mini);
			_mini = mini;
			_maxi = maxi;
		}

		public MinyValue get() {
			return _value;
		}

		float getPos() {
			return (_value.getValue() - _mini) / (float) (_maxi - _mini);
		}

		void setPos(float v) {
			_value.setValue(round(constrain(_mini + v * (_maxi - _mini), _mini,
					_maxi)));
		}

		public void update() {
			super.update();
			_value.setValue(constrain(_value.getValue(), _mini, _maxi));
		}
	}

	class PropertySliderFloat extends PropertySlider {
		private MinyFloat _value;
		private float _mini, _maxi;

		PropertySliderFloat(String name, MinyFloat value, float mini, float maxi) {
			super(name);
			_value = value;
			_mini = mini;
			_maxi = maxi;
		}

		PropertySliderFloat(String name, float mini, float maxi) {
			super(name);
			_value = new MinyFloat(mini);
			_mini = mini;
			_maxi = maxi;
		}

		public MinyValue get() {
			return _value;
		}

		float getPos() {
			return (_value.getValue() - _mini) / (_maxi - _mini);
		}

		void setPos(float v) {
			_value.setValue(constrain(_mini + v * (_maxi - _mini), _mini, _maxi));
		}

		public void update() {
			super.update();
			_value.setValue(constrain(_value.getValue(), _mini, _maxi));
		}
	}

	// ///////////////////////////////////////////////////////////////////////////////
	// BWB032513///////////////////////////////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////////////////////
	class PropertyPBar extends Property {
		private MinyInteger _value;
		private int maxBar;
		private int steps;
		private float ratio;
		private int stepVal;
		
		PropertyPBar(String name,int maxBar,int steps) {
			super(name);
			this.maxBar=maxBar;
			this.steps=steps;
		}

		float getPos() {
			return 0.0f;
		}

		void setPos(float v) {
		}

		private Rect getBox(int xoff) {
			float fpos = (_x + _w * 0.4f + 8) + (_w * 0.6f - 18) * 0;
			return new Rect((int) fpos - 5, _y + 8, 10 + xoff, 10);
		}

		public void display(PGraphics pg, int y) {
			super.display(pg, y);
			pg.fill(_parent.bg);
			pg.rect(_w * 0.4f + 3, y + 8, _w * 0.6f - 8, 10);
			pg.fill(0, 0, 255);
			pg.stroke(_parent.fg);
			ratio=_value.getValue()/(float)steps;
			int c=(int)(ratio*maxBar);
			for(int i=0;i<c;i++){
				Rect b = getBox((int)c);
				b.x -= _x;
				b.y += y - _y;
				rect(pg, b);
			}
			pg.stroke(_parent.bg);
			for(int j=0;j<maxBar;j++){
				pg.line((_w * 0.4f + 3)+stepVal*j, y+9f,(_w * 0.4f +3f)+stepVal*j, y+17);
			}
			pg.strokeWeight(1);
		}
	}

	class PropertyProgBar extends PropertyPBar {
		private int _mini = 0;
		private int _maxi;

		PropertyProgBar(String name, MinyInteger value, int maxi,int steps) {
			super(name,maxi,steps);
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

	// //////////////////////////////////////////////////////////////////////////////////
	// END_BWB///////////////////////////////////////////////////////////////////////////
	// //////////////////////////////////////////////////////////////////////////////////
	class PropertyCheckBox extends Property {
		private MinyBoolean _value;

		PropertyCheckBox(String name, MinyBoolean value) {
			super(name);
			_value = value;
		}

		PropertyCheckBox(String name) {
			super(name);
			_value = new MinyBoolean();
		}

		public MinyValue get() {
			return _value;
		}

		Rect getBox() {
			return new Rect((int) (_x + _w * 0.4 + 3), _y + 8, 10, 10);
		}

		public void display(PGraphics pg, int y) {
			super.display(pg, y);

			pg.stroke(_parent.fg);
			pg.noFill();
			Rect b = getBox();
			b.x -= _x;
			b.y += y - _y;
			rect(pg, b);

			if (_value.getValue()) {
				pg.fill(_parent.fg);
				b.grow(-2);
				rect(pg, b);
			}
		}

		public void onMousePressed() {
			if (overRect(getBox()))
				_value.setValue(!_value.getValue());
		}
	}

	class VScrollbar implements MinyWidget {
		private MinyGUI _parent;
		private float pos;
		private Rect _area;
		private boolean _over;

		VScrollbar(MinyGUI parent, Rect area) {
			_parent = parent;
			_area = area;
			pos = 0;
			_over = false;
		}

		public Rect getRect() {
			return _area;
		}

		void setPosition(Rect r) {
			_area = r;
		}

		Rect getBox() {
			return new Rect(_area.x + 2, (int) (_area.y + 2 + pos
					* (_area.h - 24)), _area.w - 4, 20);
		}

		public void onMousePressed() {
			if (overRect(getBox())) {
				_over = true;
				_parent.getLock(this);
			} else {
				float t = mouseY - (_area.y + 12);
				t /= _area.h - 24;
				pos = constrain(t, 0, 1);
			}
		}

		public void update() {
			if (mousePressed) {
				if (_over)
					_parent.getLock(this);
			} else
				_parent.releaseLock(this);

			if (_parent.hasLock(this)) {
				float t = mouseY - (_area.y + 12);
				t /= _area.h - 24;
				pos = constrain(t, 0, 1);
			} else
				_over = overRect(getBox()) && !mousePressed;
		}

		void display(PGraphics pg, int y) {
			pg.noFill();
			pg.stroke(_parent.fg);
			Rect r = new Rect(_area);
			Rect pr = _parent.getRect();
			r.x -= pr.x;
			r.y -= pr.y;
			rect(pg, r);

			pg.fill(_parent.bg);
			Rect b = getBox();
			b.x -= pr.x;
			b.y -= _area.y;
			if (_over || _parent.hasLock(this)) {
				b.x++;
				b.w--;
				b.y++;
				b.h--;
				pg.strokeWeight(2);
				rect(pg, b);
				pg.strokeWeight(1);
			} else
				rect(pg, b);
		}

		public void getFocus() {
		}

		public void lostFocus() {
			_over = false;
		}

		public void onKeyPressed() {
		}
	}

	class PropertyList extends Property {
		private MinyInteger _value;
		private String[] _choices;

		PropertyList(String name, MinyInteger value, String choices) {
			super(name);
			_value = value;
			_choices = split(choices, ';');
		}

		PropertyList(String name, String choices) {
			super(name);
			_value = new MinyInteger();
			_choices = split(choices, ';');
		}

		Rect getBox() {
			return new Rect((int) (_x + _w * 0.4 + 3), _y + 2,
					(int) (_w * 0.6 - 8), 18);
		}

		public MinyValue get() {
			return _value;
		}

		public void onMousePressed() {
			if (overRect(getBox())) {
				Rect p = _parent.getRect();
				Rect b = getBox();
				boolean below = true;
				if (b.y + b.h + (b.h - 1) * _choices.length > p.y + p.h)
					below = false;

				Rect r = new Rect(b.x, b.y
						+ (below ? b.h : -(b.h - 2) * _choices.length - 2),
						b.w, 2 + (b.h - 2) * (_choices.length));
				PropertyListFrame frame = new PropertyListFrame(_parent, _name,
						_value, _choices);
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
			rect(pg, b);
			b.grow(-1);
			pg.fill(_parent.fg);
			pg.textAlign(LEFT, CENTER);
			text(pg, _choices[(int) _value.getValue()], b);

			b = getBox();
			b.x -= _x;
			b.y += y - _y;
			b.x += b.w - 14;
			b.w = 14;
			pg.noFill();
			rect(pg, b);
			pg.line(b.x + 3, b.y + 3, b.x + b.w / 2, b.y + b.h - 4);
			pg.line(b.x + b.w / 2, b.y + b.h - 4, b.x + b.w - 3, b.y + 3);
		}
	}

	class PropertyListFrame extends MinyFrame {
		private MinyInteger _value;
		private String[] _choices;
		private boolean _moving, _over;
		private int _selected;

		PropertyListFrame(MinyGUI parent, String name, MinyInteger value,
				String[] choices) {
			super(parent, name);
			_value = value;
			_choices = choices;
			_selected = value.getValue();
			_moving = true;
			_over = false;
			_parent.getLock(this);
		}

		public void lostFocus() {
			_parent.removeFrame(this);
		}

		public void onMousePressed() {
			_parent.removeFrame(this);
			_parent.releaseLock(this);

			Rect b = getClientArea();
			b.h /= _choices.length;
			for (int i = 0; i < _choices.length; i++) {
				if (overRect(b))
					_value.setValue(i);
				b.y += b.h;
			}
		}

		public void update() {
			_value.setValue(constrain(_value.getValue(), 0, _choices.length));

			if (_moving) {
				if (!mousePressed) {
					if (_over) {
						_value.setValue(_selected);
						_parent.releaseLock(this);
						_parent.removeFrame(this);
						return;
					}
					_moving = false;
				}

				_over = false;
				Rect b = getClientArea();
				b.h /= _choices.length;
				for (int i = 0; i < _choices.length; i++) {
					if (overRect(b)) {
						_selected = i;
						_over = true;
						break;
					}
					b.y += b.h;
				}

				if (!_over)
					_selected = _value.getValue();
			}
		}

		public void display() {
			super.display();

			Rect b = getClientArea();
			b.h /= _choices.length;
			noStroke();
			fill(_parent.selectColor);
			Rect bs = new Rect(b);
			bs.y += bs.h * _selected;
			bs.w++;
			bs.h++;
			rect(bs);
			fill(_parent.fg);
			textAlign(LEFT, CENTER);
			for (int i = 0; i < _choices.length; i++) {
				text(_choices[i], b);
				b.y += b.h;
			}
		}
	}

	class PropertyColorChooser extends Property implements FrameCreator {
		private MinyColor _value;
		private PImage _imgChecker;
		private ColorChooserFrame _frame;

		public PropertyColorChooser(String name, MinyColor value) {
			super(name);
			_value = value;
			int h = getHeight() - 3;
			_imgChecker = createImage(h, h, RGB);
			_imgChecker.loadPixels();
			for (int x = 0; x < h; x++) {
				for (int y = 0; y < h; y++) {
					boolean b = (x % h < h / 2);
					if (y % h < h / 2)
						b = !b;
					_imgChecker.pixels[y * h + x] = b ? color(160) : color(95);
				}
			}
			_imgChecker.updatePixels();
		}

		public PropertyColorChooser(String name) {
			this(name, new MinyColor());
		}

		public MinyValue get() {
			return _value;
		}

		public Rect getBox() {
			return new Rect((int) (_x + _w * 0.4 + 3), _y + 1,
					(int) (_w * 0.6 - 8), 18);
		}

		public void onMousePressed() {
			if (overRect(getBox())) {
				if (_frame == null) {
					_frame = new ColorChooserFrame(_parent, _name, _value, this);
					_frame.placeAt(mouseX, mouseY);
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
			rect(pg, r);

			// Bug if calling fill(color) if alpha = 0
			// fill(_value.getValue());
			pg.fill(_value.getRed(), _value.getGreen(), _value.getBlue(),
					_value.getAlpha());
			pg.noStroke();
			r.x++;
			r.y++;
			r.w--;
			r.h--;
			rect(pg, r);
		}

		public void onCloseFrame(MinyFrame frame) {
			_frame = null;
		}
	}

	class ColorChooserFrame extends MinyFrame {
		private MinyColor _value;
		private FrameCreator _property;
		private PImage _imgChecker, _imgA, _imgR, _imgG, _imgB;
		private int _editing;
		private PGraphics _pg;

		ColorChooserFrame(MinyGUI parent, String name, MinyColor value,
				FrameCreator property) {
			super(parent, name);
			_value = value;
			_property = property;
			_editing = -1;
			setMoveable(true);
			_imgChecker = createImage(256, 20, RGB);
			_imgA = createImage(256, 20, ARGB);
			_imgR = createImage(256, 20, RGB);
			_imgG = createImage(256, 20, RGB);
			_imgB = createImage(256, 20, RGB);
			_pg = createGraphics(256, 97, JAVA2D);

			_imgChecker.loadPixels();
			for (int x = 0; x < 256; x++) {
				for (int y = 0; y < 20; y++) {
					boolean b = (x % 20 < 10);
					if (y % 20 < 10)
						b = !b;
					_imgChecker.pixels[y * 256 + x] = b ? color(160)
							: color(95);
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
			image(_imgR, x, y);
			y += 25;
			image(_imgG, x, y);
			y += 25;
			image(_imgB, x, y);
			y += 25;
			image(_imgChecker, x, y);
			image(_imgA, x, y);

			// line for each color value
			_pg.beginDraw();
			_pg.background(color(255, 255, 255, 0));
			_pg.fill(-1, 0);
			_pg.noStroke();
			_pg.rect(0, 0, 256, 97);
			_pg.fill(color(0));
			_pg.stroke(color(255));
			_pg.triangle(r, 4, r - 4, 0, r + 4, 0);
			_pg.triangle(r, 17, r - 4, 21, r + 4, 21);
			_pg.triangle(g, 29, g - 4, 25, g + 4, 25);
			_pg.triangle(g, 42, g - 4, 46, g + 4, 46);
			_pg.triangle(b, 54, b - 4, 50, b + 4, 50);
			_pg.triangle(b, 67, b - 4, 71, b + 4, 71);
			_pg.triangle(a, 79, a - 4, 75, a + 4, 75);
			_pg.triangle(a, 92, a - 4, 96, a + 4, 96);
			_pg.endDraw();
			image(_pg, area.x + 20, area.y + 4);

			// rectangles around each int area
			area.x += 19;
			area.w = 257;
			area.y += 4;
			area.h = 21;
			noFill();
			stroke(_parent.fg);
			rect(area);
			area.y += 25;
			rect(area);
			area.y += 25;
			rect(area);
			area.y += 25;
			rect(area);

			// text
			area = getClientArea();
			area.x += 5;
			area.y += 5;
			area.w = 20;
			area.h = 20;
			noStroke();
			fill(_parent.fg);
			text("R", area);
			area.y += 25;
			text("G", area);
			area.y += 25;
			text("B", area);
			area.y += 25;
			text("A", area);
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
			// a = (c >> 24) & 0xFF; r = (c >> 16) & 0xFF; g = (c >> 8) & 0xFF;
			// b = c & 0xFF;

			Rect area = getClientArea();
			area.x += 20;
			area.y += 5;
			area.w = 256;
			area.h = 20;
			int v = constrain(mouseX - area.x, 0, 255);
			if (overRect(area)) {
				_value.setRed(v);
				computeColorBoxes();
				_editing = 0;
				_parent.getLock(this);
				return;
			}

			area.y += 25;
			if (overRect(area)) {
				_value.setGreen(v);
				computeColorBoxes();
				_editing = 1;
				_parent.getLock(this);
				return;
			}

			area.y += 25;
			if (overRect(area)) {
				_value.setBlue(v);
				computeColorBoxes();
				_editing = 2;
				_parent.getLock(this);
				return;
			}

			area.y += 25;
			if (overRect(area)) {
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

			if (mousePressed) {
				// int r,g,b,a;
				int x, v;
				// int c = _value.getValue();
				// a = (c >> 24) & 0xFF; r = (c >> 16) & 0xFF; g = (c >> 8) &
				// 0xFF; b = c & 0xFF;
				x = mouseX - getClientArea().x - 20;
				v = constrain(x, 0, 255);

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

	// ---------------------------------------------------
	interface MinyValue {
		String getString();
	}

	class MinyInteger implements MinyValue {
		private Integer _v;

		MinyInteger() {
			_v = new Integer(0);
		}

		MinyInteger(Integer v) {
			_v = v;
		}

		Integer getValue() {
			return _v;
		}

		void setValue(Integer v) {
			_v = v;
		}

		public String getString() {
			return _v.toString();
		}
	}

	class MinyFloat implements MinyValue {
		private Float _v;

		MinyFloat() {
			_v = new Float(0);
		}

		MinyFloat(Float v) {
			_v = v;
		}

		Float getValue() {
			return _v;
		}

		void setValue(Float v) {
			_v = v;
		}

		public String getString() {
			return _v.toString();
		}
	}

	class MinyBoolean implements MinyValue {
		private Boolean _v;

		MinyBoolean() {
			_v = new Boolean(false);
		}

		MinyBoolean(Boolean v) {
			_v = v;
		}

		Boolean getValue() {
			return _v;
		}

		void setValue(Boolean v) {
			_v = v;
		}

		public String getString() {
			return _v.toString();
		}
	}

	class MinyString implements MinyValue {
		private String _v;

		MinyString() {
			_v = new String("");
		}

		MinyString(String v) {
			_v = v;
		}

		String getValue() {
			return _v;
		}

		void setValue(String v) {
			_v = v;
		}

		public String getString() {
			return _v;
		}
	}

	class MinyColor implements MinyValue {
		private int _v;

		MinyColor() {
			_v = -1;
		}

		MinyColor(int v) {
			_v = v;
		}

		int getValue() {
			return _v;
		}

		void setValue(int v) {
			_v = v;
		}

		public String getString() {
			return "0x" + hex(_v);
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

	// ---------------------------------------------------
	class Rect {
		int x, y, w, h;

		Rect(int nx, int ny, int nw, int nh) {// 0,0,200,400
			x = nx;
			y = ny;
			w = nw;
			h = nh;
		}

		Rect(Rect r) {
			x = r.x;
			y = r.y;
			w = r.w;
			h = r.h;
		}

		void grow(int v) {
			x -= v;
			y -= v;
			w += 2 * v;
			h += 2 * v;
		}
	}

	boolean overRect(int x, int y, int width, int height) { //
		if (mouseX >= x && mouseX <= x + width && mouseY >= y
				&& mouseY <= y + height)
			return true;
		else
			return false;
	}

	boolean overRect(Rect r) {
		return overRect(r.x, r.y, r.w, r.h);
	}

	void rect(Rect r) {
		rect(r.x, r.y, r.w, r.h);
	}

	void rect(PGraphics pg, Rect r) {
		pg.rect(r.x, r.y, r.w, r.h);
	}

	void text(String t, Rect r) {
		text(t, r.x, r.y, r.w, r.h);
	}

	void text(PGraphics pg, String t, Rect r) {
		pg.text(t, r.x, r.y, r.w, r.h);
	}

	public int sketchWidth() {
		return 480;
	}

	public int sketchHeight() {
		return 800;
	}
	// public String sketchRenderer() { return OPENGL; }
}
