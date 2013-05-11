package com.software.reuze;
//import javax.xml.bind.annotation.XmlAttribute;
//import javax.xml.bind.annotation.XmlRootElement;

//@XmlRootElement
public class ga_EllipseProperties implements da_i_Validate {
	public float cx, cy;
	public float rx;
	public float ry;
	public vg_StyleProperties draw;
	public String id, transform, style;
	private static final vg_StyleProperties defaults=new vg_StyleProperties("black",null,1);
	public float getCx() {
		return cx;
	}
//	@XmlAttribute
	public void setCx(float cx) {
		this.cx = cx;
	}
	public float getCy() {
		return cy;
	}
//	@XmlAttribute
	public void setCy(float cy) {
		this.cy = cy;
	}
	public float getRx() {
		return rx;
	}
//	@XmlAttribute
	public void setRx(float r) {
		this.rx = r;
	}
	public float getRy() {
		return ry;
	}
//	@XmlAttribute
	public void setRy(float r) {
		this.ry = r;
	}
	public vg_StyleProperties getDraw() {
		return draw;
	}
	public void setDraw(vg_StyleProperties draw) {
		this.draw = draw;
	}
	public String getId() {
		return id;
	}
//	@XmlAttribute
	public void setId(String id) {
		this.id = id;
	}
	public String getTransform() {
		return transform;
	}
//	@XmlAttribute
	public void setTransform(String transform) {
		this.transform = transform;
	}
	public String getStyle() {
		return style;
	}
//	@XmlAttribute
	public void setStyle(String style) {
		this.style = style;
	}
	public boolean isValid(int extra) {
		if (style!=null) {
	 		   draw=draw.create(style);
	 		   style=null;
	 		   return draw.isValid(0);
	 	} else if (draw==null) draw=defaults;
		return true;
	}

}
