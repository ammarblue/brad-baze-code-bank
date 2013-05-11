package reuze.demo;

//import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.software.reuze.z_BufferedImage;
import com.software.reuze.z_GIFDecoder;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;


public class demoGIF extends PApplet {
	ArrayList<PImage> animation;

	public void setup() {
	  size(400, 200);
	  frameRate(10);
	  animation= new ArrayList<PImage>();
	  z_GIFDecoder d = new z_GIFDecoder();
	  d.read("./data/bonusmany.gif");
	  int n = d.getFrameCount();
	  for (int i = 0; i < n; i++) {
	  	z_BufferedImage frame = d.getFrame(i);  // frame i
	  	int t = d.getDelay(i);  // display duration of frame in milliseconds
	  	System.out.println(i+" "+t);
	  	PImage img=new PImage(frame.getWidth(),frame.getHeight(),PConstants.ARGB);
	  	img.loadPixels();
	    frame.getRGB(0, 0, img.width, img.height, img.pixels, 0, img.width);
	    img.updatePixels();
	    img.resize(32,32);
	    animation.add(img);
	  }
	}
    int frame;
	public void draw() {
	  background(255 / (float)height * (mouseY+1));
	  if (animation.size()!=0) {
		  int i=frame%animation.size();
		  image(animation.get(i),50,50);
		  frame++;
	  }
	}
}
