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

/**
 * Abstract parent class for various 2D erosion simulations, implemented as
 * sub-classes.
 */
public abstract class ga_a_ErosionFunction {

    protected float[] elevation;
    protected int width;
    protected int height;

    protected float[] d = new float[9];
    protected float[] h = new float[9];
    protected int[] off;

    /**
     * Destructively erodes the given array.
     * 
     * @param elevation
     * @param width
     * @param height
     */
    public void erodeAll() {
        for (int y = 1, w1 = width - 1, h1 = height - 1; y < h1; y++) {
            for (int x = 1; x < w1; x++) {
                erodeAt(x, y);
            }
        }
    }

    public abstract void erodeAt(int x, int y);

    public void erodeWithinPolygon(ga_Polygon poly) {
        ga_Rectangle bounds = ga_Polygon.getBounds(poly).intersect(
                new ga_Rectangle(1, 1, width - 2, height - 2));
        ga_Vector2 pos = new ga_Vector2();
        for (int y = (int) (bounds.position.y-bounds.height), y2 = (int) bounds.position.y; y < y2; y++) { //top-bottom
            for (int x = (int) bounds.position.x, x2 = (int) (bounds.position.x+bounds.width); x < x2; x++) { //left-right
                if (poly.contains(pos.set(x, y))) {
                    erodeAt(x, y);
                }
            }
        }
    }

    public void setElevation(float[] elevation, final int width,
            final int height) {
        this.elevation = elevation;
        this.width = width;
        this.height = height;
        off = new int[] {
                -width - 1, -width, -width + 1, -1, 0, 1, width - 1, width,
                width + 1
        };
    }
}
