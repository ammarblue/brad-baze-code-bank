package com.software.reuze;
/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
//package com.badlogic.gdx.math.collision;


import java.io.Serializable;

import com.software.reuze.m_Matrix4;


//import toxi.geom.Vec3D;

//import com.badlogic.gdx.math.Matrix4;
//import com.badlogic.gdx.math.Vector3;

/**
 * Encapsulates a ray having a starting position and a unit length direction.
 * 
 * @author badlogicgames@gmail.com
 * 
 */
public class gb_Ray implements Serializable {	
	private static final long serialVersionUID = -620692054835390878L;
	public final gb_Vector3 origin = new gb_Vector3();
	public final gb_Vector3 direction = new gb_Vector3();

	/**
	 * Constructor, sets the starting position of the ray and the direction.
	 * 
	 * @param origin The starting position
	 * @param direction The direction
	 */
	public gb_Ray (gb_Vector3 origin, gb_Vector3 direction) {
		this.origin.set(origin);
		this.direction.set(direction).nor();
	}

	/**
	 * @return a copy of this ray.
	 */
	public gb_Ray cpy () {
		return new gb_Ray(this.origin, this.direction);
	}

	/**
	 * Returns and end-point given the distance. This is calculated as start-point + distance * direction.
	 * 
	 * @param distance The distance from the end point to the start point.
	 * @return The end point
	 */
	public gb_Vector3 getEndPoint (float distance) {
		return new gb_Vector3(origin).add(direction.tmp().mul(distance));
	}

	static gb_Vector3 tmp = new gb_Vector3();

	/**
	 * Multiplies the ray by the given matrix. Use this to transform a ray into another coordinate system.
	 * 
	 * @param matrix The matrix
	 * @return This ray for chaining.
	 */
	public gb_Ray mul (m_Matrix4 matrix) {
		tmp.set(origin).add(direction);
		tmp.mul(matrix);
		origin.mul(matrix);
		direction.set(tmp.sub(origin));
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	public String toString () {
		return "ray [" + origin + ":" + direction + "]";
	}

	/**
	 * Sets the starting position and the direction of this ray.
	 * 
	 * @param origin The starting position
	 * @param direction The direction
	 * @return this ray for chaining
	 */
	public gb_Ray set (gb_Vector3 origin, gb_Vector3 direction) {
		this.origin.set(origin);
		this.direction.set(direction);
		return this;
	}

	/**
	 * Sets this ray from the given starting position and direction.
	 * 
	 * @param x The x-component of the starting position
	 * @param y The y-component of the starting position
	 * @param z The z-component of the starting position
	 * @param dx The x-component of the direction
	 * @param dy The y-component of the direction
	 * @param dz The z-component of the direction
	 * @return this ray for chaining
	 */
	public gb_Ray set (float x, float y, float z, float dx, float dy, float dz) {
		this.origin.set(x, y, z);
		this.direction.set(dx, dy, dz);
		return this;
	}

	/**
	 * Sets the starting position and direction from the given ray
	 * 
	 * @param ray The ray
	 * @return This ray for chaining
	 */
	public gb_Ray set (gb_Ray ray) {

		this.origin.set(ray.origin);
		this.direction.set(ray.direction);
		return this;
	}
    /**
     * Calculates the distance between the given point and the infinite line
     * coinciding with this ray.
     * 
     * @param p
     * @return distance
     */
    public float getDistanceToPoint(gb_Vector3 p) {
    	gb_Vector3 sp = p.tmp().sub(this.origin);
        return sp.dst(direction.tmp2().mul(sp.tmp3().dot(direction)));
    }

    /**
     * Returns the point at the given distance on the ray. The distance can be
     * any real number.
     * 
     * @param dist
     * @return vector
     */
    public gb_Vector3 getPointAtDistance(float dist) {
        return origin.cpy().add(direction.tmp().mul(dist));
    }
    public gb_Vector3 getDirection() {return direction;}
}
