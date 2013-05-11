package com.software.reuze;
/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 ******************************************************************************/

//package com.badlogic.gdx.math;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.software.reuze.ga_Ray2D;


/**
 * Encapsulates a 2D rectangle defined by its upper-left corner point and its extent in x (width) and y (height).
 * @author badlogicgames@gmail.com
 * 
 */
public class ga_Rectangle implements Serializable {
	private static final long serialVersionUID = 5733252015138115702L;
	public ga_Vector2 position;
	public float width, height;

	/**
	 * Constructs a new rectangle with all values set to zero
	 */
	public ga_Rectangle () {
		position=new ga_Vector2();
	}

	/**
	 * Constructs a new rectangle with the given corner point in the upper left and dimensions.
	 * @param x The corner point x-coordinate
	 * @param y The corner point y-coordinate
	 * @param width The width
	 * @param height The height
	 */
	public ga_Rectangle (float x, float y, float width, float height) {
		position=new ga_Vector2(x,y);
		this.width = width;
		this.height = height;
	}
    public ga_Rectangle(ga_Vector2 position, float width, float height) {
    	this.position = position;
		this.width = width;
		this.height = height;
    }
	/**
	 * Constructs a rectangle based on the given rectangle
	 * @param rect The rectangle
	 */
	public ga_Rectangle (ga_Rectangle rect) {
		position=new ga_Vector2(rect.position);
		width = rect.width;
		height = rect.height;
	}
    /**
     * Constructs a new rectangle defined by either of the two diagonally-opposite points.
     * 
     * @param p1
     * @param p2
     */
    public ga_Rectangle(ga_Vector2 p1, ga_Vector2 p2) {
    	position=new ga_Vector2(p1).min(p2);
        ga_Vector2 br = p1.tmp().max(p2);
        width = br.x - position.x;
        height = br.y - position.y;
    }
    public ga_Vector2 getPosition() {return position;}
	/**
	 * @return the x-coordinate of the top left corner
	 */
	public float getX () {
		return position.x;
	}

	/**
	 * Sets the x-coordinate of the top left corner
	 * @param x The x-coordinate
	 */
	public void setX (float x) {
		this.position.x = x;
	}

	/**
	 * @return the y-coordinate of the top left corner
	 */
	public float getY () {
		return position.y;
	}

	/**
	 * Sets the y-coordinate of the top left corner
	 * @param y The y-coordinate
	 */
	public void setY (float y) {
		this.position.y = y;
	}

	/**
	 * @return the width
	 */
	public float getWidth () {
		return width;
	}

	/**
	 * Sets the width of this rectangle
	 * @param width The width
	 */
	public void setWidth (float width) {
		this.width = width;
	}

	/**
	 * @return the height
	 */
	public float getHeight () {
		return height;
	}

	/**
	 * Sets the height of this rectangle
	 * @param height The height
	 */
	public void setHeight (float height) {
		this.height = height;
	}

	/**
	 * @param rectangle the other {@link ga_Rectangle}.
	 * @return whether the other rectangle is contained in this rectangle.
	 */
	public boolean contains (ga_Rectangle rectangle) {
		float xmin = rectangle.position.x;
		float xmax = xmin + rectangle.width;

		float ymin = rectangle.position.y;
		float ymax = ymin + rectangle.height;

		return ((xmin > position.x && xmin < position.x + width) || (xmax > position.x && xmax < position.x + width))
			&& ((ymin > position.y && ymin < position.y + height) || (ymax > position.y && ymax < position.y + height));
	}
	
	// Code from Seb Lee-Delisle:
	// http://sebleedelisle.com/2009/05/super-fast-trianglerectangle-intersection-test/
	 
	public boolean isIntersecting(float x1, float y1, float x2, float y2) {
	                                  
	  float topIntersection;
	  float bottomIntersection;
	  float topPoint;
	  float bottomPoint;
	 
	  // Calculate m and c for the equation for the line (y = mx+c)
	  float m = (y2-y1) / (x2-x1);
	  float c = y1 -(m*x1);
	 
	  // If the line is going up from right to left then the top intersect point is on the left
	  if(m > 0) {
	    topIntersection = (m*position.x  + c);
	    bottomIntersection = (m*(position.x+width)  + c);
	  }
	  // Otherwise it's on the right
	  else {
	    topIntersection = (m*(position.x+width)  + c);
	    bottomIntersection = (m*position.x  + c);
	  }
	 
	  // Work out the top and bottom extents for the triangle
	  if(y1 < y2) {
	    topPoint = y1;
	    bottomPoint = y2;
	  } else {
	    topPoint = y2;
	    bottomPoint = y1;
	  }
	 
	  float topOverlap;
	  float botOverlap;
	 
	  // Calculate the overlap between those two bounds
	  topOverlap = topIntersection > topPoint ? topIntersection : topPoint;
	  botOverlap = bottomIntersection < bottomPoint ? bottomIntersection : bottomPoint;
	 
	  return (topOverlap<botOverlap) && (!((botOverlap<position.y) || (topOverlap>position.y+height)));	 
	}
	
	/**
	 * @param rectangle the other {@link ga_Rectangle}
	 * @return whether this rectangle overlaps the other rectangle.
	 */
	public boolean isIntersecting(ga_Rectangle rectangle) {
		return !(position.x > rectangle.position.x + rectangle.width || 
					position.x + width < rectangle.position.x || 
					position.y > rectangle.position.y + rectangle.height || 
					position.y + height < rectangle.position.y);
	}
	public boolean isIntersecting(ga_Vector2 center, float radius) {
        float s, d = 0;
        float x2 = position.x + width;
        float y2 = position.y + height;
        if (center.x < position.x) {
            s = center.x - position.x;
            d = s * s;
        } else if (center.x > x2) {
            s = center.x - x2;
            d += s * s;
        }
        if (center.y < position.y) {
            s = center.y - position.y;
            d += s * s;
        } else if (center.y > y2) {
            s = center.y - y2;
            d += s * s;
        }
        return d <= radius * radius;
    }
	public void set (float x, float y, float width, float height) {
		this.position.x = x;
		this.position.y = y;
		this.width = width;
		this.height = height;		
	}

	/**
	 * @param x point x coordinate
	 * @param y point y coordinate
	 * @return whether the point is contained in the rectangle
	 */
	public boolean contains (float x, float y) {
		return this.position.x <= x && this.position.x + this.width >= x && this.position.y <= y && this.position.y + this.height >= y;
	}
	public boolean contains (final ga_Vector2 p) {
		return contains(p.x, p.y);
	}

	/**
	 * Sets the values of the given rectangle to this rectangle.
	 * @param rect the other rectangle
	 */
	public void set (ga_Rectangle rect) {
		this.position.x = rect.position.x;
		this.position.y = rect.position.y;
		this.width = rect.width;
		this.height = rect.height;		
	}
	
	public String toString () {
		return position.x + "," + position.y + "," + width + "," + height;
	}
    /**
     * Updates the bounds of this rectangle by forming an union with the given
     * rect. If the rects are not overlapping, the resulting bounds will be
     * inclusive of both.
     * 
     * @param r
     * @return itself
     */
    public final ga_Rectangle union(ga_Rectangle r) {
        float tmp = m_MathUtils.max(position.x + width, r.position.x + r.width);
        position.x = m_MathUtils.min(position.x, r.position.x);
        width = tmp - position.x;
        tmp = m_MathUtils.max(position.y + height, r.position.y + r.height);
        position.y = m_MathUtils.min(position.y, r.position.y);
        height = tmp - position.y;
        return this;
    }
    public final ga_Rectangle union(ga_Vector2 v) {
        float tmp = m_MathUtils.max(position.x + width, v.x);
        position.x = m_MathUtils.min(position.x, v.x);
        width = tmp - position.x;
        tmp = m_MathUtils.max(position.y + height, v.y);
        position.y = m_MathUtils.min(position.y, v.y);
        height = tmp - position.y;
        return this;
    }
    /**
     * Creates a new rectangle by forming the intersection of this rectangle and
     * the given other rect. The resulting bounds will be the rectangle of the
     * overlay area or null if the rects do not intersect.
     * 
     * @param r
     *            intersection partner rect
     * @return new Rectangle or null
     */
    public final ga_Rectangle intersect(ga_Rectangle r) {
        ga_Rectangle isec = null;
        if (isIntersecting(r)) {
            float x1 = m_MathUtils.max(position.x, r.position.x);
            float y1 = m_MathUtils.max(position.y, r.position.y);
            float x2 = m_MathUtils.min(position.x+width, r.position.x+r.width);
            float y2 = m_MathUtils.min(position.y+height, r.position.y+r.height);
            isec = new ga_Rectangle(x1, y1, x2 - x1, y2 - y1);
        }
        return isec;
    }
    /**
     * Checks if the rectangle perimeter intersects with the given ray and if so computes
     * the first intersection point. The method takes a min/max distance
     * interval along the ray in which the intersection must occur.
     * 
     * @param ray
     *            intersection ray
     * @param minDist
     *            minimum distance
     * @param maxDist
     *            max distance
     * @return intersection point or null if no intersection in the given
     *         interval
     */
    public final ga_Vector2 intersect(ga_Ray2D ray, float minDist, float maxDist) {
    	ga_Vector2 invDir = ray.getDirection().reciprocal();
        boolean signDirX = invDir.x < 0;
        boolean signDirY = invDir.y < 0;
        ga_Vector2 min = position;         //top left
        ga_Vector2 max = position.tmp();
        max.x+=width; max.y+=height;   //bottom right
        ga_Vector2 bbox = signDirX ? max : min;
        float tmin = (bbox.x - ray.origin.x) * invDir.x;
        bbox = signDirX ? min : max;
        float tmax = (bbox.x - ray.origin.x) * invDir.x;
        bbox = signDirY ? max : min;
        float tymin = (bbox.y - ray.origin.y) * invDir.y;
        bbox = signDirY ? min : max;
        float tymax = (bbox.y - ray.origin.y) * invDir.y;
        if ((tmin > tymax) || (tymin > tmax)) {
            return null;
        }
        if (tymin > tmin) {
            tmin = tymin;
        }
        if (tymax < tmax) {
            tmax = tymax;
        }
        if ((tmin < maxDist) && (tmax > minDist)) {
            return ray.getPointAtDistance(tmin);
        }
        return null;
    }
    /**
     * Returns the centroid of the rectangle.
     * 
     * @return centroid vector
     */
    public final ga_Vector2 getCentroid() {
        return new ga_Vector2(position.x + width * 0.5f, position.y + height * 0.5f);
    }

    public final float getPerimeter() {
        return 2 * width + 2 * height;
    }
    public ga_Rectangle mul(float s) {
        ga_Vector2 c = getCentroid();
        width *= s;
        height *= s;
        position.x = c.x - width * 0.5f;
        position.y = c.y - height * 0.5f;
        return this;
    }
    /**
     * Forcefully fits the vector in the given rectangle.
     * 
     * @param r
     * @return itself
     */
    public ga_Vector2 constrain(ga_Vector2 v) {
        v.x = m_MathUtils.clip(v.x, position.x, position.x + width);
        v.y = m_MathUtils.clip(v.y, position.y, position.y + height);
        return v;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ga_Rectangle)) {
            return false;
        }
        ga_Rectangle r=(ga_Rectangle)obj;
    	return r.position.x==position.x && r.position.y==position.y && r.width==width && r.height==height;
    }
    /**
     * Factory method, constructs a new rectangle from a center point and extent
     * vector.
     * 
     * @param center
     * @param extent
     * @return new rect
     */
    public static final ga_Rectangle fromCenterExtent(final ga_Vector2 center, final ga_Vector2 extent) {
        return new ga_Rectangle(center.x-extent.x, center.y-extent.y, extent.x*2, extent.y*2);
    }

    /**
     * Factory method, computes & returns the bounding rect for the given list
     * of points.
     * 
     * @param points
     * @return bounding rect
     * @since 0021
     */
    public static final ga_Rectangle getBoundingRect(List<? extends ga_Vector2> points) {
        final ga_Vector2 first = points.get(0);
        final ga_Rectangle bounds = new ga_Rectangle(first.x, first.y, 0, 0);
        for (int i = 1, num = points.size(); i < num; i++) {
            bounds.growToContainPoint(points.get(i));
        }
        return bounds;
    }
    public final float getArea() {
        return width * height;
    }

    /**
     * Computes the aspect ratio of the rect as width over height.
     * 
     * @return aspect ratio
     */
    public final float getAspect() {
        return width / height;
    }

    public float getBottom() {
        return position.y + height;
    }

    public ga_Vector2 getBottomLeft() {
        return new ga_Vector2(position.x, position.y + height);
    }

    public final ga_Vector2 getBottomRight() {
        return new ga_Vector2(position.x + width, position.y + height);
    }

    public ga_Circle getBoundingCircle() {
        return new ga_Circle(getCentroid(),
                new ga_Vector2(width, height).len() / 2);
    }

    /**
     * Returns a vector containing the width and height of the rectangle.
     * 
     * @return dimension vector
     */
    public final ga_Vector2 getDimensions() {
        return new ga_Vector2(width, height);
    }

    /**
     * Returns one of the rectangles edges as {@link ga_Line2D}. The edge IDs are:
     * <ul>
     * <li>0 - top</li>
     * <li>1 - right</li>
     * <li>2 - bottom</li>
     * <li>3 - left</li>
     * </ul>
     * 
     * @param id
     *            edge ID
     * @return edge as Line2D
     */
    public ga_Line2D getEdge(int id) {
        ga_Line2D edge = null;
        switch (id) {
        // top
            case 0:
                edge = new ga_Line2D(position.x, position.y, position.x + width, position.y);
                break;
            // right
            case 1:
                edge = new ga_Line2D(position.x + width, position.y, position.x + width, position.y + height);
                break;
            // bottom
            case 2:
                edge = new ga_Line2D(position.x, position.y + height, position.x + width, position.y + height);
                break;
            // left
            case 3:
                edge = new ga_Line2D(position.x, position.y, position.x, position.y + height);
                break;
            default:
                throw new IllegalArgumentException("edge ID needs to be 0...3");
        }
        return edge;
    }

    public List<ga_Line2D> getEdges() {
        List<ga_Line2D> edges = new ArrayList<ga_Line2D>();
        for (int i = 0; i < 4; i++) {
            edges.add(getEdge(i));
        }
        return edges;
    }
    
    /**
     * Computes the normalized position of the given point within this
     * rectangle, so that a point at the top-left corner becomes {0,0} and
     * bottom-right {1,1}. The original point is not modified. Together with
     * {@link #getUnmappedPointInRect(Vec2D)} this function can be used to map a
     * point from one rectangle to another.
     * 
     * @param p
     *            point to be mapped
     * @return mapped Vec2D
     */
    public ga_Vector2 getMappedPointInRect(ga_Vector2 p) {
        return new ga_Vector2((p.x - position.x) / width, (p.y - position.y) / height);
    }

    /**
     * Creates a random point within the rectangle.
     * 
     * @return Vec2D
     */
    public ga_Vector2 getRandomPoint() {
        return new ga_Vector2(m_MathUtils.random(position.x, position.x + width), m_MathUtils.random(position.y, position.y
                + height));
    }
    
    /**
     * Inverse operation of {@link #getMappedPointInRect(Vec2D)}. Given a
     * normalized point it computes the position within this rectangle, so that
     * a point at {0,0} becomes the top-left corner and {1,1} bottom-right. The
     * original point is not modified. Together with
     * {@link #getUnmappedPointInRect(Vec2D)} this function can be used to map a
     * point from one rectangle to another.
     * 
     * @param p
     *            point to be mapped
     * @return mapped Vec2D
     */
    public ga_Vector2 getUnmappedPointInRect(ga_Vector2 p) {
        return new ga_Vector2(p.x * width + position.x, p.y * height + position.y);
    }

    public ga_Rectangle growToContainPoint(final ga_Vector2 p) {
        if (!contains(p)) {
            if (p.x < position.x) {
                width = position.x - p.x;
                position.x = p.x;
            } else if (p.x > (position.x+width)) {
                width = p.x - position.x;
            }
            if (p.y < position.y) {
                height = getBottom() - p.y;
                position.y = p.y;
            } else if (p.y > getBottom()) {
                height = p.y - position.y;
            }
        }
        return this;
    }
    
    public ga_Rectangle translate(float dx, float dy) {
        position.x += dx;
        position.y += dy;
        return this;
    }

    public ga_Rectangle translate(final ga_Vector2 offset) {
    	position.x += offset.x;
    	position.y += offset.y;
        return this;
    }
    
    public boolean intersectsCircle(ga_Vector2 c, float r) {
        float s, d = 0;
        float x2 = position.x + width;
        float y2 = position.y + height;
        if (c.x < position.x) {
            s = c.x - position.x;
            d = s * s;
        } else if (c.x > x2) {
            s = c.x - x2;
            d += s * s;
        }
        if (c.y < position.y) {
            s = c.y - position.y;
            d += s * s;
        } else if (c.y > y2) {
            s = c.y - y2;
            d += s * s;
        }
        return d <= r * r;
    }
    public final ga_Rectangle setDimension(ga_Vector2 dim) {
        width = dim.x;
        height = dim.y;
        return this;
    }

    public final ga_Rectangle setPosition(ga_Vector2 pos) {
        position.x = pos.x;
        position.y = pos.y;
        return this;
    }
    public static void main(String args[]) {
    	ga_Vector2 r=new ga_Rectangle(0,20,20,30).intersect(new ga_Ray2D(10,30,new ga_Vector2(-50,-50)),0,1000);
    	System.out.println(r);
    }
}
