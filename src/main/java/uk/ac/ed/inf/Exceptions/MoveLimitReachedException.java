package uk.ac.ed.inf.Exceptions;

/**
 * Exception used when the drone battery is exhausted
 */
public class MoveLimitReachedException extends Exception{
    public MoveLimitReachedException() {
        super("Move limit reached.");
    }
}
