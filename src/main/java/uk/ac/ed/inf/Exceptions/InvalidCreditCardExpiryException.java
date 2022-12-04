package uk.ac.ed.inf.Exceptions;

/**
 * Exception used when the expiry date of a credit card is invalid
 */
public class InvalidCreditCardExpiryException extends Exception{
    public InvalidCreditCardExpiryException() {
        super("The credit card is expiry date is invalid.");
    }
}
