package uk.ac.ed.inf;

import com.mapbox.geojson.*;
import uk.ac.ed.inf.Navigation.LngLat;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Output {
    public static void createGeoJSON(List<LngLat> lngLats){
        List<Point> points = new ArrayList<>();
        for (LngLat lngLat : lngLats) {
            points.add(Point.fromLngLat(lngLat.lng(), lngLat.lat())); // check for exception
        }
        Geometry geometry = LineString.fromLngLats(points);
        Feature feature = Feature.fromGeometry(geometry);
        FeatureCollection featureCollection = FeatureCollection.fromFeature(feature);
        writeToFile(featureCollection.toJson(), "output.geojson");
    }

    private static void writeToFile(String output, String filename) {
        try {
            FileWriter myFile = new FileWriter(filename); // check filename
            myFile.write(output);
            myFile.close();
        } catch (IOException e) {
            System.err.println("Could not write into a file.");
            e.printStackTrace();
            System.exit(2);
        }
    }
}
