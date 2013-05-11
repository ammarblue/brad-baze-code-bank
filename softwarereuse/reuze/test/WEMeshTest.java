package reuze.test;


import com.software.reuze.gb_StrategySubdivisionMidpoint;
import com.software.reuze.gb_Triangle;
import com.software.reuze.gb_TriangleFace;
import com.software.reuze.gb_Vector3;
import com.software.reuze.gb_WEFace;
import com.software.reuze.gb_WETriangleMesh;
import com.software.reuze.gb_WEVertex;
import com.software.reuze.gb_WEWingedEdge;

import junit.framework.TestCase;

public class WEMeshTest extends TestCase {

    private gb_WETriangleMesh m;

    /*
     * (non-Javadoc)
     * 
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        m = new gb_WETriangleMesh("plane", 4, 2);
        m.addFace(new gb_Vector3(), new gb_Vector3(100, 0, 0), new gb_Vector3(100, 100, 0));
        m.addFace(new gb_Vector3(), new gb_Vector3(100, 100, 0), new gb_Vector3(0, 100, 0));
        super.setUp();
    }

    public void testAddFace() {
        assertEquals(5, m.edges.size());
        System.out.println("mesh edges:");
        for (gb_WEWingedEdge e : m.edges.values()) {
            System.out.println(e);
        }
        gb_WEVertex v = (gb_WEVertex) m.vertices.get(new gb_Vector3());
        assertEquals(3, v.edges.size());
        assertEquals(1, v.edges.get(0).faces.size());
        assertEquals(2, v.edges.get(1).faces.size());
        System.out.println("vertex edges:");
        for (gb_WEWingedEdge e : v.edges) {
            System.out.println(e);
        }
    }

    public void testFaceEdgeCount() {
        for (gb_TriangleFace f : m.faces) {
            assertEquals(3, ((gb_WEFace) f).edges.size());
        }
    }

    public void testPerforate() {
        m.removeFace(m.getFaces().get(0));
        gb_WEFace f = (gb_WEFace) m.getFaces().get(0);
        m.perforateFace(f, 0.5f);
        System.out.println(m.edges.size() + " edges");
    }

    public void testRemoveFace() {
        assertEquals(5, m.edges.size());
        gb_WEFace f = (gb_WEFace) m.getFaces().get(0);
        m.removeFace(f);
        assertEquals(3, m.edges.size());
        assertEquals(3, m.vertices.size());
    }

    public void testSplitEdge() {
        gb_WEWingedEdge e = ((gb_WEVertex) m.vertices.get(new gb_Vector3())).edges.get(1);
        m.splitEdge(e, new gb_StrategySubdivisionMidpoint());
        assertEquals(4, m.faces.size());
        assertEquals(8, m.edges.size());
        m.computeVertexNormals();
        for (gb_TriangleFace f : m.faces) {
            System.out.println(gb_Triangle.isClockwiseInXY(f.a, f.b, f.c) + " " + f);
        }
        assertEquals(3, ((gb_WEVertex) m.faces.get(0).a).edges.size());
        assertEquals(3, ((gb_WEVertex) m.faces.get(0).b).edges.size());
        assertEquals(4, ((gb_WEVertex) m.faces.get(0).c).edges.size());
    }

    public void testSubdivide() {
        m.subdivide();
        assertEquals(8, m.faces.size());
    }
}