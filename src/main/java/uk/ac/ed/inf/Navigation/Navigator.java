package uk.ac.ed.inf.Navigation;

import uk.ac.ed.inf.Exceptions.MoveLimitReachedException;

import java.util.ArrayList;
import java.util.List;

public class Navigator { // maybe change it into singleton
    public final LngLat APPLETON = new LngLat(-3.186874, 55.944494);
    private final int MAX_MOVES = 2000;
    private LngLat dronePosition;
    private List<Area> noFlyAreas;
    private int movesLeft;
    private List<Move> moves;
    private List<Move> movesSinceAT;
    private int ticksSinceStartOfCalculation;
    private List<LngLat> visited;

    private List<LngLat> visitedSinceAT;

    public Navigator(List<Area> noFlyAreas) {
        dronePosition = APPLETON;
        movesLeft = MAX_MOVES;
        this.noFlyAreas = noFlyAreas;
        moves = new ArrayList<>();
        ticksSinceStartOfCalculation = 0;
        movesSinceAT = new ArrayList<>();
        visited = new ArrayList<>();
        visitedSinceAT = new ArrayList<>();
    }

    public List<LngLat> getVisited() {
        return visited;
    }

    public List<LngLat> navigateTo(LngLat end, String orderNo)
            throws MoveLimitReachedException {
        var visibilityGraph = new VisibilityGraph(noFlyAreas, dronePosition, end); // could be optimised
        movesLeft -= 1; // hovering
        // no need to create it every time, just update with new start/end
        List<LngLat> visitedPoints = new ArrayList<>();
        visitedPoints.add(dronePosition);
        var path = visibilityGraph.shortestPath();
        if (end != APPLETON) {
            moves.addAll(movesSinceAT);
            visited.addAll(visitedSinceAT);
            movesSinceAT = new ArrayList<Move>();
        }
        if (end == APPLETON) {
            movesSinceAT.add(new Move(orderNo, dronePosition.lng(),
                    dronePosition.lat(), Direction.HOVER.ANGLE,
                    dronePosition.lng(), dronePosition.lat(),
                    ticksSinceStartOfCalculation));
        }
        path.removeFirst(); // we are already at start
        for (LngLat next : path) {
            while (!dronePosition.closeTo(next)) {
                if (movesLeft == 0) {
                    throw new MoveLimitReachedException();
                }
                var bestDirection = chooseBestDirection(dronePosition, next);
                var nextPosition = dronePosition.
                        nextPosition(bestDirection);
                movesSinceAT.add(new Move(orderNo, dronePosition.lng(),
                        dronePosition.lat(), bestDirection.ANGLE,
                        nextPosition.lng(), nextPosition.lat(),
                        ticksSinceStartOfCalculation));
                movesLeft -= 1;
                dronePosition = nextPosition;
                visitedPoints.add(dronePosition);
                visitedSinceAT.add(dronePosition);
            }
        }
        if (end == APPLETON) {
            movesSinceAT.add(new Move(orderNo, dronePosition.lng(),
                    dronePosition.lat(), Direction.HOVER.ANGLE,
                    dronePosition.lng(), dronePosition.lat(),
                    ticksSinceStartOfCalculation));
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

            if (temp.distanceTo(end) < bestDistance
                    && outsideNoFlyZones
                    // check that this move will not cross any no-fly zones
                    && VisibilityGraph.visible(new Edge(start, temp), noFlyAreas)) {
                bestDistance = temp.distanceTo(end);
                bestDirection = direction;
            }
        }
        return bestDirection;
    }
}
