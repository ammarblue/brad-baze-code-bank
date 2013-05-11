package com.software.reuze;

import com.software.reuze.ga_Geometry2D;

public class dg_EntityObstacle extends dg_a_EntityBase { 
	public dg_EntityObstacle() {
		super();
	}

	public dg_EntityObstacle(String name, ga_Vector2D pos, double colRadius) {
		super(name, pos, colRadius);
	}

	public dg_EntityObstacle(ga_Vector2D pos, double colRadius) {
		super("", pos, colRadius);
	}

	/**
	 * Determines whether two points are either side of an obstacle. If they are then they cannot 'see' each other.
	 * 
	 * @param x0
	 * @param y0
	 * @param x1
	 * @param y1
	 * @return
	 */
	public boolean isEitherSide(double x0, double y0, double x1, double y1){
		return ga_Geometry2D.line_circle(x0, y0, x1, y1, pos.x, pos.y, colRadius);
	}

	@Override
	public boolean isInDomain(dg_EntityDomainRectangular view) {
		return ga_Geometry2D.box_box(pos.x - colRadius, pos.y - colRadius, pos.x + colRadius, pos.y + colRadius, view.lowX, view.lowY, view.highX, view.highY);
	}

	@Override
	public boolean isOver(double px, double py) {
		return ((pos.x - px)*(pos.x - px) + (pos.y - py)*(pos.y - py)) <= (colRadius * colRadius);
	}

	public String toString(){
		return (name + "  @ " + pos + "  R:" + colRadius);
	}


}
