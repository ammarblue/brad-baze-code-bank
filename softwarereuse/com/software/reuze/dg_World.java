package com.software.reuze;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;

import com.software.reuze.dg_EntityBuilding;
import com.software.reuze.dg_EntityDomainRectangular;
import com.software.reuze.dg_EntityMover;
import com.software.reuze.dg_EntityObstacle;
import com.software.reuze.dg_EntityWall;
import com.software.reuze.dg_TelegramDispatcher;
import com.software.reuze.dg_a_EntityBase;
import com.software.reuze.ga_Geometry2D;
import com.software.reuze.pt_StopWatch;
import com.software.reuze.ga_Vector2;


/**
 * This class represents the '2D game play area' on which all the game entities are placed. Internally all world
 * coordinates are stored as doubles (rather than floats) to minimize rounding errors, particularly when using 
 * steering behaviors. <br>
 * This means the size of the world is only limited by the range of numbers that can be stored in the double data
 * type ( i.e. +/- 1.7976931348623157e+308) <br>
 *   
 * This class provides the core functionality for maintaining collections of all the game entities. <br>
 *  
 * When there are a large number of game entities, cell space partitioning (CSP) can significantly reduce the CPU 
 * time needed to update all the moving entities. It will also reduce the rendering time by not attempting to draw
 * entities that fall outside of the viewable are. This is normally at the expense of additional memory needed for 
 * the data structures.<br>
 * Depending on which constructor is used when creating the 'world' object determines whether CSP is used or not.
 * The CSP implementation is totally transparent to the user - so apart from changing the constructor used no other
 * changes are needed in the users code. <br>
 *  
 * @author Peter Lager
 *
 */
public class dg_World {

	// Used to calculate the average update time
	private static pt_StopWatch timer = new pt_StopWatch();
	private static double cumTime;
	private static int count = 0;

	// This will be created when the first telegram is sent
	public static HashMap<Integer, dg_a_EntityBase> allEntities = null;

	// For use with and without partitions
	private Set<dg_EntityBuilding> buildings;
	private Set<dg_EntityWall> walls;
	private Set<dg_EntityObstacle> obstacles;
	private Set<dg_EntityMover> movers;

	// For use with partitions
	private HashMap<ga_Vector2, HashSet<dg_EntityBuilding>> building_parts;
	private HashMap<ga_Vector2, HashSet<dg_EntityWall>> wall_parts;
	private HashMap<ga_Vector2, HashSet<dg_EntityObstacle>> obstacle_parts;
	private HashMap<ga_Vector2, HashSet<dg_EntityMover>> moving_parts;

	// holds moving entities removed during updates
	private Stack<dg_EntityMover> removeStack=new Stack<dg_EntityMover>();
	
	private boolean stateMachinesOn = false;

	private boolean noOverlap = true;

	private final boolean cspOn;
	private final double  partSize;
	private final double partOverlap; 

	// Used to collect references to anything to be drawn
	// used to avoid multiple drawings in partition mode
	private Set<dg_a_EntityBase> drawSet = new TreeSet<dg_a_EntityBase> ();

	/** Number of buildings in this world */
	public int nbr_buildings = 0;
	/** Number of obstacles in this world */
	public int nbr_obstacles = 0;
	/** Number of walls in this world */
	public int nbr_walls = 0;
	/** Number of moving entities in this world */
	public int nbr_movers = 0;
	/** The average time it takes for the world to update all the components in the world. */
	public double worldUpdateTime = 0;

	/*
	 * This represents that area of the 'real game world' that is visible in the display.
	 * It does NOT represent the actual display area in pixels, but its aspect ratio should
	 * be the same as the display area aspect ratio.
	 *
	 */
	private dg_EntityDomainRectangular viewOnWorld;
	private double viewScale = 1.0;

	// These are the width and height of the display area and are only required
	// to ensure that the zoom feature is centered on the display area.
	private final double displayWidth, displayHeight;

	/**
	 * Make the default constructor private so it can't be used
	 */
	@SuppressWarnings("unused")
	private dg_World(){
		cspOn = false;
		displayWidth = displayHeight = 0;
		partSize = partOverlap = 0; 
	}

	/**
	 * Create a world without cell space partitioning. <br>
	 * The height and width are the physical display area size in pixels. It maybe smaller than 
	 * the width and height of the applications window. <br>
	 * The portion of the world to be displayed will be a initialized to a rectangle (0,0) to (width, height). <br>
	 * <pre>
	 * (0,0) ___________________________________ X axis
	 *       |                          |
	 *       |                          |
	 *       |                          |
	 *       |                          |
	 *       |                          |
	 *       |                          |
	 *       |__________________________| (width, height)
	 *       |
	 *       Y axis
	 * </pre><br>
	 * This gives a view scale of 1. (i.e. 1 pixel = 1 world unit) <br>
	 * The actual part of the world to be displayed cane be changed with the <pre>
	 * moveTo, panPixel, panWorld & setScale methods. </pre><br><br>
	 * 
	 * @param width the world display width in pixels
	 * @param height the world height width in pixels
	 */
	public dg_World(int width, int height){
		cspOn = false;
		partSize = partOverlap = 0; 
		displayWidth = width;
		displayHeight = height;
		viewOnWorld =  new dg_EntityDomainRectangular(0, 0, displayWidth, displayHeight);
		viewScale = 1.0;

		buildings = new HashSet<dg_EntityBuilding>();
		walls = new HashSet<dg_EntityWall>();
		obstacles = new HashSet<dg_EntityObstacle>();
		movers = new HashSet<dg_EntityMover>();
	}

	/**
	 * Create a world with cell space partitioning. <br>
	 * The height and width are the physical display area size in pixels. It maybe smaller than 
	 * the width and height of the applications window. <br>
	 * The portion of the world to be displayed will be a initialized to a rectangle (0,0) to (width, height). <br>
	 * <pre>
	 * (0,0) ___________________________________ X axis
	 *       |                          |
	 *       |                          |
	 *       |                          |
	 *       |                          |
	 *       |                          |
	 *       |                          |
	 *       |__________________________| (width, height)
	 *       |
	 *       Y axis
	 * </pre><br>
	 * This gives a view scale of 1. (i.e. 1 pixel = 1 world unit) <br>
	 * The actual part of the world to be displayed cane be changed with the <pre>
	 * moveTo, panPixel, panWorld & setScale methods. </pre><br><br>
	 * 
	 * This constructor will cause cell-space partitioning to be used. The parameter values passed for the partition
	 * size and overlap as a guide <br><pre>
	 * partitionSize >= 5 x MCR <br>
	 * partitionOverlap >= 2 x MCR <br>
	 * partitionSize >= 2 x partitionOverlap 
	 * partitionOverlap > whisker length if using steering behaviors with wall avoidance enabled</pre><br>
	 * where MCR is the maximum collision radius for your moving entities. The values used will have a significant 
	 * effect on performance so it is worth experimenting with different values for your game. <br>
	 * 
	 * @param width the world display width in pixels
	 * @param height the world height width in pixels
	 * @param partitionSize the size of the partition in world distance
	 * @param partitionOverlap the overlap between partitions
	 */
	public dg_World(int width, int height, double partitionSize, double partitionOverlap){
		cspOn = true;
		displayWidth = width;
		displayHeight = height;
		viewOnWorld =  new dg_EntityDomainRectangular(0, 0, displayWidth, displayHeight);
		viewScale = 1.0;

		partSize = partitionSize;
		partOverlap = partitionOverlap;

		// Needed for when moving between partitions
		movers = new HashSet<dg_EntityMover>();

		// Needed for steering behaviors
		building_parts = new HashMap<ga_Vector2, HashSet<dg_EntityBuilding>>();
		wall_parts = new HashMap<ga_Vector2, HashSet<dg_EntityWall>>();
		obstacle_parts = new HashMap<ga_Vector2, HashSet<dg_EntityObstacle>>();
		moving_parts = new HashMap<ga_Vector2, HashSet<dg_EntityMover>>();
	}
    public double time() {return timer.getRunTime();} //in seconds
	/**
	 * Calculate the pixel position from a given world position.
	 * 
	 * @param wx world x position
	 * @param wy world y position
	 * @param pxy object to hold the calculate pixel position
	 * @return the calculated pixel position
	 */
	public ga_Vector2 world2pixel(double wx, double wy, ga_Vector2 pxy){
		if(pxy == null)
			pxy = new ga_Vector2();
		int px = (int) m_MathFast.round((wx - viewOnWorld.lowX) * viewScale);
		int py = (int) m_MathFast.round((wy - viewOnWorld.lowY) * viewScale);
		pxy.set(px, py);
		return pxy;
	}

	/**
	 * Calculate the pixel position from a given world position.
	 * 
	 * @param wxy world x,y position
	 * @param pxy object to hold the calculate pixel position
	 * @return the calculated pixel position
	 */
	public ga_Vector2 world2pixel(ga_Vector2D wxy, ga_Vector2 pxy){
		return world2pixel(wxy.x, wxy.y, pxy);
	}

	/**
	 * Calculate the equivalent world position from a pixel position.
	 * 
	 * @param px display screen pixel x position
	 * @param py display screen pixel x position
	 * @param wxy a Vector2D object to hold the world position
	 * @return the calculated world position
	 */
	public ga_Vector2D pixel2world(int px, int py, ga_Vector2D  wxy){
		if(wxy == null)
			wxy = new ga_Vector2D();
		wxy.x = px;
		wxy.y = py;
		wxy.mult(1.0 / viewScale);
		wxy.x += viewOnWorld.lowX;
		wxy.y += viewOnWorld.lowY;
		return wxy;
	}

	/**
	 * Resize the world due to changes in magnification
	 * so that the image is centered in the display area.
	 * 
	 * @param scale the level of magnification.
	 */
	public void setScale(double scale){
		double newX, newY, newWidth, newHeight;
		double scaleFactor = viewScale / scale;

		newWidth = viewOnWorld.width * scaleFactor;
		newHeight = viewOnWorld.height * scaleFactor;
		viewScale = scale;

		newX = viewOnWorld.lowX + 0.5 * viewOnWorld.width * (1-scaleFactor);
		newY = viewOnWorld.lowY + 0.5 * viewOnWorld.height * (1-scaleFactor);
		viewOnWorld.setDomain_xywh(newX, newY, newWidth, newHeight);
	}

	/**
	 * Pan the world display by the given number of pixels.
	 * @param px pixels to pan horizontally.
	 */
	public void panPixelX(int px){
		panWorldX(px / viewScale);
	}

	/**
	 * Pan the world display by the given number of pixels.
	 * @param py pixels to pan vertically.
	 */
	public void panPixelY(int py){
		panWorldY(py / viewScale);
	}

	/**
	 * Pan the world display by the given number of pixels.
	 * @param px pixels to pan horizontally.
	 * @param py pixels to pan vertically.
	 */
	public void panPixelXY(int px, int py){
		panWorldXY(px / viewScale, py / viewScale);
	}


	/**
	 * Pan the world display by the given world distance.
	 * @param wx world distance to pan horizontally.
	 */
	public void panWorldX(double wx){
		viewOnWorld.move_centre_x_by(wx);
	}

	/**
	 * Pan the world display by the given world distance.
	 * @param wy world distance to pan vertically.
	 */
	public void panWorldY(double wy){
		viewOnWorld.move_centre_y_by(wy);
	}

	/**
	 * Pan the world display by the given world distance.
	 * @param wx world distance to pan horizontally.
	 * @param wy world distance to pan vertically.
	 */
	public void panWorldXY(double wx, double wy){
		viewOnWorld.move_centre_xy_by(wx, wy);
	}

	/**
	 * Move the world view so it is centered about the given world position.
	 * @param wx center horizontally about this value.
	 */
	public void moveToWorldX(double wx){
		viewOnWorld.move_centre_x_to(wx);
	}

	/**
	 * Move the world view so it is centred about the given world position.
	 * @param wy centre vertically about this value.
	 */
	public void moveToWorldY(double wy){
		viewOnWorld.move_centre_y_to(wy);
	}

	/**
	 * Move the world view so it is centred about the given world position.
	 * @param wx centre horizontally about this value.
	 * @param wy centre vertically about this value.
	 */
	public void moveToWorldXY(double wx, double wy){
		viewOnWorld.move_centre_xy_to(wx, wy);
	}

	/**
	 * Get the world x position equivalent to the left hand side of the display area. <br>
	 * This is used when rendering the world view.
	 * @return the amount to translate before drawing
	 */
	public double getXoffset(){
		return -viewOnWorld.lowX * viewScale;
	}

	/**
	 * Get the world x position equivalent to the left hand side of the display area. <br>
	 * This is used when rendering the world view.
	 * @return the amount to translate before drawing
	 */
	public double getYoffset(){
		return -viewOnWorld.lowY * viewScale;
	}

	/**
	 * Get the current viewScale (zoom factor)
	 * @return view scale value
	 */
	public double getViewScale(){
		return viewScale;
	}

	/**
	 * Get the extent of the world being drawn. It returns a EntityDomain object
	 * @return EntityDomain object with the extent of the world being drawn.
	 */
	public dg_EntityDomainRectangular getViewOnWorld(){
		return viewOnWorld;
	}

	/**
	 * Get the entity associated with a particular ID number. <br>
	 * If we are not using state machines or if the id number has not been used 
	 * then the entity will not be found.
	 * 
	 * @param entityID unique ID number for the entity
	 * @return null if the entity was not found else the entity that has the given ID number
	 */

	public dg_a_EntityBase getEntity(int entityID){
		return(allEntities == null) ? null : allEntities.get(entityID);
	}

	/**
	 * For a particular moving entity get all the buildings in the same partition.
	 * @param m the moving entity
	 * @return a set of nearby buildings
	 */
	public Set<dg_EntityBuilding> getBuildings(dg_EntityMover m){
		if(cspOn)
			return building_parts.get(getPartition(m.getPos().x, m.getPos().y));
		else
			return buildings;
	}

	/**
	 * For a particular moving entity get all the moving entities in the same partition. It 
	 * will include itself.
	 * @param m the moving entity
	 * @return a set containing all the moving entities in its partition
	 */
	public Set<dg_EntityMover> getMovers(dg_EntityMover m){
		if(cspOn)
			return moving_parts.get(getPartition(m.getPos().x, m.getPos().y));
		else
			return movers;
	}

	/**
	 * Get a list of moving entities in partitions within a given range 
	 * of a moving entity.
	 * @param m the mover we are interested in getting the neighbours
	 * @param range the radius of the circle surrounding neighbours.
	 * @return a set containing the neighbours
	 */
	public Set<dg_EntityMover> getMovers(dg_EntityMover m, double range){
		HashSet<dg_EntityMover> neighbours = null;
		if(cspOn){
			neighbours = new HashSet<dg_EntityMover>();
			double x0 	= m.getPos().x - range;
			double y0 	= m.getPos().y - range;
			double x1 	= m.getPos().x + range;
			double y1 	= m.getPos().y + range;

			int left 	= (int) m_MathFast.floor(x0 / partSize)-1;
			int right 	= (int) m_MathFast.floor(x1 / partSize)+1;
			int top 	= (int) m_MathFast.floor(y0 / partSize)-1;
			int bottom 	= (int) m_MathFast.floor(y1 / partSize)+1;

			ga_Vector2 part = new ga_Vector2();
			for(int partY = top; partY <= bottom; partY++){
				for(int partX = left; partX <= right; partX++){
					part.set(partX, partY);
					if(moving_parts.containsKey(part)){
						HashSet<dg_EntityMover> mpart = moving_parts.get(part);
						neighbours.addAll(mpart);
					}
				}
			}
		}
		else {
			neighbours = (HashSet<dg_EntityMover>) movers;
		}
		return neighbours;
	}

	/**
	 * Get a list of walls that need to be tested for wall avoidance.
	 * @param m the moving entity
	 * @return a set containing the walls (maybe empty but not null)
	 */
	public Set<dg_EntityWall> getWalls(dg_EntityMover m){
		if(cspOn)
			return wall_parts.get(getPartition(m.getPos().x, m.getPos().y));
		else
			return walls;
	}

	/**
	 * Get a list of walls that are in the box made by the moving entity's position 
	 * and the position px,py
	 * @param m the moving entity
	 * @param px
	 * @param py
	 * @return a set containing the walls (maybe empty but not null)
	 */
	public Set<dg_EntityWall> getWalls(dg_EntityMover m, double px, double py){
		if(cspOn){
			Set<dg_EntityWall> wallsBetween = new HashSet<dg_EntityWall>();
			ga_Vector2 part = new ga_Vector2();
			// Get the wall end points
			double x0 = m.getPos().x;
			double y0 = m.getPos().y;
			// Calculate the range of partitions to check
			int left 	= (int) (m_MathFast.floor(m_MathFast.min(x0, px) / partSize) - 1);
			int right 	= (int) (m_MathFast.floor(m_MathFast.max(x0, px) / partSize) + 1);
			int top 	= (int) (m_MathFast.floor(m_MathFast.min(y0, py) / partSize) - 1);
			int bottom 	= (int) (m_MathFast.floor(m_MathFast.max(y0, py) / partSize) + 1);
			// Extended partition size
			for(int partY = top; partY <= bottom; partY++){
				for(int partX = left; partX <= right; partX++){
					part.set(partX, partY);
					if(wall_parts.containsKey(part))
						wallsBetween.addAll(wall_parts.get(part));
				}
			}
			return wallsBetween;
		}
		else
			return walls;
	}

	/**
	 * Get a list of obstacles that need to be tested for wall avoidance.
	 * @param m the moving entity
	 * @return a set containing the obstacles (maybe empty but not null
	 */
	public Set<dg_EntityObstacle> getObstacles(dg_EntityMover m){
		if(cspOn)
			return obstacle_parts.get(getPartition(m.getPos().x, m.getPos().y));
		else
			return obstacles;
	}

	public Set<dg_EntityObstacle> getObstacles(double x, double y){
		if(cspOn)
			return obstacle_parts.get(getPartition(x, y));
		else
			return obstacles;
	}

	/**
	 * Get a list of obstacles that are in the box made by the moving entity's position 
	 * and the position px,py
	 * @param m the moving entity
	 * @param px
	 * @param py
	 * @return a set containing the obstacles (maybe empty but not null)
	 */
	public Set<dg_EntityObstacle> getObstacles(dg_EntityMover m, double px, double py){
		if(cspOn){
			Set<dg_EntityObstacle> obstaclesBetween = new HashSet<dg_EntityObstacle>();
			ga_Vector2 part = new ga_Vector2();
			// Get the wall end points
			double x0 = m.getPos().x;
			double y0 = m.getPos().y;
			// Calculate the range of partitions to check
			int left 	= (int) (m_MathFast.floor(m_MathFast.min(x0, px) / partSize) - 1);
			int right 	= (int) (m_MathFast.floor(m_MathFast.max(x0, px) / partSize) + 1);
			int top 	= (int) (m_MathFast.floor(m_MathFast.min(y0, py) / partSize) - 1);
			int bottom 	= (int) (m_MathFast.floor(m_MathFast.max(y0, py) / partSize) + 1);
			// Extended partition size
			for(int partY = top; partY <= bottom; partY++){
				for(int partX = left; partX <= right; partX++){
					part.set(partX, partY);
					if(obstacle_parts.containsKey(part))
						obstaclesBetween.addAll(obstacle_parts.get(part));
				}
			}
			return obstaclesBetween;
		}
		else
			return obstacles;
	}


	/**
	 * Adds a building to the world.
	 * 
	 * @param b the building to add.
	 */
	public void addBuilding(dg_EntityBuilding b){
		nbr_buildings++;
		if(allEntities != null)
			allEntities.put(b.getID(), b);

		if(cspOn){
			double x0 	= b.getPos().x + b.getExtent().lowX;
			double y0 	= b.getPos().y - b.getExtent().lowY;
			double x1 	= b.getPos().x + b.getExtent().highX;
			double y1 	= b.getPos().y + b.getExtent().highY;

			int left 	= (int) m_MathFast.floor(x0 / partSize)-1;
			int right 	= (int) m_MathFast.floor(x1 / partSize)+1;
			int top 	= (int) m_MathFast.floor(y0 / partSize)-1;
			int bottom 	= (int) m_MathFast.floor(y1 / partSize)+1;
			//			ga_Vector2 part = new ga_Vector2();
			// Extended partition size
			for(int partY = top; partY <= bottom; partY++){
				for(int partX = left; partX <= right; partX++){
					ga_Vector2 part = new ga_Vector2(partX, partY);
					if(!building_parts.containsKey(part))
						building_parts.put(part, new HashSet<dg_EntityBuilding>());
					building_parts.get(part).add(b);
				}
			}
		}
		else {
			buildings.add(b);
		}
		if(b.getStateMachine() != null) stateMachinesOn = true;
		// Now add the walls
		ga_Vector2D offset = b.getPos();
		ga_Vector2D[] contour = b.getContour();
		for(int i = 1; i < contour.length; i++)
			addWall(new dg_EntityWall(ga_Vector2D.add(contour[i-1], offset),
					ga_Vector2D.add(contour[i], offset), false));
		addWall(new dg_EntityWall(ga_Vector2D.add(contour[contour.length-1], offset), 
				ga_Vector2D.add(contour[0], offset), false));
	}

	/**
	 * Adds an obstacle to the world.
	 * @param ob the obstacle to add.
	 */
	public void addObstacle(dg_EntityObstacle ob){
		nbr_obstacles++;
		if(allEntities != null)
			allEntities.put(ob.getID(), ob);
		if(ob.getStateMachine() != null) stateMachinesOn = true;
		//		obstacles.add(ob);
		if(cspOn){
			double x0 	= ob.getPos().x - ob.getColRadius();
			double y0 	= ob.getPos().y - ob.getColRadius();
			double x1 	= ob.getPos().x + ob.getColRadius();
			double y1 	= ob.getPos().y + ob.getColRadius();

			int left 	= (int) m_MathFast.floor(x0 / partSize)-1;
			int right 	= (int) m_MathFast.floor(x1 / partSize)+1;
			int top 	= (int) m_MathFast.floor(y0 / partSize)-1;
			int bottom 	= (int) m_MathFast.floor(y1 / partSize)+1;
			//			ga_Vector2 part = new ga_Vector2();
			// Extended partition size
			double extPartSize = partSize + 2 * partOverlap;
			for(int partY = top; partY <= bottom; partY++){
				for(int partX = left; partX <= right; partX++){
					if(ga_Geometry2D.box_box(x0, y0, x1, y1,
							partX * partSize - partOverlap, partY * partSize - partOverlap, 
							partX * partSize * partOverlap + extPartSize, partY * partSize + partOverlap + extPartSize)){
						ga_Vector2 part = new ga_Vector2(partX, partY);
						if(!obstacle_parts.containsKey(part))
							obstacle_parts.put(part, new HashSet<dg_EntityObstacle>());
						obstacle_parts.get(part).add(ob);
					}
				}
			}
		}
		else {
			obstacles.add(ob);
		}
	}

	public void removeObstacle(dg_a_EntityBase ob){
		boolean removed = false;
		if(allEntities != null)
			allEntities.remove(ob.getID());
		if(cspOn){
			double x0 	= ob.getPos().x - ob.getColRadius();
			double y0 	= ob.getPos().y - ob.getColRadius();
			double x1 	= ob.getPos().x + ob.getColRadius();
			double y1 	= ob.getPos().y + ob.getColRadius();
			// Expand partitions to look at
			int left 	= (int) m_MathFast.floor(x0 / partSize)-1;
			int right 	= (int) m_MathFast.floor(x1 / partSize)+1;
			int top 	= (int) m_MathFast.floor(y0 / partSize)-1;
			int bottom 	= (int) m_MathFast.floor(y1 / partSize)+1;
			ga_Vector2 part = new ga_Vector2();
			for(int partY = top; partY <= bottom; partY++){
				for(int partX = left; partX <= right; partX++){
					part.set(partX, partY);
					if(obstacle_parts.containsKey(part) && obstacle_parts.get(part).remove(ob))
						removed = true;	
				}
			}
		}
		else {
			removed  = obstacles.remove(ob);	
		}
		if(removed) nbr_obstacles--;
	}


	public void removeWall(dg_EntityWall w){
		boolean removed = false;
		if(allEntities != null)
			allEntities.remove(w.getID());
		if(cspOn){
			double x0 = w.getPos().x;
			double x1 = w.end().x;
			double y0 = w.getPos().y;
			double y1 = w.end().y;
			// Expand partitions to look at
			int left 	= (int) m_MathFast.floor(x0 / partSize)-1;
			int right 	= (int) m_MathFast.floor(x1 / partSize)+1;
			int top 	= (int) m_MathFast.floor(y0 / partSize)-1;
			int bottom 	= (int) m_MathFast.floor(y1 / partSize)+1;
			ga_Vector2 part = new ga_Vector2();
			for(int partY = top; partY <= bottom; partY++){
				for(int partX = left; partX <= right; partX++){
					part.set(partX, partY);
					if(wall_parts.containsKey(part) && wall_parts.get(part).remove(w))
						removed = true;	
				}
			}
		}
		else {
			removed  = walls.remove(w);	
		}
		if(removed) nbr_walls--;
	}



	/**
	 * Add a wall to the world. <br>
	 * When looking to see whether the wall passes through any particular partition
	 * then it will consider the extended partition i.e. as if it had been extended
	 * on all sides by 'partSize'. <br>
	 * partSize should be greater than the whisker length used in wall direction.
	 * 
	 * @param w
	 * @param b
	 */
	public void addWall(dg_EntityWall w){
		nbr_walls++;
		if(allEntities != null)
			allEntities.put(w.getID(), w);
		if(w.getStateMachine() != null) stateMachinesOn = true;
		if(cspOn){
			// Get the wall end points
			double x0 = w.getPos().x;
			double x1 = w.end().x;
			double y0 = w.getPos().y;
			double y1 = w.end().y;
			// Calculate the range of partitions to check
			int left 	= (int) (m_MathFast.floor(m_MathFast.min(x0, x1) / partSize) - 1);
			int right 	= (int) (m_MathFast.floor(m_MathFast.max(x0, x1) / partSize) + 1);
			int top 	= (int) (m_MathFast.floor(m_MathFast.min(y0, y1) / partSize) - 1);
			int bottom 	= (int) (m_MathFast.floor(m_MathFast.max(y0, y1) / partSize) + 1);
			// Extended partition size
			double extendedPartSize = partSize + 2 * partOverlap;
			for(int partY = top; partY <= bottom; partY++){
				for(int partX = left; partX <= right; partX++){
					if(ga_Geometry2D.line_box_xywh(x0, y0, x1, y1, 
							partX * partSize - partOverlap, partY * partSize - partOverlap, extendedPartSize, extendedPartSize)){
						// Add the wall
						ga_Vector2 part = new ga_Vector2(partX, partY);
						if(!wall_parts.containsKey(part))
							wall_parts.put(part, new HashSet<dg_EntityWall>());
						wall_parts.get(part).add(w);
					}
				}
			}
		}
		else
			walls.add(w);
	}


	/**
	 * Add a moving object to the world.
	 * @param me the moving object to add
	 */
	public void addMover(dg_EntityMover me){
		nbr_movers++;
		if(allEntities != null)
			allEntities.put(me.getID(), me);
		if(me.getStateMachine() != null) stateMachinesOn = true;
		if(cspOn){
			ga_Vector2 part = getPartition(me.getPos().x, me.getPos().y);
			if(!moving_parts.containsKey(part))
				moving_parts.put(part, new HashSet<dg_EntityMover>());
			moving_parts.get(part).add(me);
		}
		movers.add(me);
	}

	public void removeMover(dg_EntityMover me){
		if(allEntities != null)
			allEntities.remove(me.getID());
		if(cspOn){
			ga_Vector2 part = getPartition(me.getPos().x, me.getPos().y);
			if(moving_parts.containsKey(part))
				moving_parts.get(part).remove(me);
		}
		// Movers always exist even with cell partitioning
		if(movers.remove(me))
			nbr_movers--;			
	}
	
	/**
	 * This is the core method which will loop through entity state machines (if any) then it will update 
	 * the positions and velocities of all moving entities (applying any steering behaviors used). <br>
	 * It also calculates and stores the average time taken to perform this update.
	 *  
	 * @param deltaTime the time in seconds since the last update.
	 */
	public void update(double deltaTime){
		// Initialize the update timer
		timer.reset();
		count++;

		// ===============================================================================
		// ============ UPDATE STATE MACHINES AND PROCESS TELEGRAMS ======================
		if(stateMachinesOn){
			dg_TelegramDispatcher.update();
			for(dg_EntityBuilding building : buildings)
				building.updateStateMachine(deltaTime, this);
			for(dg_EntityObstacle obstacle : obstacles)
				obstacle.updateStateMachine(deltaTime, this);
			for(dg_EntityWall wall : walls)
				wall.updateStateMachine(deltaTime, this);
			for(dg_EntityMover mover : movers)
				mover.updateStateMachine(deltaTime, this);
		}
		for(dg_EntityMover mover : removeStack) removeMover(mover);
		removeStack.clear();
		// ===============================================================================
		// ======== UPDATE ALL MOVING ENTITIES BASED ON THEIR STEERING BEHAVIOURS ========
		for(dg_EntityMover mover : movers)
			mover.update(deltaTime, this);

		// ===============================================================================
		// ========== ENSURE THAT ALL MOVING ENTITIES DO NOT OVERLAP (IF REQD) ===========
		if(noOverlap){
			if(cspOn)
				ensureNoOverlap(deltaTime);
			else
				ensureNoOvelapInPartition(deltaTime, movers);
		}

		// ===============================================================================
		// ========== ENSURE MOVING ENTITIES ARE IN THE CORRECT PARTITION (IF REQD) ========
		if(cspOn)
			checkPartitionMoves();

		// ===============================================================================
		// ======= CALCULATE THE AVERAGE UPDATE TIME =====================================
		// Calculate the average update time
		cumTime += timer.getElapsedTime();
		if(count >= 100){
			this.worldUpdateTime = cumTime / (double)count;
			cumTime = 0;
			count = 0;
		}
	}

	/**
	 * Ensure no overlap between moving entities when using BSP
	 * ignoring entities that touch across partition barriers.
	 */
	private void ensureNoOverlap(double deltaTime){
		// Get the hash sets for each partition
		Collection<HashSet<dg_EntityMover>> c = moving_parts.values();
		// Check each partition
		for(HashSet<dg_EntityMover> part : c)
			ensureNoOvelapInPartition(deltaTime, part);
	}

	/**
	 * Ensure no overlap between moving entities within a partition. If BSP is not
	 * being used then the 'whole world' is a single partition so this method will
	 * be used for a set of all movers. <br>
	 * Entities that touch across partition barriers are ignored.
	 * 
	 */
	private void ensureNoOvelapInPartition(double deltaTime, Set<dg_EntityMover> partSet){
		int n = partSet.size();
		if(n > 1){
			LinkedList<dg_EntityMover> list = new LinkedList<dg_EntityMover>(partSet);
			for(int i = 0; i < n - 1; i++){
				dg_EntityMover mi = list.get(i);
				for(int j = i + 1; j < n; j++)
					ensureNoOverlap(deltaTime, mi, list.get(j)); // Use World class method
			}
		}
	}

	/**
	 * Checks to see if there is overlap between the entities. If there is, it moves both 
	 * entities slightly apart along the collision plane normal.
	 * 
	 * @param deltaTime
	 * @param m0
	 * @param m1
	 */
	private void ensureNoOverlap(double deltaTime, dg_EntityMover m0, dg_EntityMover m1) {
		ga_Vector2D collisionNormal = ga_Vector2D.sub(m0.getPos(), m1.getPos());
		double dist = collisionNormal.length();
		double overlap = m0.getColRadius() + m1.getColRadius() - dist; 
		if(overlap >= 0){
			ga_Vector2D relVel = ga_Vector2D.sub(m0.getVelocity(), m1.getVelocity());
			float nv = (float) collisionNormal.dot(relVel);
			float n2 = (float) collisionNormal.dot(collisionNormal);
			int action=m0.collide(m1);
			if (action==1) {removeStack.add(m1); return;}
			if (action==2) {removeStack.add(m0); return;}
			if(nv < 0 && n2 > 1E-5) {
				collisionNormal.div(dist); // normalize
				collisionNormal.mult(10 * overlap * deltaTime);
				m0.getPos().add(collisionNormal);
				m1.getPos().sub(collisionNormal);
			}
		}
	}

	/**
	 * If we are using space partitioning then check if movers have crossed a partition boundary, and if so, move it
	 * to the correct hash set
	 */
	private void checkPartitionMoves(){
		ga_Vector2 prevPart, currPart;
		ga_Vector2D prevPos, currPos;
		for(dg_EntityMover mover : movers){
			prevPos = mover.getPrevPos();
			currPos = mover.getPos();
			prevPart = new ga_Vector2((int) m_MathFast.floor(prevPos.x / partSize), (int) m_MathFast.floor(prevPos.y / partSize));
			currPart = new ga_Vector2((int) m_MathFast.floor(currPos.x / partSize), (int) m_MathFast.floor(currPos.y / partSize));
//			return new ga_Vector2( (int) FastMath.floor(x / partSize), (int) FastMath.floor(y / partSize) );
			if(prevPart.x != currPart.x || prevPart.y != currPart.y){
				// Remove from old partition
				moving_parts.get(prevPart).remove(mover);
				// Add to new partition
				if(!moving_parts.containsKey(currPart))
					moving_parts.put(currPart, new HashSet<dg_EntityMover>());
				moving_parts.get(currPart).add(mover);
			}
		}
	}

	/**
	 * Any entity that requires rendering will have its own render object. This method ensures that
	 * the draw method of each render object is executed. <br>
	 * It will not draw entities that are completely outside the visible area of the world. 
	 */
	public void draw(){
		// Draw all buildings and obstacles that are in the view area
		if(cspOn){
			drawSet.clear();
			int left 	= (int) m_MathFast.floor(viewOnWorld.lowX / partSize);
			int right 	= (int) m_MathFast.floor(viewOnWorld.highX / partSize);
			int top 	= (int) m_MathFast.floor(viewOnWorld.lowY / partSize);
			int bottom 	= (int) m_MathFast.floor(viewOnWorld.highY / partSize);
			ga_Vector2 p = new ga_Vector2();
			// Collect a list of all buildings, obstacles and walls to be drawn so as to avoid duplication
			for(int partY = top; partY <= bottom; partY++){
				p.y = partY;
				for(int partX = left; partX <= right; partX++){
					p.x = partX;
					if(building_parts.containsKey(p))
						drawSet.addAll(building_parts.get(p));
					// Obstacles
					if(obstacle_parts.containsKey(p))
						drawSet.addAll(obstacle_parts.get(p));
					// Obstacles
					if(wall_parts.containsKey(p))
						drawSet.addAll(wall_parts.get(p));
				}
			}
			// Draw the buildings, obstacles and walls
			for(dg_a_EntityBase entity : drawSet){
				entity.draw(this);
			}
			for(int partY = top; partY <= bottom; partY++){
				p.y = partY;
				for(int partX = left; partX <= right; partX++){
					p.x = partX;
					// Movers
					if(moving_parts.containsKey(p)){
						for(dg_EntityMover mover : moving_parts.get(p))
							mover.draw(this);
					}
				}
			}
		}
		else { // BSP off
			for(dg_EntityBuilding building : buildings)
				if(building.isInDomain(viewOnWorld))
					building.draw(this);
			for(dg_EntityObstacle ob : obstacles)
				if(ob.isInDomain(viewOnWorld))
					ob.draw(this);
			for(dg_EntityWall wall : walls)
				if(wall.isInDomain(viewOnWorld))
					wall.draw(this);
			for(dg_EntityMover mover : movers)
				if(mover.isInDomain(viewOnWorld))
					mover.draw(this);	
		}
	}

	/**
	 * For a given point get the partition key
	 * @param x
	 * @param y
	 * @return
	 */
	private ga_Vector2 getPartition(double x, double y){
		return new ga_Vector2( (int) m_MathFast.floor(x / partSize), (int) m_MathFast.floor(y / partSize) );
	}


	/**
	 * @return the bspOn
	 */
	public boolean isCspOn() {
		return cspOn;
	}

	/**
	 * @return the ensureZeroOverlap
	 */
	public boolean isNoOverlapOn() {
		return noOverlap;
	}

	/**
	 * @param noOverlap the ensureZeroOverlap to set
	 */
	public void setNoOverlap(boolean noOverlap) {
		this.noOverlap = noOverlap;
	}

}
