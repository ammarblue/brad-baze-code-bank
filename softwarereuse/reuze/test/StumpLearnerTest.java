package reuze.test;
//package aima.test.core.unit.learning.learners;

//import aima.core.learning.framework.DataSet;
//import aima.core.learning.learners.StumpLearner;
//import aima.test.core.unit.learning.framework.DataSetTest;
import java.io.IOException;
import org.junit.Assert;
import org.junit.Test;

import com.software.reuze.al_Examples;
import com.software.reuze.al_LearnerDecisionTreeStump;


/**
 * StumpLearnerTest
 *
 * @author Andrew Brown
 */
public class StumpLearnerTest {

    al_Examples restaurantData;
    al_LearnerDecisionTreeStump[] learners;

    /**
     * Constructor
     */
    public StumpLearnerTest() {
        // get restaurant data
        this.restaurantData = null;
        try {
            this.restaurantData = DataSetTest.loadRestaurantData();
        } catch (IOException e) {
            Assert.fail("Could not load restaurant data from URL.");
        }
        // create stumps
        this.learners = new al_LearnerDecisionTreeStump[5];
        for (int i = 0; i < 5; i++) {
            this.learners[i] = new al_LearnerDecisionTreeStump();
            this.learners[i].train(this.restaurantData);
        }
    }

    /**
     * Ensure stumps are at least weak learning; i.e. their prediction accuracy
     * is better than guessing
     */
    @Test
    public void testStumpsForAccuracy() {
        // test
        System.out.print("StumpLearner Accuracy: ");
        for (int i = 0; i < 5; i++) {
            int[] results = this.learners[i].test(restaurantData);
            double proportionCorrect = (double) results[0] / restaurantData.size();
            System.out.print(proportionCorrect+" ");
            Assert.assertTrue(proportionCorrect >= 0.5);
        }
        System.out.println();
    }
    
    /**
     * Ensures the implementation of train() chooses random and distinct 
     * attributes when trained multiple times agains the same example set
     */
    @Test
    public void testThatDifferentAttributesChosen(){
        String firstAttributeName = this.learners[0].getDecisionTree().getAttribute().getName();
        int same = 0;
        for(int i = 1; i < this.learners.length; i++){
            String thisAttributeName = this.learners[i].getDecisionTree().getAttribute().getName();
            if( firstAttributeName.equals(thisAttributeName)) same++;
        }
        Assert.assertTrue(same < 2);
    }
}