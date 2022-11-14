package uk.ac.ed.inf;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    public void testInCentralArea1()
    {
        var point = new LngLat(-3.19, 55.944);
        assertTrue(point.inCentralArea());
    }
    public void testInCentralArea2()
    {
        var point = new LngLat(-3.18, 55.944);
        assertFalse(point.inCentralArea());
    }

    public void testInCentralArea3()
    {
        var point = new LngLat(-3.19, -55.900);
        assertFalse(point.inCentralArea());
    }

    public void testInCentralArea4()
    {
        var point = new LngLat(3.19, -55.900);
        assertFalse(point.inCentralArea());
    }

    public void testInCentralArea5()
    {
        var point = new LngLat(-3.192473, 55.946234);
        assertFalse(point.inCentralArea());
    }

    public void testInCentralArea6()
    {
        var point = new LngLat(-3.192473, 55.944233);
        assertTrue(point.inCentralArea());
    }

    public void testInCentralArea7()
    {
        var point = new LngLat(-3.192473, 55.946232);
        assertTrue(point.inCentralArea());
    }

    public void testInCentralArea8()
    {
        var point = new LngLat(-3.19, 55.942617);
        assertTrue(point.inCentralArea());
    }

    public void testInCentralArea9()
    {
        var point = new LngLat(-3.184319, 55.946233);
        assertTrue(point.inCentralArea());
    }

    public void testInCentralArea10()
    {
        var point = new LngLat(-3.192473, 55.942617);
        assertTrue(point.inCentralArea());
    }

    public void testInCentralArea11()
    {
        var point = new LngLat(-3.184319,55.942617);
        assertTrue(point.inCentralArea());
    }

    public void testInCentralArea12()
    {
        var point = new LngLat(-3.192473,55.946233);
        assertTrue(point.inCentralArea());
    }

    public void testInCentralArea13()
    {
        var point = new LngLat(-3.184319,55.945233);
        assertTrue(point.inCentralArea());
    }

    public void testInCentralArea14()
    {
        var point = new LngLat(-3.184319,55.947233);
        assertFalse(point.inCentralArea());
    }

    public void testMeadows()
    {
        var point = new LngLat(-3.192473,55.942617);
        assertTrue(point.inCentralArea());
    }

    public void testForrest()
    {
        var point = new LngLat(-3.192473,55.946233) ;
        assertTrue(point.inCentralArea());
    }

    public void testKFC()
    {
        var point = new LngLat(-3.184319,55.946233);
        assertTrue(point.inCentralArea());
    }

    public void testBuccleuch()
    {
        var point = new LngLat(-3.184319,55.942617);
        assertTrue(point.inCentralArea());
    }

    public void testBoundary1()
    {
        assertTrue(new LngLat(-3.184319,55.946232).inCentralArea());
    }

    public void testBoundary2()
    {
        assertTrue(new LngLat(-3.184320,55.946233).inCentralArea());
    }

    public void testBoundary3()
    {
        assertTrue(new LngLat(-3.190319,55.946233).inCentralArea());
    }

    public void testBoundary4()
    {
        assertFalse(new LngLat(-3.194319,55.946234).inCentralArea());
    }

    public void testBoundary5()
    {
        assertTrue(new LngLat(-3.192473,55.946232).inCentralArea());
    }

}
