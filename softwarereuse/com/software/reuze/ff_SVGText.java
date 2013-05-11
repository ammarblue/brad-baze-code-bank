package com.software.reuze;
/*import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAccessType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "content"
})
@XmlRootElement(name = "text")*/
public class ff_SVGText {

//    @XmlValue
    public String content;
//    @XmlAttribute
    public float x;
//    @XmlAttribute
    public float y;
//    @XmlAttribute
    public String id;
//    @XmlAttribute
    public String transform;
//    @XmlAttribute
    public String style;
    public String getContent() {
        return content;
    }
    public void setContent(String value) {
        this.content = value;
    }
	public float getX() {
		return x;
	}
	
	public void setX(float x) {
		this.x = x;
	}
	public float getY() {
		return y;
	}
	public void setY(float y) {
		this.y = y;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTransform() {
		return transform;
	}
	public void setTransform(String transform) {
		this.transform = transform;
	}
	public String getStyle() {
		return style;
	}
	public void setStyle(String style) {
		this.style = style;
	}
}