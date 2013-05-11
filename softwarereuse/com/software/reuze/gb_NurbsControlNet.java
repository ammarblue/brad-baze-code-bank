package com.software.reuze;

/*
 * jgeom: Geometry Library for Java
 * 
 * Copyright (C) 2005  Samuel Gerber
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

/**
 * A ControlNet for a NurbsSurface
 * 
 * @author sg
 * @version 1.0
 */
public class gb_NurbsControlNet {

    private int nU, nV;
    private gc_Vector4[][] cps;

    /**
     * Create a ControlNet from the given points the two dimensional array must
     * be a Matrix else an IllegalArgumentException is thrown.
     * 
     * @param cpnet
     *            "Matrix" of ControlPoints
     */
    public gb_NurbsControlNet(gc_Vector4[][] cpnet) throws IllegalArgumentException {
        if (cpnet.length < 1) {
            throw new IllegalArgumentException(
                    "Nurbs is not a Surface, to few ControlPoints in u Direction");
        }
        if (cpnet[0].length < 1) {
            throw new IllegalArgumentException(
                    "Nurbs is not a Surface, to few ControlPoints in v Direction");
        }
        for (int i = 1; i < cpnet.length; i++) {
            if (cpnet[i].length != cpnet[i - 1].length) {
                throw new IllegalArgumentException(
                        "ControlPoint net is not a Matrix");
            }
        }

        nU = cpnet.length;
        nV = cpnet[0].length;
        cps = cpnet;
    }

    public void center(gb_Vector3 origin) {
    	gb_Vector3 centroid = computeCentroid();
    	gb_Vector3 delta = origin != null ? origin.tmp().sub(centroid) : centroid.tmp2().inv();
                //.getInverted();
        for (int i = 0; i < nU; i++) {
            for (int j = 0; j < nV; j++) {
                cps[i][j].addXYZSelf(delta);
            }
        }
    }

    public gb_Vector3 computeCentroid() {
        gc_Vector4 centroid = new gc_Vector4();
        for (int i = 0; i < nU; i++) {
            for (int j = 0; j < nV; j++) {
                centroid.addSelf(cps[i][j]);
            }
        }
        return centroid.scaleSelf(1f / (nU * nV)).to3D();
    }

    /**
     * Get the ControlPoint at the position u,v
     * 
     * @param u
     *            index in u direction
     * @param v
     *            index in v direction
     * @return The by u and v indexed ControlPoint
     */
    public gc_Vector4 get(int u, int v) {
        return cps[u][v];
    }

    /**
     * Get all the control points
     * 
     * @return 2D array
     */
    public gc_Vector4[][] getControlPoints() {
        return cps;
    }

    /**
     * Set the ControlPoint at the position u,v
     * 
     * @param u
     *            index in u direction
     * @param v
     *            index in v direction
     * @param cp
     *            ControlPoint to set at the indexed position
     */
    public void set(int u, int v, gc_Vector4 cp) {
        cps[u][v].set(cp);
    }

    /**
     * Get number of ControlPoints in u direction
     * 
     * @return number of ControlPoints in u direction
     */
    public int uLength() {
        return nU;
    }

    /**
     * Get number of ControlPoints in v direction
     * 
     * @return number of ControlPoints in v direction
     */
    public int vLength() {
        return nV;
    }

}
