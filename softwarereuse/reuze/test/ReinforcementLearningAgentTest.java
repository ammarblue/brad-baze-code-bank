package reuze.test;
//package aima.test.core.unit.learning.reinforcement.agent;

/*import aima.core.environment.cellworld.CellContent;
import aima.core.environment.cellworld.CellWorld;
import aima.core.environment.cellworld.CellWorldAction;
import aima.core.environment.cellworld.CellWorldFactory;
import aima.core.learning.reinforcement.agent.ReinforcementAgent;
import aima.core.learning.reinforcement.example.CellWorldEnvironment;
import aima.core.probability.example.MDPFactory;
import aima.core.util.JavaRandomizer;*/
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Assert;

import com.software.reuze.a_CellWorld;
import com.software.reuze.a_CellWorldAction;
import com.software.reuze.a_CellWorldEnvironment;
import com.software.reuze.aa_a_AgentReinforcement;
import com.software.reuze.ga_CellPositionAndContent;
import com.software.reuze.m_RandomJava;


public abstract class ReinforcementLearningAgentTest {

    public static void test_RMSeiu_for_1_1(
            aa_a_AgentReinforcement<ga_CellPositionAndContent<Double>, a_CellWorldAction> reinforcementAgent,
            int numRuns, int numTrialsPerRun,
            double expectedErrorLessThan) {
        a_CellWorld<Double> cw = CellWorldFactory.createCellWorldForFig17_1();
        a_CellWorldEnvironment cwe = new a_CellWorldEnvironment(
                cw.getCellAt(1, 1),
                cw.getCells(),
                MDPFactory.createTransitionProbabilityFunctionForFigure17_1(cw),
                new m_RandomJava());

        cwe.addAgent(reinforcementAgent);

        Map<Integer, Map<ga_CellPositionAndContent<Double>, Double>> runs = new HashMap<Integer, Map<ga_CellPositionAndContent<Double>, Double>>();
        for (int r = 0; r < numRuns; r++) {
            reinforcementAgent.reset();
            cwe.executeTrials(numTrialsPerRun);
            runs.put(r, reinforcementAgent.getUtility());
        }

        // Calculate the Root Mean Square Error for utility of 1,1
        // for this trial# across all runs
        double xSsquared = 0;
        for (int r = 0; r < numRuns; r++) {
            Map<ga_CellPositionAndContent<Double>, Double> u = runs.get(r);
            Double val1_1 = u.get(cw.getCellAt(1, 1));
            if (null == val1_1) {
                throw new IllegalStateException("U(1,1,) is not present: r=" + r + ", u=" + u);
            }
            xSsquared += Math.pow(0.705 - val1_1, 2);
        }
        double rmse = Math.sqrt(xSsquared / runs.size());
        Assert.assertTrue("" + rmse + " is not < " + expectedErrorLessThan, rmse < expectedErrorLessThan);
    }

    public static void test_utility_learning_rates(
            aa_a_AgentReinforcement<ga_CellPositionAndContent<Double>, a_CellWorldAction> reinforcementAgent,
            int numRuns, int numTrialsPerRun, int rmseTrialsToReport,
            int reportEveryN) {

        if (rmseTrialsToReport > (numTrialsPerRun / reportEveryN)) {
            throw new IllegalArgumentException(
                    "Requesting to report too many RMSE trials, max allowed for args is "
                    + (numTrialsPerRun / reportEveryN));
        }

        a_CellWorld<Double> cw = CellWorldFactory.createCellWorldForFig17_1();
        a_CellWorldEnvironment cwe = new a_CellWorldEnvironment(
                cw.getCellAt(1, 1),
                cw.getCells(),
                MDPFactory.createTransitionProbabilityFunctionForFigure17_1(cw),
                new m_RandomJava());

        cwe.addAgent(reinforcementAgent);

        Map<Integer, List<Map<ga_CellPositionAndContent<Double>, Double>>> runs = new HashMap<Integer, List<Map<ga_CellPositionAndContent<Double>, Double>>>();
        for (int r = 0; r < numRuns; r++) {
            reinforcementAgent.reset();
            List<Map<ga_CellPositionAndContent<Double>, Double>> trials = new ArrayList<Map<ga_CellPositionAndContent<Double>, Double>>();
            for (int t = 0; t < numTrialsPerRun; t++) {
                cwe.executeTrial();
                if (0 == t % reportEveryN) {
                    Map<ga_CellPositionAndContent<Double>, Double> u = reinforcementAgent.getUtility();
                    if (null == u.get(cw.getCellAt(1, 1))) {
                        throw new IllegalStateException("Bad Utility State Encountered: r=" + r + ", t=" + t + ", u=" + u);
                    }
                    trials.add(u);
                }
            }
            runs.put(r, trials);
        }

        StringBuilder v4_3 = new StringBuilder();
        StringBuilder v3_3 = new StringBuilder();
        StringBuilder v1_3 = new StringBuilder();
        StringBuilder v1_1 = new StringBuilder();
        StringBuilder v3_2 = new StringBuilder();
        StringBuilder v2_1 = new StringBuilder();
        for (int t = 0; t < (numTrialsPerRun / reportEveryN); t++) {
            // Use the last run
            Map<ga_CellPositionAndContent<Double>, Double> u = runs.get(numRuns - 1).get(t);
            v4_3.append((u.containsKey(cw.getCellAt(4, 3)) ? u.get(cw.getCellAt(4, 3)) : 0.0)
                    + "\t");
            v3_3.append((u.containsKey(cw.getCellAt(3, 3)) ? u.get(cw.getCellAt(3, 3)) : 0.0)
                    + "\t");
            v1_3.append((u.containsKey(cw.getCellAt(1, 3)) ? u.get(cw.getCellAt(1, 3)) : 0.0)
                    + "\t");
            v1_1.append((u.containsKey(cw.getCellAt(1, 1)) ? u.get(cw.getCellAt(1, 1)) : 0.0)
                    + "\t");
            v3_2.append((u.containsKey(cw.getCellAt(3, 2)) ? u.get(cw.getCellAt(3, 2)) : 0.0)
                    + "\t");
            v2_1.append((u.containsKey(cw.getCellAt(2, 1)) ? u.get(cw.getCellAt(2, 1)) : 0.0)
                    + "\t");
        }
        System.out.println("(4,3)" + "\t" + v4_3);
        System.out.println("(3,3)" + "\t" + v3_3);
        System.out.println("(1,3)" + "\t" + v1_3);
        System.out.println("(1,1)" + "\t" + v1_1);
        System.out.println("(3,2)" + "\t" + v3_2);
        System.out.println("(2,1)" + "\t" + v2_1);

        StringBuilder rmseValues = new StringBuilder();
        for (int t = 0; t < rmseTrialsToReport; t++) {
            // Calculate the Root Mean Square Error for utility of 1,1
            // for this trial# across all runs
            double xSsquared = 0;
            for (int r = 0; r < numRuns; r++) {
                Map<ga_CellPositionAndContent<Double>, Double> u = runs.get(r).get(t);
                Double val1_1 = u.get(cw.getCellAt(1, 1));
                if (null == val1_1) {
                    throw new IllegalStateException("U(1,1,) is not present: r=" + r + ", t=" + t + ", runs.size=" + runs.size() + ", runs(r).size()=" + runs.get(r).size() + ", u=" + u);
                }
                xSsquared += Math.pow(0.705 - val1_1, 2);
            }
            double rmse = Math.sqrt(xSsquared / runs.size());
            rmseValues.append(rmse);
            rmseValues.append("\t");
        }
        System.out.println("RMSeiu" + "\t" + rmseValues);
    }
}
