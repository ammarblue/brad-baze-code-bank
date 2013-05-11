package reuze.test;


import com.software.reuze.gb_Plane;
import com.software.reuze.gb_Triangle;
import com.software.reuze.gb_Vector3;

import junit.framework.TestCase;

public class PlaneTest extends TestCase {

    public void testContainment() {
        gb_Triangle t = new gb_Triangle(new gb_Vector3(-100, 0, 0), new gb_Vector3(0, 0,
                -100), new gb_Vector3(0, 0, 100));
        gb_Plane pl = new gb_Plane(t.computeCentroid(), t.computeNormal());
    }

    public void testProjection() {
    	gb_Vector3 origin = new gb_Vector3(0, 100, 0);
        gb_Plane plane = new gb_Plane(origin, new gb_Vector3(0, 1, 0));
        gb_Vector3 proj;
        proj = plane.getProjectedPoint(new gb_Vector3());
        assertEquals(origin.tmp().inv(), proj);
        proj = plane.getProjectedPoint(new gb_Vector3(0, 200, 0));
        assertEquals(origin, proj);
        proj = plane.getProjectedPoint(origin);
        assertEquals(origin, proj);
    }
}
