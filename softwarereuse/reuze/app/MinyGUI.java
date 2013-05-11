package reuze.app;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;

import com.software.reuze.ff_XMLWriter;

import processing.core.PGraphics;
import reuze.app.appGUI.MinyWidget;

//---------------------------------------------------
public class MinyGUI implements Iterable<Property> {
	/**
	 * 
	 */
	private final appGUI appGUI;
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

	MinyGUI(appGUI appGUI, Rect r) {
		this.appGUI = appGUI;
		_area = r;

		_totalH = 0;

		locked = null;
		focus = null;
		bg = this.appGUI.color(128);
		fg = this.appGUI.color(0);
		selectColor = this.appGUI.color(96);

		drawBackground = true; // TODO why does this exist? the image overwrites

		properties = new ArrayList<Property>();
		frames = new ArrayList<MinyFrame>();

		_modAlt = _modShift = _modCtrl = _useScrollbar = false;
		scrollbar = new VScrollbar(this.appGUI, this, new Rect(this.appGUI,
				_area.x + _area.w - 15, _area.y, 14, _area.h - 1));
	}

	MinyGUI(appGUI appGUI, int x, int y, int w, int h) {
		this(appGUI, new Rect(appGUI, x, y, w, h));
	}

	void setPosition(Rect r) {
		_area = r;
	}

	void setPosition(int x, int y, int w, int h) {
		_area = new Rect(this.appGUI, x, y, w, h);
	}

	private void update() {
		if (_useScrollbar)
			scrollbar.update();

		if (locked != null)
			locked.update();

		if (locked == null) {
			if (this.appGUI.overRect(_area)) {
				for (int i = 0; i < properties.size(); i++)
					((Property) properties.get(i)).update();
			}

			for (int i = 0; i < frames.size(); i++)
				((MinyFrame) frames.get(i)).update();
		}
	}

	void getLock(MinyWidget p) {
		if (locked == null)
			locked = p;
	}

	boolean hasLock(MinyWidget p) {
		return (locked == p);
	}

	boolean isLocked() {
		return locked != null;
	}

	void releaseLock(MinyWidget p) {
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

		this.appGUI.noTint();
		this.appGUI.imageMode(appGUI.CORNERS);
		this.appGUI.strokeWeight(1);

		if (drawBackground) {
			this.appGUI.noStroke();
			this.appGUI.fill(bg);
			this.appGUI.rect(_area);
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
			_drawingSurface = this.appGUI.createGraphics(_area.w, _area.h,
					appGUI.JAVA2D);
		_drawingSurface.beginDraw();
		_drawingSurface.background(this.appGUI.color(255, 255, 255, 0));
		float y = 0;
		int w = _area.w;
		if (_useScrollbar) {
			scrollbar.setPosition(new Rect(this.appGUI, _area.x + _area.w - 15,
					_area.y, 14, _area.h - 1));
			scrollbar.display(_drawingSurface, 0);
			y = appGUI.min(0, -(_totalH - _area.h) * scrollbar.pos);
			w -= 15;
		}

		for (int i = 0; i < properties.size(); i++) {
			Property p = (Property) properties.get(i);
			// Rect r = p.getRect();
			p.setPosition(_area.x, _area.y + appGUI.floor(y), w);
			int h = p.getHeight();
			if (y + h > 0 && y < _area.h)
				p.display(_drawingSurface, appGUI.floor(y));
			y += h;
		}

		_drawingSurface.endDraw();
		this.appGUI.image(_drawingSurface, _area.x, _area.y);

		for (int i = 0; i < frames.size(); i++)
			((MinyFrame) frames.get(i)).display();
	}

	void onMousePressed() {
		// if(mouseButton != LEFT) return;

		if (locked != null) {
			if (this.appGUI.overRect(locked.getRect())) {
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
			if (this.appGUI.overRect(f.getRect())) {
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
			if (this.appGUI.overRect(focus.getRect())) {
				focus.onMousePressed();
				return;
			} else {
				focus.lostFocus();
				focus = null;
			}
		}

		if (!this.appGUI.overRect(_area))
			return;

		if (_useScrollbar && this.appGUI.overRect(scrollbar.getRect()))
			scrollbar.onMousePressed();

		for (int i = 0; i < properties.size(); i++) {
			Property p = (Property) properties.get(i);
			if (this.appGUI.overRect(p.getRect())) {
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
		 * if(key == CODED) { switch(keyCode) { case ALT: _modAlt = true; break;
		 * case SHIFT: _modShift = true; break; case CONTROL: _modCtrl = true;
		 * break; } }
		 */

		if (locked != null)
			locked.onKeyPressed();
		else if (focus != null)
			focus.onKeyPressed();
	}

	void onKeyReleased() {
		/*
		 * if(key == CODED) { switch(keyCode) { case ALT: _modAlt = false;
		 * break; case SHIFT: _modShift = false; break; case CONTROL: _modCtrl =
		 * false; break; } }
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
		properties.add(p);
		if (_useScrollbar)
			p.setPosition(_area.x, _area.y + _totalH, _area.w - 15);
		else
			p.setPosition(_area.x, _area.y + _totalH, _area.w);
		_totalH += p.getHeight();
	}

	/*
	 * void addButton(String name, ButtonCallback callback) { addProperty(new
	 * PropertyButton(this, name, callback)); }
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
	 * addProperty(new PropertySliderInteger(this, name, value, mini, maxi)); }
	 * 
	 * void addSlider(String name, MinyFloat value, float mini, float maxi) {
	 * addProperty(new PropertySliderFloat(this, name, value, mini, maxi)); }
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

	public void writeXML() {
		StringWriter writer = new StringWriter();
		ff_XMLWriter xml = new ff_XMLWriter(writer);
		try {
			xml.element("MinyGUI").element("Rect");
			xml = _area.writeXML(xml);
			xml.pop().element("properties")
					.attribute("elements", properties.size());
			for (int i = 0; i < properties.size(); i++) {
				xml = properties.get(i).writeXML(xml, i);
			}
			xml.pop().element("fg", fg).element("bg", bg)
					.element("selectColor", selectColor).pop();

			System.out.println(writer);
		} catch (IOException e) {
			System.out.println("there was an error");
			e.printStackTrace();
		}

	}
}