package uk.ac.ed.inf.Exceptions;

public class InvalidCardNoException extends Exception{
    public InvalidCardNoException() {
        super("The credit card number is invalid.");
    }
}
