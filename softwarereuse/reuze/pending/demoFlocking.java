package reuze.pending;

import com.software.reuze.dg_EntityDomainRectangular;
import com.software.reuze.dg_EntityVehicle;
import com.software.reuze.dg_Steering;
import com.software.reuze.dg_World;
import com.software.reuze.ga_Vector2D;
import com.software.reuze.pt_StopWatch;
import com.software.reuze.z_Processing;
import com.software.reuze.vgu_PictureArrow;

import processing.core.PApplet;


public class demoFlocking extends PApplet {
	final int NBR_BOIDS = 500;

	dg_World w;
	dg_EntityDomainRectangular wd;
	pt_StopWatch sw = new pt_StopWatch();

	dg_EntityVehicle boid;
	dg_EntityVehicle[] boids;
	dg_Steering sb;
	dg_Steering[] sbarray;
	vgu_PictureArrow view;
	float deltaTime;
	int backcol, boidcol;
	long count = 0;

	public void setup() {
	  size(800, 600);
	  z_Processing app=new z_Processing(this);
	  w = new dg_World(width, height, 67, 0);
	  wd = new dg_EntityDomainRectangular(0, 0, width - 200, height);
	  w.setNoOverlap(true);

	  view = new vgu_PictureArrow(app, color(220, 220, 255), color(220, 220, 255), 1);	


	  sb = new dg_Steering();
	  sb.enableBehaviours(sb.WANDER | sb.FLOCK);
	  sb.setWanderDetails(6, 30, 100);
	  sb.setFlockRadius(50);
	  sb.setWeight(sb.WANDER, 1);
	  sb.setWeight(sb.ALIGNMENT, 8);
	  sb.setWeight(sb.COHESION, 2.2);
	  sb.setWeight(sb.SEPARATION, 1.3);
	  sb.setWeight(sb.FLOCK, 16.0);

	  boids = new dg_EntityVehicle[NBR_BOIDS];
	  sbarray = new dg_Steering[NBR_BOIDS];
	  for (int i = 0; i < NBR_BOIDS; i++) {
		float x = width/2 + rnd(-400, 400);
		float y = height/2 + rnd(-300, 300);
	    float dirX = (rnd(0, 1) < 0.5) ? -1 : 1;
	    float dirY = (rnd(0, 1) < 0.5) ? -1 : 1;
	    float vx = dirX * rnd(5, 10);
	    float vy = dirY * rnd(5, 10);
	    boid = new dg_EntityVehicle(new ga_Vector2D(), // position
	    5, // collision radius
	    new ga_Vector2D(), // velocity
	    60, // maximum speed
	    new ga_Vector2D(), // heading
	    1, // mass
	    1, // turning rate
	    400 // max force
	    ); 
	    boid.setRenderer(view);
	    boids[i] = boid;
	    boids[i].setPos(x, y);
	    boids[i].setHeading(vx, vy);
	    boids[i].setVelocity(vx, vy);
	    sbarray[i] = (dg_Steering) sb.clone();
	    boid.setSB(sbarray[i]);
	    boid.setWorldDomain(wd);
	    w.addMover(boid);
	  }
	  frameRate(100);
	  sw.reset();
	}


	public void draw() {
	  deltaTime = (float) sw.getElapsedTime();
	  background(backcol);
	  w.update(deltaTime);
	  w.draw();
	}
	
	public void keyTyped() {
		if (key=='r') resetBoids();
	}
	
	void resetBoids() {
		  for (int i = 0; i < NBR_BOIDS; i++) {
		    w.removeMover(boids[i]);
		    float x = (width - 200)/2 + random(-150, 150);
		    float y = height/2 + random(-150, 150);
		    float dirX = (rnd(0, 1) < 0.5) ? -1 : 1;
		    float dirY = (rnd(0, 1) < 0.5) ? -1 : 1;
		    float vx = dirX * rnd(5, 10);
		    float vy = dirY * rnd(5, 10);
		    boids[i].setPos(x, y);
		    boids[i].setHeading(vx, vy);
		    boids[i].setVelocity(vx, vy);
		    w.addMover(boids[i]);
		  }
		}

	float rnd(float low, float high) {
		  return (float)Math.random()*(high - low) + low;
	}
}
