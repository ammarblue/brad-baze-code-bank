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
 * Super ellipsoid surface evaluator based on code by Paul Bourke:
 * http://local.wasp.uwa.edu.au/~pbourke/geometry/superellipse/
 */
public class gb_SuperEllipsoid implements gb_i_SurfaceFunction {

    private float p1;
    private float p2;

    public gb_SuperEllipsoid(float n1, float n2) {
        this.p1 = n1;
        this.p2 = n2;
    }

    public gb_Vector3 computeVertexFor(gb_Vector3 p, float phi, float theta) {
        phi -= m_MathUtils.HALF_PI;
        float cosPhi = m_MathUtils.cos(phi);
        float cosTheta = m_MathUtils.cos(theta);
        float sinPhi = m_MathUtils.sin(phi);
        float sinTheta = m_MathUtils.sin(theta);

        float t = m_MathUtils.sign(cosPhi)
                * (float) Math.pow(m_MathUtils.abs(cosPhi), p1);
        p.x = t * m_MathUtils.sign(cosTheta)
                * (float) Math.pow(m_MathUtils.abs(cosTheta), p2);
        p.y = m_MathUtils.sign(sinPhi)
                * (float) Math.pow(m_MathUtils.abs(sinPhi), p1);
        p.z = t * m_MathUtils.sign(sinTheta)
                * (float) Math.pow(m_MathUtils.abs(sinTheta), p2);
        return p;
    }

    public float getPhiRange() {
        return m_MathUtils.TWO_PI;
    }

    public int getPhiResolutionLimit(int res) {
        return res / 2;
    }

    public float getThetaRange() {
        return m_MathUtils.TWO_PI;
    }

    public int getThetaResolutionLimit(int res) {
        return res;
    }
}
