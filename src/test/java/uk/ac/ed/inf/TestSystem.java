package uk.ac.ed.inf;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import static org.junit.jupiter.api.Assertions.*;

public class TestSystem {

    String baseAddress = "https://ilp-rest.azurewebsites.net";

    public TestSystem() throws MalformedURLException {
    }

    @Test
    void TestDeliveriesFileOutput() throws FileNotFoundException {
        String date = "2023-01-04";

        String fileName = "deliveries-2023-01-04.json";

        // Create File object
        File file = new File(fileName);

        // Delete the File object to make tests work correctly
        // in case the files exist already
        if (file.exists()) {
            file.delete();
        }

        App.main(new String[]{date, baseAddress, "Cabbage"});

        // Check that the required files are created
        assertTrue(new File(fileName).exists());

        // Parse the contents of the output file
        JsonElement rootElement = new JsonParser().parse(new FileReader(fileName));
        JsonArray rootArray = rootElement.getAsJsonArray();
        var object0 = rootArray.get(0).getAsJsonObject();

        // Validate the precise details of a delivery in the deliveries file
        assertEquals("57E87C7A", object0.get("orderNo").getAsString());
        assertEquals("2023-01-04", object0.get("orderDate").getAsString());
        assertEquals("95", object0.get("creditCardNumber").getAsString());
        assertEquals("09/25", object0.get("creditCardExpiry").getAsString());
        assertEquals("321", object0.get("cvv").getAsString());
        assertEquals(2500, object0.get("priceTotalInPence").getAsInt());
        assertEquals("InvalidCardNumber", object0.get("outcome").getAsString());

        // Validate the contents of the deliveries file
        int invalidCardNumber = 0;
        int invalidExpiryDate = 0;
        int invalidCvv = 0;
        int invalidTotal = 0;
        int invalidPizzaCount = 0;
        int invalidNotFound = 0;
        int invalidMultipleSuppliers = 0;
        int valid = 0;
        int delivered = 0;
        for (JsonElement element : rootArray) {
            JsonObject object = element.getAsJsonObject();
            switch (object.get("outcome").getAsString())
            {
                case "InvalidCardNumber" -> invalidCardNumber++;
                case "InvalidTotal" -> invalidTotal++;
                case "InvalidCvv" -> invalidCvv++;
                case "InvalidExpiryDate" -> invalidExpiryDate++;
                case "InvalidPizzaCount" -> invalidPizzaCount++;
                case "InvalidPizzaCombinationMultipleSuppliers" -> invalidMultipleSuppliers++;
                case "InvalidPizzaNotDefined" -> invalidNotFound++;
                default -> valid++;
            }
            if (object.get("outcome").getAsString() == "Delivered") {
                delivered++;
            }
        }
        assertEquals(1, invalidCardNumber);
        assertEquals(1, invalidExpiryDate);
        assertEquals(1, invalidCvv);
        assertEquals(1, invalidTotal);
        assertEquals(1, invalidPizzaCount);
        assertEquals(1, invalidNotFound);
        assertEquals(1, invalidMultipleSuppliers);
        assertEquals(40, valid);

        // It is impossible to deliver more than 30 orders even with perfect optimisation.
        assertTrue(delivered <= 30);
    }

    @Test
    void TestDroneFileOutput() throws IOException {
        String date = "2023-01-04";

        String fileName = "drone-2023-01-04.geojson";

        // Create File object
        File file = new File(fileName);

        // Delete the File object to make tests work correctly
        // in case the files exist already
        if (file.exists()) {
            file.delete();
        }
        App.main(new String[]{date, baseAddress, "Cabbage"});

        // Check that the required files are created
        assertTrue(new File(fileName).exists());

        // Parse the contents of the output file
        String fileContents = Files.lines(Paths.get("drone-2023-01-04.geojson"))
                .collect(Collectors.joining("\n"));

        // Create an ObjectMapper instance
        ObjectMapper mapper = new ObjectMapper();

        // Use the ObjectMapper to parse the string into a JsonNode tree
        JsonNode root = mapper.readTree(fileContents);

        // Access the "type" field of the root object
        String type = root.get("type").asText();
        assertEquals("FeatureCollection", type);

        // Access the "features" field, which is an array of feature objects
        JsonNode features = root.get("features");
        assertEquals(1, features.size());

        // Access the "geometry" field of the feature
        JsonNode geometry = features.get(0).get("geometry");

        // Access the "type" field of the geometry object
        String geometryType = geometry.get("type").asText();
        assertEquals("LineString", geometryType);

        // Access the "coordinates" field of the geometry object, which is an array
        JsonNode coordinates = geometry.get("coordinates");

        // The drone cannot move more than 2000 moves per day
        assertTrue(coordinates.size() <= 2000);

        // Make sure the starting location is Appleton Tower
        assertEquals("-3.186874", coordinates.get(0).get(0).toString());
        assertEquals("55.944494", coordinates.get(0).get(1).toString());

        // Make sure the ending location is within the distance tolerance to Appleton Tower
        assertTrue(getDistance(-3.186874, 55.944494,
                coordinates.get(coordinates.size() - 1).get(0).asDouble(),
                coordinates.get(coordinates.size() - 1).get(1).asDouble()) < 0.00015);

        // Make sure the move distances are equal to 0.00015 except of hovering.
        double previousLng = -3.186724;
        double previousLat = 55.944494;
        for (JsonNode point : coordinates) {
            var moveDistance = getDistance(previousLng, previousLat,
                    point.get(0).asDouble(), point.get(1).asDouble());
            if (moveDistance != 0) { // Ignore hovering
                assertEquals(0.00015, moveDistance, 10E-6);
                previousLng = point.get(0).asDouble();
                previousLat = point.get(1).asDouble();
            }
        }
    }

    @Test
    void TestFlightPathFileOutput() throws FileNotFoundException {
        String date = "2023-01-04";

        String fileName = "flightpath-2023-01-04.json";

        // Create File object
        File file = new File(fileName);

        // Delete the File object to make tests work correctly
        // in case the files exist already
        if (file.exists()) {
            file.delete();
        }

        App.main(new String[]{date, baseAddress, "Cabbage"});

        // Check that the required files are created
        assertTrue(new File(fileName).exists());
    }

    private double getDistance(double lng1, double lat1, double lng2, double lat2) {
        double squaredDist = Math.pow(lng1 - lng2, 2)
                + Math.pow(lat1 - lat2, 2);
        return Math.sqrt(squaredDist);
    }
}
