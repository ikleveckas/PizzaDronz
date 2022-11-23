package uk.ac.ed.inf.Navigation;

import java.util.ArrayList;
import java.util.List;

public class Navigator {
    public final LngLat APPLETON = new LngLat(-3.186874, 55.944494);
    private LngLat drone;
    private List<Area> noFlyAreas;
    public Navigator(List<Area> noFlyAreas) {
        drone = APPLETON;
        this.noFlyAreas = noFlyAreas;
    }

    public List<LngLat> navigateTo(LngLat end) {
        var visibilityGraph = new VisibilityGraph(noFlyAreas, drone, end); // could be optimised
        // no need to create it every time, just update with new start/end
        List<LngLat> visitedPoints = new ArrayList<>();
        visitedPoints.add(drone);
        var path = visibilityGraph.shortestPath();
        path.removeFirst(); // we are already at start
        for (LngLat next : path) {
            while (!drone.closeTo(next)) {
                drone = drone.nextPosition(chooseBestDirection(drone, next));
                visitedPoints.add(drone);
            }
        }
        return visitedPoints;
    }

    private Direction chooseBestDirection(LngLat start, LngLat end) {
        var bestDirection = Direction.HOVER; // change from hover
        var bestDistance = start.distanceTo(end);
        for (Direction direction : Direction.values()) {
            var temp = start.nextPosition(direction);
            var outsideNoFlyZones = noFlyAreas.stream()
                    .noneMatch(x -> temp.strictlyInsideArea(x.vertices()));
            if (temp.distanceTo(end) < bestDistance && outsideNoFlyZones) {
                bestDistance = temp.distanceTo(end);
                bestDirection = direction;
            }
        }
        return bestDirection;
    }
}
