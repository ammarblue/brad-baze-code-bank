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
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;


public class ga_Voronoi {

    public static float DEFAULT_SIZE = 10000;

    protected ga_TriangulationDelaunay delaunay;
    protected ga_TriangleDelaunay initialTriangle;
    protected List<ga_Vector2> sites = new ArrayList<ga_Vector2>();

    public ga_Voronoi() {
        this(DEFAULT_SIZE);
    }

    public ga_Voronoi(float size) {
        initialTriangle = new ga_TriangleDelaunay(
                new ga_TriangleDelaunayVertex(-size, -size), new ga_TriangleDelaunayVertex(size,
                        -size), new ga_TriangleDelaunayVertex(0, size));
        this.delaunay = new ga_TriangulationDelaunay(initialTriangle);
    }

    public void addPoint(ga_Vector2 p) {
        sites.add(p.copy());
        delaunay.delaunayPlace(new ga_TriangleDelaunayVertex(p.x, p.y));
    }

    public void addPoints(Collection<? extends ga_Vector2> points) {
        for (ga_Vector2 p : points) {
            addPoint(p);
        }
    }

    public List<ga_Polygon> getRegions() {
        List<ga_Polygon> regions = new LinkedList<ga_Polygon>();
        HashSet<ga_TriangleDelaunayVertex> done = new HashSet<ga_TriangleDelaunayVertex>(
                initialTriangle);
        for (ga_TriangleDelaunay triangle : delaunay) {
            for (ga_TriangleDelaunayVertex site : triangle) {
                if (done.contains(site)) {
                    continue;
                }
                done.add(site);
                List<ga_TriangleDelaunay> list = delaunay.surroundingTriangles(
                        site, triangle);
                ga_Polygon poly = new ga_Polygon();
                for (ga_TriangleDelaunay tri : list) {
                    ga_TriangleDelaunayVertex circumeter = tri.getCircumcenter();
                    poly.add(new ga_Vector2((float) circumeter.coord(0),
                            (float) circumeter.coord(1)));
                }
                poly.calcCenter();
                regions.add(poly);
            }
        }
        return regions;
    }

    public List<ga_Vector2> getSites() {
        return sites;
    }

    public List<ga_Triangle2D> getTriangles() {
        List<ga_Triangle2D> tris = new ArrayList<ga_Triangle2D>();
        for (ga_TriangleDelaunay t : delaunay) {
            tris.add(new ga_Triangle2D(t.get(0).toVec2D(), t.get(1).toVec2D(), t
                    .get(2).toVec2D()));
        }
        return tris;
    }
}
