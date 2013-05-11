package reuze.aifiles;

import com.software.reuze.gb_Vector3;

public class dg_Powerup extends dg_GameObject {
	public static final int POWERUP_SIZE=8;
	public static final float POW_MAX_LIFE=6.5f*3;
		//constructors/functions
		public dg_Powerup() {
			super(POWERUP_SIZE);
			m_powerType=POWERUP_SHOT;
			Init();
		}
		public void Draw() {
			z_app.app.pushStyle();
			z_app.app.fill(0,255,0);
			z_app.app.ellipse(m_position.x, m_position.y, m_size*2, m_size);
			z_app.app.popStyle();
			//TODO not complete
		}
		public void Init() {
			m_type = z_app.game.OBJ_POWERUP;
			m_lifeTimer			= POW_MAX_LIFE*z_app.GetTimeScale();
		}
	    public void SetType(int type) {m_powerType = type;}

	    public static final int POWERUP_NONE=1,
			POWERUP_SHOT=2;
		//data
		int m_powerType;
}
