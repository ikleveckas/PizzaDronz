package uk.ac.ed.inf.Exceptions;

public class InvalidExpiryDateException extends Exception{
    public InvalidExpiryDateException() {
        super("The credit card is expired.");
    }
}
