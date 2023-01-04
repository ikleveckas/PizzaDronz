package uk.ac.ed.inf;

import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Integration test of order validation using the REST server
 */
public class TestOrderIntegrationWithREST {

    public TestOrderIntegrationWithREST() throws MalformedURLException
    {

    }

    URL baseAddress = new URL("https://ilp-rest.azurewebsites.net");

    @Test
    void testOrderValidationForAllOrders()
    {
        // Access the REST server to retrieve restaurants and orders instead of using the mocks
        List<Restaurant> restaurantList = Restaurant.getRestaurantsFromRestServer(baseAddress);
        List<Order> orders = Arrays.stream(new RestClient(baseAddress)
                .deserialize("/orders", Order[].class)).toList();
        orders.forEach(order -> order.validateOrder(restaurantList));
        // checks being performed
        assertEquals(150, orders.stream()
                .filter(x -> x.getOutcome() == OrderOutcome.InvalidCardNumber).count());
        assertEquals(150, orders.stream()
                .filter(x -> x.getOutcome() == OrderOutcome.InvalidExpiryDate).count());
        assertEquals(150, orders.stream()
                .filter(x -> x.getOutcome() == OrderOutcome.InvalidCvv).count());
        assertEquals(150, orders.stream()
                .filter(x -> x.getOutcome() == OrderOutcome.InvalidTotal).count());
        assertEquals(150, orders.stream()
                .filter(x -> x.getOutcome() == OrderOutcome.InvalidPizzaCount).count());
        assertEquals(150, orders.stream()
                .filter(x -> x.getOutcome() == OrderOutcome.InvalidPizzaNotDefined).count());
        assertEquals(150, orders.stream()
                .filter(x -> x.getOutcome() == OrderOutcome.InvalidPizzaCombinationMultipleSuppliers).count());
        assertEquals(6000, orders.stream()
                .filter(x -> x.getOutcome() == OrderOutcome.ValidButNotDelivered).count());
    }

    @Test
    void testOrderValidationForSpecificDay1()
    {
        // Access the REST server to retrieve restaurants and orders instead of using the mocks
        List<Restaurant> restaurantList = Restaurant.getRestaurantsFromRestServer(baseAddress);
        List<Order> orders = Arrays.stream(new RestClient(baseAddress)
                .deserialize("/orders", "2023-01-03", Order[].class)).toList();
        orders.forEach(order -> order.validateOrder(restaurantList));
        // checks being performed
        assertEquals(1, orders.stream()
                .filter(x -> x.getOutcome() == OrderOutcome.InvalidCardNumber).count());
        assertEquals(1, orders.stream()
                .filter(x -> x.getOutcome() == OrderOutcome.InvalidExpiryDate).count());
        assertEquals(1, orders.stream()
                .filter(x -> x.getOutcome() == OrderOutcome.InvalidCvv).count());
        assertEquals(1, orders.stream()
                .filter(x -> x.getOutcome() == OrderOutcome.InvalidTotal).count());
        assertEquals(1, orders.stream()
                .filter(x -> x.getOutcome() == OrderOutcome.InvalidPizzaCount).count());
        assertEquals(1, orders.stream()
                .filter(x -> x.getOutcome() == OrderOutcome.InvalidPizzaNotDefined).count());
        assertEquals(1, orders.stream()
                .filter(x -> x.getOutcome() == OrderOutcome.InvalidPizzaCombinationMultipleSuppliers).count());
        assertEquals(40, orders.stream()
                .filter(x -> x.getOutcome() == OrderOutcome.ValidButNotDelivered).count());
    }

    @Test
    void testOrderValidationForSpecificDay2()
    {
        // Access the REST server to retrieve restaurants and orders instead of using the mocks
        List<Restaurant> restaurantList = Restaurant.getRestaurantsFromRestServer(baseAddress);
        List<Order> orders = Arrays.stream(new RestClient(baseAddress)
                .deserialize("/orders", "2023-04-26", Order[].class)).toList();
        orders.forEach(order -> order.validateOrder(restaurantList));
        // checks being performed
        assertEquals(1, orders.stream()
                .filter(x -> x.getOutcome() == OrderOutcome.InvalidCardNumber).count());
        assertEquals(1, orders.stream()
                .filter(x -> x.getOutcome() == OrderOutcome.InvalidExpiryDate).count());
        assertEquals(1, orders.stream()
                .filter(x -> x.getOutcome() == OrderOutcome.InvalidCvv).count());
        assertEquals(1, orders.stream()
                .filter(x -> x.getOutcome() == OrderOutcome.InvalidTotal).count());
        assertEquals(1, orders.stream()
                .filter(x -> x.getOutcome() == OrderOutcome.InvalidPizzaCount).count());
        assertEquals(1, orders.stream()
                .filter(x -> x.getOutcome() == OrderOutcome.InvalidPizzaNotDefined).count());
        assertEquals(1, orders.stream()
                .filter(x -> x.getOutcome() == OrderOutcome.InvalidPizzaCombinationMultipleSuppliers).count());
        assertEquals(40, orders.stream()
                .filter(x -> x.getOutcome() == OrderOutcome.ValidButNotDelivered).count());
    }
}
