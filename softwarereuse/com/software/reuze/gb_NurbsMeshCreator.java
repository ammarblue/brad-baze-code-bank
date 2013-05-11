package com.software.reuze;
import com.software.reuze.gb_TriangleMesh;


public class gb_NurbsMeshCreator {

    private gb_i_NurbsSurface surf;
    private ga_Vector2 maxUV;

    public gb_NurbsMeshCreator(gb_i_NurbsSurface surf) {
        this(surf, new ga_Vector2(1, 1));
    }

    public gb_NurbsMeshCreator(gb_i_NurbsSurface surf, ga_Vector2 maxUV) {
        this.surf = surf;
        this.maxUV = maxUV;
    }

    public gb_i_Mesh createControlMesh(gb_i_Mesh mesh) {
        gb_Vector3[] prev = null;
        if (mesh == null) {
            mesh = new gb_TriangleMesh();
        }
        int resU = surf.getControlNet().uLength();
        int resV = surf.getControlNet().vLength();
        ga_Vector2 dUV = new ga_Vector2(maxUV).mul(1f / resU, 1f / resV);
        ga_Vector2 a=new ga_Vector2(); ga_Vector2 b=new ga_Vector2(); ga_Vector2 c=new ga_Vector2();
        for (int u = 0; u < resU; u++) {
            gb_Vector3[] curr = new gb_Vector3[resV + 1];
            for (int v = 0; v < resV; v++) {
                gb_Vector3 vert = surf.getControlNet().get(u, v).to3D();
                if (v > 0 && u > 0) {
                	a.set(dUV); b.set(dUV); c.set(dUV);
                    mesh.addFace(curr[v - 1], vert, prev[v - 1],   //TODO check whether new verts needed
                            a.mul(u, v - 1), b.mul(u, v),
                            c.mul(u - 1, v - 1));
                    a.set(dUV); b.set(dUV); c.set(dUV);
                    mesh.addFace(vert, prev[v], prev[v - 1], a.mul(u, v),
                            b.mul(u - 1, v), c.mul(u - 1, v - 1));
                }
                curr[v] = vert;
            }
            prev = curr;
        }
        mesh.computeVertexNormals();
        return mesh;
    }

    public gb_i_Mesh createMesh(gb_i_Mesh mesh, int resU, int resV, boolean isClosed) {
        final gb_NurbsKnotVector knotU = surf.getUKnotVector();
        final gb_NurbsKnotVector knotV = surf.getVKnotVector();
        double iresU = knotU.get(knotU.length() - 1) / resU;
        double iresV = knotV.get(knotV.length() - 1) / resV;
        gb_Vector3[] prev = null;
        gb_Vector3[] first = null;
        if (mesh == null) {
            mesh = new gb_TriangleMesh();
        }
        ga_Vector2 dUV = new ga_Vector2(maxUV).mul(1f / resU, 1f / resV);
        ga_Vector2 a=new ga_Vector2(); ga_Vector2 b=new ga_Vector2(); ga_Vector2 c=new ga_Vector2();
        for (int u = 0; u <= resU; u++) {
            gb_Vector3[] curr = new gb_Vector3[resV + 1];
            for (int v = 0; v <= resV; v++) {
                gb_Vector3 vert = null;
                if (isClosed) {
                    vert = u < resU ? surf.pointOnSurface(u * iresU, v * iresV)
                            : first[v];
                } else {
                    vert = surf.pointOnSurface(u * iresU, v * iresV);
                }
                if (v > 0 && u > 0) {
                	a.set(dUV); b.set(dUV); c.set(dUV);
                    mesh.addFace(curr[v - 1], vert, prev[v - 1],
                            a.mul(u, v - 1), b.mul(u, v),
                            c.mul(u - 1, v - 1));
                    a.set(dUV); b.set(dUV); c.set(dUV);
                    mesh.addFace(vert, prev[v], prev[v - 1], a.mul(u, v),
                            b.mul(u - 1, v), c.mul(u - 1, v - 1));
                }
                curr[v] = vert;
            }
            prev = curr;
            if (u == 0) {
                first = curr;
            }
        }
        mesh.computeVertexNormals();
        return mesh;
    }

    public gb_i_NurbsSurface getSurface() {
        return surf;
    }

    public ga_Vector2 getUVScale() {
        return maxUV;
    }

    public void setSurface(gb_i_NurbsSurface surf) {
        this.surf = surf;
    }

    public void setUVScale(ga_Vector2 maxUV) {
        this.maxUV = maxUV;
    }
}
