package com.software.reuze;


import java.util.List;


public interface gb_i_TriangleSubdivision {

    public List<gb_Vector3[]> subdivideTriangle(gb_Vector3 a, gb_Vector3 b, gb_Vector3 c,
            List<gb_Vector3[]> resultVertices);
}
