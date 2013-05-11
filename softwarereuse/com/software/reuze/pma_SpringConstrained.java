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
 * Implements a spring whose maximum relaxation distance at every time step can
 * be limited to achieve better (if physically incorrect) stability of the whole
 * spring system.
 */
public class pma_SpringConstrained extends pma_Spring {

    /**
     * Maximum relaxation distance for either end of the spring in world units
     * (by default unlimited until set by user)
     */
    public float limit = Float.MAX_VALUE;

    /**
     * @param a
     * @param b
     * @param len
     * @param str
     */
    public pma_SpringConstrained(pma_ParticleVerlet a, pma_ParticleVerlet b,
            float len, float str) {
        super(a, b, len, str);
    }

    /**
     * @param a
     * @param b
     * @param len
     * @param str
     * @param limit
     */
    public pma_SpringConstrained(pma_ParticleVerlet a, pma_ParticleVerlet b,
            float len, float str, float limit) {
        super(a, b, len, str);
        this.limit = limit;
    }

    protected void update(boolean applyConstraints) {
        ga_Vector2 delta = b.tmp().sub(a);
        // add minute offset to avoid div-by-zero errors
        float dist = delta.len() + EPS;
        float normDistStrength = (dist - restLength)
                / (dist * (a.invWeight + b.invWeight)) * strength;
        if (!a.isLocked && !isALocked) {
            a.add(delta.tmp2().mul(normDistStrength * a.invWeight).limit(limit));
            if (applyConstraints) {
                a.applyConstraints();
            }
        }
        if (!b.isLocked && !isBLocked) {
            b.sub(delta.mul(normDistStrength * b.invWeight).limit(limit));
            if (applyConstraints) {
                b.applyConstraints();
            }
        }
    }
}
