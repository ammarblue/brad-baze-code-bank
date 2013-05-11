package com.software.reuze;
//import javax.xml.bind.annotation.XmlAttribute;
//import javax.xml.bind.annotation.XmlRootElement;

//@XmlRootElement
public class vc_GradientStopProperties implements da_i_Validate {
	public String style;
	public String offset;
	public String color;
	public float opacity=1;
	public String getStyle() {
		return style;
	}
//	@XmlAttribute
	public void setStyle(String style) {
		this.style = style;
	}
	public boolean isValid(int extra) {
		return true;
	}
	public String getOffset() {
		return offset;
	}
//	@XmlAttribute
	public void setOffset(String offset) {
		this.offset = offset;
	}

}
