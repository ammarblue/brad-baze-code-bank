package reuze.aifiles;

import com.software.reuze.gb_Vector3;

public class dg_Asteroid extends dg_GameObject {
	public static int m_numAsteroids=0;
	public dg_Asteroid(float size) {
		super(size);
		m_type = z_app.game.OBJ_ASTEROID;
		m_collisionFlags = z_app.game.OBJ_ASTEROID|z_app.game.OBJ_SHIP /*TODO | OBJ_POWERUP*/;
		m_numAsteroids++;
	}

	@Override
	public void Draw() {
		z_app.app.pushStyle();
		z_app.app.fill(255,255,0);
		z_app.app.ellipse(m_position.x, m_position.y, m_size*2, m_size*2);
		z_app.app.fill(0);
		z_app.app.text(m_id,m_position.x-m_size+4,m_position.y+4);
		z_app.app.popStyle();
		//TODO not complete
	}
	/*Useful for testing
	@Override
	public gb_Vector3 Update(float dt) {
		m_position.z=0;
		m_velocity.set(0,0,0);
		m_acceleration.set(0,0,0);
		return null;}*/
	@Override
	public void DoCollision(dg_GameObject obj)
	{    //System.out.println(""+m_id+"/"+m_type+" hits "+obj.m_id+"/"+obj.m_type);
		//if I'm colliding with another asteroid, just
		//do "pool ball" collision
		//TODO explosions are doubled/cascaded when one obj overtakes and goes thru another
		//also ship overtaking asteroid decrements lives to zero immediately
		if(obj.m_type == z_app.game.OBJ_ASTEROID)
		{
			gb_Vector3 delta = m_position.tmp().sub(obj.m_position);
			//ensure at least 1 distbetween, since we divide by
			//that number below
			float distanceBetweenUs = Math.max(delta.len(),1);
			float combinedSize = m_size + obj.m_size;
			//separate the objects to disallow overlap
			//and swap velocities
			//TODO Explode();
			if(distanceBetweenUs < combinedSize)
			{
				m_position.add((delta.tmp2().div(distanceBetweenUs)).mul(combinedSize - distanceBetweenUs));
				gb_Vector3 temp = m_velocity;
				m_velocity = obj.m_velocity;
				obj.m_velocity = temp;
			}
			return;
		}

		//get rid of target
		z_app.game.AddGameObj(new dg_Effect(obj));
		super.DoCollision(obj);
		z_app.Kill(this);
		m_numAsteroids--;

		//split into two
		if(m_size>16)
		{
			dg_Asteroid ast;
			gb_Vector3 nv=new gb_Vector3(
			Math.random()*70 - 30,	
			Math.random()*70 - 30,
			0);

			ast	  =new dg_Asteroid(m_size/2);
			ast.m_angVelocity=(float) (Math.random()*70);
			ast.m_position	=m_position.cpy();
			ast.m_velocity	=m_velocity.cpy().add(nv);
			z_app.game.AddGameObj(ast);;
			ast	  =new dg_Asteroid(m_size/2);
			ast.m_angVelocity=(float) (Math.random()*70);
			ast.m_position	= m_position.cpy().add(m_size*2);
			ast.m_velocity	= m_velocity.cpy().sub(nv);
			z_app.game.AddGameObj(ast);
		}
	}

	public static void ZeroNumber() {
		m_numAsteroids=0;
	}

	public static int GetNumber() {
		return m_numAsteroids;
	}

}
