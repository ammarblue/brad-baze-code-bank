package com.software.reuze;
/*
 * Copyright (c) 2008, Keith Woodward
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. Neither the name of Keith Woodward nor the names
 *    of its contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 */
//package straightedge.geom;
import java.util.Collection;

import com.software.reuze.ga_i_PolygonHolder;


/**
 * An implementation of an Axis Aligned Bounding Box
 * Point p is the bottom-left point of the box in the normal Cartesian plane
 * with positive Y up and positive X right.
 * Point p2 is the top-right point of the box.
 *
 * @author Keith
 */
public class ga_AABB {
	public ga_Vector2 p;	// bottom left point
	public ga_Vector2 p2;	// top right point

	public static final int UP = 100;
	public static final int DOWN = 101;
	public static final int LEFT = 102;
	public static final int RIGHT = 103;

	public ga_AABB(){
		p = new ga_Vector2();
		p2 = new ga_Vector2();
	}
	public ga_AABB(float x, float y, float x2, float y2){
		p = new ga_Vector2(x, y);
		p2 = new ga_Vector2(x2, y2);
	}
	public ga_AABB(ga_Vector2 p, ga_Vector2 p2, boolean copyPoints){
		if (copyPoints == false){
			this.p = p;
			this.p2 = p2;
		}else{
			this.p = new ga_Vector2(p);
			this.p2 = new ga_Vector2(p2);
		}
	}
	public ga_AABB(ga_Vector2 p, ga_Vector2 p2){
		this(p, p2, true);
	}

	public ga_AABB(ga_AABB aabbToCopy){
		this(aabbToCopy.p, aabbToCopy.p2, true);
	}
	/**
	 * Creates a Rect that will have its x and y coordinates in the bottom left corner
	 * of the cartesian coordinate system. Note that this will be in the
	 * top left corner of the Java2D coordinate system.
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return
	 */
	public static ga_AABB createFromDiagonal(float x1, float y1, float x2, float y2) {
		ga_AABB rect = new ga_AABB();
		rect.setFromDiagonal(x1, y1, x2, y2);
		return rect;
    }
	public static ga_AABB createFromDiagonal(ga_Vector2 p, ga_Vector2 p2) {
		return createFromDiagonal(p.x, p.y, p2.x, p2.y);
    }
	public static ga_AABB createFromXYWH(float x, float y, float w, float h) {
		ga_AABB rect = new ga_AABB();
		rect.setFromXYWH(x, y, w, h);
		return rect;
    }

	public void setFromDiagonal(float x1, float y1, float x2, float y2) {
		if (x2 < x1) {
			float t = x1;
			x1 = x2;
			x2 = t;
		}
		if (y2 < y1) {
			float t = y1;
			y1 = y2;
			y2 = t;
		}
		setX(x1);
		setY(y1);
		setX2(x2);
		setY2(y2);
    }
	public void setFromDiagonal(ga_Vector2 p, ga_Vector2 p2) {
		setFromDiagonal(p.x, p.y, p2.x, p2.y);
    }
	public void setFromXYWH(float x, float y, float w, float h) {
		if (w < 0) {
			x = x+w;
			w = -w;
		}
		if (h < 0) {
			y = y+h;
			h = -h;
		}
		p.x=x;
		p.y=y;
		p2.x=x+w;
		p2.y=y+h;
    }
	public void setFromXYWH(ga_Vector2 p, float w, float h) {
		setFromXYWH(p.x, p.y, w, h);
    }

	public ga_Vector2 getBotLeft(){
		return p;
	}
	public ga_Vector2 getTopRight(){
		return p2;
	}

	public float h() {
		return p2.y - p.y;
	}
	public float w() {
		return p2.x - p.x;
	}
	public float x() {
		return p.x;
	}
	public float y() {
		return p.y;
	}
	public float x2() {
		return p2.x;
	}
	public float y2() {
		return p2.y;
	}

	public float getHeight() {
		return h();
	}
	public float getWidth() {
		return w();
	}

	public void setH(float h) {
		p2.y = p.y + h;
	}
	public void setW(float w) {
		p2.x = p.x + w;
	}

	public float getX() {
		return p.x;
	}

	public void setX(float x) {
		p.x = x;
	}

	public float getY() {
		return p.y;
	}

	public void setY(float y) {
		p.y = y;
	}

	public float getX2() {
		return p2.x;
	}

	public void setX2(float x) {
		p2.x = x;
	}

	public float getY2() {
		return p2.y;
	}

	public void setY2(float y) {
		p2.y = y;
	}

	public ga_AABB copy(){
		return new ga_AABB(this);
	}

	public String toString(){
		return ""+p.x+", "+p.y+",  "+p2.x+", "+p2.y;
	}

	public ga_Vector2 getCenter(){
		return new ga_Vector2(getCenterX(), getCenterY());
	}
	public float getCenterX(){
		return (p.x + p2.x)/2;
	}
	public float getCenterY(){
		return (p.y + p2.y)/2;
	}
	public void setCenter(ga_Vector2 c){
		setCenterX(c.x);
		setCenterY(c.y);
	}
	public void setCenterX(float newCentreX){
		float w = w();
		p.x = newCentreX - w/2;
		p2.x = newCentreX + w/2;
	}
	public void setCenterY(float newCentreY){
		float h = h();
		p.y = newCentreY - h/2;
		p2.y = newCentreY + h/2;
	}

	// this assumes that there is an intersection. b1 is moving in 'direction' and b2 is stationary
	public static float distanceUntilIntersection(ga_AABB b1, int direction, ga_AABB b2){
		if (direction == RIGHT){
			return b2.p.x - (b1.p2.x);
		}else if (direction == LEFT){
			return b1.p.x - (b2.p2.x);
		}else if (direction == DOWN){
			return b2.p.y - (b1.p2.y);
		}else if (direction == UP){
			return b1.p.y - (b2.p2.y);
		}
		return -1f;
	}
	protected static boolean isBetween(float v, float n1, float n2){
		if (v >= n1 && v <= n2){
			return true;
		}
		return false;
	}
	public boolean isFacingBox(int directionThisBoxIsFacing, ga_AABB b2){
		float cx = getCenterX();
		float cy = getCenterY();
		if (directionThisBoxIsFacing == RIGHT){
			if (isBetween(p.y, b2.p.y, b2.p2.y) || isBetween(p2.y, b2.p.y, b2.p2.y) ||
					isBetween(b2.p.y, p.y, p2.y) || isBetween(b2.p2.y, p.y, p2.y)){
				if (cx < b2.getCenterX()){
					return true;
				}
			}
		}else if (directionThisBoxIsFacing == LEFT){
			if (isBetween(p.y, b2.p.y, b2.p2.y) || isBetween(p2.y, b2.p.y, b2.p2.y) ||
					isBetween(b2.p.y, p.y, p2.y) || isBetween(b2.p2.y, p.y, p2.y)){
				if (cx > b2.getCenterX()){
					return true;
				}
			}
		}else if (directionThisBoxIsFacing == DOWN){
			if (isBetween(p.x, b2.p.x, b2.p2.x) || isBetween(p2.x, b2.p.x, b2.p2.x) ||
					isBetween(b2.p.x, p.x, p2.x) || isBetween(b2.p2.x, p.x, p2.x)){
				if (cy < b2.getCenterY()){
					return true;
				}
			}
		}else if (directionThisBoxIsFacing == UP){
			if (isBetween(p.x, b2.p.x, b2.p2.x) || isBetween(p.x, b2.p.x, b2.p2.x) ||
					isBetween(b2.p.x, p.x, p2.x) || isBetween(b2.p2.x, p.x, p2.x)){
				if (cy > b2.getCenterY()){
					return true;
				}
			}
		}
		return false;
	}

	public boolean contains(ga_Vector2 p){
		return contains(p.x, p.y);
	}
	public boolean contains(float px, float py) {
		return (px >= p.x &&
			py >= p.y &&
			px <= p2.x &&
			py <= p2.y);
    }
	public boolean intersects(ga_AABB box) {
		return intersects(box.p.x, box.p.y, box.p2.x, box.p2.y);
	}
	/*TODO check public boolean intersects(AABB box) {
        Vec3D t = box.sub(this);
        return MathUtils.abs(t.x) <= (extent.x + box.extent.x)
                && MathUtils.abs(t.y) <= (extent.y + box.extent.y)
                && MathUtils.abs(t.z) <= (extent.z + box.extent.z);
    }*/

    public boolean intersects(float x, float y, float x2, float y2) {
		return intersects(p.x, p.y, p2.x, p2.y, x, y, x2, y2);
    }

	public static boolean intersects(float leftX, float botY, float rightX, float topY,
			float leftX2, float botY2, float rightX2, float topY2) {
		return (rightX >= leftX2 &&
			topY >= botY2 &&
			leftX <= rightX2 &&
			botY <= topY2);
    }
	public boolean contains(float x, float y, float x2, float y2) {
		return (p.x <= x &&
				p.y <= y &&
				p2.x >= x2 &&
				p2.y >= y2);
	}
	
	public static void union(ga_AABB src1, ga_AABB src2, ga_AABB dest) {
		float x1 = Math.min(src1.getX(), src2.getX());
		float y1 = Math.min(src1.getY(), src2.getY());
		float x2 = Math.max(src1.getX2(), src2.getX2());
		float y2 = Math.max(src1.getY2(), src2.getY2());
		dest.setFromDiagonal(x1, y1, x2, y2);
    }
	public ga_AABB union(ga_AABB src1) {
		ga_AABB aabb = new ga_AABB();
		union(this, src1, aabb);
		return aabb;
    }
	public static ga_AABB union(ga_AABB src1, ga_AABB src2) {
		ga_AABB aabb = new ga_AABB();
		union(src1, src2, aabb);
		return aabb;
    }

	public boolean isValid(){
		if (p2.x < p.x) {
			return false;
		}
		if (p2.y < p.y) {
			return false;
		}
		return true;
	}

	/*public KPolygon createPolygon(){
		return KPolygon.createRect(this);
	}
	public AABB createFromPolygon(KPolygon polygon){
		return polygon.getAABB();
	}
	public Rectangle2D.Float getRectangle2D(){
		Rectangle2D.Float rect = new Rectangle2D.Float(p.x, p.y, w(), h());
		return rect;
	}*/

	public static ga_AABB bufferAndCopy(ga_AABB aabb, float bufferAmount){
		ga_AABB newAABB = new ga_AABB(aabb.p.x + bufferAmount, aabb.p.y + bufferAmount, aabb.p2.x + bufferAmount, aabb.p2.y + bufferAmount);
		return newAABB;
	}
	public ga_AABB bufferAndCopy(float bufferAmount){
		return bufferAndCopy(this, bufferAmount);
	}

	public static ga_AABB getAABBEnclosingExactPoints(Collection<ga_i_PolygonHolder> polygonHolders){
		return getAABBEnclosingExactPoints(polygonHolders.toArray());
	}
	public static ga_AABB getAABBEnclosingExactPoints(Object[] polygonHolders){
		if (polygonHolders.length == 0){
			ga_Vector2 botLeft = new ga_Vector2(0, 0);
			ga_Vector2 topRight = new ga_Vector2(1, 1);
			ga_AABB aabb = new ga_AABB(botLeft, topRight);
			return aabb;
		}
		// find the bounding rectangle
		float minX = Float.MAX_VALUE;
		float maxX = -Float.MAX_VALUE;
		float minY = Float.MAX_VALUE;
		float maxY = -Float.MAX_VALUE;
		float[] bounds = new float[4];
		for (int i = 0; i < polygonHolders.length; i++){
			ga_i_PolygonHolder polygonHolder = (ga_i_PolygonHolder)polygonHolders[i];
			ga_Polygon polygon = polygonHolder.getPolygon();
			polygon.getBoundsArray(bounds);
			float leftX = bounds[0];
			float rightX = bounds[2];
			float botY = bounds[1];
			float topY = bounds[3];
			if (leftX < minX){
				minX = leftX;
			}
			if (rightX > maxX){
				maxX = rightX;
			}
			if (botY < minY){
				minY = botY;
			}
			if (topY > maxY){
				maxY = topY;
			}
		}
		ga_Vector2 botLeft = new ga_Vector2(minX, minY);
		ga_Vector2 topRight = new ga_Vector2(maxX, maxY);
		ga_AABB aabb = new ga_AABB(botLeft, topRight);
		return aabb;
	}

	public static ga_AABB getAABBEnclosingCenterAndRadius(Collection<ga_i_PolygonHolder> polygonHolders){
		return getAABBEnclosingCenterAndRadius(polygonHolders.toArray());
	}
	public static ga_AABB getAABBEnclosingCenterAndRadius(Object[] polygonHolders){
		if (polygonHolders.length == 0){
			ga_Vector2 botLeft = new ga_Vector2(0, 0);
			ga_Vector2 topRight = new ga_Vector2(1, 1);
			ga_AABB aabb = new ga_AABB(botLeft, topRight);
			return aabb;
		}
		// find the bounding rectangle
		float minX = Float.MAX_VALUE;
		float maxX = -Float.MAX_VALUE;
		float minY = Float.MAX_VALUE;
		float maxY = -Float.MAX_VALUE;
		for (int i = 0; i < polygonHolders.length; i++){
			ga_i_PolygonHolder polygonHolder = (ga_i_PolygonHolder)polygonHolders[i];
			ga_Vector2 c = polygonHolder.getPolygon().getCenter();
			float r = polygonHolder.getPolygon().getRadius();
			float leftX = c.x - r;
			float rightX = c.x + r;
			float botY = c.y - r;
			float topY = c.y + r;
			if (leftX < minX){
				minX = leftX;
			}
			if (rightX > maxX){
				maxX = rightX;
			}
			if (botY < minY){
				minY = botY;
			}
			if (topY > maxY){
				maxY = topY;
			}
		}
		ga_Vector2 botLeft = new ga_Vector2(minX, minY);
		ga_Vector2 topRight = new ga_Vector2(maxX, maxY);
		ga_AABB aabb = new ga_AABB(botLeft, topRight);
		return aabb;
	}

}
