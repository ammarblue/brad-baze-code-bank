package com.software.reuze;
/*
 *   __               .__       .__  ._____.           
 * _/  |_  _______  __|__| ____ |  | |__\_ |__   ______
 * \   __\/  _ \  \/  /  |/ ___\|  | |  || __ \ /  ___/
 *  |  | (  <_> >    <|  \  \___|  |_|  || \_\ \\___ \ 
 *  |__|  \____/__/\_ \__|\___  >____/__||___  /____  >
 *                   \/       \/             \/     \/ 
 *
 * Copyright (c) 2006-2011 Karsten Schmidt
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * http://creativecommons.org/licenses/LGPL/2.1/
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301, USA
 */

import com.software.reuze.gb_IntersectionData;
import com.software.reuze.gb_Ray;
import com.software.reuze.gb_i_Intersector;
import com.software.reuze.gb_i_Reflector;

public class gb_SphereIntersectorReflector implements gb_i_Intersector, gb_i_Reflector {

    protected gb_Sphere sphere;
    protected gb_IntersectionData isectData;

    protected gb_Vector3 reflectedDir, reflectedPos;
    protected float reflectTheta;

    public gb_SphereIntersectorReflector(gb_Sphere s) {
        sphere = s;
        isectData = new gb_IntersectionData();
    }

    public gb_SphereIntersectorReflector(gb_Vector3 o, float r) {
        this(new gb_Sphere(o, r));
    }

    public gb_IntersectionData getIntersectionData() {
        return isectData;
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.geom.Reflector3D#getReflectedRayPointAtDistance(float)
     */
    public final gb_Vector3 getReflectedRayPointAtDistance(float dist) {
        if (reflectedDir != null) {
            return isectData.pos.tmp().add(reflectedDir.tmp2().mul(dist));
        } else {
            return null;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.geom.Reflector3D#getReflectionAngle()
     */
    public float getReflectionAngle() {
        return reflectTheta;
    }

    /**
     * @return the sphere
     */
    public gb_Sphere getSphere() {
        return sphere;
    }

    /**
     * Calculates the distance of the vector to the given sphere in the
     * specified direction. A sphere is defined by a 3D point and a radius.
     * Normalized directional vectors expected.
     * 
     * @param ray
     *            intersection ray
     * @return distance to sphere in world units, -1 if no intersection.
     */

    public float intersectRayDistance(gb_Ray ray) {
        final gb_Vector3 q = sphere.center.tmp().sub(ray.origin);
        float distSquared = q.len2();
        float v = q.dot(ray.direction);
        float d = sphere.radius * sphere.radius - (distSquared - v * v);

        // If there was no intersection, return -1
        if (d < 0.0) {
            return -1;
        }

        // Return the distance to the [first] intersecting point
        return v - (float) Math.sqrt(d);
    }

    public boolean intersectsRay(gb_Ray ray) {
        isectData.dist = intersectRayDistance(ray);
        isectData.isIntersection = isectData.dist >= 0;
        if (isectData.isIntersection) {
            // get the intersection point
            isectData.pos = ray.origin.cpy().add(ray.getDirection().tmp().mul(isectData.dist));
            // calculate the direction from our point to the intersection pos
            isectData.dir = isectData.pos.cpy().sub(ray.origin);
            isectData.normal = sphere.tangentPlaneNormalAt(isectData.pos);
        }
        return isectData.isIntersection;
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.geom.Reflector3D#reflectRay(toxi.geom.Vector3, toxi.geom.Vector3)
     */
    public gb_Ray reflectRay(gb_Ray ray) {
        if (intersectsRay(ray)) {
            // compute the normal vector of the sphere at the intersection
            // position
            // compute the reflection angle
            reflectTheta = isectData.dir.angleBetween(isectData.normal, true)
                    * 2 + m_MathUtils.PI;
            // then form a perpendicular vector standing on the plane spanned by
            // isectDir and sphereNormal
            // this vector will be used to mirror the ray around the
            // intersection point
            gb_Vector3 reflectNormal = isectData.dir.tmp().nor()
                    .crs(isectData.normal).nor();
            if (!reflectNormal.isZero()) {
                // compute the reflected ray direction
                reflectedDir = isectData.dir.cpy().nor().rotateAroundAxis(
                        reflectNormal, reflectTheta);
            } else {
                reflectedDir = isectData.dir.cpy().inv();
            }
            return new gb_Ray(isectData.pos, reflectedDir);
        } else {
            return null;
        }
    }

    /**
     * @param sphere
     *            the sphere to set
     */
    public void setSphere(gb_Sphere sphere) {
        this.sphere = sphere;
    }
}
