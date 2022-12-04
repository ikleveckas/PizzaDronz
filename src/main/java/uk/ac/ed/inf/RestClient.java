package uk.ac.ed.inf;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Class for retrieving data from the Rest server.
 */
public class RestClient {
    URL baseUrl;

    /**
     * Constructs the object for data retrieval from the Rest server.
     * @param baseUrl the base URL of the Rest server.
     */
    public RestClient(URL baseUrl) {
        this.baseUrl = baseUrl;
    }

    /**
     * Deserializes data from the Rest server into an object of the given class.
     * @param fromEndPoint the end point for the data retrieval in the Rest server.
     * @param klass the class of the resulting object.
     * @return the value(s) from the rest server collected into this object.
     * @param <T> describes the type parameter.
     */
    public <T> T deserialize(String fromEndPoint, Class<T> klass) {
        URL finalURL = null;
        try {
            var tmpURL = checkSlash(baseUrl.toString(), fromEndPoint);
            finalURL = new URL(new URL(tmpURL), fromEndPoint);
        } catch (MalformedURLException e) {
            System.err.println("URL is invalid: " + baseUrl + fromEndPoint);
            System.exit(2);
        }
        return generateResponse(finalURL, klass);
    }

    /**
     * Deserializes data from the Rest server into an object of the given class.
     * @param fromEndPoint the end point for the data retrieval in the Rest server.
     * @param date used for retrieving more specific data related to the given date.
     * @param klass the class of the resulting object.
     * @return the value(s) from the rest server collected into this object.
     * @param <T> describes the type parameter.
     */
    public <T> T deserialize(String fromEndPoint, String date, Class<T> klass) {
        URL finalURL = null;
        try {
            var tmpURL1 = checkSlash(baseUrl.toString(), fromEndPoint);
            var endpoint = checkSlash(fromEndPoint, date);
            finalURL = new URL(new URL(tmpURL1), endpoint + date);
        } catch (MalformedURLException e) {
            System.err.println("URL is invalid: " + baseUrl + fromEndPoint);
            System.exit(2);
        }
        return generateResponse(finalURL, klass);
    }

    private <T> T generateResponse(URL finalURL, Class<T> klass) {
        T response = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            response = objectMapper.readValue(finalURL, klass);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    private String checkSlash(String first, String second) {
        if (!first.endsWith("/") && !second.startsWith("/")) {
            return first + '/';
        } else if (first.endsWith("/") && second.startsWith("/")) {
            // drop the slash from second string to avoid double slash
            return first.substring(0, first.length() - 1);
        }
        return first;
    }
}
