package uk.ac.ed.inf.Exceptions;

public class InvalidPizzaCountException extends Exception{
    public InvalidPizzaCountException() {
        super("Ordered number of pizzas cannot be delivered " +
                "in one order.");
    }
}