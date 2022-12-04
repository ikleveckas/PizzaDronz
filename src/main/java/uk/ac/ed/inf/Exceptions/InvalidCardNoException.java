package uk.ac.ed.inf.Exceptions;

/**
 * Exception used when the credit card number is invalid.
 */
public class InvalidCardNoException extends Exception{
    public InvalidCardNoException() {
        super("The credit card number is invalid.");
    }
}
