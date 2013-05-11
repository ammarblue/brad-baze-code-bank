package reuze.aifiles;

public abstract class dg_FSMState {
	//FSM state types
	public static final int
		FSM_STATE_NONE=0;
	//constructor/functions
	public dg_FSMState() {
		this(FSM_STATE_NONE, null);
	}
	public dg_FSMState(	int type, dg_Control parent) {
		m_type = type;m_parent = parent;
	}
    abstract void Enter();
    abstract void Exit();
    abstract void Update(float dt);
    abstract void Init();
    int  CheckTransitions() {return FSM_STATE_NONE;}

	//data
	dg_Control   m_parent;
	int        m_type;
}
