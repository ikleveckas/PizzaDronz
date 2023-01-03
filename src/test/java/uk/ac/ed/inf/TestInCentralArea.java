package uk.ac.ed.inf;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import uk.ac.ed.inf.Navigation.Area;
import uk.ac.ed.inf.Navigation.LngLat;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Extensive unit tests for inCentralArea method.
 */
public class TestInCentralArea
{

    @BeforeEach
    void displayTestName(TestInfo testInfo)
    {
        System.out.println(testInfo.getDisplayName());
    }

    LngLat centralAreaBoundary1 = new LngLat(-3.192473, 55.946233);
    LngLat centralAreaBoundary2 = new LngLat(-3.192473, 55.942617);
    LngLat centralAreaBoundary3 = new LngLat(-3.184319, 55.942617);
    LngLat centralAreaBoundary4 = new LngLat(-3.184319, 55.946233);
    List<LngLat> centralAreaVertices = new ArrayList<>();
    boolean value1 = centralAreaVertices.add(centralAreaBoundary1);
    boolean value2 = centralAreaVertices.add(centralAreaBoundary2);
    boolean value3 = centralAreaVertices.add(centralAreaBoundary3);
    boolean value4 = centralAreaVertices.add(centralAreaBoundary4);
    boolean value5 = centralAreaVertices.add(centralAreaBoundary1);
    Area centralArea = new Area(centralAreaVertices);

    @Test
    public void testInCentralArea1()
    {
        // Test with a point inside the Central Area
        var point = new LngLat(-3.19, 55.944);
        assertTrue(point.inCentralArea(centralArea));
    }
    @Test
    public void testInCentralArea2()
    {
        // Test with a point far outside of the Central Area
        var point = new LngLat(-3.18, 55.944);
        assertFalse(point.inCentralArea(centralArea));
    }

    @Test
    public void testInCentralArea3()
    {
        // Test with a point outside the Central Area
        var point = new LngLat(-3.19, -55.900);
        assertFalse(point.inCentralArea(centralArea));
    }

    @Test
    public void testInCentralArea4()
    {
        // Test with a point very far outside the Central Area
        var point = new LngLat(3.19, 0);
        assertFalse(point.inCentralArea(centralArea));
    }

    @Test
    public void testInCentralArea5()
    {
        // Test with a point very just outside the Central Area boundary
        var point = new LngLat(-3.192473, 55.946234);
        assertFalse(point.inCentralArea(centralArea));
    }

    @Test
    public void testInCentralArea6()
    {
        // Test with a point just inside the Central Area boundary
        var point = new LngLat(-3.192473, 55.944233);
        assertTrue(point.inCentralArea(centralArea));
    }

    @Test
    public void testInCentralArea7()
    {
        // Test with a point just inside the Central Area boundary
        var point = new LngLat(-3.192473, 55.946232);
        assertTrue(point.inCentralArea(centralArea));
    }

    @Test
    public void testInCentralArea8()
    {
        // Test with a point inside the Central Area boundary
        var point = new LngLat(-3.19, 55.942617);
        assertTrue(point.inCentralArea(centralArea));
    }

    @Test
    public void testInCentralArea9()
    {
        // Test with a point inside the Central Area boundary
        var point = new LngLat(-3.184319, 55.946233);
        assertTrue(point.inCentralArea(centralArea));
    }

    @Test
    public void testInCentralArea10()
    {
        // Test with a point inside the Central Area boundary
        var point = new LngLat(-3.192473, 55.942617);
        assertTrue(point.inCentralArea(centralArea));
    }

    @Test
    public void testInCentralArea11()
    {
        // Test with a point just inside the Central Area boundary
        var point = new LngLat(-3.184319,55.942617);
        assertTrue(point.inCentralArea(centralArea));
    }

    @Test
    public void testInCentralArea12()
    {
        // Test with a point just inside the Central Area boundary
        var point = new LngLat(-3.192473,55.946233);
        assertTrue(point.inCentralArea(centralArea));
    }

    @Test
    public void testInCentralArea13()
    {
        // Test with a point just inside the Central Area boundary
        var point = new LngLat(-3.184319,55.945233);
        assertTrue(point.inCentralArea(centralArea));
    }

    @Test
    public void testInCentralArea14()
    {
        // Test with a point just outside the Central Area
        var point = new LngLat(-3.184319,55.947233);
        assertFalse(point.inCentralArea(centralArea));
    }

    @Test
    public void testMeadows()
    {
        // Test with a point that lies on Meadows vertex of the central area
        var point = new LngLat(-3.192473,55.942617);
        assertTrue(point.inCentralArea(centralArea));
    }

    @Test
    public void testForrest()
    {
        // Test with a point that lies on Forrest vertex of the central area
        var point = new LngLat(-3.192473,55.946233) ;
        assertTrue(point.inCentralArea(centralArea));
    }

    @Test
    public void testKFC()
    {
        // Test with a point that lies on KFC vertex of the central area
        var point = new LngLat(-3.184319,55.946233);
        assertTrue(point.inCentralArea(centralArea));
    }

    @Test
    public void testBuccleuch()
    {
        // Test with a point that lies on Buccleuch vertex of the central area
        var point = new LngLat(-3.184319,55.942617);
        assertTrue(point.inCentralArea(centralArea));
    }

    @Test
    public void testBoundary1()
    {
        // Test with a point that lies just inside the boundary of the central area
        assertTrue(new LngLat(-3.184319,55.946232).inCentralArea(centralArea));
    }

    @Test
    public void testBoundary2()
    {
        // Test with a point that lies just inside the boundary of the central area
        assertTrue(new LngLat(-3.184320,55.946233).inCentralArea(centralArea));
    }

    @Test
    public void testBoundary3()
    {
        // Test with a point that lies just inside the boundary of the central area
        assertTrue(new LngLat(-3.190319,55.946233).inCentralArea(centralArea));
    }

    @Test
    public void testBoundary4()
    {
        // Test with a point that lies on the boundary of the central area
        assertFalse(new LngLat(-3.194319,55.946234).inCentralArea(centralArea));
    }

    @Test
    public void testBoundary5()
    {
        // Test with a point that lies just inside the boundary of the central area
        assertTrue(new LngLat(-3.192473,55.946232).inCentralArea(centralArea));
    }
}
