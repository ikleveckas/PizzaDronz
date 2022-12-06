package uk.ac.ed.inf;

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
    public static void main( String[] args ) {
        if (args.length != 3) {
            System.err.println("Exactly 3 arguments were expected!");
            System.exit(2);
        }
        String dateInput = args[0];
        String baseUrlInput = args[1];
        String seedInput = args[2];
        OrderProcessor orderProcessor = new OrderProcessor(dateInput, baseUrlInput);
        orderProcessor.processOrders();
    }
}
