package uk.ac.ed.inf.Navigation;

import com.fasterxml.jackson.annotation.JsonProperty;
import uk.ac.ed.inf.RestClient;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public record Area(List<LngLat> vertices) { // assumes that the last and first vertices are the same
    public List<LngLat> getAdjacent(LngLat vertex) {
        List<LngLat> adjacent = new ArrayList<>();
        if (vertices().size() > 0) {
            /*
            adjacent.add(vertices.get((vertices.indexOf(v) - 1 + vertices().size()) % vertices().size()));
            adjacent.add(vertices.get((vertices.indexOf(v) + 1) % vertices().size()));

             */
            var vertexIndex = vertices.indexOf(vertex);
            if (vertexIndex > 0) { // if the vertex is in the list of vertices
                adjacent.add(vertices.get(vertexIndex - 1));
                // assuming that the first vertex is the same as last in the list
                adjacent.add(vertices.get(vertexIndex + 1));
            }
        }
        return adjacent;
    }

    public List<Edge> getEdges() {
        List<Edge> edges = new ArrayList<>();
        if (vertices().size() > 0) {
            for (int i = 0; i < vertices().size() - 1; i++) {
                edges.add(new Edge(vertices.get(i), vertices.get(i + 1)));
            }
            //edges.add(new Edge(vertices.get(vertices().size() - 1), vertices.get(0)));
        }
        return edges;
    }

    private record noFlyZoneObject(@JsonProperty("coordinates") List<List<Double>> coordinates,
                                   @JsonProperty("name") String name){

    }
    public static List<Area> getNoFlyZones(URL serverBaseAddress) { // needs refactoring
        List<Area> result = new ArrayList<>();

        var noFlyZoneObjects = new RestClient(serverBaseAddress)
                .deserialise("/noFlyZones", noFlyZoneObject[].class);
        for (noFlyZoneObject noFlyZoneObject : noFlyZoneObjects) {
            List<LngLat> coordinates = new ArrayList<>();
            for (List<Double> lngLatPair : noFlyZoneObject.coordinates()) {
                coordinates.add(new LngLat(lngLatPair.get(0), lngLatPair.get(1))); // INDEX EXCEPTION
            }
            result.add(new Area(coordinates));
        }
        return result;
    }
}
