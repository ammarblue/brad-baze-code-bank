package reuze.aifiles;

import com.software.reuze.gb_Sphere;
import com.software.reuze.gb_Vector3;
import com.software.reuze.m_MathUtils;

public class dg_GameObject {
	public final static int NO_LIFE_TIMER=99999;
		//constructors/functions
	    public dg_GameObject(float _size)
	    {
	    	m_position		= new gb_Vector3(0,0,0);
	        m_velocity		= new gb_Vector3(0,0,0);
	        Init(_size);
	    }

		public dg_GameObject() {this(1);}
	    public dg_GameObject(final gb_Vector3 _p, float _angle, float size) {
	    	m_position		= _p;
	        m_velocity		= new gb_Vector3(0,0,0);
	        m_angle			= _angle;
	        Init(size);
	    }
	    public dg_GameObject(final gb_Vector3 _p, float _angle, final gb_Vector3 _v) {
	    	m_position		= _p;
	        m_velocity		= _v;
	    	m_angle			= _angle;
	        Init(1);
	    }
		public void Draw() {}
		public void Init(float size) {
			m_id = ++id;
			m_acceleration = new gb_Vector3(0,0,0);
		    m_angVelocity	= 0;
		    m_active		= true;
		    m_size			= size;
		    m_boundSphere	= new gb_Sphere(m_position, size);
		    m_collisionFlags= z_app.game.OBJ_NONE;
		    m_type			= z_app.game.OBJ_NONE;
		    m_lifeTimer		= NO_LIFE_TIMER;
		    m_maxSpeed		= z_app.game.AI_MAX_SPEED_TRY/z_app.game.m_timeScale;
		}
		public gb_Vector3 Update(float dt) {
			m_velocity.add(m_acceleration.tmp().mul(dt));
			gb_Vector3.clamp(m_velocity,0.0f,m_maxSpeed);

		    m_position.add(m_velocity.tmp().mul(dt));
		    z_app.game.Clip(m_position);
		    m_angle     += dt*m_angVelocity;
		    m_angle      = m_MathUtils.clamp180(m_angle);

		    m_position.z = 0.0f;
		    
		    if(m_lifeTimer != NO_LIFE_TIMER)
		    {
		        m_lifeTimer -= dt;
		        if (m_lifeTimer<0.0f) { 
		            z_app.Kill(this);
		        }
		    }
		    return m_position;
		}
		public void Action(dg_GameObject obj, String action) { }
		public boolean IsIntersecting(dg_GameObject obj) {
			m_boundSphere.center		 = m_position;
			obj.m_boundSphere.center = obj.m_position;
			boolean b=m_boundSphere.intersects(obj.m_boundSphere);
			return b;
		}
		public void DoCollision(dg_GameObject obj) {
			z_app.Kill(obj);
		}
		public void Explode(String injury) { }
		public gb_Vector3 UnitVectorFacing() {//unit vector in facing direction clockwise 0-180 -Y 180.0001-360 +Y
			return new gb_Vector3(Math.cos(-Math.PI*m_angle/180.0),Math.sin(-Math.PI*m_angle/180.0),0);
		}
		public gb_Vector3 UnitVectorVelocity() {//unit vector in velocity direction
			return m_velocity.cpy().nor();
		}
		
		//data
		gb_Vector3	m_position;	  	  
		float		m_angle;  
		gb_Vector3  m_velocity;  
		gb_Vector3  m_acceleration;
	    float		m_angVelocity;
	    boolean		m_active;  
		float		m_size;
		gb_Sphere	m_boundSphere;
		int			m_type;   //32 possible object types
		int 		m_collisionFlags; //collision can be symmetric or asymmetric (flagSet.collide(type in flagSet)
		float		m_lifeTimer;
		int         m_id;
		dg_Control	m_control;
		float       m_maxSpeed;
		static int id;
		boolean		 IsOfType(int type) {return ((m_type & type) != 0);}
		public boolean IsIntersecting(gb_Vector3 p1, gb_Vector3 p2) {
			m_boundSphere.center		 = m_position;
			return m_boundSphere.intersects(p1,p2);
		}
		@Override
		public String toString() {
			return m_active+" "+m_type+" "+m_id+" "+m_position.x+" "+m_position.y+" "+m_position.z;
		}
}
