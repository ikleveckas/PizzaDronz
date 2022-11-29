package uk.ac.ed.inf.Navigation;

public record Edge(LngLat a, LngLat b) {

    public boolean intersects (Edge other) {
        var det = (b.lng() - a.lng()) * (other.b.lat() - other.a.lat())
                - (other.b.lng() - other.a.lng()) * (b.lat() - a.lat());
        if (det == 0) {
            return false;
        } else {
            var lambda = ((other.b.lat() - other.a.lat()) * (other.b.lng() - a.lng())
                    + (other.a.lng() - other.b.lng()) * (other.b.lat() - a.lat())) / det;
            var gamma = ((a.lat() - b.lat()) * (other.b.lng() - a.lng())
                    + (b.lng() - a.lng()) * (other.b.lat() - a.lat())) / det;
            return (0 < lambda && lambda < 1) && (0 < gamma && gamma < 1);
        }
    }
}
