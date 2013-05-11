package reuze.test;


import com.software.reuze.ga_Rectangle;

import junit.framework.TestCase;

public class RectangleTest extends TestCase {

    public void testIntersection() {
        ga_Rectangle a = new ga_Rectangle(100, 100, 100, 100);
        ga_Rectangle b = new ga_Rectangle(80, 80, 100, 100);
        ga_Rectangle i = a.intersect(b);
        assertEquals(new ga_Rectangle(100, 100, 80, 80), i);
        b = new ga_Rectangle(80, 80, 20, 20);
        i = a.intersect(b);
        assertTrue(i.getPerimeter()==0);
        b.width = 10;
        i = a.intersect(b);
        assertNull(i);
        b = new ga_Rectangle(180, 180, 30, 50);
        i = a.intersect(b);
        assertEquals(new ga_Rectangle(180, 180, 20, 20), i);
    }

    public void testRectMerge() {
        ga_Rectangle r = new ga_Rectangle(-10, 2, 3, 3);
        ga_Rectangle s = new ga_Rectangle(-8, 4, 5, 3);
        r = r.union(s);
        assertEquals(new ga_Rectangle(-10, 2, 7, 5), r);
        r = new ga_Rectangle(0, 0, 3, 3);
        s = new ga_Rectangle(-1, 2, 1, 1);
        r = r.union(s);
        assertEquals(new ga_Rectangle(-1, 0, 4, 3), r);
    }
}
