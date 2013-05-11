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




import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.software.reuze.m_Matrix4;


public class gb_PointCloud implements Iterable<gb_Vector3> {

    protected List<gb_Vector3> points;

    protected gb_Vector3 min, max;
    protected gb_Vector3 centroid;

    protected float radiusSquared;

    public gb_PointCloud() {
        this(100);
    }

    public gb_PointCloud(int numPoints) {
        points = new ArrayList<gb_Vector3>(numPoints);
        clear();
    }

    public gb_PointCloud addAll(List<? extends gb_Vector3> plist) {
        for (gb_Vector3 p : plist) {
            addPoint(p);
        }
        return this;
    }

    public gb_PointCloud addPoint(gb_Vector3 p) {
        points.add(p);
        min.min(p);
        max.max(p);
        centroid.set(min.tmp().add(max).mul(0.5f));
        radiusSquared = m_MathUtils.max(radiusSquared,
                p.dst2(centroid));
        return this;
    }

    /**
     * Applies the given transformation matrix to all points in the cloud.
     * 
     * @param m
     *            transformation matrix
     * @return itself
     */
    public gb_PointCloud applyMatrix(m_Matrix4 m) {
        for (gb_Vector3 p : points) {
            p.set(m.applyTo(p));
        }
        updateBounds();
        return this;
    }

    /**
     * Updates all points in the cloud so that their new centroid is at the
     * origin.
     * 
     * @return itself
     */
    public gb_PointCloud center() {
        return center(null);
    }

    /**
     * Updates all points in the cloud so that their new centroid is at the
     * given point.
     * 
     * @param origin
     *            new centroid
     * @return itself
     */
    public gb_PointCloud center(gb_Vector3 origin) {
        getCentroid();
        gb_Vector3 delta = origin != null ? origin.tmp().sub(centroid) : centroid.tmp().inv();
        for (gb_Vector3 p : points) {
            p.add(delta);
        }
        min.add(delta);
        max.add(delta);
        centroid.add(delta);
        return this;
    }

    /**
     * Removes all points from the cloud and resets the bounds and centroid.
     * 
     * @return itself
     */
    public gb_PointCloud clear() {
        points.clear();
        min = gb_Vector3.MAX_VALUE.cpy();
        max = gb_Vector3.MIN_VALUE.cpy();
        centroid = new gb_Vector3();
        return this;
    }

    /**
     * Creates a deep copy of the cloud
     * 
     * @return copied instance
     */
    public gb_PointCloud copy() {
        gb_PointCloud c = new gb_PointCloud(points.size());
        for (gb_Vector3 p : points) {
            c.addPoint(p.cpy());
        }
        return c;
    }

    public gb_AABB3 getBoundingBox() {
        return gb_AABB3.fromMinMax(min, max);
    }

    public gb_Sphere getBoundingSphere() {
        return new gb_Sphere(getCentroid(), (float) Math.sqrt(radiusSquared));
    }

    /**
     * @return the cloud centroid
     */
    public gb_Vector3 getCentroid() {
        return centroid;
    }

    /**
     * @return an iterator for the backing point collection.
     * 
     * @see java.lang.Iterable#iterator()
     */
    public Iterator<gb_Vector3> iterator() {
        return points.iterator();
    }

    /**
     * Removes the point from the cloud, but doesn't update the bounds
     * automatically.
     * 
     * @param p
     * @return true, if point has been removed.
     */
    public boolean removePoint(gb_Vector3 p) {
        return points.remove(p);
    }

    /**
     * @return the current number of points in the cloud
     */
    public int size() {
        return points.size();
    }

    /**
     * Recalculates the bounding box, bounding sphere and centroid of the cloud.
     * 
     * @return itself
     */
    public gb_PointCloud updateBounds() {
        min = gb_Vector3.MAX_VALUE.cpy();
        max = gb_Vector3.MIN_VALUE.cpy();
        for (gb_Vector3 p : points) {
            min.min(p);
            max.max(p);
        }
        centroid.set(min.tmp().add(max).mul(0.5f));
        radiusSquared = 0;
        for (gb_Vector3 p : points) {
            radiusSquared = m_MathUtils.max(radiusSquared,
                    p.dst2(centroid));
        }
        return this;
    }
}
