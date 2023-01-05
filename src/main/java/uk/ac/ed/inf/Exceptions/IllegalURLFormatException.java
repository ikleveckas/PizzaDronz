package uk.ac.ed.inf.Exceptions;

public class IllegalURLFormatException extends Exception{
    public IllegalURLFormatException() {
        super("The given URL is not accessible.");
    }
}
