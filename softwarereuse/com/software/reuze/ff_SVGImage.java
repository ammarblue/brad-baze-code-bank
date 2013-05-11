package com.software.reuze;
//import javax.xml.bind.annotation.XmlAttribute;
//import javax.xml.bind.annotation.XmlRootElement;

//@XmlRootElement(name="image")
public class ff_SVGImage implements da_i_Validate { 
	public float x=Float.NaN;
	public float y;
	public float width, height;
	public String id, transform;
	public String image;
	public String getImage() {
		return image;
	}
//	@XmlAttribute(name="xlink-href")
	public void setImage(String image) {
		this.image = image;
	}
	public String getTransform() {
		return transform;
	}
//	@XmlAttribute
	public void setTransform(String transform) {
		this.transform = transform;
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
	public boolean isValid(int extra) {
	   if (x==Float.NaN) return false;
	   return true;
	}
}