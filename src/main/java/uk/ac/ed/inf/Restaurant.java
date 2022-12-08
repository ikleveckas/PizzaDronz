package uk.ac.ed.inf;

import com.fasterxml.jackson.annotation.JsonProperty;
import uk.ac.ed.inf.Navigation.LngLat;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents a restaurant that can provide pizzas to the PizzaDronz service.
 */
public class Restaurant {
    private LngLat coordinates; // coordinates of the restaurant
    private Menu[] menu; // menu entries of the restaurant
    private double distanceFromAT; // used for sorting purpose
    private List<Order> ordersToRestaurant;

    /**
     * Constructs a restaurant object.
     * @param longitude the longitude coordinate of a restaurant.
     * @param latitude the latitude coordinate of a restaurant.
     * @param menu the menu entries in the restaurant.
     */
    public Restaurant(@JsonProperty("longitude") double longitude,
                      @JsonProperty("latitude") double latitude,
                      @JsonProperty("menu") Menu[] menu) {
        this.coordinates = new LngLat(longitude, latitude);
        this.menu = menu;
        distanceFromAT = -1; // start with unreachable, will be updated later
        ordersToRestaurant = new ArrayList<>();
    }

    /**
     * Adds a given order to this restaurant.
     * @param order an order to add to this restaurant order list.
     */
    public void addOrder(Order order) {
        ordersToRestaurant.add(order);
    }

    /**
     * @return the list of orders that are made to this restaurant.
     */
    public List<Order> getOrdersToRestaurant() {
        return ordersToRestaurant;
    }

    /**
     * @param distanceFromAT distance from Appleton tower to this restaurant.
     */
    public void setDistanceFromAT(double distanceFromAT) {
        this.distanceFromAT = distanceFromAT;
    }

    /**
     * @return the coordinates of this restaurant.
     */
    public LngLat getCoordinates() {
        return coordinates;
    }

    /**
     * @return the menu entries of the restaurant
     */
    public Menu[] getMenu() {
        return menu;
    }

    /**
     * Collects restaurants defined in the server into one array.
     * @param serverBaseAddress The base address of the server.
     * @return The list of restaurants which are defined on the server.
     * If there is an error reading server data, an empty list is returned.
     */
    public static List<Restaurant> getRestaurantsFromRestServer(URL serverBaseAddress) {
        var deserialisedRestaurants = new RestClient(serverBaseAddress).
                deserialize("/restaurants", Restaurant[].class);
        if (deserialisedRestaurants != null) {
            return Arrays.stream(deserialisedRestaurants).toList();
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * Comparator for ordering the restaurants in terms of the distance from AT.
     * @param other restaurant to compare.
     * @return <code>0</code> if the distance from AT is equal to the distance
     * from AT to the other restaurant. <code>-1</code> if this restaurant is closer
     * to AT then the other restaurant, <code>1</code> if further.
     */
    public int compareTo(Restaurant other) {
        if (distanceFromAT == -1 && other.distanceFromAT == -1) {
            return 0;
        } else if (distanceFromAT == -1) {
            return 1;
        } else if (other.distanceFromAT == -1) {
            return -1;
        } else return Double.compare(distanceFromAT, other.distanceFromAT);
    }
}
