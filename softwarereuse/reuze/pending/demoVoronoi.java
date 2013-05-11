package reuze.pending;

import com.software.reuze.ga_Vector2;

import processing.core.PApplet;

public class demoVoronoi extends PApplet {
	ga_Vector2 p[];
	Voronoi v;
	public void setup() {
		size(400,400);
		p=new ga_Vector2[10];
		for (int i=0; i<p.length; i++)
			p[i]=new ga_Vector2(random(300),random(300));
		v=new Voronoi(0,0,400,400);
		v.compute(p, 0, p.length-1);
	}
	public void draw() {
		translate(50,50);
		fill(0);
		for (int i=0; i<p.length; i++)
			ellipse(p[i].x,p[i].y,4,4);
		fill(0,255,0);
		if (v.ok) {
			for (Voronoi.Site s:v.sites) {
				for (ga_Vector2 v:s.points) ellipse(v.x,v.y,4,4);
			}
		}
	}
}
