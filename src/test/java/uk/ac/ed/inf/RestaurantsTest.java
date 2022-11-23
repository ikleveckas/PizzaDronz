package uk.ac.ed.inf;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import uk.ac.ed.inf.Exceptions.InvalidPizzaCombinationException;

import java.io.IOException;
import java.net.URL;

public class RestaurantsTest
        extends TestCase
{
    private Restaurant[] participants;
    private Order order = new Order();
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public RestaurantsTest( String testName )
    {
        super( testName );
        try {
            participants =
                    Restaurant.getRestaurantsFromRestServer(
                            new URL("https://ilp-rest.azurewebsites.net/"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( RestaurantsTest.class );
    }

    public void testValidCombination1()
    {
        try {
            assertEquals(order.getDeliveryCost(participants, new String[]{"Margarita", "Calzone", "Margarita"}),
                    3700);
        } catch (InvalidPizzaCombinationException e) {
            fail();
        }
    }

    public void testValidCombination2()
    {
        try {
            assertEquals(order.getDeliveryCost(participants, new String[]{"Super Cheese", "All Shrooms"}),
                    2500);
        } catch (InvalidPizzaCombinationException e) {
            fail();
        }
    }

    public void testNoPizzas()
    {
        try {
            int result = order.getDeliveryCost(participants, new String[]{});
            fail();
        } catch (InvalidPizzaCombinationException e) {
            assertTrue(true);
        }
    }

    public void test5Pizzas()
    {
        try {
            int result = order.getDeliveryCost(participants, new String[]{"Margarita",
                    "Margarita","Margarita","Margarita","Margarita"});
            fail();
        } catch (InvalidPizzaCombinationException e) {
            assertTrue(true);
        }
    }

    public void testInvalidCombination()
    {
        try {
            int result = order.getDeliveryCost(participants, new String[]{"Margarita",
                    "All Shrooms"});
            fail();
        } catch (InvalidPizzaCombinationException e) {
            assertTrue(true);
        }
    }

    public void testPizzaDoesNotExist()
    {
        try {
            int result = order.getDeliveryCost(participants, new String[]{"Omelette au Fromage"});
            fail();
        } catch (InvalidPizzaCombinationException e) {
            assertTrue(true);
        }
    }
}

