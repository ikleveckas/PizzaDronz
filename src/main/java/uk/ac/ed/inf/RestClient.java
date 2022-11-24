package uk.ac.ed.inf;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class RestClient {
    URL baseUrl;

    public RestClient(URL baseUrl) {
        this.baseUrl = baseUrl;
    }

    public URL getBaseUrl() {
        return baseUrl;
    }
    /*

    public void download(String fromEndPoint, String toFileName) {
        URL finalURL = null;
        try { // add checks for / at the end of base
            finalURL = new URL(baseUrl.toString() + fromEndPoint);
        } catch (MalformedURLException e) {
            System.err.println("URL is invalid");
            System.exit(2);
        }

        try (BufferedInputStream in = new BufferedInputStream(finalURL.openStream())) {
            FileOutputStream fileOutputStream =
                    new FileOutputStream()
        }
    }
     */

    public <T> T deserialise(String fromEndPoint, Class<T> klass) {
        URL finalURL = null;
        T response = null;

        try {
            var tmpURL = checkSlash(baseUrl.toString(), fromEndPoint);
            finalURL = new URL(new URL(tmpURL), fromEndPoint);
        } catch (MalformedURLException e) {
            System.err.println("URL is invalid: " + baseUrl + fromEndPoint);
            System.exit(2);
        }

        try {
            response = new ObjectMapper().readValue(finalURL, klass);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }
 // AVOID DOUBLE CODE
    public <T> T deserialise(String fromEndPoint, String date, Class<T> klass) {
        URL finalURL = null;
        T response = null;

        try {
            var tmpURL1 = checkSlash(baseUrl.toString(), fromEndPoint);
            var endpoint = checkSlash(fromEndPoint, date);
            finalURL = new URL(new URL(tmpURL1), endpoint + date);
        } catch (MalformedURLException e) {
            System.err.println("URL is invalid: " + baseUrl + fromEndPoint);
            System.exit(2);
        }

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
