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



/*import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import toxi.math.MathUtils;*/

//@XmlAccessorType(XmlAccessType.FIELD)
public class gb_Triangle {

	public static gb_Triangle createEquilateralFrom(gb_Vector3 a, gb_Vector3 b) {
        gb_Vector3 c = a.cpy().interpolate(b, 0.5f);
        gb_Vector3 dir = b.tmp().sub(a);
        gb_Vector3 n = a.tmp2().crs(dir.nor());
        c.add(n.norTo(dir.len() * m_MathUtils.SQRT3 / 2));
        return new gb_Triangle(a, b, c);
    }
	public static boolean isClockwiseInXY(final gb_Vector3 a, final gb_Vector3 b, final gb_Vector3 c) {
        float determ = (b.x - a.x) * (c.y - a.y) - (c.x - a.x) * (b.y - a.y);
        return (determ < 0.0);
    }

    public static boolean isClockwiseInXZ(final gb_Vector3 a, final gb_Vector3 b, final gb_Vector3 c) {
        float determ = (b.x - a.x) * (c.z - a.z) - (c.x - a.x) * (b.z - a.z);
        return (determ < 0.0);
    }

    public static boolean isClockwiseInYZ(final gb_Vector3 a, final gb_Vector3 b, final gb_Vector3 c) {
        float determ = (b.y - a.y) * (c.z - a.z) - (c.y - a.y) * (b.z - a.z);
        return (determ < 0.0);
    }

    //@XmlElement(required = true)
    public gb_Vector3 a, b, c;

    //@XmlElement(required = true)
    public gb_Vector3 normal;

    //@XmlTransient
    public gb_Vector3 centroid;

    public gb_Triangle() {
    }

    public gb_Triangle(final gb_Vector3 a, final gb_Vector3 b, final gb_Vector3 c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    /**
     * Computes the the point closest to the current vector on the surface of
     * triangle abc.
     * 
     * From Real-Time Collision Detection by Christer Ericson, published by
     * Morgan Kaufmann Publishers, Copyright 2005 Elsevier Inc
     * 
     * @return closest point on triangle (result may also be one of a, b or c)
     */
    public final gb_Vector3 closestPointOnSurface(final gb_Vector3 p) {
        final gb_Vector3 ab = b.cpy().sub(a);
        final gb_Vector3 ac = c.cpy().sub(a);
        final gb_Vector3 bc = c.cpy().sub(b);

        gb_Vector3 pa = p.cpy().sub(a);
        gb_Vector3 pb = p.cpy().sub(b);
        gb_Vector3 pc = p.cpy().sub(c);

        gb_Vector3 ap = a.cpy().sub(p);
        gb_Vector3 bp = b.cpy().sub(p);
        gb_Vector3 cp = c.cpy().sub(p);

        // Compute parametric position s for projection P' of P on AB,
        // P' = A + s*AB, s = snom/(snom+sdenom)
        float snom = pa.dot(ab);

        // Compute parametric position t for projection P' of P on AC,
        // P' = A + t*AC, s = tnom/(tnom+tdenom)
        float tnom = pa.dot(ac);

        if (snom <= 0.0f && tnom <= 0.0f) {
            return a; // Vertex region early out
        }
        
        float sdenom = pb.dot(a.cpy().sub(b));
        float tdenom = pc.dot(a.cpy().sub(c));

        // Compute parametric position u for projection P' of P on BC,
        // P' = B + u*BC, u = unom/(unom+udenom)
        float unom = pb.dot(bc);
        float udenom = pc.dot(b.cpy().sub(c));

        if (sdenom <= 0.0f && unom <= 0.0f) {
            return b; // Vertex region early out
        }
        if (tdenom <= 0.0f && udenom <= 0.0f) {
            return c; // Vertex region early out
        }

        // P is outside (or on) AB if the triple scalar product [N PA PB] <= 0
        gb_Vector3 n = ab.cpy().crs(ac);
        float vc = n.dot(ap.crs(bp));

        // If P outside AB and within feature region of AB,
        // return projection of P onto AB
        if (vc <= 0.0f && snom >= 0.0f && sdenom >= 0.0f) {
            // return a + snom / (snom + sdenom) * ab;
            return a.cpy().add(ab.mul(snom / (snom + sdenom)));
        }

        // P is outside (or on) BC if the triple scalar product [N PB PC] <= 0
        float va = n.dot(bp.crs(cp));
        // If P outside BC and within feature region of BC,
        // return projection of P onto BC
        if (va <= 0.0f && unom >= 0.0f && udenom >= 0.0f) {
            // return b + unom / (unom + udenom) * bc;
            return b.cpy().add(bc.mul(unom / (unom + udenom)));
        }

        // P is outside (or on) CA if the triple scalar product [N PC PA] <= 0
        float vb = n.dot(cp.crs(ap));
        // If P outside CA and within feature region of CA,
        // return projection of P onto CA
        if (vb <= 0.0f && tnom >= 0.0f && tdenom >= 0.0f) {
            // return a + tnom / (tnom + tdenom) * ac;
            return a.cpy().add(ac.mul(tnom / (tnom + tdenom)));
        }

        // P must project inside face region. Compute Q using barycentric
        // coordinates
        float u = va / (va + vb + vc);
        float v = vb / (va + vb + vc);
        float w = 1.0f - u - v; // = vc / (va + vb + vc)
        // return u * a + v * b + w * c;
        return a.cpy().mul(u).add(b.tmp2().mul(v)).add(c.tmp().mul(w));
    }

    public gb_Vector3 computeCentroid() {
        centroid = a.cpy().add(b).add(c).mul(1f / 3);
        return centroid;
    }

    public gb_Vector3 computeNormal() {
        normal = a.cpy().sub(c).crs(a.tmp().sub(b)).nor();
        return normal;
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
    public boolean contains(final gb_Vector3 p) {
        if (p.equals(a) || p.equals(b) || p.equals(c)) {
            return true;
        }
        gb_Vector3 v1 = p.tmp().sub(a).nor();
        gb_Vector3 v2 = p.tmp2().sub(b).nor();
        gb_Vector3 v3 = p.tmp3().sub(c).nor();

        double total_angles = Math.acos(v1.dot(v2));
        total_angles += Math.acos(v2.dot(v3));
        total_angles += Math.acos(v3.dot(v1));

        return (m_MathUtils.abs((float) total_angles - m_MathUtils.TWO_PI) <= 0.005f);
    }

    public gb_Triangle flipVertexOrder() {
        gb_Vector3 t = a;
        a = c;
        c = t;
        return this;
    }

    public gb_Vector3 fromBarycentric(final gb_Vector3 p) {
        return new gb_Vector3(a.x * p.x + b.x * p.y + c.x * p.z, a.y * p.x
                + b.y * p.y + c.y * p.z, a.z * p.x + b.z * p.y + c.z
                * p.z);
    }

    public gb_AABB3 getBoundingBox() {
        gb_Vector3 min = c.cpy().min(a.tmp().min(b));
        gb_Vector3 max = c.cpy().max(a.tmp().max(b));
        return gb_AABB3.fromMinMax(min, max);
    }

    /**
     * Finds and returns the closest point on any of the triangle edges to the
     * point given.
     * 
     * @param p
     *            point to check
     * @return closest point
     */

    public gb_Vector3 getClosestPointTo(final gb_Vector3 p) {
        gb_Line3D edge = new gb_Line3D(a, b);
        gb_Vector3 Rab = edge.closestPointTo(p);
        gb_Vector3 Rbc = edge.set(b, c).closestPointTo(p);
        gb_Vector3 Rca = edge.set(c, a).closestPointTo(p);

        final float dAB = p.tmp().sub(Rab).len2();
        final float dBC = p.tmp().sub(Rbc).len2();
        final float dCA = p.tmp().sub(Rca).len2();

        float min = dAB;
        gb_Vector3 result = Rab;

        if (dBC < min) {
            min = dBC;
            result = Rbc;
        }
        if (dCA < min) {
            result = Rca;
        }

        return result;
    }

    public boolean isClockwiseInXY() {
        return gb_Triangle.isClockwiseInXY(a, b, c);
    }

    public boolean isClockwiseInXZ() {
        return gb_Triangle.isClockwiseInXY(a, b, c);
    }

    public boolean isClockwiseInYZ() {
        return gb_Triangle.isClockwiseInXY(a, b, c);
    }

    public void set(final gb_Vector3 a2, final gb_Vector3 b2, final gb_Vector3 c2) {
        a = a2;
        b = b2;
        c = c2;
    }

    public final gb_Vector3 toBarycentric(final gb_Vector3 p) {
        gb_Vector3 e = b.tmp2().sub(a).crs(c.tmp().sub(a));
        gb_Vector3 n = e.cpy().nor();

        // Compute twice area of triangle ABC
        float areaABC = n.dot(e);
        // Compute lambda1
        float areaPBC = n.dot(b.tmp3().sub(p).crs(c.tmp().sub(p)));
        float l1 = areaPBC / areaABC;

        // Compute lambda2
        float areaPCA = n.dot(c.tmp3().sub(p).crs(a.tmp().sub(p)));
        float l2 = areaPCA / areaABC;

        // Compute lambda3
        float l3 = 1.0f - l1 - l2;

        return new gb_Vector3(l1, l2, l3);
        // return new final Vector3(a.x * l1 + b.x * l2 + c.x * l3, a.y * l1 + b.y * l2
        // + c.y * l3, a.z * l1 + b.z * l2 + c.z * l3);
    }

    public String toString() {
        return "Triangle3D: " + a + "," + b + "," + c;
    }
}
