package reuze.app;

import java.io.IOException;
import java.io.StringWriter;

import com.software.reuze.ff_XMLWriter;

import processing.core.PGraphics;
import reuze.app.appGUI.FrameCreator;

public class PropertyGraph extends Property implements appGUI.FrameCreator {
	/**
	 * 
	 */
	private final appGUI appGUI;
	private InterpolatedFloat _value;
	private PGraphics _graph;
	private GraphFrame _frame;

	PropertyGraph(appGUI appGUI, String name, InterpolatedFloat value) {
		super(appGUI, name);
		this.appGUI = appGUI;
		_value = value;
	}

	void updateGraph() {
		Rect r = getBox();
		r.x++;
		r.w--;
		r.y++;
		r.h--;
		if (_graph == null || _graph.width != r.w || _graph.height != r.h)
			_graph = this.appGUI.createGraphics(r.w, r.h, appGUI.JAVA2D);
		_graph.beginDraw();
		_graph.background(this.appGUI.color(255, 255, 255, 0));
		_graph.fill(-1);
		_graph.rect(0, 0, r.w, r.h);
		_graph.stroke(_parent.fg);
		float[] v = _value.getTable(r.w);
		float YMin = _value.getYMin(), YMax = _value.getYMax();
		float lastY = appGUI.map(v[0], YMin, YMax, r.h - 1, 0);
		for (int x = 1; x < r.w; x++) {
			float y = appGUI.map(v[x], YMin, YMax, r.h - 1, 0);
			_graph.line(x - 1, lastY, x, y);
			lastY = y;
		}
		_graph.endDraw();
	}

	public int getHeight() {
		return 40;
	}

	Rect getBox() {
		return new Rect(this.appGUI, (int) (_x + _w * 0.4 + 3), _y + 1,
				(int) (_w * 0.6 - 8), getHeight() - 2);
	}

	public void onMousePressed() {
		if (this.appGUI.overRect(getBox())) {
			if (_frame == null) {
				_frame = new GraphFrame(this.appGUI, _parent, _name, _value,
						this);
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
		if (_graph == null || r.w - 1 != _graph.width
				|| _parent.fg != _graph.strokeColor)
			updateGraph();

		pg.noFill();
		pg.stroke(_parent.fg);
		this.appGUI.rect(pg, r);
		pg.image(_graph, r.x + 1, r.y + 1);
	}

	public void onCloseFrame(MinyFrame frame) {
		_frame = null;
		updateGraph();
	}

	public ff_XMLWriter writeXML(ff_XMLWriter xml, int i) {

		try {
			xml.element(String.valueOf(i)).attribute("type", "PropertyGraph")
					.element("name", _name).pop();
		} catch (IOException e) {
			System.out.println("Error: PropertyGraph.writeXML()");
			e.printStackTrace();
		}

		return xml;
	}
}