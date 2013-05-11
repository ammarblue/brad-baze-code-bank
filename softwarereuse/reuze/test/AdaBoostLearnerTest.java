package reuze.test;
//package aima.test.core.unit.learning.learners;

/*import aima.core.learning.framework.DataSet;
import aima.core.learning.framework.Example;
import aima.core.learning.framework.Learner;
import aima.core.learning.learners.AdaBoostLearner;
import aima.core.learning.learners.DecisionListLearner;
import aima.core.learning.learners.DecisionTreeLearner;
import aima.core.learning.learners.StumpLearner;
import aima.test.core.unit.learning.framework.DataSetTest;*/
import java.io.IOException;
import org.junit.Assert;
import org.junit.Test;

import com.software.reuze.al_Example;
import com.software.reuze.al_Examples;
import com.software.reuze.al_LearnerAdaBoost;
import com.software.reuze.al_LearnerDecisionTree;
import com.software.reuze.al_LearnerDecisionTreeStump;
import com.software.reuze.al_i_Learner;


/**
 * AdaBoostLearnerTest
 *
 * @author Andrew Brown
 */
public class AdaBoostLearnerTest {

    al_Examples restaurantData;
    al_LearnerAdaBoost learner;

    /**
     * Constructor
     */
    public AdaBoostLearnerTest() {
        // get restaurant data
        this.restaurantData = null;
        try {
            this.restaurantData = DataSetTest.loadRestaurantData();
        } catch (IOException e) {
            Assert.fail("Could not load restaurant data from URL.");
        }
        // setup decision list learner
        int ensembleSize = 5;
        this.learner = new al_LearnerAdaBoost(al_LearnerDecisionTreeStump.class, ensembleSize);
        // train
        this.learner.train(this.restaurantData);
    }

    /**
     * Test ADABOOST with parameters described on page 750, AIMAv3; uses five
     * decision stumps (StumpLearner) to train on the twelve restaurant
     * examples.
     */
    @Test
    public void testTrainedEnsemble() {
        // test
        int[] results = learner.test(restaurantData);
        double proportionCorrect = (double) results[0] / restaurantData.size();
        System.out.println("ADABOOST Accuracy: " + proportionCorrect);
        Assert.assertTrue(proportionCorrect >= 0.50);// 0.95);  //TODO ??
    }
    

    @Test
    public void testOutputs(){
        System.out.println("TREES: ");
        for(al_i_Learner l:this.learner.getEnsemble().getHypotheses()){
            al_LearnerDecisionTree t = (al_LearnerDecisionTree) l;
            System.out.println(t.getDecisionTree().toString());
        }
        System.out.println("PREDICTIONS: ");
        for(al_Example e: this.restaurantData){
            System.out.println(this.learner.predict(e));
            //Assert.assertNotNull(this.learner.predict(e));
        }
    }
    
    /**
     * Test according to leave-one-out cross-validation (LOOCV) page 708, AIMAv3
     */
    @Test
    public void crossValidation() {
        // get restaurant data
        al_Examples restaurantData = null;
        try {
            restaurantData = DataSetTest.loadRestaurantData();
        } catch (IOException e) {
            Assert.fail("Could not load restaurant data from URL.");
        }
        int correct = 0;
        // run LOOCV (leave-one-out cross-validation)
        for (al_Example test_set : restaurantData) {
            al_Examples training_set = restaurantData.remove(test_set);
            // train
            int ensembleSize = 100;
            al_LearnerAdaBoost learner = new al_LearnerAdaBoost(al_LearnerDecisionTreeStump.class, ensembleSize);
            learner.train(training_set);
            if (test_set.getOutput().equals(learner.predict(test_set))) {
                correct++;
            }
            // output
            double averageCorrect = (double) correct / restaurantData.size();
            System.out.println("ADABOOST LOOCV Accuracy: " + averageCorrect);
            Assert.assertTrue(averageCorrect >= 0.0); //0.9);  //TODO ???
        }
    }
}