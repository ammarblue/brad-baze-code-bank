package reuze.test;


import com.software.reuze.ga_Vector2;
import com.software.reuze.gb_Ray;
import com.software.reuze.gb_Sphere;
import com.software.reuze.gb_SphereIntersectorReflector;
import com.software.reuze.gb_Vector3;
import com.software.reuze.m_MathUtils;

import junit.framework.TestCase;

public class SphereTest extends TestCase {

	/**
     * Earth's mean radius in km
     * (http://en.wikipedia.org/wiki/Earth_radius#Mean_radii)
     */
    public static final float EARTH_RADIUS = (float) ((2 * 6378.1370 + 6356.752314245) / 3.0);
    
    public void testIsInSphere() {
        gb_Vector3 p = new gb_Vector3(0, -10, 0);
        gb_Sphere s = new gb_Sphere(new gb_Vector3(), 10);
        assertEquals(s.contains(p), true);
        p.set(0, 10.1f, 0);
        assertEquals(s.contains(p), false);
    }

    public void testReflectRay() {
        gb_SphereIntersectorReflector si = new gb_SphereIntersectorReflector(
                new gb_Vector3(0, 0, 0), 10);
        gb_Ray r = si.reflectRay(new gb_Ray(new gb_Vector3(100, 100, 0), new gb_Vector3(-1,
                -1, 0)));
        float absDiff = r.getDirection().angleBetween(new gb_Vector3(1, 1, 0), true);
        System.out.println(r + " diff: " + absDiff);
        assertEquals(absDiff < 0.002, true);
    }

    public void testSurfaceDistance() {
        ga_Vector2 p = new ga_Vector2(90, 60).mul(m_MathUtils.DEG2RAD);
        ga_Vector2 q = new ga_Vector2(90, 61).mul(m_MathUtils.DEG2RAD);
        gb_Sphere e = new gb_Sphere(EARTH_RADIUS);
        double dist = (float) e.surfaceDistanceBetween(p, q);
        assertTrue(m_MathUtils.abs(dist - 111.1952) < 0.1);
    }
}
