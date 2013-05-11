package com.software.reuze;



import java.util.List;

import com.software.reuze.ga_Triangle2D;


public interface ga_i_PolygonTesselator {

    /**
     * Tesselates the given polygon into a set of triangles.
     * 
     * @param poly
     *            polygon
     * @return list of triangles
     */
    public List<ga_Triangle2D> tesselatePolygon(ga_Polygon poly);

}