package uk.ac.ed.inf.Exceptions;

public class InvalidCvvException extends Exception{
    public InvalidCvvException() {
        super("The cvv number is invalid.");
    }
}
