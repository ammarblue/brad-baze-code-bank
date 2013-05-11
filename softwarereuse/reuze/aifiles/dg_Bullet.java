package reuze.aifiles;

import com.software.reuze.gb_Vector3;

public class dg_Bullet extends dg_GameObject {
		//constructors/functions
	public dg_Bullet(dg_GameObject _parent, gb_Vector3 _p, float _angle) {
		super(_p.cpy(),_angle, 2.0f);
		m_parent = _parent;
		gb_Vector3 vel;
		vel=UnitVectorFacing();
		vel.mul(z_app.game.BULLET_SPEED).add(_parent!=null?_parent.m_velocity:gb_Vector3.ZERO);
		m_velocity = vel;
		Init();
	}
	public void Init()
	{
		m_type = z_app.game.OBJ_BULLET;
		m_collisionFlags = z_app.game.OBJ_ASTEROID;
		m_lifeTimer = z_app.game.BULLET_MAX_LIFE;
		this.m_maxSpeed=9999;
	}
	@Override
	public void Draw() {
		z_app.app.pushStyle();
      	float x=m_position.x, y=m_position.y;
		z_app.app.fill(0,0,255);
		z_app.app.ellipse(m_position.x, m_position.y,4,4);
		z_app.app.popStyle();
		//TODO not complete
	}
	@Override
	public gb_Vector3 Update(float dt)
	{
		gb_Vector3 temp=super.Update(dt);
		if(m_lifeTimer<0.0f) 
		{
			if (m_parent!=null)
				m_parent.Action(this, "kill_Bullet");
		}
		return temp;
	}
	@Override
	public void DoCollision(dg_GameObject obj) {
		//take both me and the other object out
	 	if (obj!=null && obj.m_active)
		{
			z_app.game.AddGameObj(new dg_Effect(obj));
			obj.DoCollision(this);
		}

		if (m_parent!=null)
		{ System.out.println(m_parent);
			z_app.IncrementScore(m_parent, z_app.game.ASTEROID_SCORE_VAL);
			m_parent.Action(this, "kill_Bullet");
		}
		super.DoCollision(obj);
	}

		//data
		dg_GameObject	m_parent;
}
