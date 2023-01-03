package uk.ac.ed.inf;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import uk.ac.ed.inf.Navigation.Area;
import uk.ac.ed.inf.Navigation.LineSegment;
import uk.ac.ed.inf.Navigation.LngLat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestLineSegment {

    @BeforeEach
    void displayTestName(TestInfo testInfo) {
        System.out.println(testInfo.getDisplayName());
    }

    @Test
    public void testDoesNotIntersectAreasSquare() {
        // Create a list of areas
        List<Area> areas = new ArrayList<>();
        // Create a square area with vertices at (0, 0), (0, 1), (1, 1), and (1, 0)
        List<LngLat> squareVertices = Arrays.asList(new LngLat(0, 0), new LngLat(0, 1),
                new LngLat(1, 1), new LngLat(1, 0), new LngLat(0, 0));
        Area square = new Area(squareVertices);
        // Add the square area to the list of areas
        areas.add(square);

        // Create a line segment that intersects the square area
        LineSegment intersectingSegment = new LineSegment(new LngLat(0.5, 0), new LngLat(0.5, 1.5));
        // Call the doesNotIntersectAreas method with the list of areas and the intersecting line segment
        boolean result = intersectingSegment.doesNotIntersectAreas(areas);
        // Assert that the result is false (the line segment intersects one of the areas)
        assertFalse(result);

        // Create a line segment that does not intersect any of the areas
        LineSegment nonIntersectingSegment = new LineSegment(new LngLat(2, 0), new LngLat(2, 1));
        // Call the doesNotIntersectAreas method with the list of areas and the non-intersecting line segment
        result = nonIntersectingSegment.doesNotIntersectAreas(areas);
        // Assert that the result is true (the line segment does not intersect any of the areas)
        assertTrue(result);
    }

    @Test
    public void testDoesNotIntersectAreasIntersectBoth() {
        // Test that the method returns false when given a line segment that intersects multiple areas in the list
        List<Area> overlappingAreas = new ArrayList<>();
        // Create an area with vertices at (0, 0), (0, 1), (1, 1), and (1, 0)
        List<LngLat> area1Vertices = Arrays.asList(new LngLat(0, 0), new LngLat(0, 1),
                new LngLat(1, 1), new LngLat(1, 0), new LngLat(0, 0));
        Area area1 = new Area(area1Vertices);
        // Create an area with vertices at (0.5, 0.5), (0.5, 1.5), (1.5, 1.5), and (1.5, 0.5)
        List<LngLat> area2Vertices = Arrays.asList(new LngLat(0.5, 0.5), new LngLat(0.5, 1.5),
                new LngLat(1.5, 1.5), new LngLat(1.5, 0.5), new LngLat(0.5, 0.5));
        Area area2 = new Area(area2Vertices);
        // Add the areas to the list
        overlappingAreas.add(area1);
        overlappingAreas.add(area2);

        // Create a line segment that intersects both areas
        LineSegment intersectingSegment = new LineSegment(new LngLat(0.5, 0), new LngLat(0.5, 1.5));
        // Call the doesNotIntersectAreas method with the list of overlapping areas and the intersecting line segment
        boolean result = intersectingSegment.doesNotIntersectAreas(overlappingAreas);
        // Assert that the result is false (the line segment intersects both areas)
        assertFalse(result);

    }

    @Test
    public void testDoesNotIntersectAreasOneIntersected() {
        // Test that the method returns false when given a line segment that intersects one area in the list
        List<Area> overlappingAreas = new ArrayList<>();
        // Create an area with vertices at (0, 0), (0, 1), (1, 1), and (1, 0)
        List<LngLat> area1Vertices = Arrays.asList(new LngLat(0, 0), new LngLat(0, 1),
                new LngLat(1, 1), new LngLat(1, 0), new LngLat(0, 0));
        Area area1 = new Area(area1Vertices);
        // Create an area with vertices at (0.5, 0.5), (0.5, 1.5), (1.5, 1.5), and (1.5, 0.5)
        List<LngLat> area2Vertices = Arrays.asList(new LngLat(0.5, 0.5), new LngLat(0.5, 1.5),
                new LngLat(1.5, 1.5), new LngLat(1.5, 0.5), new LngLat(0.5, 0.5));
        Area area2 = new Area(area2Vertices);
        // Add the areas to the list
        overlappingAreas.add(area1);
        overlappingAreas.add(area2);

        // Create a line segment that intersects only one of the areas
        LineSegment intersectingSegment = new LineSegment(new LngLat(0.6, 0.1), new LngLat(0.6, 0.6));
        // Call the doesNotIntersectAreas method with the list of overlapping areas and the intersecting line segment
        boolean result = intersectingSegment.doesNotIntersectAreas(overlappingAreas);
        // Assert that the result is false (the line segment intersects one of the areas)
        assertFalse(result);
    }


}
