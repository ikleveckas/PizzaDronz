package uk.ac.ed.inf.Exceptions;

/**
 * Exception used when the ordered pizza combination cannot be delivered
 * by the same restaurant.
 */
public class InvalidPizzaCombinationException extends Exception{
    public InvalidPizzaCombinationException() {
        super("Ordered pizza combination cannot be delivered " +
                "by the same restaurant.");
    }
}
