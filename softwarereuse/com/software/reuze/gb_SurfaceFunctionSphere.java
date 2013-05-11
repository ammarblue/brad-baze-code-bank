package com.software.reuze;
import com.software.reuze.gb_SurfaceMeshBuilder;
import com.software.reuze.gb_i_SurfaceFunction;

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
/**
 * This implementation of a {@link SurfaceFunction} samples a given
 * {@link gb_Sphere} instance when called by the {@link gb_SurfaceMeshBuilder}.
 */
public class gb_SurfaceFunctionSphere implements gb_i_SurfaceFunction {

    public gb_Sphere sphere;

    protected float phiRange = m_MathUtils.PI;
    protected float thetaRange = m_MathUtils.TWO_PI;

    public gb_SurfaceFunctionSphere() {
        this(1);
    }

    /**
     * Creates a new instance using a sphere of the given radius, located at the
     * world origin.
     * 
     * @param radius
     */
    public gb_SurfaceFunctionSphere(float radius) {
        this(new gb_Sphere(new gb_Vector3(), radius));
    }

    /**
     * Creates a new instance using the given sphere
     * 
     * @param s
     *            sphere
     */
    public gb_SurfaceFunctionSphere(gb_Sphere s) {
        this.sphere = s;
    }

    public gb_Vector3 computeVertexFor(gb_Vector3 p, float phi, float theta) {
        phi -= m_MathUtils.HALF_PI;
        float cosPhi = m_MathUtils.cos(phi);
        float cosTheta = m_MathUtils.cos(theta);
        float sinPhi = m_MathUtils.sin(phi);
        float sinTheta = m_MathUtils.sin(theta);
        float t = m_MathUtils.sign(cosPhi) * m_MathUtils.abs(cosPhi);
        p.x = t * m_MathUtils.sign(cosTheta) * m_MathUtils.abs(cosTheta);
        p.y = m_MathUtils.sign(sinPhi) * m_MathUtils.abs(sinPhi);
        p.z = t * m_MathUtils.sign(sinTheta) * m_MathUtils.abs(sinTheta);
        return p.mul(sphere.radius).add(sphere.center);
    }

    public float getPhiRange() {
        return phiRange;
    }

    public int getPhiResolutionLimit(int res) {
        return res;
    }

    public float getThetaRange() {
        return thetaRange;
    }

    public int getThetaResolutionLimit(int res) {
        return res;
    }

    public void setMaxPhi(float max) {
        phiRange = m_MathUtils.min(max / 2, m_MathUtils.PI);
    }

    public void setMaxTheta(float max) {
        thetaRange = m_MathUtils.min(max, m_MathUtils.TWO_PI);
    }
}
