package com.software.reuze;

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
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

/**
 * This class defines a 2D ellipse and provides several utility methods for it.
 */
public class ga_Ellipse {

    public ga_Vector2 position;
	public ga_Vector2 radius = new ga_Vector2();
    public float focus;

    public ga_Ellipse() {
        this(0, 0, 1);

    }

    public ga_Ellipse(float rx, float ry) {
        this(0, 0, rx, ry);
    }

    public ga_Ellipse(float x, float y, float r) {
        this(x, y, r, r);

    }

    public ga_Ellipse(float x, float y, float rx, float ry) {
        position=new ga_Vector2(x, y);
        setRadii(rx, ry);
    }

    public ga_Ellipse(ga_Vector2 v, float r) {
        this(v.x, v.y, r, r);
    }

    public ga_Ellipse(ga_Vector2 v, ga_Vector2 r) {
        this(v.x, v.y, r.x, r.y);
    }

    public boolean containsPoint(ga_Vector2 p) {
        ga_Vector2[] foci = getFoci();
        return p.dst(foci[0]) + p.dst(foci[1]) < 2 * m_MathUtils
                .max(radius.x, radius.y);
    }

    /**
     * Computes the area covered by the ellipse.
     * 
     * @return area
     */
    public float getArea() {
        return m_MathUtils.PI * radius.x * radius.y;
    }

    /**
     * Computes the approximate circumference of the ellipse, using this
     * equation: <code>2 * PI * sqrt(1/2 * (rx*rx+ry*ry))</code>.
     * 
     * The precise value is an infinite series elliptical integral, but the
     * approximation comes sufficiently close. See Wikipedia for more details:
     * 
     * http://en.wikipedia.org/wiki/Ellipse
     * 
     * @return circumference
     */
    public float getCircumference() {
        // wikipedia solution:
        // return (float) (MathUtils.PI * (3 * (radius.x + radius.y) - Math
        // .sqrt((3 * radius.x + radius.y) * (radius.x + 3 * radius.y))));
        return (float) Math.sqrt(0.5 * radius.len2()) * m_MathUtils.TWO_PI;
    }

    /**
     * @return the focus
     */
    public ga_Vector2[] getFoci() {
        ga_Vector2[] foci = new ga_Vector2[2];
        if (radius.x > radius.y) {
            foci[0] = position.sub(focus, 0);
            foci[1] = position.add(focus, 0);
        } else {
            foci[0] = position.sub(0, focus);
            foci[1] = position.add(0, focus);
        }
        return foci;
    }

    /**
     * @return the 2 radii of the ellipse as a Vector2
     */
    public ga_Vector2 getRadii() {
        return radius.copy();
    }

    /**
     * Sets the radii of the ellipse to the new values.
     * 
     * @param rx
     * @param ry
     * @return itself
     */
    public ga_Ellipse setRadii(float rx, float ry) {
        radius.set(rx, ry);
        focus = radius.len();
        return this;
    }

    /**
     * Sets the radii of the ellipse to the values given by the vector.
     * 
     * @param r
     * @return itself
     */
    public ga_Ellipse setRadii(ga_Vector2 r) {
        return setRadii(r.x, r.y);
    }

    /**
     * Creates a {@link Polygon2D} instance of the ellipse sampling it at the
     * given resolution.
     * 
     * @param res
     *            number of steps
     * @return ellipse as polygon
     */
    public ga_Polygon toPolygon2D(int res) {
        ga_Polygon poly = new ga_Polygon();
        float step = m_MathUtils.TWO_PI / res;
        for (int i = 0; i < res; i++) {
            poly.add(ga_Vector2.fromTheta(i * step).mul(radius).add(position));
        }
        poly.calcCenter();
        return poly;
    }
}
