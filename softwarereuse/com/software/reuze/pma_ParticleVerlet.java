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
import java.util.Collection;
import java.util.List;


/*import toxi.geom.ReadonlyVec2D;
import toxi.geom.Rect;
import toxi.geom.Vec2D;
import toxi.physics2d.behaviors.ParticleBehavior2D;
import toxi.physics2d.constraints.ParticleConstraint2D;*/

/**
 * An individual 2D particle for use by the VerletPhysics and VerletSpring
 * classes. A particle has weight, can be locked in space and its position
 * constrained inside an (optional) axis-aligned bounding box.
 */
public class pma_ParticleVerlet extends ga_Vector2 {

    protected ga_Vector2 prev;
    protected boolean isLocked;

    /**
     * Bounding box, by default set to null to disable
     */
    public ga_Rectangle bounds; //TODO replace with a constraint

    /**
     * An optional particle constraints, called immediately after a particle is
     * updated (and only used if particle is unlocked (default)
     */
    public List<pma_i_ConstraintParticle> constraints;

    public List<pma_i_BehaviorParticle> behaviors;
    /**
     * Particle weight, default = 1
     */
    protected float weight, invWeight;

    protected ga_Vector2 force = new ga_Vector2();

    /**
     * Creates particle at position xy
     * 
     * @param x
     * @param y
     */
    public pma_ParticleVerlet(float x, float y) {
        this(x, y, 1);
    }

    /**
     * Creates particle at position xy with weight w
     * 
     * @param x
     * @param y
     * @param w
     */
    public pma_ParticleVerlet(float x, float y, float w) {
        super(x, y);
        prev = new ga_Vector2(this);
        setWeight(w);
    }

    /**
     * Creates particle at the position of the passed in vector
     * 
     * @param v
     *            position
     */
    public pma_ParticleVerlet(final ga_Vector2 v) {
        this(v.x, v.y, 1);
    }

    /**
     * Creates particle with weight w at the position of the passed in vector
     * 
     * @param v
     *            position
     * @param w
     *            weight
     */
    public pma_ParticleVerlet(final ga_Vector2 v, float w) {
        this(v.x, v.y, w);
    }

    /**
     * Creates a copy of the passed in particle
     * 
     * @param p
     */
    public pma_ParticleVerlet(pma_ParticleVerlet p) {
        this(p.x, p.y, p.weight);
        isLocked = p.isLocked;
    }

    public pma_ParticleVerlet addBehavior(pma_i_BehaviorParticle behavior) {
        return addBehavior(behavior, 1);
    }

    public pma_ParticleVerlet addBehavior(pma_i_BehaviorParticle behavior,
            float timeStep) {
        if (behaviors == null) {
            behaviors = new ArrayList<pma_i_BehaviorParticle>(1);
        }
        behavior.configure(timeStep);
        behaviors.add(behavior);
        return this;
    }
    
    public pma_ParticleVerlet addBehavior(
            Collection<pma_i_BehaviorParticle> behaviors) {
        return addBehavior(behaviors, 1);
    }

    public pma_ParticleVerlet addBehavior(
            Collection<pma_i_BehaviorParticle> behaviors, float timeStemp) {
        for (pma_i_BehaviorParticle b : behaviors) {
            addBehavior(b, timeStemp);
        }
        return this;
    }

    /**
     * Adds the given constraint implementation to the list of constraints
     * applied to this particle at each time step.
     * 
     * @param c
     *            constraint instance
     * @return itself
     */
    public pma_ParticleVerlet addConstraint(pma_i_ConstraintParticle c) {
        if (constraints == null) {
            constraints = new ArrayList<pma_i_ConstraintParticle>(1);
        }
        constraints.add(c);
        return this;
    }
    
    public pma_ParticleVerlet addConstraint(
            Collection<pma_i_ConstraintParticle> constraints) {
        for (pma_i_ConstraintParticle c : constraints) {
            addConstraint(c);
        }
        return this;
    }

    public pma_ParticleVerlet addForce(ga_Vector2 f) {
        force.add(f);
        return this;
    }

    public pma_ParticleVerlet addVelocity(ga_Vector2 v) {
        prev.sub(v);
        return this;
    }

    public void applyBehaviors() {
        if (behaviors != null) {
            for (pma_i_BehaviorParticle b : behaviors) {
                b.apply(this);
            }
        }
    }

    public void applyConstraints() {
        if (constraints != null) {
            for (pma_i_ConstraintParticle pc : constraints) {
                pc.apply(this);
            }
        }
    }

    protected void applyForce() {
        float xx=this.x, yy=this.y;
        add(tmp2().sub(prev).add(force.tmp().mul(weight)));
        prev.set(xx, yy);
        force.clear();
    }

    public pma_ParticleVerlet clearForce() {
        force.clear();
        return this;
    }

    public pma_ParticleVerlet clearVelocity() {
        prev.set(this);
        return this;
    }

    /**
     * @return the inverse weight (1/weight)
     */
    public final float getInvWeight() {
        return invWeight;
    }

    /**
     * Returns the particle's position at the most recent time step.
     * 
     * @return previous position
     */
    public ga_Vector2 getPreviousPosition() {
        return prev;
    }

    public ga_Vector2 getVelocity() {
        return tmp().sub(prev);
    }

    /**
     * @return the weight
     */
    public final float getWeight() {
        return weight;
    }

    /**
     * @return true, if particle is locked
     */
    public final boolean isLocked() {
        return isLocked;
    }

    /**
     * Locks/immobilizes particle in space
     * 
     * @return itself
     */
    public pma_ParticleVerlet lock() {
        isLocked = true;
        return this;
    }

    public pma_ParticleVerlet removeAllBehaviors() {
        behaviors.clear();
        return this;
    }

    /**
     * Removes any currently applied constraints from this particle.
     * 
     * @return itself
     */
    public pma_ParticleVerlet removeAllConstraints() {
        constraints.clear();
        return this;
    }

    public boolean removeBehavior(pma_i_BehaviorParticle b) {
        return behaviors.remove(b);
    }
    
    public boolean removeBehaviors(Collection<pma_i_BehaviorParticle> behaviors) {
        return this.behaviors.removeAll(behaviors);
    }

    /**
     * Attempts to remove the given constraint instance from the list of active
     * constraints.
     * 
     * @param c
     *            constraint to remove
     * @return true, if successfully removed
     */
    public boolean removeConstraint(pma_i_ConstraintParticle c) {
        return constraints.remove(c);
    }
    
    public boolean removeConstraints(
            Collection<pma_i_ConstraintParticle> constraints) {
        return this.constraints.removeAll(constraints);
    }

    public pma_ParticleVerlet scaleVelocity(float scl) {
        prev.interpolateTo(this, 1f - scl);
        return this;
    }

    public pma_ParticleVerlet setPreviousPosition(ga_Vector2 p) {
        prev.set(p);
        return this;
    }

    public void setWeight(float w) {
        weight = w;
        invWeight = 1f / w;
    }

    /**
     * Unlocks particle again
     * 
     * @return itself
     */
    public pma_ParticleVerlet unlock() {
        clearVelocity();
        isLocked = false;
        return this;
    }

    public void update() {
        if (!isLocked) {
            applyBehaviors();
            applyForce();
            applyConstraints();
        }
    }
}
