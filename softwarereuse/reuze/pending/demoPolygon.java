package reuze.pending;

import com.software.reuze.ga_Polygon;
import com.software.reuze.ga_Vector2;

import processing.core.PApplet;

public class demoPolygon extends PApplet {
	public final static int N=150;
	ga_Polygon p,q;
	public void setup() {
		size(400,400);
		int X=200, Y=200;
		p=new ga_Polygon();
		p.add(new ga_Vector2(X,Y));
		p.add(new ga_Vector2(X,Y-N));
		p.add(new ga_Vector2(X+N/2,Y-N));
		p.add(new ga_Vector2(X+N/2,Y-N/2));
		p.add(new ga_Vector2(X+5,Y-N/2));
		p.add(new ga_Vector2(X+N*3/4,Y));
		p.add(new ga_Vector2(X,Y));
		p.calcAll();
		q=new ga_Polygon(p);
		q.calcCenter();
		q.scale(0.25f);
	}
	public void draw() {
		background(0);
		q.translateTo(mouseX, mouseY);
		if (p.contains(q)) fill(0,255,0);
		else if (p.intersects(q)) fill(255,0,0); else fill(-1);
		beginShape();
		for (ga_Vector2 v:p.points) vertex(v.x,v.y);
		endShape();
		beginShape();
		for (ga_Vector2 v:q.points) vertex(v.x,v.y);
		endShape();
	}
}
