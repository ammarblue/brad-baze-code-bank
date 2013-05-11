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

import java.util.ArrayList;
import java.util.List;


/**
 * Utility builder/grouping/management class to connect a set of particles into
 * a physical string/thread. Custom spring types can be used by sub-classing this
 * class and overwriting the
 * {@link #createSpring(pma_ParticleVerlet, pma_ParticleVerlet, float, float)}
 * method.
 */
public class pma_ParticleString {

    protected pma_PhysicsVerlet physics;
    public List<pma_ParticleVerlet> particles;
    public List<pma_Spring> links;

    /**
     * Takes a list of already created particles connects them into a continuous
     * string using springs.
     * 
     * @param physics
     *            physics engine instance
     * @param plist
     *            particle list
     * @param strength
     *            spring strength
     */
    public pma_ParticleString(pma_PhysicsVerlet physics,
            List<pma_ParticleVerlet> plist, float strength) {
        this.physics = physics;
        particles = new ArrayList<pma_ParticleVerlet>(plist);
        links = new ArrayList<pma_Spring>(particles.size() - 1);
        pma_ParticleVerlet prev = null;
        for (pma_ParticleVerlet p : particles) {
            physics.addParticle(p);
            if (prev != null) {
                pma_Spring s = createSpring(prev, p, prev.dst(p),
                        strength);
                links.add(s);
                physics.addSpring(s);
            }
            prev = p;
        }
    }

    /**
     * Creates a number of particles along a line and connects them into a
     * string using springs.
     * 
     * @param physics
     *            physics engine
     * @param pos
     *            start position
     * @param step
     *            step direction & distance between successive particles
     * @param num
     *            number of particles
     * @param mass
     *            particle mass
     * @param strength
     *            spring strength
     */
    public pma_ParticleString(pma_PhysicsVerlet physics, ga_Vector2 pos, ga_Vector2 step,
            int num, float mass, float strength) {
        this.physics = physics;
        particles = new ArrayList<pma_ParticleVerlet>(num);
        links = new ArrayList<pma_Spring>(num - 1);
        float len = step.len();
        pma_ParticleVerlet prev = null;
        pos = pos.copy();
        for (int i = 0; i < num; i++) {
            pma_ParticleVerlet p = new pma_ParticleVerlet(pos.copy(), mass);
            particles.add(p);
            physics.particles.add(p);
            if (prev != null) {
                pma_Spring s = createSpring(prev, p, len, strength);
                links.add(s);
                physics.addSpring(s);
            }
            prev = p;
            pos.add(step);
        }
    }

    /**
     * Removes the entire string from the physics simulation, incl. all of its
     * particles & springs.
     */
    public void clear() {
        for (pma_Spring s : links) {
            physics.removeSpringElements(s);
        }
        particles.clear();
        links.clear();
    }

    /**
     * Creates a spring instance connecting 2 successive particles of the
     * string. Overwrite this method to create a string custom spring types
     * (subclassed from {@link VerletSpring3D}).
     * 
     * @param a
     *            1st particle
     * @param b
     *            2nd particle
     * @param len
     *            rest length
     * @param strength
     * @return spring
     */
    protected pma_Spring createSpring(pma_ParticleVerlet a,
            pma_ParticleVerlet b, float len, float strength) {
        return new pma_Spring(a, b, len, strength);
    }

    /**
     * Returns the first particle of the string.
     * 
     * @return first particle
     */
    public pma_ParticleVerlet getHead() {
        return particles.get(0);
    }

    /**
     * Returns number of particles of the string.
     * 
     * @return particle count
     */
    public int getNumParticles() {
        return particles.size();
    }

    /**
     * Returns last particle of the string.
     * 
     * @return last particle
     */
    public pma_ParticleVerlet getTail() {
        return particles.get(particles.size() - 1);
    }
    
    public List<pma_ParticleVerlet> getParticles() {
		return particles;
	}
}
