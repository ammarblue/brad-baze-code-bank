package reuze.test;
//package aima.test.core.unit.learning.framework;

/*import aima.core.learning.framework.Attribute;
import aima.core.learning.framework.DataSet;
import aima.core.learning.framework.Example;
import aima.core.util.Util;*/
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.software.reuze.al_Example;
import com.software.reuze.al_Examples;
import com.software.reuze.d_PropertyListPair;
import com.software.reuze.m_MathUtil2;


/**
 * DataSetTest
 *
 * @author Ravi Mohan
 * @author Andrew Brown
 */
public class DataSetTest {

    al_Examples examples;

    /**
     * Setup a 50-count example set, each of the form: { "String": "...",
     * "Number": 0.28489 (random), "Boolean": true (random), "Count": 17 (i) }
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        // sample data set creation
        this.examples = new al_Examples();
        for (int i = 0; i < 50; i++) {
            al_Example<Double> example = new al_Example<Double>();
            // create attributes
            d_PropertyListPair<String> string = new d_PropertyListPair<String>("String", "...");
            example.add(string);
            d_PropertyListPair<Double> number = new d_PropertyListPair<Double>("Number", Math.random());
            example.add(number);
            d_PropertyListPair<Boolean> bool = new d_PropertyListPair<Boolean>("Boolean", (Math.random() > 0.5));
            example.add(bool);
            d_PropertyListPair<Integer> count = new d_PropertyListPair<Integer>("Count", i);
            example.add(count);
            // create output
            example.setOutput((Double) Math.random());
            // add to set
            this.examples.add(example);
        }
    }

    /**
     * Demonstrate how to find sizes of examples
     */
    @Test
    public void testSize() {
        // set size
        Assert.assertEquals(50, this.examples.size());
        // example size
        Assert.assertEquals(4, this.examples.getExample(0).getAttributes().length);
    }

    /**
     * Demonstrate how to retrieve examples and attributes
     */
    @Test
    public void testGet() {
        // get attribute types
        d_PropertyListPair<String> a = this.examples.getExample(29).get("String"); // <Type> required to access generic object
        Assert.assertEquals("...", a.getValue());
        // get output
        al_Example<Double> e = this.examples.getExample(19); // <Type> required to access generic object
        Assert.assertNotNull(e.getOutput());
    }

    /**
     * Demonstrate getPossibleAttributes() usage; the method should create an
     * entry for every (name, value) combination.
     */
    @Test
    public void testGetPossibleAttributes() {
        // predicted count for each attribute
        int strings = this.examples.getValuesOf("String").size(); // = 1
        int number = this.examples.getValuesOf("Number").size(); // = 50
        int bool = this.examples.getValuesOf("Boolean").size(); // = 2
        int count = this.examples.getValuesOf("Count").size(); // = 50
        // test
        int expected = strings + number + bool + count;
        int actual = this.examples.getPossibleAttributes().size();
        Assert.assertEquals(expected, actual);
    }

    /**
     * Demonstrate usage of getValuesOf(); we know that the "String" attribute
     * should only have one distinct value, see setUp()
     */
    @Test
    public void testGetValuesOf() {
        int expected = 1;
        int actual = this.examples.getValuesOf("String").size();
        Assert.assertEquals(expected, actual);
    }

    /**
     * Demonstrate usage of splitBy(); should split the set by attribute value.
     * Tested by adding up the sizes of each value-set and comparing with the
     * total
     */
    @Test
    public void testSplitBy() {
        int expected = this.examples.size();
        // split
        HashMap<Boolean, al_Examples> attributeValues = this.examples.splitBy("Boolean");
        // add up each split set
        int actual = 0;
        for (Boolean b : attributeValues.keySet()) {
            actual += attributeValues.get(b).size();
        }
        // test
        Assert.assertEquals(expected, actual);
    }

    /**
     * Demonstrate find() usage
     */
    @Test
    public void testFind() {
        // look for "Boolean" false
        int expected = 0;
        for (al_Example e : this.examples) {
            if (e.get("Boolean").getValue().equals(false)) {
                expected++;
            }
        }
        // test
        int actual = this.examples.find("Boolean", false).size();
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test whether entropy is calculated correctly
     */
    @Test
    public void testEntropy() {
        // test entropy of "String"
        double expected = 0.0;
        double actual = this.examples.getEntropyOf("String");
        Assert.assertEquals(expected, actual, 0.0001);
        // test entropy of "Boolean"
        expected = 1.0; // should be close to 1.0 
        actual = this.examples.getEntropyOf("Boolean");
        Assert.assertEquals(expected, actual, 0.1);
    }

    @Test
    public void testInformationGain() {
        // @todo
    }

    @Test
    public void testLoadFrom() {
        try {
            // load restaurant data
            al_Examples restaurants = DataSetTest.loadRestaurantData();
            // test
            Assert.assertEquals("No", restaurants.getExample(1).getOutput());
            Assert.assertEquals("Burger", restaurants.getExample(11).get("Type").getValue());

        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail("Failed to retrieve data: " + e.getMessage());
        }
    }

    /**
     * Check mean and standard deviation with data at
     * http://aima-data.googlecode.com/svn/trunk/iris.txt
     */
    @Test
    public void testDeviationAndMean() {
        try {
            // prepare
            al_Examples iris = DataSetTest.loadIrisData();
            ArrayList<Double> sepalLengths = new ArrayList<Double>();
            for (al_Example e : iris) {
                d_PropertyListPair<Double> a = e.get("sepal-length");
                sepalLengths.add(a.getValue());
            }
            double sepalLengthMean = m_MathUtil2.mean(sepalLengths);
            double sepalLengthDeviation = m_MathUtil2.stdDev(sepalLengths, sepalLengthMean);
            // test
            Assert.assertEquals(5.84, sepalLengthMean, 0.01);
            Assert.assertEquals(0.83, sepalLengthDeviation, 0.01);
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail("Failed to retrieve data: " + e.getMessage());
        }
    }

    /**
     * Demonstrate loading an example set from a URL; uses the trunk version of
     * aima-data found at
     * http://aima-data.googlecode.com/svn/trunk/restaurant.csv.
     */
    public static al_Examples loadRestaurantData() throws IOException {
        // create restaurant sample; used example[0] from page 700, AIMAv3
        al_Example<String> sample = new al_Example();
        sample.add(new d_PropertyListPair<String>("Alt", "Yes"));
        sample.add(new d_PropertyListPair<String>("Bar", "No"));
        sample.add(new d_PropertyListPair<String>("Fri", "No"));
        sample.add(new d_PropertyListPair<String>("Hun", "Yes"));
        sample.add(new d_PropertyListPair<String>("Pat", "Some"));
        sample.add(new d_PropertyListPair<String>("Price", "$$$"));
        sample.add(new d_PropertyListPair<String>("Rain", "No"));
        sample.add(new d_PropertyListPair<String>("Res", "Yes"));
        sample.add(new d_PropertyListPair<String>("Type", "French"));
        sample.add(new d_PropertyListPair<String>("Est", "0-10"));
        sample.setOutput("Yes");
        // load restaurant data
        URL url = new URL("http://aima-data.googlecode.com/svn/trunk/restaurant.csv");
        al_Examples restaurants = al_Examples.loadFrom(url, ",", sample);
        // return
        return restaurants;
    }

    /**
     * Demonstrate loading an example set from a URL; uses the trunk version of
     * aima-data found at http://aima-data.googlecode.com/svn/trunk/iris.csv.
     */
    public static al_Examples loadIrisData() throws IOException {
        // create iris sample
        al_Example<String> sample = new al_Example();
        sample.add(new d_PropertyListPair<Double>("sepal-length", 0.0));
        sample.add(new d_PropertyListPair<Double>("sepal-width", 0.0));
        sample.add(new d_PropertyListPair<Double>("petal-length", 0.0));
        sample.add(new d_PropertyListPair<Double>("petal-width", 0.0));
        sample.setOutput("...");
        // load iris data
        URL url = new URL("http://aima-data.googlecode.com/svn/trunk/iris.csv");
        al_Examples iris = al_Examples.loadFrom(url, ",", sample);
        // return
        return iris;
    }
}
