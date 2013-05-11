package reuze.test;


import com.software.reuze.d_ArrayUtils;
import com.software.reuze.d_PropertiesTyped;
import com.software.reuze.d_SetGeneric;

import junit.framework.TestCase;

public class PrefUtilsTest extends TestCase {

    d_PropertiesTyped config = new d_PropertiesTyped();

    protected void setUp() throws Exception {
        assertTrue(config.load("data/test.properties"));
    }

    protected void tearDown() throws Exception {
        config = null;
    }

    public void testIntArray() {
        int[] items = config.getIntArray("test.intarray");
        assertEquals(23, items[0]);
        assertEquals(42, items[1]);
        assertEquals(88, items[2]);
        assertEquals(-12, items[3]);
    }

    public void testFloatArray() {
        float[] items = config.getFloatArray("test.floatarray");
        assertEquals(3.1415926f, items[0], 0.001);
        // 2nd item is NaN, so should be ignored
        // and 23.42 becomes 2nd array item
        assertEquals(23.42f, items[1], 0.001);
    }

    public void testEmptyArray() {
        int[] items = config.getIntArray("test.emptyarray");
        assertEquals(0, items.length);
        float[] fitems = config.getFloatArray("test.emptyarray");
        assertEquals(0, fitems.length);
        byte[] bitems = config.getByteArray("test.emptyarray");
        assertEquals(0, bitems.length);
        String[] sitems = config.getStringArray("test.emptyarray");
        assertEquals(0, sitems.length);
    }

    public void testArrayReverse() {
        byte[] array = new byte[] { 1, 2, 3, 4, 5 };
        d_ArrayUtils.reverse(array);

    }

    public void testStringArray() {
        String[] items = config.getStringArray("test.stringarray");
        assertEquals(3, items.length);
        assertEquals(true, items[1].equalsIgnoreCase("world"));
    }

    public void testObjectSet() {
        d_SetGeneric<String> strings = new d_SetGeneric<String>("Hello");
        String s = strings.pickRandom();
        assertEquals(s, "Hello");
        assertTrue(strings.contains("Hello"));
    }
}
