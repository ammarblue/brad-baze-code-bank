package reuze.pending;

import processing.core.PApplet;
import processing.core.PImage;

public class demoSpriteImagePS extends PApplet implements SpriteManagerI {
	PImage im=null, im2=null;
	SpriteImage si=null;
	public void setup() {
		size(400,400,P3D);
		im=loadImage("./res/img/MonsterD.png");
		si=new SpriteImage(im,0,0,64,64,0,0,0,this);;		
		si.sheet=im;
		si.xPic0=0;
		si.yPic0=0;
		si.wPic=64;
		si.hPic=64;
		si.xPic=0;
		si.yPic=0;
		si.width=64;
		si.height=64;
		si.count=5;
		si.draw=this;
		for (int i=0; i<si.count; i++) {
		  im2=createImage(si.width,si.height,ARGB);
		  im2.blend(im, si.xPic+si.xPic0+i*si.wPic, si.yPic+si.yPic0, si.width, si.height, 0, 0, si.width, si.height, ADD);
		  si.setCache(i, im2);
		}
		frameRate(5);
	}
	public void setup2() {
		size(400,400);
		im=loadImage("./data/enemysheet.png");
		si=new SpriteImage(im,0,0,16,32,0,4,3,this);;		
		si.sheet=im;
		si.xPic0=0;
		si.yPic0=4;
		si.wPic=16;
		si.hPic=32;
		si.xPic=0;
		si.yPic=0;
		si.width=16;
		si.height=28;
		si.count=3;
		si.draw=this;
		for (int i=0; i<si.count; i++) {
		  im2=createImage(si.width,si.height,ARGB);
		  im2.blend(im, si.xPic+si.xPic0+i*si.wPic, si.yPic+si.yPic0, si.width, si.height, 0, 0, si.width, si.height, ADD);
		  si.setCache(i, im2);
		}
		frameRate(5);
	}
	int count=0;
	public void draw() {
		background(100,100,100);
		line(50,50,100,50);
		scale(2);
		//translate(50,50);
		//rotateY(radians(180));
		//translate(-50,-50);
		si.render(null,count,25,25);
		count=(count+1)%si.count;
	}

	public void render(Object graphics, int screenX, int screenY, Object cache) {
		image((PImage)cache,screenX,screenY);
	}

	public void render(Object graphics, int screenX, int screenY, int x, int y,
			int w, int h, Object o) {
		// TODO Auto-generated method stub
		
	}

	public void render(Object graphics, int screenX, int screenY, float rotate,
			float scale, Object object) {
		// TODO Auto-generated method stub
		
	}

	public void render(Object graphics, int screenX, int screenY, int x, int y,
			int w, int h, float rotate, float scale, Object sheet) {
		// TODO Auto-generated method stub
		
	}
}
