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

public class pma_ConstraintAngular implements pma_i_ConstraintParticle {

    public ga_Vector2 rootPos;
    public float theta;

    public pma_ConstraintAngular(float theta) {
        this.theta = theta;
    }

    public pma_ConstraintAngular(int theta) {
        this.theta = m_MathUtils.radians(theta);
    }

    public pma_ConstraintAngular(ga_Vector2 p, int theta) {
        rootPos = new ga_Vector2(p);
        this.theta = m_MathUtils.radians(theta);
    }

    public void apply(pma_ParticleVerlet p) {
    	ga_Vector2 delta = p.tmp2().sub(rootPos);
        float heading = m_MathUtils.floor(delta.heading() / theta) * theta;
        p.set(rootPos.tmp().add(ga_Vector2.fromTheta(heading).mul(delta.len())));
    }

}
