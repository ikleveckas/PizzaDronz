package uk.ac.ed.inf;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;

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

        String fileName1 = "deliveries-2023-01-04.json";
        String fileName2 = "drone-2023-01-04.geojson";
        String fileName3 = "flightpath-2023-01-04.json";

        // Create File objects
        File file1 = new File(fileName1);
        File file2 = new File(fileName2);
        File file3 = new File(fileName3);

        // Delete the File objects to make tests work correctly
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

        App.main(new String[]{date, baseAddress, "Cabbage"});

        // Check that the required files are created
        assertTrue(new File(fileName1).exists());
        assertTrue(new File(fileName2).exists());
        assertTrue(new File(fileName3).exists());

        // Parse the contents of the output files
        JsonElement rootElement = new JsonParser().parse(new FileReader(fileName1));
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
        for (JsonElement element : rootArray) {
            JsonObject object = element.getAsJsonObject();
            switch (object.get("outcome").getAsString())
            {
                case "InvalidCardNumber" -> invalidCardNumber += 1;
                case "InvalidTotal" -> invalidTotal += 1;
                case "InvalidCvv" -> invalidCvv += 1;
                case "InvalidExpiryDate" -> invalidExpiryDate += 1;
                case "InvalidPizzaCount" -> invalidPizzaCount += 1;
                case "InvalidPizzaCombinationMultipleSuppliers" -> invalidMultipleSuppliers += 1;
                case "InvalidPizzaNotDefined" -> invalidNotFound += 1;
                default -> valid += 1;
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
    }

    @Test
    void TestDeliveriesFileOutput() throws FileNotFoundException {
        String date = "2023-01-04";

        String fileName1 = "deliveries-2023-01-04.json";
        String fileName2 = "drone-2023-01-04.geojson";
        String fileName3 = "flightpath-2023-01-04.json";

        // Create File objects
        File file1 = new File(fileName1);
        File file2 = new File(fileName2);
        File file3 = new File(fileName3);

        // Delete the File objects to make tests work correctly
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

        App.main(new String[]{date, baseAddress, "Cabbage"});

        // Check that the required files are created
        assertTrue(new File(fileName1).exists());
        assertTrue(new File(fileName2).exists());
        assertTrue(new File(fileName3).exists());

        // Parse the contents of the output files
        JsonElement rootElement = new JsonParser().parse(new FileReader(fileName1));
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
        for (JsonElement element : rootArray) {
            JsonObject object = element.getAsJsonObject();
            switch (object.get("outcome").getAsString())
            {
                case "InvalidCardNumber" -> invalidCardNumber += 1;
                case "InvalidTotal" -> invalidTotal += 1;
                case "InvalidCvv" -> invalidCvv += 1;
                case "InvalidExpiryDate" -> invalidExpiryDate += 1;
                case "InvalidPizzaCount" -> invalidPizzaCount += 1;
                case "InvalidPizzaCombinationMultipleSuppliers" -> invalidMultipleSuppliers += 1;
                case "InvalidPizzaNotDefined" -> invalidNotFound += 1;
                default -> valid += 1;
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

        
    }
}
