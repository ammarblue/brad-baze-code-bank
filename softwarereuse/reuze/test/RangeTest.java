package reuze.test;


import com.software.reuze.m_RangeFloat;
import com.software.reuze.m_RangeFloatBiased;
import com.software.reuze.m_RangeIntegerBiased;

import junit.framework.TestCase;

public class RangeTest extends TestCase {

    private void dumpArray(Float[] range) {
        for (float i : range) {
            System.out.print(i + ",");
        }
        System.out.println("");
    }

    public void testCopy() {
        m_RangeFloatBiased r = new m_RangeFloatBiased();
        r.pickRandom();
        m_RangeFloatBiased c = r.copy();
        assertEquals(r.currValue, c.currValue);
        assertEquals(r.getBias(), c.getBias());
        assertEquals(r.getStandardDeviation(), c.getStandardDeviation());
        m_RangeIntegerBiased ri = new m_RangeIntegerBiased();
        ri.pickRandom();
        m_RangeIntegerBiased ci = ri.copy();
        assertEquals(ri.currValue, ci.currValue);
        assertEquals(ri.getBias(), ci.getBias());
        assertEquals(ri.getStandardDeviation(), ci.getStandardDeviation());
    }

    public void testRangeArray() {
        Float[] r = new m_RangeFloat(0, 10).toArray(0.1f);
        dumpArray(r);
    }
}
