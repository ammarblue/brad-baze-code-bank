package reuze.test;


import com.software.reuze.gb_Triangle;
import com.software.reuze.gb_Vector3;

import junit.framework.TestCase;

public class TriangleTest extends TestCase {

    public void testBarycentric() {
        gb_Vector3 a = new gb_Vector3(-100, -100, 0);
        gb_Vector3 c = new gb_Vector3(100, 0, 0);
        gb_Vector3 b = new gb_Vector3(-100, 100, 0);
        gb_Triangle t = new gb_Triangle(a, b, c);
        assertTrue(a.equalsWithTolerance(t.fromBarycentric(t.toBarycentric(a)),
                0.01f));
        assertTrue(b.equalsWithTolerance(t.fromBarycentric(t.toBarycentric(b)),
                0.01f));
        assertTrue(c.equalsWithTolerance(t.fromBarycentric(t.toBarycentric(c)),
                0.01f));
    }

    public void testCentroid() {
        gb_Vector3 a = new gb_Vector3(100, 0, 0);
        gb_Vector3 b = new gb_Vector3(0, 100, 0);
        gb_Vector3 c = new gb_Vector3(0, 0, 100);
        gb_Triangle t = new gb_Triangle(a, b, c);
        gb_Vector3 centroid = t.computeCentroid();
        assertTrue("incorrect centroid",
                centroid.equals(new gb_Vector3(100, 100, 100).mul(1f / 3)));
    }

    public void testClockwise() {
        gb_Vector3 a = new gb_Vector3(0, 100, 0);
        gb_Vector3 b = new gb_Vector3(100, 0, -50);
        gb_Vector3 c = new gb_Vector3(-100, -100, 100);
        assertTrue("not clockwiseXY", gb_Triangle.isClockwiseInXY(a, b, c));
        assertTrue("not clockwiseXZ", gb_Triangle.isClockwiseInXY(a, b, c));
        assertTrue("not clockwiseYZ", gb_Triangle.isClockwiseInXY(a, b, c));
    }

    public void testContainment() {
        gb_Vector3 a = new gb_Vector3(100, 0, 0);
        gb_Vector3 b = new gb_Vector3(0, 100, 0);
        gb_Vector3 c = new gb_Vector3(0, 0, 100);
        gb_Triangle t = new gb_Triangle(a, b, c);
        assertTrue(t.contains(a));
        assertTrue(t.contains(b));
        assertTrue(t.contains(c));
        assertTrue(t.contains(t.computeCentroid()));
        assertFalse(t.contains(a.tmp().add(0.1f, 0, 0)));
    }

    public void testEquilateral() {
        gb_Triangle t = gb_Triangle.createEquilateralFrom(new gb_Vector3(-100, 0, 0),
                new gb_Vector3(100, 0, 0));

    }

    public void testNormal() {
        gb_Vector3 a = new gb_Vector3(0, 100, 0);
        gb_Vector3 b = new gb_Vector3(100, 0, 0);
        gb_Vector3 c = new gb_Vector3(-100, -100, 0);
        gb_Triangle t = new gb_Triangle(a, b, c);
        gb_Vector3 n = t.computeNormal();
        assertTrue("normal wrong", n.equals(new gb_Vector3(0, 0, 1)));
    }
}
