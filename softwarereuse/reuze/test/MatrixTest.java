package reuze.test;
import com.software.reuze.gb_Vector3;
import com.software.reuze.m_MathUtils;
import com.software.reuze.m_Matrix4;

import junit.framework.TestCase;

public class MatrixTest extends TestCase {

    public void testInverse() {
        m_Matrix4 m = new m_Matrix4();
        m.translate(100, 100, 0);
        m.rotateX(m_MathUtils.HALF_PI);
        m.scale(10, 10, 10);
        System.out.println(m);
        gb_Vector3 v = new gb_Vector3(0, 1, 0);
        gb_Vector3 w = m.applyTo(v);
        m = m.inv();
        gb_Vector3 v2 = m.applyTo(w);
        System.out.println(w);
        System.out.println(v2);
        assertTrue(v2.equalsWithTolerance(v, 0.0001f));
    }

    public void testRotate() {
        m_Matrix4 m = new m_Matrix4();
        m.rotateX(m_MathUtils.HALF_PI);
        gb_Vector3 v = m.applyTo(new gb_Vector3(0, 1, 0));
        assertTrue(new gb_Vector3(0, 0, 1).equalsWithTolerance(v, 0.00001f));
        m.idt();
        m.rotateY(m_MathUtils.HALF_PI);
        v = m.applyTo(new gb_Vector3(1, 0, 0));
        assertTrue(new gb_Vector3(0, 0, -1).equalsWithTolerance(v, 0.00001f));
        m.idt();
        m.rotateZ(m_MathUtils.HALF_PI);
        v = m.applyTo(new gb_Vector3(1, 0, 0));
        assertTrue(new gb_Vector3(0, 1, 0).equalsWithTolerance(v, 0.00001f));
        m.idt();
        m.rotateAroundAxis(new gb_Vector3(0, 1, 0), m_MathUtils.HALF_PI);
        v = m.applyTo(new gb_Vector3(1, 0, 0));
        assertTrue(new gb_Vector3(0, 0, 1).equalsWithTolerance(v, 0.00001f));
    }

    public void testTranslate() {
        m_Matrix4 m = new m_Matrix4();
        m.translate(100, 100, 100);
        assertEquals(new gb_Vector3(100, 100, 100), m.applyTo(new gb_Vector3()));
    }
}