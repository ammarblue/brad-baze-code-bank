package com.software.reuze;


import java.util.List;


public class gb_TriangleSubdivisionMidpoint implements gb_i_TriangleSubdivision {

    public List<gb_Vector3[]> subdivideTriangle(gb_Vector3 a, gb_Vector3 b, gb_Vector3 c,
            List<gb_Vector3[]> resultVertices) {
        gb_Vector3 mab = new gb_Vector3(a).interpolate(b, 0.5f);
        gb_Vector3 mbc = new gb_Vector3(b).interpolate(c, 0.5f);
        gb_Vector3 mca = new gb_Vector3(c).interpolate(a, 0.5f);
        resultVertices.add(new gb_Vector3[] {
                a, mab, mca
        });
        resultVertices.add(new gb_Vector3[] {
                mab, b, mbc
        });
        resultVertices.add(new gb_Vector3[] {
                mbc, c, mca
        });
        resultVertices.add(new gb_Vector3[] {
                mab, mbc, mca
        });
        return resultVertices;
    }

}
