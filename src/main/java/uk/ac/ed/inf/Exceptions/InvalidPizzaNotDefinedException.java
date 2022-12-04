package uk.ac.ed.inf.Exceptions;

/**
 * Exception used when at least one of the ordered pizzas is invalid
 */
public class InvalidPizzaNotDefinedException extends Exception{
    public InvalidPizzaNotDefinedException() {
        super("One or more of the ordered pizzas does not exist.");
    }
}
