package uk.ac.ed.inf;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

    private String orderNo;
    private String orderDate;
    private String creditCardNumber;
    private String creditCardExpiry;
    private String cvv;
    private int priceTotalInPence;
    private String[] orderItems;

    private Outcome outcome;

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

    public String[] getOrderItems() {
        return orderItems;
    }

    public int getPriceTotalInPence() {
        return priceTotalInPence;
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public String getCvv() {
        return cvv;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public String getCreditCardExpiry() {
        return creditCardExpiry;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public Outcome getOutcome() {
        return outcome;
    }

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
        findRestaurant(restaurants);
        var restaurant = findRestaurant(restaurants);
        if (restaurant == null) { // If no restaurant has the requested pizza combination
            throw new InvalidPizzaCombinationException();
        } else { // Otherwise sum the pizza costs and add 100 pence for each pizza
            var menu = restaurant.getMenu();
            return Arrays.stream(orderItems)
                    .reduce(DELIVERY_CHARGE, (subtotal, element) -> subtotal
                            + Arrays.stream(menu)
                            .filter(x -> x.name().equals(element))
                            .findFirst().get().priceInPence(), Integer::sum);
        }
    }


    /**
     * Finds the menu of a restaurant, which contains the requested pizza combination.
     * @param restaurants participating restaurants, including their menus.
     * @param pizzas individual pizzas ordered.
     * @return the menus of a restaurant that can serve the requested pizza combination.
     */
    private Menu[] findMenu(Restaurant[] restaurants,
                            String[] pizzas)
    {
        // restaurant can also not be found
        //return restaurant.getMenu();
        return null;
    }

    public Restaurant findRestaurant(List<Restaurant> restaurants) {
        // Find the restaurant which has first pizza
        // Using the fact that no two restaurants have the first pizza
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

    /**
     * Checks that the given menus contain the requested pizza combination.
     * @param menu given menus that should contain the requested pizza combination.
     * @param pizzas individual pizzas ordered
     * @return <code>true</code> if the menus can satisfy the requested pizza combination;
     * <code>false</code> otherwise.
     */
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
    static Order[] getOrdersFromRestServer(URL baseAddress, String date) {
        return new RestClient(baseAddress).deserialise("/orders", date, Order[].class);
    }

    public void validateOrder(List<Restaurant> restaurants) {
        try {
            var calculatedPrice = getDeliveryCost(restaurants);
            if (calculatedPrice != priceTotalInPence) {
                outcome = Outcome.InvalidTotal;
            }
            if (CreditCardValidation.validateCreditCard(
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
        } catch (InvalidExpiryDateException e) {
            outcome = Outcome.InvalidExpiryDate;
        } catch (InvalidCvvException e) {
            outcome = Outcome.InvalidCvv;
        }
    }
}
