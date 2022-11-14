package uk.ac.ed.inf;

/**
 * Represents the 16 different compass directions the drone can face.
 * Also contains the hovering, when there is no direction. <p></p>
 */
public enum Direction {

    /*
     * Compass directions are stored in degrees, starting with 0 degrees
     * at East direction, 90 degrees North, 180 degrees West, 270 degrees South.
     * The secondary and tertiary directions represent the directions between
     * these four major compass directions.
     */
    HOVER(null),
    E(0.0),
    ENE(22.5),
    NE(45.0),
    NNE(67.5),
    N(90.0),
    NNW(112.5),
    NW(135.0),
    WNW(157.5),
    W(180.0),
    WSW(202.5),
    SW(225.0),
    SSW(247.5),
    S(270.0),
    SSE(292.5),
    SE(315.0),
    ESE(337.5);

    public final Double ANGLE; // The compass direction in degrees.

    /**
     * Assign the given direction in degrees to an enum value
     * @param angle the compass direction in degrees.
     */
    private Direction(Double angle) {
        this.ANGLE = angle;
    }
}
