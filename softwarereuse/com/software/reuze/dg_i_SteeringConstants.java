package com.software.reuze;
public interface dg_i_SteeringConstants {

	// Bit positions for flags
    public static final int BIT_WALL_AVOID			= 0;
    public static final int BIT_OBSTACLE_AVOID		= 1;
    public static final int BIT_EVADE				= 2;
    public static final int BIT_FLEE				= 3;
    public static final int BIT_SEPARATION			= 4; // These three
    public static final int BIT_ALIGNMENT			= 5; // together for
    public static final int BIT_COHESION			= 6; // flocking
    public static final int BIT_SEEK				= 7;
    public static final int BIT_ARRIVE				= 8;
    public static final int BIT_WANDER				= 9;
    public static final int BIT_PURSUIT				= 10;
    public static final int BIT_OFFSET_PURSUIT		= 11;
    public static final int BIT_INTERPOSE			= 12;
    public static final int BIT_HIDE				= 13;
    public static final int BIT_PATH				= 14;
    public static final int BIT_FLOCK				= 15;
      
    // The number of different behavior types. This MUST be accurate
    public static final int NBR_OF_BEHAVIOUR_TYPES	= 16;
    
    public static final int WALL_AVOID			= 1 << BIT_WALL_AVOID;
    public static final int OBSTACLE_AVOID		= 1 << BIT_OBSTACLE_AVOID;
    public static final int EVADE				= 1 << BIT_EVADE;
    public static final int FLEE				= 1 << BIT_FLEE;
    public static final int SEPARATION			= 1 << BIT_SEPARATION;	// These three
    public static final int ALIGNMENT			= 1 << BIT_ALIGNMENT;	// together for
    public static final int COHESION			= 1 << BIT_COHESION;	// flocking
    public static final int SEEK				= 1 << BIT_SEEK;
    public static final int ARRIVE				= 1 << BIT_ARRIVE;
    public static final int WANDER				= 1 << BIT_WANDER;
    public static final int PURSUIT				= 1 << BIT_PURSUIT;
    public static final int OFFSET_PURSUIT		= 1 << BIT_OFFSET_PURSUIT;
    public static final int INTERPOSE			= 1 << BIT_INTERPOSE;
    public static final int HIDE				= 1 << BIT_HIDE;
    public static final int PATH				= 1 << BIT_PATH;
    public static final int FLOCK 				= 1 << BIT_FLOCK;
    // All behaviors
    public static final int ALL_SB_MASK			= (1 << NBR_OF_BEHAVIOUR_TYPES) - 1;
    
    public static final int WEIGHTED_AVERAGE   = 0;
    public static final int PRIORITIZED        = 1;
 
    public static final double[] DECEL_TWEEK	= new double[] {0.0, 0.3, 0.6, 0.9}; 
    public static final int FAST        		= 1;
    public static final int NORMAL           	= 2;
    public static final int SLOW   				= 3;
    public static final double ARRIVE_DIST		= 0.1;
    
    public static final int AGENT0				= 0;
    public static final int AGENT1				= 1;
    public static final int AGENT_TO_PURSUE		= 2;
    public static final int AGENT_TO_EVADE		= 3;
    public static final int NBR_AGENT_ARRAY		= 4;
       
    public static boolean CLOSED_PATH 			= false;
    
    public static final String[] CALC_METHOD = new String[] { "Weighted Avg", "Prioritised" };
    
    public static final String[] ARRIVE_SPEED = new String[] { "Fast", "Normal", "Slow" };
    
	public static final int PASS_THROUGH		= 10000;
	public static final int WRAP 				= 10001;
	public static final int REBOUND 			= 10002;
	

}
