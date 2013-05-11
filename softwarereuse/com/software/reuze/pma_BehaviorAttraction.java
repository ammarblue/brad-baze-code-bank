package com.software.reuze;
import java.util.List;


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

public class pma_BehaviorAttraction implements pma_i_BehaviorParticle {

    protected ga_Vector2 attractor;
    protected float attrStrength;

    protected float radius, radiusSquared;
    protected float strength;
    protected float jitter;
    protected float timeStep;

    public pma_BehaviorAttraction(ga_Vector2 attractor, float radius, float strength) {
        this(attractor, radius, strength, 0);
    }

    public pma_BehaviorAttraction(ga_Vector2 attractor, float radius, float strength,
            float jitter) {
        this.attractor = attractor;
        this.strength = strength;
        this.jitter = jitter;
        setRadius(radius);
    }

    public void apply(pma_ParticleVerlet p) {
    	ga_Vector2 delta = attractor.tmp().sub(p);
        float dist = delta.len2();
        if (dist < radiusSquared) {
        	delta.norTo((1.0f - dist / radiusSquared))
                    .jitter(jitter).mul(attrStrength);
            p.addForce(delta);
        }
    }

    public void configure(float timeStep) {
        this.timeStep = timeStep;
        setStrength(strength);
    }

    /**
     * @return the attractor
     */
    public ga_Vector2 getAttractor() {
        return attractor;
    }

    /**
     * @return the jitter
     */
    public float getJitter() {
        return jitter;
    }

    /**
     * @return the radius
     */
    public float getRadius() {
        return radius;
    }

    /**
     * @return the strength
     */
    public float getStrength() {
        return strength;
    }

    /**
     * @param attractor
     *            the attractor to set
     */
    public void setAttractor(ga_Vector2 attractor) {
        this.attractor = attractor;
    }

    /**
     * @param jitter
     *            the jitter to set
     */
    public void setJitter(float jitter) {
        this.jitter = jitter;
    }

    public void setRadius(float r) {
        this.radius = r;
        this.radiusSquared = r * r;
    }

    /**
     * @param strength
     *            the strength to set
     */
    public void setStrength(float strength) {
        this.strength = strength;
        this.attrStrength = strength * timeStep;
    }
    public void applyWithIndex(gb_SpatialIndex<ga_Vector2> spaceHash) {
        List<ga_Vector2> selection = spaceHash.itemsWithinRadius(attractor, radius);
        final ga_Vector2 temp = new ga_Vector2();
        if (selection != null) {
            for (ga_Vector2 p : selection) {
                temp.set(p);
                apply((pma_ParticleVerlet) p);
                spaceHash.reindex(temp, p);
            }
        }
    }
    public boolean supportsSpatialIndex() {
        return true;
    }
}
