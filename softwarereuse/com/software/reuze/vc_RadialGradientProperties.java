package com.software.reuze;
import java.util.ArrayList;

//import javax.xml.bind.annotation.XmlAttribute;
//import javax.xml.bind.annotation.XmlElement;
//import javax.xml.bind.annotation.XmlRootElement;

//@XmlRootElement
public class vc_RadialGradientProperties implements da_i_Validate {
	public String cx, cy, r, fx, fy, spreadMethod, gradientTransform, gradientUnits;
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
	public String getCx() {
		return cx;
	}
//	@XmlAttribute
	public void setCx(String cx) {
		this.cx = cx;
	}
	public String getCy() {
		return cy;
	}
//	@XmlAttribute
	public void setCy(String cy) {
		this.cy = cy;
	}
	public String getR() {
		return r;
	}
//	@XmlAttribute
	public void setR(String r) {
		this.r = r;
	}
	public String getFx() {
		return fx;
	}
//	@XmlAttribute
	public void setFx(String fx) {
		this.fx = fx;
	}
	public String getFy() {
		return fy;
	}
//	@XmlAttribute
	public void setFy(String fy) {
		this.fy = fy;
	}
	public boolean isValid(int extra) {
		if (c==null) {
		CX=cx==null?0.5f:vc_LinearGradientProperties.getPercent(cx);
		CY=cy==null?0.5f:vc_LinearGradientProperties.getPercent(cy);
		R=r==null?1f:vc_LinearGradientProperties.getPercent(r);
		FX=fx==null?0.5f:vc_LinearGradientProperties.getPercent(fx);
		FY=fy==null?0.5f:vc_LinearGradientProperties.getPercent(fy);
		
		if (spreadMethod==null) spreadMethod="pad";
		if (gradientUnits!=null && gradientUnits.compareToIgnoreCase("userspaceonuse")!=0) gradientUnits=null;
        int size = stopList.size();
        c=new z_Colors[size];
        f=new float[size];
        vc_LinearGradientProperties.getGradients(stopList, f ,c);
		}
		return true;
	}
    public z_Colors c[];
    public float f[];
	float CX, CY, R, FX, FY;
}