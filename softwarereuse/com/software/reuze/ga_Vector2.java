package com.software.reuze;
/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
//package com.badlogic.gdx.math;

import java.io.Serializable;
import java.util.Random;
/**
 * Encapsulates a 2D vector. Allows chaining methods by returning a reference to itself
 * @author badlogicgames@gmail.com
 * 
 */
public class ga_Vector2 implements Serializable {	
	private static final long serialVersionUID = 913902788239530931L;
    public static enum Axis {

        X(ga_Vector2.X),
        Y(ga_Vector2.Y);

        private final ga_Vector2 vector;

        private Axis(final ga_Vector2 v) {
            this.vector = v;
        }

        public ga_Vector2 getVector() {
            return vector;
        }
    }
    /**
     * Defines positive X axis
     */
    public static final ga_Vector2 X = new ga_Vector2(1, 0);

    /**
     * Defines positive Y axis
     */
    public static final ga_Vector2 Y = new ga_Vector2(0, 1);

    /** Defines the zero vector. */
    public static final ga_Vector2 ZERO = new ga_Vector2();

    /**
     * Defines vector with both coords set to Float.MIN_VALUE. Useful for
     * bounding box operations.
     */
    public static final ga_Vector2 MIN = new ga_Vector2(Float.MIN_VALUE,
            Float.MIN_VALUE);

    /**
     * Defines vector with both coords set to Float.MAX_VALUE. Useful for
     * bounding box operations.
     */
    public static final ga_Vector2 MAX = new ga_Vector2(Float.MAX_VALUE,
            Float.MAX_VALUE);
	/** static temporary vector **/
	private final static ga_Vector2 tmp = new ga_Vector2();
	/** static temporary vector **/
	private final static ga_Vector2 tmp2 = new ga_Vector2();
	/** the x-component of this vector **/
	public float x;
	/** the y-component of this vector **/
	public float y;

	/**
	 * Constructs a new vector at (0,0)
	 */
	public ga_Vector2 () {

	}

	/**
	 * Constructs a vector with the given components
	 * @param x The x-component
	 * @param y The y-component
	 */
	public ga_Vector2 (float x, float y) {
		this.x = x;
		this.y = y;
	}
	public ga_Vector2 (double x, double y) {
		this.x = (float)x;
		this.y = (float)y;
	}
	public ga_Vector2 (int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Constructs a vector from the given vector
	 * @param v The vector
	 */
	public ga_Vector2 (final ga_Vector2 v) {
		set(v);
	}

	/**
	 * @return a copy of this vector
	 */
	public ga_Vector2 copy () {
		return new ga_Vector2(this);
	}

	/**
	 * @return The Euclidian length
	 */
	public float len () {
		return (float)Math.sqrt(x * x + y * y);
	}

	/**
	 * @return The squared Euclidian length
	 */
	public float len2 () {
		return x * x + y * y;
	}

	/**
	 * Sets this vector from the given vector
	 * @param v The vector
	 * @return This vector for chaining
	 */
	public ga_Vector2 set (final ga_Vector2 v) {
		x = v.x;
		y = v.y;
		return this;
	}

	/**
	 * Sets the components of this vector
	 * @param x The x-component
	 * @param y The y-component
	 * @return This vector for chaining
	 */
	public ga_Vector2 set (float x, float y) {
		this.x = x;
		this.y = y;
		return this;
	}
	public ga_Vector2 set (double x, double y) {
		this.x = (float)x;
		this.y = (float)y;
		return this;
	}

	/**
	 * Subtracts the given vector from this vector.
	 * @param v The vector
	 * @return This vector for chaining
	 */
	public ga_Vector2  sub(final ga_Vector2 v) {
		x -= v.x;
		y -= v.y;
		return this;
	}

	/**
	 * Normalizes this vector
	 * @return This vector for chaining
	 */
	public ga_Vector2 nor () {
		float len = len();
		if (len != 0) {
			x /= len;
			y /= len;
		}
		return this;
	}

	/**
	 * Adds the given vector to this vector
	 * @param v The vector
	 * @return This vector for chaining
	 */
	public ga_Vector2 add (final ga_Vector2 v) {
		x += v.x;
		y += v.y;
		return this;
	}

	/**
	 * Adds the given components to this vector
	 * @param x The x-component
	 * @param y The y-component
	 * @return This vector for chaining
	 */
	public ga_Vector2 add (float x, float y) {
		this.x += x;
		this.y += y;
		return this;
	}

	/**
	 * @param v The other vector
	 * @return The dot product between this and the other vector
	 */
	public float dot (final ga_Vector2 v) {
		return x * v.x + y * v.y;
	}

	/**
	 * Multiplies this vector by a scalar
	 * @param scalar The scalar
	 * @return This vector for chaining
	 */
	public ga_Vector2 mul (float scalar) {
		x *= scalar;
		y *= scalar;
		return this;
	}

	/**
	 * @param v The other vector
	 * @return the distance between this and the other vector
	 */
	public float dst (final ga_Vector2 v) {
		final float x_d = v.x - x;
		final float y_d = v.y - y;
		return (float)Math.sqrt(x_d * x_d + y_d * y_d);
	}

	/**
	 * @param x The x-component of the other vector
	 * @param y The y-component of the other vector
	 * @return the distance between this and the other vector
	 */
	public float dst (float x, float y) {
		final float x_d = x - this.x;
		final float y_d = y - this.y;
		return (float)Math.sqrt(x_d * x_d + y_d * y_d);
	}

	/**
	 * @param v The other vector
	 * @return the squared distance between this and the other vector
	 */
	public float dst2 (final ga_Vector2 v) {
		final float x_d = v.x - x;
		final float y_d = v.y - y;
		return x_d * x_d + y_d * y_d;
	}
	@Override public boolean equals(final Object p){
		return equalsWithTolerance((ga_Vector2)p, 0.0005f);
	}
    public boolean equalsWithTolerance(final ga_Vector2 v, float tolerance) {
    	if (v==null || tolerance<=0.0f) return false;
        if (m_MathUtils.abs(x - v.x) < tolerance) {
            if (m_MathUtils.abs(y - v.y) < tolerance) {
                return true;
            }
        }
        return false;
    }
	public static float dst(final ga_Vector2 p, final ga_Vector2 p2){
		return dst(p.x, p.y, p2.x, p2.y);
    }
	public static float dst(float x1, float y1,
								  float x2, float y2){
		x1 -= x2;
		y1 -= y2;
		return (float)Math.sqrt(x1 * x1 + y1 * y1);
    }
	public float dst2(float x2, float y2){
		return dst2(this.x, this.y, x2, y2);
	}
	public static float dst2(float x1, float y1,
									float x2, float y2){
		x1 -= x2;
		y1 -= y2;
		return (x1 * x1 + y1 * y1);
    }

	public String toString () {
		return x + "," + y;
	}

	/**
	 * Subtracts the other vector from this vector.
	 * @param x The x-component of the other vector
	 * @param y The y-component of the other vector
	 * @return This vector for chaining
	 */
	public ga_Vector2  sub(float x, float y) {
		this.x -= x;
		this.y -= y;
		return this;
	}

	/**
	 * @return a temporary copy of this vector. Use with care as this is backed by a single static Vector2 instance. v1.tmp().add(
	 *         v2.tmp() ) will not work!
	 */
	public ga_Vector2 tmp () {
		return tmp.set(this);
	}
	/**
	 * @return a temporary copy of this vector. Use with care as this is backed by a single static Vector2 instance. v1.tmp().add(
	 *         v2.tmp() ) will not work!
	 */
	public ga_Vector2 tmp2 () {
		return tmp2.set(this);
	}

	public ga_Vector2 mul(float a, float b) {
        x *= a;  y *= b;
        return this;
    }

    public ga_Vector2 mul(ga_Vector2 s) {
        return mul(s.x, s.y);
    }
    
    public ga_Vector2 div(float a, float b) {
        x /= a;  y /= b;
        return this;
    }
    
    public ga_Vector2 div(float a) {
        return div(a, a);
    }

    public ga_Vector2 div(ga_Vector2 s) {
        return div(s.x, s.y);
    }
	/**
	 * Calculates the 2D cross product between this
	 * and the given vector.
	 * @param v the other vector
	 * @return the cross product
	 */
	public float crs(final ga_Vector2 v) {		
		return this.x * v.y - this.y * v.x;
	}
	
	/**
	 * Calculates the 2D cross product between this
	 * and the given vector.
	 * @param x the x-coordinate of the other vector
	 * @param y the y-coordinate of the other vector
	 * @return the cross product
	 */
	public float crs(float x, float y) {
		 return this.x * y - this.y * x;
	}
	
	/**
	 * @return the angle in degrees of this vector (point) relative to the x-axis. Angles are counter-clockwise and between 0 and 360.
	 */
	public float angle() {
      float angle = (float)Math.atan2(y, x) * m_MathUtils.radiansToDegrees;
      if(angle < 0)
          angle += 360;
      return angle;
  }
	
	/**
	 * Rotates the Vector2 by the given angle(radians or degrees), counter-clockwise.
	 * @param angle the angle in degrees or radians
	 * @param radians, true indicates radians
	 * @return the 
	 */
   public ga_Vector2 rotate(float angle, boolean radians) {
      float rad = angle * (radians?1:m_MathUtils.degreesToRadians);
      float cos = (float)Math.cos(rad);
      float sin = (float)Math.sin(rad);
      
      float newX = this.x * cos - this.y * sin;
      y = this.x * sin + this.y * cos;
      
      this.x = newX;      
      return this;
  }
   
	/**
	 * Linearly interpolates between this vector and the target vector by alpha which is in the range [0,1]. The result is stored
	 * in this vector.
	 * 
	 * @param target The target vector
	 * @param alpha The interpolation coefficient
	 * @return This vector for chaining.
	 */
	public ga_Vector2 lerp (final ga_Vector2 target, float alpha) {
		mul(1.0f - alpha);
		add(target.tmp().mul(alpha));
		return this;
	}
	public static boolean linesIntersect(final ga_Vector2 p1, final ga_Vector2 p2, final ga_Vector2 p3, final ga_Vector2 p4){
		return linesIntersect(p1.x, p1.y, p2.x, p2.y, p3.x, p3.y, p4.x, p4.y);
	}
	public static boolean linesIntersect(float x1, float y1, float x2, float y2,
										 float x3, float y3, float x4, float y4){
		// Return false if either of the lines have zero length
		if (x1 == x2 && y1 == y2 ||
			x3 == x4 && y3 == y4){
			return false;
		}
		// Fastest method, based on Franklin Antonio's "Faster Line Segment Intersection" topic "in Graphics Gems III" book (http://www.graphicsgems.org/)
		float ax = x2-x1;
		float ay = y2-y1;
		float bx = x3-x4;
		float by = y3-y4;
		float cx = x1-x3;
		float cy = y1-y3;
		
		float alphaNumerator = by*cx - bx*cy;
		float commonDenominator = ay*bx - ax*by;
		if (commonDenominator > 0){
			if (alphaNumerator < 0 || alphaNumerator > commonDenominator){
				return false;
			}
		}else if (commonDenominator < 0){
			if (alphaNumerator > 0 || alphaNumerator < commonDenominator){
				return false;
			}
		}
		float betaNumerator = ax*cy - ay*cx;
		if (commonDenominator > 0){
			if (betaNumerator < 0 || betaNumerator > commonDenominator){
				return false;
			}
		}else if (commonDenominator < 0){
			if (betaNumerator > 0 || betaNumerator < commonDenominator){
				return false;
			}
		}
		// if commonDenominator == 0 then the lines are parallel.
		if (commonDenominator == 0){
			// This code wasn't in Franklin Antonio's method. It was added by Keith Woodward.
			// The lines are parallel.
			// Check if they're collinear.
			float collinearityTestForP3 = x1*(y2-y3) + x2*(y3-y1) + x3*(y1-y2);	// see http://mathworld.wolfram.com/Collinear.html
			// If p3 is collinear with p1 and p2 then p4 will also be collinear, since p1-p2 is parallel with p3-p4
			if (collinearityTestForP3 == 0){
				// The lines are collinear. Now check if they overlap.
				if (x1 >= x3 && x1 <= x4 || x1 <= x3 && x1 >= x4 ||
					x2 >= x3 && x2 <= x4 || x2 <= x3 && x2 >= x4 ||
					x3 >= x1 && x3 <= x2 || x3 <= x1 && x3 >= x2){
					if (y1 >= y3 && y1 <= y4 || y1 <= y3 && y1 >= y4 ||
						y2 >= y3 && y2 <= y4 || y2 <= y3 && y2 >= y4 ||
						y3 >= y1 && y3 <= y2 || y3 <= y1 && y3 >= y2){
						return true;
					}
				}
			}
			return false;
		}
		return true;
	}
	public ga_Vector2 translate(ga_Vector2 pointIncrement){
		translate(pointIncrement.x, pointIncrement.y);
		return this;
	}
	public ga_Vector2 translate(float xIncrement, float yIncrement){
		this.x += xIncrement;
		this.y += yIncrement;
		return this;
	}
	public ga_Vector2 translateCopy(final ga_Vector2 pointIncrement){
		ga_Vector2 p = this.copy();
		p.translate(pointIncrement.x, pointIncrement.y);
		return p;
	}
	public ga_Vector2 translateCopy(float xIncrement, float yIncrement){
		ga_Vector2 p = this.copy();
		p.translate(xIncrement, yIncrement);
		return p;
	}
	
	public ga_Vector2 rotate(float angle, final ga_Vector2 center) {
		rotate(angle, center.x, center.y);
		return this;
	}
	public ga_Vector2 rotate(float angle, float xCenter, float yCenter) {
		float currentAngle;
		float distance;
		currentAngle = (float)Math.atan2(y - yCenter, x - xCenter);
		currentAngle += angle;
		distance = ga_Vector2.dst(x, y, xCenter, yCenter);
		x = xCenter + (float)(distance*Math.cos(currentAngle));
		y = yCenter + (float)(distance*Math.sin(currentAngle));
		return this;
	}
	public ga_Vector2 rotateCopy(float angle, final ga_Vector2 center) {
		ga_Vector2 p = this.copy();
		p.rotate(angle, center.x, center.y);
		return p;
	}
	public ga_Vector2 rotateCopy(float angle, float xCenter, float yCenter) {
		ga_Vector2 p = this.copy();
		float currentAngle = (float)Math.atan2(p.y - yCenter, p.x - xCenter);
		currentAngle += angle;
		float distance = ga_Vector2.dst(p.x, p.y, xCenter, yCenter);
		p.x = xCenter + (float)(distance*Math.cos(currentAngle));
		p.y = yCenter + (float)(distance*Math.sin(currentAngle));
		return p;
	}
	public float ptSegDist(float x1, float y1, float x2, float y2){
		return ptSegDist(x1, y1, x2, y2, x, y);
	}
	public float ptSegDist(final ga_Vector2 start, final ga_Vector2 end){
		return ptSegDist(start.x, start.y, end.x, end.y, x, y);
	}
	public static float ptSegDist(final ga_Vector2 start, final ga_Vector2 end, final ga_Vector2 p){
		return ptSegDist(start.x, start.y, end.x, end.y, p.x, p.y);
	}
	public static float ptSegDist(float x1, float y1, float x2, float y2, float px, float py){
		return (float)Math.sqrt(ptSegDistSq(x1, y1, x2, y2, px, py));
	}
	
	public float ptSegDistSq(float x1, float y1, float x2, float y2){
		return ptSegDistSq(x1, y1, x2, y2, x, y);
	}
	public float ptSegDistSq(final ga_Vector2 start, final ga_Vector2 end){
		return ptSegDistSq(start.x, start.y, end.x, end.y, x, y);
	}
	public static float ptSegDistSq(final ga_Vector2 start, final ga_Vector2 end, final ga_Vector2 p){
		return ptSegDistSq(start.x, start.y, end.x, end.y, p.x, p.y);
	}
	public static float ptSegDistSq(float x1, float y1, float x2, float y2, float px, float py){
		//from: Line2D.Float.ptSegDistSq(x1, y1, x2, y2, px, py);
		// Adjust vectors relative to x1,y1
		// x2,y2 becomes relative vector from x1,y1 to end of segment
		x2 -= x1;
		y2 -= y1;
		// px,py becomes relative vector from x1,y1 to test point
		px -= x1;
		py -= y1;
		float dotprod = px * x2 + py * y2;
		float projlenSq;
		if (dotprod <= 0.0) {
			// px,py is on the side of x1,y1 away from x2,y2
			// distance to segment is length of px,py vector
			// "length of its (clipped) projection" is now 0.0
			projlenSq = 0.0f;
		} else {
			// switch to backwards vectors relative to x2,y2
			// x2,y2 are already the negative of x1,y1=>x2,y2
			// to get px,py to be the negative of px,py=>x2,y2
			// the dot product of two negated vectors is the same
			// as the dot product of the two normal vectors
			px = x2 - px;
			py = y2 - py;
			dotprod = px * x2 + py * y2;
			if (dotprod <= 0.0) {
				// px,py is on the side of x2,y2 away from x1,y1
				// distance to segment is length of (backwards) px,py vector
				// "length of its (clipped) projection" is now 0.0
				projlenSq = 0.0f;
			} else {
				// px,py is between x1,y1 and x2,y2
				// dotprod is the length of the px,py vector
				// projected on the x2,y2=>x1,y1 vector times the
				// length of the x2,y2=>x1,y1 vector
				projlenSq = dotprod * dotprod / (x2 * x2 + y2 * y2);
			}
		}
		// Distance to line is now the length of the relative point
		// vector minus the length of its projection onto the line
		// (which is zero if the projection falls outside the range
		//  of the line segment).
		float lenSq = px * px + py * py - projlenSq;
		if (lenSq < 0) {
			lenSq = 0;
		}
		return lenSq;
	}
	public static ga_Vector2 getLineLineIntersection(final ga_Vector2 p1, final ga_Vector2 p2, final ga_Vector2 p3, final ga_Vector2 p4){
		return getLineLineIntersection(p1.x, p1.y, p2.x, p2.y, p3.x, p3.y, p4.x, p4.y);
	}
	public static ga_Vector2 getLineLineIntersection(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
		float det1And2 = det(x1, y1, x2, y2);
		float det3And4 = det(x3, y3, x4, y4);
		float x1LessX2 = x1 - x2;
		float y1LessY2 = y1 - y2;
		float x3LessX4 = x3 - x4;
		float y3LessY4 = y3 - y4;
		float det1Less2And3Less4 = det(x1LessX2, y1LessY2, x3LessX4, y3LessY4);
		if (det1Less2And3Less4 == 0){
			// the denominator is zero so the lines are parallel and there's either no solution (or multiple solutions if the lines overlap) so return null.
			return null;
		}
		float x = (det(det1And2, x1LessX2,
						det3And4, x3LessX4) /
					det1Less2And3Less4);
		float y = (det(det1And2, y1LessY2,
						det3And4, y3LessY4) /
					det1Less2And3Less4);
		//		float x = (det(det(x1, y1, x2, y2), x1 - x2,
		//				det(x3, y3, x4, y4), x3 - x4) /
		//				det(x1 - x2, y1 - y2, x3 - x4, y3 - y4));
		//		float y = (det(det(x1, y1, x2, y2), y1 - y2,
		//				det(x3, y3, x4, y4), y3 - y4) /
		//				det(x1 - x2, y1 - y2, x3 - x4, y3 - y4));
		//		if (Double.isNaN(x) || Double.isNaN(y)){
		//			return null;
		//		}
		return new ga_Vector2(x, y);
	}
	public static float det(float a, float b, float c, float d) {
		return a * d - b * c;
	}
	public static ga_Vector2 getClosestPointOnSegment(float x1, float y1, float x2, float y2, float px, float py){
		ga_Vector2 closestPoint = new ga_Vector2();
		float x2LessX1 = x2 - x1;
		float y2LessY1 = y2 - y1;
		float lNum = x2LessX1*x2LessX1 + y2LessY1*y2LessY1;
		float rNum = ((px - x1)*x2LessX1 + (py - y1)*y2LessY1) / lNum;
		//		float lNum = (x2 - x1)*(x2 - x1) + (y2 - y1)*(y2 - y1);
		//		float rNum = ((px - x1)*(x2 - x1) + (py - y1)*(y2 - y1)) / lNum;
		if (rNum <= 0){
			closestPoint.x = x1;
			closestPoint.y = y1;
		}else if (rNum >= 1){
			closestPoint.x = x2;
			closestPoint.y = y2;
		}else{
			closestPoint.x = (x1 + rNum*x2LessX1);
			closestPoint.y = (y1 + rNum*y2LessY1);
		}
		return closestPoint;
		//    from:  http://www.codeguru.com/forum/showthread.php?t=194400
		//    Let the point be C (Cx,Cy) and the line be AB (Ax,Ay) to (Bx,By).
		//    Let P be the point of perpendicular projection of C on AB.  The parameter
		//    r, which indicates P's position along AB, is computed by the dot product
		//    of AC and AB divided by the square of the length of AB:
		//
		//    (1)     AC dot AB
		//        r = ---------
		//            ||AB||^2
		//
		//    r has the following meaning:
		//
		//        r=0      P = A
		//        r=1      P = B
		//        r<0      P is on the backward extension of AB
		//        r>1      P is on the forward extension of AB
		//        0<r<1    P is interior to AB
		//
		//    The length of a line segment in d dimensions, AB is computed by:
		//
		//        L = sqrt( (Bx-Ax)^2 + (By-Ay)^2 + ... + (Bd-Ad)^2)
		//
		//    so in 2D:
		//
		//        L = sqrt( (Bx-Ax)^2 + (By-Ay)^2 )
		//
		//    and the dot product of two vectors in d dimensions, U dot V is computed:
		//
		//        D = (Ux * Vx) + (Uy * Vy) + ... + (Ud * Vd)
		//
		//    so in 2D:
		//
		//        D = (Ux * Vx) + (Uy * Vy)
		//
		//    So (1) expands to:
		//
		//            (Cx-Ax)(Bx-Ax) + (Cy-Ay)(By-Ay)
		//        r = -------------------------------
		//                          L^2
		//
		//    The point P can then be found:
		//
		//        Px = Ax + r(Bx-Ax)
		//        Py = Ay + r(By-Ay)
		//
		//    And the distance from A to P = r*L.
		//
		//    Use another parameter s to indicate the location along PC, with the
		//    following meaning:
		//           s<0      C is left of AB
		//           s>0      C is right of AB
		//           s=0      C is on AB
		//
		//    Compute s as follows:
		//
		//            (Ay-Cy)(Bx-Ax)-(Ax-Cx)(By-Ay)
		//        s = -----------------------------
		//                        L^2
		//
		//
		//    Then the distance from C to P = |s|*L.
	}
	public ga_Vector2 getClosestPointOnSegment(float x1, float y1, float x2, float y2){
		return getClosestPointOnSegment(x1, y1, x2, y2, x, y);
	}
	public ga_Vector2 getClosestPointOnSegment(ga_Vector2 p1, ga_Vector2 p2){
		return getClosestPointOnSegment(p1.x, p1.y, p2.x, p2.y, x, y);
	}
	public ga_Vector2 createPointFromAngle(float angle, float distance){
		return createPointFromAngle(x, y, angle, distance);
	}
	public static ga_Vector2 createPointFromAngle(float x, float y, float angle, float distance){
		ga_Vector2 p = new ga_Vector2();
		float xDist = (float)Math.cos(angle)*distance;
		float yDist = (float)Math.sin(angle)*distance;
		p.x = (x+xDist);
		p.y = (y+yDist);
		return p;
	}
	public ga_Vector2 createPointToward(ga_Vector2 p, float distance){
		return createPointToward(x, y, p.x, p.y, distance);
	}
	public ga_Vector2 createPointToward(float x2, float y2, float distance){
		return createPointToward(x, y, x2, y2, distance);
	}
	public static ga_Vector2 createPointToward(float x, float y, float x2, float y2, float distance){
		ga_Vector2 p = new ga_Vector2();
		float xDiff = (x2 - x);
		float yDiff = (y2 - y);
		float ptDist = (float)Math.sqrt(xDiff*xDiff + yDiff*yDiff);
		float distOnPtDist = distance/ptDist;
		float xDist = xDiff*distOnPtDist;
		float yDist = yDiff*distOnPtDist;
		p.x = (x+xDist);
		p.y = (y+yDist);
		return p;
	}
	/**
	 * Returns a positive double if (px, py) is counter-clockwise to (x2, y2) relative to (x1, y1).
	 * in the cartesian coordinate space (positive x-axis extends right, positive y-axis extends up).
	 * Returns a negative double if (px, py) is clockwise to (x2, y2) relative to (x1, y1).
	 * Returns a 0.0 if (px, py), (x1, y1) and (x2, y2) are collinear.
	 * Note that this method gives different results to java...geom.Line2D.relativeCCW() since Java2D
	 * uses a different coordinate system (positive x-axis extends right, positive y-axis extends down).
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @param px
	 * @param py
	 * @return
	 */
	public static int relCCW(float x1, float y1,
							 float x2, float y2,
							 float px, float py){
		float ccw = relCCWDouble(x1, y1, x2, y2, px, py);
		return (ccw < 0.0) ? -1 : ((ccw > 0.0) ? 1 : 0);
    }
	
	public int relCCW(float x1, float y1, float x2, float y2){
		return relCCW(x1, y1, x2, y2, x, y);
	}
	public int relCCW(ga_Vector2 p1, ga_Vector2 p2){
		return relCCW(p1.x, p1.y, p2.x, p2.y, x, y);
	}
	public static float relCCWDouble(float x1, float y1,
				  float x2, float y2,
				  float px, float py) {
		x2 -= x1;
		y2 -= y1;
		px -= x1;
		py -= y1;
		float ccw = py * x2 - px * y2;
		return ccw;
	}
	public float relCCWDouble(final ga_Vector2 p1, final ga_Vector2 p2){
		return relCCWDouble(p1.x, p1.y, p2.x, p2.y, x, y);
	}
	public static float relCCWDoubleExtra(float x1, float y1,
										   float x2, float y2,
										   float px, float py){
		x2 -= x1;
		y2 -= y1;
		px -= x1;
		py -= y1;
		float ccw = py * x2 - px * y2;
		if (ccw == 0.0) {
			// The point is colinear, classify based on which side of
			// the segment the point falls on.  We can calculate a
			// relative value using the projection of px,py onto the
			// segment - a negative value indicates the point projects
			// outside of the segment in the direction of the particular
			// end-point used as the origin for the projection.
			ccw = px * x2 + py * y2;
			if (ccw > 0.0) {
				// Reverse the projection to be relative to the original x2,y2
				// x2 and y2 are simply negated.
				// px and py need to have (x2 - x1) or (y2 - y1) subtracted
				//    from them (based on the original values)
				// Since we really want to get a positive answer when the
				//    point is "beyond (x2,y2)", then we want to calculate
				//    the inverse anyway - thus we leave x2 & y2 negated.
				px -= x2;
				py -= y2;
				ccw = px * x2 + py * y2;
				if (ccw < 0.0) {
					ccw = 0.0f;
				}
			}
		}
		return ccw;
	}
	public ga_Vector2 midPoint(final ga_Vector2 p){
		return midPoint(x, y, p.x, p.y);
	}
	public static ga_Vector2 midPoint(final ga_Vector2 p, final ga_Vector2 p2){
		return midPoint(p.x, p.y, p2.x, p2.y);
	}
	public static ga_Vector2 midPoint(float x, float y, float x2, float y2){
		return new ga_Vector2((x + x2)/2f, (y + y2)/2f);
	}
	public float ptLineDist(float x1, float y1, float x2, float y2){
		return ptLineDist(x1, y1, x2, y2, x, y);
	}
	public float ptLineDist(final ga_Vector2 start, final ga_Vector2 end){
		return ptLineDist(start.x, start.y, end.x, end.y, x, y);
	}
	public static float ptLineDist(final ga_Vector2 start, final ga_Vector2 end, final ga_Vector2 p){
		return ptLineDist(start.x, start.y, end.x, end.y, p.x, p.y);
	}
	public static float ptLineDist(float x1, float y1, float x2, float y2, float px, float py){
		return (float)Math.sqrt(ptLineDistSq(x1, y1, x2, y2, px, py));
	}
	
	public float ptLineDistSq(float x1, float y1, float x2, float y2){
		return ptLineDistSq(x1, y1, x2, y2, x, y);
	}
	public float ptLineDistSq(final ga_Vector2 start, final ga_Vector2 end){
		return ptLineDistSq(start.x, start.y, end.x, end.y, x, y);
	}
	public static float ptLineDistSq(final ga_Vector2 start, final ga_Vector2 end, final ga_Vector2 p){
		return ptLineDistSq(start.x, start.y, end.x, end.y, p.x, p.y);
	}
	public static float ptLineDistSq(float x1, float y1, float x2, float y2, float px, float py){
		//from: Line2D.Float.ptLineDistSq(x1, y1, x2, y2, px, py);
		// Adjust vectors relative to x1,y1
		// x2,y2 becomes relative vector from x1,y1 to end of segment
		x2 -= x1;
		y2 -= y1;
		// px,py becomes relative vector from x1,y1 to test point
		px -= x1;
		py -= y1;
		float dotprod = px * x2 + py * y2;
		// dotprod is the length of the px,py vector
		// projected on the x1,y1=>x2,y2 vector times the
		// length of the x1,y1=>x2,y2 vector
		float projlenSq = dotprod * dotprod / (x2 * x2 + y2 * y2);
		// Distance to line is now the length of the relative point
		// vector minus the length of its projection onto the line
		float lenSq = px * px + py * py - projlenSq;
		if (lenSq < 0) {
			lenSq = 0;
		}
		return lenSq;
	}
        public float findSignedAngle(float ox, float oy){
		return findSignedAngle(this.x, this.y, ox, oy);
	}
	public float findSignedAngle(final ga_Vector2 dest){
		return findSignedAngle(this, dest);
	}
	public static float findSignedAngle(final ga_Vector2 start, final ga_Vector2 dest){
		return findSignedAngle(start.x, start.y, dest.x, dest.y);
	}
	public static float findSignedAngle(float x1, float y1, float x2, float y2){
		float x = x2 - x1;
		float y = y2 - y1;
		float angle = (float)Math.atan2(y, x);
		//		float angle = (float)(FastMath.atan2_fast((float)y, (float)x));
		//		float angle = (float)(FastMath.atan2((float)y, (float)x));
		return angle;
	}
	public float findAngle(float ox, float oy){
		return findAngle(this.x, this.y, ox, oy);
	}
	public float findAngle(final ga_Vector2 dest){
		return findAngle(this, dest);
	}

	public float findAngleFromOrigin(){
		float angle = findSignedAngleFromOrigin();
		if (angle < 0) {
			angle += m_MathUtils.TWO_PI;
		}
		return angle;
	}
	public float findSignedAngleFromOrigin(){
		return (float)Math.atan2(y, x);
	}
	
	public static float findAngle(final ga_Vector2 start, final ga_Vector2 dest){
		return findAngle(start.x, start.y, dest.x, dest.y);
	}
	public static float findAngle(float x1, float y1, float x2, float y2){
		float angle = findSignedAngle(x1, y1, x2, y2);
		if (angle < 0) {
			angle += m_MathUtils.TWO_PI;
		}
		return angle;
	}
	
	public float findSignedRelativeAngle(float x1, float y1, float x2, float y2){
		return findSignedRelativeAngle(this.x, this.y, x1, y1, x2, y2);
	}
	public float findSignedRelativeAngle(final ga_Vector2 start, final ga_Vector2 end){
		return findSignedRelativeAngle(this, start, end);
	}
	public static float findSignedRelativeAngle(final ga_Vector2 point, final ga_Vector2 start, final ga_Vector2 end){
		return findSignedRelativeAngle(point.x, point.y, start.x, start.y, end.x, end.y);
	}
	public static float findSignedRelativeAngle(float x, float y, float x1, float y1, float x2, float y2){
		float lineAngle = findAngle(x1, y1, x2, y2);
		float pointAngle = findAngle(x1, y1, x, y);
		if (pointAngle < lineAngle){
			pointAngle += m_MathUtils.TWO_PI;
		}
		float relativePointAngle = pointAngle - lineAngle;
		if (relativePointAngle > Math.PI){
			relativePointAngle -= m_MathUtils.TWO_PI;
		}
		assert relativePointAngle <= Math.PI && relativePointAngle >= -Math.PI : relativePointAngle;
		return relativePointAngle;
	}
	
	public float findRelativeAngle(float x1, float y1, float x2, float y2){
		return findRelativeAngle(this.x, this.y, x1, y1, x2, y2);
	}
	public float findRelativeAngle(final ga_Vector2 start, final ga_Vector2 end){
		return findRelativeAngle(this, start, end);
	}
	public static float findRelativeAngle(final ga_Vector2 point, final ga_Vector2 start, final ga_Vector2 end){
		return findRelativeAngle(point.x, point.y, start.x, start.y, end.x, end.y);
	}
	public static float findRelativeAngle(float x, float y, float x1, float y1, float x2, float y2){
		float relativePointAngle = findSignedRelativeAngle(x, y, x1, y1, x2, y2);
		if (relativePointAngle < -Math.PI){
			relativePointAngle += m_MathUtils.TWO_PI;
		}
		assert relativePointAngle <= 2*Math.PI && relativePointAngle >= 0 : relativePointAngle;
		return relativePointAngle;
	}
    /**
     * Creates a new vector from the given angle in the XY plane.
     * 
     * The resulting vector for theta=0 is equal to the positive X axis.
     * 
     * @param theta
     * @return new vector pointing into the direction of the passed in angle
     */
    public static final ga_Vector2 fromTheta(float theta) {
        return new ga_Vector2((float) Math.cos(theta), (float) Math.sin(theta));
    }
    /**
     * Limits the vector's magnitude to the length given
     * 
     * @param lim
     *            new maximum magnitude
     * @return itself
     */
    public ga_Vector2 limit(float lim) {
        if (len2() > lim * lim) {
            return nor().mul(lim);
        }
        return this;
    }
    /**
     * Adjusts the vector components to the maximum values of both vectors
     * 
     * @param v
     * @return itself
     */
    public ga_Vector2 max(final ga_Vector2 v) {
        x = m_MathUtils.max(x, v.x);
        y = m_MathUtils.max(y, v.y);
        return this;
    }
    
    /**
     * Adjusts the vector components to the minimum values of both vectors
     * 
     * @param v
     * @return itself
     */
    public ga_Vector2 min(final ga_Vector2 v) {
        x = m_MathUtils.min(x, v.x);
        y = m_MathUtils.min(y, v.y);
        return this;
    }

    /**
     * Normalizes the vector to the given length.
     * 
     * @param len
     *            desired length
     * @return itself
     */
    public ga_Vector2 norTo(float len) {
        float mag = (float) Math.sqrt(x * x + y * y);
        if (mag > 0) {
            mag = len / mag;
            x *= mag;
            y *= mag;
        }
        return this;
    }

    public ga_Vector2 perpendicular() {
        float t = x;
        x = -y;
        y = t;
        return this;
    }

    public ga_Vector2 reciprocal() {
        x = 1f / x;
        y = 1f / y;
        return this;
    }

    public ga_Vector2 reflect(final ga_Vector2 normal) {
        return set(normal.tmp().mul(this.dot(normal) * 2).sub(this));
    }
    
    /**
     * Scales vector uniformly by factor -1 ( v = -v ), overrides coordinates
     * with result
     * 
     * @return itself
     */
    public ga_Vector2 invert() {
        x *= -1;
        y *= -1;
        return this;
    }
    
    /**
     * Interpolates the vector towards the given target vector, using linear
     * interpolation
     * 
     * @param v
     *            target vector
     * @param f
     *            interpolation factor (should be in the range 0..1)
     * @return itself, result overrides current vector
     */
    public ga_Vector2 interpolateTo(final ga_Vector2 v, float f) {
        x += (v.x - x) * f;
        y += (v.y - y) * f;
        return this;
    }
    public float angleBetween(final ga_Vector2 v) {
        return (float) Math.acos(dot(v));
    }

    public float angleBetween(final ga_Vector2 v, boolean forceNormalize) {
        float theta;
        if (forceNormalize) {
            theta = tmp2().nor().dot(v.tmp().nor());
        } else {
            theta = dot(v);
        }
        return (float) Math.acos(theta);
    }

    /**
     * Sets all vector components to 0.
     * 
     * @return itself
     */
    public ga_Vector2 clear() {
        x = y = 0;
        return this;
    }

    public int compareTo(final ga_Vector2 v) {
        if (x == v.x && y == v.y) {
            return 0;
        }
        return (int) (len2() - v.len2());
    }

    /**
     * Forcefully fits the vector in the given rectangle defined by the points.
     * 
     * @param min
     * @param max
     * @return itself
     */
    public ga_Vector2 constrain(final ga_Vector2 min, final ga_Vector2 max) {
        x = m_MathUtils.clip(x, min.x, max.x);
        y = m_MathUtils.clip(y, min.y, max.y);
        return this;
    }
    /**
     * Returns a unique code for this vector object based on it's values. If two
     * vectors are logically equivalent, they will return the same hash code
     * value.
     * 
     * @return the hash code value of this vector.
     */
    @Override public int hashCode() {
        return 37 * Float.floatToIntBits(x) + Float.floatToIntBits(y);
    }

    public float heading() {
        return (float) Math.atan2(y, x);
    }

    public final ga_Vector2 jitter(float j) {
        return jitter(j, j);
    }

    /**
     * Adds random jitter to the vector in the range -j ... +j using the default
     * {@link Random} generator of {@link m_MathUtils}.
     * 
     * @param jx
     *            maximum x jitter
     * @param jy
     *            maximum y jitter
     * @return itself
     */
    public final ga_Vector2 jitter(float jx, float jy) {
        x += m_MathUtils.normalizedRandom() * jx;
        y += m_MathUtils.normalizedRandom() * jy;
        return this;
    }

    public final ga_Vector2 jitter(Random rnd, float j) {
        return jitter(rnd, j, j);
    }

    public final ga_Vector2 jitter(Random rnd, float jx, float jy) {
        x += m_MathUtils.normalizedRandom(rnd) * jx;
        y += m_MathUtils.normalizedRandom(rnd) * jy;
        return this;
    }

    public final ga_Vector2 jitter(Random rnd, ga_Vector2 jv) {
        return jitter(rnd, jv.x, jv.y);
    }

    public final ga_Vector2 jitter(ga_Vector2 jv) {
        return jitter(jv.x, jv.y);
    }
    /**
     * Static factory method. Creates a new random unit vector using the Random
     * implementation set as default for the {@link m_MathUtils} class.
     * 
     * @return a new random normalized unit vector.
     */
    public static final ga_Vector2 random() {
        return random(m_MathUtils.RND);
    }

    /**
     * Static factory method. Creates a new random unit vector using the given
     * Random generator instance. I recommend to have a look at the
     * https://uncommons-maths.dev.java.net library for a good choice of
     * reliable and high quality random number generators.
     * 
     * @return a new random normalized unit vector.
     */
    public static final ga_Vector2 random(Random rnd) {
        ga_Vector2 v = new ga_Vector2(rnd.nextFloat() * 2 - 1, rnd.nextFloat() * 2 - 1);
        return v.nor();
    }
    public final ga_Vector2 setComponent(ga_Vector2.Axis id, float val) {
        switch (id) {
            case X:
                x = val;
                break;
            case Y:
                y = val;
                break;
        }
        return this;
    }

    public final ga_Vector2 setComponent(int id, float val) {
        switch (id) {
            case 0:
                x = val;
                break;
            case 1:
                y = val;
                break;
            default:
                throw new IllegalArgumentException(
                        "component id needs to be 0 or 1");
        }
        return this;
    }
    public float getComponent(ga_Vector2.Axis id) {
        switch (id) {
            case X:
                return x;
            case Y:
                return y;
        }
        return 0;
    }

    public final float getComponent(int id) {
        switch (id) {
            case 0:
                return x;
            case 1:
                return y;
        }
        throw new IllegalArgumentException("index must be 0 or 1");
    }
    public float[] toArray() {
        return new float[] {
                x, y
        };
    }

    public final ga_Vector2 toCartesian() {
        float xx = (float) (x * Math.cos(y));
        y = (float) (x * Math.sin(y));
        x = xx;
        return this;
    }

    public final ga_Vector2 toPolar() {
        float r = (float) Math.sqrt(x * x + y * y);
        y = (float) Math.atan2(y, x);
        x = r;
        return this;
    }
}
