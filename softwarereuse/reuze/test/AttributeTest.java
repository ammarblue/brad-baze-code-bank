package reuze.test;
//package aima.test.core.unit.learning.framework;

//import aima.core.learning.framework.Attribute;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.software.reuze.d_PropertyListPair;


/**
 * AttributeTest
 * @author Andrew Brown
 */
public class AttributeTest {

    d_PropertyListPair<String> attribute1;
    d_PropertyListPair<Boolean> attribute2;
    d_PropertyListPair<Double[]> attribute3;

    @Before
    public void setUp() {
        this.attribute1 = new d_PropertyListPair("Height", "72 inches");
        this.attribute2 = new d_PropertyListPair("WillRain?", false);
        this.attribute3 = new d_PropertyListPair("List", new Double[]{0.1, 0.34, 0.5});
    }

    /**
     * Demonstrate usage of basic methods
     */
    @Test
    public void testBasics() {
        int expected = 837;
        d_PropertyListPair<Integer> number = new d_PropertyListPair("Random", 0);
        // set value from string
        number.setValue("837");
        Assert.assertTrue(number.getValue() == expected);
        // set name
        number.setName("Non-random");
        Assert.assertEquals("Non-random", number.getName());
    }
    
    /**
     * Demonstrate isValid() usage
     */
    @Test
    public void testIsValid(){
        Assert.assertEquals(false, this.attribute1.isValid(null));
        Assert.assertEquals(true, this.attribute1.isValid("..."));
    }

    /**
     * Test equals()
     */
    @Test
    public void testEquals() {
        d_PropertyListPair<String> same = new d_PropertyListPair<String>("Height", "72 inches");
        Assert.assertTrue(this.attribute1.equals(same));
        d_PropertyListPair<String> not_same = new d_PropertyListPair<String>("Length", "72 inches");
        Assert.assertTrue(!this.attribute1.equals(not_same));
        d_PropertyListPair<String> not_same2 = new d_PropertyListPair<String>("Height", "235 inches");
        Assert.assertTrue(!this.attribute1.equals(not_same2));
    }
}
