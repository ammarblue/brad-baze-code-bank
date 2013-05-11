package com.software.reuze;

//package org.newdawn.slick.geom;

import java.util.ArrayList;

/**
 * A set of utilities to play with geometry
 * 
 * @author kevin
 */
public class ga_ShapeOps {
	/** The tolerance for determining changes and steps */
	public float EPSILON = 0.0001f;
	/** The tolerance for determining direction change */
	public float EDGE_SCALE = 1f;
	/** The maximum number of points returned by an operation - prevents full lockups */
	public int MAX_POINTS = 10000;
	/** The listener to notify of operations */
	public ga_i_ShapeListener listener;
	
	/**
	 * Subtract one shape from another - note this is experimental and doesn't
	 * currently handle islands
	 *  
	 * @param target The target to be subtracted from
	 * @param missing The shape to subtract 
	 * @return The newly created shapes
	 */
	public ga_a_Shape[] subtract(ga_a_Shape target, ga_a_Shape missing) {	
		target = target.transform(new ga_ShapeTransform());
		missing = missing.transform(new ga_ShapeTransform());

		int count = 0;
		for (int i=0;i<target.getPointCount();i++) {
			if (missing.contains(target.getPoint(i)[0], target.getPoint(i)[1])) {
				count++;
			}
		}
		
		if (count == target.getPointCount()) {
			return new ga_a_Shape[0];
		}
		
		if (!target.intersects(missing)) {
			return new ga_a_Shape[] {target};
		}
		
		int found = 0;
		for (int i=0;i<missing.getPointCount();i++) {
			if (target.contains(missing.getPoint(i)[0], missing.getPoint(i)[1])) {
				if (!onPath(target, missing.getPoint(i)[0], missing.getPoint(i)[1])) {
					found++;
				}
			}
		}
		for (int i=0;i<target.getPointCount();i++) {
			if (missing.contains(target.getPoint(i)[0], target.getPoint(i)[1])) {
				if (!onPath(missing, target.getPoint(i)[0], target.getPoint(i)[1])) 
				{
					found++;
				}
			}
		}
		
		if (found < 1) {
			return new ga_a_Shape[] {target};
		}
		
		return combine(target, missing, true);
	}

	/**
	 * Check if the given point is on the path
	 * 
	 * @param path The path to check
	 * @param x The x coordinate of the point to check
	 * @param y The y coordinate of the point to check
	 * @return True if the point is on the path
	 */
	private boolean onPath(ga_a_Shape path, float x, float y) {
		for (int i=0;i<path.getPointCount()+1;i++) {
			int n = rationalPoint(path, i+1);
			ga_ShapeLine line = getLine(path, rationalPoint(path, i), n);
			if (line.distance(new ga_Vector2(x,y)) < EPSILON*100) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Set the listener to be notified of geometry based operations
	 * 
	 * @param listener The listener to be notified of geometry based operations
	 */
	public void setListener(ga_i_ShapeListener listener) {
		this.listener = listener;
	}
	
	/**
	 * Join to shapes together. Note that the shapes must be touching
	 * for this method to work.
	 * 
	 * @param target The target shape to union with
	 * @param other The additional shape to union
	 * @return The newly created shapes 
	 */
	public ga_a_Shape[] union(ga_a_Shape target, ga_a_Shape other) {
		target = target.transform(new ga_ShapeTransform());
		other = other.transform(new ga_ShapeTransform());
		
		if (!target.intersects(other)) {
			return new ga_a_Shape[] {target, other};
		}
		
		// handle the case where intersects is true but really we're talking
		// about edge points
		boolean touches = false;
		int buttCount = 0;
		for (int i=0;i<target.getPointCount();i++) {
			if (other.contains(target.getPoint(i)[0], target.getPoint(i)[1])) {
				if (!other.hasVertex(target.getPoint(i)[0], target.getPoint(i)[1])) {
					touches = true;
					break;
				}
			}
			if (other.hasVertex(target.getPoint(i)[0], target.getPoint(i)[1])) {
				buttCount++;
			} 
		}
		for (int i=0;i<other.getPointCount();i++) {
			if (target.contains(other.getPoint(i)[0], other.getPoint(i)[1])) {
				if (!target.hasVertex(other.getPoint(i)[0], other.getPoint(i)[1])) {
					touches = true;
					break;
				}
			}
		}
		
		if ((!touches) && (buttCount < 2)) {
			return new ga_a_Shape[] {target, other};
		}
		
		// so they are definitely touching, consider the union
		return combine(target, other, false);
	}
	
	/**
	 * Perform the combination
	 * 
	 * @param target The target shape we're updating
	 * @param other The other shape in the operation
	 * @param subtract True if it's a subtract operation, otherwise it's union
	 * @return The set of shapes produced
	 */
	private ga_a_Shape[] combine(ga_a_Shape target, ga_a_Shape other, boolean subtract) {
		if (subtract) {
			ArrayList shapes = new ArrayList();
			ArrayList used = new ArrayList();
			
			// remove any points that are contianed in the shape we're removing, these
			// are implicitly used
			for (int i=0;i<target.getPointCount();i++) {
				float[] point = target.getPoint(i);
				if (other.contains(point[0], point[1])) {
					used.add(new ga_Vector2(point[0], point[1]));
					if (listener != null) {
						listener.pointExcluded(point[0], point[1]);
					}
				}
			}

			for (int i=0;i<target.getPointCount();i++) {
				float[] point = target.getPoint(i);
				ga_Vector2 pt = new ga_Vector2(point[0], point[1]);
				
				if (!used.contains(pt)) {
					ga_a_Shape result = combineSingle(target, other, true, i);
					shapes.add(result);
					for (int j=0;j<result.getPointCount();j++) {
						float[] kpoint = result.getPoint(j);
						ga_Vector2 kpt = new ga_Vector2(kpoint[0], kpoint[1]);
						used.add(kpt);
					}
				}
			}
			
			return (ga_a_Shape[]) shapes.toArray(new ga_a_Shape[0]);
		} else {
			for (int i=0;i<target.getPointCount();i++) {
				if (!other.contains(target.getPoint(i)[0], target.getPoint(i)[1])) {
					if (!other.hasVertex(target.getPoint(i)[0], target.getPoint(i)[1])) {
						ga_a_Shape shape = combineSingle(target, other, false, i);
						return new ga_a_Shape[] {shape};
					}
				}
			}
			
			return new ga_a_Shape[] {other};
		}
	}
	
	/**
	 * Combine two shapes
	 * 
	 * @param target The target shape
	 * @param missing The second shape to apply
	 * @param subtract True if we should subtract missing from target, otherwise union
	 * @param start The point to start at
	 * @return The newly created shape
	 */
	private ga_a_Shape combineSingle(ga_a_Shape target, ga_a_Shape missing, boolean subtract, int start) {
		ga_a_Shape current = target;
		ga_a_Shape other = missing;
		int point = start;
		int dir = 1;
		
		ga_ShapePolygon poly = new ga_ShapePolygon();
		boolean first = true;
		
		int loop = 0;
		
		// while we've not reached the same point
		float px = current.getPoint(point)[0];
		float py = current.getPoint(point)[1];
		
		while (!poly.hasVertex(px, py) || (first) || (current != target)) {
			first = false;
			loop++;
			if (loop > MAX_POINTS) {
				break;
			}
			
			// add the current point to the result shape
			poly.addPoint(px,py);
			if (listener != null) {
				listener.pointUsed(px,py);
			}

			// if the line between the current point and the next one intersect the
			// other shape work out where on the other shape and start traversing it's 
			// path instead
			ga_ShapeLine line = getLine(current, px, py, rationalPoint(current, point+dir));
			HitResult hit = intersect(other, line);
			
			if (hit != null) {
				ga_ShapeLine hitLine = hit.line;
				ga_Vector2 pt = hit.pt;
				px = pt.x;
				py = pt.y;
				
				if (listener != null) {
					listener.pointIntersected(px,py);
				}
				
				if (other.hasVertex(px, py)) {
					point = other.indexOf(pt.x,pt.y);
					dir = 1;
					px = pt.x;
					py = pt.y;
					
					ga_a_Shape temp = current;
					current = other;
					other = temp;
					continue;
				}
				
				float dx = hitLine.getDX() / hitLine.length();
				float dy = hitLine.getDY() / hitLine.length();
				dx *= EDGE_SCALE;
				dy *= EDGE_SCALE;
				
				if (current.contains(pt.x + dx, pt.y + dy)) {
					// the point is the next one, we need to take the first and traverse
					// the path backwards
					if (subtract) {
						if (current == missing) {
							point = hit.p2;
							dir = -1;
						} else {
							point = hit.p1;
							dir = 1;
						}
					} else {
						if (current == target) {
							point = hit.p2;
							dir = -1;
						} else {
							point = hit.p2;
							dir = -1;
						}
					}
					
					// swap the shapes over, we'll traverse the other one 
					ga_a_Shape temp = current;
					current = other;
					other = temp;
				} else if (current.contains(pt.x - dx, pt.y - dy)) {
					if (subtract) {
						if (current == target) {
							point = hit.p2;
							dir = -1;
						} else {
							point = hit.p1;
							dir = 1;
						}
					} else {
						if (current == missing) {
							point = hit.p1;
							dir = 1;
						} else {
							point = hit.p1;
							dir = 1;
						}
					}
					
					// swap the shapes over, we'll traverse the other one 
					ga_a_Shape temp = current;
					current = other;
					other = temp;
				} else {
					// give up
					if (subtract) {
						break;
					} else {
						point = hit.p1;
						dir = 1;
						ga_a_Shape temp = current;
						current = other;
						other = temp;
						
						point = rationalPoint(current, point+dir);
						px = current.getPoint(point)[0];
						py = current.getPoint(point)[1];
					}
				}
			} else {
				// otherwise just move to the next point in the current shape
				point = rationalPoint(current, point+dir);
				px = current.getPoint(point)[0];
				py = current.getPoint(point)[1];
			}
		}

		poly.addPoint(px,py);
		if (listener != null) {
			listener.pointUsed(px,py);
		}
		
		return poly;
	}
	
	/**
	 * Intersect a line with a shape
	 * 
	 * @param shape The shape to compare
	 * @param line The line to intersect against the shape
	 * @return The result describing the intersection or null if none
	 */
	public HitResult intersect(ga_a_Shape shape, ga_ShapeLine line) {
		float distance = Float.MAX_VALUE;
		HitResult hit = null;
		
		for (int i=0;i<shape.getPointCount();i++) {
			int next = rationalPoint(shape, i+1);
			ga_ShapeLine local = getLine(shape, i, next);
		
			ga_Vector2 pt = line.intersect(local, true);
			if (pt != null) {
				float newDis = pt.dst(line.getStart());
				if ((newDis < distance) && (newDis > EPSILON)) {
					hit = new HitResult();
					hit.pt = pt;
					hit.line = local;
					hit.p1 = i;
					hit.p2 = next;
					distance = newDis;
				}
			}
		}
		
		return hit;
	}
	
	/**
	 * Rationalize a point in terms of a given shape
	 * 
	 * @param shape The shape 
	 * @param p The index of the point
	 * @return The index that is rational for the shape
	 */
	public static int rationalPoint(ga_a_Shape shape, int p) {
		while (p < 0) {
			p += shape.getPointCount();
		}
		while (p >= shape.getPointCount()) {
			p -=  shape.getPointCount();
		}
		
		return p;
	}
	
	/**
	 * Get a line between two points in a shape
	 * 
	 * @param shape The shape
	 * @param s The index of the start point
	 * @param e The index of the end point
	 * @return The line between the two points
	 */
	public ga_ShapeLine getLine(ga_a_Shape shape, int s, int e) {
		float[] start = shape.getPoint(s);
		float[] end = shape.getPoint(e);
		
		ga_ShapeLine line = new ga_ShapeLine(start[0],start[1],end[0],end[1]);
		return line;
	}

	/**
	 * Get a line between two points in a shape
	 * 
	 * @param shape The shape
	 * @param sx The x coordinate of the start point
	 * @param sy The y coordinate of the start point
	 * @param e The index of the end point
	 * @return The line between the two points
	 */
	public ga_ShapeLine getLine(ga_a_Shape shape, float sx, float sy, int e) {
		float[] end = shape.getPoint(e);
		
		ga_ShapeLine line = new ga_ShapeLine(sx,sy,end[0],end[1]);
		return line;
	}
	
	/**
	 * A lightweigtht description of a intersection between a shape and 
	 * line.
	 */
	public class HitResult {
		/** The line on the target shape that intersected */
		public ga_ShapeLine line;
		/** The index of the first point on the target shape that forms the line */
		public int p1;
		/** The index of the second point on the target shape that forms the line */
		public int p2;
		/** The position of the intersection */
		public ga_Vector2 pt;
	}
}