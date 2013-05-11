package reuze.test;
//package aima.test.core.unit.learning.framework;

//import aima.core.learning.framework.Attribute;
//import aima.core.learning.framework.Example;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.software.reuze.al_Example;
import com.software.reuze.d_PropertyListPair;


/**
 * ExampleTest
 * @author Andrew Brown
 */
public class ExampleTest {

    al_Example<Boolean> example1;
    al_Example<Integer> example2;

    @Before
    public void setUp() {
        // horse example
        this.example1 = new al_Example();
        this.example1.setOutput(true);
        this.example1.add(new d_PropertyListPair<Boolean>("Four-legged", true));
        this.example1.add(new d_PropertyListPair<Boolean>("Hairy", true));
        this.example1.add(new d_PropertyListPair<Boolean>("Gallops", true));
        // addition example
        this.example2 = new al_Example();
        this.example2.setOutput(3);
        this.example2.add(new d_PropertyListPair<Integer>("First-operand", 1));
        this.example2.add(new d_PropertyListPair<String>("Operator", "+"));
        this.example2.add(new d_PropertyListPair<Integer>("Second-operand", 2));
    }

    /**
     * Demonstrate basic usage of Example
     */
    @Test
    public void testBasics() {
        Assert.assertEquals(true, this.example1.get("Hairy").getValue());
        Assert.assertEquals("Operator", this.example2.getAttributes()[1].getName());
        Assert.assertTrue(3 == this.example2.getOutput());
    }

    /**
     * Test equals()
     */
    @Test
    public void testEquals() {
        // not a horse
        al_Example<Boolean> not_horse = new al_Example();
        not_horse.setOutput(false);
        not_horse.add(new d_PropertyListPair<Boolean>("Four-legged", true));
        not_horse.add(new d_PropertyListPair<Boolean>("Hairy", true));
        not_horse.add(new d_PropertyListPair<Boolean>("Gallops", false));
        Assert.assertNotSame(not_horse, this.example1);
        //is a horse
        al_Example<Boolean> is_horse = new al_Example();
        is_horse.setOutput(true);
        is_horse.add(new d_PropertyListPair<Boolean>("Four-legged", true));
        is_horse.add(new d_PropertyListPair<Boolean>("Hairy", true));
        is_horse.add(new d_PropertyListPair<Boolean>("Gallops", true));
        Assert.assertEquals(is_horse, this.example1);

    }
}
