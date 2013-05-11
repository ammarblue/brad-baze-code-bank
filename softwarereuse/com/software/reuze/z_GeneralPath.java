package com.software.reuze;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
public class z_GeneralPath {
	public GeneralPath path;
	public z_GeneralPath() {
		path=new GeneralPath();
	}
	public void moveTo(float fx, float fy) {
		path.moveTo(fx, fy);
	}
	public void lineTo(float fx, float fy) {
		path.lineTo(fx, fy);
	}
	public void curveTo(float fx, float fy, float x2, float y2, float x, float y) {
		path.curveTo(fx, fy, x2, y2, x, y);
	}
	public void quadTo(float fx, float fy, float x, float y) {
		path.quadTo(fx, fy, x, y);
	}
	public void closePath() {
		path.closePath();
	}
	public void append(z_Arc2DFloat arc, boolean b) {
		path.append(arc, b);
	}
	z_Point2D getCurrentPoint(Object mustBeNull) {
		Point2D x=path.getCurrentPoint();
		return new z_Point2D(x.getX(),x.getY());
	}
}
