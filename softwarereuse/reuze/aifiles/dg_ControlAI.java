package reuze.aifiles;

public abstract class dg_ControlAI extends dg_Control {
	static final int CONTROLTYPE_AI=1;
	public dg_ControlAI() {this(null);}
	public dg_ControlAI(dg_Ship ship) {
		super(ship);
			if (m_ship!=null )
				m_ship.m_control = this;
			m_type = CONTROLTYPE_AI;

			m_debugTxt = "";
		    m_target   = null;
		}
		@Override
		public abstract void Update(float dt);
		public abstract void UpdatePerceptions(float dt);
		public abstract void Draw() ;
		public abstract void Reset();

	//debug data
	dg_GameObject m_target;
	String	 m_debugTxt;
	float	 m_targetDir;
}
