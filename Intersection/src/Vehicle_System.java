import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PVector;

public class Vehicle_System extends PApplet{
	ArrayList vehicles;
	int num;

	Vehicle_System(int num){
    this.num=num;
    vehicles=new ArrayList();
    PVector position;
    PVector speed;
    int c;
    for(int i=0;i<num;i++){
      //initializing code
      c= color(random(10,255),random(10,255),random(10,255));
      if (random(0,1) < 0.5) {
        position = new PVector(300+(float)(i*80)*pow(-1,i%2),(float)(300),0);
        //position on the x-achsis
        speed  = new PVector(0.4f*(i+1),0,0);
        //speed in the positive x-direction
        vehicles.add(new PKW(position,speed,4.0f,1.5f+i*0.1f,0.25f*TWO_PI,c));
      } 
      else {
        position = new PVector((float)(300),(float)(i*50),0);
        // in the y-achsis
        speed = new PVector(0,0.4f*(i+1),0);
        //speed in the y-direction downwards
        vehicles.add(new LKW(position,speed,2.5f,1.0f+i*0.12f,0*TWO_PI,c)); 
      }
    }
  }

	public Vehicle_System() {
		// TODO Auto-generated constructor stub
	}

	// -------------------------------------------------------------------------update
	// (vehicle system)
	void update(){
    for(int i=0;i<vehicles.size();i++){
      Vehicle v = (Vehicle) vehicles.get(i);
      float zuf=round(random(1,3));
      if((int)(zuf)==2)zuf=1;

      if(abs(v.angle_to_go-0.75f*TWO_PI)<0.01f&&abs(v.ort.x-340)<1){
        v.abbiegen(zuf*PI/2);
      }
      else if(abs(v.angle_to_go-0.25f*TWO_PI)<0.01f&&abs(v.ort.x-260)<1){
        //horiz. nach rechts  
        v.abbiegen(zuf*PI/2);
      }
      else if(abs(v.angle_to_go-0.5f*TWO_PI)<0.01f&&abs(v.ort.y-340)<1){
        v.abbiegen(zuf*PI/2);
        //vertikal nach oben
      }
      else if((abs(v.angle_to_go)<0.01||abs(v.angle_to_go-TWO_PI)<0.01)&&abs(v.ort.y-260)<1){
        v.abbiegen(zuf*PI/2);
      }
      v.update();
      v.display();
      values_display(v,i);
    }
  }

	void values_display(Vehicle v, int i) {
		fill(255);
		text(i, 650 + i * 30, 400);

	}

	// -----------------------------------------------------------------background
	void backgr() {
		background(0);
		fill(0);
		noStroke();
		rect(300, 300, 1200, 80);
		rect(300, 300, 80, 600);
		strokeWeight(2);
		stroke(255);
		strokeCap(PROJECT);
		dottedLine(0, 300, 255, 300, 20);
		dottedLine(350, 300, 900, 300, 40);
		dottedLine(300, 0, 300, 255, 20);
		dottedLine(300, 350, 300, 600, 20);
		line(250, 305, 250, 335);
		line(350, 265, 350, 295);
		line(305, 350, 335, 350);
		line(265, 255, 295, 255);
		fill(200, 255, 20);

		text("vehicle nr:     ", 550, 400);
		text("braking:        ", 550, 420);
		text("at crossing:    ", 550, 440);
		text("waiting at cr.: ", 550, 460);
		text("can`t go left: ", 550, 480);
		noStroke();
		rectMode(CORNER);
		fill(80);
		rect(630, 425, 300, 20);
		rect(630, 465, 300, 20);
		rect(630, 385, 300, 20);
		fill(255);
		rectMode(CENTER);
	}

	// ------------------------------------------------------dottedLine
	void dottedLine(int x1, int y1, int x2, int y2, int steps) {
		for (int i = 0; i < steps; i = i + 2) {
			line(x1 + i * (x2 - x1) / steps, y1 + i * (y2 - y1) / steps, x1
					+ (i + 1) * (x2 - x1) / steps, y1 + (i + 1) * (y2 - y1)
					/ steps);
		}
	}
}
