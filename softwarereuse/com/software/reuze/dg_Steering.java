package com.software.reuze;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.software.reuze.m_MathUtils;


/**
 * Objects of this class are used to describe the steering behavior for a Vehicle object. Each
 * vehicle should have its own SB object. <br>
 * 
 * This class supports 15 different behaviors which can be combined to provide steering
 * behaviors suitable for different game environments. <br>
 * 
 * A large number of constants are defined to simplify the use of this class.<br>
 * <b>Behavior constants</b><br><pre>
 * WALL_AVOID	OBSTACLE_AVOID		EVADE		FLEE	SEPARATION	<br>
 * ALIGNMENT 	COHESION			SEEK		ARRIVE	WANDER <br>
 * PURSUIT		OFFSET_PURSUIT		INTERPOSE	HIDE	PATH	<br>
 * FLOCK</pre><br><br>
 * 
 * <b>Force calculation constants</b><br><pre>
 * WEIGHTED_AVERAGE   PRIORITIZED </pre><br><br>
 * 
 * <b>Agents (other Vehicle objects) to pursue, evade etc.</b><br><pre>
 * AGENT0	AGENT1		AGENT_TO_PURSUE		AGENT_TO_FLEE </pre><br><br>
 * 
 * @author Peter Lager
 *
 */
public class dg_Steering implements Cloneable, dg_i_SteeringConstants{

	private static final double WANDER_MIN_ANGLE 		= -1.0 * m_MathFast.PI;
	private static final double WANDER_MAX_ANGLE 		= m_MathFast.PI;
	private static final double WANDER_ANGLE_RANGE 	=  2.0 * m_MathFast.PI;
	
	// These are used during force calculations so do not need to be cloned
	private Set<dg_EntityObstacle> obstacles = null;
	private Set<dg_EntityWall> walls = null;
	private Set<dg_EntityMover> movers = null;

	// these values are passed as parameters and are stored at the start
	// of the calculateForce method so do not need to be cloned
	private double deltaTime = 0;
	private dg_World world;

	// This is set when this steering behavior is added to a vehicle
	// it indicates the owner of this behavior
	private dg_EntityVehicle owner = null;   

	// ======================================================================
	// The following variables need to be cloned as they are unique 
	// to the particular behavior.
	// ======================================================================
	private int flags = 0;

	/**
	 * Default values for steering behavior objects.
	 */
	private double[] weightings = new double[] {
			220.0,		// wall avoidance weight
			80.0,		// obstacle avoidance weight
			1.0,		// evade weight
			20.0,		// flee weight
			1.0, 		// separation weight
			4.0,		// alignment weight
			15.0, 		// cohesion weight
			20.0, 		// seek weight
			20.0, 		// arrive weight
			5.0, 		// wander weight
			100.0, 		// pursuit weight
			10.0, 		// offset pursuit weight
			10.0, 		// interpose weight
			10.0, 		// hide weight
			20.0, 		// follow path weight
			1.0 		// flock weight
	};

	// How should the steering force be calculated
	private int forceCalcMethod = WEIGHTED_AVERAGE;

	// Target for arrive and seek
	private ga_Vector2D gotoTarget = new ga_Vector2D();
	// Deceleration rate for arrive
	private int arriveRate = NORMAL;
	private double arriveDist = 0.3;

	private ga_Vector2D fleeTarget = new ga_Vector2D();
	// Panic distance squared for flee to be effective
	private double fleeDistance = 100;

	// Used in path following
	private LinkedList<dag_GraphNode> path = new LinkedList<dag_GraphNode>();
	private double pathSeekDist = 20;                                          
	private double pathArriveDist = 0.3;                                          

	// Obstacle avoidance
	private double detectBoxLength = 20.0;

	/**
	 * the first 2 are used for the interpose
	 * AGENT0, AGENT1, AGENT_TO_PURSUE, AGENT_TO_EVADE
	 */
	private dg_EntityVehicle[] agents = new dg_EntityVehicle[NBR_AGENT_ARRAY];
	private ga_Vector2D pursueOffset = new ga_Vector2D();

	// the current angle to target on the wander circle 
	private double wanderAngle = 0;
	// the maximum amount of angular displacement per second between each frame
	private double wanderAngleJitter = 60;
	// the radius of the constraining circle for the wander behavior
	private double wanderRadius = 6.0;
	// distance the wander circle is projected in front of the agent
	private double wanderDist = 60.0;
	// The maximum angular displacement in this frame. Used in demos only
	private double wanderAngleDelta = 0;

	// Cats whiskers used for wall avoidance
	private int nbrWhiskers = 5;
	private double whiskerFOV = 3.0;	// radians
	private double whiskerLength = 30;

	// The maximum distance between moving entities for them to be considered
	// as neighbors. Used for group behaviors
	private double neighbourDist = 100.0;

	/**
	 * Do not use this method, it is used by the Vehicle class when a steering
	 * behavior is added.
	 * 
	 * @param vehicle
	 */
	public void setOwner(final dg_EntityVehicle vehicle){
		this.owner = vehicle;
	}

	/**
	 * Can set the target position for behaviors SEEK, ARRIVE or FLEE
	 * @param behavior SB.SEEK, SB.ARRIVE or SB.FLEE
	 * @param x x coordinate
	 * @param y y coordinate
	 */
	public void setTarget(int behavior, double x, double y){
		switch(behavior){
		case SEEK:
		case ARRIVE:
			gotoTarget.set(x, y);
			break;
		case FLEE:
			fleeTarget.set(x, y);
			break;
		}
	}

	/**
	 * Can set the target position for behaviors SEEK, ARRIVE or FLEE
	 * @param behavior SB.SEEK, SB.ARRIVE or SB.FLEE
	 * @param v target position
	 */
	public void setTarget(int behavior, final ga_Vector2D v){
		setTarget(behavior, v.x, v.y);
	}

	/**
	 * Get the target position for behaviors SEEK, ARRIVE or FLEE
	 * @param behavior SB.SEEK, SB.ARRIVE or SB.FLEE
	 * @return the target position
	 */	
	public ga_Vector2D getTarget(int behavior){
		switch(behavior){
		case SEEK:
		case ARRIVE:
			return gotoTarget;
		case FLEE:
			return fleeTarget;
		}
		return null;
	}

	/**
	 * Set one of the 4 different agents AGENT0, AGENT1, AGENT_TO_PURSUE or AGENT_TO_FLEE
	 * @param agentNo SB.AGENT0, SB.AGENT1, SB.AGENT_TO_PURSUE or SB.AGENT_TO_FLEE
	 * @param agent the agent (Vehicle) object
	 */
	public void setAgent(int agentNo, dg_EntityVehicle agent){
		if(agentNo >= 0 && agentNo < agents.length)
			agents[agentNo] = agent;
	}

	/**
	 * Get one of the 4 different agents AGENT0, AGENT1, AGENT_TO_PURSUE or AGENT_TO_FLEE
	 * @param agentNo SB.AGENT0, SB.AGENT1, SB.AGENT_TO_PURSUE or SB.AGENT_TO_FLEE
	 * return agent or null if it does not exist
	 */
	public dg_EntityVehicle getAgent(int agentNo){
		if(agentNo >= 0 && agentNo < agents.length)
			return agents[agentNo];
		else
			return null;
	}

	/**
	 * Set the offset position for a pursuit. The position should be calculated as if
	 * the pursued was facing along the x axis.
	 * @param offset coordinates for offset 
	 */
	public void setPursuitOffset(ga_Vector2D offset){
		pursueOffset.set(offset);			
	}

	/**
	 * Set the offset position for a pursuit. The position should be calculated as if
	 * the pursued was facing along the x axis.
	 * @param offsetX
	 * @param offsetY
	 */
	public void setPursuitOffset(double offsetX, double offsetY){
		pursueOffset.set(offsetX, offsetY);			
	}

	/**
	 * Get the pursuit offset co-ordinates
	 * @return
	 */
	public ga_Vector2D getPursuitOffset(){
		return pursueOffset;
	}

	/**
	 * Get the path the entity is following
	 * @return
	 */
	public LinkedList<dag_GraphNode> getPath(){
		return path;
	}

	/**
	 * Set the path the entity should follow. Ignore if route is null or has
	 * less than 2 nodes.
	 * @param route the path to follow
	 */
	public void setPath(List<dag_GraphNode> route){
		if(route != null && route.size() >= 2){
			path = (LinkedList<dag_GraphNode>) route;
			//		gotoTarget.set(path.getFirst().x(), path.getFirst().y());
			enableBehaviours(PATH);
		}
	}

	/**
	 * Add the route to the end of the existing path. Do nothing if the 
	 * parameter is null.
	 * @param route the path to add
	 */
	public void addToPath(List<dag_GraphNode> route){
		if(route != null){
			path.addAll(route);
			//		gotoTarget.set(path.getFirst().x(), path.getFirst().y());
			enableBehaviours(PATH);
		}
	}

	/**
	 * Calculate and add the route from the end of the existing path (or the nearest node if 
	 * there is no existing path) to the dest Node. The path calculation will use A* with 
	 * crow's flight heuristic when calculating the route. <br>
	 * The instruction is ignored if the graph or destination node does not exist.
	 * @param graph
	 * @param dest
	 */
	public void addToPath(final dag_GraphFloatingEdges graph, final dag_GraphNode dest){
		dag_GraphNode start = null;
		if(graph != null && dest != null) {
			if(path.isEmpty())
				start = graph.getNodeNearest(owner.getPos().x, owner.getPos().y, 0);
			else
				start = path.getLast();
			if (start.equals(dest)) return;
			dag_i_GraphSearch gs = new dag_GraphSearch_Astar(graph);
			LinkedList<dag_GraphNode> toadd = gs.search(start.id(), dest.id());
			path.addAll(toadd);
			if(!path.isEmpty())
				enableBehaviours(PATH);
		}
	}

	/**
	 * Get the node the entity is currently moving towards. If there is no
	 * path defined or the entity has reached the end of its last path 
	 * the method returns null.
	 * @return current path target or null if none
	 */
	public dag_GraphNode getPathStart(){
		if(!path.isEmpty())
			return path.getFirst();
		else
			return null;
	}

	/**
	 * Set the minimum length of the obstacle detection box. <br>
	 * The actual box length increases with the entity speed and at
	 * maximum speed will be double the minimum length.
	 * @param boxLength
	 */
	public void setDetectBoxLength(double boxLength){
		detectBoxLength = boxLength;
	}

	/**
	 * Only provided to enable drawing the box during testing. <br>
	 * 
	 * @return
	 */
	public double getDetectBoxLength(){
		return detectBoxLength;
	}

	/**
	 * Sets the wall detection whiskers parameters. <br>
	 *  
	 * @param numOfWhiskers the number of whiskers (should be >= 3)
	 * @param fov the total field of view of whiskers in radians
	 * @param maxLength the max length of a whisker
	 */
	public void setWallAvoidDetails(int numOfWhiskers, double fov, double maxLength){
		nbrWhiskers = numOfWhiskers;
		whiskerFOV = fov;
		whiskerLength = maxLength;
	}

	/**
	 * @return the nbrWhiskers
	 */
	public int getNbrWhiskers() {
		return nbrWhiskers;
	}

	/**
	 * @return the whiskerFOV
	 */
	public double getWhiskerFOV() {
		return whiskerFOV;
	}

	/**
	 * @return the whiskerLength
	 */
	public double getWhiskerLength() {
		return whiskerLength;
	}

	/**
	 * Set wander behavior factors. <br>
	 * @param jitter
	 * @param radius
	 * @param dist
	 */
	public void setWanderDetails(double jitter, double radius, double dist){
		wanderAngleJitter = jitter;
		wanderRadius = radius;
		wanderDist = dist;
	}

	/**
	 * @return the wanderRadius
	 */
	public double getWanderRadius() {
		return wanderRadius;
	}

	/**
	 * @return the wanderDist
	 */
	public double getWanderDist() {
		return wanderDist;
	}

	public double getWanderAngle(){
		return wanderAngle;
	}
	
	public double getWanderAngleDelta(){
		return wanderAngleDelta;
	}
	
	/**
	 * Sets the radius for the group behaviors (separation, alignment and cohesion)
	 * 
	 * @param radius the neighborhood radius
	 */
	public void setFlockRadius(double radius){
		neighbourDist = radius;
	}
	
	/**
	 * Set which method is to be used for calculating the steering force, options are <b><pre>
	 * SBF.WEIGHTED_AVERAGE (default) or SBF.PRIORITIZED <br></pre>
	 * any other value will be ignored.
	 * @param method the method to use
	 */
	public void setCalculateMethod(int method){
		if(method == WEIGHTED_AVERAGE || method == PRIORITIZED)
			forceCalcMethod = method;
	}
	
	/**
	 * Calculates (according to selected calculation method) the steering forces from any active behaviors. 
	 * @param deltaTime time since last update in seconds
	 * @param world the game world object
	 * @return the calculated steering force as a Vector2D
	 */
	public ga_Vector2D calculateForce(double deltaTime, dg_World world){
		obstacles = null;
		walls = null;
		movers = null;

		this.deltaTime = deltaTime;
		this.world = world;

		ga_Vector2D steeringForce = ga_Vector2D.ZERO;

		switch(forceCalcMethod){
		case WEIGHTED_AVERAGE:
			steeringForce = calculateWeightedAverage();
			break;
		case PRIORITIZED:
			steeringForce = calculatePrioritised();
			break;
		}
		obstacles = null;
		walls = null;
		movers = null;
		return steeringForce;
	}

	/**
	 * Uses the weighted average method to calculate the steering force.
	 * @return the calculated steering force as a Vector2D
	 */
	private ga_Vector2D calculateWeightedAverage(){
		ga_Vector2D accum = new ga_Vector2D();
		ga_Vector2D f = new ga_Vector2D();
		if((flags & WALL_AVOID) != 0){
			f = wallAvoidance();
			f.mult(weightings[BIT_WALL_AVOID]);
			accum.add(f);
		}
		if((flags & OBSTACLE_AVOID) != 0){
			f = obstacleAvoidance();
			f.mult(weightings[BIT_OBSTACLE_AVOID]);
			accum.add(f);
		}
		if((flags & EVADE) != 0){
			f = evade();
			f.mult(weightings[BIT_EVADE]);
			accum.add(f);
		}
		if((flags & FLEE) != 0){
			f = flee();
			f.mult(weightings[BIT_FLEE]);
			accum.add(f);
		}
		if((flags & FLOCK) != 0){
			f = flock();
			f.mult(weightings[BIT_FLOCK]);
			accum.add(f);
		}
		else {
			if((flags & SEPARATION) != 0){
				f = separation();
				f.mult(weightings[BIT_SEPARATION]);
				accum.add(f);
			}
			if((flags & ALIGNMENT) != 0){
				f = alignment();
				f.mult(weightings[BIT_ALIGNMENT]);
				accum.add(f);
			}
			if((flags & COHESION) != 0){
				f = cohesion();
				f.mult(weightings[BIT_COHESION]);
				accum.add(f);
			}
		}
		if((flags & SEEK) != 0){
			f = seek();
			f.mult(weightings[BIT_SEEK]);
			accum.add(f);
		}
		if((flags & ARRIVE) != 0){
			f = arrive();
			f.mult(weightings[BIT_ARRIVE]);
			accum.add(f);
		}
		if((flags & WANDER) != 0){
			f = wander();
			f.mult(weightings[BIT_WANDER]);
			accum.add(f);
		}
		if((flags & PURSUIT) != 0){
			f = pursuit();
			f.mult(weightings[BIT_PURSUIT]);
			accum.add(f);
		}
		if((flags & OFFSET_PURSUIT) != 0){
			f = offsetPursuit();
			f.mult(weightings[BIT_OFFSET_PURSUIT]);
			accum.add(f);
		}
		if((flags & INTERPOSE) != 0){
			f = interpose();
			f.mult(weightings[BIT_INTERPOSE]);
			accum.add(f);
		}
		if((flags & HIDE) != 0){
			f = hide();
			f.mult(weightings[BIT_HIDE]);
			accum.add(f);
		}
		if((flags & PATH) != 0){
			f = pathFollow();
			f.mult(weightings[BIT_PATH]);
			accum.add(f);
		}
		return accum;		
	}

	/**
	 * Uses the prioritized weighted average method to calculate the steering force.
	 * @return the calculated steering force as a Vector2D
	 */
	private ga_Vector2D calculatePrioritised(){
		double maxForce = owner.getMaxForce();
		ga_Vector2D accum = new ga_Vector2D();
		ga_Vector2D f = new ga_Vector2D();
		if((flags & WALL_AVOID) != 0){
			f = wallAvoidance();
			f.mult(weightings[BIT_WALL_AVOID]);
			if(!accumulateForce(accum, f, maxForce)) return accum;
		}
		if((flags & OBSTACLE_AVOID) != 0){
			f = obstacleAvoidance();
			f.mult(weightings[BIT_OBSTACLE_AVOID]);
			if(!accumulateForce(accum, f, maxForce)) return accum;
		}
		if((flags & EVADE) != 0){
			f = evade();
			f.mult(weightings[BIT_EVADE]);
			if(!accumulateForce(accum, f, maxForce)) return accum;
		}
		if((flags & FLEE) != 0){
			f = flee();
			f.mult(weightings[BIT_FLEE]);
			if(!accumulateForce(accum, f, maxForce)) return accum;
		}
		if((flags & FLOCK) != 0){
			f = flock();
			f.mult(weightings[BIT_FLOCK]);
			if(!accumulateForce(accum, f, maxForce)) return accum;
		}
		else {
			if((flags & SEPARATION) != 0){
				f = separation();
				f.mult(weightings[BIT_SEPARATION]);
				if(!accumulateForce(accum, f, maxForce)) return accum;
			}
			if((flags & ALIGNMENT) != 0){
				f = alignment();
				f.mult(weightings[BIT_ALIGNMENT]);
				if(!accumulateForce(accum, f, maxForce)) return accum;
			}
			if((flags & COHESION) != 0){
				f = cohesion();
				f.mult(weightings[BIT_COHESION]);
				if(!accumulateForce(accum, f, maxForce)) return accum;
			}
		}
		if((flags & SEEK) != 0){
			f = seek();
			f.mult(weightings[BIT_SEEK]);
			if(!accumulateForce(accum, f, maxForce)) return accum;
		}
		if((flags & ARRIVE) != 0){
			f = arrive();
			f.mult(weightings[BIT_ARRIVE]);
			if(!accumulateForce(accum, f, maxForce)) return accum;
		}
		if((flags & WANDER) != 0){
			f = wander();
			f.mult(weightings[BIT_WANDER]);
			if(!accumulateForce(accum, f, maxForce)) return accum;
		}
		if((flags & PURSUIT) != 0){
			f = pursuit();
			f.mult(weightings[BIT_PURSUIT]);
			if(!accumulateForce(accum, f, maxForce)) return accum;
		}
		if((flags & OFFSET_PURSUIT) != 0){
			f = offsetPursuit();
			f.mult(weightings[BIT_OFFSET_PURSUIT]);
			if(!accumulateForce(accum, f, maxForce)) return accum;
		}
		if((flags & INTERPOSE) != 0){
			f = interpose();
			f.mult(weightings[BIT_INTERPOSE]);
			if(!accumulateForce(accum, f, maxForce)) return accum;
		}
		if((flags & HIDE) != 0){
			f = hide();
			f.mult(weightings[BIT_HIDE]);
			if(!accumulateForce(accum, f, maxForce)) return accum;
		}
		if((flags & PATH) != 0){
			f = pathFollow();
			f.mult(weightings[BIT_PATH]);
			if(!accumulateForce(accum, f, maxForce)) return accum;
		}
		return accum;			
	}


	//--------------------- AccumulateForce ----------------------------------
	//
	// This function calculates how much of its max steering force the 
	// vehicle has left to apply and then applies that amount of the
	// force to add.
	//------------------------------------------------------------------------
	private boolean accumulateForce(ga_Vector2D runningTotal, ga_Vector2D forceToAdd, double maxForce){ 
		//calculate how much steering force the vehicle has used so far
		double magnitudeSoFar = runningTotal.length();
		//calculate how much steering force remains to be used by this vehicle
		double magnitudeLeft = maxForce - magnitudeSoFar;
		//calculate the magnitude of the force we want to add
		double magnitudeToAdd = forceToAdd.length();

		//if the magnitude of the sum of ForceToAdd and the running total
		//does not exceed the maximum force available to this vehicle, just
		//add together. Otherwise add as much of the ForceToAdd vector is
		//possible without going over the max.
		if (magnitudeToAdd < magnitudeLeft){
			runningTotal.add(forceToAdd);
			return true;
		}
		else {
			forceToAdd.normalize();
			forceToAdd.mult(magnitudeLeft);
			//add it to the steering force
			runningTotal.add(forceToAdd);
			return false;
		}
	}


	//--------------------------- Hide ---------------------------------------
	//
	//------------------------------------------------------------------------
	
	private ga_Vector2D hide(){
		return hide(owner, agents[AGENT_TO_EVADE]);
	}
	
	public ga_Vector2D hide(dg_EntityMover me, dg_EntityMover from){
		double distToNearest = Double.MAX_VALUE;
		ga_Vector2D bestHidingSpot = null;
		//		Obstacle closest = null;

		// This may be required by other behaviors
		if(obstacles == null)
			obstacles = world.getObstacles(me);
		if(obstacles == null || obstacles.size() < 2)
			return ga_Vector2D.ZERO;

		for(dg_EntityObstacle ob : obstacles){
			//calculate the position of the hiding spot for this obstacle
			ga_Vector2D hidingSpot = getHidingPosition(me, ob, from);

			//work in distance-squared space to find the closest hiding
			//spot to the agent
			double dist = ga_Vector2D.distSq(hidingSpot, me.getPos());

			if (dist < distToNearest){
				distToNearest = dist;
				bestHidingSpot = hidingSpot;
				//				closest = ob;
			}  
		}
		//if no suitable obstacles found then Evade the hunter
		if (bestHidingSpot == null)
			return evade(me, from);
		else // Go to hiding place
			return arrive(me, bestHidingSpot, FAST);
	}

	//------------------------- GetHidingPosition ----------------------------
	//
	// Given the position of a hunter, and the position and radius of
	// an obstacle, this method calculates a position DistanceFromBoundary 
	// away from its bounding radius and directly opposite the hunter
	//------------------------------------------------------------------------
	private ga_Vector2D getHidingPosition(dg_EntityMover me, dg_EntityObstacle ob, dg_a_EntityBase target){
		// calculate how far away the agent is to be from the chosen obstacle's
		// bounding radius

		double distAway = ob.getColRadius() + me.getColRadius();

		//calculate the heading toward the object from the hunter
		ga_Vector2D hidePos = ga_Vector2D.sub(ob.getPos(), target.getPos());
		hidePos.normalize();
		hidePos.mult(distAway);
		hidePos.add(ob.getPos());

		//scale it to size and add to the obstacles position to get
		//the hiding spot.
		return hidePos;
	}

	//---------------------------- Alignment ---------------------------------
	//
	// returns a force that attempts to align this agents heading with that
	// of its neighbors
	//------------------------------------------------------------------------
	private ga_Vector2D alignment(){
		return alignment(owner);
	}
	
	public ga_Vector2D alignment(dg_EntityMover me){
		ga_Vector2D avgHeading = new ga_Vector2D();
		if(movers == null)
			movers = world.getMovers(me, neighbourDist);
		if(movers == null || movers.size() < 2)
			return avgHeading;

		int neighbourCount = 0;
		double ndistSq = neighbourDist * neighbourDist;
		for(dg_EntityMover mover : movers){
			double distSq = ga_Vector2D.distSq(me.getPos(), mover.getPos());
			if(mover != me && mover != agents[AGENT_TO_EVADE] && distSq < ndistSq){
				avgHeading.add(mover.getHeading());
				neighbourCount++;
			}
		}
		if(neighbourCount > 0){
			avgHeading.div(neighbourCount);
			avgHeading.sub(me.getHeading());
			avgHeading.normalize();
		}
		return avgHeading;
	}

	
	//---------------------------- Separation --------------------------------
	//
	// this calculates a force that repels from the other neighbors
	//------------------------------------------------------------------------
	private ga_Vector2D separation(){
		return separation(owner);
	}
	
	public ga_Vector2D separation(dg_EntityMover me){  
		ga_Vector2D sForce = new ga_Vector2D();

		if(movers == null)
			movers = world.getMovers(me, neighbourDist);
		if(movers == null || movers.size() < 2)
			return sForce;

		int neighbourCount = 0;
		double ndistSq = neighbourDist * neighbourDist;
		for(dg_EntityMover mover : movers){
			double distSq = ga_Vector2D.distSq(me.getPos(), mover.getPos());
			if(mover != me && mover != agents[AGENT_TO_EVADE] && distSq < ndistSq){
				ga_Vector2D toAgent = ga_Vector2D.sub(me.getPos(), mover.getPos());
				toAgent.div(distSq);
				sForce.add(toAgent);
				neighbourCount++;
			}
		}
		if(neighbourCount > 0){
			sForce.normalize();
		}
		return sForce;
	}

	//-------------------------------- Cohesion ------------------------------
	//
	// returns a steering force that attempts to move the agent towards the
	// center of mass of the agents in its immediate area
	//------------------------------------------------------------------------
	private ga_Vector2D cohesion(){
		return cohesion(owner);
	}
	
	private ga_Vector2D cohesion(dg_EntityMover me){
		//first find the center of mass of all the agents
		ga_Vector2D centreOfMass = new ga_Vector2D();
		ga_Vector2D sForce = new ga_Vector2D();

		if(movers == null)
			movers = world.getMovers(me, neighbourDist);
		if(movers == null || movers.size() < 2)
			return sForce;

		//used to count the number of vehicles in the neighborhood
		int neighbourCount = 0;

		double ndistSq = neighbourDist * neighbourDist;
		for(dg_EntityMover mover : movers){
			double distSq = ga_Vector2D.distSq(me.getPos(), mover.getPos());
			if(mover != me && mover != agents[AGENT_TO_EVADE] && distSq < ndistSq){
				centreOfMass.add(mover.getPos());
				neighbourCount++;
			}
		}
		if(neighbourCount > 0){
			centreOfMass.div(neighbourCount);
			sForce = seek(me, centreOfMass);
			//the magnitude of cohesion is usually much larger than separation or
			//alignment so it usually helps to normalize it.
			sForce.normalize();
		}
		return sForce;
	}

	private ga_Vector2D flock(){
		return flock(owner);
	}
	
	public ga_Vector2D flock(dg_EntityMover me){
		ga_Vector2D sepForce = new ga_Vector2D();
		ga_Vector2D cohForce = new ga_Vector2D();
		ga_Vector2D alnForce = new ga_Vector2D();
		ga_Vector2D flockForce = new ga_Vector2D();

		if(movers == null)
			movers = world.getMovers(me, neighbourDist);
		
		// Intermediate values for calculation
		ga_Vector2D avgHeading = new ga_Vector2D();
		ga_Vector2D toAgent = new ga_Vector2D();
		
		int neighbourCount = 0;
		double distSq;
		
		double ndistSq = neighbourDist * neighbourDist;

		for(dg_EntityMover mover : movers){
			distSq = ga_Vector2D.distSq(me.getPos(), mover.getPos());
			if(mover != me && mover != agents[AGENT_TO_EVADE] && distSq < ndistSq){
				neighbourCount++;
				// Cohesion
				cohForce.add(mover.getPos());
				// Alignment
				avgHeading.add(mover.getHeading());
				// Separation
				toAgent.set(me.getPos());
				toAgent.sub(mover.getPos());
				toAgent.div(distSq);
				sepForce.add(toAgent);
			}			
		}
		if(neighbourCount > 0){
			// Cohesion
			cohForce.div(neighbourCount);
			cohForce.sub(me.getPos());
			cohForce.normalize();
			cohForce.mult(me.getMaxSpeed());
			cohForce.sub(me.getVelocity());
			cohForce.normalize();
			cohForce.mult(weightings[BIT_COHESION]);
			
			// Separation
			sepForce.normalize();
			sepForce.mult(weightings[BIT_SEPARATION]);
			
			// Alignment
			avgHeading.div(neighbourCount);
			avgHeading.sub(me.getHeading());
			alnForce.set(avgHeading);
			alnForce.mult(weightings[BIT_ALIGNMENT]);

			// Add them up
			flockForce.set(cohForce);
			flockForce.add(sepForce);
			flockForce.add(alnForce);
		}
		return flockForce;
	}

	//--------------------------- Interpose ----------------------------------
	//
	// Given two agents, this method returns a force that attempts to 
	// position the vehicle between them
	//------------------------------------------------------------------------
	private ga_Vector2D interpose(){
		return interpose(owner, agents[AGENT0], agents[AGENT1]);
	}

	public ga_Vector2D interpose(dg_EntityMover me, dg_EntityMover m0, dg_EntityMover m1){
		if(m0 == null || m1 == null){
			System.out.println("Interpose: need to set agents");
			return ga_Vector2D.ZERO;
		}
		//first we need to figure out where the two agents are going to be at 
		//time T in the future. This is approximated by determining the time
		//taken to reach the mid way point at the current time at at max speed.
		ga_Vector2D currMidPoint = ga_Vector2D.add(m0.getPos(), m1.getPos());
		currMidPoint.div(2.0);
		double timeToReachMidPoint = ga_Vector2D.dist(me.getPos(), currMidPoint) /   me.getMaxSpeed();

		//now we have T, we assume that agent A and agent B will continue on a
		//straight trajectory and extrapolate to get their future positions
		ga_Vector2D agent0Pos = ga_Vector2D.mult(m0.getVelocity(), timeToReachMidPoint);
		agent0Pos.add(m0.getPos());
		ga_Vector2D agent1Pos = ga_Vector2D.mult(m1.getVelocity(), timeToReachMidPoint);
		agent1Pos.add(m1.getPos());

		//calculate the mid point of these predicted positions
		ga_Vector2D target = ga_Vector2D.add(agent0Pos, agent1Pos);
		target.div(2.0);
		return arrive(me, target, FAST);
	}


	//--------------------------- WallAvoidance --------------------------------
	//
	// This returns a steering force that will keep the agent away from any
	// walls it may encounter
	//------------------------------------------------------------------------
	private ga_Vector2D wallAvoidance(){
		return wallAvoidance(owner);
	}
	
	public ga_Vector2D wallAvoidance(dg_EntityMover me){
		ga_Vector2D sForce = ga_Vector2D.ZERO;

		// This maybe required by other behaviors
		if(walls == null)
			walls = world.getWalls(me);
		if(walls == null || walls.size() < 2)
			return sForce;

		ga_Vector2D[] feelers = getFeelers(me);

		// Distance to currently calculated interception point
		double distToThisIP    = 0.0;
		// Records distance to nearest IP found so far
		double distToClosestIP = Double.MAX_VALUE;

		dg_EntityWall closestWall = null;		// The current wall with the nearest intersection point
		ga_Vector2D closestPoint = null;  	// The current closest intersection point
		int feeler = 0;					// found with this feeler


		// For each feeler in turn
		for (int flr=0; flr < feelers.length; ++flr)
		{
			// test against each wall for any intersection points
			for(dg_EntityWall wall : walls){
				ga_Vector2D intercept = ga_Geometry2D.line_line_p(me.getPos(),
						feelers[flr],
						wall.start(),
						wall.end());
				if(intercept != null){
					distToThisIP = ga_Vector2D.dist(intercept, me.getPos());
					// If this is the closest found so far remember it
					if (distToThisIP < distToClosestIP){
						distToClosestIP = distToThisIP;
						closestWall = wall;
						closestPoint = intercept;
						feeler = flr;
					}
				}
			} // next wall
		} // next feeler

		// If an intersection point has been detected, calculate a force  
		// that will direct the agent away
		if (closestWall != null){
			//calculate by what distance the projected position of the agent
			//will overshoot the wall
			ga_Vector2D overShoot = ga_Vector2D.sub(feelers[feeler], closestPoint);

			//create a force in the direction of the wall normal, with a 
			//magnitude of the overshoot
			sForce = ga_Vector2D.mult(closestWall.norm(), overShoot.length());
		}
		return sForce;
	}

	/**
	 * FOR INTERNAL USE ONLY <br>
	 * Calculates and returns an array of feelers around the vehicle. It is only made public
	 * so that they can be displayed when testing.
	 * 
	 * @return array of whisker vectors
	 */
	public ga_Vector2D[] getFeelers(dg_EntityMover me){
		return ga_Transformations.createWhiskers( nbrWhiskers, // NumWhiskers
				whiskerLength, // WhiskerLength
				whiskerFOV, //fov
				me.getHeading(), // facing
				me.getPos() // origin
		);
	}

	//---------------------- ObstacleAvoidance -------------------------------
	//
	// Given a list of Obstacles, this method returns a steering force
	// that will prevent the agent colliding with the closest obstacle
	//------------------------------------------------------------------------
	private ga_Vector2D obstacleAvoidance(){
		return obstacleAvoidance(owner);
	}
	
	public ga_Vector2D obstacleAvoidance(dg_EntityMover me){
		ga_Vector2D desiredVelocity = new ga_Vector2D();

		// This may be required by other behaviors
		if(obstacles == null)
			obstacles = world.getObstacles(me);
		if(obstacles == null || obstacles.isEmpty())
			return desiredVelocity;

		dg_EntityObstacle closestIO = null;
		double distToClosestIP = Double.MAX_VALUE;
		ga_Vector2D localPosOfClosestIO = null;
		double dboxLength = detectBoxLength * (1.0 + me.getSpeed() / me.getMaxSpeed());

		ga_Vector2D velocity = ga_Vector2D.normalize(me.getVelocity());
		ga_Vector2D vside = velocity.getPerp();

		for(dg_EntityObstacle ob : obstacles){
			ga_Vector2D localPos = ga_Transformations.pointToLocalSpace(ob.getPos(), velocity, vside, me.getPos());
			//			double expandedRadius = ob.getNoGoRadius();
			double expandedRadius = ob.getColRadius() + me.getColRadius();
			if(localPos.x >= 0 && localPos.x < dboxLength + expandedRadius){
				if(m_MathFast.abs(localPos.y) < expandedRadius){
					double cX = localPos.x;
					double cY = localPos.y;
					double sqrtPart = m_MathFast.sqrt(expandedRadius * expandedRadius - cY*cY);

					double ip = cX - sqrtPart;
					if(ip <= 0)
						ip = cX + sqrtPart;

					if(ip < distToClosestIP){
						distToClosestIP = ip;
						closestIO = ob;
						localPosOfClosestIO = localPos;
					}
				}
			}
		} // end of for loop
		ga_Vector2D sForce = new ga_Vector2D();
		if(closestIO != null){
			double multiplier = 1.0 + (dboxLength - localPosOfClosestIO.x) / dboxLength;
			sForce.y = (closestIO.getColRadius() - localPosOfClosestIO.y) * multiplier;
			double breakingWeight = 0.01;
			sForce.x = (closestIO.getColRadius() - localPosOfClosestIO.x) * breakingWeight;
			desiredVelocity = ga_Transformations.vectorToWorldSpace(sForce, velocity, vside);
		}
		return desiredVelocity;
	}


	//------------------------------- Seek -----------------------------------
	//
	// Given a target, this behavior returns a steering force which will
	// direct the agent towards the target
	//------------------------------------------------------------------------
	private ga_Vector2D seek(){
		return seek(owner, gotoTarget);
	}

	public ga_Vector2D seek(dg_EntityMover me, ga_Vector2D target){
		ga_Vector2D desiredVelocity = ga_Vector2D.sub(target, me.getPos());
		desiredVelocity.normalize();
		desiredVelocity.mult(me.getMaxSpeed());
		desiredVelocity.sub(me.getVelocity());
		return desiredVelocity;
	}


	//----------------------------- Flee -------------------------------------
	//
	// Does the opposite of Seek
	//------------------------------------------------------------------------
	private ga_Vector2D flee(){
		return flee(owner, fleeTarget);
	}

	public ga_Vector2D flee(dg_EntityMover me, ga_Vector2D target){
		//only flee if the target is within 'panic distance'. Work in distance
		//squared space.
		ga_Vector2D desiredVelocity = ga_Vector2D.ZERO;
		if(ga_Vector2D.distSq(target, me.getPos()) < fleeDistance * fleeDistance){
			desiredVelocity = ga_Vector2D.sub(me.getPos(), target);
			desiredVelocity.normalize();
			desiredVelocity.mult(me.getMaxSpeed());
			desiredVelocity.sub(me.getVelocity());
		}
		return desiredVelocity;
	}


	//--------------------------- Wander -------------------------------------
	//
	// This behavior makes the agent wander about randomly
	//------------------------------------------------------------------------
	private ga_Vector2D wander(){ 
		// this behavior is dependent on the update rate, so this line must
		// be included when using time independent frame rate.
		wanderAngleDelta = wanderAngleJitter * deltaTime;
		wanderAngle += wanderAngleDelta * m_MathUtils.RandomClamped();
		// Not really essential considering the range of the type double.
		if(wanderAngle < WANDER_MIN_ANGLE)
			wanderAngle += WANDER_ANGLE_RANGE;
		else if(wanderAngle > WANDER_MAX_ANGLE)
			wanderAngle -= WANDER_ANGLE_RANGE;
		
		// Calculate position on wander circle
		ga_Vector2D targetLocal = new ga_Vector2D(wanderRadius * m_MathFast.cos(wanderAngle), wanderRadius * m_MathFast.sin(wanderAngle));
		// Add wander distance
		targetLocal.add(wanderDist, 0);

		// project the target into world space
		ga_Vector2D targetWorld = ga_Transformations.pointToWorldSpace(targetLocal,
				owner.getHeading(),   // was heading()
				owner.getSide(), 
				owner.getPos());

		// and steer towards it
		targetWorld.sub(owner.getPos());
		return targetWorld; 
	}


//	private Vector2D wander(){ 
//		// this behavior is dependent on the update rate, so this line must
//		// be included when using time independent frame rate.
//		double jitter = wanderJitter * deltaTime;
//		// first, add a small random vector to the target's position
//		Vector2D deltaTarget = new Vector2D(MathUtils.RandomClamped() * jitter,
//				MathUtils.RandomClamped() * jitter);
//		wanderTarget.add(deltaTarget);
//
//		// re-project this new vector back on to a unit circle
//		wanderTarget.normalize();
//
//		// increase the length of the vector to the same as the radius
//		// of the wander circle
//		wanderTarget.mult(wanderRadius);
//		// move the target into a position WanderDist in front of the agent
//		Vector2D targetLocal = new Vector2D(wanderTarget);
//		targetLocal.add(wanderDist, 0);
//
//		// project the target into world space
//		Vector2D targetWorld = Transformations.pointToWorldSpace(targetLocal,
//				owner.getHeading(),   // was heading()
//				owner.getSide(), 
//				owner.getPos());
//
//		// and steer towards it
//		targetWorld.sub(owner.getPos());
//		return targetWorld; 
//	}



	////--------------------------- Arrive -------------------------------------
	//
	// This behavior is similar to seek but it attempts to arrive at the
	// target with a zero velocity
	//------------------------------------------------------------------------
	public ga_Vector2D arrive(final dg_EntityMover me, final ga_Vector2D target, int rate){
		ga_Vector2D desiredVelocity = ga_Vector2D.ZERO;
		double dist = ga_Vector2D.dist(target, me.getPos());
		if(dist > arriveDist){
			double speed = dist / DECEL_TWEEK[rate];
			speed = m_MathFast.min(speed, me.getMaxSpeed());
			desiredVelocity = ga_Vector2D.sub(target, me.getPos());
			desiredVelocity.mult(speed / dist);
			desiredVelocity.sub( me.getVelocity());
		}
		return desiredVelocity;		
	}

	private ga_Vector2D arrive(){
		return arrive(owner, gotoTarget, arriveRate);		
	}


	//------------------------- Offset Pursuit -------------------------------
	//
	// Produces a steering force that keeps a vehicle at a specified offset
	// from a leader vehicle
	//------------------------------------------------------------------------
	public ga_Vector2D offsetPursuit(dg_EntityMover me, dg_EntityMover leader, ga_Vector2D offset){
		//calculate the offset's position in world space
		ga_Vector2D worldOffsetPos = ga_Transformations.pointToWorldSpace( offset,
				leader.getHeading(),
				leader.getSide(),
				leader.getPos());

		ga_Vector2D toOffset = ga_Vector2D.sub(worldOffsetPos, me.getPos());

		//the lookahead time is proportional to the distance between the leader
		//and the pursuer; and is inversely proportional to the sum of both
		//agent's velocities
		double lookAheadTime = toOffset.length() / (me.getMaxSpeed() + leader.getSpeed());

		//now Arrive at the predicted future position of the offset
		ga_Vector2D target = new ga_Vector2D(leader.getVelocity());
		target.mult(lookAheadTime);
		target.add(worldOffsetPos);
		return arrive(me, target, FAST);
	}

	private ga_Vector2D offsetPursuit(){
		return offsetPursuit(owner, agents[AGENT_TO_PURSUE], pursueOffset);
//		//calculate the offset's position in world space
//		Vector2D worldOffsetPos = Transformations.pointToWorldSpace(pursueOffset,
//				agents[AGENT_TO_PURSUE].getHeading(),
//				agents[AGENT_TO_PURSUE].getSide(),
//				agents[AGENT_TO_PURSUE].getPos());
//
//		Vector2D toOffset = Vector2D.sub(worldOffsetPos, owner.getPos());
//
//		//the lookahead time is proportional to the distance between the leader
//		//and the pursuer; and is inversely proportional to the sum of both
//		//agent's velocities
//		double lookAheadTime = toOffset.length() / 
//		(owner.getMaxSpeed() + agents[AGENT_TO_PURSUE].getSpeed());
//
//		//now Arrive at the predicted future position of the offset
//		gotoTarget.set(agents[AGENT_TO_PURSUE].getVelocity());
//		gotoTarget.mult(lookAheadTime);
//		gotoTarget.add(worldOffsetPos);
//		arriveRate = FAST;
//		return arrive();
	}


	/**  =========================================================================================
	 * Pursuit
	 * Seek to the agent's predicted position based on velocities and estimated time to intercept
	 * @return
	 */
	public ga_Vector2D pursuit(dg_EntityMover me, dg_EntityMover toPursue){
		ga_Vector2D toAgent = ga_Vector2D.sub(toPursue.getPos(), me.getPos());
		double relativeHeading = me.getHeading().dot(toPursue.getHeading());

		if ( (toAgent.dot(me.getHeading()) > 0) &&  (relativeHeading < -0.95)){  //acos(0.95)=18 degs
			gotoTarget.set(toPursue.getPos());
			return seek();
		}
		// the lookahead time is proportional to the distance between the agent
		// and the pursuer; and is inversely proportional to the sum of the
		// agent's velocities
		double lookAheadTime = toAgent.length() / (me.getMaxSpeed() + toPursue.getVelocity().length());
		ga_Vector2D target = new ga_Vector2D(toPursue.getPos());
		target.add(ga_Vector2D.mult(toPursue.getVelocity(), lookAheadTime));
		return seek(me, target);
	}
	
//	public Vector2D pursuit(MovingEntity toPursue){
//		return pursuit(owner, toPursue);
//	}
	
	private ga_Vector2D pursuit(){
		return pursuit(owner, agents[AGENT_TO_PURSUE]);
//		Vector2D toAgent = Vector2D.sub(agents[AGENT_TO_PURSUE].getPos(), owner.getPos());
//		double relativeHeading = owner.getHeading().dot(agents[AGENT_TO_PURSUE].getHeading());
//
//		if ( (toAgent.dot(owner.getHeading()) > 0) &&  (relativeHeading < -0.95)){  //acos(0.95)=18 degs
//			gotoTarget.set(agents[AGENT_TO_PURSUE].getPos());
//			return seek();
//		}
//		// the lookahead time is proportional to the distance between the agent
//		// and the pursuer; and is inversely proportional to the sum of the
//		// agent's velocities
//		double lookAheadTime = toAgent.length() / (owner.getMaxSpeed() + agents[AGENT_TO_PURSUE].getVelocity().length());
//		gotoTarget.set(agents[AGENT_TO_PURSUE].getPos());
//		gotoTarget.add(Vector2D.mult(agents[AGENT_TO_PURSUE].getVelocity(), lookAheadTime));
//		return seek();
	}

	/**  =========================================================================================
	 * Evade
	 * Flee from the estimated position of the pursuer.
	 */
	private ga_Vector2D evade(){
		return evade(owner, agents[AGENT_TO_EVADE]);
//		Vector2D fromAgent = Vector2D.sub(agents[AGENT_TO_EVADE].getPos(), owner.getPos());
//
//		//the lookahead time is proportional to the distance between the pursuer
//		//and the pursuer; and is inversely proportional to the sum of the
//		//agents' velocities
//		double lookAheadTime = fromAgent.length() / (owner.getMaxSpeed() + agents[AGENT_TO_EVADE].getVelocity().length());
//
//		fleeTarget.set(agents[AGENT_TO_EVADE].getPos());
//		fleeTarget.add(Vector2D.mult(agents[AGENT_TO_EVADE].getVelocity(), lookAheadTime));
//
//		return flee();
	}
	
	public ga_Vector2D evade(dg_EntityMover me, dg_EntityMover evadeAgent){
		ga_Vector2D target = new ga_Vector2D(evadeAgent.getPos());
		ga_Vector2D fromAgent = ga_Vector2D.sub(target, me.getPos());

		//the lookahead time is proportional to the distance between the pursuer
		//and the pursuer; and is inversely proportional to the sum of the
		//agents' velocities
		double lookAheadTime = fromAgent.length() / (me.getMaxSpeed() + evadeAgent.getVelocity().length());

		target.add(ga_Vector2D.mult(evadeAgent.getVelocity(), lookAheadTime));

		return flee(me, target);	
	}

	//------------------------------- FollowPath -----------------------------
	//
	// Given a series of Vector2Ds, this method produces a force that will
	// move the agent along the waypoints in order. The agent uses the
	//'Seek' behavior to move to the next waypoint - unless it is the last
	// waypoint, in which case it 'Arrives'
	//------------------------------------------------------------------------
	private ga_Vector2D pathFollow()
	{ 
		if (!path.isEmpty()) {
			dag_GraphNode g=path.getFirst();
			ga_Vector2D target = new ga_Vector2D(g.x, g.y);
			double dist = (path.size() > 1) ? pathSeekDist : pathArriveDist;
			dist *= dist; // Square the distance
			// If we are close enough to target then remove it from the list
			// for next time.
			if (ga_Vector2D.distSq(target, owner.getPos()) < dist) {
				path.remove(g);
				if (owner.isTracking) owner.arrive(g);
			}
			return (path.size() == 1) ? arrive(owner, target, FAST) : seek(owner, target);
		}
		disableBehaviours(PATH);
		return ga_Vector2D.ZERO;
	}

	/**
	 * Enable the behavior or behaviors
	 * @param behaviours the behaviors to enable.
	 */
	public void enableBehaviours(int behaviours){
		this.flags |= behaviours;
	}

	/**
	 * Disable the behavior or behaviors
	 * @param behaviours the behaviors to disable.
	 */
	public void disableBehaviours(int behaviours){
		behaviours = ALL_SB_MASK - behaviours;
		flags &= behaviours;
	}

	/**
	 * Returns true if the indicated behavior(s) are (all) active.
	 * @param behavior the behavior or behaviors we are interested in.
	 * @return true if (all) the behavior(s) are active.
	 */
	public boolean isBehaviourOn(int behavior){
		return ((flags & behavior) == behavior);
	}

	/**
	 * Get the force calculation weighting for a <b>single</b> behavior. <br>
	 * @param behavior should represent a <b>single</b> behavior
	 * @return weighting
	 */
	public double getWeight(int behavior){
		return weightings[bitPos(behavior)];
	}

	/**
	 * Set the force calculation weighting for a particular behavior or behaviors.
	 * @param behavior the behaviors ORed together
	 * @param weight
	 */
	public void setWeight(int behavior, double weight){
		int counter = 0;
		while(behavior > 0){
			if((behavior & 1) == 1)
				weightings[counter] = weight;
			counter++;
			behavior >>= 1;
		}
	}

	/**
	 * Creates and returns a deep copy of this object.
	 */
	public Object clone(){
		dg_Steering copy = null;
		try {
			copy = (dg_Steering)super.clone();
			copy.weightings = new double[this.weightings.length];
			System.arraycopy(weightings, 0, copy.weightings, 0, copy.weightings.length);
			copy.gotoTarget = new ga_Vector2D(gotoTarget);
			copy.fleeTarget = new ga_Vector2D(fleeTarget);
			copy.agents = new dg_EntityVehicle[agents.length];
			System.arraycopy(agents, 0, copy.agents, 0, copy.agents.length);
			copy.pursueOffset = new ga_Vector2D(pursueOffset);
			for(dag_GraphNode n : path)
				copy.path.add(new dag_GraphNode(n));
		} 
		catch (CloneNotSupportedException e) {
			System.out.println("Failed to clone this object.");
		}
		return copy;
	}

	/**
	 * This method provides a String object that describes the active behaviors and their details.
	 * This is useful when debugging.
	 */
	public String toString(){
		StringBuilder s = new StringBuilder("================= STEERING BEHAVIOR ==================\n  Active behaviors >>>  ");
		int f = flags, idx = 0;
		while(f > 0){
			if( (f & 1) != 0)
				s.append(bnames[idx]);
			f >>= 1;
		idx++;			
		}
		f = flags;
		if((f & WALL_AVOID) != 0) 
			s.append(d_MessageString.build("\n\tWall avoidance: Weighting {0}  Whiskers: number {1}  FOV {2}  length {3}", getWeight(WALL_AVOID), nbrWhiskers, whiskerFOV, whiskerLength));		
		if((f & OBSTACLE_AVOID) != 0) s.append(d_MessageString.build("\n\tObstacle avoidance: Weighting {0}", getWeight(OBSTACLE_AVOID)));
		if((f & EVADE) != 0) s.append(d_MessageString.build("\n\tEvade: Weighting {0}  form {1}", getWeight(EVADE), agents[AGENT_TO_EVADE]));
		if((f & FLEE) != 0) s.append(d_MessageString.build("\n\tFlee: Weighting {0}  from {1}", getWeight(FLEE), agents[AGENT_TO_EVADE]));
		if((f & SEPARATION) != 0) s.append(d_MessageString.build("\n\tSeparation: Weighting {0}", getWeight(SEPARATION)));
		if((f & ALIGNMENT) != 0) s.append(d_MessageString.build("\n\tAlignment: Weighting {0}", getWeight(ALIGNMENT)));
		if((f & COHESION) != 0) s.append(d_MessageString.build("\n\tCohesion: Weighting {0}", getWeight(COHESION)));
		if((f & SEEK) != 0) s.append(d_MessageString.build("\n\tSeek: Weighting {0}  for {1}", getWeight(SEEK), gotoTarget));
		if((f & ARRIVE) != 0) s.append(d_MessageString.build("\n\tArrive: Weighting {0}  target is {1}", getWeight(ARRIVE), gotoTarget));
		if((f & WANDER) != 0) 
			s.append(d_MessageString.build("\n\tWander: Weighting {0}  Whiskers: jitter {1}  radius {2}  distance {3}", getWeight(WANDER), wanderAngleJitter, wanderRadius, wanderDist));		
		if((f & PURSUIT) != 0) s.append(d_MessageString.build("\n\tPursue: Weighting {0}  pursue {1}", getWeight(PURSUIT), agents[AGENT_TO_PURSUE]));
		if((f & OFFSET_PURSUIT) != 0) s.append(d_MessageString.build("\n\tOffset Pursuue: Weighting {0}  follow {1}  offset {2}", getWeight(OFFSET_PURSUIT), agents[AGENT_TO_PURSUE], pursueOffset));
		if((f & INTERPOSE) != 0) s.append(d_MessageString.build("\n\tInterpose: Weighting {0}  between {1} and {2}", getWeight(FLEE), agents[AGENT0], agents[AGENT1]));
		if((f & HIDE) != 0) s.append(d_MessageString.build("\n\tHide: Weighting {0}", getWeight(HIDE)));
		if((f & PATH) != 0) s.append(d_MessageString.build("\n\tPath: Weighting {0}", getWeight(PATH)));
		s.append("\n=======================================================\n");
		return new String(s);
	}

	// Some static helper methods and variables
	private static String[] bnames = new String[] {
		" wall-avoid",	
		" obstacle-avoid",
		" evade ",
		" flee ",
		" separation ", 
		" alignment ",
		" cohesion ",
		" seek ",
		" arrive ",
		" wander ",
		" pursuit ", 
		" offset-pursuit",
		" interpose ",
		" hide ",
		" path "
	};

	/**
	 * For a given a bit mask find the position of the least significant bit.
	 * @param flag
	 * @return position of the least significant bit.
	 */
	private static int bitPos(int flag){
		int counter;
		for (counter = 0; flag > 1; flag >>= 1, counter++);
		return counter;		
	}

} // End of class
