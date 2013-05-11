package com.software.reuze;
/*
 * Copyright (c) 2006-2011 Karsten Schmidt
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * http://creativecommons.org/licenses/LGPL/2.1/
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301, USA
 */




import java.util.ArrayList;
import java.util.List;

import com.software.reuze.ga_Ray2D;

//import toxi.geom.Line2D.LineIntersection.Type;

public class ga_Line2D {

    public static class LineIntersection {

        public static enum Type {
        	COINCIDENT,
            COINCIDENT_NO_INTERSECT,
            PARALLEL,
            NON_INTERSECTING,
            INTERSECTING
        }

        private Type type;
        private ga_Vector2 pos;

        public LineIntersection(Type type, final ga_Vector2 pos) {
            this.type = type;
            this.pos = pos;
        }

        /**
         * @return the pos
         */
        public ga_Vector2 getPos() {
            return pos;
        }

        /**
         * @return the type
         */
        public Type getType() {
            return type;
        }

        public String toString() {
            return type + "," + pos;
        }
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
    public static final List<ga_Vector2> splitIntoSegments(ga_Vector2 a, ga_Vector2 b,
            float stepLength, List<ga_Vector2> segments, boolean addFirst) {
        if (segments == null) {
            segments = new ArrayList<ga_Vector2>();
        }
        if (addFirst) {
            segments.add(a.copy());
        }
        float dist = a.dst(b);
        if (dist > stepLength) {
            ga_Vector2 pos = a.copy();
            ga_Vector2 step = b.tmp().sub(a).limit(stepLength);
            while (dist > stepLength) {
                pos.add(step);
                segments.add(pos.copy());
                dist -= stepLength;
            }
        }
        segments.add(b.copy());
        return segments;
    }

    public ga_Vector2 a, b;


    public ga_Line2D(ga_Vector2 a, ga_Vector2 b) {
        this.a = a;
        this.b = b;
    }
    
    public ga_Line2D(float x1, float y1, float x2, float y2) {
        this.a = new ga_Vector2(x1,y1);
        this.b = new ga_Vector2(x2,y2);
    }


    /**
     * Computes the closest point on this line to the point given.
     * 
     * @param p
     *            point to check against
     * @return closest point on the line
     */
    public ga_Vector2 closestPointTo(final ga_Vector2 p) {
        ga_Vector2 v = b.tmp().sub(a);
        final float t = p.tmp2().sub(a).dot(v) / v.len2();
        // Check to see if t is beyond the extents of the line segment
        if (t < 0.0f) {
            return a.copy();
        } else if (t > 1.0f) {
            return b.copy();
        }
        // Return the point between 'a' and 'b'
        return a.copy().add(v.mul(t));
    }
    public float distanceToPoint(ga_Vector2 p) {
        return closestPointTo(p).dst(p);
    }

    public float distanceToPointSquared(ga_Vector2 p) {
        return closestPointTo(p).dst2(p);
    }
    public ga_Line2D copy() {
    	//TODO what is right action in ALL copy if part is null or NaN?
        return new ga_Line2D(a.copy(), b.copy());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ga_Line2D)) {
            return false;
        }
        ga_Line2D l = (ga_Line2D) obj;
        return (a.equals(l.a) || a.equals(l.b))
                && (b.equals(l.b) || b.equals(l.a));
    }

    public ga_Vector2 getDirection() {
        return b.copy().sub(a).nor();
    }

    public float len() {
        return a.dst(b);
    }

    public float len2() {
        return a.dst2(b);
    }

    public ga_Vector2 getMidPoint() {
        return a.copy().add(b).mul(0.5f);
    }

    public ga_Vector2 getNormal() {
        return b.copy().sub(a).perpendicular();
    }

    public float getTheta() {
        return a.angleBetween(b, true);
    }

    public boolean isEndPoint(ga_Vector2 p) {
        return a.equals(p) || b.equals(p);
    }

    @Override
    public int hashCode() {
        return a.hashCode() + b.hashCode();
    }

    /**
     * Computes intersection between this and the given line. The returned value
     * is a {@link LineIntersection} instance and contains both the type of
     * intersection as well as the intersection point (if existing).
     * 
     * Based on: http://local.wasp.uwa.edu.au/~pbourke/geometry/lineline2d/
     * 
     * @param l
     *            line to intersect with
     * @return intersection result
     */
    public LineIntersection intersectLine(ga_Line2D l) {
        LineIntersection isec = null;
        float denom =
                (l.b.y - l.a.y) * (b.x - a.x) - (l.b.x - l.a.x) * (b.y - a.y);

        float na =
                (l.b.x - l.a.x) * (a.y - l.a.y) - (l.b.y - l.a.y)
                        * (a.x - l.a.x);
        float nb = (b.x - a.x) * (a.y - l.a.y) - (b.y - a.y) * (a.x - l.a.x);

        if (denom != 0.0) {
            float ua = na / denom;
            float ub = nb / denom;
            if (ua >= 0.0f && ua <= 1.0 && ub >= 0.0 && ub <= 1.0) {
                isec =
                        new LineIntersection(LineIntersection.Type.INTERSECTING,
                                a.tmp().interpolateTo(b, ua));
            } else {
                isec = new LineIntersection(LineIntersection.Type.NON_INTERSECTING, null);
            }
        } else {
        	if (na == 0.0 && nb == 0.0) {
                if (this.distanceToPoint(l.a) == 0.0) {
                    isec = new LineIntersection(LineIntersection.Type.COINCIDENT, null);
                } else {
                    isec = new LineIntersection(LineIntersection.Type.COINCIDENT_NO_INTERSECT, null);
                }
            } else {
                isec = new LineIntersection(LineIntersection.Type.PARALLEL, null);
            }
        }
        return isec;
    }

    public ga_Line2D offsetAndGrowBy(float offset, float scale, final ga_Vector2 ref) {
        ga_Vector2 m = getMidPoint();
        ga_Vector2 d = getDirection();
        ga_Vector2 n = d.perpendicular();
        if (ref != null && m.sub(ref).dot(n) < 0) {
            n.invert();
        }
        n.norTo(offset);
        a.add(n);
        b.add(n);
        d.mul(scale);
        a.sub(d);
        b.add(d);
        return this;
    }

    public ga_Line2D scale(float scale) {
        float delta = (1 - scale) * 0.5f;
        ga_Vector2 newA = a.copy();
        a.interpolateTo(b, delta);
        b.interpolateTo(newA, delta);
        return this;
    }

    public ga_Line2D set(ga_Vector2 a, ga_Vector2 b) {
        this.a = a;
        this.b = b;
        return this;
    }

    public List<ga_Vector2> splitIntoSegments(List<ga_Vector2> segments,
            float stepLength, boolean addFirst) {
        return splitIntoSegments(a, b, stepLength, segments, addFirst);
    }

    public ga_Ray2D toRay2D() {
        return new ga_Ray2D(a.copy(), b.copy().sub(a).nor());
    }
}
