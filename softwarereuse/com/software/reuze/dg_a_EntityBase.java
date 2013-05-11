package com.software.reuze;

import com.software.reuze.dg_StateMachine;
import com.software.reuze.dg_Telegram;
import com.software.reuze.vg_i_Renderer;

/**
 * This abstract class is the base for all game entities. <br>
 * 
 * Derived classes should call one of the 3 constructors to ensure that all game entities 
 * are given a unique ID number which can be used for messaging when using state machines.
 * 
 * @author Peter Lager
 *
 */
public abstract class dg_a_EntityBase implements Comparable<dg_a_EntityBase> {

	private static int nextID = 1000;
	
	protected final int entityID;
	protected String name = "";

	protected dg_StateMachine sm = null;

	protected vg_i_Renderer renderer = null;
	protected boolean visible = true;
	
	// Its center location in the world
	protected ga_Vector2D pos = new ga_Vector2D();

	// The length of this entity's bounding radius
	protected double colRadius;

	/**
	 * Default constructor will give the entity a unique ID and name.
	 */
	public dg_a_EntityBase(){
		entityID = nextID++;
		name = "E"+ entityID;
	}

	/**
	 * Constructor will give the entity a unique ID. 
	 * @param name the name of this entity - default name is used if none provided.
	 */
	public dg_a_EntityBase(String name){
		entityID = nextID++;
		if(name == null || name.length() == 0)
			this.name = "E"+ entityID;
		else
			this.name = name;
	}

	/**
	 * Constructor will give the entity a unique ID. 
	 * @param name the name of this entity - default name is used if none provided.
	 * @param pos the world position for this entity
	 * @param colRadius the bounding radius for this entity
	 */
	public dg_a_EntityBase(String name, ga_Vector2D pos, double colRadius) {
		entityID = nextID++;
		if(name == null || name.length() == 0)
			this.name = "E"+ entityID;
		else
			this.name = name;
		this.pos.set(pos);
		this.colRadius = colRadius;
	}
	
	public dg_a_EntityBase(dg_a_EntityBase eb) {
		entityID = nextID++;
		this.name = "E"+ entityID;
		this.colRadius=eb.colRadius;
		this.sm=eb.sm;
		this.visible=eb.visible;
		this.renderer=eb.renderer;
		this.pos.set(eb.pos.x,eb.pos.y);
	}

	/**
	 * Get the entity's unique ID number
	 * @return
	 */
	public int getID(){
		return entityID;
	}

	/**
	 * Is this entity visible
	 * @return the visible
	 */
	public boolean isVisible() {
		return visible;
	}

	/**
	 * Set the entity's visibility
	 * @param visible true or false
	 */
	public void setVisible(boolean visible){
		this.visible = visible;
	}
	
	/**
	 * Get the bounding radius for this object.
	 * @return the collision radius
	 */
	public double getColRadius() {
		return colRadius;
	}

	/**
	 * Set the bounding radius for this object
	 * @param colRadius the collision radius to set
	 */
	public void setColRadius(double colRadius) {
		this.colRadius = colRadius;
	}

	/**
	 * Determine whether this entity is inside or part inside the domain. This method is
	 * used by the world draw method to see if this entity should be drawn.
	 * @param view the world domain
	 * @return true if any part of this entity is inside the domain
	 */
	public abstract boolean isInDomain(dg_EntityDomainRectangular view);

	/**
	 * Determine whether the given world position is over a given entity.
	 * @param px
	 * @param py
	 * @return
	 */
	public abstract boolean isOver(double px, double py);

	/**
	 * Default update method for any entity in the world.
	 * @param deltaTime elapsed time since last update
	 * @param world the game world object
	 */
	public void update(double deltaTime, dg_World world){
	}
	
	/**
	 * If this entity has a state machine then call its update method.
	 * @param deltaTime elapsed time since last update (seconds)
	 * @param world the game world object
	 */
	public void updateStateMachine(double deltaTime, dg_World world){
		if(sm != null)
			sm.update(deltaTime, world);
	}
	
	/**
	 * Add a render object to this entity. This is essential if you want to display 
	 * the entity. 
	 * @param renderer the renderer to use (it must implement Renderer)
	 */
	public void setRenderer(vg_i_Renderer renderer){
		this.renderer = renderer;
	}
	
	/**
	 * Get the render object. This is useful if you want to change how an object is drawn
	 * during a game. In most cases you will have to cast it to the appropriate type before
	 * using it.
	 * @return the render object.
	 */
	public vg_i_Renderer getRenderer(){
		return renderer;
	}


	/**
	 * Set the current position for this entity.
	 * @param x world x position
	 * @param y world y position
	 */
	public void setPos(double x, double y){
		pos.x = x;
		pos.y = y;
	}
	
	/**
	 * Move the current position for this entity.
	 * @param x world x distance to move
	 * @param y world y distance to move
	 */
	public void incPos(double x, double y){
		pos.x += x;
		pos.y += y;
	}
	
	/**
	 * Get the current position.
	 * @return current position
	 */
	public ga_Vector2D getPos(){
		return pos;
	}
	
	/**
	 * Change the name of this entity
	 * @param name the new name to use
	 */
	public void setName(String name){
		this.name = name;
	}
	
	/**
	 * Get the current name of the entity.
	 * @return
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * Determines whether the two points given are 'either side of the object' if true then 
	 * the two positions are not visible to each other.
	 * @param x0 
	 * @param y0
	 * @param x1
	 * @param y1
	 * @return
	 */
	public abstract boolean isEitherSide(double x0, double y0, double x1, double y1);

	/**
	 * Determines whether the two points given are 'either side of the object' if true then 
	 * the two positions are not visible to each other.
	 * @param p0
	 * @param p1
	 * @return
	 */
	public boolean isEitherSide(ga_Vector2D p0, ga_Vector2D p1){
		return isEitherSide(p0.x, p0.y, p1.x, p1.y);
	}

	/**
	 * Returns a reference to the entity's state machine or
	 * null if there is no SM
	 * @return
	 */
	public dg_StateMachine getStateMachine(){
		return sm;
	}

	/**
	 * DO NOT CALL THIS METHOD DIRECTLY <br>
	 * Handle a telegram message sent by another entity. <br>
	 * Do nothing if this entity does not have a state machine
	 * @param msg the telegram being sent
	 * @return true if the state machine handled the message else false
	 */
	public boolean handleMessage(dg_Telegram msg){
		return (sm == null) ? false : sm.onMessage(msg);
	}

	/**
	 * DO NOT CALL THIS METHOD DIRECTLY <br>
	 * The main update method for an entity, called during the world update.
	 * 
	 * @param deltaTime
	 */
	public void update(double deltaTime){}
	
	/**
	 * DO NOT CALL THIS METHOD DIRECTLY <br>
	 * If this shape has a renderer and is visible, call the renderer's draw method passing all 
	 * appropriate
	 * 
	 * Override in child classes
	 */
	public void draw(dg_World world){
		if(renderer != null && visible){
			// 								zero velocity and heading
			renderer.draw((float) pos.x, (float) pos.y, 0, 0, 0, 0 , this);
		}
	}

	/**
	 * Compare 2 entities based on their ID numbers
	 */
	public int compareTo(dg_a_EntityBase ga){
		if(ga != null)
			return entityID==ga.entityID?0:(entityID>ga.entityID?1:-1);
		else
			return 1;  //larger than null
	}

	/**
	 * Entity ID number and name and position returned as a String
	 */
	public String toString(){
		return name +" "+pos;
	}
}
