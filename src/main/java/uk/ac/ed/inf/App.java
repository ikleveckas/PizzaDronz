package uk.ac.ed.inf;

import uk.ac.ed.inf.Exceptions.IllegalDateFormatException;
import uk.ac.ed.inf.Exceptions.IllegalNumberOfArgumentsException;
import uk.ac.ed.inf.Exceptions.IllegalURLFormatException;

/**
 * Main class.
 */
public class App {
    /**
     * Main method. Checks that the number of arguments is correct and passes
     * them to <code>OrderProcessor</code> which validates the arguments and processes
     * the orders.
     * @param args command line arguments.
     */
    public static void main( String[] args ) throws IllegalNumberOfArgumentsException, IllegalDateFormatException, IllegalURLFormatException {
        if (args.length != 3) {
            throw new IllegalNumberOfArgumentsException();
        }
        String dateInput = args[0];
        String baseUrlInput = args[1];
        String seedInput = args[2];
        OrderProcessor orderProcessor = new OrderProcessor(dateInput, baseUrlInput);
        orderProcessor.processOrders();
    }
}
