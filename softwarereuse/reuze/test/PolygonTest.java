package reuze.test;
import com.software.reuze.ga_Circle;
import com.software.reuze.ga_Polygon;
import com.software.reuze.ga_Vector2;

import junit.framework.TestCase;

public class PolygonTest extends TestCase {

    public void testAreaAndCentroid() {
        ga_Polygon p = new ga_Polygon();
        p.add(new ga_Vector2());
        p.add(new ga_Vector2(1, 0));
        p.add(new ga_Vector2(1, 1));
        p.add(new ga_Vector2(0, 1));
        p.add(new ga_Vector2());
        assertEquals(5, p.size());
        p.calcAll();
        assertEquals(.70710677f, p.getRadius());
        assertEquals(4f, p.getPerimeter());
        assertEquals(1f, p.getAndCalcSignedArea());
        assertEquals(1f, p.getArea());
        assertEquals(new ga_Vector2(0.5f, 0.5f), p.center);
        assertEquals(new ga_Vector2(0.5f, 0.5f), p.getCentroid());
    }

    public void testCircleArea() {
        float radius = 1;
        int subdiv = 36;
        ga_Polygon p = new ga_Circle(radius).toPolygon(subdiv);
        p.calcAll();
        float area = p.getArea();
        float area2 = new ga_Circle(radius).area();
        float ratio = area / area2;
        assertTrue((1 - ratio) < 0.01);
    }

    public void testClockwise() {
        ga_Polygon p = new ga_Circle(50).toPolygon(8);
        assertFalse(p.isCounterClockWise());
    }

    public void testContainment() {
        final ga_Vector2 origin = new ga_Vector2(100, 100);
        ga_Polygon p = new ga_Circle(origin, 50).toPolygon(8);
        assertTrue(p.contains(origin));
        assertTrue(p.contains(p.points.get(0)));
        assertFalse(p.contains(p.points.get(3).tmp().mul(1.01f)));
    }

    public void testIncreaseVertcount() {
        final ga_Vector2 origin = new ga_Vector2(100, 100);
        ga_Polygon p = new ga_Circle(origin, 50).toPolygon(3);
        p.increaseVertexCount(6);
        assertEquals(6, p.size());
    }

    public void testReduce() {
        ga_Polygon p = new ga_Circle(100).toPolygon(30);
        float len = p.points.get(0).dst(p.points.get(1));
        p.reduceVertices(len * 0.99f);
        assertEquals(30, p.size());
        p.reduceVertices(len * 1.5f);
        assertEquals(15, p.size());
    }
}
