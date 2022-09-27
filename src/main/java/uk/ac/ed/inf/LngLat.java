package uk.ac.ed.inf;

public record LngLat(double lng, double lat) {

    public boolean inCentralArea() {
        return true;
    }

    public double distanceTo(LngLat other) {
        double squaredDist = Math.pow(lng - other.lng, 2) + Math.pow(lat - other.lat, 2);
        return Math.sqrt(squaredDist);
    }

    public boolean closeTo(LngLat other) {
        return distanceTo(other) < 0.00015;
    }

    public LngLat nextPosition(double direction) {
        double newLng = lng + 0.00015 * Math.cos(direction);
        double newLat = lat + 0.00015 * Math.sin(direction);
        return new LngLat(newLng, newLat);
    }
}
