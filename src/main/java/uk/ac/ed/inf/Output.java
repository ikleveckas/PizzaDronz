package uk.ac.ed.inf;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mapbox.geojson.*;
import uk.ac.ed.inf.Navigation.LngLat;
import uk.ac.ed.inf.Navigation.Move;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Contains the methods for generating output.
 */
public class Output {
    /**
     * Creates a GeoJSON file that contains the drone flightpath.
     * @param lngLats the drone flightpath.
     * @param date the date to be included in the file name.
     */
    public static void createGeoJSON(List<LngLat> lngLats, String date){
        List<Point> points = new ArrayList<>();
        var title = "drone-" + date + ".geojson";
        for (LngLat lngLat : lngLats) {
            points.add(Point.fromLngLat(lngLat.lng(), lngLat.lat())); // check for exception
        }
        Geometry geometry = LineString.fromLngLats(points);
        Feature feature = Feature.fromGeometry(geometry);
        FeatureCollection featureCollection = FeatureCollection.fromFeature(feature);
        writeToFile(featureCollection.toJson(), title);
    }

    /**
     * Creates a JSON file that contains the outcomes for each order.
     * @param orders the list of orders to be written.
     * @param date the date to be included in the file name.
     */
    public static void createDeliveriesJSON(
            @JsonSerialize(contentUsing = ListSerializer.class) List<Order> orders,
            String date) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            var title = "deliveries-" + date + ".json";
            File myFile = new File(title);
            objectMapper.writer().writeValue(myFile, orders);
        } catch (Exception e) {
            System.err.println("A problem occurred while writing to a file");
            System.exit(2);
        }
    }

    /**
     * Creates a JSON file that contains the drone flightpath in moves.
     * @param moves the list of moves the drone took.
     * @param date the date to be included in the file name.
     */
    public static void createFlightpathJSON(
            @JsonSerialize(contentUsing = ListSerializer.class) List<Move> moves,
            String date) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            var title = "flightpath-" + date + ".json";
            File myFile = new File(title);
            moves.forEach(Move::setTicksSinceStartOfCalculation);
            objectMapper.writer().writeValue(myFile, moves);
        } catch (Exception e) {
            System.err.println("A problem occurred while writing to a file");
            System.exit(2);
        }
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

    private static final class ListSerializer<T> extends JsonSerializer<List<T>> {

        /**
         * Serialises the given list of items into a JSON array of records.
         */
        @Override
        public void serialize(List<T> items, JsonGenerator jsonGenerator,
                              SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeStartArray();
            for (T item : items) {
                jsonGenerator.writeStartObject();
                jsonGenerator.writeObjectField("item", item);
                jsonGenerator.writeEndObject();
            }
            jsonGenerator.writeEndArray();
        }
    }
}
