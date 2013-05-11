package reuze.demo;
import java.util.Iterator;

import com.software.reuze.vc_ColorHistogram;
import com.software.reuze.vc_ColorHistogramEntry;
import com.software.reuze.z_Colors;

import processing.core.PApplet;
import processing.core.PImage;

public class demoToxiImageColorHistogram  extends PApplet {
	PImage img, workImg;

	float tolerance=0.33f;
	  
	public void setup() {
	  size(1000,500);
	  background(255);
	  noStroke();
	  img=loadImage("../data/pencils.jpg");
	  workImg=new PImage(img.width,img.height,ARGB);
	  // create a color histogram of image, using only 10% of its pixels and the given tolerance
	  vc_ColorHistogram hist=vc_ColorHistogram.newFromARGBArray(img.pixels, img.pixels.length/10, tolerance, true);
	  // now snap the color of each pixel to the closest color of the histogram palette
	  // (that's really a posterization/quantization effect)
	  z_Colors col;
	  for(int i=0; i<img.pixels.length; i++) {
	    col=new z_Colors(img.pixels[i]);
	    z_Colors closest=col;
	    float minD=1;
	    for(vc_ColorHistogramEntry e : hist) {
	      float d=col.distanceToRGB(e.getColor());
	      if (d<minD) {
	        minD=d;
	        closest=e.getColor();
	      }
	    }
	    workImg.pixels[i]=closest.toARGB();
	  }
	  workImg.updatePixels();
	  // display original and posterized images
	  image(img,0,0);
	  image(workImg,workImg.width,0);
	  // display power curve distribution of histogram colors as bar chart
	  float x=0;
	  int w=width/hist.getEntries().size();
	  for(Iterator<vc_ColorHistogramEntry> i=hist.iterator(); i.hasNext() && x<width;) {
	    vc_ColorHistogramEntry e=i.next();
	    println(e.getColor()+": "+e.getFrequency());
	    fill(e.getColor().toARGB());
	    float h=e.getFrequency()*height;
	    rect(x,height-h,w,h);
	    x+=w;
	  }  
	}
}