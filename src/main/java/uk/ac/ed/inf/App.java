package uk.ac.ed.inf;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

/**
 * Hello world!
 *
 */
public class App {
    public static void main( String[] args ) throws MalformedURLException {
        Restaurant[] participants =
                Restaurant.getRestaurantsFromRestServer(
                        new URL("https://ilp-rest.azurewebsites.net/"));
        Arrays.stream(participants[0].getMenu()).toList().forEach(System.out::println);
        Arrays.stream(participants).toList().forEach(x -> Arrays.stream(x.getMenu()).toList().forEach(System.out::println));
    }
}
