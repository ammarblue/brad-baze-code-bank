package reuze.aifiles;

import com.software.reuze.gb_Vector3;
import com.software.reuze.m_MathUtils;

public class dg_Effect extends dg_GameObject {
	public static final float EFFECT_MAX_LIFE=1.0f;
		//data
		int	m_parentType;
		float	m_parentAngle;
		public dg_Effect() {
			this(null);
		}
		public dg_Effect(dg_GameObject parent) {
			super();
			if(parent!=null) {
				gb_Vector3 temp=parent.m_velocity.tmp().nor().mul(parent.m_size);;
				m_position = parent.m_position.cpy().add(temp);
				m_parentType  = parent.m_type;
				m_parentAngle = parent.m_angle;
			}
			Init();
		}
		public void Init()
		{
			m_lifeTimer		= EFFECT_MAX_LIFE;
			m_type			= z_app.game.OBJ_EFFECT;
		}
		@Override
		public void Draw() {
			z_app.app.pushStyle();
			if(m_parentType == z_app.game.OBJ_ASTEROID)
			{
				float size=m_lifeTimer*20.0f*m_MathUtils.random(.8f, 1f);
				z_app.app.fill(m_MathUtils.random(127, 255),0,0);
				z_app.app.ellipse(m_position.x, m_position.y, size,size);
			}
			z_app.app.popStyle();
			//TODO not complete
		}
}
