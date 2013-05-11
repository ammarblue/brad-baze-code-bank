package reuze.aifiles;

import com.software.reuze.gb_Vector3;
import com.software.reuze.m_MathUtils;

public class dg_Ship extends dg_GameObject {
	public final static int MAX_SHIP_SPEED   =120;
	public final static int MAX_AG_SHIP_SPEED=120;
	public final static int MAX_TRACTOR_DIST =180;
	public final static int MAX_TRACTOR_POWER=300;
		//constructor/functions
		public dg_Ship() {this(6);}
		public dg_Ship(float size) {
			super(size);
			m_shotPowerLevel   = 0;
			m_type             = z_app.game.OBJ_SHIP;
			m_collisionFlags   = z_app.game.OBJ_POWERUP;
			m_harmfulBulletType= z_app.game.OBJ_S_BULLET;
			Init();
		}
		@Override
		public void Draw() {
			z_app.app.pushStyle();
			z_app.app.pushMatrix();
			z_app.app.stroke(z_app.app.color(0,255,0));
			z_app.app.translate(m_position.x, m_position.y);
			z_app.app.rotate((float) (2*Math.PI-Math.toRadians(m_angle)));
			z_app.app.translate(-m_size, -m_size);
			z_app.app.line(0, 0, m_size*2, m_size);
			z_app.app.line(m_size*2, m_size, 0, m_size*2);
			z_app.app.line(m_size * 0.2f, m_size * 0.2f, m_size*0.2f, m_size*1.8f);
			z_app.app.ellipseMode(z_app.app.CENTER);
			z_app.app.ellipse(m_size*2, m_size, 4,4);
			if (m_thrust) {
				z_app.app.stroke(m_MathUtils.random(127,255),m_MathUtils.random(0,127),0);
				z_app.app.strokeWeight(m_size*3/2);
				z_app.app.line(-m_size * 0.2f, m_size,-m_size * 0.2f-5, m_size);
			}
			z_app.app.popMatrix();
			z_app.app.popStyle();
			//TODO not complete
		}
		public void Init() {
			m_invincibilityTimer= 3.0f;
			m_thrust           = false;
			m_revThrust        = false;
			m_position		   = new gb_Vector3(z_app.app.width/2,z_app.app.height/2,0);
			m_velocity		   = new gb_Vector3(0,0,0);
			m_angle			   = 0;
			m_angVelocity	   = 0;
			m_activeBulletCount= 0;
		    m_agThrust         = false;
		    m_tractor          = false;
		    m_agNorm           = new gb_Vector3(0,0,0);
		    m_tractorNorm      = new gb_Vector3(0,0,0);
		    m_shotPowerLevel   = 0;
		}
		@Override
		public gb_Vector3 Update(float dt) {
		    if (m_thrust || m_revThrust)
		    {
		    	m_acceleration=UnitVectorFacing().mul(m_thrust?MAX_SHIP_SPEED:-MAX_SHIP_SPEED);
		    } else m_acceleration=gb_Vector3.ZERO;

		    if(m_agThrust) {
		        AGMove(dt);
		        //zero out this vector, that's part
				//of what makes this an "anti-grav" drive...
		        //m_agNorm.set(0,0,0);
		    }
		    if(m_tractor)
		        ApplyTractorBeam(dt);
		    
		    if(m_invincibilityTimer > 0)
		        m_invincibilityTimer -= dt;
		    return super.Update(dt);
		}
		@Override
		public boolean IsIntersecting(dg_GameObject obj) {
			//no self damage, only react to other ship's bullets
			//if((obj->m_type & GameObj::OBJ_BULLET) && obj->m_type != m_harmfulBulletType) return false;
			//only collide if you're not invincible
		    if(m_invincibilityTimer > 0) return false;
		    return super.IsIntersecting(obj);
		}
		@Override
		public void DoCollision(dg_GameObject obj) {
			if (obj.m_type==z_app.game.OBJ_POWERUP) {
				GetPowerup(((dg_Powerup)obj).m_powerType);
				super.DoCollision(obj);
				return;
			}
			//just take myself out
			z_app.RespawnTimer();
		    z_app.Kill(this);
			super.DoCollision(obj);
		}
		
		//controls
		public void ThrustOn()     {m_thrust=true;m_revThrust=false;}
		public void ThrustReverse(){m_revThrust=true;m_thrust=false;}
		public void ThrustOff()    {m_thrust=false;m_revThrust=false;}
		public void TurnLeft() {
			m_angVelocity =  Math.max(120.0f,320.0f/z_app.GetTimeScale());
		}	
		public void TurnRight() {
			m_angVelocity = Math.min(-120.0f,-320.0f/z_app.GetTimeScale());
		}	
		public void StopTurn()		{m_angVelocity =    0.0f;}
		public void Stop() {
			m_velocity.set(0,0,0);
			m_angVelocity =    0.0f;
		}
	    public void Hyperspace() {
	    	m_position.x = (float) (Math.random()*z_app.app.width);
	        m_position.y = (float) (Math.random()*z_app.app.height);
	    }

	    public void TractorBeamOn(gb_Vector3 offset) {
	    	m_tractor = true;
	        m_tractorNorm = offset.cpy().nor();
	    }
	    public void StopTractorBeam(){m_tractor = false;}
	    public void ApplyTractorBeam(float dt) {
	    	//is anybody intersecting the tractor line?
	        //have to pass back to Game object, which is keeper of 
	        //all the objects in the game world
	    	gb_Vector3 temp = m_tractorNorm.tmp().mul(MAX_TRACTOR_POWER);
	    	gb_Vector3 endOfTractor = m_position.cpy().add(temp);
	        z_app.ApplyForce(z_app.game.OBJ_POWERUP,m_position,endOfTractor,temp, dt);
	    }
	    public void AGThrustOn(gb_Vector3 offset) {
	    	m_agThrust = true;
	        m_agNorm = offset.cpy().nor();
	        m_agNorm.z	= 0.0f;
	    }
	    public void AGThrustAccumulate(gb_Vector3 offset) {
	    	m_agThrust = true;
	        m_agNorm.add(offset);
	    }
	    public void StopAGThrust()   {m_agThrust = false;}
	    public void AGMove(float dt) {
	    	//anti-gravity move, no acceleration or velocity
	        //directly affects position
	        m_position.add(m_agNorm.tmp().nor().mul(dt*MAX_AG_SHIP_SPEED));
	    }
	    public gb_Vector3 GetAGvector() {return m_agNorm;}
	    public boolean IsThrustOn() {return m_thrust;}
	    public boolean IsTurningRight() {
	    	return m_angVelocity < 0.0f;
	    }
	    public boolean IsTurningLeft() {
	    	return m_angVelocity > 0.0f;
	    }

		//Powerup management
		public void GetPowerup(int powerupType) {
			switch(powerupType) {
		    case dg_Powerup.POWERUP_SHOT:
		        if(m_shotPowerLevel < z_app.game.MAX_SHOT_LEVEL) 
		            m_shotPowerLevel++;
		        break;
		    default:
		        break;
		    }
		}
		public int GetShotLevel() {return m_shotPowerLevel;}
		public int GetNumBullets() {return m_activeBulletCount;}
		public void IncNumBullets(int num) {m_activeBulletCount+=num;}
		public void IncNumBullets() {m_activeBulletCount++;}
		public void MakeInvincible(float time) {m_invincibilityTimer = time;}

		//bullet management
		public int MaxBullet() {
			int num = 0;
			switch(m_shotPowerLevel)
			{
				case 3:
					num = 25;
					break;
				case 2:
					num = 20;
					break;
				case 1:
					num = 15;
					break;
				case 0:
				default:
					num = 10;
					break;
			}
			return num;
		}
		@Override
		public void Action(dg_GameObject obj, String action) {
			if (m_activeBulletCount > 0) m_activeBulletCount--;
		}
		public void Shoot(float angle) {
			if(angle == -1)
		        angle = m_angle;
		    
			if(m_activeBulletCount > MaxBullet())
		  		return;
			
			dg_Bullet bb;
			switch(m_shotPowerLevel)
			{
				case 3:
					m_activeBulletCount+=4;
					bb =new dg_Bullet(this,m_position,angle-180.0f);
					z_app.game.AddGameObj(bb);
					bb =new dg_Bullet(this,m_position,angle);
					z_app.game.AddGameObj(bb);
					bb =new dg_Bullet(this,m_position,angle-90.0f);
					z_app.game.AddGameObj(bb);
					bb =new dg_Bullet(this,m_position,angle+90.0f);
					z_app.game.AddGameObj(bb);
					break;
				case 2:
					m_activeBulletCount+=3;
					bb =new dg_Bullet(this,m_position,angle-180.0f);
					z_app.game.AddGameObj(bb);
					bb =new dg_Bullet(this,m_position,angle);
					z_app.game.AddGameObj(bb);
					bb =new dg_Bullet(this,m_position,angle-90.0f);
					z_app.game.AddGameObj(bb);
					break;
				case 1:
					m_activeBulletCount+=2;
					bb =new dg_Bullet(this,m_position,angle-180.0f);
					z_app.game.AddGameObj(bb);
					bb =new dg_Bullet(this,m_position,angle);
					z_app.game.AddGameObj(bb);
					break;
				default:
					m_activeBulletCount++;
					bb =new dg_Bullet(this,m_position,angle);
					z_app.game.AddGameObj(bb);
					break;
			} //switch
		}
		public void Shoot() {Shoot(-1);}
		public float GetClosestGunAngle(float angle) {
			if(Math.abs(angle)< 45 || m_shotPowerLevel == 0)
		        return m_angle;
		    if(Math.abs(angle)> 135 && m_shotPowerLevel > 0)
		        return m_angle+180;
		    if(angle < 0 && m_shotPowerLevel >1)
		        return m_angle-90;
		    if(angle > 0 && m_shotPowerLevel >2)
		        return m_angle+90;		    
		    return m_angle;
		}
	    public float GetClosestGunApproachAngle(float angle) {
	    	if(Math.abs(angle) > 90 && m_shotPowerLevel != 0)
	            return m_angle+180;
	        return m_angle;
	    }
		/*returns angle 0-180 counter-clockwise left turn, 0 to -180 clockwise right turn*/
		public float Approach(dg_GameObject asteroid, float maxSpeed, float standOff) {
			if (asteroid==null) return 999;
			gb_Vector3 deltaPos = asteroid.m_position.tmp().sub(m_position);
			//use braking vector if you're going too fast
			boolean needToBrake = false;
			float speed		 = m_velocity.len();
			if(speed > maxSpeed) {
				needToBrake = true;
				deltaPos	= m_velocity.tmp().inv();
				//sub off our current velocity, to get direction of wanted velocity
				deltaPos.sub(m_velocity);
			} else if (standOff!=0) {
				//if the other guy is "to my front" and we're moving towards each other...
				if ((deltaPos.dot(UnitVectorVelocity()) < 0) || (UnitVectorVelocity().dot(asteroid.UnitVectorVelocity()) > -0.93)) { //magic number == about 21 degrees
					gb_Vector3 shipVel = m_velocity.tmp2();
					shipVel.nor().mul(maxSpeed);
					float combinedSpeed	  = shipVel.add(asteroid.m_velocity).len();
					if (Math.abs(combinedSpeed)>.01) {
						float predictionTime  = deltaPos.len() / combinedSpeed;
						gb_Vector3 targetPos = asteroid.m_position.tmp().add(asteroid.m_velocity.tmp2().mul(predictionTime));
						z_app.game.Clip(targetPos);
						deltaPos  = targetPos.sub(m_position);
					}
				}
				//sub off our current velocity, to get direction of wanted velocity
				deltaPos.sub(m_velocity);
			}

			//find new direction, and head to it
			float newDir	 = -m_MathUtils.directionXangle(deltaPos);
			float d=asteroid.m_position.dst(m_position); 
			if (standOff==0 && d<m_size*3+asteroid.m_size) {
				ThrustOn();
				StopTurn();
				return m_angle;
			}
			//ship nose angle down screen 0 to -180 clkwise, else 0 to 180 counter-clkwise
			float angDelta	 = m_MathUtils.clamp180(m_angle - newDir);
			boolean canApproachInReverse = needToBrake || GetShotLevel()!=0;
			if (Math.abs(angDelta) < 3 || (Math.abs(angDelta)> 177 && canApproachInReverse && standOff!=0) ) {
				StopTurn();
				//thrust
				if(d > asteroid.m_size+m_size+standOff) {
					if (Math.abs(angDelta)<3) ThrustOn(); else ThrustReverse();
				} else ThrustOff();
			} else if(newDir<0 && m_angle<0) {
				if (newDir<m_angle) {TurnRight(); } else {TurnLeft();}
			} else if(newDir>=0 && m_angle>=0) {
				if (newDir<m_angle) {TurnRight();} else {TurnLeft();}
			} else if(newDir<0 && m_angle>=0) {
				if ((360+newDir-m_angle)>180) {TurnRight();} else {TurnLeft();}
			} else if(newDir>=0 && m_angle<0) {
				if ((360-newDir+m_angle)<180) {TurnRight();} else {TurnLeft();}
			}
			return newDir;
		}
		public void Collide(dg_GameObject obj) {
			gb_Vector3 t=obj.m_position.tmp().sub(m_position);
			gb_Vector3 u=new gb_Vector3(100,0,0);   //angle to the horizontal
			float z=t.dot(u)/u.len()/t.len();
			float j=(float) Math.toDegrees(Math.acos(z));
			if (t.y<=0 && Math.abs(m_angle-j)>3) {
				if (Math.abs(m_angle-j)<10) ThrustOn(); else ThrustOff();
				if (m_angle>j) TurnRight(); else TurnLeft();
			} else if (t.y>0 && Math.abs(m_angle+j)>3) {
				if (Math.abs(m_angle+j)<10) ThrustOn(); else ThrustOff();
				if ((m_angle>=0 && m_angle>j) || (m_angle<0 && Math.abs(m_angle)>j)) TurnLeft(); else TurnRight();
			} else StopTurn();
			//System.out.println(z+" "+j+" "+o.m_angle);
		}
		public void Evade(dg_GameObject obj) {
			gb_Vector3 t=obj.m_position.tmp().sub(m_position);
			gb_Vector3 u=new gb_Vector3(100,0,0);   //angle to the horizontal
			float z=t.dot(u)/u.len()/t.len();
			float j=(float) Math.toDegrees(Math.acos(z));
			if (t.y<=0) {
				if (Math.abs(m_angle-j)>=45) ThrustOn(); else ThrustOff();
				if (m_angle<=j) TurnRight(); else TurnLeft();
			} else {
				if (Math.abs(m_angle+j)>=45) ThrustOn(); else ThrustOff();
				if ((m_angle>=0 && m_angle<=j) || (m_angle<0 && Math.abs(m_angle)<=j)) TurnLeft(); else TurnRight();
			}
			//System.out.println(z+" "+j+" "+o.m_angle);
		}
		//data
		dg_Control m_control;
		int		m_activeBulletCount;
		boolean	m_thrust;
	    boolean	m_revThrust;
	    boolean	m_agThrust;
	    boolean	m_tractor;
		int		m_shotPowerLevel;
		float   m_invincibilityTimer;
	    gb_Vector3 m_agNorm;
	    gb_Vector3 m_tractorNorm;
	    int		m_harmfulBulletType;
}
