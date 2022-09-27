package uk.ac.ed.inf;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.io.IOException ;
import java.net.MalformedURLException;


public class Restaurant {
    private String name;
    private LngLat coordinates;
    private Menu[] menu;
    public Restaurant(@JsonProperty("name") String name,
                      @JsonProperty("longitude") double longitude,
                      @JsonProperty("latitude") double latitude,
                      @JsonProperty("menu") Menu[] menu) {
        this.name = name;
        this.coordinates = new LngLat(longitude, latitude);
        this.menu = menu;
    }

    public Menu[] getMenu() {
        return menu;
    }

    public static Restaurant[] getRestaurantsFromRestServer(URL serverBaseAddress) {
        Restaurant[] payload = null;
        URL url;
        try {
            if (!serverBaseAddress.toString().endsWith("/")) {
                url = new URL(serverBaseAddress + "/restaurants");
            }
            else {
                url = new URL(serverBaseAddress + "restaurants");
            }
            ObjectMapper objectMapper = new ObjectMapper();
            payload = objectMapper.readValue(url, Restaurant[].class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return payload;
    }
}
