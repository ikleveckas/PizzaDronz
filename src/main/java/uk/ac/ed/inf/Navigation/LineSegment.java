package uk.ac.ed.inf.Navigation;

import java.util.List;

/**
 * Representation of a line segment.
 * @param a first endpoint of a line segment.
 * @param b second endpoint of a line segment.
 */
public record LineSegment(LngLat a, LngLat b) {

    /**
     * Checks that the given line segment does not intersect
     * any edges of any area in the given list.
     * @param areas list of areas to check for intersection.
     * @return <code>true</code> if the edge does not intersect any edges in any areas,
     * <code>false</code> otherwise.
     */
    public boolean doesNotIntersectAreas(List<Area> areas) {
        for (Area area : areas) {
            for (LineSegment e: area.getEdges()) {
                if (intersects(e) || e.a().pointBetweenCorners(a(), b())
                        || e.b().pointBetweenCorners(a(), b())) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean intersects (LineSegment other) {
        // Implemented using algorithm and formulas from https://stackoverflow.com/a/24392281
        // Determinant to check if an intersection exists
        var det = (b.lng() - a.lng()) * (other.b.lat() - other.a.lat())
                - (other.b.lng() - other.a.lng()) * (b.lat() - a.lat());
        if (det == 0) { // The intersection does not exist
            return false;
        } else {
            // Distance along the first line segment to a common point
            var lambda = ((other.b.lat() - other.a.lat())
                    * (other.b.lng() - a.lng())
                    + (other.a.lng() - other.b.lng())
                    * (other.b.lat() - a.lat())) / det;
            // Distance along the second line segment to a common point
            var gamma = ((a.lat() - b.lat())
                    * (other.b.lng() - a.lng())
                    + (b.lng() - a.lng())
                    * (other.b.lat() - a.lat())) / det;
            return (0 < lambda && lambda < 1) && (0 < gamma && gamma < 1);
        }
    }
}
