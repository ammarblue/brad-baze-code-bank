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
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */



//import toxi.geom.mesh.Mesh3D;
//import toxi.geom.mesh.TriangleMesh;
//import toxi.math.MathUtils;

/**
 * A geometric definition of a cone (and cylinder as a special case) with
 * support for mesh creation/representation. The class is currently still
 * incomplete in that it doesn't provide any other features than the
 * construction of a cone shaped mesh.
 */
public class gb_Cone  {

    public gb_Vector3 position, dir;
    public float radiusSouth;
    public float radiusNorth;
    public float length;

    /**
     * Constructs a new cone instance.
     * 
     * @param pos
     *            center position
     * @param dir
     *            direction
     * @param rNorth
     *            radius on the side in the forward direction
     * @param rSouth
     *            radius on the side in the opposite direction
     * @param len
     *            length of the cone
     */
    public gb_Cone(gb_Vector3 pos, gb_Vector3 dir, float rNorth,
            float rSouth, float len) {
        position=pos;
        this.dir = dir.nor();
        this.radiusNorth = rNorth;
        this.radiusSouth = rSouth;
        this.length = len;
    }

    public gb_i_Mesh toMesh(int steps) {
        return toMesh(steps, 0);
    }

    public gb_i_Mesh toMesh(int steps, float thetaOffset) {
        return toMesh(null, steps, thetaOffset, true, true);
    }

    public gb_i_Mesh toMesh(gb_i_Mesh mesh, int steps, float thetaOffset,
            boolean topClosed, boolean bottomClosed) {
        gb_Vector3 c = position.tmp().add(0.01f, 0.01f, 0.01f);
        gb_Vector3 n = c.cpy().crs(dir.tmp2().nor()).nor();
        gb_Vector3 halfAxis = dir.tmp().mul(length * 0.5f);
        gb_Vector3 p = position.cpy().sub(halfAxis);
        gb_Vector3 q = position.cpy().add(halfAxis);
        gb_Vector3[] south = new gb_Vector3[steps];
        gb_Vector3[] north = new gb_Vector3[steps];
        float phi = m_MathUtils.TWO_PI / steps;
        for (int i = 0; i < steps; i++) {
            float theta = i * phi + thetaOffset;
            gb_Vector3 nr = n.tmp().rotateAroundAxis(dir, theta);
            south[i] = nr.cpy().mul(radiusSouth).add(p);
            north[i] = nr.cpy().mul(radiusNorth).add(q);
        }
        int numV = steps * 2 + 2;
        int numF =
                steps * 2 + (topClosed ? steps : 0)
                        + (bottomClosed ? steps : 0);
        if (mesh == null) {
            mesh = new gb_TriangleMesh("cone", numV, numF);
        }
        for (int i = 0, j = 1; i < steps; i++, j++) {
            if (j == steps) {
                j = 0;
            }
            mesh.addFace(south[i], north[i], south[j], null, null, null, null);
            mesh.addFace(south[j], north[i], north[j], null, null, null, null);
            if (bottomClosed) {
                mesh.addFace(p, south[i], south[j], null, null, null, null);
            }
            if (topClosed) {
                mesh.addFace(north[i], q, north[j], null, null, null, null);
            }
        }
        return mesh;
    }
}