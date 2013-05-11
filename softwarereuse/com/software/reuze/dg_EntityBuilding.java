package com.software.reuze;
import com.software.reuze.ga_Geometry2D;

public class dg_EntityBuilding extends dg_a_EntityBase {

	private ga_Vector2D[] contour;
	private Integer[] triangle;
	private double minX, maxX, minY, maxY;
	
	public dg_EntityBuilding() {
		super();
	}
	public dg_EntityBuilding(ga_Vector2D offset, ga_Vector2D[] contour){
		this("", offset, contour);
	}
	/**
	* Create a building with the given contour.
	*
	* @param name
	* @param offset
	* @param contour open list (shape is automatically closed) of corners in counter-clockwise order
	*/
	public dg_EntityBuilding(String name, ga_Vector2D offset, ga_Vector2D[] contour){
		super(name);
		setContour(contour);
		pos.set(offset);
	}
    public void setContour(ga_Vector2D[] contour) {
    	this.contour = new ga_Vector2D[contour.length];
		minX = minY = Double.MAX_VALUE;
		maxX = maxY = -Double.MAX_VALUE;
		for(int i = 0; i < contour.length; i++){
			this.contour[i] = new ga_Vector2D(contour[i]);
			minX = m_MathFast.min(minX, this.contour[i].x);
			maxX = m_MathFast.max(maxX, this.contour[i].x);
			minY = m_MathFast.min(minY, this.contour[i].y);
			maxY = m_MathFast.max(maxY, this.contour[i].y);
		}
		this.triangle = ga_Triangulator.triangulate(this.contour);
    }
	public dg_EntityDomainRectangular getExtent(){
		return new dg_EntityDomainRectangular(minX, minY, maxX, maxY);
	}

	@Override
	public boolean isOver(double px, double py) {
		boolean result = false;	
		px -= pos.x;
		py -= pos.y;
		if(ga_Geometry2D.isInsideRectangle_xyxy(minX, minY, maxX, maxY, px, py)){
			for(int i = 0; i < triangle.length; i += 3){
				result = ga_Geometry2D.isInsideTriangle(contour[triangle[i]], contour[triangle[i+1]], contour[triangle[i+2]], px, py);
				if(result) break;
			}
		}
		return result;
	}

	/**
	 * Determines whether two points are on either side of a building. If they are then they cannot 'see' each other.
	 * 
	 * @param x0 
	 * @param y0
	 * @param x1
	 * @param y1
	 * @return true if the building is position between the 2 points 
	 */
	public boolean isEitherSide(double x0, double y0, double x1, double y1){
		// Check the bounding box of the building first
		if(ga_Geometry2D.line_box_xyxy(x0, y0, x1, y1, minX, minY, maxX, maxY)){
			// OK so now check against the walls of the building
			for(int i = 1; i < contour.length; i++){
				if(ga_Geometry2D.line_line(x0, y0, x1, y1, contour[i-1].x, contour[i-1].y, contour[i].x, contour[i].y))
					return true;
			}
			if(ga_Geometry2D.line_line(x0, y0, x1, y1, contour[contour.length-1].x, contour[contour.length-1].y, contour[0].x, contour[0].y))
				return true;
		}
		return false;		
	}
		
	/**
	 * @return the contour
	 */
	public ga_Vector2D[] getContour() {
		return contour;
	}

	/**
	 * @return the triangle
	 */
	public Integer[] getTriangle() {
		return triangle;
	}

	@Override
	public boolean isInDomain(dg_EntityDomainRectangular view) {
		return ga_Geometry2D.box_box(minX, minY, maxX, maxY, view.lowX, view.lowY, view.highX, view.highY);
	}

	public String toString(){
		StringBuilder s = new StringBuilder(name + "  @ " + pos + "\n  ");
		for(ga_Vector2D p : contour){
			s.append(p + " ");
		}
		return new String(s);
	}

}
