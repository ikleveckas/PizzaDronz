package uk.ac.ed.inf;

import uk.ac.ed.inf.Exceptions.*;
import uk.ac.ed.inf.Navigation.Area;
import uk.ac.ed.inf.Navigation.Navigator;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class OrderProcessor {
    private String date;
    private List<Order> orders;
    private List<Order> validOrders;
    private List<Restaurant> restaurants; // decide between array and list
    private List<Area> noFlyZones;


    public URL baseUrl;
    public OrderProcessor(String date, String baseUrl) {
        if (CreditCardValidation.validDateISO(date)) {
            this.date = date; // should validate date
        } else {
            System.err.println("Could not assign given date.");
            System.exit(2);
        }
        this.baseUrl = makeURL(baseUrl);
        orders = Arrays.stream(
                Order.getOrdersFromRestServer(this.baseUrl, date)).toList();
        restaurants = Arrays.stream(
                Restaurant.getRestaurantsFromRestServer(this.baseUrl)).toList();
        noFlyZones = Area.getNoFlyZones(this.baseUrl);
    }

    public void processOrders() {
        orders.forEach(x -> x.validateOrder(restaurants));
        validOrders = orders.stream().
                filter(x -> x.getOutcome() == Outcome.ValidButNotDelivered).toList();
        validOrders.forEach(x -> x.findRestaurant(restaurants).addOrder(x));
        setDistances();
        Navigator drone = new Navigator(noFlyZones);
        drone.deliverOrders(sortRestaurantsByDistance(restaurants));
        System.out.println(validOrders.stream().filter(x -> x.getOutcome() == Outcome.Delivered).count());
        System.out.println(orders.size());
        System.out.println(validOrders.size());
        Output.createDeliveriesJSON(orders, date);
        Output.createFlightpathJSON(drone.getMoves(), date);
        Output.createGeoJSON(drone.getVisited());
    }



    private URL makeURL(String string) {
        try {
            return new URL(string);
        } catch (MalformedURLException e) {
            System.err.println("Could not assign the default Rest server URL.");
            e.printStackTrace();
            System.exit(2);
            return null; // will not be reached
        }
    }

    private void setDistances() {
        for (Restaurant restaurant : restaurants) {
            Navigator navigator = new Navigator(noFlyZones);
            try {
                var trip = navigator.navigateTo(restaurant.getCoordinates(), "");
                restaurant.setTripDistance(trip.size());
            } catch (MoveLimitReachedException e) {
                // in case the restaurant is unreachable from AT
                restaurant.setTripDistance(-1); // negative distance means unreachable
            }
        }
    }


    private List<Restaurant> sortRestaurantsByDistance(List<Restaurant> restaurantList) {
        return restaurantList.stream()
                .sorted(Restaurant::compareTo).collect(Collectors.toList());
    }
}
