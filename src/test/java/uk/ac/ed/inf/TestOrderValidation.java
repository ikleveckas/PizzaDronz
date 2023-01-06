package uk.ac.ed.inf;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests how the CreditCardValidation integrates with the rest of the order validation functionality.
 * Contains the mock orders and mock restaurants. They are created in order to ensure that the method
 * works without reliance on the REST server.
 */
public class TestOrderValidation {
    // Mock orders
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
    Order order6 = new Order("6915C39D", "2023-04-20",
            "4361903257117952", "11/28", "136",
            12600, new String[]{"Meat Lover", "Vegan Delight", "Meat Lover",
            "Vegan Delight", "Meat Lover", "Vegan Delight", "Meat Lover", "Vegan Delight", "Meat Lover", "Vegan Delight"});
    Order order7 = new Order("0F52FCDF", "2023-04-20",
            "4314323622451564", "06/26", "844",
            3400, new String[]{"Super Cheese", "All Shrooms", "Margarita"});
    Order order8 = new Order("6D619CEF", "2023-04-20",
            "5521918403006150", "02/27", "637",
            2500, new String[]{"Margarita", "Calzone"});
    Order order9 = new Order("556F6E10", "2023-04-20",
            "4318895558890762", "06/24", "157",
            2600, new String[]{"Meat Lover", "Vegan Delight"});
    Order order10 = new Order("6D105E41", "2023-04-20",
            "4199892155787149", "06/28",
            "420", 2400, new String[]{"Super Cheese", "All Shrooms"});
    Order order11 = new Order("34DB59D0", "2023-04-20",
            "2221485286767381", "02/27",
            "140", 2400, new String[]{"Proper Pizza", "Pineapple & Ham & Cheese"});
    Order order12 = new Order("04B82828", "2023-04-20",
            "4783198625820706", "06/26",
            "797", 2500, new String[]{"Margarita", "Calzone"});
    Order order13 = new Order("21D9F7D2", "2023-04-20",
            "5114787736194592", "06/27",
            "076", 2600, new String[]{"Meat Lover", "Vegan Delight"});
    Order order14 = new Order("2E5CD5C5", "2023-04-20",
            "4318895558890762", "02/27", "508",
            2400, new String[]{"Super Cheese", "All Shrooms"});
    Order order15 = new Order("3E6D3F6B", "2023-04-20",
            "2720832604512781", "03/27", "987",
            2400, new String[]{"Proper Pizza", "Pineapple & Ham & Cheese"});
    Order order16 = new Order("65E5D82A", "2023-04-20",
            "5357289011308001", "06/26", "966",
            2500, new String[]{"Margarita", "Calzone"});
    Order order17 = new Order("7F11F3D9", "2023-04-20",
            "2221738182511489", "06/28", "408",
            2600, new String[]{"Meat Lover", "Vegan Delight"});
    Order order18 = new Order("73C4E4D4", "2023-04-20",
            "5307901979176938", "02/27", "317",
            2400, new String[]{"Super Cheese", "All Shrooms"});
    Order order19 = new Order("8F3F3DBC", "2023-04-20",
            "4199892155787149", "02/27", "826",
            2400, new String[]{"Proper Pizza", "Pineapple & Ham & Cheese"});
    Order order20 = new Order("9F3F3DBC", "2023-04-20",
            "5231931756799264", "06/26", "011",
            2500, new String[]{"Margarita", "Calzone"});
    Order order21 = new Order("AF3F3DBC", "2023-04-20",
            "2221738182511489", "06/28", "408",
            2600, new String[]{"Meat Lover", "Vegan Delight"});
    Order order22 = new Order("BF3F3DBC", "2023-04-20",
            "5522487248954403", "02/27", "317",
            2400, new String[]{"Super Cheese", "All Shrooms"});
    Order order23 = new Order("CF3F3DBC", "2023-04-20",
            "2221485286767381", "02/27", "826",
            2400, new String[]{"Proper Pizza", "Pineapple & Ham & Cheese"});
    Order order24 = new Order("DF3F3DBC", "2023-04-20",
            "2720086115039616", "06/26", "011",
            2500, new String[]{"Margarita", "Calzone"});
    Order order25 = new Order("EF3F3DBC", "2023-04-20",
            "4984173030350174", "06/28", "408",
            2600, new String[]{"Meat Lover", "Vegan Delight"});
    Order order26 = new Order("FF3F3DBC", "2023-04-20",
            "4374179558767895", "02/27", "317",
            2400, new String[]{"Super Cheese", "All Shrooms"});
    Order order27 = new Order("GF3F3DBC", "2023-04-20",
            "4621539910166501", "02/27", "826",
            2400, new String[]{"Proper Pizza", "Pineapple & Ham & Cheese"});
    Order order28 = new Order("HF3F3DBC", "2023-04-20",
            "5357289011308001", "06/26", "011",
            2500, new String[]{"Margarita", "Calzone"});
    List<Order> orders = new ArrayList<>(Arrays.asList(order1, order2, order3,
            order4, order5, order6, order7, order8, order9, order10, order11,
            order12, order13, order14, order15, order16, order17, order18,
            order19, order20, order21, order22, order23, order24, order25,
            order26, order27, order28));

    // Mock restaurants
    Restaurant restaurant1 = new Restaurant(-3.1912869215011597, 55.945535152517735,
            new Menu[]{new Menu("Margarita", 1000),
                    new Menu("Calzone", 1400)});
    Restaurant restaurant2 = new Restaurant(-3.202541470527649, 55.943284737579376,
            new Menu[]{new Menu("Meat Lover", 1400),
                    new Menu("Vegan Delight", 1100)});
    Restaurant restaurant3 = new Restaurant(-3.1838572025299072, 55.94449876875712,
            new Menu[]{new Menu("Super Cheese", 1400),
                    new Menu("All Shrooms", 900)});
    Restaurant restaurant4 = new Restaurant(-3.1940174102783203, 55.94390696616939,
            new Menu[]{new Menu("Proper Pizza", 1400),
                    new Menu("Pineapple & Ham & Cheese", 900)});
    List<Restaurant> restaurants = new ArrayList<>(Arrays.asList(restaurant1,
            restaurant2, restaurant3, restaurant4));


    @Test
    void testValidateOrder() {
        // Integration test for the credit card validation and order outcome generation
        
        for(Order order : orders) {
            order.validateOrder(restaurants);
        }
        assertEquals(OrderOutcome.InvalidCardNumber, orders.get(0).getOutcome());
        assertEquals(OrderOutcome.InvalidExpiryDate, orders.get(1).getOutcome());
        assertEquals(OrderOutcome.InvalidCvv, orders.get(2).getOutcome());
        assertEquals(OrderOutcome.InvalidTotal, orders.get(3).getOutcome());
        assertEquals(OrderOutcome.InvalidPizzaNotDefined, orders.get(4).getOutcome());
        assertEquals(OrderOutcome.InvalidPizzaCount, orders.get(5).getOutcome());
        assertEquals(OrderOutcome.InvalidPizzaCombinationMultipleSuppliers, orders.get(6).getOutcome());
        assertEquals(28, orders.size());
        assertEquals(21, orders.stream()
                .filter(x -> x.getOutcome() == OrderOutcome.ValidButNotDelivered).count());
    }
}
