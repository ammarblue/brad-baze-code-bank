package reuze.test;
//package aima.test.core.unit.learning.inductive;

/*import aima.core.learning.framework.Attribute;
import aima.core.learning.framework.DataSet;
import aima.core.learning.framework.Example;
import aima.core.learning.inductive.DecisionTree;
import aima.core.learning.inductive.DecisionTreeLeaf;
import aima.test.core.unit.learning.framework.DataSetTest;*/
import java.io.IOException;
import java.util.HashMap;
import org.junit.*;

import com.software.reuze.al_DecisionTreeBoolean;
import com.software.reuze.al_DecisionTreeBooleanLeaf;
import com.software.reuze.al_Example;
import com.software.reuze.al_Examples;
import com.software.reuze.d_PropertyListPair;

import static org.junit.Assert.*;

/**
 * DecisionTreeTest
 *
 * @author Andrew Brown
 */
public class DecisionTreeTest {

    @Before
    public void setUp() {
    }

    /**
     * Test predictions
     */
    @Test
    public void testPredict() {
        // get restaurant data
        al_Examples restaurantData = null;
        try {
            restaurantData = DataSetTest.loadRestaurantData();
        } catch (IOException e) {
            Assert.fail("Could not load restaurant data from URL.");
        }
        // create tree
        al_DecisionTreeBoolean t = new al_DecisionTreeBoolean<String>(new d_PropertyListPair<String>("Price", null));
        t.addBranch("$", new al_DecisionTreeBooleanLeaf("No"));
        t.addLeaf("$$", "Yes"); // alternate form, preferred
        t.addLeaf("$$$", "Maybe");
        // test
        Assert.assertEquals("No", t.predict(restaurantData .getExample(1)));
        Assert.assertEquals("Yes", t.predict(restaurantData .getExample(5)));
        Assert.assertEquals("Maybe", t.predict(restaurantData .getExample(0)));
        // test tree coverage
        for (al_Example e : restaurantData ) {
            Assert.assertNotNull(t.predict(e));
        }
    }

    @Test(expected = RuntimeException.class)
    public void testPredictWithDepth() throws Exception {
        // get restaurant data
        al_Examples restaurant = DataSetTest.loadRestaurantData();
        // create tree
        al_DecisionTreeBoolean t1 = new al_DecisionTreeBoolean<String>(new d_PropertyListPair<String>("Price", null));
        al_DecisionTreeBoolean t2 = new al_DecisionTreeBoolean<String>(new d_PropertyListPair<String>("Est", null));
        al_DecisionTreeBoolean t3 = new al_DecisionTreeBoolean<String>(new d_PropertyListPair<String>("Type", null));
        t1.addBranch("$$$", t2);
        t2.addBranch("0-10", t3);
        t3.addLeaf("French", "Yes");
        // test
        Assert.assertEquals("Yes", t1.predict(restaurant.getExample(0)));
        Assert.assertEquals(null, t1.predict(restaurant.getExample(4))); // should throw RuntimeException since no branch is set for ">60"
    }

    /**
     * Demonstrate toString() usage
     */
    @Test
    public void testToString() {
        // create tree
        al_DecisionTreeBoolean t1 = new al_DecisionTreeBoolean<String>(new d_PropertyListPair<String>("Price", null));
        al_DecisionTreeBoolean t2 = new al_DecisionTreeBoolean<String>(new d_PropertyListPair<String>("Est", null));
        al_DecisionTreeBoolean t3 = new al_DecisionTreeBoolean<String>(new d_PropertyListPair<String>("Type", null));
        t1.addBranch("$$$", t2);
        t2.addBranch("0-10", t3);
        t2.addLeaf(">60", "No");
        t3.addLeaf("French", "Yes");
        // test
        Assert.assertEquals(
                "[Price]" + "\n"
                + "  |- $$$ -> " + "\n"
                + "  [Est]" + "\n"
                + "    |- 0-10 -> " + "\n"
                + "    [Type]" + "\n"
                + "      |- French -> Yes" + "\n"
                + "    |- >60 -> No" + "\n",
                t1.toString());
    }
}
