package reuze.test;


import com.software.reuze.ga_Triangle2D;
import com.software.reuze.ga_Vector2;
import com.software.reuze.gb_Vector3;

import junit.framework.TestCase;

public class Triangle2DTest extends TestCase {

    public void testBarycentric() {
        ga_Vector2 a = new ga_Vector2(-100, 0);
        ga_Vector2 b = new ga_Vector2(0, -100);
        ga_Vector2 c = new ga_Vector2(100, 0);
        ga_Triangle2D t = new ga_Triangle2D(a, b, c);
        assertEquals(new gb_Vector3(1, 0, 0), t.toBarycentric(a));
        assertEquals(new gb_Vector3(0, 1, 0), t.toBarycentric(b));
        assertEquals(new gb_Vector3(0, 0, 1), t.toBarycentric(c));
        // test roundtrip
        assertEquals(a, t.fromBarycentric(t.toBarycentric(a)));
        assertEquals(b, t.fromBarycentric(t.toBarycentric(b)));
        assertEquals(c, t.fromBarycentric(t.toBarycentric(c)));
        ga_Vector2 p = new ga_Vector2(0, 0);
        assertEquals(p, t.fromBarycentric(t.toBarycentric(p)));
        // test point outside
        gb_Vector3 bp = t.toBarycentric(new ga_Vector2(0, -150));
        assertTrue(bp.len() > 1);
    }

    public void testCentroid() {
        ga_Vector2 a = new ga_Vector2(-100, 0);
        ga_Vector2 b = new ga_Vector2(0, 100);
        ga_Vector2 c = new ga_Vector2(100, 0);
        ga_Triangle2D t = new ga_Triangle2D(a, b, c);
        ga_Vector2 centroid = t.computeCentroid();
        assertTrue("incorrect centroid",
                centroid.equals(new ga_Vector2(0, 100).mul(1f / 3)));
    }

    public void testClockwise() {
        ga_Vector2 a = new ga_Vector2(-100, 0);
        ga_Vector2 b = new ga_Vector2(0, -100);
        ga_Vector2 c = new ga_Vector2(100, 0);
        ga_Vector2 d = new ga_Vector2(50, 50);
        // clockwise
        assertTrue(ga_Triangle2D.isClockwise(a, b, c));
        assertTrue(ga_Triangle2D.isClockwise(b, c, d));
        assertTrue(ga_Triangle2D.isClockwise(c, d, a));
        assertTrue(ga_Triangle2D.isClockwise(a, c, d));
        // anticlockwise
        assertFalse(ga_Triangle2D.isClockwise(a, c, b));
        assertFalse(ga_Triangle2D.isClockwise(d, c, b));
        assertFalse(ga_Triangle2D.isClockwise(a, d, c));
    }

    public void testContainment() {
        ga_Vector2 a = new ga_Vector2(-100, 0);
        ga_Vector2 b = new ga_Vector2(0, -100);
        ga_Vector2 c = new ga_Vector2(100, 0);
        ga_Triangle2D t = new ga_Triangle2D(a, b, c);
        assertTrue(t.contains(new ga_Vector2(0, -50)));
        assertTrue(t.contains(a));
        assertTrue(t.contains(b));
        assertTrue(t.contains(c));
        assertFalse(t.contains(new ga_Vector2(0, -101)));
        // check anti-clockwise
        t.flipVertexOrder();
        assertTrue(t.contains(new ga_Vector2(0, -50)));
        assertTrue(t.contains(a));
        assertTrue(t.contains(b));
        assertTrue(t.contains(c));
        assertFalse(t.contains(new ga_Vector2(0, -101)));
    }

    public void testEquilateral() {
        ga_Triangle2D t = ga_Triangle2D.createEquilateralFrom(new ga_Vector2(-100, 0),
                new ga_Vector2(100, 0));
        assertEquals(new ga_Vector2(0, -57.735027f), t.computeCentroid());
    }

    public void testIntersection() {
        ga_Vector2 a = new ga_Vector2(-100, 0);
        ga_Vector2 b = new ga_Vector2(0, -100);
        ga_Vector2 c = new ga_Vector2(100, 0);
        ga_Vector2 d = new ga_Vector2(-200, -50);
        ga_Vector2 e = new ga_Vector2(0, 100);
        ga_Vector2 f = new ga_Vector2(0, -30);
        ga_Triangle2D t = new ga_Triangle2D(a, b, c);
        ga_Triangle2D t2 = new ga_Triangle2D(d, e, f);
        assertTrue(t.intersects(t2));
        f.x = 100;
        assertTrue(t.intersects(t2));
        assertTrue(t.intersects(new ga_Triangle2D(a, c, e)));
        assertFalse(t.intersects(new ga_Triangle2D(a.tmp().add(0, 0.01f), c.tmp2().add(
                0, 0.01f), e)));
        assertTrue(t.intersects(new ga_Triangle2D(a.tmp().add(0, 0.01f), c.tmp2().add(
                0, 0.01f), f)));
    }
}
