package com.software.reuze;

public class ff_SVGTextStyle extends ff_SVGText implements da_i_Validate {
	public vg_StyleProperties draw;
	private static final vg_StyleProperties defaults=new vg_StyleProperties("black",null,1);
	public ff_SVGTextStyle(ff_SVGText object) {
		x=object.x;
		y=object.y;
		content=object.content;
		id=object.id;
		style=object.style;
		transform=object.transform;
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
