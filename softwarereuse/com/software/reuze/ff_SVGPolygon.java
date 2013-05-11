package com.software.reuze;
//import javax.xml.bind.annotation.XmlAttribute;
//import javax.xml.bind.annotation.XmlRootElement;

//@XmlRootElement(name="polygon")
public class ff_SVGPolygon {
	public String id;
	public String transform, style, points;
	public String getPoints() {
		return points;
	}
//	@XmlAttribute
	public void setPoints(String points) {
		this.points = points;
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
	public vg_PathProperties getPath() {
		if (points==null) return null;
		StringBuilder sb=new StringBuilder(points);
		sb.insert(0, 'M');
		int i=0;
		for (;;) {
			i=sb.indexOf(" ", i);
			if (i<=0) break;
			sb.replace(i, i+1, "L");
			i++;
		}
		System.out.println(sb.toString());
		vg_PathProperties p=new vg_PathProperties();
		p.d=sb.toString();
		p.id=id;
		p.style=style;
		p.transform=transform;
		return p;
	}

}
