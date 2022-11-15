package uk.ac.ed.inf;

import java.util.List;

public class Navigator {
    public final LngLat APPLETON = new LngLat(-3.186874, 55.944494);
    private LngLat drone;
    private VisibilityGraph visibilityGraph;
    private List<NoFlyArea> noFlyAreas;
    public Navigator(List<NoFlyArea> noFlyAreas) {
        drone = APPLETON;
        this.noFlyAreas = noFlyAreas;
    }

    public void navigateFromTo(LngLat start, LngLat end) {
        //visibilityGraph = new VisibilityGraph(noFlyAreas, start, end); // could be optimised
        // no need to create it every time, just update with new start/end
        var path = visibilityGraph.shortestPath();
        path.removeFirst(); // we are already at start
        for (LngLat next : path) {

        }
    }

    private void moveDrone(LngLat start, LngLat end) {

    }

    private void chooseBestDirection(LngLat start, LngLat end) {
        var bestDirection = Direction.HOVER;
        var bestDistance = start.distanceTo(end);
        for (Direction direction : Direction.values()) {
            var temp = start.nextPosition(direction);

        }
    }
}
