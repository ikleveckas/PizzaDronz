package uk.ac.ed.inf.Exceptions;

public class IllegalNumberOfArgumentsException extends Exception{
    public IllegalNumberOfArgumentsException() {
        super("Exactly 3 arguments were expected!");
    }
}
