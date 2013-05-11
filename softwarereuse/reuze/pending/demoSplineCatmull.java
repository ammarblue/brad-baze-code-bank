package reuze.pending;

import com.software.reuze.ga_SplineCatmull;
import com.software.reuze.ga_Vector2;

import processing.core.PApplet;

public class demoSplineCatmull extends PApplet {
   ga_SplineCatmull sc;
   ga_Vector2 v[];
   float x;
   public void setup() {
	   size(400,400);
	   sc=new ga_SplineCatmull();
	   v=new ga_Vector2[7];
	   v[0]=new ga_Vector2(50,100);
	   v[1]=new ga_Vector2(75,150);
	   v[2]=new ga_Vector2(100,50);
	   v[3]=new ga_Vector2(150,200);
	   v[4]=new ga_Vector2(50,300);
	   v[5]=new ga_Vector2(20,20);
	   v[6]=new ga_Vector2(80,100);
	   sc.initialize(v, null);
	   frameRate(1);
	   x=0;
	   background(-1);
   }
   ga_Vector2 xx;
   public void draw() {
	   fill(color(0,255,0));
	   ga_Vector2 xy=sc.evaluate(x);
	   this.ellipse(xy.x, xy.y, 20, 20);
	   stroke(-1);
	   if (xx!=null) line(width/2,height/2,width/2+xx.x,height/2+xx.y);
	   xx=sc.velocity(x);
	   stroke(0);
	   line(width/2,height/2,width/2+xx.x,height/2+xx.y);
	   text("velocity",width/2+10,height/2);
	   x+=0.01;
	   if (x>1) x=0;
   }
}
