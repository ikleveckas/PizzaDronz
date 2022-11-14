package uk.ac.ed.inf;

import java.util.Arrays;

/**
 * Represents the order of a customer.
 */
public class Order {
    public static final int DELIVERY_CHARGE = 100; // in pence
    public static final int MIN_PIZZA_COUNT = 1;
    public static final int MAX_PIZZA_COUNT = 4;

    /**
     * Calculates the <code>int</code> cost in pence of having
     * a valid pizza combination delivered by drone.
     * @param restaurants participating restaurants, including their menus.
     * @param pizzas individual pizzas ordered.
     * @return <code>int</code> cost in pence of having all pizzas delivered
     * by the drone, including the delivery charge.
     * @throws InvalidPizzaCombinationException if the pizza combination cannot
     * be delivered from same restaurant
     */
    public int getDeliveryCost(Restaurant[] restaurants, String[] pizzas)
            throws InvalidPizzaCombinationException {
        // One restaurant can deliver between 1 and 4 pizzas inclusive at a time.
        if (pizzas.length < MIN_PIZZA_COUNT
                || pizzas.length > MAX_PIZZA_COUNT) {
            throw new InvalidPizzaCombinationException();
        }
        var menu = findMenu(restaurants, pizzas);
        if (menu == null) { // If no restaurant has the requested pizza combination
            throw new InvalidPizzaCombinationException();
        } else { // Otherwise sum the pizza costs and add 100 pence for each pizza
            return Arrays.stream(pizzas)
                    .reduce(0, (subtotal, element) -> DELIVERY_CHARGE
                            + subtotal
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
        // Find the restaurant which has first pizza
        // Using the fact that no two restaurants have the first pizza
        var restaurant = Arrays.stream(restaurants)
                .filter(x -> Arrays.stream(x.getMenu())
                        .anyMatch(y -> y.name().equals(pizzas[0])))
                .findFirst();
        // If restaurant has the first pizza, then it should have all pizzas
        if (restaurant.isPresent()
                && allPizzasInMenu(restaurant.get().getMenu(), pizzas)) {
            return restaurant.get().getMenu();
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
}
