package uk.ac.ed.inf;

import org.junit.jupiter.api.Test;
import uk.ac.ed.inf.Navigation.Area;

import java.net.MalformedURLException;
import java.net.URL;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for RestClient class.
 * Tests that the RestClient can retrieve data from the REST
 * server as expected.
 */
public class TestRestClientRetrieval {

    /**
     * constructor method (used to also handle URL exceptions)
     * @throws MalformedURLException error thrown when an invalid URL is used
     */
    public TestRestClientRetrieval() throws MalformedURLException
    {

    }
    URL baseAddress = new URL("https://ilp-rest.azurewebsites.net");
    URL fakeAddress = new URL("https://carbonatedmilk.net");

    @Test
    void testDeserializeRestaurants() {
        // Test all restaurants are retrieved from the REST server correctly
        Restaurant[] deserializedRestaurants = new RestClient(baseAddress)
                .deserialize("/restaurants", Restaurant[].class);
        double expectedLength = 4;
        double actualLength = deserializedRestaurants.length;
        assertEquals(expectedLength, actualLength);

        double expectedLng = -3.1912869215011597;
        double actualLng = deserializedRestaurants[0].getCoordinates().lng();
        assertEquals(expectedLng, actualLng);

        double expectedLat = 55.945535152517735;
        double actualLat = deserializedRestaurants[0].getCoordinates().lat();
        assertEquals(expectedLat, actualLat);

        double expectedMenuSize = 2;
        double actualMenuSize = deserializedRestaurants[0].getMenu().length;
        assertEquals(expectedMenuSize, actualMenuSize);

        String expectedPizzaName = "Margarita";
        String actualPizzaName = deserializedRestaurants[0].getMenu()[0].name();
        assertEquals(expectedPizzaName, actualPizzaName);

        int expectedPizzaPrice = 1000;
        int actualPizzaPrice = deserializedRestaurants[0].getMenu()[0].priceInPence();
        assertEquals(expectedPizzaPrice, actualPizzaPrice);
    }

    @Test
    void testDeserializeOrders() {
        // Test all orders are retrieved from the REST server correctly
        Order[] deserializedOrders = new RestClient(baseAddress)
                .deserialize("/orders", Order[].class);
        double expectedSize = 7050;
        double actualSize = deserializedOrders.length;
        assertEquals(expectedSize, actualSize);

        // Test the order number is retrieved correctly for the first order
        String expectedOrderNo = "1AFFE082";
        String actualOrderNo = deserializedOrders[0].getOrderNo();
        assertEquals(expectedOrderNo, actualOrderNo);

        // Test the order number is retrieved correctly for the last order
        expectedOrderNo = "0E89B60E";
        actualOrderNo = deserializedOrders[7049].getOrderNo();
        assertEquals(expectedOrderNo, actualOrderNo);

        // Test the order date is retrieved correctly
        String expectedOrderDate = "2023-01-01";
        String actualOrderDate = deserializedOrders[0].getOrderDate();
        assertEquals(expectedOrderDate, actualOrderDate);

        // Test the credit card number is retrieved correctly
        String expectedCreditCardNo = "2402902";
        String actualCreditCardNo = deserializedOrders[0].getCreditCardNumber();
        assertEquals(expectedCreditCardNo, actualCreditCardNo);

        // Test the credit card expiry date is retrieved correctly
        String expectedCreditExpiry = "04/28";
        String actualCreditExpiry = deserializedOrders[0].getCreditCardExpiry();
        assertEquals(expectedCreditExpiry, actualCreditExpiry);

        // Test the credit card cvv is retrieved correctly
        String expectedCvv = "922";
        String actualCvv = deserializedOrders[0].getCvv();
        assertEquals(expectedCvv, actualCvv);

        // Test the price is retrieved correctly
        int expectedPrice = 2400;
        int actualPrice = deserializedOrders[0].getPriceTotalInPence();
        assertEquals(expectedPrice, actualPrice);
    }

    @Test
    void testDeserializeIncorrectAddress() {
        // Test no orders are retrieved for incorrect REST server address
        Order[] deserializedOrders = new RestClient(fakeAddress)
                .deserialize("/orders", Order[].class);
        assertNull(deserializedOrders);
    }

    @Test
    void testDeserializeIncorrectEndpoint() {
        // Test no orders are retrieved for incorrect REST server address
        Order[] deserializedOrders = new RestClient(baseAddress)
                .deserialize("/dasdasd", Order[].class);
        assertNull(deserializedOrders);
    }

    @Test
    void testDeserializeOrdersForAGivenDayWithoutSlash() {
        // Test orders are retrieved correctly for a given day without slash on date
        String date = "2023-03-20";
        Order[] deserializedOrders = new RestClient(baseAddress)
                .deserialize("/orders", date, Order[].class);
        double expected = 47;
        double actual = deserializedOrders.length;
        assertEquals(expected, actual);
    }

    @Test
    void testDeserializeOrdersForAGivenDayWithSlashOnDate() {
        // Test orders are retrieved correctly for a given day with slash on date
        String date = "/2023-03-20";
        Order[] deserializedOrders = new RestClient(baseAddress)
                .deserialize("/orders", date, Order[].class);
        double expected = 47;
        double actual = deserializedOrders.length;
        assertEquals(expected, actual);
    }

    @Test
    void testDeserializeOrdersForAGivenDayWithSlashOnEndPoint() {
        // Test orders are retrieved correctly for a given day with slash on endpoint
        String date = "2023-03-20";
        Order[] deserializedOrders = new RestClient(baseAddress)
                .deserialize("/orders/", date, Order[].class);
        double expected = 47;
        double actual = deserializedOrders.length;
        assertEquals(expected, actual);
    }

    @Test
    void testDeserializeOrdersForAGivenDayWithSlashOnBoth() {
        // Test orders are retrieved correctly for a given day with slashes on date and endpoint
        String date = "/2023-03-20";
        Order[] deserializedOrders = new RestClient(baseAddress)
                .deserialize("/orders/", date, Order[].class);
        double expected = 47;
        double actual = deserializedOrders.length;
        assertEquals(expected, actual);
    }

    @Test
    void testDeserializeAreas() {
        // Test all no-fly zones are retrieved from the REST server correctly
        Area[] deserializedNoFlyZones = new RestClient(baseAddress)
                .deserialize("/noFlyZones", Area[].class);
        double expected = 4;
        double actual = deserializedNoFlyZones.length;
        assertEquals(expected, actual);
    }


}
