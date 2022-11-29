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

    public static void createDeliveriesJSON(
            @JsonSerialize(contentUsing = ListSerializer.class) List<Order> orders,
            String date) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String title = "deliveries-" + date + ".json";
            File myFile = new File(title);
            objectMapper.writer().writeValue(myFile, orders);
        } catch (Exception e) {
            System.err.println("A problem occurred while writing to a file");
            System.exit(2);
        }
    }

    public static void createFlightpathJSON(
            @JsonSerialize(contentUsing = ListSerializer.class) List<Move> moves,
            String date) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String title = "flightpath-" + date + ".json";
            File myFile = new File(title);
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


    private class ListSerializer<T> extends JsonSerializer<List<T>> {

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
