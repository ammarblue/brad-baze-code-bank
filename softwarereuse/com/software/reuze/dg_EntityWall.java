package com.software.reuze;

import com.software.reuze.ga_Geometry2D;

public class dg_EntityWall extends dg_a_EntityBase { //used to outline buildings

	ga_Vector2D end = new ga_Vector2D();
	ga_Vector2D wallNorm;
	
	public dg_EntityWall(ga_Vector2D start, ga_Vector2D end, boolean visible) {
		this("", start, end, visible);
	}

	public dg_EntityWall(String name, ga_Vector2D start, ga_Vector2D end, boolean visible) {
		super(name, start, 0);
		this.end.set(end.x,end.y);
		wallNorm = new ga_Vector2D(-(end.y - start.y), end.x - start.x);
		wallNorm.normalize();
		this.visible = visible;
	}

	public ga_Vector2D start(){
		return pos;
	}
	
	public ga_Vector2D end(){
		return end;
	}
	
	public ga_Vector2D norm(){
		return wallNorm;
	}

	public boolean isEitherSide(double x0, double y0, double x1, double y1){
		return ga_Geometry2D.line_line(x0, y0, x1, y1, pos.x, pos.y, end.x, end.y);
	}
	
	@Override
	public boolean isInDomain(dg_EntityDomainRectangular view) {
		return ga_Geometry2D.line_box_xyxy(pos.x, pos.y, end.x, end.y, view.lowX, view.lowY, view.highX, view.highY);
	}

	@Override
	public boolean isOver(double px, double py) {
		return false;
	}
	
}
