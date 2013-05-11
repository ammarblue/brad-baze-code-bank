package reuze.aifiles;

import com.software.reuze.gb_Vector3;
import com.software.reuze.m_MathUtils;

public class dg_StateGoToBusy extends dg_FSMState {
	//constructor/functions
    public dg_StateGoToBusy(dg_Control parent) {
    	super(z_app.game.FSM_STATE_GBSPOT,parent);
    }
	public void Update(float dt)
	{
		dg_ControlAIFSM parent = (dg_ControlAIFSM)m_parent;
	    dg_Ship    ship     = (dg_Ship)parent.m_ship;
	  //the number parameters to this get function mean more then 2 asteroids
		//is considered "high occupancy" and I only want the search to spiral out 
		//from my position at most four times (the whole size of the map)
		dg_GameObject asteroid = z_app.game.GetConcentricOccupanceGameObj((dg_GameObject)ship, 2, 4, false);

	    if(asteroid==null || ship==null )
	        return;
	    System.out.println("going to "+asteroid.m_id);
	    //find future position of asteroid
	    gb_Vector3 futurePowPosition = asteroid.m_position.tmp2();
	    gb_Vector3 deltaPos = futurePowPosition.tmp().sub(ship.m_position);
	    float dist  = deltaPos.len();
	    float speed = parent.m_maxSpeed;
	    float time = dist/speed;
	    futurePowPosition.add(asteroid.m_velocity.tmp3().sub(ship.m_velocity).mul(time));
		z_app.game.Clip(futurePowPosition);
		gb_Vector3 deltaFPos = futurePowPosition.sub(ship.m_position);
	    deltaFPos.nor();
	    
	    //add braking vec if you're going too fast
	    speed = ship.m_velocity.len();
	    if(speed > parent.m_maxSpeed)
	        deltaFPos.sub(ship.UnitVectorVelocity());

	    //DOT out my velocity
	    gb_Vector3 shpUnitVel = ship.UnitVectorVelocity();
	    float dotVel = shpUnitVel.dot(deltaFPos);
	    float proj = 1-dotVel;
	    deltaFPos.sub(shpUnitVel.mul(proj));
	    deltaFPos.nor();

	  //find direction, and head to it
		float newDir	 = -m_MathUtils.directionXangle(deltaFPos);
		//ship nose angle down screen 0 to 180 clkwise, else 0 to 180 counter-clkwise
		float angDelta	 = m_MathUtils.clamp180(ship.m_angle - newDir);
		String turn="";
		if ( Math.abs(angDelta) <3 )
		{
			//thrust
			ship.StopTurn(); turn="STOP";
			if (parent.m_nearestAsteroid==null || (parent.m_nearestAsteroidDist > parent.m_nearestAsteroid.m_size + 19)) {
	                if (Math.abs(angDelta)<3) ship.ThrustOn(); else ship.ThrustReverse();
	        } else ship.ThrustOff();
		}
		else if(newDir<0 && ship.m_angle<0) {
			if (newDir<ship.m_angle) {ship.TurnRight(); turn="right";} else {ship.TurnLeft(); turn="left";}
		} else if(newDir>=0 && ship.m_angle>=0) {
				if (newDir<ship.m_angle) {ship.TurnRight(); turn="right";} else {ship.TurnLeft(); turn="left";}
		} else if(newDir<0 && ship.m_angle>=0) {
				if ((360+newDir-ship.m_angle)>180) {ship.TurnRight(); turn="right";} else {ship.TurnLeft(); turn="left";}
		} else if(newDir>=0 && ship.m_angle<0) {
			if ((360-newDir+ship.m_angle)<180) {ship.TurnRight(); turn="right";} else {ship.TurnLeft(); turn="left";}
		}
	    
	    parent.m_target.m_position = ship.m_velocity.cpy().mul(time).add(futurePowPosition);
	    parent.m_targetDir = newDir;
	    parent.m_debugTxt = "Goto Busy Spot "+ship.m_angle+" "+turn;
	}

	//---------------------------------------------------------
	int CheckTransitions()
	{
		dg_ControlAIFSM parent = (dg_ControlAIFSM)m_parent;

	    if(parent.m_willCollide)
	        return z_app.game.FSM_STATE_EVADE;
	    //if(parent.m_huntThresholdReached) return States.FSM_STATE_HUNT;
	    if(parent.m_nearestAsteroidDist < z_app.game.APPROACH_DIST)
	        return z_app.game.FSM_STATE_ATTACK;

	    return z_app.game.FSM_STATE_GBSPOT;    
	}

	//---------------------------------------------------------
	void Exit()
	{
	    if(((dg_ControlAIFSM)m_parent).m_ship!=null)
	    {
	    	((dg_Ship)((dg_ControlAIFSM)m_parent).m_ship).ThrustOff();
	    	((dg_Ship)((dg_ControlAIFSM)m_parent).m_ship).StopTurn();
	    }
	}
	@Override
	void Enter() {
		// TODO Auto-generated method stub
		
	}
	@Override
	void Init() {
		// TODO Auto-generated method stub
		
	}
}
