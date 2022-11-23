package uk.ac.ed.inf.Navigation;

public record Edge(LngLat a, LngLat b) {
    public boolean intersects(Edge other) {
        return LngLat.intersect(a, b, other.a, other.b);
    }
}
