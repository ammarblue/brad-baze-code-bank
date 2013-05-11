package com.software.reuze;


import java.util.List;


public class gb_i_TriangleSubdivisionCentroid implements gb_i_TriangleSubdivision {

    public List<gb_Vector3[]> subdivideTriangle(gb_Vector3 a, gb_Vector3 b, gb_Vector3 c,
            List<gb_Vector3[]> resultVertices) {
        gb_Vector3 centroid = new gb_Vector3(a).add(b).add(c).mul(1 / 3.0f);
        resultVertices.add(new gb_Vector3[] {
                a, b, centroid
        });
        resultVertices.add(new gb_Vector3[] {
                b, c, centroid
        });
        resultVertices.add(new gb_Vector3[] {
                c, a, centroid
        });
        return resultVertices;
    }

}
