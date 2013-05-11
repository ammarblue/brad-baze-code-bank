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

import com.software.reuze.gb_Ray;
import com.software.reuze.gb_SurfaceMeshBuilder;
import com.software.reuze.gb_Triangle;
import com.software.reuze.gb_i_Mesh;

//import com.badlogic.gdx.math.Vector3;
/**
 * Encapsulates a 3D sphere with a center and a radius
 * 
 * @author badlogicgames@gmail.com
 * 
 */
public class gb_Sphere implements Serializable {	
	private static final long serialVersionUID = -6487336868908521596L;
	/** the radius of the sphere **/
	public float radius;
	/** the center of the sphere **/
	public gb_Vector3 center;

	/**
	 * Constructs a sphere with the given center and radius
	 * @param center The center
	 * @param radius The radius
	 */
	public gb_Sphere (gb_Vector3 center, float radius) {
		this.center = new gb_Vector3(center);
		this.radius = radius;
	}
	public gb_Sphere (float radius) {
		this.center = new gb_Vector3();
		this.radius = radius;
	}
	
	/**
	 * @param sphere the other sphere
	 * @return whether this and the other sphere intersect
	 */
	public boolean intersects(gb_Sphere sphere) {
		return center.dst2(sphere.center) < (radius + sphere.radius) * (radius + sphere.radius);
	}
    public boolean contains(final gb_Vector3 p) {
        float d = center.tmp().sub(p).len2();
        return (d <= radius * radius);
    }
    /**
     * Considers the current vector as center of a collision sphere with radius
     * r and checks if the triangle abc intersects with this sphere. The Vec3D p
     * The point on abc closest to the sphere center is returned via the
     * supplied result vector argument.
     * 
     * @param t
     *            triangle to check for intersection
     * @param result
     *            a non-null vector for storing the result
     * @return true, if sphere intersects triangle ABC
     */
    public boolean intersectSphereTriangle(gb_Triangle t, gb_Vector3 result) {
        // Find Vec3D P on triangle ABC closest to sphere center
        result.set(t.closestPointOnSurface(center));

        // Sphere and triangle intersect if the (squared) distance from sphere
        // center to Vec3D p is less than the (squared) sphere radius
        gb_Vector3 v = result.tmp().sub(center);
        return v.len2() <= radius * radius;
    }
    
    public boolean intersects(gb_Vector3 p1, gb_Vector3 p2)
    {//circle to line segment
        float top = (center.x - p1.x)*(p2.x - p1.x)  + 
                    (center.y - p1.y)*(p2.y - p1.y)  +
                    (center.z - p1.z)*(p2.z - p1.z);
        float bottom = (p2.x - p1.x)*(p2.x - p1.x)  + 
                       (p2.y - p1.y)*(p2.y - p1.y)  +
                       (p2.z - p1.z)*(p2.z - p1.z);
        float result = top/bottom;
        if(Math.abs(result) >= 0 && Math.abs(result) <= 1.0f )
        {
        	gb_Vector3 point=p2.tmp().sub(p1).mul(result).add(p1);
            if(point.dst2(center) <= radius*radius)
                return true;
        }
        return false;
    }
    
    /**
     * Alternative to {@link gb_SphereIntersectorReflector}. Computes primary &
     * secondary intersection points of this sphere with the given ray. If no
     * intersection is found the method returns null. In all other cases, the
     * returned array will contain the distance to the primary intersection
     * point (i.e. the closest in the direction of the ray) as its first index
     * and the other one as its second. If any of distance values is negative,
     * the intersection point lies in the opposite ray direction (might be
     * useful to know). To get the actual intersection point coordinates, simply
     * pass the returned values to {@link Ray3D#getPointAtDistance(float)}.
     * 
     * @param ray
     * @return 2-element float array of intersection points or null if ray
     *         doesn't intersect sphere at all.
     */
    public float[] intersectRay(gb_Ray ray) {
        float[] result = null;
        gb_Vector3 q = ray.origin.tmp().sub(this.center);
        float distSquared = q.len2();
        float v = -q.dot(ray.getDirection());
        float d = radius * radius - (distSquared - v * v);
        if (d >= 0.0) {
            d = (float) Math.sqrt(d);
            float a = v + d;
            float b = v - d;
            if (!(a < 0 && b < 0)) {
                if (a > 0 && b > 0) {
                    if (a > b) {
                        float t = a;
                        a = b;
                        b = t;
                    }
                } else {
                    if (b > 0) {
                        float t = a;
                        a = b;
                        b = t;
                    }
                }
            }
            result = new float[] {
                    a, b
            };
        }
        return result;
    }
    
    /**
     * Computes the surface distance on this sphere between two points given as
     * lon/lat coordinates. The x component of each vector needs to contain the
     * longitude and the y component the latitude (both in radians).
     * 
     * Algorithm from: http://www.csgnetwork.com/gpsdistcalc.html
     * 
     * @param p
     * @param q
     * @return distance on the sphere surface
     */
    public double surfaceDistanceBetween(ga_Vector2 p, ga_Vector2 q) {
        double t1 = Math.sin(p.y) * Math.sin(q.y);
        double t2 = Math.cos(p.y) * Math.cos(q.y);
        double t3 = Math.cos(p.x - q.x);
        double t4 = t2 * t3;
        double t5 = t1 + t4;
        double dist = Math.atan(-t5 / Math.sqrt(-t5 * t5 + 1)) + 2
                * Math.atan(1);
        if (Double.isNaN(dist)) {
            dist = 0;
        } else {
            dist *= radius;
        }
        return dist;
    }

    /**
     * Calculates the normal vector on the sphere in the direction of the
     * current point.
     * 
     * @param q
     * @return a unit normal vector to the tangent plane of the ellipsoid in the
     *         point.
     */
    public gb_Vector3 tangentPlaneNormalAt(gb_Vector3 q) {
        return q.cpy().sub(center).nor();
    }

    public gb_i_Mesh toMesh(int res) {
        return toMesh(null, res);
    }

    public gb_i_Mesh toMesh(gb_i_Mesh mesh, int res) {
        gb_SurfaceMeshBuilder builder =
                new gb_SurfaceMeshBuilder(new gb_SurfaceFunctionSphere(this));
        return builder.createMesh(mesh, res, 1);
    }
    @Override
    public String toString() {
    	return center+" "+radius;
    }
}
