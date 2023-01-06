package uk.ac.ed.inf;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestDeserialize {

    @Test
    public void tesDeserializeJSON() throws IOException {
        File file = new File("test-orders.json");
        URL url = file.toURI().toURL();
        var deserializedOrders = Arrays.stream(RestClient.generateResponse(url, Order[].class)).toList();

        Order order1 = new Order("71A08A46", "2023-04-20",
                "1953846368418", "05/25", "662",
                2500, new String[]{"Margarita", "Calzone"});
        Order order2 = new Order("753DD7EC", "2023-04-20",
                "5144321966027803", "01/11", "657",
                2600, new String[]{"Meat Lover", "Vegan Delight"});
        Order order3 = new Order("209DED03", "2023-04-20",
                "5567026379271611", "03/26", "26",
                2400, new String[]{"Super Cheese", "All Shrooms"});
        Order order4 = new Order("24952CB8", "2023-04-20",
                "4557566667712417", "08/28", "012",
                3087, new String[]{"Proper Pizza", "Pineapple & Ham & Cheese"});
        Order order5 = new Order("3B26264E", "2023-04-20",
                "4750584810803814", "04/25", "812",
                2500, new String[]{"Margarita", "Calzone", "Pizza-Surprise -498702880"});

        List<Order> expectedOrders = new ArrayList<>(Arrays.asList(order1, order2, order3,
                order4, order5));

        assertEquals(expectedOrders, deserializedOrders);
    }
    @Test
    public void testDeserializeLargerJSON() throws IOException {
        File file = new File("test-orders-large.json");
        URL url = file.toURI().toURL();
        var deserializedOrders = RestClient.generateResponse(url, Order[].class);

        // Test the number of orders is correct
        assertEquals(7050, deserializedOrders.length);

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
}
