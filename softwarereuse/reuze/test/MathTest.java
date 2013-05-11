package reuze.test;
import com.software.reuze.m_MathUtils;

import junit.framework.TestCase;

public class MathTest extends TestCase {

	public void testFastCos() {
        float maxErr = Float.MIN_VALUE;
        for (float i = 0; i <= 8 * 360; i++) {
            float theta = i * m_MathUtils.DEG2RAD * 0.25f;
            float sin = (float) Math.cos(theta);
            float fs = (float)m_MathUtils.cos((float)theta);
            float err = (fs - sin);
            maxErr = m_MathUtils.max(m_MathUtils.abs(err), maxErr);
            //System.out.println(i + ": sin=" + sin + " fastsin=" + fs + " err="+ err);
        }
        System.out.println("cos max err: " + maxErr);
    }
	
	public void testFastSin() {
        float maxErr = Float.MIN_VALUE;
        for (float i = 0; i <= 8 * 360; i++) {
            float theta = i * m_MathUtils.DEG2RAD * 0.25f;
            float sin = (float) Math.cos(theta);
            float fs = m_MathUtils.cos(theta);
            float err = (fs - sin);
            maxErr = m_MathUtils.max(m_MathUtils.abs(err), maxErr);
            System.out.println(i + ": sin=" + sin + " fastsin=" + fs + " err="
                    + err);
        }
        System.out.println("sin max err: " + maxErr);
    }
}
