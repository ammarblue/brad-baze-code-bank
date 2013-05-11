package com.software.reuze;

/*
 *   __               .__       .__  ._____.           
 * _/  |_  _______  __|__| ____ |  | |__\_ |__   ______
 * \   __\/  _ \  \/  /  |/ ___\|  | |  || __ \ /  ___/
 *  |  | (  <_> >    <|  \  \___|  |_|  || \_\ \\___ \ 
 *  |__|  \____/__/\_ \__|\___  >____/__||___  /____  >
 *                   \/       \/             \/     \/ 
 *
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

/**
 * This class defines an origin and set of axis vectors for a 3D cartesian
 * coordinate system.
 */
public class gb_Origin3D {

    public gb_Vector3 origin;
    public gb_Vector3 xAxis, yAxis, zAxis;

    /**
     * Creates a new origin at the world origin using the standard XYZ axes
     */
    public gb_Origin3D() {
        this(new gb_Vector3());
    }

    public gb_Origin3D(float x, float y, float z) {
        this(new gb_Vector3(x, y, z));
    }

    public gb_Origin3D(m_Matrix4 mat) {
        this.origin = mat.applyTo(new gb_Vector3());
        this.xAxis = new gb_Vector3(mat.applyTo(gb_Vector3.X.tmp()).sub(origin).nor());
        this.yAxis = new gb_Vector3(mat.applyTo(gb_Vector3.Y.tmp()).sub(origin).nor());
        zAxis = new gb_Vector3(xAxis).crs(yAxis);
    }

    /**
     * Creates a new origin at the given origin using the standard XYZ axes
     * 
     * @param o
     *            origin
     */
    public gb_Origin3D(gb_Vector3 o) {
        origin = o;
        xAxis = gb_Vector3.X;
        yAxis = gb_Vector3.Y;
        zAxis = gb_Vector3.Z;
    }

    /**
     * Attempts to create a cartesian coordinate system with the given point as
     * its origin and the direction as its Z-axis. In cases when two of the
     * direction vector components are equal, the constructor will throw an
     * {@link IllegalArgumentException}.
     * 
     * @param o
     *            origin of the coordinate system
     * @param dir
     *            z-axis
     */
    public gb_Origin3D(gb_Vector3 o, gb_Vector3 dir) {
        this.origin = o;
        this.zAxis = new gb_Vector3(dir.tmp().nor());
        gb_Vector3 av = null;
        gb_Vector3 a = zAxis.getClosestAxis();
        if (a == gb_Vector3.X) {
            av = gb_Vector3.Z;
        } else if (a == gb_Vector3.Y) {
            av = gb_Vector3.Z;
        } else if (a == gb_Vector3.Z) {
            av = gb_Vector3.X;
        }
        if (av == null) {
            throw new IllegalArgumentException(
                    "can't create a coordinate system for direction: " + dir);
        }
        xAxis = new gb_Vector3(av).inv().crs(dir).nor();
        yAxis = new gb_Vector3(xAxis.tmp().crs(zAxis).nor());

    }

    /**
     * @param o
     *            origin of the coordinate system
     * @param x
     *            x-direction of the coordinate system
     * @param y
     *            y-direction of the coordinate system
     * @throws IllegalArgumentException
     *             if x and y vectors are not orthogonal
     */
    public gb_Origin3D(gb_Vector3 o, gb_Vector3 x, gb_Vector3 y) throws IllegalArgumentException {
        origin = o;
        xAxis = x;
        yAxis = y;
        xAxis.nor();
        yAxis.nor();
        if (Math.abs(xAxis.dot(yAxis)) > 0.0001) {
            throw new IllegalArgumentException("Axis vectors aren't orthogonal");
        }
        zAxis = new gb_Vector3(xAxis.tmp().crs(yAxis));
    }
}
