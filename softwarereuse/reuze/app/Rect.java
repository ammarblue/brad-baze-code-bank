package reuze.app;

import java.io.IOException;
import java.io.StringWriter;

import com.software.reuze.ff_XMLWriter;

//---------------------------------------------------
public class Rect {
	/**
	 * 
	 */
	private final appGUI appGUI;
	int x, y, w, h;

	Rect(appGUI appGUI, int nx, int ny, int nw, int nh) {
		this.appGUI = appGUI;
		x = nx;
		y = ny;
		w = nw;
		h = nh;
	}

	Rect(appGUI appGUI, Rect r) {
		this.appGUI = appGUI;
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

	public ff_XMLWriter writeXML(ff_XMLWriter xml) {

		try {
			xml.element("x", x).element("y", y).element("w", w).element("h", h);
		} catch (IOException e) {
			System.out.println("Error: Rect.writeXML()");
		}
		return xml;

	}
}