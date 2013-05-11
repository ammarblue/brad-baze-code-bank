package reuze.test;

import java.util.ArrayList;

import com.software.reuze.ff_STLLoader;
import com.software.reuze.gb_TriangleFace;
import com.software.reuze.gb_TriangleMesh;
import com.software.reuze.gb_Vector3;
import com.software.reuze.gb_Vector3Id;


import junit.framework.TestCase;

public class TriangleMeshTest extends TestCase {

    gb_TriangleMesh mesh;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mesh = new gb_TriangleMesh("foo");
        mesh.addFace(new gb_Vector3(), new gb_Vector3(100, 100, 0), new gb_Vector3(100, 0, 0));
        mesh.addFace(new gb_Vector3(100, 100, 0), new gb_Vector3(100, 0, -100),
                new gb_Vector3(100, 0, 0));
        mesh.addFace(new gb_Vector3(100f, 0, -100), new gb_Vector3(0, 100, -100),
                new gb_Vector3(0, 0, -100));
    }

    public void testFaceNormals() {
        assertEquals(new gb_Vector3(0, 0, 1), mesh.faces.get(0).normal);
        assertEquals(new gb_Vector3(1, 0, 0), mesh.faces.get(1).normal);
        assertEquals(new gb_Vector3(0, 0, -1), mesh.faces.get(2).normal);
    }

    public void testSTLImport() {
        double total = 0;
        int numIter = 100;
        for (int i = 0; i < numIter; i++) {
            long t = System.nanoTime();
            mesh = (gb_TriangleMesh) new ff_STLLoader().loadBinary("./data/test.stl",
                    ff_STLLoader.TRIANGLEMESH);
            total += (System.nanoTime() - t);
        }
        System.out.println("avg. mesh construction time: " + total * 1e-6
                / numIter);
        assertNotNull(mesh);
        assertEquals(714, mesh.getNumVertices());
        assertEquals(1424, mesh.getNumFaces());
        System.out.println(mesh);
    }

    public void testUniqueVertices() {
        ArrayList<gb_Vector3> verts = new ArrayList<gb_Vector3>(mesh.vertices.values());
        assertEquals(6, mesh.vertices.size());
        for (gb_TriangleFace f : mesh.faces) {
            assertEquals(verts.get(f.a.id), f.a);
            assertEquals(verts.get(f.b.id), f.b);
            assertEquals(verts.get(f.c.id), f.c);
        }
    }

    public void testVertexNormals() {
        mesh.computeVertexNormals();
        gb_Vector3Id[] verts = null;
        for (gb_TriangleFace f : mesh.faces) {
            verts = f.getVertices(verts);
            System.out.println(f);
        }
    }
}
