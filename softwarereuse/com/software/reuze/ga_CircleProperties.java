package com.software.reuze;
public class ga_CircleProperties implements da_i_Validate {
	public float cx, cy;
	public float r;
	public vg_StyleProperties draw;
	public String id, transform, style;
	private static final vg_StyleProperties defaults=new vg_StyleProperties("black",null,1);
	public float getCx() {
		return cx;
	}
	public void setCx(float cx) {
		this.cx = cx;
	}
	public float getCy() {
		return cy;
	}
	public void setCy(float cy) {
		this.cy = cy;
	}
	public float getR() {
		return r;
	}
	public void setR(float r) {
		this.r = r;
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
	public boolean isValid(int extra) {
		if (style!=null) {
	 		   draw=draw.create(style);
	 		   style=null;
	 		   return draw.isValid(0);
	 	} else if (draw==null) draw=defaults;
		return true;
	}

}
