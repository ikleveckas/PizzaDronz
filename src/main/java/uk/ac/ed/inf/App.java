package uk.ac.ed.inf;

import uk.ac.ed.inf.Exceptions.IllegalNumberOfArgumentsException;

/**
 * Main class.
 */
public class App {
    /**
     * Main method. Checks that the number of arguments is correct and passes
     * them to <code>OrderProcessor</code> which validates the arguments and processes
     * the orders.
     * @param args command line arguments.
     * @throws IllegalArgumentException if the arguments are invalid.
     * @throws IllegalNumberOfArgumentsException if the number of arguments is invalid.
     */
    public static void main( String[] args )
            throws IllegalArgumentException, IllegalNumberOfArgumentsException {
        if (args.length != 3) {
            throw new IllegalNumberOfArgumentsException
                    ("Exactly 3 arguments were expected!");
        }
        String dateInput = args[0];
        String baseUrlInput = args[1];
        String seedInput = args[2];
        OrderProcessor orderProcessor = new OrderProcessor(dateInput, baseUrlInput);
        orderProcessor.processOrders();
    }
}
