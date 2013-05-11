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



/*import toxi.geom.Triangle3D;
import toxi.geom.Vector2;
import toxi.geom.Vector3;*/

public class gb_TriangleFace {

    public gb_Vector3Id a, b, c;
    public ga_Vector2 uvA, uvB, uvC;
    public gb_Vector3 normal;

    public gb_TriangleFace(gb_Vector3Id a, gb_Vector3Id b, gb_Vector3Id c) {
        this.a = a;
        this.b = b;
        this.c = c;
        normal = a.cpy().sub(c).crs(a.tmp().sub(b)).nor();
        a.addFaceNormal(normal);
        b.addFaceNormal(normal);
        c.addFaceNormal(normal);
    }

    public gb_TriangleFace(gb_Vector3Id a, gb_Vector3Id b, gb_Vector3Id c, ga_Vector2 uvA, ga_Vector2 uvB, ga_Vector2 uvC) {
        this(a, b, c);
        this.uvA = uvA;
        this.uvB = uvB;
        this.uvC = uvC;
    }

    public void computeNormal() {
        normal.set(a).sub(c).crs(a.tmp().sub(b)).nor();
    }

    public void flipVertexOrder() {
        gb_Vector3Id t = a;
        a = b;
        b = t;
        normal.inv();
    }

    public gb_Vector3 getCentroid() {
        return a.cpy().add(b).add(c).mul(1f / 3f);
    }

    public final gb_Vector3Id[] getVertices(gb_Vector3Id[] verts) {
        if (verts != null) {
            verts[0] = a;
            verts[1] = b;
            verts[2] = c;
        } else {
            verts = new gb_Vector3Id[] { a, b, c };
        }
        return verts;
    }

    public String toString() {
        return getClass().getName() + " " + a + ", " + b + ", " + c;
    }

    /**
     * Creates a generic {@link gb_Triangle} instance using this face's vertices.
     * The new instance is made up of copies of the original vertices and
     * manipulating them will not impact the originals.
     * 
     * @return triangle copy of this mesh face
     */
    public gb_Triangle toTriangle() {
        return new gb_Triangle(a.cpy(), b.cpy(), c.cpy());
    }
}