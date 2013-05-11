package com.software.reuze;

//package org.newdawn.slick.geom;

/**
 * A simple Circle geometry
 * 
 * @author Kevin Glass
 */
public strictfp class ga_ShapeCircle extends ga_ShapeEllipse {
	/** The radius of the circle */
	public float radius;
	
	/**
	 * Create a new circle based on its radius
	 * 
	 * @param centerPointX The x location of the center of the circle
	 * @param centerPointY The y location of the center of the circle
	 * @param radius The radius of the circle
	 */
	public ga_ShapeCircle(float centerPointX, float centerPointY, float radius) {
        this(centerPointX, centerPointY, radius, DEFAULT_SEGMENT_COUNT);
	}

	/**
	 * Create a new circle based on its radius
	 * 
	 * @param centerPointX The x location of the center of the circle
	 * @param centerPointY The y location of the center of the circle
	 * @param radius The radius of the circle
	 * @param segmentCount The number of segments to build the circle out of
	 */
	public ga_ShapeCircle(float centerPointX, float centerPointY, float radius, int segmentCount) {
        super(centerPointX, centerPointY, radius, radius, segmentCount);
        this.x = centerPointX - radius;
        this.y = centerPointY - radius;
        this.radius = radius;
        boundingCircleRadius = radius;
	}
	
	/** 
	 * Get the x coordinate of the centre of the circle
	 * 
	 * @return The x coordinate of the centre of the circle
	 */
	public float getCenterX() {
		return getX() + radius;
	}
	
	/** 
	 * Get the y coordinate of the centre of the circle
	 * 
	 * @return The y coordinate of the centre of the circle
	 */
	public float getCenterY() {
		return getY() + radius;
	}
	
	/**
	 * Set the radius of this circle
	 * 
	 * @param radius The radius of this circle
	 */
	public void setRadius(float radius) {
		if (radius != this.radius) {
	        pointsDirty = true;
			this.radius = radius;
	        setRadii(radius, radius);
		}
	}
	
	/**
	 * Get the radius of the circle
	 * 
	 * @return The radius of the circle
	 */
	public float getRadius() {
		return radius;
	}
	
	/**
	 * Check if this circle touches another
	 * 
	 * @param shape The other circle
	 * @return True if they touch
	 */
	public boolean intersects(ga_a_Shape shape) {
        if(shape instanceof ga_ShapeCircle) {
            ga_ShapeCircle other = (ga_ShapeCircle)shape;
    		float totalRad2 = getRadius() + other.getRadius();
    		
    		if (Math.abs(other.getCenterX() - getCenterX()) > totalRad2) {
    			return false;
    		}
    		if (Math.abs(other.getCenterY() - getCenterY()) > totalRad2) {
    			return false;
    		}
    		
    		totalRad2 *= totalRad2;
    		
    		float dx = Math.abs(other.getCenterX() - getCenterX());
    		float dy = Math.abs(other.getCenterY() - getCenterY());
    		
    		return totalRad2 >= ((dx*dx) + (dy*dy));
        }
        else if(shape instanceof ga_ShapeRectangle) {
            return intersects((ga_ShapeRectangle)shape);
        }
        else {
            return super.intersects(shape);
        }
	}
	
	/**
	 * Check if a point is contained by this circle
	 * 
	 * @param x The x coordinate of the point to check
	 * @param y The y coordinate of the point to check
	 * @return True if the point is contained by this circle
	 */
    public boolean contains(float x, float y) 
    { 
        return (x - getX()) * (x - getX()) + (y - getY()) * (y - getY()) < getRadius() * getRadius(); 
    }
    
    /**
     * Check if circle contains the line 
     * @param line ga_ShapeLine to check against 
     * @return True if line inside circle 
     */ 
    private boolean contains(ga_ShapeLine line) { 
         return contains(line.getX1(), line.getY1()) && contains(line.getX2(), line.getY2()); 
    }
    
	/**
	 * @see org.newdawn.slick.geom.Ellipse#findCenter()
	 */
    protected void findCenter() {
        center = new float[2];
        center[0] = x + radius;
        center[1] = y + radius;
    }

    /**
     * @see org.newdawn.slick.geom.Ellipse#calculateRadius()
     */
    protected void calculateRadius() {
        boundingCircleRadius = radius;
    }

    /**
	 * Check if this circle touches a rectangle
	 * 
	 * @param other The rectangle to check against
	 * @return True if they touch
	 */
	private boolean intersects(ga_ShapeRectangle other) {
		ga_ShapeRectangle box = other;
		ga_ShapeCircle circle = this;
		
		if (box.contains(x+radius,y+radius)) {
			return true;
		}
		
		float x1 = box.getX();
		float y1 = box.getY();
		float x2 = box.getX() + box.getWidth();
		float y2 = box.getY() + box.getHeight();
		
		ga_ShapeLine[] lines = new ga_ShapeLine[4];
		lines[0] = new ga_ShapeLine(x1,y1,x2,y1);
		lines[1] = new ga_ShapeLine(x2,y1,x2,y2);
		lines[2] = new ga_ShapeLine(x2,y2,x1,y2);
		lines[3] = new ga_ShapeLine(x1,y2,x1,y1);
		
		float r2 = circle.getRadius() * circle.getRadius();
		
		ga_Vector2 pos = new ga_Vector2(circle.getCenterX(), circle.getCenterY());
		
		for (int i=0;i<4;i++) {
			float dis = lines[i].distanceSquared(pos);
			if (dis < r2) {
				return true;
			}
		}
		
		return false;
	}
	
	/** 
     * Check if circle touches a line. 
     * @param other The line to check against 
     * @return True if they touch 
     */ 
    private boolean intersects(ga_ShapeLine other) { 
        // put it nicely into vectors 
        ga_Vector2 lineSegmentStart = new ga_Vector2(other.getX1(), other.getY1()); 
        ga_Vector2 lineSegmentEnd = new ga_Vector2(other.getX2(), other.getY2()); 
        ga_Vector2 circleCenter = new ga_Vector2(getCenterX(), getCenterY()); 

        // calculate point on line closest to the circle center and then 
        // compare radius to distance to the point for intersection result 
        ga_Vector2 closest; 
        ga_Vector2 segv = lineSegmentEnd.copy().sub(lineSegmentStart); 
        ga_Vector2 ptv = circleCenter.copy().sub(lineSegmentStart); 
        float segvLength = segv.len(); 
        float projvl = ptv.dot(segv) / segvLength; 
        if (projvl < 0) 
        { 
            closest = lineSegmentStart; 
        } 
        else if (projvl > segvLength) 
        { 
            closest = lineSegmentEnd; 
        } 
        else 
        { 
            ga_Vector2 projv = segv.tmp().mul(projvl / segvLength); 
            closest = lineSegmentStart.copy().add(projv); 
        } 
        boolean intersects = circleCenter.copy().sub(closest).len2() <= getRadius()*getRadius(); 
        
        return intersects; 
    } 
}