package uk.ac.ed.inf.Navigation;
import uk.ac.ed.inf.Navigation.Area;
import uk.ac.ed.inf.Navigation.Edge;
import uk.ac.ed.inf.Navigation.LngLat;
import uk.ac.ed.inf.Navigation.QueueEntry;

import java.util.*;
import java.util.stream.Collectors;

public class VisibilityGraph {
    private HashMap<LngLat, List<LngLat>> adjLists;
    private List<LngLat> vertices;
    private LngLat start;
    private LngLat end;

    private List<Area> areas;
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


    public LinkedList<LngLat> shortestPath() {
        //List<LngLat> path = new ArrayList<>();
        LinkedList<LngLat> path = new LinkedList<>();

        HashMap<LngLat, Double> dist = new HashMap<>();
        HashMap<LngLat, LngLat> prev = new HashMap<>();

        dist.put(start, (double) 0);
        PriorityQueue<QueueEntry> Q = new PriorityQueue<>();

        for (LngLat vertex : vertices) {
            if (!vertex.equals(start)) {
                dist.put(vertex, (double) 100000); //using 100000
                prev.put(vertex, null);
            }
            Q.add(new QueueEntry(vertex, dist.get(vertex)));
        }

        while (!Q.isEmpty()) {
            var u = Q.poll();
            if (u.getKey().equals(end)) {
                break;
            }
            for (LngLat v : adjLists.get(u.getKey())) {
                var alt = dist.get(u.getKey()) + u.getKey().distanceTo(v);
                if (alt < dist.get(v)) {
                    dist.replace(v, alt);
                    prev.replace(v, u.getKey());
                    Q.removeIf(x -> x.getKey().equals(v));
                    Q.add(new QueueEntry(v, alt));
                }
            }
        }

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
                        start, end);
                fillGraph(vertex, visibleFromVertex);
            }
        }

        var visibleFromStart = visibleVertices(start,
                new Area(new ArrayList<>()),
                areas, start, end);
        fillGraph(start, visibleFromStart);

        /* No need to add visibility from end as we are never going to move from the end
        var visiblefromEnd = visibleVertices(start,
                new NoFlyArea(new ArrayList<>()),
                noFlyAreas, start, end);
        fillGraph(start, visibleFromStart);
        */
    }

    private void fillGraph(LngLat vertex, List<LngLat> visibleFromVertex) {
        adjLists.put(vertex, visibleFromVertex);
    }

    private List<LngLat> visibleVertices(LngLat vertex, Area thisArea,
                                         List<Area> otherAreas,
                                         LngLat start, LngLat end) {
        var result = thisArea.getAdjacent(vertex);
        var otherVertices = getAllVertices(otherAreas);
        for (LngLat otherVertex : otherVertices) {
            Edge visibilityLine = new Edge(vertex, otherVertex);
            if (visible(visibilityLine, areas)) {
                result.add(otherVertex);
            }
        }
        /* No need to build visibility to start, as we are never going to move back to start
        Edge visibilityLineStart = new Edge(vertex, start);
        if (visible(visibilityLineStart, thisArea, otherAreas)) {
            result.add(start);
        }
         */
        Edge visibilityLineEnd = new Edge(vertex, end);
        if (visible(visibilityLineEnd, areas)) {
            result.add(end);
        }
        return result;
    }

    public static boolean visible(Edge visibilityLine, List<Area> areas) { // need to think where to place this
        for (Area area : areas) {
            for (Edge e: area.getEdges()) {
                if (e.intersects(visibilityLine)
                        || e.a().pointBetweenCorners(visibilityLine.a(), visibilityLine.b())
                        || e.b().pointBetweenCorners(visibilityLine.a(), visibilityLine.b())) {
                    return false;
                }
            }
        }
        return true;
    }
}
