package reuze.aifiles;

import java.util.ArrayList;

public class dg_FSMMachine /*extends dg_FSMState*/ {
	//FSM machine types
	public static final int
		FSM_MACH_NONE=0;
	dg_Control   m_parent;
	//constructor/functions
	public dg_FSMMachine() {
		this(FSM_MACH_NONE,null);
	}
	public dg_FSMMachine(int mType,int sType, dg_Control parent) {
		//super(sType,parent);
		m_parent=parent;
		m_type = mType;
		m_currentState = null;
		m_defaultState = null;
		m_goalState    = null;
		m_states       = new ArrayList<dg_FSMState>();
	}
    public void SetDefaultState(dg_FSMState state) {m_defaultState = state;}
    public void SetGoalID(int goal) {m_goalID = goal;}
    
    //data
    int m_type;
    ArrayList<dg_FSMState> m_states;
    dg_FSMState m_currentState;
    dg_FSMState m_defaultState;
    dg_FSMState m_goalState;
    int		  m_goalID;
    public dg_FSMMachine(int type, dg_Control parent) {
    	//super(FSM_STATE_NONE, parent);
    		m_parent=parent;
    	    m_type = type;
    		m_currentState = null;
    		m_defaultState = null;
    		m_goalState    = null;
    		m_states       = new ArrayList<dg_FSMState>();
    	}

    	//---------------------------------------------------------
    	public void Update(float dt)
    	{
    		//don't do anything if you have no states
    		if(m_states.size() == 0 )
    			return;

    		//don't do anything if there's no current 
    		//state, and no default state
    		if(m_currentState==null)
    			m_currentState = m_defaultState;
    		if(m_currentState==null)
    			return;

    		//check for transitions, and then update
    		int oldStateID = m_currentState.m_type;
    		m_goalID = m_currentState.CheckTransitions();
    		
    		//switch if there was a transition
    		if(m_goalID != oldStateID)
    		{
    			if(TransitionState(m_goalID))
    			{
    				m_currentState.Exit();
    				m_currentState = m_goalState;
    				m_currentState.Enter();
    			}
    		}
    		m_currentState.Update(dt);	
    		
    	}

    	//---------------------------------------------------------
    	void AddState(dg_FSMState state)
    	{
    		m_states.add(state);
    	}

    	//---------------------------------------------------------
    	boolean TransitionState(int goal)
    	{
    		//don't do anything if you have no states
    		if(m_states.size() == 0 )
    			return false;
    		
    		//determine if we have state of type 'goal'
    		//in the list, and switch to it, otherwise, quit out
    		for(int i =0;i<m_states.size();i++)
    		{
    			if(m_states.get(i).m_type == goal)
    			{
    				m_goalState = m_states.get(i);
    				return true;
    			}
    		}
    		return false;
    	}

    	//---------------------------------------------------------
    	public void Reset()
    	{
    		//Exit();
    		if(m_currentState!=null)
    			m_currentState.Exit();
    		m_currentState = m_defaultState;

    		//init all the states
    		for(int i =0;i<m_states.size();i++)
    			m_states.get(i).Init();

    	    //and now enter the m_defaultState, if any
    	    if(m_currentState!=null)
    	        m_currentState.Enter();
    	    
    	}
		/*@Override
		void Enter() {
			if (m_currentState!=null)
				m_currentState.Enter();			
		}
		@Override
		void Exit() {
			if (m_currentState!=null)
				m_currentState.Exit();		
		}
		@Override
		void Init() {
			// TODO Auto-generated method stub			
		}*/
}
