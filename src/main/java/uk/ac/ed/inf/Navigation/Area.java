package uk.ac.ed.inf.Navigation;

import com.fasterxml.jackson.annotation.JsonProperty;
import uk.ac.ed.inf.RestClient;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Representation of an area object.
 * @param vertices the coordinates of area vertices.
 *                 Assumes the first and last vertices are the same.
 */
public record Area(List<LngLat> vertices) {
    /**
     * Finds the neighbour vertices that are directly connected
     * to the given vertex by edges.
     * @param vertex the coordinates of a vertex to find adjacent vertices.
     * @return the adjacent vertices.
     */
    public List<LngLat> getAdjacent(LngLat vertex) {
        List<LngLat> adjacent = new ArrayList<>();
        if (vertices().size() > 0) {
            var vertexIndex = vertices.indexOf(vertex);
            if (vertexIndex > 0) { // if the vertex is in the list of vertices
                adjacent.add(vertices.get(vertexIndex - 1));
                // assuming that the first vertex is the same as last in the list
                adjacent.add(vertices.get(vertexIndex + 1));
            }
        }
        return adjacent;
    }

    /**
     * Generates a list of edges which define the boundaries of the area.
     * @return a list of edges that bound this area.
     */
    public List<LineSegment> getEdges() {
        List<LineSegment> edges = new ArrayList<>();
        if (vertices().size() > 0) {
            for (int i = 0; i < vertices().size() - 1; i++) {
                edges.add(new LineSegment(vertices.get(i), vertices.get(i + 1)));
            }
        }
        return edges;
    }

    /**
     * Generates a list of no-fly zones based on the data on the server.
     * @param serverBaseAddress the base address of the server.
     * @return list of no-fly zones stored in the server.
     * If the server cannot be accessed, empty list is returned.
     */
    public static List<Area> getNoFlyZones(URL serverBaseAddress) {
        List<Area> result = new ArrayList<>();
        var noFlyZoneObjects = new RestClient(serverBaseAddress)
                .deserialize("/noFlyZones", noFlyZoneObject[].class);
        try {
            for (noFlyZoneObject noFlyZoneObject : noFlyZoneObjects) {
                List<LngLat> coordinates = new ArrayList<>();
                for (List<Double> lngLatPair : noFlyZoneObject.coordinates()) {
                    coordinates.add(new LngLat(lngLatPair.get(0), lngLatPair.get(1)));
                }
                result.add(new Area(coordinates));
            }
        } catch (IndexOutOfBoundsException e) {
            return new ArrayList<>(); // empty area in case data is corrupted on the server
        }
        return result;
    }

    private record noFlyZoneObject(@JsonProperty("coordinates")
                                   List<List<Double>> coordinates,
                                   @JsonProperty("name") String name){
    }
}
