package com.software.reuze;

import com.software.reuze.ga_Vector2;
import com.software.reuze.m_MathUtils;


/*
 * Copyright (c) 2006-2011 Karsten Schmidt
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * http://creativecommons.org/licenses/LGPL/2.1/
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA
 */



/*import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;*/

//import toxi.geom.Line2D.LineIntersection.Type;
//import toxi.math.MathUtils;

//@XmlAccessorType(XmlAccessType.FIELD)
public class ga_Triangle2D {

    public static ga_Triangle2D createEquilateralFrom(final ga_Vector2 a, final ga_Vector2 b) {
        ga_Vector2 c = a.copy().interpolateTo(b, 0.5f);
        ga_Vector2 dir = a.tmp().sub(b);
        ga_Vector2 n = dir.tmp2().perpendicular();
        c.add(n.norTo(dir.len() * m_MathUtils.SQRT3 / 2));
        return new ga_Triangle2D(a, b, c);
    }

    public static boolean isClockwise(ga_Vector2 a, ga_Vector2 b, ga_Vector2 c) {
        float determ = (b.x - a.x) * (c.y - a.y) - (c.x - a.x) * (b.y - a.y);
        return (determ > 0.0);
    }

    //@XmlElement(required = true)
    public ga_Vector2 a, b, c;

    //@XmlTransient
    public ga_Vector2 centroid;

    public ga_Triangle2D() {
    }

    public ga_Triangle2D(final ga_Vector2 a, final ga_Vector2 b, final ga_Vector2 c) {
        this.a = a.copy();
        this.b = b.copy();
        this.c = c.copy();
    }

    public ga_Triangle2D adjustTriangleSizeBy(float offset) {
        return adjustTriangleSizeBy(offset, offset, offset);
    }

    public ga_Triangle2D adjustTriangleSizeBy(float offAB, float offBC, float offCA) {
        computeCentroid();
        ga_Line2D ab =
                new ga_Line2D(a.copy(), b.copy()).offsetAndGrowBy(offAB, 100000,
                        centroid);
        ga_Line2D bc =
                new ga_Line2D(b.copy(), c.copy()).offsetAndGrowBy(offBC, 100000,
                        centroid);
        ga_Line2D ca =
                new ga_Line2D(c.copy(), a.copy()).offsetAndGrowBy(offCA, 100000,
                        centroid);
        a = ab.intersectLine(ca).getPos();
        b = ab.intersectLine(bc).getPos();
        c = bc.intersectLine(ca).getPos();
        computeCentroid();
        return this;
    }

    public ga_Vector2 computeCentroid() {
        centroid = a.copy().add(b).add(c).mul(1f / 3f);
        return centroid;
    }

    /**
     * Checks if point vector is inside the triangle created by the points a, b
     * and c. These points will create a plane and the point checked will have
     * to be on this plane in the region between a,b,c.
     * 
     * Note: The triangle must be defined in clockwise order a,b,c
     * 
     * @return true, if point is in triangle.
     */
    public boolean contains(final ga_Vector2 p) {
        ga_Vector2 v1 = p.tmp().sub(a);
        ga_Vector2 v2 = p.tmp2().sub(b);
        ga_Vector2 v3 = p.copy().sub(c);
        if (v1.equals(ga_Vector2.ZERO) || v2.equals(ga_Vector2.ZERO) || v3.equals(ga_Vector2.ZERO)) {
            return true;
        }
        v1.nor();
        v2.nor();
        v3.nor();
        double total_angles = Math.acos(v1.dot(v2));
        total_angles += Math.acos(v2.dot(v3));
        total_angles += Math.acos(v3.dot(v1));
        return (m_MathUtils.abs(total_angles - m_MathUtils.TWO_PI) <= 0.001);
    }
    public boolean contains(float x, float y) {
    	float aa = (a.x - x)*(b.y - y) - (b.x - x)*(a.y - y);

        float bb = (b.x - x)*(c.y - y) - (c.x - x)*(b.y - y);

        float cc = (c.x - x)*(a.y - y) - (a.x - x)*(c.y - y);

        return (Math.signum(aa) == Math.signum(bb) && Math.signum(bb) == Math.signum(cc));
    }

    public ga_Triangle2D copy() {
        return new ga_Triangle2D(a.copy(), b.copy(), c.copy());
    }

    public ga_Triangle2D flipVertexOrder() {
        ga_Vector2 t = a;
        a = c;
        c = t;
        return this;
    }

    public ga_Vector2 fromBarycentric(final gb_Vector3 p) {
        return new ga_Vector2(a.x * p.x + b.x * p.y + c.x * p.z, a.y * p.x
                + b.y * p.y + c.y * p.z);
    }
    
    /**
     * Produces the barycentric coordinates of the given point within this
     * triangle. These coordinates can then be used to re-project the point into
     * a different triangle using its {@link #fromBarycentric(ReadonlyVec3D)}
     * method.
     * 
     * @param p
     *            point in world space
     * @return barycentric coords as {@link Vec3D}
     */
    public gb_Vector3 toBarycentric(ga_Vector2 p) {
        return new gb_Triangle(gb_Vector3.to3DXY(a), gb_Vector3.to3DXY(b), gb_Vector3.to3DXY(c))
                .toBarycentric(gb_Vector3.to3DXY(p));
    }

    public float getArea() {
        return b.tmp2().sub(a).crs(c.tmp().sub(a)) * 0.5f;
    }

    public ga_Rectangle getBounds() {
    	ga_Vector2 t=c.tmp2().max(a.tmp().max(b));
    	float w=c.x, h=c.y;
        return new ga_Rectangle(c.tmp().min(a.tmp2().min(b)), w, h);
    }

    public ga_Circle getCircumCircle() {
        gb_Vector3 cr = gb_Vector3.bisect(a,b).crs(gb_Vector3.bisect(b,c));
        ga_Vector2 circ = new ga_Vector2(cr.x / cr.z, cr.y / cr.z);
        float sa = a.dst(b);
        float sb = b.dst(c);
        float sc = c.dst(a);
        float radius =
                sa
                        * sb
                        * sc
                        / (float) Math.sqrt((sa + sb + sc) * (-sa + sb + sc)
                                * (sa - sb + sc) * (sa + sb - sc));
        return new ga_Circle(circ, radius);
    }

    public float getCircumference() {
        return a.dst(b) + b.dst(c) + c.dst(a);
    }

    /**
     * Finds and returns the closest point on any of the triangle edges to the
     * point given.
     * 
     * @param p
     *            point to check
     * @return closest point
     */
    public ga_Vector2 getClosestPointTo(final ga_Vector2 p) {
        ga_Line2D edge = new ga_Line2D(a, b);
        ga_Vector2 Rab = edge.closestPointTo(p);
        ga_Vector2 Rbc = edge.set(b, c).closestPointTo(p);
        ga_Vector2 Rca = edge.set(c, a).closestPointTo(p);

        float dAB = p.sub(Rab).len2();
        float dBC = p.sub(Rbc).len2();
        float dCA = p.sub(Rca).len2();

        float min = dAB;
        ga_Vector2 result = Rab;

        if (dBC < min) {
            min = dBC;
            result = Rbc;
        }
        if (dCA < min) {
            result = Rca;
        }

        return result;
    }

    public boolean intersects(ga_Triangle2D tri) {
        if (contains(tri.a) || contains(tri.b)
                || contains(tri.c)) {
            return true;
        }
        if (tri.contains(a) || tri.contains(b)
                || tri.contains(c)) {
            return true;
        }
        ga_Line2D[] ea =
                new ga_Line2D[] { new ga_Line2D(a, b), new ga_Line2D(b, c),
                        new ga_Line2D(c, a) };
        ga_Line2D[] eb =
                new ga_Line2D[] { new ga_Line2D(tri.a, tri.b),
                        new ga_Line2D(tri.b, tri.c), new ga_Line2D(tri.c, tri.a) };
        for (ga_Line2D la : ea) {
            for (ga_Line2D lb : eb) {
                ga_Line2D.LineIntersection.Type type = la.intersectLine(lb).getType();
                if (type != ga_Line2D.LineIntersection.Type.NON_INTERSECTING && type != ga_Line2D.LineIntersection.Type.PARALLEL) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isClockwise() {
        return ga_Triangle2D.isClockwise(a, b, c);
    }

    public void set(ga_Vector2 a2, ga_Vector2 b2, ga_Vector2 c2) {
        a = a2;
        b = b2;
        c = c2;
    }

    /**
     * Creates a {@link ga_Polygon} instance of the triangle. The vertices of
     * this polygon are disconnected from the ones defining this triangle.
     * 
     * @return triangle as polygon
     */
    public ga_Polygon toPolygon2D() {
        ga_Polygon poly = new ga_Polygon();
        poly.add(a.copy());
        poly.add(b.copy());
        poly.add(c.copy());
        poly.calcCenter();
        return poly;
    }

    public String toString() {
        return "Triangle2D: " + a + "," + b + "," + c;
    }
}
