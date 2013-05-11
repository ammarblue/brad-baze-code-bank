package com.software.reuze;

/**
 * Simplified version of <code>java.awt.geom.Point2D</code>. We do not want
 * dependencies to presentation layer packages here.
 * 
 * @author R. Lunde
 * @author Mike Stampone
 */
public class ga_Point {
	public double x;
	public double y;
	public ga_Point() {
		
	}
	public ga_Point(double x, double y) {
		this.x = x;
		this.y = y;
	}
	public void set( float a, float b ) { x = a; y = b; }
	/**
	 * Returns the X coordinate of this <code>Point2D</code> in
	 * <code>double</code> precision.
	 * 
	 * @return the X coordinate of this <code>Point2D</code>.
	 */
	public double getX() {
		return x;
	}

	/**
	 * Returns the Y coordinate of this <code>Point2D</code> in
	 * <code>double</code> precision.
	 * 
	 * @return the Y coordinate of this <code>Point2D</code>.
	 */
	public double getY() {
		return y;
	}

	/**
	 * Returns the Euclidean distance between a specified point and this point.
	 * 
	 * @return the Euclidean distance between a specified point and this point.
	 */
	public double distance(ga_Point pt) {
		double result = (pt.getX() - x) * (pt.getX() - x);
		result += (pt.getY() - y) * (pt.getY() - y);
		return Math.sqrt(result);
	}
}
