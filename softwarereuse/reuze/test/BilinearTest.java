package reuze.test;
import com.software.reuze.ga_Vector2;
import com.software.reuze.m_InterpolatePointBilinear;

import junit.framework.TestCase;

public class BilinearTest extends TestCase {

    public void testBilinear() {
        ga_Vector2 p = new ga_Vector2();
        ga_Vector2 q = new ga_Vector2(100, 100);
        float val;
        m_InterpolatePointBilinear ipb=new m_InterpolatePointBilinear();
        val = ipb.bilinear(new ga_Vector2(10, 0), p, q, 100, 200, 200,
                100);
        assertEquals(110f, val);
        val = ipb.bilinear(new ga_Vector2(50, 0), p, q, 100, 200, 200,
                100);
        assertEquals(150f, val);
        val = ipb.bilinear(new ga_Vector2(90, 10), p, q, 100, 200, 200,
                100);
        assertEquals(182f, val);
        val = ipb.bilinear(new ga_Vector2(90, 100), p, q, 100, 200, 200,
                100);
        assertEquals(110f, val);
        val = ipb.bilinear(10, 10, 0, 0, 100, 100, 100, 200, 200,
                100);
        assertEquals(118f, val);
    }
}
