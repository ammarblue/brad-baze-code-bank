package com.XML;


import java.awt.geom.Point2D;

public class z_Point2D extends Point2D.Float {
	public z_Point2D(z_Point2D x) {
		super((float)x.getX(),(float)x.getY());
	}

	public z_Point2D(double x, double y) {
		super((float)x, (float)y);
	}
	public z_Point2D(float x, float y) {
		super(x,y);
	}

	public z_Point2D() {
		super();
	}
}
