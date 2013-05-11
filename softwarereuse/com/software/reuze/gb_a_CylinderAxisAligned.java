package com.software.reuze;
import com.software.reuze.gb_Cone;

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
public abstract class gb_a_CylinderAxisAligned {

    protected gb_Vector3 pos;
    protected float radius;
    protected float radiusSquared;
    protected float length;

    protected gb_a_CylinderAxisAligned(gb_Vector3 pos, float radius, float length) {
        this.pos = pos.cpy();
        setRadius(radius);
        setLength(length);
    }

    /**
     * Checks if the given point is inside the cylinder.
     * 
     * @param p
     * @return true, if inside
     */
    public abstract boolean containsPoint(gb_Vector3 p);

    /**
     * @return the length
     */
    public float getLength() {
        return length;
    }

    /**
     * @return the cylinder's orientation axis
     */
    public abstract gb_Vector3 getMajorAxis();

    /**
     * Returns the cylinder's position (centroid).
     * 
     * @return the pos
     */
    public gb_Vector3 getPosition() {
        return pos.cpy();
    }

    /**
     * @return the cylinder radius
     */
    public float getRadius() {
        return radius;
    }

    /**
     * @param length
     *            the length to set
     */
    public void setLength(float length) {
        this.length = length;
    }

    /**
     * @param pos
     *            the pos to set
     */
    public void setPosition(gb_Vector3 pos) {
        this.pos.set(pos);
    }

    /**
     * @param radius
     */
    public void setRadius(float radius) {
        this.radius = radius;
        this.radiusSquared = radius * radius;
    }

    /**
     * Builds a TriangleMesh representation of the cylinder at a default
     * resolution 30 degrees.
     * 
     * @return mesh instance
     */
    public gb_i_Mesh toMesh() {
        return toMesh(12, 0);
    }

    /**
     * Builds a TriangleMesh representation of the cylinder using the given
     * number of steps and start angle offset.
     * 
     * @param steps
     * @param thetaOffset
     * @return mesh
     */
    public gb_i_Mesh toMesh(int steps, float thetaOffset) {
        return toMesh(null, steps, thetaOffset);
    }

    public gb_i_Mesh toMesh(gb_i_Mesh mesh, int steps, float thetaOffset) {
        return new gb_Cone(pos, getMajorAxis(), radius, radius, length)
                .toMesh(mesh, steps, thetaOffset, true, true);
    }
}