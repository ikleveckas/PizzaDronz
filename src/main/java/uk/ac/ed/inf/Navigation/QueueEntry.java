package uk.ac.ed.inf.Navigation;

public class QueueEntry implements Comparable<QueueEntry> {
    private LngLat key;
    private Double distance;

    private int centralAreaCrossings;
    public QueueEntry(LngLat key, Double distance) {
        this.key = key;
        this.distance = distance;
        centralAreaCrossings = 0;
    }

    public QueueEntry(LngLat key, Double distance,
                      int centralAreaCrossings) {
        this.key = key;
        this.distance = distance;
        this.centralAreaCrossings = centralAreaCrossings;
    }

    public LngLat getKey() {
        return key;
    }

    public void increaseCrossings() {
        centralAreaCrossings++;
    }

    public int getCentralAreaCrossings() {
        return centralAreaCrossings;
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
