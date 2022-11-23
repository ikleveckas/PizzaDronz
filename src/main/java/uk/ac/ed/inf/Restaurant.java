package uk.ac.ed.inf;

import com.fasterxml.jackson.annotation.JsonProperty;
import uk.ac.ed.inf.Navigation.LngLat;

import java.net.URL;

/**
 * Represents a restaurant that can provide pizzas to the PizzaDronz service.
 */
public class Restaurant {
    private String name; // name of the restaurant
    private LngLat coordinates; // coordinates of the restaurant
    private Menu[] menu; // menu entries of the restaurant
    public Restaurant(@JsonProperty("name") String name,
                      @JsonProperty("longitude") double longitude,
                      @JsonProperty("latitude") double latitude,
                      @JsonProperty("menu") Menu[] menu) {
        this.name = name;
        this.coordinates = new LngLat(longitude, latitude);
        this.menu = menu;
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
}
