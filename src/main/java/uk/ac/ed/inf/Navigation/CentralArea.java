package uk.ac.ed.inf.Navigation;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URL;

/**
 * Singleton that stores the Central area coordinates.
 */
public class CentralArea {
    private static CentralArea instance = new CentralArea();
    private LngLat[] coordinates = null;
    private CentralArea() {
        // Private constructor, so that only one instance can exist
    }

    /**
     * Accesses the server and reads the Central area coordinates.
     * @param serverBaseAddress the base address of the server.
     *                          To use the default address, supply null.
     * @return the coordinates of the central area corners stored in the server.
     * If the server cannot be accessed, null is returned.
     */
    private LngLat[] readRestData(URL serverBaseAddress) {
        LngLat[] payload = null;
        try {
            if (serverBaseAddress == null) {
                serverBaseAddress = new URL("https://ilp-rest.azurewebsites.net/");
            } else if (!serverBaseAddress.toString().endsWith("/")) {
                serverBaseAddress = new URL(serverBaseAddress + "/");
            }
            URL url = new URL(serverBaseAddress + "centralArea");
            ObjectMapper objectMapper = new ObjectMapper();
            payload = objectMapper.readValue(url, LngLat[].class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return payload;
    }

    /**
     * Provides the coordinates of the Central area.
     * Ensures that they are retrieved from the server only once.
     * @param serverBaseAddress the base address of the server.
     *                          To use the default address, supply null.
     * @return the coordinates of the central area corners.
     */
    public LngLat[] getCoordinates(URL serverBaseAddress) {
        if (coordinates == null) {
            coordinates = readRestData(serverBaseAddress);
        }
        return coordinates;
    }

    /**
     * @return the singleton instance that stores the central area coordinates.
     */
    public static CentralArea getInstance() {
        return instance;
    }
}
