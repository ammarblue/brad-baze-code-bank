package reuze.demo;

import processing.core.PApplet;

public class demoRoughSea extends PApplet {
	/* OpenProcessing Tweak of *@*http://www.openprocessing.org/sketch/63979*@* */
	/* !do not delete the line above, required for linking your tweak if you re-upload */

	//----------------------------------------------------------
	// sketch:  PG_RoughSea.pde
	// v1.0  2012-06-12  inital release
	//
	// Greetings and credits go to
	//   Jim Bumgardner for his 'Mousy Clouds' sketch #1273 and to  
	//   Sam Hinton for his 'lowlevel' sketch #3614,
	//   who inspired me for this sketch. 
	//
	// key input:
//	     f     toogle frames per second display
//	     s     save as picture 'RoughSea.png'  
	//  <blanc>  stop animation (but not all ;-))
	//----------------------------------------------------------

	//import processing.opengl.PGraphics3D;   // use it if >= v2.0

	int w=600, h=400;
	boolean animate = true;
	boolean showFPS = false;

	//----------------------------------------------------------
	public void setup()
	{
	  size(600, 400, P3D);
	  textSize(18);
	  noStroke();
	  w = width;
	  h = height;
	  noiseDetail(5, 0.5f);
	}
	//----------------------------------------------------------
	public void draw()
	{
	  lights();
	  drawSky();
	  drawWaves();
	  fill (0);
	  if (showFPS) 
	    text (round(frameRate) + " fps", 30,40,20);
	}
	//----------------------------------------------------------
	public void keyPressed()
	{
	  if (key == ' ') animate = !animate;
	  if (key == 'f') showFPS = !showFPS;
	}

	//========================== sky ===========================

	int skyHeight = 280;   // in pixel
	int ps = 5;            // pixel step
	float xNoise = 0.006f;  // noise steps 
	float yNoise = 0.009f;
	float ox, oy;          // cloud origin
	int[] colors = new int[w*skyHeight/ps];

	//----------------------------------------------------------
	void drawSky()
	{
	  colorMode(HSB, 1);
	  if (animate)
	  { ox += 0.005;   // move cloud origin
	    oy += 0.004;
	  }
	  int ni = 0;
	  float xstep = xNoise;
	  float ystep = yNoise;
	  float time = millis() * 0.0001f;  // for cloud animation
	  beginShape(QUADS);
	  for (int y = 0; y < skyHeight; y+=ps)
	  {
	    xstep *= 1.01;   // perspective trick
	    ystep *= 1.03;   
	    float vy = oy+y*ystep;
	    for (int x = 0; x < w+ps; x+=ps)
	    {
	      int n = w/ps+2;
	      float v = noise(ox+x*xstep, vy, time);
	      colors[ni] = color(0.65f-y*0.1f/height, 0.75f-v, 0.5f+v*v);     
	      fill(colors[ni]);
	      if ((x > 0) && (y > 0))
	      { 
	        fill(colors[ni-n]);      vertex(x-ps, y-ps, 20);  
	        fill(colors[ni-n+1]);    vertex(x,    y-ps, 20);
	        fill(colors[ni]);        vertex(x,    y,    20);
	        fill(colors[ni-1]);      vertex(x-ps, y,    20);
	      }
	      ni++;
	    }
	  }
	  endShape();
	}

	//========================= waves ==========================

	int ws = 10;     // wave detail step 
	float x1,y1, x2,y2, r, j=0.008f;

	//----------------------------------------------------------
	void drawWaves()
	{
	  fill(0.6f, 0.8f, 1f, 0.95f);
	  beginShape(TRIANGLES);
	  for (int i=0; i<w*h; i+=ws)
	  {
	    x1 = i%w;
	    y1 = i/w;     
	    y2 = y1+ws;
	    x2 = x1+ws;
	    float v1=nv(y1*w+x1);
	    float v2=nv(y2*w+x2);
	    // triangle 1
	    vertex(x1, v1, y1);
	    vertex(x2, nv(y1*w+x2), y1);
	    vertex(x2, v2, y2);
	    // triangle 2
	    vertex(x2, v2, y2);
	    vertex(x1, nv(y2*w+x1), y2);
	    vertex(x1, v1, y1);

	    i += (x1==0) ? w*(ws-1) : 0;
	  }
	  endShape();
	  if (animate) r -= j;
	}
	//----------------------------------------------------------
	float nv(float i)  // get noise value
	{ 
	  return noise(i%w*j, i*j/w+r)*ws*10+h/2;
	}

}
