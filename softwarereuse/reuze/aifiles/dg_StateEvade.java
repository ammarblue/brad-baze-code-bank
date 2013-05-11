package reuze.aifiles;

import com.software.reuze.gb_Vector3;
import com.software.reuze.m_MathUtils;

public class dg_StateEvade extends dg_FSMState {
	//constructor/functions
    public dg_StateEvade(dg_Control parent) {
    	super(z_app.game.FSM_STATE_EVADE,parent);
    }
    @Override
	public void Update(float dt)
	{   //TODO evade should be added to object, can evade simply be approach to a position?
	    //evade by going to the quad opposite as the asteroid
	    //is moving, add in a deflection, and cancel out your movement
	    dg_ControlAIFSM parent = (dg_ControlAIFSM)m_parent;
	    dg_GameObject asteroid = parent.m_nearestAsteroid;
	    if (asteroid==null) return;
	    dg_Ship    ship     = (dg_Ship)parent.m_ship;
	    gb_Vector3 vecSteer = ship.m_position.tmp().crs(asteroid.m_position);
	    gb_Vector3 vecBrake = ship.m_position.tmp2().sub(asteroid.m_position);
	    vecSteer.add(vecBrake);
	    
	    float newDir = -m_MathUtils.directionXangle(vecSteer);
	    float angDelta = m_MathUtils.clamp180(ship.m_angle - newDir);
	    float dangerAdjAngle = ((1.0f - parent.m_nearestAsteroidDist/ z_app.game.APPROACH_DIST)*10.0f) + 1.0f;
	    if(Math.abs(angDelta) <dangerAdjAngle || Math.abs(angDelta)> 180-dangerAdjAngle)//thrust
	    {
	        ship.StopTurn();
	        if(ship.m_velocity.len() < parent.m_maxSpeed || parent.m_nearestAsteroidDist< 19+asteroid.m_size) {
	            if (Math.abs(angDelta)<dangerAdjAngle) ship.ThrustOn(); else ship.ThrustReverse();
	        } else ship.ThrustOff();

	        //if I'm pointed right at the asteroid, shoot
	        ship.Shoot();
	    }
	    else if(Math.abs(angDelta)<=90)//turn front
	    {
	        if(angDelta >0)
	            ship.TurnRight();
	        else
	            ship.TurnLeft();
	    }
	    else//turn rear
	    {
	        if(angDelta<0)
	            ship.TurnRight();
	        else
	            ship.TurnLeft();
	    }
	    
	    
	    parent.m_target.m_position = parent.m_nearestAsteroid.m_position;
	    parent.m_targetDir = newDir;
	    parent.m_debugTxt = "Evade "+newDir;
	}

	//---------------------------------------------------------
	public int CheckTransitions()
	{
		dg_ControlAIFSM parent = (dg_ControlAIFSM)m_parent;

	    if(!parent.m_willCollide)
	        return z_app.game.FSM_STATE_APPROACH;

	    return z_app.game.FSM_STATE_EVADE;
	}

	//---------------------------------------------------------
	public void Exit()
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
