package com.software.reuze;
import java.util.ArrayList;


//import javax.xml.bind.annotation.XmlAttribute;
//import javax.xml.bind.annotation.XmlElement;
//import javax.xml.bind.annotation.XmlRootElement;

//import org.w3c.dom.Element;

//@XmlRootElement
public class vc_LinearGradientProperties implements da_i_Validate {
	public String x1, y1, x2, y2,spreadMethod,gradientTransform,gradientUnits;
	public String id;
//	@XmlElement(name="stop")
	public ArrayList<vc_GradientStopProperties> stopList;
	public void setStopList(ArrayList<vc_GradientStopProperties> stopList) {
		this.stopList = stopList;
	}
	public String getGradientTransform() {
		return gradientTransform;
	}
//	@XmlAttribute
	public void setGradientTransform(String gradientTransform) {
		this.gradientTransform = gradientTransform;
	}
	public String getGradientUnits() {
		return gradientUnits;
	}
//	@XmlAttribute
	public void setGradientUnits(String gradientUnits) {
		this.gradientUnits = gradientUnits;
	}
	public String getSpreadMethod() {
		return spreadMethod;
	}
//	@XmlAttribute
	public void setSpreadMethod(String spreadMethod) {
		this.spreadMethod = spreadMethod;
	}
	public String getId() {
		return id;
	}
//	@XmlAttribute
	public void setId(String id) {
		this.id = id;
	}
	public String getX1() {
		return x1;
	}
//	@XmlAttribute
	public void setX1(String x1) {
		this.x1 = x1;
	}
	public String getY1() {
		return y1;
	}
//	@XmlAttribute
	public void setY1(String y1) {
		this.y1 = y1;
	}
	public String getX2() {
		return x2;
	}
//	@XmlAttribute
	public void setX2(String x2) {
		this.x2 = x2;
	}
	public String getY2() {
		return y2;
	}
//	@XmlAttribute
	public void setY2(String y2) {
		this.y2 = y2;
	}
	public static float getPercent(String s) {
		if (s==null) return 0;
		int i=s.indexOf('%');
		if (i>0) {
			float f = Float.valueOf(s.substring(0, i));
			if (f < 0) f=0;
			if (f > 100) f=100;
			return f/100f;
		}
		return 0;
	}
	public static void getGradients(ArrayList<vc_GradientStopProperties> stopList, float[] f /*inout*/, z_Colors[] c /*inout*/) {
		for (int i = 0; i < stopList.size(); i++) {
          	 vc_GradientStopProperties eel=stopList.get(i);
          	 if (eel.offset!=null) f[i]=getPercent(eel.offset);
          	 if ((i>0)&&(f[i]<f[i-1])) f[i]=f[i-1];
          	 if (eel.style!=null) {
          		 vg_StyleProperties s=null;
          		 s=s.create(eel.style);
             	 c[i]=new z_Colors(vc_ColorNamesInt.getARGB(s.stopColor, s.stopOpacity));
          	 } else c[i]=new z_Colors(vc_ColorNamesInt.getARGB(eel.color, eel.opacity));
       }
	}
	public boolean isValid(int extra) {
		if (c==null) {
		X1=getPercent(x1);
		X2=getPercent(x2);
		Y1=getPercent(y1);
		Y2=getPercent(y2);
		if (spreadMethod==null) spreadMethod="pad";
		if (gradientUnits!=null && gradientUnits.compareToIgnoreCase("userspaceonuse")!=0) gradientUnits=null;
        int size = stopList.size();
        c=new z_Colors[size];
        f=new float[size];
        getGradients(stopList, f ,c);
		}
		return true;
	}
    public float X1, X2=100, Y1, Y2;
    public z_Colors c[];
    public float f[];
}