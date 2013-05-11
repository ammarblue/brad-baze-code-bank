package reuze.test;
import junit.framework.Test;
import junit.framework.TestSuite;

public class AllMathTests {

    public static Test suite() {
        TestSuite suite = new TestSuite(AllMathTests.class.getName());
        // $JUnit-BEGIN$
        suite.addTestSuite(BilinearTest.class);
        suite.addTestSuite(CipherRot13Test.class);
        suite.addTestSuite(ComplexTest.class);
        suite.addTestSuite(FractionTest.class);
        suite.addTestSuite(MathTest.class);
        suite.addTestSuite(MatrixTest.class);
        suite.addTestSuite(MatrixUtilTest.class);
        suite.addTestSuite(NoiseSimplexTest.class);
        suite.addTestSuite(RangeTest.class);
        suite.addTestSuite(RunLengthTest.class);
        suite.addTestSuite(Vector3Test.class);
        // $JUnit-END$
        return suite;
    }

}