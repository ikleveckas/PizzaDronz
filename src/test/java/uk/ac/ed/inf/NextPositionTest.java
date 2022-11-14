package uk.ac.ed.inf;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class NextPositionTest
        extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public NextPositionTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( NextPositionTest.class );
    }

    public void testEast()
    {
        LngLat point = new LngLat(-3.19, 55.94).nextPosition(Direction.E);
        assertTrue(point.closeTo(new LngLat(-3.18985, 55.94)));
    }

    public void testNorth()
    {
        LngLat point = new LngLat(-3.19, 55.94).nextPosition(Direction.N);
        assertTrue(point.closeTo(new LngLat(-3.19, 55.94015)));
    }

    public void testNorthMany()
    {
        LngLat point = new LngLat(-3.19, 55.94);
        for (int i=0; i<1000; i++) {
            point = point.nextPosition(Direction.N);
        }
        assertTrue(point.closeTo(new LngLat(-3.19, 56.09)));
    }

    public void testSswMany()
    {
        LngLat point = new LngLat(-3.19, 55.94);
        for (int i=0; i<1000; i++) {
            point = point.nextPosition(Direction.SSW);
        }
        assertTrue(point.closeTo(new LngLat(-3.247402515, 55.80141807)));
    }
}
