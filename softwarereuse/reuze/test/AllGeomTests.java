package reuze.test;


import junit.framework.Test;
import junit.framework.TestSuite;

public class AllGeomTests {

    public static Test suite() {
        TestSuite suite = new TestSuite(AllGeomTests.class.getName());
        // $JUnit-BEGIN$
        suite.addTestSuite(AABBTest.class);
        suite.addTestSuite(CircleTest.class);
        suite.addTestSuite(Geometry2DTest.class);
        suite.addTestSuite(Intersection2DTest.class);
        suite.addTestSuite(Line2DTest.class);
        suite.addTestSuite(Line3DTest.class);
        suite.addTestSuite(MatrixTest.class);
        suite.addTestSuite(PlaneTest.class);
        suite.addTestSuite(PointTest.class);
        suite.addTestSuite(PolygonTest.class);
        suite.addTestSuite(QuaternionTest.class);
        suite.addTestSuite(RectangleTest.class);
        suite.addTestSuite(SphereTest.class);
        suite.addTestSuite(TreeTest.class);
        suite.addTestSuite(Triangle2DTest.class);
        suite.addTestSuite(TriangleMeshTest.class);
        suite.addTestSuite(TriangleTest.class);
        suite.addTestSuite(Vector3Test.class);
        suite.addTestSuite(WEMeshTest.class);
        // $JUnit-END$
        return suite;
    }

}
