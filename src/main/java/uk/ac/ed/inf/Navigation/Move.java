package uk.ac.ed.inf.Navigation;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Representation of one move of a drone.
 */
public class Move {
    private final String orderNo;
    private final double fromLongitude;
    private final double fromLatitude;
    private final Double angle;
    private final double toLongitude;
    private final double toLatitude;
    private final long startOfCalculation;
    private long ticksSinceStartOfCalculation;

    /**
     * Creates a move object.
     * @param orderNo {@link #getOrderNo()}.
     * @param fromLongitude {@link #getFromLongitude()}.
     * @param fromLatitude {@link #getFromLatitude()}.
     * @param angle {@link #getAngle()}.
     * @param toLongitude {@link #getToLongitude()}.
     * @param toLatitude {@link #getToLatitude()}.
     * @param startOfCalculation the start time of calculations for the day.
     */
    public Move(String orderNo, double fromLongitude, double fromLatitude,
                Double angle, double toLongitude, double toLatitude,
                long startOfCalculation) {
        this.orderNo = orderNo;
        this.fromLongitude = fromLongitude;
        this.fromLatitude = fromLatitude;
        this.angle = angle;
        this.toLongitude = toLongitude;
        this.toLatitude = toLatitude;
        this.startOfCalculation = startOfCalculation;
        this.ticksSinceStartOfCalculation = -1; // default value
    }

    /**
     * Marks the number of ticks it took for this move to finish calculation.
     */
    public void setTicksSinceStartOfCalculation() {
        ticksSinceStartOfCalculation = System.nanoTime() - startOfCalculation;
    }

    /**
     * @return the eight-character order number for the pizza order
     * which the drone is currently collecting or delivering.
     */
    @JsonProperty("orderNo")
    public String getOrderNo() {
        return orderNo;
    }

    /**
     * @return the longitude of the drone at the start of this move.
     */
    @JsonProperty("fromLongitude")
    public double getFromLongitude() {
        return fromLongitude;
    }

    /**
     * @return the latitude of the drone at the start of this move.
     */
    @JsonProperty("fromLatitude")
    public double getFromLatitude() {
        return fromLatitude;
    }

    /**
     * @return the angle of travel of the drone in this move.
     */
    @JsonProperty("angle")
    public Double getAngle() {
        return angle;
    }

    /**
     * @return the longitude of the drone at the end of this move.
     */
    @JsonProperty("toLongitude")
    public double getToLongitude() {
        return toLongitude;
    }

    /**
     * @return the latitude of the drone at the end of this move.
     */
    @JsonProperty("toLatitude")
    public double getToLatitude() {
        return toLatitude;
    }

    /**
     * @return the elapsed ticks since the computation started for the day.
     */
    @JsonProperty("ticksSinceStartOfCalculation")
    public long getTicksSinceStartOfCalculation() {
        return ticksSinceStartOfCalculation;
    }
}
