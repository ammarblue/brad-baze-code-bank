package reuze.test;


import java.util.HashMap;
import java.util.List;

import com.software.reuze.gb_Line3D;
import com.software.reuze.gb_Vector3;
import com.software.reuze.gb_WEVertex;
import com.software.reuze.gb_WEWingedEdge;
import com.software.reuze.m_MathUtils;


import junit.framework.TestCase;

public class Line3DTest extends TestCase {

    public void testClosestPoint() {
        gb_Vector3 a = new gb_Vector3();
        gb_Vector3 b = new gb_Vector3(100, 0, 0);
        gb_Vector3 c = new gb_Vector3(50, 50, 0);
        gb_Line3D line = new gb_Line3D(a, b);
        gb_Vector3 isec = line.closestPointTo(c);
        assertEquals(m_MathUtils.abs(isec.x - c.x) < 0.5, true);
        c = new gb_Vector3(-50, -50, 0);
        isec = line.closestPointTo(c);
        assertEquals(isec.equals(a), true);
    }

    public void testHashing() {
        gb_Line3D l1 = new gb_Line3D(new gb_Vector3(100, 420, -50), new gb_Vector3(-888, 230,
                2999));
        gb_Line3D l2 = new gb_Line3D(new gb_Vector3(-888, 230, 2999), new gb_Vector3(100, 420,
                -50));
        assertTrue(l1.equals(l2));
        assertEquals(l1.hashCode(), l2.hashCode());
        l2.a = new gb_Vector3();
        assertFalse(l1.equals(l2));
        l1.b.clr();
        assertTrue(l1.equals(l2));
        HashMap<gb_Line3D, gb_WEWingedEdge> map = new HashMap<gb_Line3D, gb_WEWingedEdge>();
        map.put(l1, new gb_WEWingedEdge(new gb_WEVertex(l1.a, 0),
                new gb_WEVertex(l1.b, 1), null, 0));
        gb_WEWingedEdge e = map.get(l1);
        assertEquals(l1, e);
    }
    
    public void testSplitSegments() {
    	gb_Vector3 a = new gb_Vector3(0, 0, 0);
    	gb_Vector3 b = new gb_Vector3(100, 0, 0);
        List<gb_Vector3> list = gb_Line3D.splitIntoSegments(a, b, 8, null, true);
        assertEquals(14, list.size());
        // testing adding to existing list and skipping start point
        gb_Line3D.splitIntoSegments(b, a, 10, list, false);
        for (int i = 0; i < list.size(); i++) {
            System.out.println(i + ": " + list.get(i));
        }
        assertFalse(b.equals(list.get(14)));
        assertEquals(24, list.size());
    }
}
