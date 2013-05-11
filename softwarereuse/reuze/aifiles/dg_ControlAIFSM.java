package reuze.aifiles;

import com.software.reuze.gb_Vector3;
import com.software.reuze.m_InterpolateLerp;

public class dg_ControlAIFSM extends dg_ControlAI {
		//constructor/functions
		public dg_ControlAIFSM() {this(null);}
		
		//perception data
		dg_GameObject	m_nearestEnemy;
		dg_GameObject	m_nearestAsteroid;
		dg_GameObject	m_nearestPowerup;
		float       m_nearestAsteroidDist;
		float       m_nearestPowerupDist;
		boolean        m_willCollide;
		boolean        m_powerupNear;
		float       m_safetyRadius;
		//boolean        m_huntThresholdReached;
		//scripted data
		float       m_maxSpeed;
		float		m_safeRadius;
		/*float       m_shipAggressionCloseDistance;
		float       m_shipAggressionProximity;
		int         m_shipAggression;
		int         m_shipAggressionCount;
		int         m_shipAggressionCountSetting;
		boolean        m_powerupSeek;
		float		m_powerupScanDist;
		boolean		m_enemySeek;
		float		m_approachDist;
		float		m_appDist;
		float		m_attDist;*/
		//data
		dg_FSMMachine m_machine;
		static final int FSM_MACH_MAINSHIP=1;
		public dg_ControlAIFSM(dg_Ship ship) {
			super(ship);
			    //construct the state machine and add the necessary states
			    m_machine = new dg_FSMMachine(FSM_MACH_MAINSHIP,this);
			    //dg_FSMMachineApproach mApproach = new dg_FSMMachineApproach(this);
			    dg_StateApproach mApproach = new dg_StateApproach(this);
			    m_machine.AddState(mApproach);
			    m_machine.AddState(new dg_StateAttack(this));
			    m_machine.AddState(new dg_StateEvade(this));
			    m_machine.AddState(new dg_StateGetPowerup(this));
			    //m_machine.AddState(new dg_StateGoToBusy(this));
			    dg_StateIdle idle = new dg_StateIdle(this);
			    m_machine.AddState(idle);
			    //m_machine.SetDefaultState(idle);
			    m_machine.SetDefaultState(mApproach);
			    m_machine.Reset();
			}

			//---------------------------------------------------------
			public void Init()
			{
			    m_willCollide  = false;
			    m_powerupNear  = false;
			    m_nearestAsteroid = null;
			    m_nearestPowerup  = null;
			    m_safetyRadius    = 15.0f;
			    m_maxSpeed        = z_app.game.AI_MAX_SPEED_TRY;///Game.m_timeScale;
			    //m_huntThresholdReached = false;
			    //script settable parameters
				//these are the default values
			    m_maxSpeed        = z_app.game.AI_MAX_SPEED_TRY;
				/*m_shipAggressionCloseDistance	= 150.0f;
				m_shipAggressionProximity		= 100.0f;
				m_shipAggression				= 40;
				m_shipAggressionCount			= 0;
				m_shipAggressionCountSetting	= 4;
				m_powerupSeek					= true;
				m_powerupScanDist				= 300.0f;
				m_enemySeek						= true;
				m_appDist						= 180.0f;
				m_attDist						= 180.0f;*/
			    if(m_target==null)
			    {
			        m_target = new dg_Target(1);
			        z_app.game.AddGameObj(m_target);
			    }
			}

			//---------------------------------------------------------
			public void Update(float dt)
			{
			    if(m_ship==null)
			    {
			        m_machine.Reset();
			        return;
			    }
			    
			    UpdatePerceptions(dt);
			    m_machine.Update(dt);
			}

			//---------------------------------------------------------
			public void UpdatePerceptions(float dt)
			{
			    if(m_willCollide)
			        m_safetyRadius = 2*m_safeRadius;
			    else
			        m_safetyRadius = m_safeRadius;
			    
			    //store closest asteroid and powerup
			    m_nearestAsteroid = z_app.game.GetClosestGameObj(m_ship,z_app.game.OBJ_ASTEROID);
			    m_nearestPowerup  = z_app.game.GetClosestGameObj(m_ship,z_app.game.OBJ_POWERUP);
			    
			  //max scan distance on the nearest enemy goes to the size of the whole map when I've decided to hunt
				float enemyScanDist = /*m_huntThresholdReached?*/ Math.max(z_app.app.width/3,z_app.app.height/3) /*: 300.0f*/;
				m_nearestEnemy    = z_app.game.GetClosestGameObj(m_ship,z_app.game.OBJ_SAUCER,enemyScanDist);
				
			    //asteroid collision determination
			    m_willCollide = false;
			    m_nearestAsteroidDist=9999;
			    if(m_nearestAsteroid!=null)
			    {
			        float speed = m_ship.m_velocity.len();
			        m_nearestAsteroidDist = m_nearestAsteroid.m_position.dst(m_ship.m_position);
			        gb_Vector3 normDelta = m_nearestAsteroid.m_position.tmp().sub(m_ship.m_position);
			        normDelta.nor();
			        float astSpeed = m_nearestAsteroid.m_velocity.len();
			        float shpSpeedAdj = m_ship.UnitVectorVelocity().dot(normDelta)*speed;
			        normDelta.inv();
			        float astSpeedAdj = m_nearestAsteroid.UnitVectorVelocity().dot(normDelta)*astSpeed;
			        speed = shpSpeedAdj+astSpeedAdj;

//			        if(speed > astSpeed)
//			            dotVel  = DOT(m_ship.UnitVectorVelocity(),normDelta);
//			        else 
//			        {
//			            speed = astSpeed;
//			            dotVel = DOT(m_nearestAsteroid.UnitVectorVelocity(),-normDelta);
//			        }
			        float spdAdj = m_InterpolateLerp.lerp(speed/m_maxSpeed,0.0f,90.0f);
			        float adjSafetyRadius = m_safetyRadius+spdAdj+m_nearestAsteroid.m_size;
			        
			        //if you're too close, and I'm heading somewhat towards you,
			        //flag a collision
			        if(m_nearestAsteroidDist <= adjSafetyRadius && speed > 0) {
			        	//System.out.println(m_willCollide+" "+m_nearestAsteroid.m_id);
			            m_willCollide = true;
			        }
			    }
			  //enemy ship calculations
			    /*m_huntThresholdReached=false;
				if(m_nearestEnemy!=null && m_enemySeek)
				{
					float distToEnemy = m_ship.m_position.dst(m_nearestEnemy.m_position);
					if(m_nearestAsteroid==null)//if there's no asteroids at all
						m_huntThresholdReached = true;
					else if(distToEnemy < m_shipAggressionCloseDistance)//if I'm really close anyways
						m_huntThresholdReached = true;
					else if(m_nearestAsteroidDist - distToEnemy > m_shipAggressionProximity)//if the saucer is much closer then any asteroid
						m_huntThresholdReached = true;
					else 
					{
						//if I just feel like attacking, roll dice between 0-99
						//ship has to roll positive "m_shipAggressionCountSetting"
						//number of times to trigger
						if(Math.random()*100 < m_shipAggression)
						{
							m_shipAggressionCount++;
							if(m_shipAggressionCount > m_shipAggressionCountSetting)
								m_huntThresholdReached = true;
						}
						else
							m_shipAggressionCount = 0;
					}
				}*/
				
			    //powerup near determination
			    m_powerupNear = false;
			    m_nearestPowerupDist=9999;
			    if(m_nearestPowerup!=null)
			    {
			        m_nearestPowerupDist = m_nearestPowerup.m_position.dst(m_ship.m_position);
			        if(m_nearestPowerupDist <= z_app.game.POWERUP_SCAN_DIST && 
			        		m_nearestAsteroidDist > m_nearestPowerupDist && ((dg_Ship)m_ship).GetShotLevel() < z_app.game.MAX_SHOT_LEVEL)
			        {
			            m_powerupNear     = true;
			        }
			    }
			    
			}

			@Override
			public void Draw() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void Reset() {
				m_machine.Reset();
				Init();
			}

}
