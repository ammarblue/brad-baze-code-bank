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
import java.util.Iterator;
import java.util.List;


/*import toxi.geom.Rect;
import toxi.geom.Vec2D;
import toxi.physics2d.behaviors.GravityBehavior2D;
import toxi.physics2d.behaviors.ParticleBehavior2D;
import toxi.physics2d.constraints.ParticleConstraint2D;*/

/**
 * 3D particle physics engine using Verlet integration based on:
 * http://en.wikipedia.org/wiki/Verlet_integration
 * http://www.teknikus.dk/tj/gdc2001.htm
 * 
 */
public class pma_PhysicsVerlet {

    public static void addConstraintToAll(pma_i_ConstraintParticle c,
            List<pma_ParticleVerlet> list) {
        for (pma_ParticleVerlet p : list) {
            p.addConstraint(c);
        }
    }

    public static void removeConstraintFromAll(pma_i_ConstraintParticle c,
            List<pma_ParticleVerlet> list) {
        for (pma_ParticleVerlet p : list) {
            p.removeConstraint(c);
        }
    }

    /**
     * List of particles
     */
    public ArrayList<pma_ParticleVerlet> particles;

    /**
     * List of spring/stick connectors
     */
    public ArrayList<pma_Spring> springs;

    /**
     * Default time step = 1.0
     */
    protected float timeStep;

    /**
     * Default iterations for verlet solver = 50
     */
    protected int numIterations;

    /**
     * Optional bounding rect to constrain particles too
     */
    protected ga_Rectangle worldBounds;

    public final List<pma_i_BehaviorParticle> behaviors = new ArrayList<pma_i_BehaviorParticle>(
            1);

    public final List<pma_i_ConstraintParticle> constraints = new ArrayList<pma_i_ConstraintParticle>(
            1);

    protected float drag;
    
    protected gb_SpatialIndex<ga_Vector2> index;
    
    /**
     * Initializes a Verlet engine instance using the default values.
     */
    public pma_PhysicsVerlet() {
        this(null, 50, 0, 1);
    }

    /**
     * Initializes an Verlet engine instance with the passed in configuration.
     * 
     * @param gravity
     *            3D gravity vector
     * @param numIterations
     *            iterations per time step for verlet solver
     * @param drag
     *            drag value 0...1
     * @param timeStep
     *            time step for calculating forces
     */
    public pma_PhysicsVerlet(ga_Vector2 gravity, int numIterations, float drag,
            float timeStep) {
        particles = new ArrayList<pma_ParticleVerlet>();
        springs = new ArrayList<pma_Spring>();
        this.numIterations = numIterations;
        this.timeStep = timeStep;
        setDrag(drag);
        if (gravity != null) {
            addBehavior(new pma_BehaviorForceGravity(gravity));
        }
    }

    public void addBehavior(pma_i_BehaviorParticle behavior) {
        behavior.configure(timeStep);
        behaviors.add(behavior);
    }

    public void addConstraint(pma_i_ConstraintParticle constraint) {
        constraints.add(constraint);
    }

    /**
     * Adds a particle to the list
     * 
     * @param p
     * @return itself
     */
    public pma_PhysicsVerlet addParticle(pma_ParticleVerlet p) {
        particles.add(p);
        return this;
    }

    /**
     * Adds a spring connector
     * 
     * @param s
     * @return itself
     */
    public pma_PhysicsVerlet addSpring(pma_Spring s) {
        if (getSpring(s.a, s.b) == null) {
            springs.add(s);
        }
        return this;
    }
    
    /**
     * @return the index
     */
    public gb_SpatialIndex<ga_Vector2> getIndex() {
        return index;
    }
    /**
     * @param index
     *            the index to set
     */
    public void setIndex(gb_SpatialIndex<ga_Vector2> index) {
        this.index = index;
    }
    /**
     * Applies all global constraints and constrains all particle positions to
     * the world bounding rect set.
     */
    protected void applyConstaints() {
        boolean hasGlobalConstraints = constraints.size() > 0;
        for (pma_ParticleVerlet p : particles) {
            if (hasGlobalConstraints) {
                for (pma_i_ConstraintParticle c : constraints) {
                    c.apply(p);
                }
            }
            if (p.bounds != null) {
            	p.bounds.constrain(p);
            }
            if (worldBounds != null) {
            	worldBounds.constrain(p);
            }
        }
    }

    public pma_PhysicsVerlet clear() {
        behaviors.clear();
        constraints.clear();
        particles.clear();
        springs.clear();
        return this;
    }

    public ga_Rectangle getCurrentBounds() {
        ga_Vector2 min = new ga_Vector2(Float.MAX_VALUE, Float.MAX_VALUE);
        ga_Vector2 max = new ga_Vector2(Float.MIN_VALUE, Float.MIN_VALUE);
        for (Iterator<pma_ParticleVerlet> i = particles.iterator(); i.hasNext();) {
            pma_ParticleVerlet p = i.next();
            min.min(p);
            max.max(p);
        }
        return new ga_Rectangle(min, max);
    }

    public float getDrag() {
        return 1f - drag;
    }

    /**
     * @return the numIterations
     */
    public int getNumIterations() {
        return numIterations;
    }

    /**
     * Attempts to find the spring element between the 2 particles supplied
     * 
     * @param a
     *            particle 1
     * @param b
     *            particle 2
     * @return spring instance, or null if not found
     */
    public pma_Spring getSpring(ga_Vector2 a, ga_Vector2 b) {
        for (pma_Spring s : springs) {
            if ((s.a == a && s.b == b) || (s.a == b && s.b == a)) {
                return s;
            }
        }
        return null;
    }

    /**
     * @return the timeStep
     */
    public float getTimeStep() {
        return timeStep;
    }

    /**
     * @return the worldBounds
     */
    public ga_Rectangle getWorldBounds() {
        return worldBounds;
    }

    public boolean removeBehavior(pma_i_BehaviorParticle c) {
        return behaviors.remove(c);
    }

    public boolean removeConstraint(pma_i_ConstraintParticle c) {
        return constraints.remove(c);
    }

    /**
     * Removes a particle from the simulation.
     * 
     * @param p
     *            particle to remove
     * @return true, if removed successfully
     */
    public boolean removeParticle(pma_ParticleVerlet p) {
        return particles.remove(p);
    }

    /**
     * Removes a spring connector from the simulation instance.
     * 
     * @param s
     *            spring to remove
     * @return true, if the spring has been removed
     */
    public boolean removeSpring(pma_Spring s) {
        return springs.remove(s);
    }

    /**
     * Removes a spring connector and its both end point particles from the
     * simulation
     * 
     * @param s
     *            spring to remove
     * @return true, only if spring AND particles have been removed successfully
     */
    public boolean removeSpringElements(pma_Spring s) {
        if (removeSpring(s)) {
            return (removeParticle(s.a) && removeParticle(s.b));
        }
        return false;
    }

    public void setDrag(float drag) {
        this.drag = 1f - drag;
    }

    /**
     * @param numIterations
     *            the numIterations to set
     */
    public void setNumIterations(int numIterations) {
        this.numIterations = numIterations;
    }

    /**
     * @param timeStep
     *            the timeStep to set
     */
    public void setTimeStep(float timeStep) {
        this.timeStep = timeStep;
        for (pma_i_BehaviorParticle b : behaviors) {
            b.configure(timeStep);
        }
    }

    /**
     * Sets bounding box
     * 
     * @param world
     * @return itself
     */
    public pma_PhysicsVerlet setWorldBounds(ga_Rectangle world) {
        worldBounds = world;
        return this;
    }

    /**
     * Progresses the physics simulation by 1 time step and updates all forces
     * and particle positions accordingly
     * 
     * @return itself
     */
    public pma_PhysicsVerlet update() {
        updateParticles();
        updateSprings();
        applyConstaints();
        updateIndex();
        return this;
    }

    /**
     * Updates all particle positions
     */
    protected void updateParticles() {
        for (pma_i_BehaviorParticle b : behaviors) {
        	if (index != null && b.supportsSpatialIndex()) {
                b.applyWithIndex(index);
            } else {
                for (pma_ParticleVerlet p : particles) {
                    b.apply(p);
                }
            }
        }
        for (pma_ParticleVerlet p : particles) {
            p.scaleVelocity(drag);
            p.update();
        }
    }

    /**
     * Updates all spring connections based on new particle positions
     */
    protected void updateSprings() {
        if (springs.size() > 0) {
            for (int i = numIterations; i > 0; i--) {
                for (pma_Spring s : springs) {
                    s.update(i == 1);
                }
            }
        }
    }
    
    private void updateIndex() {
        if (index != null) {
            index.clear();
            for (pma_ParticleVerlet p : particles) {
                index.index(p);
            }
        }
    }
}