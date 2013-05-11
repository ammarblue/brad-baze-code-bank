package reuze.test;


import java.util.ArrayList;
import java.util.List;

import com.software.reuze.ga_QuadtreePoint;
import com.software.reuze.ga_Vector2;
import com.software.reuze.gb_AABB3;
import com.software.reuze.gb_PointOctree;
import com.software.reuze.gb_Sphere;
import com.software.reuze.gb_Vector3;


import junit.framework.TestCase;

public class TreeTest extends TestCase {

    public void testOctree() {
        gb_PointOctree t = new gb_PointOctree(new gb_Vector3(), 100);
        t.setMinNodeSize(0.5f);
        assertEquals(t.addPoint(new gb_Vector3(0, 0, 0)), true);
        assertEquals(t.addPoint(new gb_Vector3(1, 0, 0)), true);
        gb_PointOctree leaf1 = t.getLeafForPoint(new gb_Vector3(0, 0, 0));
        gb_PointOctree leaf2 = t.getLeafForPoint(new gb_Vector3(1, 0, 0));
        assertNotSame(leaf1, leaf2);
        assertEquals(t.addPoint(new gb_Vector3(0, 100, 0)), true);
        assertEquals(t.addPoint(new gb_Vector3(101, 0, 0)), false);
        List<gb_Vector3> points = t.getPointsWithinSphere(new gb_Sphere(new gb_Vector3(
                50, 0, 0), 50));
        assertEquals(points.size() == 2, true);
        points = t.getPointsWithinBox(new gb_AABB3(new gb_Vector3(50, 50, 50),
                new gb_Vector3(50, 50, 50)));
        assertEquals(points.size() == 3, true);
    }

    public void testQuadtree() {
        ga_QuadtreePoint t = new ga_QuadtreePoint(new ga_Vector2(), 100);
        t.setMinNodeSize(2);
        assertEquals(t.addPoint(new ga_Vector2(0, 0)), true);
        assertEquals(t.addPoint(new ga_Vector2(1, 1)), true);
        assertEquals(t.addPoint(new ga_Vector2(4, 0)), true);
        ga_QuadtreePoint leaf1 = t.getLeafForPoint(new ga_Vector2(0, 0));
        ga_QuadtreePoint leaf2 = t.getLeafForPoint(new ga_Vector2(4, 0));
        assertNotSame(leaf1, leaf2);
        List<ga_Vector2> points = t.getPointsWithinRect(leaf1);
        assertEquals(2, points.size());
    }

}
