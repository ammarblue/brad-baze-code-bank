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
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301, USA
 */
/**
 * An extensible builder class for {@link gb_TriangleMesh}es based on 3D surface
 * functions using spherical coordinates. In order to create mesh, you'll need
 * to supply a {@link gb_i_SurfaceFunction} implementation to the builder.
 */
public class gb_SurfaceMeshBuilder {

    protected gb_i_SurfaceFunction function;

    public gb_SurfaceMeshBuilder(gb_i_SurfaceFunction function) {
        this.function = function;
    }

    public gb_i_Mesh createMesh(int res) {
        return createMesh(null, res, 1);
    }

    public gb_i_Mesh createMesh(gb_i_Mesh mesh, int res, float size) {
        return createMesh(mesh, res, size, true);
    }

    public gb_i_Mesh createMesh(gb_i_Mesh mesh, int res, float size, boolean isClosed) {
        if (mesh == null) {
            mesh = new gb_TriangleMesh();
        }
        gb_Vector3 a = new gb_Vector3();
        gb_Vector3 b = new gb_Vector3();
        gb_Vector3 pa = new gb_Vector3(), pb = new gb_Vector3();
        gb_Vector3 a0 = new gb_Vector3(), b0 = new gb_Vector3();
        int phiRes = function.getPhiResolutionLimit(res);
        float phiRange = function.getPhiRange();
        int thetaRes = function.getThetaResolutionLimit(res);
        float thetaRange = function.getThetaRange();
        float pres = 1f / (1 == res % 2 ? res - 0 : res);
        for (int p = 0; p < phiRes; p++) {
            float phi = p * phiRange * pres;
            float phiNext = (p + 1) * phiRange * pres;
            for (int t = 0; t <= thetaRes; t++) {
                float theta;
                theta = t * thetaRange / res;
                a =
                        function.computeVertexFor(a, phiNext, theta).mul(size);
                b = function.computeVertexFor(b, phi, theta).mul(size);
                if (b.dst(a) < 0.0001) {
                    b.set(a);
                }
                if (t > 0) {
                    if (t == thetaRes && isClosed) {
                        a.set(a0);
                        b.set(b0);
                    }
                    mesh.addFace(pa, pb, a);
                    mesh.addFace(pb, b, a);
                } else {
                    a0.set(a);
                    b0.set(b);
                }
                pa.set(a);
                pb.set(b);
            }
        }
        return mesh;
    }

    /**
     * @return the function
     */
    public gb_i_SurfaceFunction getFunction() {
        return function;
    }

    /**
     * @param function
     *            the function to set
     */
    public void setFunction(gb_i_SurfaceFunction function) {
        this.function = function;
    }
}
