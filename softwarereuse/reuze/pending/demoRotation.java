package reuze.pending;
import processing.core.PApplet;
import java.util.ArrayList;

import com.software.reuze.ga_RectangleRotated;
import com.software.reuze.ga_Vector2;

public class demoRotation extends PApplet {

	ArrayList<ColoredRect> r=new ArrayList<ColoredRect>();
	ColoredRect cr=new ColoredRect();
	PApplet app;
	public void setup() {
	  size(400, 400);
	  cr.fill=true;
	  cr.set(100, 100, 150, 100);
	  cr.velocity=1;
	  cr.Origin=new ga_Vector2(175,150);
	  app=this;
	}

	public void draw() {
	  background(-1);
	  for (ColoredRect R:r) 
		  R.fill=cr.Intersects(R);
	  cr.draw();
	  for (ColoredRect R:r) R.draw();
	}
	public void mousePressed() {
		ColoredRect cr=new ColoredRect();
		cr.set(mouseX,mouseY,random(10,160), random(10,160));
		cr.Origin=new ga_Vector2(cr.position.x+cr.width/2,cr.position.y+cr.height/2);
		cr.angle=(int) random(360);
		cr.velocity=(int) random(1,10);
		r.add(cr);
	}
	class ColoredRect extends ga_RectangleRotated {
		int angle, velocity;
		boolean fill;
		public ColoredRect() {
			super();
		}
		public void draw() {
			if (this.fill) fill(0,0,255); else fill(-1);
			pushMatrix();
			app.translate(position.x+width/2,position.y+height/2);
			app.rotate(Rotation);
			app.rect(-width/2,-height/2,width,height);
			popMatrix();
			angle=(angle+velocity)%360;
			Rotation=(float) Math.toRadians(angle);
		}
	}
}
