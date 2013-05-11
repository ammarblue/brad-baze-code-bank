package reuze.aifiles;

import com.software.reuze.gb_Vector3;

public class dg_Planet extends dg_GameObject {
	public dg_Planet() {
		super(z_app.game.PLANET_SIZE);
		Init();
	}
	//---------------------------------------------------------
	public void Init()
	{
		m_type = z_app.game.OBJ_PLANET;
		m_collisionFlags = z_app.game.OBJ_ASTEROID | z_app.game.OBJ_BULLET | 
			z_app.game.OBJ_SHIP     | z_app.game.OBJ_POWERUP;
		m_position.set(z_app.app.width/2,z_app.app.height/2,0.0f);

	}

	//---------------------------------------------------------
	public void Draw()
	{
		//just a circle
		z_app.app.pushStyle();
		z_app.app.fill(0,255,0);
		z_app.app.ellipseMode(z_app.app.CENTER);
		z_app.app.ellipse(m_position.x,m_position.y, m_size*2, m_size*2);
		z_app.app.pushStyle();
	}

	//---------------------------------------------------------
	public void DoCollision(dg_GameObject obj)
	{
		//if you're a bullet, just kill the bullet
		if((obj.m_type & z_app.game.OBJ_BULLET) != 0)
		{
			dg_Bullet bullet = (dg_Bullet)obj;
			if (bullet.m_parent!=null)
				bullet.m_parent.Action(this,"kill_bullet");
			obj.m_active = false;
		}
		else//if you're a ship or an asteroid, just bounce off
		{
			gb_Vector3 delta = obj.m_position.tmp().sub(m_position);

			gb_Vector3 impulse   = delta.tmp2();
			impulse.nor();
			obj.m_velocity.sub(impulse.mul(impulse.dot(obj.m_velocity)*2));
			
			//find overlap distance (ensure that it's not zero, since we
			//divide by this value below
			float distanceBetweenUs = Math.max(delta.len(),1);
			float combinedSize = m_size + obj.m_size;
			//separate the objects to disallow overlap
			if(distanceBetweenUs < combinedSize)
			{
				delta.div(distanceBetweenUs);
				obj.m_position.add(delta.mul(combinedSize - distanceBetweenUs));
			}
		}
	}
}
