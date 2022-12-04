package uk.ac.ed.inf;

import com.fasterxml.jackson.annotation.JsonProperty;
import uk.ac.ed.inf.Exceptions.*;

import java.net.URL;
import java.util.Arrays;
import java.util.List;

/**
 * Represents the order of a customer.
 */
public class Order{
    public static final int DELIVERY_CHARGE = 100; // in pence
    public static final int MIN_PIZZA_COUNT = 1;
    public static final int MAX_PIZZA_COUNT = 4;

    private final String orderNo;
    private final String orderDate;
    private final String creditCardNumber;
    private final String creditCardExpiry;
    private final String cvv;
    private final int priceTotalInPence;
    private final String[] orderItems;

    private Outcome outcome;

    /**
     * Constructs an order object.
     * @param orderNo {@link #getOrderNo()}
     * @param orderDate the date of an order.
     * @param creditCardNumber the 16 digit credit card number.
     * @param creditCardExpiry the credit card expiry date in MM/YY format.
     * @param cvv the credit card 3 digit cvv number.
     * @param priceTotalInPence the total price of an order in pence.
     * @param orderItems the pizzas included in this order.
     */
    public Order (@JsonProperty("orderNo") String orderNo,
                  @JsonProperty("orderDate") String orderDate,
                  @JsonProperty("creditCardNumber") String creditCardNumber,
                  @JsonProperty("creditCardExpiry") String creditCardExpiry,
                  @JsonProperty("cvv") String cvv,
                  @JsonProperty("priceTotalInPence") int priceTotalInPence,
                  @JsonProperty("orderItems") String[] orderItems) {
        this.orderNo = orderNo;
        this.orderDate = orderDate;
        this.creditCardNumber = creditCardNumber;
        this.creditCardExpiry = creditCardExpiry;
        this.cvv = cvv;
        this.priceTotalInPence = priceTotalInPence;
        this.orderItems = orderItems;
        outcome = Outcome.ValidButNotDelivered;// default
    }

    /**
     * @return the eight-character order number for the pizza order which
     * the drone is currently collecting or delivering
     */
    public String getOrderNo() {
        return orderNo;
    }

    /**
     * @return {@link #setOutcome(Outcome)}
     */
    public Outcome getOutcome() {
        return outcome;
    }

    /**
     * @param outcome the outcome of this order.
     */
    public void setOutcome(Outcome outcome) {
        this.outcome = outcome;
    }

    /**
     * Calculates the <code>int</code> cost in pence of having
     * a valid pizza combination delivered by drone.
     * @param restaurants participating restaurants, including their menus.
     * @return <code>int</code> cost in pence of having all pizzas delivered
     * by the drone, including the delivery charge.
     * @throws InvalidPizzaCombinationException if the pizza combination cannot
     * be delivered from same restaurant
     */
    public int getDeliveryCost(List<Restaurant> restaurants)
            throws InvalidPizzaCombinationException,
            InvalidPizzaCountException,
            InvalidPizzaNotDefinedException {
        // One restaurant can deliver between 1 and 4 pizzas inclusive at a time.
        if (orderItems.length < MIN_PIZZA_COUNT
                || orderItems.length > MAX_PIZZA_COUNT) {
            throw new InvalidPizzaCountException();
        }
        if (!allPizzasExist(restaurants, orderItems)) {
            throw new InvalidPizzaNotDefinedException();
        }
        var restaurant = findRestaurant(restaurants);
        if (restaurant == null) { // If no restaurant has all requested pizzas
            throw new InvalidPizzaCombinationException();
        } else { // Otherwise sum the pizza costs and add 100 pence for each pizza
            var menu = restaurant.getMenu(); // restaurant has all pizzas
            return Arrays.stream(orderItems)
                    .reduce(DELIVERY_CHARGE, (subtotal, element) -> subtotal
                            + Arrays.stream(menu)
                            .filter(x -> x.name().equals(element))
                            .findFirst().get().priceInPence(), Integer::sum);
        }
    }

    /**
     * Finds the restaurant which has first pizza of this order.
     * Using the fact that no two restaurants have the first pizza.
     * @param restaurants the available list of restaurants.
     * @return the restaurant which contains the first pizza,
     * <code>null</code> if no restaurant contains the first pizza.
     */
    public Restaurant findRestaurant(List<Restaurant> restaurants) {
        var restaurant = restaurants.stream()
                .filter(x -> Arrays.stream(x.getMenu())
                        .anyMatch(y -> y.name().equals(orderItems[0])))
                .findFirst();
        // If restaurant has the first pizza, then it should have all pizzas
        if (restaurant.isPresent()
                && allPizzasInMenu(restaurant.get().getMenu(), orderItems)) {
            return restaurant.get();
        } else { // Otherwise no valid restaurant was found
            return null;
        }
    }

    private boolean allPizzasInMenu(Menu[] menu,
                                    String[] pizzas) {
        // Check if all pizzas' names match some menu item name.
        return Arrays.stream(pizzas)
                .allMatch(pizza -> Arrays.stream(menu)
                        .anyMatch(y -> y.name().equals(pizza)));
    }

    private boolean allPizzasExist(List<Restaurant> restaurants,
                                   String[] pizzas) {
        for (String pizza : pizzas) {
            boolean exist = false;
            for (Restaurant restaurant : restaurants) {
                if (Arrays.stream(restaurant.getMenu())
                        .anyMatch(x -> x.name().equals(pizza))) {
                    exist = true;
                    break;
                }
            }
            if (!exist) {
                return false;
            }
        }
        return true;
    }

    /**
     * Collects orders defined in the Rest server into one array.
     * @param baseAddress the base address of the server.
     * @param date the date for which the orders are collected.
     * @return The array of orders for the given day listed on the Rest server.
     */
    public static Order[] getOrdersFromRestServer(URL baseAddress, String date) {
        return new RestClient(baseAddress)
                .deserialize("/orders", date, Order[].class);
    }

    /**
     * Validates the order and sets the appropriate outcome.
     * @param restaurants the available list of restaurants.
     */
    public void validateOrder(List<Restaurant> restaurants) {
        try {
            var calculatedPrice = getDeliveryCost(restaurants);
            if (calculatedPrice != priceTotalInPence) {
                outcome = Outcome.InvalidTotal;
            } else if (CreditCardValidation.validateCreditCard(
                    creditCardNumber, creditCardExpiry,
                    cvv, orderDate)) {
                outcome = Outcome.ValidButNotDelivered;
            }
        } catch (InvalidPizzaCountException e) {
            outcome = Outcome.InvalidPizzaCount;
        } catch (InvalidPizzaCombinationException e) {
            outcome = Outcome.InvalidPizzaCombinationMultipleSuppliers;
        } catch (InvalidPizzaNotDefinedException e) {
            outcome = Outcome.InvalidPizzaNotDefined;
        } catch (InvalidCardNoException e) {
            outcome = Outcome.InvalidCardNumber;
        } catch (InvalidCreditCardExpiryException e) {
            outcome = Outcome.InvalidExpiryDate;
        } catch (InvalidCvvException e) {
            outcome = Outcome.InvalidCvv;
        }
    }


}
