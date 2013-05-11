package reuze.test;
//package aima.test.core.unit.learning.inductive;

/*import aima.core.learning.framework.Attribute;
import aima.core.learning.framework.DataSet;
import aima.core.learning.framework.Example;
import aima.core.learning.inductive.DecisionList;
import aima.test.core.unit.learning.framework.DataSetTest;*/
import java.io.IOException;
import org.junit.Assert;
import org.junit.Test;

import com.software.reuze.al_DecisionListTest;
import com.software.reuze.al_DecisionListTests;
import com.software.reuze.al_Example;
import com.software.reuze.al_Examples;
import com.software.reuze.d_PropertyListPair;


/**
 * DecisionListTest
 * @author Andrew Brown
 */
public class DecisionListTests {

    /**
     * Test default prediction; should be null
     */
    @Test
    public void testDefaultPrediction() {
        al_DecisionListTests dl = new al_DecisionListTests();
        al_Example<Integer> e = new al_Example();
        e.add(new d_PropertyListPair<Integer>("Test", 10));
        e.setOutput(20);
        // test
        Assert.assertEquals(null, dl.predict(e));
    }

    /**
     * Test predict(); a test will return it's output value if it matches the
     * example; null otherwise
     */
    @Test
    public void testPredict() {
        // get restaurant data
        al_Examples restaurantData = null;
        try{
            restaurantData = DataSetTest.loadRestaurantData();
        }
        catch(IOException e){
            Assert.fail("Could not load restaurant data from URL.");
        }
        // create decision list test on two attributes: Price and Est
        al_DecisionListTest<String> t = new al_DecisionListTest();
        t.add(new d_PropertyListPair<String>("Price", "$$$"));
        t.add(new d_PropertyListPair<String>("Est", ">60"));
        t.setOutput("No");
        // create decision list
        al_DecisionListTests dl = new al_DecisionListTests();
        dl.add(t);
        // test fall-through
        Assert.assertEquals(null, dl.predict(restaurantData.getExample(0)));
        // test matching
        Assert.assertEquals("No", dl.predict(restaurantData.getExample(4)));
    }
    
    /**
     * Demonstrate use of mergeWith()
     */
    @Test
    public void testMergeWith() {
        // create decision list tests
        al_DecisionListTest<String> t1 = new al_DecisionListTest();
        al_DecisionListTest<String> t2 = new al_DecisionListTest();
        al_DecisionListTest<String> t3 = new al_DecisionListTest();
        // create decision lists
        al_DecisionListTests dl1 = new al_DecisionListTests();
        dl1.add(t1);
        al_DecisionListTests dl2 = new al_DecisionListTests();
        dl2.add(t2);
        al_DecisionListTests dl3 = new al_DecisionListTests();
        dl3.add(t3);
        // merge
        al_DecisionListTests merged = dl1.mergeWith(dl2.mergeWith(dl3));
        Assert.assertEquals(3, merged.size());
    }

    /**
     * Demonstrate use of toString()
     */
    @Test
    public void testToString() {
        // create decision list test on two attributes: Price and Est
        al_DecisionListTest<String> t = new al_DecisionListTest();
        t.add(new d_PropertyListPair<String>("Price", "$$$"));
        t.add(new d_PropertyListPair<String>("Est", ">60"));
        t.setOutput("No");
        // create decision list
        al_DecisionListTests dl = new al_DecisionListTests();
        dl.add(t);
        // test
        Assert.assertEquals("IF [Price=$$$ AND Est=>60] THEN No ELSE null", dl.toString());
    }
}
