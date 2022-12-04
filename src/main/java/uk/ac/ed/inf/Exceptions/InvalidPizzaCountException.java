package uk.ac.ed.inf.Exceptions;

/**
 * Exception used when the ordered number of pizzas is invalid.
 */
public class InvalidPizzaCountException extends Exception{
    public InvalidPizzaCountException() {
        super("Ordered number of pizzas cannot be delivered " +
                "in one order.");
    }
}