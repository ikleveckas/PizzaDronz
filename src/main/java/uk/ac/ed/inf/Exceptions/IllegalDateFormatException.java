package uk.ac.ed.inf.Exceptions;

public class IllegalDateFormatException extends Exception{
    public IllegalDateFormatException() {
        super("Incorrect date format. The date format should be YYYY-MM-DD.");
    }
}
