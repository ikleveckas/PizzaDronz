package uk.ac.ed.inf.Navigation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import static uk.ac.ed.inf.Navigation.Direction.HOVER;

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
    public static final double MOVE_DISTANCE = 0.00015;

    /**
     * Checks if this point is inside the given area boundaries.
     * If the point is on the boundary, it is classified as either inside or outside.
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
    public boolean strictlyInsideArea(List<LngLat> corners) {
        boolean result = false;
        int i, j;
        for (i = 0, j = corners.size() - 1; i < corners.size(); j = i++) {
            // Checks if the edge of the polygon is crossed
            if ((corners.get(i).lat > lat) != (corners.get(j).lat > lat) &&
                    (lng < (corners.get(j).lng - corners.get(i).lng)
                            * (lat - corners.get(i).lat)
                            / (corners.get(j).lat - corners.get(i).lat)
                            + corners.get(i).lng)) {
                // Each time the edge is crossed,
                // the point switches between inside and outside
                result = !result;
            }
        }
        return result;
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

    /**
     * Checks if the other LngLat object has equal coordinates to this
     * LngLat object.
     * @param other the other LngLat object.
     * @return <code>true</code> if the other LngLat object has equal
     * coordinates, <code>false</code> otherwise.
     */
    public boolean equals(LngLat other) {
        return lng == other.lng && lat == other.lat;
    }

    /**
     * Calculates the Pythagorean distance between this point and
     * the given point.
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
     * defined in specification (currently close if distance less than 0.00015);
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
            double angleInRad = Math.toRadians(direction.ANGLE);
            double newLng = lng + MOVE_DISTANCE * Math.cos(angleInRad);
            double newLat = lat + MOVE_DISTANCE * Math.sin(angleInRad);
            return new LngLat(newLng, newLat);
        }
    }
}
