package uk.ac.ed.inf;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import uk.ac.ed.inf.Navigation.Direction;
import uk.ac.ed.inf.Navigation.LngLat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the LngLat class.
 * Ensures that individual public methods in this class work as expected.
 * InCentralArea method is tested more extensively and thus can be found
 * in a separate file to reduce clutter in this file.
 */
public class TestLngLat
{

    @BeforeEach
    void displayTestName(TestInfo testInfo)
    {
        System.out.println(testInfo.getDisplayName());
    }

    @Test
    public void testStrictlyInsideArea() {
        // Test with a point inside a square
        List<LngLat> corners = Arrays.asList(
                new LngLat(0, 0),
                new LngLat(0, 1),
                new LngLat(1, 1),
                new LngLat(1, 0)
        );
        LngLat testPoint1 = new LngLat(0.5, 0.5);
        assertTrue(testPoint1.strictlyInsideArea(corners));

        // Test with a point outside a square
        LngLat testPoint2 = new LngLat(1.5, 0.5);
        assertFalse(testPoint2.strictlyInsideArea(corners));

        // Test with a point on the edge of a square
        LngLat testPoint3 = new LngLat(1, 0.5);
        assertFalse(testPoint3.strictlyInsideArea(corners));

        // Test with a point inside a triangle
        corners = Arrays.asList(
                new LngLat(0, 0),
                new LngLat(1, 0),
                new LngLat(0.5, 1)
        );
        assertTrue(new LngLat(0.5, 0.5).strictlyInsideArea(corners));

        // Test with a point outside a triangle
        assertFalse(new LngLat(0.5, 1.5).strictlyInsideArea(corners));

        // Test with a point on the edge of a triangle
        assertFalse(new LngLat(0.5, 1).strictlyInsideArea(corners));

        // Test with an empty list of corners
        corners = new ArrayList<>();
        assertFalse(new LngLat(0, 0).strictlyInsideArea(corners));
    }

    @Test
    public void testPointBetweenCorners() {
        // Create some test LngLat objects
        LngLat point1 = new LngLat(1.0, 2.0);
        LngLat point2 = new LngLat(3.0, 4.0);
        LngLat point3 = new LngLat(2.0, 3.0);
        LngLat point4 = new LngLat(1.0, 2.0);
        LngLat point5 = new LngLat(4.0, 5.0);
        LngLat point6 = new LngLat(1.0, 1.0);
        LngLat point7 = new LngLat(2.0, 2.0);
        LngLat point8 = new LngLat(3.0, 3.0);

        // Test that point3 is between point1 and point2
        assertTrue(point3.pointBetweenCorners(point1, point2));

        // Test that point1 is not between point2 and point3
        assertFalse(point1.pointBetweenCorners(point2, point3));

        // Test that point4 is not between point1 and point2 (it's equal to point1)
        assertFalse(point4.pointBetweenCorners(point1, point2));

        // Test points that are collinear but not strictly between each other
        assertFalse(point5.pointBetweenCorners(point1, point2));

        // Test points that are not collinear
        assertFalse(point8.pointBetweenCorners(point6, point7));

        // Test points that are collinear and strictly between each other
        assertTrue(point7.pointBetweenCorners(point6, point8));
    }

    @Test
    public void testEquals_sameLngLat_returnsTrue() {
        LngLat lngLat = new LngLat(1.0, 2.0);
        LngLat otherLngLat = new LngLat(1.0, 2.0);
        assertTrue(lngLat.equals(otherLngLat));
    }

    @Test
    public void testEquals_differentLngLat_returnsFalse() {
        LngLat lngLat = new LngLat(1.0, 2.0);
        LngLat otherLngLat = new LngLat(3.0, 4.0);
        assertFalse(lngLat.equals(otherLngLat));
    }

    @Test
    public void testEquals_differentObjectType_returnsFalse() {
        LngLat lngLat = new LngLat(1.0, 2.0);
        Object obj = new Object();
        assertNotEquals(lngLat, obj);
    }

    @Test
    public void testEquals_nullObject_returnsFalse() {
        LngLat lngLat = new LngLat(1.0, 2.0);
        assertFalse(lngLat.equals(null));
    }

    @Test
    public void testDistanceTo_sameLocation() {
        LngLat point1 = new LngLat(0, 0);
        LngLat point2 = new LngLat(0, 0);
        double expected = 0;
        double actual = point1.distanceTo(point2);
        assertEquals(expected, actual, 0.01);
    }

    @Test
    public void testDistanceTo_positiveCoordinates() {
        LngLat point1 = new LngLat(0, 0);
        LngLat point2 = new LngLat(3, 4);
        double expected = 5;
        double actual = point1.distanceTo(point2);
        assertEquals(expected, actual, 0.01);
    }

    @Test
    public void testDistanceTo_negativeCoordinates() {
        LngLat point1 = new LngLat(-1, -1);
        LngLat point2 = new LngLat(1, 1);
        double expected = 2.8284271; // sqrt(8)
        double actual = point1.distanceTo(point2);
        assertEquals(expected, actual, 0.01);
    }

    @Test
    public void testDistanceTo_differentNegativeCoordinates() {
        LngLat point1 = new LngLat(-5, -5);
        LngLat point2 = new LngLat(-3, -3);
        double expected = 2.8284271; // sqrt(8)
        double actual = point1.distanceTo(point2);
        assertEquals(expected, actual, 0.01);
    }

    @Test
    public void testCloseTo_sameLocation() {
        LngLat point1 = new LngLat(0, 0);
        LngLat point2 = new LngLat(0, 0);
        assertTrue(point1.closeTo(point2));
    }

    @Test
    public void testCloseTo_withinTolerance() {
        LngLat point1 = new LngLat(0, 0);
        LngLat point2 = new LngLat(0.00014, 0);
        assertTrue(point1.closeTo(point2));
    }

    @Test
    public void testCloseTo_atTolerance() {
        LngLat point1 = new LngLat(0, 0);
        LngLat point2 = new LngLat(0.00015, 0);
        assertFalse(point1.closeTo(point2));
    }

    @Test
    public void testCloseTo_withinTolerance_yCoord() {
        LngLat point1 = new LngLat(0, 0);
        LngLat point2 = new LngLat(0, 0.00014);
        assertTrue(point1.closeTo(point2));
    }

    @Test
    public void testCloseTo_atTolerance_yCoord() {
        LngLat point1 = new LngLat(0, 0);
        LngLat point2 = new LngLat(0, 0.00015);
        assertFalse(point1.closeTo(point2));
    }

    @Test
    public void testHoverDoesNotChangeCoordinates() {
        LngLat initialPosition = new LngLat(10.0, 20.0);
        LngLat nextPosition = initialPosition.nextPosition(Direction.HOVER);
        assertEquals(initialPosition.lng(), nextPosition.lng());
        assertEquals(initialPosition.lat(), nextPosition.lat());
    }

    final double DELTA = Math.pow(10, -12);
    @Test
    public void testMoveNorth() {
        LngLat initialPosition = new LngLat(10.0, 20.0);
        LngLat nextPosition = initialPosition.nextPosition(Direction.N);
        double expectedLatChange = 0.00015;
        double actualLatChange = nextPosition.lat() - initialPosition.lat();
        assertEquals(expectedLatChange, actualLatChange, DELTA);
    }

    @Test
    public void testMoveNorthEast() {
        LngLat initialPosition = new LngLat(10.0, 20.0);
        LngLat nextPosition = initialPosition.nextPosition(Direction.NE);
        double expectedLatChange = 0.00015 * Math.sqrt(2) / 2;
        double expectedLngChange = 0.00015 * Math.sqrt(2) / 2;
        double actualLatChange = nextPosition.lat() - initialPosition.lat();
        double actualLngChange = nextPosition.lng() - initialPosition.lng();
        assertEquals(expectedLatChange, actualLatChange, DELTA);
        assertEquals(expectedLngChange, actualLngChange, DELTA);
    }

    @Test
    public void testMoveEast() {
        LngLat initialPosition = new LngLat(10.0, 20.0);
        LngLat nextPosition = initialPosition.nextPosition(Direction.E);
        double expectedLngChange = 0.00015;
        double actualLngChange = nextPosition.lng() - initialPosition.lng();
        assertEquals(expectedLngChange, actualLngChange, DELTA);
    }

    @Test
    public void testMoveSouthEast() {
        LngLat initialPosition = new LngLat(10.0, 20.0);
        LngLat nextPosition = initialPosition.nextPosition(Direction.SE);
        double expectedLatChange = -0.00015 * Math.sqrt(2) / 2;
        double expectedLngChange = 0.00015 * Math.sqrt(2) / 2;
        double actualLatChange = nextPosition.lat() - initialPosition.lat();
        double actualLngChange = nextPosition.lng() - initialPosition.lng();
        assertEquals(expectedLatChange, actualLatChange, DELTA);
        assertEquals(expectedLngChange, actualLngChange, DELTA);
    }

    @Test
    public void testMoveSouthWest() {
        LngLat initialPosition = new LngLat(10.0, 20.0);
        LngLat nextPosition = initialPosition.nextPosition(Direction.SW);
        double expectedLatChange = -0.00015 * Math.sqrt(2) / 2;
        double expectedLngChange = -0.00015 * Math.sqrt(2) / 2;
        double actualLatChange = nextPosition.lat() - initialPosition.lat();
        double actualLngChange = nextPosition.lng() - initialPosition.lng();
        assertEquals(expectedLatChange, actualLatChange, DELTA);
        assertEquals(expectedLngChange, actualLngChange, DELTA);
    }

    @Test
    public void testMoveWest() {
        LngLat initialPosition = new LngLat(10.0, 20.0);
        LngLat nextPosition = initialPosition.nextPosition(Direction.W);
        double expectedLngChange = -0.00015;
        double actualLngChange = nextPosition.lng() - initialPosition.lng();
        assertEquals(expectedLngChange, actualLngChange, DELTA);
    }

    @Test
    public void testMoveNorthWest() {
        LngLat initialPosition = new LngLat(10.0, 20.0);
        LngLat nextPosition = initialPosition.nextPosition(Direction.NW);
        double expectedLatChange = 0.00015 * Math.sqrt(2) / 2;
        double expectedLngChange = -0.00015 * Math.sqrt(2) / 2;
        double actualLatChange = nextPosition.lat() - initialPosition.lat();
        double actualLngChange = nextPosition.lng() - initialPosition.lng();
        assertEquals(expectedLatChange, actualLatChange, DELTA);
        assertEquals(expectedLngChange, actualLngChange, DELTA);
    }

    @Test
    public void testMoveNorthNorthWest() {
        LngLat point = new LngLat(-3.1924, 55.94637);
        double expected = 55.946508581929876;
        double actual = point.nextPosition(Direction.NNW).lat();
        assertEquals(expected, actual, DELTA);
    }

    @Test
    public void testMoveWestNorthWest() {
        LngLat point = new LngLat(-3.19, 55.946233);
        double expected = -3.1901385819298764;
        double actual = point.nextPosition(Direction.WNW).lng();
        assertEquals(expected,
                actual, DELTA);
    }
}

