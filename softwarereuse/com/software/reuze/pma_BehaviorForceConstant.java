package com.software.reuze;

//import toxi.physics2d.VerletParticle2D;

public class pma_BehaviorForceConstant implements pma_i_BehaviorParticle {

    protected ga_Vector2 force;
    protected ga_Vector2 scaledForce = new ga_Vector2();
    protected float timeStep;

    public pma_BehaviorForceConstant(ga_Vector2 force) {
        this.force = force;
    }

    public void apply(pma_ParticleVerlet p) {
        p.addForce(scaledForce);
    }

    public void configure(float timeStep) {
        this.timeStep = timeStep;
        setForce(force);
    }

    /**
     * @return the force
     */
    public ga_Vector2 getForce() {
        return force;
    }

    /**
     * @param force
     *            the force to set
     */
    public void setForce(ga_Vector2 force) {
        this.force = force;
        this.scaledForce.set(force);
        this.scaledForce.mul(timeStep);
    }
    public void applyWithIndex(gb_SpatialIndex<ga_Vector2> spaceHash) {
        throw new UnsupportedOperationException("not implemented");
    }
    public boolean supportsSpatialIndex() {
        return false;
    }
}
