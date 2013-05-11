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



//import toxi.math.MathUtils;

public class gb_TriangleIntersector implements gb_i_Intersector {

    public gb_Triangle triangle;
    private gb_IntersectionData isectData;

    public gb_TriangleIntersector() {
        this(new gb_Triangle());
    }

    public gb_TriangleIntersector(gb_Triangle t) {
        this.triangle = t;
        this.isectData = new gb_IntersectionData();
    }

    public gb_IntersectionData getIntersectionData() {
        return isectData;
    }

    /**
     * @return the triangle
     */
    public gb_Triangle getTriangle() {
        return triangle;
    }

    public boolean intersectsRay(gb_Ray ray) {
        isectData.isIntersection = false;
        gb_Vector3 n = triangle.computeNormal();
        float dotprod = n.dot(ray.direction);
        if (dotprod < 0) {
            gb_Vector3 rt = ray.origin.tmp().sub(triangle.a);
            float t =
                    -(n.x * rt.x + n.y * rt.y + n.z * rt.z)
                            / (n.x * ray.direction.x + n.y * ray.direction.y + n.z
                                    * ray.direction.z);
            if (t >= m_MathUtils.EPS) {
                gb_Vector3 pos = ray.getPointAtDistance(t);
                // TODO commented out orientation check since it seems
                // unnecessary and i can't remember why I used it for the Audi
                // project, needs more testing
                // if (isSameClockDir(triangle.a, triangle.b, pos, n)) {
                // if (isSameClockDir(triangle.b, triangle.c, pos, n)) {
                // if (isSameClockDir(triangle.c, triangle.a, pos, n)) {
                isectData.isIntersection = true;
                isectData.pos = pos;
                isectData.normal = n;
                isectData.dist = t;
                isectData.dir = isectData.pos.cpy().sub(ray.origin).nor();
                // }
                // }
                // }
            }
        }
        return isectData.isIntersection;
    }

    protected boolean isSameClockDir(gb_Vector3 a, gb_Vector3 b, gb_Vector3 c, gb_Vector3 norm) {
        float nx, ny, nz;
        nx = ((b.y - a.y) * (c.z - a.z)) - ((c.y - a.y) * (b.z - a.z));
        ny = ((b.z - a.z) * (c.x - a.x)) - ((c.z - a.z) * (b.x - a.x));
        nz = ((b.x - a.x) * (c.y - a.y)) - ((c.x - a.x) * (b.y - a.y));
        float dotprod = nx * norm.x + ny * norm.y + nz * norm.z;
        return dotprod < 0;
    }

    /**
     * @param tri
     *            the triangle to set
     */
    public gb_TriangleIntersector setTriangle(gb_Triangle tri) {
        this.triangle = tri;
        return this;
    }
}
