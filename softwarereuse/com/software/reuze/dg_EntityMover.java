package com.software.reuze;

import java.util.Set;

import com.software.reuze.dg_i_SteeringConstants;
import com.software.reuze.ga_Geometry2D;


/**
 * The class that models the behavior of a moving entity that is not acting under
 * the influence of a steering behavior.
 * 
 * 
 * @author Peter Lager
 *
 */
public class dg_EntityMover extends dg_a_EntityBase {

	// The domain the entity is constrained to.
	protected dg_EntityDomainRectangular   		wd = null;
	protected int domainConstraint = dg_i_SteeringConstants.WRAP;

	// World position after last update
	protected ga_Vector2D		prevPos = new ga_Vector2D();

	protected ga_Vector2D		velocity = new ga_Vector2D();;

	// a normalized vector pointing in the direction the entity is heading. 
	protected ga_Vector2D		heading = new ga_Vector2D();;
	// a normalized vector pointing in the entity's rest heading. 
	protected ga_Vector2D		headingAtRest = null;

	//a normalized vector perpendicular to the heading vector
	protected ga_Vector2D		side = new ga_Vector2D(); 

	// The mass of the entity
	protected double		mass;

	// The maximum speed this entity may travel at.
	protected double		maxSpeed;

	// The maximum force this entity can use to power itself 
	protected double 		maxForce;

	// The maximum rate (radians per second) this vehicle can rotate         
	protected double		maxTurnRate;
	// The current rate of turn (radians per second)         
	protected double		currTurnRate;
	// The previous rate of turn i.e. on last update (radians per second)         
	protected double		prevTurnRate;

	// The distance that the entity can see another moving entity
	protected double viewDistance = 50;;
	// Field of view (radians)
	protected double viewFOV = 1.047; // Default is 60 degrees
	public dg_EntityMover() {
		super();
	}

	/**
	 * This is the constructor that should be used when creating a MovingEntity
	 * @param name the name of the entity
	 * @param position initial world position
	 * @param radius bounding radius
	 * @param velocity initial velocity
	 * @param max_speed maximum speed
	 * @param heading initial heading
	 * @param mass initial mass
	 * @param turn_rate how fast the entity can turn (radians / second)
	 * @param max_force the maximum force that can be applied by a steering behavior
	 */
	public  dg_EntityMover(	String name,
			ga_Vector2D position,
			double   radius,
			ga_Vector2D velocity,
			double   max_speed,
			ga_Vector2D heading,
			double   mass,
			double   turn_rate,
			double   max_force)
	{
		super(name, position, radius);
		this.heading = heading;
		this.velocity = velocity;
		this.mass = mass;
		this.side.set(heading.getPerp());
		this.maxSpeed = max_speed;
		this.maxTurnRate = turn_rate;
		this.currTurnRate = turn_rate;
		this.prevTurnRate = turn_rate;
		this.maxForce = max_force;
	}
	
	public dg_EntityMover(dg_EntityMover em) {
		super(em);
		this.currTurnRate=em.currTurnRate;
		this.domainConstraint=em.domainConstraint;
		this.heading.set(em.heading.x,em.heading.y);
		if (em.headingAtRest!=null) headingAtRest=new ga_Vector2D(em.headingAtRest);
		this.mass=em.mass;
		this.maxForce=em.maxForce;
		this.maxSpeed=em.maxSpeed;
		this.maxTurnRate=em.maxTurnRate;
		this.prevPos.set(em.prevPos.x,em.prevPos.y);
		this.prevTurnRate=em.prevTurnRate;
		this.side.set(em.side.x,em.side.y);
		this.velocity.set(em.velocity.x,em.velocity.y);
		this.viewDistance=em.viewDistance;
		this.viewFOV=em.viewFOV;
		this.wd=em.wd;
	}

	/**
	 * Gets the position of the entity prior to the last update.
	 * @return previous position before last update
	 */
	public ga_Vector2D getPrevPos(){
		return prevPos;
	}

	/**
	 * Gets the current velocity
	 * @return the current velocity of this entity
	 */
	public ga_Vector2D getVelocity(){
		return velocity;
	}

	/**
	 * Sets the current velocity for this entity
	 * @param newVel the new velocity vector
	 */
	public void setVelocity(ga_Vector2D newVel){
		velocity.set(newVel.x,newVel.y);
	}
	
	/**
	 * Sets the current velocity
	 * @param vx 
	 * @param vy
	 */
	public void setVelocity(double vx, double vy){
		velocity.set(vx, vy);
	}

	public void setHeading(ga_Vector2D h){
		heading.set(h.x,h.y);
	}
	
	public void setHeading(double x, double y){
		heading.set(x, y);
	}
	
	
	/**
	 * Get the mass of this entity
	 * @return the entity's mass
	 */
	public double getMass(){
		return mass;
	}

	/**
	 * Get the side vector for this entity. The side vector is
	 * always perpendicular to the heading and normalized.
	 * @return the side vector
	 */
	public ga_Vector2D getSide(){
		return side;
	}

	/**
	 * Get the maximum speed allowed for this entity.
	 * @return max speed allowed
	 */
	public double getMaxSpeed(){
		return maxSpeed;
	}                       

	/**
	 * Sets the maximum speed this entity is allowed to reach
	 * @param maxSpeed
	 */
	public void setMaxSpeed(float maxSpeed){
		this.maxSpeed = maxSpeed;
	}

	/**
	 * Gets the maximum force that can be applied to this entity.
	 * @return max force allowed
	 */
	public double getMaxForce(){
		return maxForce;
	}

	/**
	 * Sets the maximum force that can be applied to this entity.
	 * @param mf max force allowed
	 */
	public void setMaxForce(float mf){
		maxForce = mf;
	}

	/**
	 * See if the current speed exceeds the maximum speed permitted.
	 * @return true if the speed is greater or equal to the max speed.
	 */
	public boolean isSpeedMaxedOut(){
		return velocity.lengthSq() >= maxSpeed*maxSpeed;
	}

	/**
	 * Get the entity's speed.
	 * @return speed in direction of travel
	 */
	public double getSpeed(){
		return velocity.length();
	}

	/**
	 * Get the square of the entity's speed.
	 * @return speed in direction of travel squared
	 */
	public double getSpeedSq(){
		return velocity.lengthSq();
	}

	/**
	 * Get the current heading for this entity
	 * @return the entity's heading
	 */
	public ga_Vector2D getHeading(){
		return heading;
	}


	/**
	 * get the default heading for this entity.
	 * @return the headingAtRest
	 */
	public ga_Vector2D getHeadingAtRest() {
		return headingAtRest;
	}

	/**
	 * Set the default heading for this entity. If the parameter
	 * is <pre>null</pre> then the default heading is canceled.
	 * The default heading will be normalized.
	 *  
	 * @param restHeading the headingAtRest to set
	 */
	public void setHeadingAtRest(ga_Vector2D restHeading) {
		if(restHeading == null){
			headingAtRest = null;
		}
		else {
			if(headingAtRest == null)
				headingAtRest = new ga_Vector2D(restHeading);
			else
				headingAtRest.set(restHeading.x,restHeading.y);	
			headingAtRest.normalize();
		}
	}

	/**
	 * Cancels the default heading for this entity so when it finally stops
	 * it will be facing the last direction it was facing.
	 */
	public void cancelHeadingAtRest(){
		headingAtRest = null;
	}

	public double getMaxTurnRate(){
		return maxTurnRate;
	}

	public void setMaxTurnRate(double val){
		maxTurnRate = val;
	}

	public double getTurnRate(){
		return currTurnRate;
	}

	public void setTurnRate(double val){
		currTurnRate = m_MathFast.min(val, maxTurnRate);
	}


	/**
	 * @return the viewDistance
	 */
	public double getViewDistance() {
		return viewDistance;
	}

	/**
	 * @param viewDistance the viewDistance to set
	 */
	public void setViewDistance(double viewDistance) {
		this.viewDistance = viewDistance;
	}

	/**
	 * @return the viewFOV
	 */
	public double getViewFOV() {
		return viewFOV;
	}

	/**
	 * @param viewFOV the viewFOV to set
	 */
	public void setViewFOV(double viewFOV) {
		this.viewFOV = viewFOV;
	}

	/**
	 * Set the world domain for this entity. The default constraint is SBF.WRAP where the
	 * entity leaves the domain it is wrapped to the other side.
	 * @param wd the world domain for this entity
	 */
	public void setWorldDomain(dg_EntityDomainRectangular wd){
		this.wd = wd;
		domainConstraint = dg_i_SteeringConstants.WRAP;
	}

	/**
	 * 
	 * @param wd the world domain for this entity
	 * @param constraint SBF.WRAP or SBF.REBOUND or SBF.PASS_THROUGH
	 */
	public void setWorldDomain(dg_EntityDomainRectangular wd, int constraint){
		this.wd = wd;
		if (constraint == dg_i_SteeringConstants.WRAP || constraint == dg_i_SteeringConstants.REBOUND || constraint == dg_i_SteeringConstants.PASS_THROUGH)
			domainConstraint = constraint;
	}

	public void applyDomainConstraint(){
		switch(domainConstraint){
		case dg_i_SteeringConstants.WRAP:
			if (pos.x < wd.lowX)
				pos.x += wd.width;
			else if (pos.x > wd.highX)
				pos.x -= wd.width;
			if (pos.y < wd.lowY)
				pos.y += wd.height;
			else if (pos.y > wd.highY)
				pos.y -= wd.height;
			break;
		case dg_i_SteeringConstants.REBOUND:
			if (pos.x < wd.lowX)
				velocity.x = m_MathFast.abs(velocity.x);
			else if (pos.x > wd.highX)
				velocity.x = -m_MathFast.abs(velocity.x);
			if (pos.y < wd.lowY)
				velocity.y = m_MathFast.abs(velocity.y);
			else if (pos.y > wd.highY)
				velocity.y = -m_MathFast.abs(velocity.y);
			break;
		default:
			break;
		}
	}

	/**
	 * Determines whether two points are either side of this moving entity. If they are 
	 * then they cannot 'see' each other.
	 * 
	 * @param x0
	 * @param y0
	 * @param x1
	 * @param y1
	 * @return
	 */
	public boolean isEitherSide(double x0, double y0, double x1, double y1){
		return ga_Geometry2D.line_circle(x0, y0, x1, y1, pos.x, pos.y, colRadius);
	}

	/**
	 * This method determines whether this entity can see a particular position in the world.
	 * It first checks to see if it is within this entity's view distance and field of view (FOV).
	 * If it is then it checks to see if there are any walls or obstacles between them.
	 * 
	 * @param world
	 * @param x0
	 * @param y0
	 * @return
	 */
	public boolean canSee(dg_World world, double x0, double y0){
		ga_Vector2D toTarget = new ga_Vector2D(x0 - pos.x, y0 - pos.y);
		// See if in view range
		double distToTarget = toTarget.length();
		if(distToTarget > viewDistance)
			return false;
		// See if in field of view
		toTarget.div(distToTarget);	// normalize toTarget
		double cosAngle = heading.dot(toTarget);
		if (cosAngle <  m_MathFast.cos(viewFOV / 2))
			return false;
		// If we get here then the position is within range and field of view, but do we have an obstruction.
		// First check for an intervening wall 
		Set<dg_EntityWall> walls = world.getWalls(this, x0, y0);
		if (walls != null && !walls.isEmpty()){
			for(dg_EntityWall wall : walls){
				if(wall.isEitherSide(pos.x, pos.y, x0, y0))
					return false;
			}
		}
		// First check for an intervening obstacle 
		Set<dg_EntityObstacle> obstacles = world.getObstacles(this, x0, y0);
		if (obstacles != null && !obstacles.isEmpty()){
			for(dg_EntityObstacle obstacle : obstacles){
				if(obstacle.isEitherSide(pos.x, pos.y, x0, y0))
					return false;					
			}
		}
		return true;
	}


	/**
	 * ----------------------- RotateHeadingToFacePosition ------------------
	 * 
	 * given a target position, this method rotates the entity's heading and
	 * side vectors by an amount not greater than m_dMaxTurnRate until it
	 * directly faces the target.
	 * 
	 * @param elapsed time
	 * @param faceTarget the world position to face
	 * @return true when the heading is facing in the desired direction
	 */
	public boolean rotateHeadingToFacePosition(double deltaTime, ga_Vector2D faceTarget){
		// Calculate the normalized vector to the face target
		ga_Vector2D alignTo = ga_Vector2D.sub(faceTarget, pos);
		alignTo.normalize();
		return rotateHeadingToAlignWith(deltaTime, alignTo);
	}

	public boolean rotateHeadingToAlignWith(double deltaTime, final ga_Vector2D allignTo){
		// Calculate the angle between the heading vector and the target
		double angleBetween = heading.angleBetween(allignTo);

		// Return true if the player is virtually facing the target
		if (angleBetween < Double.MIN_VALUE) return true;

		// Calculate the amount of turn possible in time allowed
		double angleToTurn = currTurnRate * deltaTime;

		// Prevent over steer by clamping the amount to turn to the angle angle 
		// between the heading vector and the target
		if (angleToTurn > angleBetween) angleToTurn = angleBetween;

		// The next few lines use a rotation matrix to rotate the player's heading
		// vector accordingly
		m_Matrix2D rotMatrix = new m_Matrix2D();

		// The direction of rotation is needed to create the rotation matrix
		rotMatrix.rotate(angleToTurn * allignTo.sign(heading));	
		// Rotate heading
		heading = rotMatrix.transformVector2D(heading);
		heading.normalize();
		// Calculate new side
		side = heading.getPerp();
		return false;
	}

	/**
	 * Determine whether this entity is inside or part inside the domain. This method is
	 * used by the world draw method to see if this entity should be drawn.
	 * @param view the world domain
	 * @return true if any part of this entity is inside the domain
	 */
	@Override
	public boolean isInDomain(dg_EntityDomainRectangular view) {
		return (pos.x >= view.lowX && pos.x <= view.highX && pos.y >= view.lowY && pos.y <= view.highY);
	}

	@Override
	public boolean isOver(double px, double py) {
		return ((pos.x - px)*(pos.x - px) + (pos.y - py)*(pos.y - py)) <= (colRadius * colRadius);
	}

	/**
	 * Update method for any moving entity in the world that is not under
	 * the influence of a steering behavior.
	 * @param deltaTime elapsed time since last update
	 * @param world the game world object
	 */
	@Override
	public void update(double deltaTime, dg_World world) {
		// Remember the starting position
		prevPos.set(pos.x,pos.y);
		// Update position
		pos.set(pos.x + velocity.x * deltaTime, pos.y + velocity.y * deltaTime);
		// Apply domain constraints
		if (wd != null)
			applyDomainConstraint();
		// Update heading
		if (velocity.lengthSq() > 1)
			rotateHeadingToAlignWith(deltaTime, velocity);
		else {
			velocity.set(0,0);
			if (this.headingAtRest != null && headingAtRest.equals(ga_Vector2D.ZERO)){
				rotateHeadingToAlignWith(deltaTime, headingAtRest);
			}
		}
		// Ensure heading and side are normalized
		heading.normalize();
		side.set(-heading.y, heading.x);
	}

	@Override
	public void draw(dg_World world) {
		if (renderer != null && visible){
			renderer.draw((float) pos.x, (float) pos.y, 
					(float) velocity.x, (float) velocity.y, 
					(float) heading.x, (float) heading.y, this); 
		}
	}
	
	public int collide(dg_EntityMover m1) {
		return 0;
		/*boolean a=this.name.startsWith("Pa")||this.name.charAt(0)=='L';
		boolean b=m1.name.startsWith("Pa")||m1.name.charAt(0)=='L';
		if (a&&b) return 0;
		boolean c=this.name.startsWith("Pe");
		boolean d=m1.name.startsWith("Pe");
		if (c&&d) return 0;
		System.out.println(this.name+" "+m1.name);
		return a?1:2;*/
	}
}
