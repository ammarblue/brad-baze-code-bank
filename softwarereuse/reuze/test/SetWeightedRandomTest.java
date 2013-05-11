package reuze.test;


import java.util.HashMap;

import com.software.reuze.d_SetEntryInt;
import com.software.reuze.d_SetWeightedRandom;

import junit.framework.TestCase;

public class SetWeightedRandomTest extends TestCase {

    private void checkDistribution(d_SetWeightedRandom<String> set) {
        HashMap<String, Integer> stats = new HashMap<String, Integer>();
        for (int i = 0; i < 100000; i++) {
            String id = set.getRandom();
            if (stats.get(id) == null) {
                stats.put(id, 1);
            } else {
                stats.put(id, stats.get(id) + 1);
            }
        }
        for (String id : stats.keySet()) {
            System.out.println(id + ": " + stats.get(id));
        }
    }

    public void testEmpty() {
        d_SetWeightedRandom<String> set = new d_SetWeightedRandom<String>();
        assertEquals(null, set.getRandom());
    }

    public void testOrder() {
        d_SetWeightedRandom<String> set = new d_SetWeightedRandom<String>();
        set.add("bar", 2);
        set.add("foo", 1);
        set.add("toxi", 4);
        set.add("bollox", 1);
        int i = 0;
        for (d_SetEntryInt<String> e : set.getElements()) {
            System.out.println(i + ":" + e);
            i++;
        }
    }

    public void testRandom() {
        d_SetWeightedRandom<String> set = new d_SetWeightedRandom<String>();
        set.add("bar", 2);
        set.add("toxi", 4);
        set.add("foo", 1);
        set.add("bollox", 1);
        checkDistribution(set);
    }

    public void testRemove() {
        d_SetWeightedRandom<String> set = new d_SetWeightedRandom<String>();
        set.add("foo", 2);
        set.remove("foo");
        assertEquals(0, set.getElements().size());
    }

    public void testSingle() {
        d_SetWeightedRandom<String> set = new d_SetWeightedRandom<String>();
        set.add("foo", 10);
        assertEquals("foo", set.getRandom());
    }
}
