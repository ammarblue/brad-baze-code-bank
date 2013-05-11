package reuze.demo;
import processing.core.PApplet;

public class demoShapeSuper  extends PApplet {
//http://www.syedrezaali.com/blog/?page_id=746
	Integrator iRadius; 
	Integrator a;
	Integrator b;
	Integrator m;
	Integrator n1; 
	Integrator n2; 
	Integrator n3;
	Integrator stkWeight;
	float theta = 0; 
	int sliderValue = 100;
	public void setup()
	{
	  size(1280,720); 
	  smooth();
	  frameRate(60); 
	  
	  iRadius = new Integrator(65,.2f,.2f);
	  a = new Integrator(1,.2f,.2f);
	  b = new Integrator(.1f,.2f,.2f);
	  m = new Integrator(10.8f,.2f,.2f);
	  n1 = new Integrator(6.5f,.2f,.2f);
	  n2 = new Integrator(24.25f,.2f,.2f);
	  n3 = new Integrator(-4.25f,.2f,.2f);
	  stkWeight = new Integrator(4.05f,.2f,.2f);	 
	  background(0);   
	}
	public void draw()
	{
		  iRadius.update();
		  a.update();
		  b.update();
		  m.update();
		  n1.update();
		  n2.update();
		  n3.update();
		  stkWeight.update(); 
		  background(0); 
		  stroke(255); 
		  strokeCap(ROUND); 
		  noFill();
		  strokeWeight(stkWeight.value); 
		  beginShape();
		  for(theta = 0; theta <TWO_PI+0.001; theta+=0.005)
		  {
		     float raux = pow(abs(1.0f/a.value)*abs(cos((m.value*theta/4.0f))),n2.value) + pow(abs(1.0f/b.value)*abs(sin(m.value*theta/4.0f)),n3.value);
		     float r = iRadius.value*pow(abs(raux),(-1.0f/n1.value));
		     float x=width*.5f+r*cos(theta);
		     float y=height*.5f+r*sin(theta); 
		     vertex(x,y); 
		  }    
		  endShape(); 
		}


	class Integrator {

		  final float DAMPING = 0.5f;
		  final float ATTRACTION = 0.2f;

		  float value;
		  float vel;
		  float accel;
		  float force;
		  float mass = 1;

		  float damping = DAMPING;
		  float attraction = ATTRACTION;
		  boolean targeting;
		  float target;


		  Integrator() { }


		  Integrator(float value) {
		    this.value = value;
		  }


		  Integrator(float value, float damping, float attraction) {
		    this.value = value;
		    this.damping = damping;
		    this.attraction = attraction;
		  }


		  void set(float v) {
		    value = v;
		  }


		  void update() {
		    if (targeting) {
		      force += attraction * (target - value);      
		    }

		    accel = force / mass;
		    vel = (vel + accel) * damping;
		    value += vel;

		    force = 0;
		  }


		  void target(float t) {
		    targeting = true;
		    target = t;
		  }


		  void noTarget() {
		    targeting = false;
		  }
		}
}