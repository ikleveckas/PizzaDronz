package uk.ac.ed.inf.Exceptions;

/**
 * Exception used when the cvv number of a credit card is invalid.
 */
public class InvalidCvvException extends Exception{
    public InvalidCvvException() {
        super("The cvv number is invalid.");
    }
}
