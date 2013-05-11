package reuze.test;


import com.software.reuze.ga_Line2D;
import com.software.reuze.ga_Vector2;

import junit.framework.TestCase;

public class Line2DTest extends TestCase {

    public void testHashing() {
        ga_Line2D l1 = new ga_Line2D(new ga_Vector2(100, 420), new ga_Vector2(-888, 230));
        ga_Line2D l2 = new ga_Line2D(new ga_Vector2(-888, 230), new ga_Vector2(100, 420));
        assertTrue(l1.equals(l2));
        System.out.println(l1.hashCode());
        assertEquals(l1.hashCode(), l2.hashCode());
        l2.a = new ga_Vector2();
        assertFalse(l1.equals(l2));
        l1.b.clear();
        assertTrue(l1.equals(l2));
    }

    public void testIntersection() {
        ga_Line2D l = new ga_Line2D(new ga_Vector2(), new ga_Vector2(100, 100));
        ga_Line2D k = new ga_Line2D(new ga_Vector2(0, 50), new ga_Vector2(100, 50));
        ga_Line2D.LineIntersection isec = l.intersectLine(k);
        assertEquals(ga_Line2D.LineIntersection.Type.INTERSECTING, isec.getType());
        assertEquals(new ga_Vector2(50, 50), isec.getPos());
        k = l.copy();
        assertEquals(ga_Line2D.LineIntersection.Type.COINCIDENT, l.intersectLine(k)
                .getType());
        k = new ga_Line2D(new ga_Vector2(110, 110), new ga_Vector2(220, 220));
        assertEquals(ga_Line2D.LineIntersection.Type.COINCIDENT_NO_INTERSECT, l
                .intersectLine(k).getType());
        k = new ga_Line2D(new ga_Vector2(-100, -100), new ga_Vector2(100, 50));
        assertEquals(ga_Line2D.LineIntersection.Type.NON_INTERSECTING, l.intersectLine(k)
                .getType());
        k = new ga_Line2D(new ga_Vector2(200, -100), new ga_Vector2(400, 100));
        assertEquals(ga_Line2D.LineIntersection.Type.PARALLEL, l.intersectLine(k)
                .getType());
    }

    public void testOrientation() {
        ga_Line2D l = new ga_Line2D(new ga_Vector2(0, 0), new ga_Vector2(100, 0));
        System.out.println(l.getDirection().angleBetween(ga_Vector2.Y, true));
    }

    public void testScale() {
        ga_Line2D l = new ga_Line2D(new ga_Vector2(200, 200), new ga_Vector2(100, 100));
        float len = l.len();
        l.scale(0.9f);
        assertEquals(0.9f * len, l.len());
        l = new ga_Line2D(new ga_Vector2(100, 200), new ga_Vector2(200, 100));
        len = l.len();
        l.scale(3f);
        assertEquals(3f * len, l.len());
    }
}
