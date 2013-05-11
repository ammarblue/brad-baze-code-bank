package reuze.aifiles;

import java.util.ArrayList;

import com.software.reuze.gb_Vector3;
import com.software.reuze.m_MathUtils;

import processing.core.PApplet;

public class aitest extends PApplet {
	dg_Ship o;
	dg_ControlAIFSM c;
	dg_FSMState s;
	ArrayList<dg_Asteroid> a;
	dg_GameSession g;
	public void setup() {
		size(512,512);
		z_app.app=this;
		g=new dg_GameSession();
		z_app.game=g;
		g.Init();
		o=new dg_Ship();
		o.m_position=new gb_Vector3(200,200,0);
		c=new dg_ControlAIFSM(o);
		c.Init();
		s=new dg_StateGetPowerup(c);
		a=new ArrayList<dg_Asteroid>();
		for (int i=0; i<30; i++) {
			a.add(new dg_Asteroid(random(8,20)));
			a.get(i).m_position.set(width/2,height/2,0);
			a.get(i).m_velocity=new gb_Vector3(-22,22,0);
		}
		c.m_nearestPowerup=a.get(0);
		go=new dg_Asteroid(1);
		//s=new dg_StateApproach(c);
		//s=new dg_StateAttack(c);
		//s=new dg_StateEvade(c);
		c.m_nearestAsteroid=a.get(0);
		g.m_oInfluenceMap=new dg_InfluenceMapOccupance();
		g.m_oInfluenceMap.DrawGrid(true);
		g.m_oInfluenceMap.DrawInfluence(true);
		g.m_oInfluenceMap.RegisterGameObj(o);
		for (dg_GameObject z:a) g.m_oInfluenceMap.RegisterGameObj(z);
		g.m_oInfluenceMap.DrawTheGrid();
		g.m_oInfluenceMap.DrawTheInfluence();
		//dg_SteerAlignment sa=new dg_SteerAlignment(cs,o.OBJ_ASTEROID);
		for (dg_GameObject z:a) g.m_activeObj.add(z);
		/*gb_Vector3 v=new gb_Vector3();
		sa.Update(1, v);*/
		//o=(dg_Ship) z_app.game.GetConcentricOccupanceGameObj((dg_GameObject)o, 2, 4, false);
		//frameRate(10);
	}
	int x=0;
	public void draw() {
		if (++x%200==0) {
			for (dg_GameObject z:a) z.m_velocity.add(m_MathUtils.random(-80,80),m_MathUtils.random(-80,80),0);
		}
		background(color(100,200,100));
		o.Update(0.005f);
		for (dg_GameObject z:a) z.Update(0.005f);
		//line(o.m_position.x,o.m_position.y,a.m_position.x,a.m_position.y);
		g.m_oInfluenceMap.Update(0.005f);
		g.m_oInfluenceMap.Draw();
		o.Draw();
		for (dg_GameObject z:a) z.Draw();
		//dg_GameObject foo=z_app.game.GetConcentricOccupanceGameObj((dg_GameObject)o, 3, 4, false);
		//if (foo!=null) ellipse(foo.m_position.x,foo.m_position.y,20,20);
		/*if (Align(o, o.OBJ_ASTEROID, 150, go)) {
			ellipse(go.m_position.x,go.m_position.y,20,20);
			o.Approach(go, o.m_maxSpeed, 19);
		}*/
		//o.Approach(a.get(0), o.m_maxSpeed, 19);
	}
	dg_Asteroid go;
	public boolean Align(dg_GameObject ship, int objectTypes, float distance, dg_GameObject out/*out*/) {
    	dg_GameObject[] nearbyObjects=
    	z_app.game.GetGameObjVector(ship,objectTypes,distance);
    	if (nearbyObjects==null || nearbyObjects.length==0 || out==null) return false;
    	out.m_velocity.set(0,0,0);
    	out.m_position.set(0,0,0);

    	int numObjects = nearbyObjects.length;
    	int shipWasIncluded = 0;

    	for(int i = 0; i< numObjects; i ++)
    	{
    		//don't align with yourself
    		if(nearbyObjects[i] == ship)
    		{
    			//need this because we'll be averaging alignment below, and need to use
    			//correct number of objects to divide by
    			shipWasIncluded = 1;
    			continue;
    		}

    		out.m_velocity.add(nearbyObjects[i].m_velocity);
    		out.m_position.add(nearbyObjects[i].m_position);
    	}

    	//if there were any objects to align with...
    	numObjects -= shipWasIncluded;
    	if(numObjects>1)
    	{
    		out.m_velocity.div(numObjects);
    		out.m_position.div(numObjects);
    	}
    	//if(numObjects>0)System.out.println(numObjects+" "+out.m_position+" "+out.m_velocity);
    	return numObjects!=0;
	}
	public void mousePressed() {
		o.AGThrustOn(new gb_Vector3(mouseX,mouseY,0).sub(o.m_position));
	}
}
