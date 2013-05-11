package com.software.reuze;

import com.software.reuze.dag_GraphNode;
import com.software.reuze.dg_Steering;

public class dg_EntityVehicle extends dg_EntityMover {
	
	protected dg_Steering sb;
	public boolean isTracking;
	public boolean isTracking() {
		return isTracking;
	}

	public void setTracking(boolean isTracking) {
		this.isTracking = isTracking;
	}

	public dg_EntityVehicle() {
		super();
	}

	public  dg_EntityVehicle(	ga_Vector2D position,
			double   radius,
			ga_Vector2D velocity,
			double   max_speed,
			ga_Vector2D heading,
			double   mass,
			double   turn_rate,
			double   max_force){

		super("",  position, radius, velocity, max_speed, heading, mass,
				turn_rate, max_force);
	}
	
	public  dg_EntityVehicle(	String name,
			ga_Vector2D position,
			double   radius,
			ga_Vector2D velocity,
			double   max_speed,
			ga_Vector2D heading,
			double   mass,
			double   turn_rate,
			double   max_force){

		super(name, position, radius, velocity, max_speed, heading, mass,
				turn_rate, max_force);
	}
	
	/*
	 * copy constructor
	 */
	public dg_EntityVehicle(dg_EntityVehicle ve) {
		super(ve);
		this.sb=ve.sb;
		this.isTracking=ve.isTracking;
	}

	public void setSB(dg_Steering sb){
		this.sb = sb;
		this.sb.setOwner(this);
	}

	public dg_Steering getSB(){
		return sb;
	}

	public double getMaxForce(){
		return maxForce;
	}

	/**
	 * Update method for any moving entity in the world that is under
	 * the influence of a steering behavior.
	 * @param deltaTime elapsed time since last update
	 * @param world the game world object
	 */
	public void update(double deltaTime, dg_World world) {
		// Remember the starting position
		prevPos.set(pos);
		// Accumulator for forces
		ga_Vector2D accel = new ga_Vector2D();
		if(sb != null){
			ga_Vector2D force = sb.calculateForce(deltaTime, world);
			force.truncate(maxForce);
			accel.set(ga_Vector2D.div(force, mass));
			// Change velocity according to acceleration and elapsed time
			accel.mult(deltaTime);
			velocity.add(accel);
		}
		// Make sure we don't exceed maximum speed
		velocity.truncate(maxSpeed);
		// Change position according to velocity and elapsed time
		pos.add(ga_Vector2D.mult(velocity, deltaTime));
		// Apply domain constraints
		if(wd != null)
			applyDomainConstraint();
		// Update heading
		if(velocity.lengthSq() > 1)
			rotateHeadingToAlignWith(deltaTime, velocity);
		else {
			velocity.set(0,0);
			if(this.headingAtRest != null && headingAtRest != ga_Vector2D.ZERO){
				rotateHeadingToAlignWith(deltaTime, headingAtRest);
			}
		}
//		if(wd != null){
//			//		wd.wrapArroundPosition(pos);
//			if(pos.x < wd.lowX)
//				pos.x += wd.width;
//			else if(pos.x > wd.highX)
//				pos.x -= wd.width;
//			if(pos.y < wd.lowY)
//				pos.y += wd.height;
//			else if(pos.y > wd.highY)
//				pos.y -= wd.height;
//		}
		// Ensure heading and side are normalized
		heading.normalize();
		side.set(-heading.y, heading.x);
	}

	public String toString(){
		StringBuilder s = new StringBuilder(name + "  @ " + pos + "  V:" + velocity + "  H: "+ heading + "\n");
		s.append("\tCR: " + colRadius + "   MASS: " + mass + "   MTR: " + maxTurnRate + "   MF: " + maxForce);
		return new String(s);
	}

	public void arrive(dag_GraphNode g) {
		// TODO Auto-generated method stub
	}
}
