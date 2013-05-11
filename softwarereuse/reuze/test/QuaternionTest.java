package reuze.test;


import com.software.reuze.gb_Quaternion;
import com.software.reuze.gb_Vector3;
import com.software.reuze.m_MathUtils;

import junit.framework.TestCase;

public class QuaternionTest extends TestCase {

    public void testCreateFromAxisAngle() {
        gb_Vector3 axis = new gb_Vector3(100, 100, 100);
        float angle = m_MathUtils.PI * 1.5f;
        gb_Quaternion a = gb_Quaternion.createFromAxisAngle(axis, angle);
        assertTrue(Math.sin(-m_MathUtils.QUARTER_PI)-a.w<=1.e-3);
        float[] reverse = a.toAxisAngle();
        gb_Vector3 revAxis = new gb_Vector3(reverse[1], reverse[2], reverse[3]);
        assertTrue(axis.nor().equalsWithTolerance(revAxis, 0.01f));
        assertTrue(m_MathUtils.abs(angle - reverse[0]) < 0.01);
    }

    public void testEuler() {
        gb_Quaternion q = gb_Quaternion.createFromEuler(m_MathUtils.QUARTER_PI,
                m_MathUtils.QUARTER_PI, 0);
        System.out.println(q);
        float[] reverse = q.toAxisAngle();
        System.out.println("toAxisAngle():");
        for (float f : reverse) {
            System.out.println(f);
        }
    }

    public void testSlerp() {
        gb_Quaternion a = new gb_Quaternion(0, new gb_Vector3(0, 0, -1));
        gb_Quaternion b = new gb_Quaternion(0, new gb_Vector3(0, 0, 1));
        gb_Quaternion c = a.interpolateTo(b, 0.05f);
        System.out.println(c);
    }
}
