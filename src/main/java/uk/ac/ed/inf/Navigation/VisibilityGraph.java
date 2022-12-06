package uk.ac.ed.inf.Navigation;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Graph representation of the no-fly zone vertices and start/end points.
 */
public class VisibilityGraph {

    // lists of adjacent vertices of each vertex
    private final HashMap<LngLat, List<LngLat>> adjLists;
    private final List<LngLat> vertices;
    private final LngLat start;
    private final LngLat end;

    private final List<Area> areas; // obstacle areas

    /**
     * Constructs the visibility graph that connects area vertices and
     * start/end points.
     * @param areas the obstacle areas used in the visibility graph.
     * @param start the start point coordinates.
     * @param end the end point coordinates.
     */
    public VisibilityGraph(List<Area> areas,
                           LngLat start, LngLat end) {
        this.start = start;
        this.end = end;
        this.areas = areas;
        adjLists = new HashMap<>();
        vertices = getAllVertices(areas);
        vertices.add(start);
        vertices.add(end);
        buildGraph(areas, start, end);
    }


    /**
     * Generates the shortest path from start to end in straight lines
     * using Dijkstra's algorithm.
     * @return the shortest path from start point to end point.
     */
    public LinkedList<LngLat> shortestPath() {
        HashMap<LngLat, Double> dist = new HashMap<>();
        HashMap<LngLat, LngLat> prev = new HashMap<>();
        dist.put(start, (double) 0);
        PriorityQueue<QueueEntry> queue = new PriorityQueue<>();
        for (LngLat vertex : vertices) { // initialise values for Dijkstra
            if (!vertex.equals(start)) {
                dist.put(vertex, Double.POSITIVE_INFINITY);
                prev.put(vertex, null);
            }
            queue.add(new QueueEntry(vertex, dist.get(vertex)));
        }
        while (!queue.isEmpty()) { // find the shortest paths using Dijkstra
            var current = queue.poll(); // next shortest path vertex
            if (current.vertex().equals(end)) {
                break; // can finish if the shortest path to end is found
            }
            for (LngLat adjacent : adjLists.get(current.vertex())) {
                var alt = dist.get(current.vertex()) + current.vertex().distanceTo(adjacent);
                if (alt < dist.get(adjacent)) { // update path if an alternative path is shorter
                    dist.replace(adjacent, alt);
                    prev.replace(adjacent, current.vertex());
                    // update distance to adjacent in queue
                    queue.removeIf(x -> x.vertex().equals(adjacent));
                    queue.add(new QueueEntry(adjacent, alt));
                }
            }
        }
        return backTrackPath(start, end, prev);
    }

    private LinkedList<LngLat> backTrackPath(LngLat start, LngLat end,
                                             HashMap<LngLat, LngLat> prev) {
        LinkedList<LngLat> path = new LinkedList<>();
        var u = end;
        if (prev.get(u) != null || u.equals(start)) {
            while (u != null) {
                path.addFirst(u);
                u = prev.get(u);
            }
        }
        return path;
    }

    private List<LngLat> getAllVertices(List<Area> areas) {
        List<LngLat> vertices = new ArrayList<>();
        for (Area area : areas) {
            vertices.addAll(area.vertices());
        }
        return vertices;
    }

    private void buildGraph(List<Area> areas, LngLat start, LngLat end) {
        for (Area area : areas) {
            for (LngLat vertex : area.vertices()) {
                var visibleFromVertex = visibleVertices(vertex, area,
                        areas.stream().filter(x -> !area.equals(x)).collect(Collectors.toList()),
                        end);
                adjLists.put(vertex, visibleFromVertex); // fill graph
            }
        }

        var visibleFromStart = visibleVertices(start,
                new Area(new ArrayList<>()),
                areas, end);
        adjLists.put(start, visibleFromStart);
        // No need to add visibility from end as we are never going to move from the end
    }

    private List<LngLat> visibleVertices(LngLat vertex, Area thisArea,
                                         List<Area> otherAreas,
                                         LngLat end) {
        var result = thisArea.getAdjacent(vertex);
        var otherVertices = getAllVertices(otherAreas);
        for (LngLat otherVertex : otherVertices) {
            if (new LineSegment(vertex, otherVertex).doesNotIntersectAreas(areas)) {
                result.add(otherVertex);
            }
        }
        // No need to build visibility to start, as we are never going to move back to start
        if (new LineSegment(vertex, end).doesNotIntersectAreas(areas)) {
            result.add(end);
        }
        return result;
    }

    private record QueueEntry(LngLat vertex, Double distance)
            implements Comparable<QueueEntry> {

        @Override
            public int compareTo(QueueEntry other) {
                return this.distance.compareTo(other.distance);
            }
        }
}
