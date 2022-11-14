package uk.ac.ed.inf;

import java.util.ArrayList;
import java.util.List;

public record NoFlyArea (List<LngLat> vertices) {
    public List<LngLat> getAdjacent(LngLat v) {
        List<LngLat> adjacent = new ArrayList<>();
        if (vertices().size() > 0) {
            adjacent.add(vertices.get((vertices.indexOf(v) - 1 + vertices().size()) % vertices().size()));
            adjacent.add(vertices.get((vertices.indexOf(v) + 1) % vertices().size()));
        }
        return adjacent;
    }

    public List<Edge> getEdges() {
        List<Edge> edges = new ArrayList<>();
        if (vertices().size() > 0) {
            for (int i = 0; i < vertices().size() - 1; i++) {
                edges.add(new Edge(vertices.get(i), vertices.get(i + 1)));
            }
            edges.add(new Edge(vertices.get(vertices().size() - 1), vertices.get(0)));
        }
        return edges;
    }
}
