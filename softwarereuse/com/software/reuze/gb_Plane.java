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
//package com.badlogic.gdx.math;


import java.io.Serializable;

import com.software.reuze.gb_Triangle;
import com.software.reuze.gb_TriangleMesh;
import com.software.reuze.gb_i_Mesh;


/**
 * A plane defined via a unit length normal and the distance from the origin, as you learned in your math class.
 * 
 * @author badlogicgames@gmail.com
 * 
 */
public class gb_Plane implements Serializable {	
	private static final long serialVersionUID = -1240652082930747866L;

	/**
	 * Enum specifying on which side a point lies respective to the plane and it's normal. {@link PlaneSide#Front} is the side to
	 * which the normal points.
	 * 
	 * @author mzechner
	 * 
	 */
	public enum PlaneSide {
		OnPlane, Back, Front
	}

	public gb_Vector3 position, normal= gb_Vector3.Y.cpy();
	public float d = 0;
    public static final gb_Plane XY = new gb_Plane(gb_Vector3.Z, new gb_Vector3());
    public static final gb_Plane XZ = new gb_Plane(gb_Vector3.Y, new gb_Vector3());
    public static final gb_Plane YZ = new gb_Plane(gb_Vector3.X, new gb_Vector3());

    public gb_Plane() {
    	position=new gb_Vector3();
    }

    public gb_Plane(gb_Triangle t) {
        this(t.computeCentroid(), t.computeNormal());
    }

	/**
	 * Constructs a new plane based on the normal and distance to the origin.
	 * 
	 * @param normal The plane normal
	 * @param d The distance to the origin
	 */
	public gb_Plane (gb_Vector3 normal, float d) {
		position=new gb_Vector3();
		this.normal.set(normal).nor();
		this.d = d;
	}

	/**
	 * Constructs a new plane based on the normal and a point on the plane.
	 * 
	 * @param normal The normal
	 * @param point The point on the plane
	 */
	public gb_Plane (gb_Vector3 point, gb_Vector3 normal) {
		position=point;
		this.normal.set(normal).nor();
		this.d = -this.normal.dot(point);
	}

	/**
	 * Constructs a new plane out of the three given points that are considered to be on the plane. The normal is calculated via a
	 * cross product between (point1-point2)x(point2-point3)
	 * 
	 * @param point1 The first point
	 * @param point2 The second point
	 * @param point3 The third point
	 */
	public gb_Plane (gb_Vector3 point1, gb_Vector3 point2, gb_Vector3 point3) {
		set(point1, point2, point3);
	}

	/**
	 * Sets the plane normal and distance to the origin based on the three given points which are considered to be on the plane.
	 * The normal is calculated via a cross product between (point1-point2)x(point2-point3)
	 * 
	 * @param point1
	 * @param point2
	 * @param point3
	 */
	public void set (gb_Vector3 point1, gb_Vector3 point2, gb_Vector3 point3) {
		gb_Vector3 l = point1.tmp().sub(point2);
		gb_Vector3 r = point2.tmp2().sub(point3);
		gb_Vector3 nor = l.crs(r).nor();
		normal.set(nor);
		d = -point1.dot(nor);
	}

	/**
	 * Sets the plane normal and distance
	 * 
	 * @param nx normal x-component
	 * @param ny normal y-component
	 * @param nz normal z-component
	 * @param d distance to origin
	 */
	public void set (float nx, float ny, float nz, float d) {
		normal.set(nx, ny, nz);
		this.d = d;
	}

	/**
	 * Calculates the shortest signed distance between the plane and the given point.
	 * 
	 * @param point The point
	 * @return the shortest signed distance between the plane and the point
	 */
	public float distance (gb_Vector3 point) {
		return normal.dot(point) + d;
	}

	/**
	 * Returns on which side the given point lies relative to the plane and its normal. PlaneSide.Front refers to the side the
	 * plane normal points to.
	 * 
	 * @param point The point
	 * @return The side the point lies relative to the plane
	 */
	public PlaneSide testPoint (gb_Vector3 point) {
		float dist = normal.dot(point) + d;

		if (dist == 0)
			return PlaneSide.OnPlane;
		else if (dist < 0)
			return PlaneSide.Back;
		else
			return PlaneSide.Front;
	}
    /**
     * Classifies the relative position of the given point to the plane using
     * the given tolerance.
     * 
     * @return One of the 3 classification types: FRONT, BACK, ON_PLANE
     */
    public PlaneSide testPoint(gb_Vector3 p, float tolerance) {
        float d = position.tmp().sub(p).nor().dot(normal);
        if (d < -tolerance) {
            return PlaneSide.Front;
        } else if (d > tolerance) {
            return PlaneSide.Back;
        }
        return PlaneSide.OnPlane;
    }

	/**
	 * Returns whether the plane is facing the direction vector. Think of the direction vector as the direction a camera looks in.
	 * This method will return true if the front side of the plane determined by its normal faces the camera.
	 * 
	 * @param direction the direction
	 * @return whether the plane is front facing
	 */
	public boolean isFrontFacing (gb_Vector3 direction) {
		float dot = normal.dot(direction);
		return dot <= 0;
	}

	/**
	 * @return The normal
	 */
	public gb_Vector3 getNormal () {
		return normal;
	}

	/**
	 * @return The distance to the origin
	 */
	public float getD () {
		return d;
	}

	/**
	 * Sets the plane to the given point and normal.
	 * 
	 * @param point the point on the plane
	 * @param normal the normal of the plane
	 */
	public void set (gb_Vector3 point, gb_Vector3 normal) {
		this.normal.set(normal);
		d = -point.dot(normal);
	}

	/**
	 * Sets this plane from the given plane
	 * 
	 * @param plane the plane
	 */
	public void set (gb_Plane plane) {
		this.normal.set(plane.normal);
		this.d = plane.d;
	}

	public String toString () {
		return position.toString() + ", "+normal.toString() + ", " + d;
	}
    /**
     * Creates a TriangleMesh representation of the plane as a finite, squared
     * quad of the requested size, centered around the current plane point.
     * 
     * @param size
     *            desired edge length
     * @return mesh
     */
    public gb_i_Mesh toMesh(float size) {
        return toMesh(null, size);
    }

    public gb_i_Mesh toMesh(gb_i_Mesh mesh, float size) {
        if (mesh == null) {
            mesh = new gb_TriangleMesh("plane", 4, 2);
        }
        gb_Vector3 p =
                position.equalsWithTolerance(gb_Vector3.ZERO, 0.01f) ? position.tmp().add(0.01f, 0.01f,
                        0.01f) : position.tmp();
        size *= 0.5f;
        gb_Vector3 n = p.crs(normal).norTo(size);
        gb_Vector3 m = n.tmp2().crs(normal).norTo(size);
        gb_Vector3 a = position.cpy().add(n).add(m);
        gb_Vector3 b = position.cpy().add(n).sub(m);
        gb_Vector3 c = position.cpy().sub(n).sub(m);
        gb_Vector3 d = position.cpy().sub(n).add(m);
        mesh.addFace(a, d, b, null, null, null, null);
        mesh.addFace(b, d, c, null, null, null, null);
        return mesh;
    }
    
    public gb_Vector3 getProjectedPoint(final gb_Vector3 p) {
    	gb_Vector3 dir;
        if (normal.dot(position.tmp().sub(p)) < 0) {
            dir = normal.tmp().inv();
        } else {
            dir = normal;
        }
        gb_Vector3 proj = new gb_Ray(p, dir)
                .getPointAtDistance(this.distance(p));
        return proj;
    }
}
