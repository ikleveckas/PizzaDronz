package uk.ac.ed.inf.Exceptions;

public class InvalidPizzaNotDefinedException extends Exception{
    public InvalidPizzaNotDefinedException() {
        super("One or more of the ordered pizzas does not exist.");
    }
}
