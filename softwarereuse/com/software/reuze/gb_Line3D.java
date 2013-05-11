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
//package com.badlogic.gdx.math.collision;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


//import com.badlogic.gdx.math.Vector3;

/**
 * A Segment is a line in 3-space having a starting and an ending position.
 * 
 * @author mzechner
 * 
 */
public class gb_Line3D implements Serializable {	
	private static final long serialVersionUID = 2739667069736519602L;

	/** the starting position **/
	public gb_Vector3 a;

	/** the ending position **/
	public gb_Vector3 b;

	/**
	 * Constructs a new Segment from the two points given.
	 * 
	 * @param a the first point
	 * @param b the second point
	 */
	public gb_Line3D (gb_Vector3 a, gb_Vector3 b) {
		this.a=a;
		this.b=b;
	}

	/**
	 * Constructs a new Segment from the two points given.
	 * @param aX the x-coordinate of the first point
	 * @param aY the y-coordinate of the first point
	 * @param aZ the z-coordinate of the first point
	 * @param bX the x-coordinate of the second point
	 * @param bY the y-coordinate of the second point
	 * @param bZ the z-coordinate of the second point
	 */
	public gb_Line3D (float aX, float aY, float aZ, float bX, float bY, float bZ) {
		this.a.set(aX, aY, aZ);
		this.b.set(bX, bY, bZ);
	}
    /**
     * Splits the line between A and B into segments of the given length,
     * starting at point A. The tweened points are added to the given result
     * list. The last point added is B itself and hence it is likely that the
     * last segment has a shorter length than the step length requested. The
     * first point (A) can be omitted and not be added to the list if so
     * desired.
     * 
     * @param a
     *            start point
     * @param b
     *            end point (always added to results)
     * @param stepLength
     *            desired distance between points
     * @param segments
     *            existing array list for results (or a new list, if null)
     * @param addFirst
     *            false, if A is NOT to be added to results
     * @return list of result vectors
     */
    public static final List<gb_Vector3> splitIntoSegments(gb_Vector3 a, gb_Vector3 b,
            float stepLength, List<gb_Vector3> segments, boolean addFirst) {
        if (segments == null) {
            segments = new ArrayList<gb_Vector3>();
        }
        if (addFirst) {
            segments.add(a.cpy());
        }
        float dist = a.dst(b);
        if (dist > stepLength) {
            gb_Vector3 pos = a.cpy();
            gb_Vector3 step = b.tmp().sub(a).limit(stepLength);
            while (dist > stepLength) {
                pos.add(step);
                segments.add(pos);
                dist -= stepLength;
            }
        }
        segments.add(b.cpy());
        return segments;
    }

    /**
     * Calculates the line segment that is the shortest route between this line
     * and the given one. Also calculates the coefficients where the end points
     * of this new line lie on the existing ones. If these coefficients are
     * within the 0.0 .. 1.0 interval the end-points of the intersection line are
     * within the given line segments, if not then the intersection line is
     * outside.
     * 
     * <p>
     * Code based on original by Paul Bourke:<br/>
     * http://local.wasp.uwa.edu.au/~pbourke/geometry/lineline3d/
     * </p>
     */
    /*public boolean isIntersectionInside() {
        return type == Type.INTERSECTING && mua >= 0 && mua <= 1
                && mub >= 0 && mub <= 1;
    }*/
    public gb_Line3D closestLineTo(final gb_Line3D l) {

        gb_Vector3 p43 = l.a.tmp2().sub(l.b);
        if (p43.isZero()) {
            return null;
        }
        gb_Vector3 p21 = b.cpy().sub(a);
        if (p21.isZero()) {
            return null;
        }
        gb_Vector3 p13 = a.tmp().sub(l.a);

        float d1343 = p13.x * p43.x + p13.y * p43.y + p13.z * p43.z;
        float d4321 = p43.x * p21.x + p43.y * p21.y + p43.z * p21.z;
        float d1321 = p13.x * p21.x + p13.y * p21.y + p13.z * p21.z;
        float d4343 = p43.x * p43.x + p43.y * p43.y + p43.z * p43.z;
        float d2121 = p21.x * p21.x + p21.y * p21.y + p21.z * p21.z;

        float denom = d2121 * d4343 - d4321 * d4321;
        if (m_MathUtils.abs(denom) < m_MathUtils.EPS) {
            return null;
        }
        float numer = d1343 * d4321 - d1321 * d4343;
        float mua = numer / denom;
        float mub = (d1343 + d4321 * mua) / d4343;

        gb_Vector3 pa = a.cpy().add(p21.mul(mua));
        gb_Vector3 pb = l.a.cpy().add(p43.mul(mub));
        return new gb_Line3D(pa, pb);
    }

    /**
     * Computes the closest point on this line to the given one.
     * 
     * @param p
     *            point to check against
     * @return closest point on the line
     */
    public gb_Vector3 closestPointTo(final gb_Vector3 p) {
        final gb_Vector3 v = b.tmp2().sub(a);
        final float t = p.tmp().sub(a).dot(v) / v.len2();
        // Check to see if t is beyond the extents of the line segment
        if (t < 0.0f) {
            return a.cpy();
        } else if (t > 1.0f) {
            return b.cpy();
        }
        // Return the point between 'a' and 'b'
        return a.cpy().add(v.mul(t));
    }

    public gb_Line3D copy() {
        return new gb_Line3D(a.cpy(), b.cpy());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof gb_Line3D)) {
            return false;
        }
        gb_Line3D l = (gb_Line3D) obj;
        return (a.equals(l.a) || a.equals(l.b))
                && (b.equals(l.b) || b.equals(l.a));
    }

    public gb_Vector3 getDirection() {
        return b.cpy().sub(a).nor();
    }

    public float getLength() {
        return a.dst(b);
    }

    public float getLengthSquared() {
        return a.dst2(b);
    }

    public gb_Vector3 getMidPoint() {
        return a.cpy().add(b).mul(0.5f);
    }

    public gb_Vector3 getNormal() {
        return b.cpy().crs(a);
    }

    public boolean hasEndPoint(final gb_Vector3 p) {
        return a.equals(p) || b.equals(p);
    }

    @Override
    public int hashCode() {
        return a.hashCode() + b.hashCode();
    }

    public gb_Line3D offsetAndGrowBy(float offset, float scale, final gb_Vector3 ref) {
        gb_Vector3 m = getMidPoint();
        gb_Vector3 d = getDirection();
        gb_Vector3 n = a.tmp().crs(d).nor();
        if (ref != null && m.sub(ref).dot(n) < 0) {
            n.inv();
        }
        n.norTo(offset);
        a.add(n);
        b.add(n);
        d.mul(scale);
        a.sub(d);
        b.add(d);
        return this;
    }

    public gb_Line3D set(final gb_Vector3 a, final gb_Vector3 b) {
        this.a = a;
        this.b = b;
        return this;
    }

    public List<gb_Vector3> splitIntoSegments(List<gb_Vector3> segments,
            float stepLength, boolean addFirst) {
        return splitIntoSegments(a, b, stepLength, segments, addFirst);
    }

    public gb_Ray toRay() {
        return new gb_Ray(a.cpy(), b.cpy().sub(a).nor());
    }

    public String toString() {
        return a.toString() + " -> " + b.toString();
    }
}
