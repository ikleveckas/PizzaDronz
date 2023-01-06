package uk.ac.ed.inf;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.*;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import uk.ac.ed.inf.Exceptions.IllegalDateFormatException;
import uk.ac.ed.inf.Exceptions.IllegalNumberOfArgumentsException;
import uk.ac.ed.inf.Exceptions.IllegalURLFormatException;
import uk.ac.ed.inf.Navigation.Area;
import uk.ac.ed.inf.Navigation.LngLat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class TestSystem {

    String baseAddress = "https://ilp-rest.azurewebsites.net";

    @Test
    void TestDeliveriesFileOutput()
            throws FileNotFoundException, IllegalNumberOfArgumentsException,
            IllegalDateFormatException, IllegalURLFormatException {
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

        // Check that the required file is created
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
    void TestDroneFileOutput()
            throws IOException, IllegalNumberOfArgumentsException,
            IllegalDateFormatException, IllegalURLFormatException {
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

        // Check that the required file is created
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

        // The drone cannot move more than 2000 moves per day + 1 starting position
        assertTrue(coordinates.size() <= 2001);

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
    void TestFlightPathFileOutput()
            throws FileNotFoundException, IllegalNumberOfArgumentsException,
            IllegalDateFormatException, IllegalURLFormatException {
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

        // Check that the required file is created
        assertTrue(new File(fileName).exists());

        // Parse the resulting file
        Gson gson = new Gson();
        List<FlightPath> flightPaths = Arrays.stream(gson.
                fromJson(new FileReader("flightpath-2023-01-04.json"),
                        FlightPath[].class)).toList();

        // The drone cannot move more than 2000 moves per day + 1 starting position
        assertTrue(flightPaths.size() <= 2001);

        // Make sure the starting location is Appleton Tower
        assertEquals(-3.186874, flightPaths.get(0).fromLongitude);
        assertEquals(55.944494, flightPaths.get(0).fromLatitude);

        // Make sure the ending location is within the distance tolerance to Appleton Tower
        assertTrue(getDistance(-3.186874, 55.944494,
                flightPaths.get(flightPaths.size() - 1).fromLongitude,
                flightPaths.get(flightPaths.size() - 1).fromLatitude) < 0.00015);

        double[] possibleAngles = new double[]{0.0, 22.5, 45.0, 67.5, 90.0, 112.5, 135,
                157.5, 180, 202.5, 225.0, 247.5, 270.0, 292.5, 315.0, 337.5};
        for (FlightPath flightPath : flightPaths) {
            var moveDistance = getDistance(flightPath.fromLongitude, flightPath.fromLatitude,
                    flightPath.toLongitude, flightPath.toLatitude);
            if (moveDistance != 0) { // Ignore hovering
                // Test the move distances are equal to 0.00015 except of hovering.
                assertEquals(0.00015, moveDistance, 10E-6);
                // Test the angle is chosen from the available set of angles.
                var present = Arrays.stream(possibleAngles).anyMatch(x -> flightPath.angle.equals(x));
                assertTrue(present);
            }
            else {
                assertNull(flightPath.angle);
            }
            LngLat centralAreaBoundary1 = new LngLat(-3.192473, 55.946233);
            LngLat centralAreaBoundary2 = new LngLat(-3.192473, 55.942617);
            LngLat centralAreaBoundary3 = new LngLat(-3.184319, 55.942617);
            LngLat centralAreaBoundary4 = new LngLat(-3.184319, 55.946233);
            List<LngLat> centralAreaVertices = List.of(centralAreaBoundary1, centralAreaBoundary2,
                    centralAreaBoundary3, centralAreaBoundary4);
            Area centralArea = new Area(centralAreaVertices);

            // Test the drone never enters central area
            assertFalse(new LngLat(flightPath.toLongitude, flightPath.toLongitude).
                    inCentralArea(centralArea));
        }
    }

    @Test
    void TestWrongURL()
            throws IllegalNumberOfArgumentsException, IllegalDateFormatException,
            IllegalURLFormatException {
        String date = "2023-01-04";
        String fakeAddress = "ILoveBeetrotsWithCarbonatedMilk.za";
        String fileName1 = "deliveries-2023-01-04.json";
        String fileName2 = "drone-2023-01-04.geojson";
        String fileName3 = "flightpath-2023-01-04.json";

        // Create File objects
        File file1 = new File(fileName1);
        File file2 = new File(fileName2);
        File file3 = new File(fileName3);

        // Delete the File object to make tests work correctly
        // in case the files exist already
        if (file1.exists()) {
            file1.delete();
        }
        if (file2.exists()) {
            file2.delete();
        }
        if (file3.exists()) {
            file3.delete();
        }

        // Test that an appropriate exception with explanation is thrown
        Throwable exception = assertThrows(IllegalURLFormatException.class,
                () -> App.main(new String[]{date, fakeAddress, "Cabbage"}));
        assertEquals("The given URL is not accessible.", exception.getMessage());

        // Check that the required files are not created
        assertFalse(new File(fileName1).exists());
        assertFalse(new File(fileName2).exists());
        assertFalse(new File(fileName3).exists());
    }
    @Test
    void TestTooManyArguments() throws Exception {
        String date = "2023-01-04";
        String fileName1 = "deliveries-2023-01-04.json";
        String fileName2 = "drone-2023-01-04.geojson";
        String fileName3 = "flightpath-2023-01-04.json";

        // Create File objects
        File file1 = new File(fileName1);
        File file2 = new File(fileName2);
        File file3 = new File(fileName3);

        // Delete the File object to make tests work correctly
        // in case the files existed already
        if (file1.exists()) {
            file1.delete();
        }
        if (file2.exists()) {
            file2.delete();
        }
        if (file3.exists()) {
            file3.delete();
        }

        // Test that an appropriate exception with explanation is thrown
        Throwable exception = assertThrows(IllegalNumberOfArgumentsException.class,
                () -> App.main(new String[]{date, baseAddress, "Cabbage", "Beetroot"}));
        assertEquals("Exactly 3 arguments were expected!", exception.getMessage());
        // Check that the required file is not created as there are too many arguments
        assertFalse(new File(fileName1).exists());
        assertFalse(new File(fileName2).exists());
        assertFalse(new File(fileName3).exists());
    }

    @Test
    void TestNoArguments() {
        String fileName1 = "deliveries-2023-01-04.json";
        String fileName2 = "drone-2023-01-04.geojson";
        String fileName3 = "flightpath-2023-01-04.json";

        // Create File objects
        File file1 = new File(fileName1);
        File file2 = new File(fileName2);
        File file3 = new File(fileName3);

        // Delete the File object to make tests work correctly
        // in case the files existed already
        if (file1.exists()) {
            file1.delete();
        }
        if (file2.exists()) {
            file2.delete();
        }
        if (file3.exists()) {
            file3.delete();
        }

        // Test that an appropriate exception with explanation is thrown
        Throwable exception = assertThrows(IllegalNumberOfArgumentsException.class,
                () -> App.main(new String[]{}));
        assertEquals("Exactly 3 arguments were expected!", exception.getMessage());

        // Check that the required file is not created as there are too many arguments
        assertFalse(new File(fileName1).exists());
        assertFalse(new File(fileName2).exists());
        assertFalse(new File(fileName3).exists());
    }

    @Test
    void TestWrongDate() {
        String date = "chinnnnchin";
        String fileName1 = "deliveries-2023-01-04.json";
        String fileName2 = "drone-2023-01-04.geojson";
        String fileName3 = "flightpath-2023-01-04.json";

        // Create File objects
        File file1 = new File(fileName1);
        File file2 = new File(fileName2);
        File file3 = new File(fileName3);

        // Delete the File object to make tests work correctly
        // in case the files existed already
        if (file1.exists()) {
            file1.delete();
        }
        if (file2.exists()) {
            file2.delete();
        }
        if (file3.exists()) {
            file3.delete();
        }

        // Test that an appropriate exception with explanation is thrown
        Throwable exception = assertThrows(IllegalDateFormatException.class,
                () -> App.main(new String[]{date, baseAddress, "seeeeeeed"}));
        assertEquals("Incorrect date format. The date format should be YYYY-MM-DD.",
                exception.getMessage());

        // Check that the required file is not created as there are too many arguments
        assertFalse(new File(fileName1).exists());
        assertFalse(new File(fileName2).exists());
        assertFalse(new File(fileName3).exists());
    }

    @Test
    void TestSystemPerformanceLightLoad1()
            throws IllegalNumberOfArgumentsException, IllegalDateFormatException, IllegalURLFormatException {
        long startTime = System.currentTimeMillis();
        App.main(new String[]{"2023-02-02", baseAddress, "betjeinemokitaitunubas"});
        long endTime = System.currentTimeMillis();
        // Test the system runtime was less than 60 seconds (60000 ms)
        assertTrue(endTime - startTime <= 60000);
    }

    @Test
    void TestSystemPerformanceLightLoad2()
            throws IllegalNumberOfArgumentsException, IllegalDateFormatException, IllegalURLFormatException {
        long startTime = System.currentTimeMillis();
        App.main(new String[]{"2023-03-20", baseAddress, "jeituskaitaisitatumldc"});
        long endTime = System.currentTimeMillis();

        // Test the system runtime was less than 60 seconds (60000 ms)
        assertTrue(endTime - startTime <= 60000);
    }

    @Test
    void TestSystemPerformanceHeavyLoad()
            throws IllegalNumberOfArgumentsException, IllegalDateFormatException, IllegalURLFormatException {
        long startTime = System.currentTimeMillis();

        // Get orders for all days
        App.main(new String[]{"", baseAddress, "Turmtsitaskaitai?"});
        long elapsedTime = System.currentTimeMillis() - startTime;
        System.out.println(elapsedTime);

        // Test the system runtime was less than 60 seconds (60000 ms)
        assertTrue(elapsedTime <= 60000);
    }


    private double getDistance(double lng1, double lat1, double lng2, double lat2) {
        double squaredDist = Math.pow(lng1 - lng2, 2)
                + Math.pow(lat1 - lat2, 2);
        return Math.sqrt(squaredDist);
    }

    private class FlightPath {
        String orderNo;
        double fromLongitude;
        double fromLatitude;
        Double angle;
        double toLongitude;
        double toLatitude;
        long ticksSinceStartOfCalculation;

        @Override
        public String toString() {
            return String.format("orderNo: %s, fromLongitude: %f, fromLatitude: %f, angle: %f, toLongitude: %f, toLatitude: %f, ticksSinceStartOfCalculation: %d",
                    orderNo, fromLongitude, fromLatitude, angle, toLongitude, toLatitude, ticksSinceStartOfCalculation);
        }
    }
}
