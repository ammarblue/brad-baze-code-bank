package reuze.aifiles;

public class dg_StateIdle extends dg_FSMState {
	public dg_StateIdle(dg_Control parent) {
		super(z_app.game.FSM_STATE_IDLE, parent);
	}
	public void Update(float dt)
	{
	    //Do nothing
	    dg_ControlAIFSM parent = (dg_ControlAIFSM)m_parent;
	    parent.m_debugTxt = "Idle";
	    ((dg_Ship)parent.m_ship).StopTurn();
	}

	//---------------------------------------------------------
	public int CheckTransitions()
	{
		dg_ControlAIFSM parent = (dg_ControlAIFSM)m_parent;

	    if(parent.m_willCollide)
	        return z_app.game.FSM_STATE_EVADE;

	    if(parent.m_nearestAsteroid!=null)
	    {
	        if(parent.m_nearestAsteroidDist > z_app.game.APPROACH_DIST)
	            return z_app.game.FSM_STATE_APPROACH; //States.FSM_STATE_MAPPR;
	        else
	            return z_app.game.FSM_STATE_ATTACK;
	    }
	    //if(parent.m_huntThresholdReached) return FSM_STATE_MAPPR;
	    if(parent.m_powerupNear)
	        return z_app.game.FSM_STATE_GETPOWERUP;

	    return z_app.game.FSM_STATE_IDLE;
	}
	@Override
	void Enter() {
		// TODO Auto-generated method stub		
	}
	@Override
	void Exit() {
		// TODO Auto-generated method stub		
	}
	@Override
	void Init() {
		// TODO Auto-generated method stub		
	}
}
