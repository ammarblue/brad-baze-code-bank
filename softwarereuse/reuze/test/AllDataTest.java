package reuze.test;
import junit.framework.Test;
import junit.framework.TestSuite;

public class AllDataTest {

    public static Test suite() {
        TestSuite suite = new TestSuite(AllDataTest.class.getName());
        // $JUnit-BEGIN$
        suite.addTestSuite(ArrayUtilTest.class);
        suite.addTestSuite(BagTest.class);
        suite.addTestSuite(BitArrayTest.class);
        suite.addTestSuite(BitMatrixTest.class);
        suite.addTestSuite(BitSourceTest.class);
        suite.addTestSuite(BitVectorTest.class);
        suite.addTestSuite(CellArrayTest.class);
        suite.addTestSuite(DateTest.class);
        suite.addTestSuite(EncoderTest.class);
        suite.addTestSuite(EncoderTest.class);
        suite.addTestSuite(MaskUtilTest.class);
        suite.addTestSuite(PrefUtilsTest.class);
        suite.addTestSuite(QRCodeTest.class);
        suite.addTestSuite(SetWeightedRandomTest.class);
        // $JUnit-END$
        return suite;
    }

}