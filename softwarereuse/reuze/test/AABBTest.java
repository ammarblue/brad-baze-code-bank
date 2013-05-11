package reuze.test;


import com.software.reuze.gb_AABB3;
import com.software.reuze.gb_Ray;
import com.software.reuze.gb_Sphere;
import com.software.reuze.gb_Triangle;
import com.software.reuze.gb_Vector3;

import junit.framework.TestCase;

public class AABBTest extends TestCase {

    public void testAABB2AABB() {
        gb_AABB3 box = new gb_AABB3(new gb_Vector3(100, 0, 0), new gb_Vector3(20, 20, 20));
        gb_AABB3 b2 = new gb_AABB3(new gb_Vector3(100, 30.1f, 0), new gb_Vector3(10, 10, 10));
        assertEquals(box.intersectsBox(b2), false);
    }

    public void testAABBNormal() {
        gb_AABB3 box = new gb_AABB3(new gb_Vector3(100, 100, 100), new gb_Vector3(100, 100, 100));
        gb_Vector3 p = new gb_Vector3(100, 300, 100);
        assertEquals(gb_Vector3.Y, box.getNormalForPoint(p));
        p.set(100, -300, 100);
        assertEquals(gb_Vector3.Y.cpy().inv(), box.getNormalForPoint(p));
        p.set(300, 100, 100);
        assertEquals(gb_Vector3.X, box.getNormalForPoint(p));
        p.set(-300, 100, 100);
        assertEquals(gb_Vector3.X.cpy().inv(), box.getNormalForPoint(p));
        p.set(100, 100, 300);
        assertEquals(gb_Vector3.Z, box.getNormalForPoint(p));
        p.set(100, 100, -300);
        assertEquals(gb_Vector3.Z.cpy().inv(), box.getNormalForPoint(p));
    }

    public void testAABBRayIntersect() {
        gb_AABB3 box = gb_AABB3.fromMinMax(new gb_Vector3(), new gb_Vector3(100, 100, 100));
        gb_Ray r = new gb_Ray(new gb_Vector3(50, 10, 10), new gb_Vector3(0, 1, 0));
        assertEquals(box.intersectsRay(r, -1000, 1000), new gb_Vector3(50,0,10));
    }

    public void testAABBSphere() {
        gb_AABB3 box = new gb_AABB3(new gb_Vector3(100, 0, 0), new gb_Vector3(20, 20, 20));
        gb_Sphere s = new gb_Sphere(new gb_Vector3(100, 0, 0), 50);
        assertEquals(box.intersectsSphere(s), true);
    }

    public void testAABBTri() {
        gb_AABB3 box = new gb_AABB3(new gb_Vector3(), new gb_Vector3(100, 100, 100));
        gb_Vector3 a = new gb_Vector3(-90, 0, 0);
        gb_Vector3 b = new gb_Vector3(-110, -200, 0);
        gb_Vector3 c = new gb_Vector3(-110, 200, 0);
        gb_Triangle tri = new gb_Triangle(a, b, c);
        assertTrue(box.intersectsTriangle(tri));
    }

    public void testInclude() {
        gb_AABB3 box = gb_AABB3.fromMinMax(new gb_Vector3(), new gb_Vector3(100, 100, 100));
        System.out.println(box);
        gb_Vector3 p = new gb_Vector3(-150, -50, 110);
        box.includePoint(p);
        System.out.println(box);
        System.out.println(box.getMin() + "   " + box.getMax());
        //<aabb> pos: {x:50.0, y:50.0, z:50.0} ext: {x:50.0, y:50.0, z:50.0}
        //{x:-25.0, y:25.0, z:55.0} {x:125.0, y:75.0, z:55.0}
        assertTrue(box.contains(p));
    }

    public void testIsec() {
        gb_AABB3 box = gb_AABB3.fromMinMax(new gb_Vector3(), new gb_Vector3(100, 100, 100));
        gb_AABB3 box2 = gb_AABB3.fromMinMax(new gb_Vector3(10, 10, 10),
                new gb_Vector3(80, 80, 80));
        assertTrue(box.intersectsBox(box2));
        assertTrue(box2.intersectsBox(box));
    }

    public void testIsInAABB() {
        gb_AABB3 box = new gb_AABB3(new gb_Vector3(100, 0, 0), new gb_Vector3(20, 20, 20));
        gb_Vector3 p = new gb_Vector3(80, -19.99f, 0);
        assertEquals(box.isInAABB(p), true);
        assertEquals(box.isInAABB(new gb_Vector3(120.01f, 19.99f, 0)), false);
    }
}
