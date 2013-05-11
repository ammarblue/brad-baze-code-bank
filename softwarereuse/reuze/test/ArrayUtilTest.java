package reuze.test;


import java.util.Random;

import com.software.reuze.d_ArrayUtils;
import com.software.reuze.d_SetGeneric;
import com.software.reuze.m_RangeInteger;

import junit.framework.TestCase;

public class ArrayUtilTest extends TestCase {

    private void dumpArray(Integer[] range) {
        for (int i : range) {
            System.out.print(i + ",");
        }
        System.out.println("");
    }

    public void testGenericSet() {
        d_SetGeneric<Integer> set = new d_SetGeneric<Integer>(1, 2, 23, 42, 81);
        assertEquals(5, set.getItems().size());
        int prev = 0;
        for (int i = 0; i < set.size(); i++) {
            int val = set.pickRandomUnique();
            assertTrue(val != prev);
            prev = val;
        }
    }

    public void testShuffle() {
        Integer[] range = new m_RangeInteger(0, 10).toArray();
        dumpArray(range);
        d_ArrayUtils.shuffle(range, new Random(23));
        dumpArray(range);
    }
}
