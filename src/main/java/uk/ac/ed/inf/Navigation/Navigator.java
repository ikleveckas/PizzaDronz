package uk.ac.ed.inf.Navigation;

import uk.ac.ed.inf.Exceptions.MoveLimitReachedException;

import java.util.*;

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

    public List<Move> getMoves() {
        return moves;
    }

    private void hover(String orderNo) throws MoveLimitReachedException {
        movesLeft -= 1;
        if (movesLeft <= 0) {
            throw new MoveLimitReachedException();
        }
        movesSinceAT.add(new Move(orderNo, dronePosition.lng(),
                dronePosition.lat(), Direction.HOVER.ANGLE,
                dronePosition.lng(), dronePosition.lat(),
                ticksSinceStartOfCalculation));
    }
    public void navigateLoop(LngLat end, String orderNo)
        throws MoveLimitReachedException {
            navigateTo(end, orderNo);
            hover(orderNo); // hover when collecting from a restaurant
            navigateTo(APPLETON, orderNo);
            hover(orderNo); // hover when delivering pizzas to AT
            moves.addAll(movesSinceAT);
            visited.addAll(visitedSinceAT);
            movesSinceAT = new ArrayList<>();
            visitedSinceAT = new ArrayList<>();
    }

    public void navigateTo(LngLat end, String orderNo)
            throws MoveLimitReachedException {

        var visibilityGraph = new VisibilityGraph(noFlyAreas, dronePosition, end);
        // no need to create it every time, just update with new start/end
        // could be optimised
        var path = visibilityGraph.shortestPath();
        visitedSinceAT.add(dronePosition);
        path.removeFirst(); // we are already at start so no need to reach it
        for (LngLat next : path) {
            while (!dronePosition.closeTo(next)) {
                if (movesLeft <= 0) {
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
                visitedSinceAT.add(dronePosition);
            }
        }
    }

    private Direction chooseBestDirection(LngLat start, LngLat end) {
        var firstChoice = T1(start, end);
        if (firstChoice != Direction.HOVER) {
            return firstChoice;
        } else {
            return T2(start, end);
        }
    }

    private Direction T1(LngLat start, LngLat end) {
        var bestDirection = Direction.HOVER; // change from hover
        var bestDistance = start.distanceTo(end);
        for (Direction direction : Direction.values()) {
            var temp = start.nextPosition(direction);
            var outsideNoFlyZones = noFlyAreas.stream()
                    .noneMatch(x -> temp.strictlyInsideArea(x.vertices()));
            if (temp.distanceTo(end) < bestDistance
                    && outsideNoFlyZones
                    // check that this move will not cross any no-fly zones
                    && VisibilityGraph.visible(new Edge(start, temp), noFlyAreas)
                    && VisibilityGraph.visible(new Edge(temp, end), noFlyAreas)) {
                bestDistance = temp.distanceTo(end);
                bestDirection = direction;
            }
        }
        return bestDirection;
    }

    private Direction T2(LngLat start, LngLat end) {
        var bestDirection = Direction.E; // change from hover
        var bestDistance = start.distanceTo(end);
        for (Direction direction : Direction.values()) {
            if (direction != Direction.HOVER) {
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
        }
        return bestDirection;
    }
}
