package uk.ac.ed.inf;

import uk.ac.ed.inf.Exceptions.*;
import uk.ac.ed.inf.Navigation.Area;
import uk.ac.ed.inf.Navigation.Navigator;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Logic for processing the orders for the given day.
 */
public class OrderProcessor {
    private String date;
    private final List<Order> orders;
    private final List<Restaurant> restaurants;
    private final List<Area> noFlyZones;
    public URL baseUrl;

    /**
     * Constructs the <code>OrderProcessor</code> object and validates
     * the command line input.
     * @param date the date for which the orders should be processed.
     * @param baseUrl the base URL address of the Rest server.
     */
    public OrderProcessor(String date, String baseUrl) {
        if (validDateISO(date)) {
            this.date = date;
        } else {
            System.err.println("The input date is invalid. Please input date in " +
                    "YYYY-MM-DD format.");
            System.exit(2);
        }
        this.baseUrl = makeURL(baseUrl);
        orders = Order.getOrdersFromRestServer(this.baseUrl, date);
        restaurants = Restaurant.getRestaurantsFromRestServer(this.baseUrl);
        noFlyZones = Area.getNoFlyZones(this.baseUrl);
    }

    /**
     * Processes orders for the given day while aiming to maximise the
     * number of deliveries for valid orders.
     */
    public void processOrders() {
        orders.forEach(x -> x.validateOrder(restaurants));
        List<Order> validOrders = orders.stream().
                filter(x -> x.getOutcome() == OrderOutcome.ValidButNotDelivered).toList();
        validOrders.forEach(x -> x.findRestaurant(restaurants).addOrder(x));
        setDistances();
        Navigator drone = new Navigator(noFlyZones);
        drone.deliverOrders(sortRestaurantsByDistance(restaurants));
        System.out.println(validOrders.stream().filter(x -> x.getOutcome() == OrderOutcome.Delivered).count());
        System.out.println(orders.size());
        System.out.println(validOrders.size());
        Output.createDeliveriesJSON(orders, date);
        Output.createFlightpathJSON(drone.getMoves(), date);
        Output.createGeoJSON(drone.getVisited(), date);
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
                restaurant.setDistanceFromAT(trip.size());
            } catch (MoveLimitReachedException e) {
                // in case the restaurant is unreachable from AT
                restaurant.setDistanceFromAT(-1); // negative distance means unreachable
            }
        }
    }


    private List<Restaurant> sortRestaurantsByDistance(List<Restaurant> restaurantList) {
        return restaurantList.stream()
                .sorted(Restaurant::compareTo).collect(Collectors.toList());
    }

    private boolean validDateISO(String isoDate) {
        SimpleDateFormat isoFormat = new SimpleDateFormat("yy-MM-dd");
        isoFormat.setLenient(false);
        try {
            isoFormat.parse(isoDate);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }
}
