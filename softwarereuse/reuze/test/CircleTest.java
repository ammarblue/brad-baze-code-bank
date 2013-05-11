package reuze.test;
import com.software.reuze.ga_Circle;
import com.software.reuze.ga_Vector2;

import junit.framework.TestCase;

public class CircleTest extends TestCase {

    private void showPoints(ga_Vector2[] points) {
        if (points != null) {
            for (ga_Vector2 p : points) {
                System.out.println(p);
            }
        } else {
            System.out.println("<null>");
        }
    }

    public void testCircleCircleIntersection() {
        ga_Circle a = new ga_Circle(100);
        ga_Circle b = new ga_Circle(new ga_Vector2(200, 100), 200);
        ga_Vector2[] isec = a.intersects(b);
        assertTrue(isec != null);
        assertTrue(isec[0].equals(new ga_Vector2(0, 100)));
        showPoints(isec);
        b.setRadius(100);
        isec = a.intersects(b);
        assertTrue(isec == null);
        b.setRadius(99).set(0, 0);
        isec = a.intersects(b);
        assertTrue(isec == null);
        b.position.x = 1;
        isec = a.intersects(b);
        assertTrue(isec != null);
        showPoints(isec);
    }
}
