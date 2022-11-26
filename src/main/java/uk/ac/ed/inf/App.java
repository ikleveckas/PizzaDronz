package uk.ac.ed.inf;

import uk.ac.ed.inf.Navigation.Area;
import uk.ac.ed.inf.Navigation.LngLat;
import uk.ac.ed.inf.Navigation.Navigator;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Main class.
 */
public class App {
    public static void main( String[] args ) throws MalformedURLException {
        /*
        LngLat a1 = new LngLat(0, 0);
        LngLat a2 = new LngLat(0, 4);
        LngLat b1 = new LngLat(0, 1);
        LngLat b2 = new LngLat(0, 2);
        System.out.println(LngLat.intersect(a1, a2, b1, b2));
         */
        /*
        List<LngLat> as = new ArrayList<>();
        as.add(new LngLat(1, 3));
        as.add(new LngLat(1, 2));
        as.add(new LngLat(2, 2));
        as.add(new LngLat(2, 3));

        List<LngLat> bs = new ArrayList<>();
        bs.add(new LngLat(3, 4));
        bs.add(new LngLat(3, 1));
        bs.add(new LngLat(4, 1));
        bs.add(new LngLat(4, 4));
*/
        /*
        Area a = new Area(as);
        Area b = new Area(bs);

        List<Area> areas = new ArrayList<>();
        areas.add(a);
        areas.add(b);
        //VisibilityGraph g = new VisibilityGraph(areas, new LngLat(0, 3), new LngLat(5, 3));
        //var path = g.shortestPath();
        Navigator navigator = new Navigator(areas);

         */
        //var visitedPoints = navigator.navigateTo(new LngLat(5, 3));
        /*
        List<Area> noFlyZones = new ArrayList<>();
        try {
            noFlyZones = Area.getNoFlyZones(new URL("https://ilp-rest.azurewebsites.net/"));
        } catch (MalformedURLException e) {

        }
        var navigator = new Navigator(noFlyZones);
        var flightPath = navigator.navigateTo(new LngLat(-3.1913, 55.9455));
        Output.createGeoJSON(flightPath);
         */
        /*
        var orders = Order.getOrdersFromRestServer(
                new URL("https://ilp-rest.azurewebsites.net/"), "2023-04-24");
*/

        Model model = new Model("2023-01-14");
        model.processOrders();


/*
        List<Area> noFlyZones = new ArrayList<>();
        try {
            noFlyZones = Area.getNoFlyZones(new URL("https://ilp-rest.azurewebsites.net/"));
        } catch (MalformedURLException e) {

        }
        var navigator = new Navigator(noFlyZones);
        try {
            navigator.navigateLoop(new LngLat(	-3.2025, 55.9433), "");
        } catch (Exception e) {
            var a = 0;
        }
        System.out.println(navigator.getVisited().size());
        Output.createGeoJSON(navigator.getVisited());
*/

    }
}
