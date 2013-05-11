package reuze.demo;
import java.util.Iterator;

import com.software.reuze.m_InterpolateValueCosine;
import com.software.reuze.vc_ColorGradient;
import com.software.reuze.vc_ColorList;
import com.software.reuze.z_Colors;

import processing.core.PApplet;

public class demoToxiColorGradient  extends PApplet {
	public void setup() {
		  size(1000,200);
		  //noLoop();
		}

		public void draw() {
		  vc_ColorGradient grad=new vc_ColorGradient();
		  // use alternative interpolation function when mouse is pressed
		  if (!mousePressed) {
		    grad.setInterpolator(new m_InterpolateValueCosine());
		  }
		  grad.addColorAt(0,z_Colors.getColor("BLACK"));
		  grad.addColorAt(width,z_Colors.getColor("BLUE"));
		  grad.addColorAt(mouseX,z_Colors.getColor("RED"));
		  grad.addColorAt(width-mouseX,z_Colors.getColor("YELLOW"));
		  vc_ColorList l=grad.calcGradient(0,width);
		  int x=0;
		  for(Iterator i=l.iterator(); i.hasNext();) {
		    z_Colors c=(z_Colors)i.next();
		    int y=c.toARGB();
		    stroke(y);
		    line(x,0,x,height);
		    x++;
		  }
		}
}