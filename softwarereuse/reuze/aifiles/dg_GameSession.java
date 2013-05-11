package reuze.aifiles;

import java.util.ArrayList;
import java.util.Stack;
import com.software.reuze.gb_Vector3;
import com.software.reuze.m_MathUtils;

public class dg_GameSession {
	public final static int BONUS_LIFE_SCORE=10000;
	public final static float AI_MAX_SPEED_TRY =80.0f;
	public final static int MAX_SHOT_LEVEL=3;
	public static final int ASTEROID_SCORE_VAL=100;
	public static final float BULLET_SPEED=150.0f;
	public static final float APPROACH_DIST    =180.0f; //for AI FSM
	public static final float PLANET_SIZE=64f;
	public static final float BULLET_MAX_LIFE=1.0f;
	public static final float POWERUP_SCAN_DIST=180.0f;
	//constructor/main functions

	//controls
	public static final int
	CONTROL_THRUST_ON=1,
	CONTROL_THRUST_REVERSE=2,
	CONTROL_THRUST_OFF=3,
	CONTROL_RIGHT_ON=4,
	CONTROL_LEFT_ON=5,
	CONTROL_STOP_TURN=6,
	CONTROL_STOP=7,
	CONTROL_SHOOT=8,
	CONTROL_HYPERSPACE=9,
	CONTROL_PAUSE=10,
	CONTROL_STEP=11,
	CONTROL_AI_ON=12,
	CONTROL_AI_OFF=13,
	CONTROL_COUNT=14,
	CONTROL_RELOAD_SCRIPT=15;
	
	 //collision flags/object types
	public final static int
		OBJ_NONE		= 0x00000000,
		OBJ_ASTEROID	= 0x00000002,
        OBJ_SHIP		= 0x00000004,
		OBJ_BULLET		= 0x00000008,
		OBJ_EFFECT		= 0x00000010,
		OBJ_POWERUP		= 0x00000020,
		OBJ_TARGET      = 0x00000040,
        OBJ_SAUCER		= 0x00000080,
        OBJ_S_BULLET	= 0x00000100,
		OBJ_PLANET  	= 0x00000200
    ;
	//score functions
	public void IncrementScore(int inc)	{IncrementScore(null, inc);}
	public void IncrementScore(dg_GameObject ship, int inc) {
		if(ship==null || ship == m_mainShip) m_score += inc; else m_score2 += inc;
	}
	public void ResetScore()				{m_score = 0;m_score2=0;m_bonusScore = BONUS_LIFE_SCORE;}

	//game related functions

	//data
    dg_Ship         m_mainShip;
	dg_Ship 		m_enemyShip;
	dg_Planet       m_planet;

    dg_ControlHuman m_humanControl;
	dg_ControlAI    m_AIControl;
	dg_ControlAI    m_enemyAIControl;

	int  m_bonusScore;
	float m_respawnTimer;
	float m_powerupTimer;
	float m_enemyTimer;
	States  m_state;
	int  m_score, m_score2;
	int  m_numLives;
	int  m_waveNumber;
	boolean m_AIOn;
	int  m_timeScale;

	public static enum States
	{
		STATE_PLAY,
		STATE_PAUSE,
		STATE_NEXTWAVE,
		STATE_GAMEOVER,
		STATE_STEP,
		STATE_STEPWAIT
	};

	ArrayList<dg_GameObject> m_activeObj;
	static Stack<dg_GameObject> m_pending;
	static Stack<dg_GameObject> m_deleted;
	dg_InfluenceMap m_oInfluenceMap;
	dg_InfluenceMap m_cInfluenceMap;
	public dg_GameSession() {
	}
	public dg_GameSession(String options)
	{
		this();
	}
	//FSM state types
	public static final int
		FSM_STATE_NONE=0,
		FSM_STATE_APPROACH=1,
		FSM_STATE_ATTACK=2,
		FSM_STATE_EVADE=3,
		FSM_STATE_GETPOWERUP=4,
		FSM_STATE_IDLE=5,
		FSM_STATE_HUNT=6,
		FSM_STATE_GBSPOT=7,
		FSM_STATE_MAPPR=8
	;
	public void Init() {
		m_AIOn	       = false;
		m_timeScale    = 1;
		m_activeObj=new ArrayList<dg_GameObject>();
		m_pending=new Stack<dg_GameObject>();
		m_deleted=new Stack<dg_GameObject>();
		m_humanControl = new dg_ControlHuman(null);
		m_AIControl    = new dg_ControlAIFSM();
		m_enemyAIControl    = new dg_ControlAIFSM(null);
		m_oInfluenceMap = new dg_InfluenceMapOccupance();
		m_oInfluenceMap.Init(4,4,z_app.app.width,z_app.app.height);
		m_oInfluenceMap.DrawTheGrid();
		m_oInfluenceMap.DrawTheInfluence();

		m_cInfluenceMap = null;
//		m_cInfluenceMap = new ControlInfluenceMap();
//		m_cInfluenceMap->Init(32,32,INITIAL_WORLD_SIZE,INITIAL_WORLD_SIZE);
//		m_cInfluenceMap->DrawGrid();
//		m_cInfluenceMap->DrawInfluence();
	}
	public void Clip(gb_Vector3 p)
	{
		if(p.x > z_app.app.width) p.x -=z_app.app.width; 
		if(p.y > z_app.app.height) p.y -=z_app.app.height; 
		if(p.x < 0)         p.x +=z_app.app.width; 
		if(p.y < 0)         p.y +=z_app.app.height; 
	}
	public void StartGame()
	{
		m_mainShip		= null;
		m_powerupTimer	= 0.2f;
		m_enemyTimer	= 5.0f;
		m_waveNumber	= 1;
		m_numLives		= 3;
		dg_Asteroid.ZeroNumber();
		m_respawnTimer  = -1;
		m_activeObj.clear();
		if (!m_pending.empty()) throw new RuntimeException("pending != 0 in start");
		m_pending.clear();
		if (!m_deleted.empty()) throw new RuntimeException("deleted != 0 in start");
		m_deleted.clear();
		m_AIControl.Init();
		m_enemyAIControl.Init();
		m_planet = new dg_Planet();
		AddGameObj(m_planet);
		ResetScore();
		StartNextWave();
	}
	public void StartNextWave()
	{
		m_state			= States.STATE_PLAY;
		LaunchAsteroidWave();
		//have to reset the AI controller each new level
		if(m_AIOn)
		{
			m_AIControl.Reset();
			m_enemyAIControl.Reset();
		}
	}
	public void LaunchAsteroidWave()
	{
		dg_Asteroid a;
		for(int i=0;i<2*m_waveNumber;i++)
		{
			a=new dg_Asteroid(8*(1+m_MathUtils.random(10)));
			do
			{
				a.m_position.x= (float) (Math.random()*z_app.app.width);	
				a.m_position.y= (float) (Math.random()*z_app.app.height);
			}
			while( m_mainShip!=null && a.IsIntersecting(m_mainShip));
			a.m_position.z= 0;
			a.m_velocity.x= (float) (Math.random()*70 - 30);	
			a.m_velocity.y= (float) (Math.random()*70 - 30);
			a.m_velocity.z= 0;
			AddGameObj(a);
		}
	}
	public static void AddGameObj(dg_GameObject obj)
	{
		m_pending.add(obj);
		if(z_app.game.UsingIM())
			z_app.game.IMRegisterGameObj(obj);
	}
	public void Draw() {
		z_app.app.pushStyle();
		switch(m_state)
		{
		case STATE_STEPWAIT:
		case STATE_STEP:
		case STATE_PLAY:
			for(dg_GameObject li:m_activeObj) {
				if (li.m_active) li.Draw();
			}
			z_app.app.fill(0,255,0);
			z_app.app.text("Score: "+m_score+"/"+m_score2+" Wave: "+m_waveNumber,5,z_app.app.height-20);
			DrawLives();
			if(m_AIOn)//debug text drawing
			{
				z_app.app.text("AI ON "+m_AIControl.m_debugTxt,z_app.app.width/4,z_app.app.height-20);
			}
			break;
		case STATE_PAUSE:
			z_app.app.text("Game Paused, Press p",5,z_app.app.height-20);
			break;
		case STATE_GAMEOVER:
			z_app.app.text("FINAL SCORE: "+m_score+"/"+m_score2+" Start Again(y/n)",5,z_app.app.height-20);
			break;
		case STATE_NEXTWAVE:
			z_app.app.text("Wave Completed, Press Space Bar",5,z_app.app.height-20);
			if(m_AIOn) StartNextWave();
			break;
		default:
			break;
		}
		z_app.app.popStyle();
	}
	public void DrawLives()
	{
		z_app.app.fill(0,255,0);
		z_app.app.text("Lives: "+m_numLives,z_app.app.width-60,z_app.app.height-20);

	}
	public void Update(float dt)
	{
		m_activeObj.addAll(m_pending);
		m_pending.clear();
		//update the message pump
		//g_MessagePump.Update(dt);

		//update the influence map
		IMUpdate(dt);
		for (dg_GameObject list1:m_activeObj)
		{
			//update logic and positions
			if (!list1.m_active) continue;
			//if you have an AI controller, and 
			//the AI is turned on, update
			if(list1.m_control!=null && m_AIOn)
				list1.m_control.Update(dt);
			list1.Update(dt);
			Clip(list1.m_position);	    		
			//check for collisions
			if (list1.m_collisionFlags != OBJ_NONE)
			{
				for(dg_GameObject list2:m_activeObj)
				{
					//the first obj may have already collided with something, making it inactive
					if (!(list1.m_active&list2.m_active)) continue;

					//don't collide with yourself
					if (list1 == list2) continue;

					if ((list1.m_collisionFlags & list2.m_type)==list2.m_type && 
							list1.IsIntersecting(list2)) 
					{
						list1.DoCollision(list2);
					}
				} //for
			}
		}
		m_activeObj.removeAll(m_deleted);
		m_deleted.clear();
		//check for no main ship, respawn
		if (m_mainShip == null || m_respawnTimer>=0.0f)
		{
			m_respawnTimer-=dt;
			if(m_respawnTimer<0.0f)
			{
				m_mainShip = new dg_Ship();	
				AddGameObj(m_mainShip);
				m_humanControl.SetShip(m_mainShip);
				m_AIControl.SetShip(m_mainShip);
				if (m_AIOn) m_mainShip.m_control = m_AIControl;
			}
		}

		//occasionally spawn a powerup
		m_powerupTimer-=dt;
		if (m_powerupTimer <0.0f)
		{
			m_powerupTimer = (float) (Math.random()*6.0f + 4.0f);
			dg_Powerup pow = new dg_Powerup();
			pow.m_position.x= (float) (Math.random()*z_app.app.width);	
			pow.m_position.y= (float) (Math.random()*z_app.app.height); 
			pow.m_position.z= 0;
			pow.m_velocity.x= (float) (Math.random()*40 - 20);	
			pow.m_velocity.y= (float) (Math.random()*40 - 20);
			pow.m_velocity.z= 0;
			AddGameObj(pow);
		}

		//check for additional life bonus each 10K points
		if(m_score >= m_bonusScore)
		{
			m_numLives++;
			m_bonusScore += BONUS_LIFE_SCORE;
		}
		//occasionally spawn a saucer
		if(m_enemyShip==null)
		{
				m_enemyShip = new dg_Saucer();
				AddGameObj(m_enemyShip);
				m_enemyAIControl.SetShip(m_enemyShip);
				m_enemyShip.m_control = m_enemyAIControl;
		}
		//check for finished wave
		if(dg_Asteroid.GetNumber()<=0 && m_state==States.STATE_PLAY)
		{
			m_waveNumber++;
			WaveOver();
		}

		//check for finished game, and reset
		if (m_numLives<=0)
			GameOver();

		m_humanControl.Update(dt);

		//update AI control, if turned on
		if(m_AIOn) m_AIControl.Update(dt);
		m_enemyAIControl.Update(dt);
	}
	public void UseControl(int control)
	{
		if(m_mainShip==null)// && !Game.m_AIOn)
			return;
		switch(control)
		{
		case CONTROL_THRUST_ON:
			m_mainShip.ThrustOn();
			break;
		case CONTROL_THRUST_REVERSE:
			m_mainShip.ThrustReverse();
			break;
		case CONTROL_THRUST_OFF:
			m_mainShip.ThrustOff();
			break;
		case CONTROL_RIGHT_ON:
			m_mainShip.TurnRight();
			break;
		case CONTROL_LEFT_ON:
			m_mainShip.TurnLeft();
			break;
		case CONTROL_STOP_TURN:
			m_mainShip.StopTurn();
			break;
		case CONTROL_STOP:
			m_mainShip.Stop();
			break;
		case CONTROL_SHOOT:
			m_mainShip.Shoot();
			break;
		case CONTROL_HYPERSPACE:
			m_mainShip.Hyperspace();
			break;
		case CONTROL_PAUSE:
			if(m_state == States.STATE_PLAY)
				m_state = States.STATE_PAUSE;
			else
				m_state = States.STATE_PLAY;
			break;
		case CONTROL_STEP:
			if(m_state == States.STATE_STEPWAIT || m_state == States.STATE_STEP)
				m_state = States.STATE_PLAY;
			else
				m_state = States.STATE_STEP;
			break;
		case CONTROL_AI_ON:
			m_AIOn = true;
			m_mainShip.m_control = m_AIControl;
			break;
		case CONTROL_AI_OFF:
			m_AIOn = false;
			break;
		default:
			break;
		}
	}

	public void GameOver()
	{
		//kill everything
		m_activeObj.clear();
		m_state = States.STATE_GAMEOVER;
	}
	boolean RemoveBulletOrExplosion(dg_GameObject obj)
	{
		if(obj.m_type == OBJ_BULLET || obj.m_type == OBJ_EFFECT)
		{
			Kill(obj);
			return true;
		}
		return false;
	}
	public void WaveOver()
	{
		//kill all the bullets and explosions
		for (dg_GameObject list1:m_activeObj) RemoveBulletOrExplosion(list1);

		//reset the ship stuff
		if (m_mainShip!=null) m_mainShip.Init();
		if (m_enemyShip!=null) m_enemyShip.Init();
		m_state = States.STATE_NEXTWAVE;
	}
	//---------------------------------------------------------
	public void Kill(dg_GameObject ship)
	{
		if(ship==null) return;
		ship.m_active=false;
		if (ship == m_mainShip)
		{
			m_mainShip = null;
			m_humanControl.m_ship = null;
			m_AIControl.m_ship = null;
			m_numLives--;
		} else if(ship == m_enemyShip)
		{
			m_enemyShip = null;
			m_enemyAIControl.m_ship = null;
		}
		if(z_app.game.UsingIM())
			z_app.game.IMRemoveGameObj(ship);
		m_deleted.add(ship);
		//go through the obj list and make sure nobody is still pointing to
		//this ship
		if (ship.m_type==OBJ_SHIP || ship.m_type==OBJ_SAUCER)
			for(dg_GameObject list1:m_activeObj)
			{
				if(list1.m_type == OBJ_BULLET && ((dg_Bullet)list1).m_parent == ship)
					((dg_Bullet)list1).m_parent = null;
			}

	}
	dg_GameObject GetClosestGameObj(dg_GameObject obj, int type) {
		return GetClosestGameObj(obj, type, 100000000.0f);
	}
	//---------------------------------------------------------
	dg_GameObject GetClosestGameObj(dg_GameObject obj, int type, float maxScan)
	{
		//go through the list, find the closest object of param "type"
		//to the param "obj"
		float closeDist = maxScan;
		dg_GameObject closeObj = null;

		for (dg_GameObject list1:m_activeObj)
		{
			//watch out for yourself
			if(list1.m_type != type || !list1.m_active || list1 == obj)
				continue;
			//TODO Distance should be an object method        
	        //our "distance apart" should take into account our size
			float combinedSize = list1.m_size + obj.m_size;
			float dist = list1.m_position.dst2(obj.m_position)-combinedSize*combinedSize;
			if(dist< closeDist)
			{
				closeDist = dist;
				closeObj = list1;
			}
		}
		return closeObj;
	}

	//---------------------------------------------------------
	dg_GameObject GetClosestGameObj(gb_Vector3 point, int type)
	{
		//go through the list, find the closest object of param "type"
		//to the param "point"
		float closeDist = 100000000.0f;
		dg_GameObject closeObj = null;

		for(dg_GameObject list1:m_activeObj)
		{
			if (!list1.m_active) continue;
			float dist = list1.m_position.dst2(point)-list1.m_size*list1.m_size;
			if (list1.m_type == type && dist< closeDist)
			{
				closeDist = dist;
				closeObj = list1;
			}
		}
		return closeObj;

	}
	dg_GameObject GetClosestGameObjMovingAtMe(dg_GameObject obj, int type, float maxDist) {
		//go through the list, find the closest object of param "type"
		//to the param "obj" that is moving towards me
		float closeDist = 100000000.0f;
		dg_GameObject closeObj = null;

		for(dg_GameObject list1:m_activeObj)
		{
			//watch out for yourself
			if(list1 == obj)
				continue;

			//our "distance apart" should take into account our size
			float combindedSize = list1.m_size + obj.m_size;
			float dist = list1.m_position.dst(obj.m_position) - combindedSize;
			gb_Vector3 toObj = obj.m_position.tmp().sub(list1.m_position);
			boolean movingDot = toObj.dot(list1.UnitVectorVelocity()) > 0;
			if((list1.m_type & type)!=0				&& 
			    dist< closeDist							&& 
			   (maxDist != -1.0? dist < maxDist : true) &&
			   movingDot)
			{
				closeDist = dist;
				closeObj = list1;
			}
		}
		return closeObj;
	}
	dg_GameObject GetClosestGameObjICanReach(dg_GameObject obj, int type, float maxDist) {
		//go through the list, find the closest object of param "type"
		//to the param "obj" that is moving towards me
		float closeDist = 100000000.0f;
		dg_GameObject closeObj = null;

		for(dg_GameObject list1:m_activeObj)
		{
			//watch out for yourself
			if(list1 == obj)
				continue;

			//our "distance apart" should take into account our size
			float combindedSize = list1.m_size + obj.m_size;
			float dist = list1.m_position.dst(obj.m_position) - combindedSize;
			gb_Vector3 combinedVelocity = list1.m_velocity.tmp().add(obj.UnitVectorVelocity().tmp2().mul(obj.m_maxSpeed));
			float combinedSpeed = combinedVelocity.len();
			boolean canReach = (dist/combinedSpeed) <= list1.m_lifeTimer;
			if((list1.m_type & type)!=0				&& 
				dist< closeDist							&& 
				(maxDist != -1.0? dist < maxDist : true) &&
				canReach)
			{
				closeDist = dist;
				closeObj = list1;
			}
		}
		return closeObj;
	}
	final static int dst2(int x, int y, int x2, int y2) {
		return (x2-x)*(x2-x)+(y2-y)*(y2-y);
	}
	dg_GameObject GetConcentricOccupanceGameObj(dg_GameObject obj, int thresholdValue, int maxLoops, boolean allowBorderWrap) {
		//go through the list, find the closest influence object
		//to the param "obj"
		int closeDist = 100000000;
		dg_GameObject closeObj = null;

		gb_Vector3 objPos = obj.m_position.tmp();

		//first, find the nearby spot with the most influence objects
		int currentGridX,currentGridY;

		m_oInfluenceMap.ConvertPositionToGrid(objPos);
		currentGridX=(int) objPos.x; currentGridY=(int) objPos.y;
		int myGridX=currentGridX, myGridY=currentGridY;
		//search variables
		int bestGridX=0, bestGridY=0, bestCount = 0;
		int  numLoops	= 0;
		do
		{
			//check spot immediately to my left
			currentGridX--;
			int zoneCount = m_oInfluenceMap.GetInfluenceValue(currentGridX,currentGridY);
			boolean ignore = !allowBorderWrap && m_oInfluenceMap.IsOutOfBounds(currentGridX,currentGridY);
			if(zoneCount > bestCount && !ignore)
			{
				bestGridX = currentGridX;
				bestGridY = currentGridY;
				bestCount = zoneCount;
				if (bestCount > thresholdValue) break;
			}			

			//now start the spiral around
			//go up
			for(int i = 0;i<numLoops*2+1; i++)
			{
				currentGridY++;
				zoneCount = m_oInfluenceMap.GetInfluenceValue(currentGridX,currentGridY);
				if (zoneCount < bestCount) continue;
				ignore = !allowBorderWrap && m_oInfluenceMap.IsOutOfBounds(currentGridX,currentGridY);
				if(!ignore)
				{
					if (zoneCount == bestCount) {
						int d=dst2(myGridX,myGridY,currentGridX,currentGridY);
						if (d>=closeDist) continue;
						closeDist=d;
					}
					bestGridX = currentGridX;
					bestGridY = currentGridY;
					bestCount = zoneCount;
				}
			}
			
			if (bestCount > thresholdValue) break;

			//increment the loop now that we've started it, 
			//simplifies the indexing
			numLoops++;

			//go right
			for(int i = 0;i<numLoops*2; i++)
			{
				currentGridX++;
				zoneCount = m_oInfluenceMap.GetInfluenceValue(currentGridX,currentGridY);
				if (zoneCount < bestCount) continue;
				ignore = !allowBorderWrap && m_oInfluenceMap.IsOutOfBounds(currentGridX,currentGridY);
				if(!ignore)
				{
					if (zoneCount == bestCount) {
						int d=dst2(myGridX,myGridY,currentGridX,currentGridY);
						if (d>=closeDist) continue;
						closeDist=d;
					}
					bestGridX = currentGridX;
					bestGridY = currentGridY;
					bestCount = zoneCount;
				}
			}

			if (bestCount > thresholdValue) break;

			//go down
			for(int i = 0;i<numLoops*2; i++)
			{
				currentGridY--;
				zoneCount = m_oInfluenceMap.GetInfluenceValue(currentGridX,currentGridY);
				if (zoneCount < bestCount) continue;
				ignore = !allowBorderWrap && m_oInfluenceMap.IsOutOfBounds(currentGridX,currentGridY);
				if(!ignore)
				{
					if (zoneCount == bestCount) {
						int d=dst2(myGridX,myGridY,currentGridX,currentGridY);
						if (d>=closeDist) continue;
						closeDist=d;
					}
					bestGridX = currentGridX;
					bestGridY = currentGridY;
					bestCount = zoneCount;
				}
			}

			if (bestCount > thresholdValue) break;

			//go left
			for(int i = 0;i<numLoops*2; i++)
			{
				currentGridX--;
				zoneCount = m_oInfluenceMap.GetInfluenceValue(currentGridX,currentGridY);
				if (zoneCount < bestCount) continue;
				ignore = !allowBorderWrap && m_oInfluenceMap.IsOutOfBounds(currentGridX,currentGridY);
				if(zoneCount > bestCount && !ignore)
				{
					if (zoneCount == bestCount) {
						int d=dst2(myGridX,myGridY,currentGridX,currentGridY);
						if (d>=closeDist) continue;
						closeDist=d;
					}
					bestGridX = currentGridX;
					bestGridY = currentGridY;
					bestCount = zoneCount;
				}
			}

			if (bestCount > thresholdValue) break;

		} while (numLoops < maxLoops);
		if (bestCount==0) return null;
		for(dg_GameObject list1:m_activeObj)
		{
			//watch out for yourself
			if(list1 == obj)
				continue;

			//only consider objects that are "influence" objects
			//that are in the closest busiest zone (as found above)
			//if(!list1->m_influence) continue;
			int v=m_oInfluenceMap.GetInfluenceValue(list1.m_position);
			//System.out.println(list1.m_id+" "+v);
			if (v==bestCount)
				return list1;
		}
		return closeObj;
	}
	dg_GameObject[] GetGameObjVector(dg_GameObject obj, int type, float maxDist) {
		//go through the list, find all the objects of param "type" and of optional
		//maxDist, and add them to a referenced list
		//to the param "obj"
		dg_GameObject[] temp=new dg_GameObject[m_activeObj.size()];
		maxDist*=maxDist;
		int i=0;
		for(dg_GameObject list1:m_activeObj)
		{
			//watch out for yourself
			if((list1.m_type & type)==0 || list1 == obj)
				continue;

			float dist = list1.m_position.dst2(obj.m_position);
			if(dist < maxDist)
				temp[i++]=list1;
		}
		if (i==m_activeObj.size()) return temp;
		if (i==0) return null;
		dg_GameObject[] temp2=new dg_GameObject[i];
		System.arraycopy(temp, 0, temp2, 0, i);
		return temp2;
	}

	//---------------------------------------------------------
	int GetNumGameObj(int type)
	{
		//go through the list, count up all the objects of param "type"
		//to the param "point"
		int count = 0;
		for(dg_GameObject list1:m_activeObj)
		{
			if (list1.m_type == type)
				count++;
		}
		return count;

	}

	//---------------------------------------------------------
	void ApplyForce(int type, gb_Vector3 force, float dt)
	{
		for(dg_GameObject list1:m_activeObj)
		{
			if(list1.m_type != type)
				continue;

			list1.m_velocity.add(force.tmp().mul(dt));
		}
	}

	//---------------------------------------------------------
	void ApplyForce(int type,gb_Vector3 p1, gb_Vector3 p2, gb_Vector3 force, float dt)
	{
		for(dg_GameObject list1:m_activeObj)
		{
			if(list1.m_type != type)
				continue;

			//check if the object is colliding with 
			//the force line segment
			if(list1.IsIntersecting(p1,p2))
				list1.m_velocity.add(force.tmp().mul(dt));
		}
	}
	boolean UsingIM() {return m_oInfluenceMap!=null || m_cInfluenceMap!=null;}
	void IMRemoveGameObj(dg_GameObject obj) {
		if(m_oInfluenceMap!=null)
			m_oInfluenceMap.RemoveGameObj(obj);
		if(m_cInfluenceMap!=null)
			m_cInfluenceMap.RemoveGameObj(obj);
	}
	void IMRegisterGameObj(dg_GameObject obj) {
		if(m_oInfluenceMap!=null)
			m_oInfluenceMap.RegisterGameObj(obj);
		if(m_cInfluenceMap!=null)
			m_cInfluenceMap.RegisterGameObj(obj);
	}
	void IMUpdate(float dt) {
		if(m_oInfluenceMap!=null)
			m_oInfluenceMap.Update(dt);
		if(m_cInfluenceMap!=null)
			m_cInfluenceMap.Update(dt);
	}
	void IMDraw() {
		if(m_oInfluenceMap!=null)
			m_oInfluenceMap.Draw();
		if(m_cInfluenceMap!=null)
			m_cInfluenceMap.Draw();
	}
	dg_InfluenceMap GetOInfluenceMap() {return m_oInfluenceMap;}
	dg_InfluenceMap GetCInfluenceMap() {return m_cInfluenceMap;}
	void FindWrappedPoint(gb_Vector3 p, int x, int y)
	{
		//this returns the point mirrored in a game world in the 
		//direction of the x&y directions. 
		//Useful values of x&y are -1,0,+1
		switch(x)
		{
		case -1:
			p.x -= z_app.app.width;
			break;
		case 0:
			break;
		case 1:
			p.x += z_app.app.width;
			break;
		default:
			throw new RuntimeException("err");
		}

		switch(y)
		{
		case -1:
			p.y -= z_app.app.height;
			break;
		case 0:
			break;
		case 1:
			p.y += z_app.app.height;
			break;
		default:
			throw new RuntimeException("err");
		}
	}
	public int getInfluenceType(dg_GameObject object) {
		if(object.m_type == z_app.game.OBJ_SHIP || object.m_type == z_app.game.OBJ_SAUCER)
	        return dg_InfluenceMapControl.OT_FRIENDLY;
	    if(object.m_type == z_app.game.OBJ_BULLET || object.m_type == z_app.game.OBJ_S_BULLET)
	    	return dg_InfluenceMapControl.OT_BULLET;
	    if(object.m_type == z_app.game.OBJ_ASTEROID)
	    	return dg_InfluenceMapControl.OT_ENEMY;
	    return dg_InfluenceMapControl.OT_MISC;
	}
}
