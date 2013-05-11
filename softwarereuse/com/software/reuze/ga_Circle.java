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

/**
 * A convenient 2D circle class.
 * @author mzechner
 *
 */
public class ga_Circle implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1635526673379056264L;
	public static final int DEFAULT_RES = 20;
	public ga_Vector2 position;
	public float radius;
	
	public ga_Circle(float x, float y, float radius) {
		position=new ga_Vector2(x,y);
		this.radius = radius;
	}
	public ga_Circle(float radius) {
		position=new ga_Vector2();
		this.radius = radius;
	}
	
	public ga_Circle(ga_Vector2 position, float radius) {
		this.position=position;
		this.radius = radius;
	}
	public void set(float x, float y) {
		position.set(x,y);
	}
	public boolean contains(float x, float y) {
		x = this.position.x - x;
		y = this.position.y - y;
		return x*x + y*y <= radius * radius;
	}
	
	public boolean contains(ga_Vector2 point) {
		float x = this.position.x - point.x;
		float y = this.position.y - point.y;
		return x*x + y*y <= radius * radius;
	}
	public static boolean contains(ga_Vector2 center, float radius, ga_Vector2 point) {
		float x = center.x - point.x;
		float y = center.y - point.y;
		return x*x + y*y <= radius * radius;
	}
	public float area() {return m_MathUtils.PI*radius*radius;}
	public ga_Vector2 getPosition() {return position;}
	public float getRadius() {return radius;}
    public ga_Polygon toPolygon2D() {
        return toPolygon(DEFAULT_RES);
    }
    public ga_Vector2[] intersects(ga_Circle c) {
    	ga_Vector2[] res = null;
    	ga_Vector2 delta = c.position.tmp().sub(this.position);
        float d = delta.len();
        float r1 = radius;
        float r2 = c.radius;
        if (d <= r1 + r2 && d >= Math.abs(r1 - r2)) {
            float a = (r1 * r1 - r2 * r2 + d * d) / (2.0f * d);
            d = 1 / d;
            ga_Vector2 p = position.copy().add(delta.tmp2().mul(a * d));
            float h = (float) Math.sqrt(r1 * r1 - a * a);
            delta.perpendicular().mul(h * d);
            ga_Vector2 i1 = p.copy().add(delta);
            ga_Vector2 i2 = p.copy().sub(delta);
            res = new ga_Vector2[] {
                    i1, i2
            };
        }
        return res;
    }
    /**
     * Creates a {@link Polygon2D} instance of the ellipse sampling it at the
     * given resolution.
     * 
     * @param res
     *            number of steps
     * @return ellipse as polygon
     */
    public ga_Polygon toPolygon(int res) {
        ga_Polygon poly = new ga_Polygon();
        float step = m_MathUtils.TWO_PI / res;
        for (int i = 0; i < res; i++) {
            poly.add(ga_Vector2.fromTheta(i * step).mul(radius).add(position));
        }
        poly.calcCenter();
        return poly;
    }
    /**
     * Creates a random point within the ellipse using a
     * {@link m_RangeFloatBiased} to create a more uniform distribution.
     * 
     * @return Vec2D
     */
    public ga_Vector2 getRandomPoint() {
        float theta = m_MathUtils.random(m_MathUtils.TWO_PI);
        m_RangeFloatBiased rnd = new m_RangeFloatBiased(0f, 1f, 1f, m_MathUtils.SQRT2);
        return ga_Vector2.fromTheta(theta).mul(radius*rnd.pickRandom())
                .add(position);
    }
    public String toString() {
    	return position+","+radius;
    }
	public ga_Circle setRadius(float r) {
		radius=r;
		return this;
	}
}
