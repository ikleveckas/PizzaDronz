package uk.ac.ed.inf;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Main class.
 */
public class App {
    public static void main( String[] args ) {
        /*
        LngLat a1 = new LngLat(0, 0);
        LngLat a2 = new LngLat(0, 4);
        LngLat b1 = new LngLat(0, 1);
        LngLat b2 = new LngLat(0, 2);
        System.out.println(LngLat.intersect(a1, a2, b1, b2));
         */
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

        NoFlyArea a = new NoFlyArea(as);
        NoFlyArea b = new NoFlyArea(bs);

        List<NoFlyArea> areas = new ArrayList<>();
        areas.add(a);
        areas.add(b);
        VisibilityGraph g = new VisibilityGraph(areas, new LngLat(0, 3), new LngLat(5, 3));
        var path = g.shortestPath();
    }
}
