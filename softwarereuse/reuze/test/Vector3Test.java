package reuze.test;


import java.util.List;

import com.software.reuze.gb_Line3D;
import com.software.reuze.gb_Vector3;


import junit.framework.TestCase;

public class Vector3Test extends TestCase {

    public void testClosestAxis() {
        assertEquals(gb_Vector3.X, new gb_Vector3(-1, 0.9f, 0.8f).getClosestAxis());
        assertEquals(null, new gb_Vector3(1, -1, 0).getClosestAxis());
        assertEquals(null, new gb_Vector3(1, 0, -1).getClosestAxis());
        assertEquals(gb_Vector3.Y,
                new gb_Vector3(0.8f, -1, -0.99999f).getClosestAxis());
        assertEquals(null, new gb_Vector3(0.8f, -1, 1).getClosestAxis());
        assertEquals(gb_Vector3.Z, new gb_Vector3(0.8f, -1, 1.1f).getClosestAxis());
        assertEquals(gb_Vector3.X, new gb_Vector3(1, 0, 0).getClosestAxis());
        assertEquals(gb_Vector3.Y, new gb_Vector3(0, -1, 0).getClosestAxis());
        assertEquals(gb_Vector3.Z, new gb_Vector3(0, 0, 1).getClosestAxis());
    }

    public void testSphericalInstance() {
        gb_Vector3 v = new gb_Vector3(-1, 1, 1);
        gb_Vector3 w = v.cpy();
        v.toSpherical();
        v.toCartesian();
        System.out.println(v);
        assertTrue(v.equalsWithTolerance(w, 0.0001f));
    }

    public void testSplitSegments() {
        gb_Vector3 a = new gb_Vector3(0, 0, 0);
        gb_Vector3 b = new gb_Vector3(100, 0, 0);
        List<gb_Vector3> list = gb_Line3D.splitIntoSegments(a, b, 8, null, true);
        assertEquals(14, list.size());
        // testing adding to existing list and skipping start point
        gb_Line3D.splitIntoSegments(b, a, 10, list, false);
        assertFalse(b.equals(list.get(14)));
        assertEquals(24, list.size());
    }
}
