package uk.ac.ed.inf;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import static uk.ac.ed.inf.Direction.HOVER;

/**
 * Representation of the point on a 2D plane.
 * @param lng longitude of the point
 * @param lat latitude of the point
 */
@JsonIgnoreProperties(value = {"name"})
public record LngLat(@JsonProperty("longitude") double lng,
                     @JsonProperty("latitude") double lat) {

    // The following constants are defined separately because they
    // represent different things. Also, it is easier to change them
    // if needed in the future, since now there is a clear separation.
    public static final double CLOSE = 0.00015;
    public static final double STEP = 0.00015;

    /**
     * Checks if this point is within the Central area.
     * Border and corner values are included in the Central area.
     * @return <code>true</code> if this point is within the Central area;
     * <code>false</code> if the point is not in the Central area or
     * if the Central area coordinates cannot be retrieved from the server.
     */
    public boolean inCentralArea() {
        // The Central area coordinates are accessed using the default REST-service URL.
        // Default URL is represented by null.
        var centralArea = CentralArea.getInstance().getCoordinates(null);
        // avoid null pointer exception if the central area is not retrieved
        if (centralArea != null) {
            return (onCorner(centralArea) ||
                    onBoundary(centralArea) ||
                    strictlyInsideArea(centralArea));
        } else {
            return false;
        }
    }

    /**
     * Checks if this point is inside the given area boundaries.
     * If the point is on the boundary, it is classified as either inside or outside.
     *
     * The idea has been adapted from:
     * <a href="http://www.ecse.rpi.edu/Homepages/wrf/Research/Short_Notes/pnpoly.html">
     *     http://www.ecse.rpi.edu/Homepages/wrf/Research/Short_Notes/pnpoly.html</a>
     * Can also access through the following link:
     * <a href="https://stackoverflow.com/a/8721483">https://stackoverflow.com/a/8721483</a>
     *
     * @param corners the coordinates of the polygon corners.
     * @return <code>true</code> if this point is strictly within the given polygon;
     * <code>false</code> if this point is outside the given area.
     */
    private boolean strictlyInsideArea(LngLat[] corners) {
        boolean result = false;
        int i, j;
        for (i = 0, j = corners.length - 1; i < corners.length; j = i++) {
            // Checks if the edge of the polygon is crossed
            if ((corners[i].lat > lat) != (corners[j].lat > lat) &&
                    (lng < (corners[j].lng - corners[i].lng)
                            * (lat - corners[i].lat)
                            / (corners[j].lat - corners[i].lat)
                            + corners[i].lng)) {
                // Each time the edge is crossed,
                // the point switches between inside and outside
                result = !result;
            }
        }
        return result;
    }

    /**
     * Checks if this point is on one of the polygon edges.
     * @param corners the coordinates of the polygon corners.
     * @return <code>true</code> if this point lies on any of the polygon edges;
     * <code>false</code> otherwise.
     */
    private boolean onBoundary(LngLat[] corners) {
        // Check if the point is between two neighbouring corners
        for (int i = 0; i < corners.length - 1; i++) {
            if (pointBetweenCorners(corners[i], corners[i + 1])) {
                return true;
            }
        }
        // Finally check if the point is between the first and the last corners
        return pointBetweenCorners(corners[corners.length - 1],
                corners[0]);
    }

    /**
     * Checks is this point lies on the line strictly between the two given points.
     * @param corner1 first ending of the line.
     * @param corner2 second ending of the line.
     * @return <code>true</code> if this point is on the line strictly between the two given points;
     * <code>false</code> otherwise.
     */
    public boolean pointBetweenCorners(LngLat corner1, LngLat corner2) {
        return (corner1.distanceTo(corner2)
                == distanceTo(corner1) + distanceTo(corner2))
                && !equals(corner1)
                && !equals(corner2);
    }

    public boolean equals(LngLat other) {
        return lng == other.lng && lat == other.lat;
    }

    /**
     * Checks if this point coordinates match any of the given points.
     * @param corners the coordinates of the polygon corners.
     * @return <code>true</code> if the point coordinates match any of the given points;
     * <code>false</code> otherwise.
     */
    private boolean onCorner(LngLat[] corners) {
        for (int i = 0; i < corners.length; i++) {
            if (lng == corners[i].lng && lat == corners[i].lat) {
                return true;
            }
        }
        return false;
    }

    /**
     * Calculates the Pythagorean distance between this point and the given point.
     * @param other the point to calculate the distance to.
     * @return Pythagorean distance in degrees to the given point.
     */
    public double distanceTo(LngLat other) {
        double squaredDist = Math.pow(lng - other.lng, 2)
                + Math.pow(lat - other.lat, 2);
        return Math.sqrt(squaredDist);
    }

    /**
     * Checks if the distance between this point and the given is strictly less
     * than the constant CLOSE defined in the specification (0.00015)
     * @param other the point to which the distance is checked
     * @return <code>true</code> if the point is within the distance tolerance
     * defined in specification (currently close if distance < 0.00015);
     * <code>false</code> otherwise.
     */
    public boolean closeTo(LngLat other) {
        return distanceTo(other) < CLOSE;
    }

    /**
     * Calculates the next point coordinates, given the enum direction.
     * @param direction one of the 16 compass directions or hover action.
     * @return new LngLat record with the updated coordinates.
     */
    public LngLat nextPosition(Direction direction) {
        if (direction == HOVER) { // Hovering does not change the coordinates
            return new LngLat(lng, lat);
        } else {
            double angleInRad = Math.toRadians(direction.ANGLE.doubleValue());
            double newLng = lng + STEP * Math.cos(angleInRad);
            double newLat = lat + STEP * Math.sin(angleInRad);
            return new LngLat(newLng, newLat);
        }
    }

    public static boolean intersect(LngLat a1, LngLat a2, LngLat b1, LngLat b2) {
        var det = (a2.lng - a1.lng) * (b2.lat - b1.lat)
                - (b2.lng - b1.lng) * (a2.lat - a1.lat);
        if (det == 0) {
            return false;
        } else {
            var lambda = ((b2.lat - b1.lat) * (b2.lng - a1.lng)
                    + (b1.lng - b2.lng) * (b2.lat - a1.lat)) / det;
            var gamma = ((a1.lat - a2.lat) * (b2.lng - a1.lng)
                    + (a2.lng - a1.lng) * (b2.lat - a1.lat)) / det;
            return (0 < lambda && lambda < 1) && (0 < gamma && gamma < 1);
        }
    }


}
