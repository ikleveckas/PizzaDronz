package uk.ac.ed.inf;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.PriorityQueue;

public class VisibilityGraph {
    private HashMap<LngLat, List<LngLat>> adjLists;
    private List<LngLat> vertices;
    private LngLat start;
    private LngLat end;
    public VisibilityGraph(List<NoFlyArea> noFlyAreas, LngLat start, LngLat end) {
        this.start = start;
        this.end = end;
        adjLists = new HashMap<>();
        vertices = getAllVertices(noFlyAreas);
        vertices.add(start);
        vertices.add(end);
        buildGraph(noFlyAreas, start, end);
    }


    public List<LngLat> shortestPath() {
        List<LngLat> path = new ArrayList<>();

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
                path.add(0, u);
                u = prev.get(u);
            }
        }
        return path;
    }

    private List<LngLat> getAllVertices(List<NoFlyArea> noFlyAreas) {
        List<LngLat> vertices = new ArrayList<>();
        for (NoFlyArea area : noFlyAreas) {
            vertices.addAll(area.vertices());
        }
        return vertices;
    }

    private void buildGraph(List<NoFlyArea> noFlyAreas, LngLat start, LngLat end) {
        for (NoFlyArea area : noFlyAreas) {
            for (LngLat vertex : area.vertices()) {
                var visibleFromVertex = visibleVertices(vertex, area,
                        noFlyAreas.stream().filter(x -> !area.equals(x)).collect(Collectors.toList()),
                        start, end);
                fillGraph(vertex, visibleFromVertex);
            }
        }

        var visibleFromStart = visibleVertices(start,
                new NoFlyArea(new ArrayList<>()),
                noFlyAreas, start, end);
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

    private List<LngLat> visibleVertices(LngLat vertex, NoFlyArea thisArea,
                                         List<NoFlyArea> otherAreas,
                                         LngLat start, LngLat end) {
        var result = thisArea.getAdjacent(vertex);
        var otherVertices = getAllVertices(otherAreas);
        for (LngLat otherVertex : otherVertices) {
            Edge visibilityLine = new Edge(vertex, otherVertex);
            if (visible(visibilityLine, thisArea, otherAreas)) {
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
        if (visible(visibilityLineEnd, thisArea, otherAreas)) {
            result.add(end);
        }
        return result;
    }

    private boolean visible(Edge visibilityLine, NoFlyArea thisArea, List<NoFlyArea> otherAreas) {
        for (Edge e : thisArea.getEdges()) {
            if (e.intersects(visibilityLine)
                    || e.a().pointBetweenCorners(visibilityLine.a(), visibilityLine.b())
                    || e.b().pointBetweenCorners(visibilityLine.a(), visibilityLine.b())) {
                return false;
            }
        }
        for (NoFlyArea otherArea : otherAreas) {
            for (Edge e: otherArea.getEdges()) {
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
