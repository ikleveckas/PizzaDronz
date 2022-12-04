package uk.ac.ed.inf.Navigation;
import uk.ac.ed.inf.Exceptions.MoveLimitReachedException;
import uk.ac.ed.inf.Order;
import uk.ac.ed.inf.Outcome;
import uk.ac.ed.inf.Restaurant;
import java.util.*;

/**
 * Logic for drone navigation.
 */
public class Navigator {
    public final LngLat APPLETON = new LngLat(-3.186874, 55.944494); // Appleton Tower
    private final int MAX_MOVES = 2000; // number of moves drone can do before recharging
    private LngLat dronePosition; // current coordinates of the drone
    private final List<Area> noFlyAreas; // no-fly areas which drone does not fly into
    private int movesLeft; // number of moves left before drone battery is exhausted
    private final List<Move> moves; // moves of all successful deliveries
    private List<Move> movesSinceAT; // moves for the last order
    private final List<LngLat> visited; // points visited delivering successful deliveries

    private List<LngLat> visitedSinceAT; // points visited for the last order

    private long startOfCalculation; // time when the calculation of drone moves started

    /**
     * @param noFlyAreas the no-fly areas which the drone cannot fly into.
     */
    public Navigator(List<Area> noFlyAreas) {
        dronePosition = APPLETON;
        movesLeft = MAX_MOVES;
        this.noFlyAreas = noFlyAreas;
        moves = new ArrayList<>();
        movesSinceAT = new ArrayList<>();
        visited = new ArrayList<>();
        visitedSinceAT = new ArrayList<>();
    }

    /**
     * @return the list of locations the drone visits after
     * delivering completed orders.
     */
    public List<LngLat> getVisited() {
        return visited;
    }

    /**
     * @return the list of moves the drone takes after delivering
     * completed orders.
     */
    public List<Move> getMoves() {
        return moves;
    }

    private void hover(String orderNo) throws MoveLimitReachedException {
        if (movesLeft <= 0) {
            throw new MoveLimitReachedException();
        }
        movesLeft -= 1;
        movesSinceAT.add(new Move(orderNo, dronePosition.lng(),
                dronePosition.lat(), Direction.HOVER.ANGLE,
                dronePosition.lng(), dronePosition.lat(),
                startOfCalculation));
        visitedSinceAT.add(dronePosition);
    }

    /**
     * Generates drone moves to collect orders from a given restaurant and
     * go back to Appleton Tower.
     * @param end the endpoint coordinates.
     * @param orderNo the number of an order which the drone is delivering.
     * @throws MoveLimitReachedException if the drone does not have enough moves
     * to reach the given endpoint.
     */
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

    /**
     * Generates drone moves to deliver orders to a given list of restaurants.
     * The algorithm attempts to deliver orders to restaurants
     * in the provided ordering.
     * @param orderedRestaurants ordered list of restaurants.
     */
    public void deliverOrders(List<Restaurant> orderedRestaurants) {
        var outOfBattery = false;
        startOfCalculation = System.nanoTime();
        for (Restaurant restaurant : orderedRestaurants) {
            if (outOfBattery) {
                // stop computation without adding temporary moves for the order
                // during which the drone battery is exhausted
                break;
            }
            var ordersToRestaurant = restaurant.getOrdersToRestaurant();
            for (Order order : ordersToRestaurant) {
                try {
                    navigateLoop(restaurant.getCoordinates(), order.getOrderNo());
                    order.setOutcome(Outcome.Delivered);
                } catch (MoveLimitReachedException e) {
                    outOfBattery = true;
                    break;
                }
            }
        }
    }

    /**
     * Generates path that drone takes while navigating to the given endpoint.
     * @param end endpoint of drone navigation.
     * @param orderNo the number of an order which the drone is delivering.
     * @return list of points the drone moves through while going to the endpoint.
     * @throws MoveLimitReachedException if the drone does not have enough moves
     * to reach the given endpoint.
     */
    public List<LngLat> navigateTo(LngLat end, String orderNo)
            throws MoveLimitReachedException {

        var visibilityGraph = new VisibilityGraph(noFlyAreas, dronePosition, end);
        var path = visibilityGraph.shortestPath();
        path.removeFirst(); // we are already at start so no need to reach it
        for (LngLat next : path) {
            while (!dronePosition.closeTo(next)) {
                if (movesLeft <= 0) {
                    throw new MoveLimitReachedException();
                }
                moveTowardsNextPoint(next, orderNo);
            }
        }
        return visitedSinceAT;
    }

    private void moveTowardsNextPoint(LngLat next, String orderNo) {
        var bestDirection = chooseBestDirection(dronePosition, next);
        var nextPosition = dronePosition.
                nextPosition(bestDirection);
        movesSinceAT.add(new Move(orderNo, dronePosition.lng(),
                dronePosition.lat(), bestDirection.ANGLE,
                nextPosition.lng(), nextPosition.lat(),
                startOfCalculation));
        movesLeft -= 1;
        dronePosition = nextPosition;
        visitedSinceAT.add(dronePosition);
    }

    private Direction chooseBestDirection(LngLat start, LngLat end) {
        var firstChoice = chooseVisiblePath(start, end);
        if (firstChoice != Direction.HOVER) {
            // if possible to make improvement using visible path, prefer visible path
            return firstChoice;
        } else {
            // otherwise choose any path even if it's not visible
            return chooseAnyPath(start, end);
        }
    }

    private Direction chooseVisiblePath(LngLat start, LngLat end) {
        var bestDirection = Direction.HOVER;
        var bestDistance = start.distanceTo(end);
        for (Direction direction : Direction.values()) {
            var temp = start.nextPosition(direction);
            var outsideNoFlyZones = noFlyAreas.stream()
                    .noneMatch(x -> temp.strictlyInsideArea(x.vertices()));
            if (temp.distanceTo(end) < bestDistance
                    && outsideNoFlyZones
                    // check that this move will not cross any no-fly zones
                    && new LineSegment(start, temp).doesNotIntersectAreas(noFlyAreas)
                    // and also the end is visible from the resulting point
                    && new LineSegment(temp, end).doesNotIntersectAreas(noFlyAreas)) {
                bestDistance = temp.distanceTo(end);
                bestDirection = direction;
            }
        }
        return bestDirection;
    }

    private Direction chooseAnyPath(LngLat start, LngLat end) {
        var bestDirection = Direction.E;
        var bestDistance = start.distanceTo(end);
        for (Direction direction : Direction.values()) {
            // do not choose hover as it does not move the drone
            if (direction != Direction.HOVER) {
                var temp = start.nextPosition(direction);
                var outsideNoFlyZones = noFlyAreas.stream()
                        .noneMatch(x -> temp.strictlyInsideArea(x.vertices()));
                if (temp.distanceTo(end) < bestDistance
                        && outsideNoFlyZones
                        // check that this move will not cross any no-fly zones
                        && new LineSegment(start, temp).doesNotIntersectAreas(noFlyAreas)) {
                    bestDistance = temp.distanceTo(end);
                    bestDirection = direction;
                }
            }
        }
        return bestDirection;
    }
}
