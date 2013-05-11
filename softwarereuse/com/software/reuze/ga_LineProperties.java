package com.software.reuze;
//import javax.xml.bind.annotation.XmlAttribute;
//import javax.xml.bind.annotation.XmlRootElement;

//@XmlRootElement
public class ga_LineProperties implements da_i_Validate {
	public float x1, y1;
	public float x2;
	public float y2;
    public vg_StyleProperties draw;
    public String id, transform, style;
	public static final vg_StyleProperties defaults=new vg_StyleProperties(null,"black",1);
	@Override
	public String toString() {
		return "line [x1=" + x1 + ", y1=" + y1 + ", x2=" + x2 + ", y2=" + y2
				+ ", draw=" + draw + ", id=" + id + ", transform=" + transform
				+ ", style=" + style + "]";
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
	public float getX1() {
		return x1;
	}
//	@XmlAttribute
	public void setX1(float x1) {
		this.x1 = x1;
	}
	public float getY1() {
		return y1;
	}
//	@XmlAttribute
	public void setY1(float y1) {
		this.y1 = y1;
	}
	public float getX2() {
		return x2;
	}
//	@XmlAttribute
	public void setX2(float x2) {
		this.x2 = x2;
	}
	public float getY2() {
		return y2;
	}
//	@XmlAttribute
	public void setY2(float y2) {
		this.y2 = y2;
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
