package reuze.pending;

import com.software.reuze.z_BufferedImage;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;
import reuze.awt.ib_EdgeDetectionCanny;
import reuze.awt.ib_EdgeDetectionSobel;

public class demoEdgeDetection extends PApplet {
	PImage img, img2;
	z_BufferedImage buf;
	public void setup() {
		size(500,500);
		img=loadImage("mearth.jpg");
		buf=new z_BufferedImage(img.width,img.height,z_BufferedImage.TYPE_INT_ARGB);
		int[] rgbArray=img.pixels;
		for(int i=0;i<img.width;i++){
			for(int j=0;j<img.height;j++){
				buf.setRGB(j,i,rgbArray[i*img.height+j]);
			}
		}
		buf=canny(buf);
		/*ib_EdgeDetectionSobel sobel = new ib_EdgeDetectionSobel();
		buf = sobel.findEdgesAll(buf, 90);
		buf = sobel.noiseReduction(buf, 1);*/
		img2=createImage(buf, img.width, img.height);
	}
	public void draw() {
		image(img2,40,40);
	}
	public PImage createImage(z_BufferedImage frame, int width, int height) {
		PImage img=new PImage(frame.getWidth(),frame.getHeight(),PConstants.ARGB);
		frame.getRGB(0, 0, img.width, img.height, img.pixels, 0, img.width);
		img.updatePixels();
		img.resize(width,height);
		return img;
	}
	public z_BufferedImage canny(z_BufferedImage buf) {
		 //create the detector
		 ib_EdgeDetectionCanny detector = new ib_EdgeDetectionCanny();
		 //adjust its parameters as desired
		 detector.setLowThreshold(0.5f);
		 detector.setHighThreshold(1f);
		 //apply it to an image
		 detector.setSourceImage(buf);
		 detector.process();
		 buf = detector.getEdgesImage();
		 return buf;
	}
}
