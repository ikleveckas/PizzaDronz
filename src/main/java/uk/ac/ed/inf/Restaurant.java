package uk.ac.ed.inf;

import com.fasterxml.jackson.annotation.JsonProperty;
import uk.ac.ed.inf.Navigation.LngLat;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a restaurant that can provide pizzas to the PizzaDronz service.
 */
public class Restaurant {
    private String name; // name of the restaurant
    private LngLat coordinates; // coordinates of the restaurant
    private Menu[] menu; // menu entries of the restaurant
    private double distanceFromAT; // used for sorting purpose
    private List<Order> ordersToRestaurant;
    public Restaurant(@JsonProperty("name") String name,
                      @JsonProperty("longitude") double longitude,
                      @JsonProperty("latitude") double latitude,
                      @JsonProperty("menu") Menu[] menu) {
        this.name = name;
        this.coordinates = new LngLat(longitude, latitude);
        this.menu = menu;
        distanceFromAT = -1; // start with unreachable, will be updated later
        ordersToRestaurant = new ArrayList<>();
    }

    public void addOrder(Order order) {
        ordersToRestaurant.add(order);
    }

    public List<Order> getOrdersToRestaurant() {
        return ordersToRestaurant;
    }

    public double getDistanceFromAT() {
        return distanceFromAT;
    }

    public void setDistanceFromAT(double distanceFromAT) {
        this.distanceFromAT = distanceFromAT;
    }

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
     * @return The array of restaurants which are defined on the server.
     */
    public static Restaurant[] getRestaurantsFromRestServer(URL serverBaseAddress) {
        return new RestClient(serverBaseAddress).deserialise("/restaurants", Restaurant[].class);
    }

    public int compareTo(Restaurant other) {
        if (distanceFromAT == -1 && other.distanceFromAT == -1) {
            return 0;
        } else if (distanceFromAT == -1) {
            return 1;
        } else if (other.distanceFromAT == -1) {
            return -1;
        } else if (distanceFromAT < other.distanceFromAT) {
            return -1;
        } else if (distanceFromAT == other.distanceFromAT) {
            return 0;
        } else {
            return 1;
        }
    }
}
