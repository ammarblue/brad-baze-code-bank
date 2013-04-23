package com.XML;


/*
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



//import javax.xml.bind.annotation.XmlAccessType;
//import javax.xml.bind.annotation.XmlAccessorType;
//import javax.xml.bind.annotation.XmlElement;

/**
 * A simple 2D ray datatype
 */
//@XmlAccessorType(XmlAccessType.FIELD)
public class ga_Ray2D {

    //@XmlElement(required = true)
    public ga_Vector2 origin;
	public ga_Vector2 direction;

    public ga_Ray2D() {
    	origin=new ga_Vector2();
        direction = ga_Vector2.Y.copy();
    }

    public ga_Ray2D(float x, float y, final ga_Vector2 d) {
        origin=new ga_Vector2(x, y);
        direction = d.nor();
    }

    public ga_Ray2D(final ga_Vector2 o, final ga_Vector2 d) {
        this(o.x, o.y, d);
    }

    /**
     * Returns the ray's direction vector.
     * 
     * @return vector
     */
    public ga_Vector2 getDirection() {
        return direction.copy();
    }

    /**
     * Calculates the distance between the given point and the infinite line
     * coinciding with this ray.
     * 
     * @param p
     * @return distance
     */
    public float getDistanceToPoint(ga_Vector2 p) {
        ga_Vector2 sp = p.tmp().sub(origin);
        return sp.dst(direction.tmp2().mul(sp.dot(direction)));
    }

    public ga_Vector2 getPointAtDistance(float dist) {
        return origin.copy().add(direction.tmp().mul(dist));
    }

    /**
     * Uses a normalized copy of the given vector as the ray direction.
     * 
     * @param d
     *            new direction
     * @return itself
     */
    public ga_Ray2D setDirection(final ga_Vector2 d) {
        direction.set(d).nor();
        return this;
    }

    /**
     * Converts the ray into a 2D Line segment with its start point coinciding
     * with the ray origin and its other end point at the given distance along
     * the ray.
     * 
     * @param dist
     *            end point distance
     * @return line segment
     */
    public ga_Line2D toLine2DWithPointAtDistance(float dist) {
        return new ga_Line2D(origin, getPointAtDistance(dist));
    }

    public String toString() {
        return "Ray2D: " + origin.toString() + " dir: " + direction;
    }
}
