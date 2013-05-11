package com.software.reuze;
//import javax.xml.bind.annotation.XmlAttribute;
//import javax.xml.bind.annotation.XmlRootElement;

//@XmlRootElement
public class ga_RectangleProperties implements da_i_Validate { 
	public float x=Float.NaN;
	public float y;
	public float width, height;
	public float rx, ry;
	public vg_StyleProperties draw;
	public String id, transform, style;
	private static final vg_StyleProperties defaults=new vg_StyleProperties("black",null,1);
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
	public void setWH(float width, float height) {
		this.width=width;  this.height=height;
	}
	public void setWH(int width, int height) {
		this.width=width;  this.height=height;
	}
	public void setXY(float x, float y) {
		this.x=x;  this.y=y;
	}
	public void setXY(int x, int y) {
		this.x=x;  this.y=y;
	}
	public float getRx() {
		return rx;
	}
//	@XmlAttribute
	public void setRx(float rx) {
		this.rx = rx;
	}
	public void setRx(int rx) {
		this.rx = rx;
	}
	public float getRy() {
		return ry;
	}
//	@XmlAttribute
	public void setRy(float ry) {
		this.ry = ry;
	}
	public void setRy(int ry) {
		this.ry = ry;
	}
    public String getId() {
		return id;
	}
//    @XmlAttribute
	public void setId(String id) {
		this.id = id;
	}
	public float getX() {
		return x;
	}
//    @XmlAttribute
	public void setX(float x) {
		this.x = x;
	}
    public void setX(int x) {
		this.x = x;
	}
	public float getY() {
		return y;
	}
//	@XmlAttribute
	public void setY(float y) {
		this.y = y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public float getWidth() {
		return width;
	}
//	@XmlAttribute
	public void setWidth(float width) {
		this.width = width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public float getHeight() {
		return height;
	}
//	@XmlAttribute
	public void setHeight(float height) {
		this.height = height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	@Override
	public String toString() {
		return "rect [id="+id+", x=" + x + ", y=" + y + ", width=" + width + ", height="
				+ height + ", style=" + draw + ", transform=" + transform + ", rx="+rx+ ", ry="+ry+"]";
	}
	public boolean isValid(int extra) {
	   if (x==Float.NaN) return false;
	   if (rx<0) rx=Math.max(0,ry);
 	   if (ry<0) ry=rx;
 	   rx=Math.min(width/2, rx);
 	   ry=Math.min(height/2, ry);
 	   if (style!=null) {
 		   draw=draw.create(style);
 		   style=null;
 		   return draw.isValid(0);
 	   } else if (draw==null) draw=defaults;
	   return true;
	}
	public vg_StyleProperties getDraw() {
		return draw;
	}
	public void setDraw(vg_StyleProperties draw) {
		this.draw = draw;
	}
}