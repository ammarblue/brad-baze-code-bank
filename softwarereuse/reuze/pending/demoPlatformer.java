package reuze.pending;

import com.software.reuze.f_In;
import com.software.reuze.f_StdIn;

import processing.core.PApplet;
import processing.core.PImage;

public class demoPlatformer extends PApplet {
	PImage background[];
	StringBuffer tileRows[];
	int N,W;
	PImage tiles[];
	PImage player;
	float playerX,playerY;
	float vx,vy,ax,ay;
	public void setup() {
		size(800,480);
		background=new PImage[3];
		background[0]=loadImage("res/img/Backgrounds/Layer0_0.png");
		background[1]=loadImage("res/img/Backgrounds/Layer1_0.png");
		background[2]=loadImage("res/img/Backgrounds/Layer2_0.png");
		N=height/32;
		W=width/40;
		tileRows=new StringBuffer[N];
		f_In f=new f_In("./data/res/levels/0.txt");
		for (int i=0;;i++) {
			String ss=f.readLine();
			if (ss==null) break;
			StringBuffer s=new StringBuffer(ss);
			if (ss.equals("....................")) s=null;
			else {
				int j=ss.indexOf('1');
				if (j>=0) {
					playerY=i; playerX=j;
					s.setCharAt(j, '.');
				}
			}
			tileRows[i]=s;
			System.out.println(s);
		}
		f.close();
		tiles=new PImage[3];
		tiles[0]=loadImage("res/img/Tiles/BlockA0.png");
		tiles[1]=loadImage("res/img/Tiles/Gem.png");
		tiles[2]=loadImage("res/img/Tiles/Exit.png");
		PImage p=loadImage("res/img/Player.png");
		player=createImage(64,64,ARGB);
		player.blend(p, 0, 128, 64, 64, 0, 0, 64, 64, ADD);
	}
	public void draw() {
		image(background[0],0,0);
		image(background[1],0,0);
		image(background[2],0,0);
		for (int i=0; i<N; i++) {
			if (tileRows[i]==null) continue;
			for (int j=0; j<W; j++) {
				if (tileRows[i].charAt(j)=='#') image(tiles[0],j*40,i*32);
				if (tileRows[i].charAt(j)=='G') image(tiles[1],j*40+random(2),i*32+random(2));
				if (tileRows[i].charAt(j)=='X') image(tiles[2],j*40,i*32);
			}
		}
		image(player,((int)playerX)*40,((int)playerY-1)*32);
		movePlayer(vx,vy);
		vy+=ay;
	}
	public void keyPressed() {
		if (key=='d') {vy=0; movePlayer(1,0); }
		else if (key=='a') {vy=0; movePlayer(-1,0); }
		else if (vy==0 && key=='w') {vx=0.2f; vy-=0.6f; ay=0.05f;}
		else if (vy==0 && key=='s') {
			vx=-0.25f; vy-=0.6f; ay=0.05f;}
	}
	public void movePlayer(float x, float y) { //x and y should not be larger than 1
		if (x==0 && y==0) return;
		float temp=playerX+x, tempY=playerY+y;
		if (temp<=0) temp=0;
		else if (temp>=W) temp=W-1;
		if (tempY<=1) tempY=1;
		else if (tempY>=N) tempY=N-1;
		if (tileRows[(int) tempY]!=null && tileRows[(int) tempY].charAt((int) temp)!='.') {
			vx=0; vy=0; ay=0; y=0;
			temp=playerX; tempY=playerY;
		}
		if (tileRows[(int) tempY-1]!=null && tileRows[(int) tempY-1].charAt((int) temp)!='.') {
			vx=0; vy=0; ay=0; y=0;
			temp=playerX; tempY=playerY;
		}
		if (y==0 && tempY!=(N-1)) {
			if (tileRows[(int) tempY+1]==null) ay=0.05f;
			else if (tileRows[(int) tempY+1].charAt((int) temp)=='.') ay=0.05f;
		}
		playerX=temp;
		playerY=tempY;
	}
}
