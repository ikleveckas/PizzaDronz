package uk.ac.ed.inf;

public class QueueEntry implements Comparable<QueueEntry> {
    private LngLat key;
    private Double distance;
    public QueueEntry(LngLat key, Double distance) {
        this.key = key;
        this.distance = distance;
    }

    public LngLat getKey() {
        return key;
    }

    @Override
    public int compareTo(QueueEntry other) {
        if (this.distance < other.distance) {
            return -1;
        } else if (this.distance == other.distance) {
            return 0;
        } else {
            return 1;
        }
    }
}
