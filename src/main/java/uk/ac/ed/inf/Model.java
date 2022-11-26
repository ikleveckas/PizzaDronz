package uk.ac.ed.inf;

import uk.ac.ed.inf.Exceptions.*;
import uk.ac.ed.inf.Navigation.Area;
import uk.ac.ed.inf.Navigation.Navigator;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Model {
    // get date
    // process orders
    private String date;
    private List<Order> orders;
    private List<Order> validOrders;
    private List<Restaurant> restaurants; // decide between array and list
    private List<Area> noFlyZones;


    public final URL BASE_URL = makeURL("https://ilp-rest.azurewebsites.net/");
    public Model(String date) {
        this.date = date; // should validate date
        orders = Arrays.stream(
                Order.getOrdersFromRestServer(BASE_URL, date)).toList();
        restaurants = Arrays.stream(
                Restaurant.getRestaurantsFromRestServer(BASE_URL)).toList();
        noFlyZones = Area.getNoFlyZones(BASE_URL);
    }

    public void processOrders() {
        orders.forEach(x -> x.validateOrder(restaurants));
        validOrders = orders.stream().
                filter(x -> x.getOutcome() == Outcome.ValidButNotDelivered).toList();
        validOrders.forEach(x -> x.findRestaurant(restaurants).addOrder(x));
        setDistances();
        sortRestaurantsByDistance();
        System.out.println(restaurants.stream()
                .map(p -> Double.toString(p.getTripDistance()))
                .collect(Collectors.joining(" ")));
        Navigator drone = new Navigator(noFlyZones);
        boolean done = false;
        for (Restaurant restaurant : restaurants) {
            if (done) {
                break;
            }
            var ordersToRestaurant = restaurant.getOrdersToRestaurant();
            for (Order order : ordersToRestaurant) {
                try {
                    drone.navigateLoop(restaurant.getCoordinates(), order.getOrderNo());
                    order.setOutcome(Outcome.Delivered);
                } catch (MoveLimitReachedException e) {
                    done = true;
                    break;
                }
            }
        }
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
            return null;
        }
    }

    private void setDistances() {
        for (Restaurant restaurant : restaurants) {
            Navigator navigator = new Navigator(noFlyZones);
            try {
                navigator.navigateLoop(restaurant.getCoordinates(), "");
                restaurant.setTripDistance(navigator.getVisited().size());
            } catch (MoveLimitReachedException e) {
                // in case the restaurant is unreachable from AT
                restaurant.setTripDistance(-1); // negative distance means unreachable
            }
        }
    }


    private void sortRestaurantsByDistance() {
        restaurants = restaurants.stream().sorted(Restaurant::compareTo).collect(Collectors.toList());
    }
}
