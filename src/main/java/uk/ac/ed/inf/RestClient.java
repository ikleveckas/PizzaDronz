package uk.ac.ed.inf;

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
            var tmpURL = baseUrl.toString();
            if (!tmpURL.endsWith("/") && !fromEndPoint.startsWith("/")) {
                tmpURL += '/';
            } else if (tmpURL.endsWith("/") && fromEndPoint.startsWith("/")) {
                // drop the / from tmpUrl to avoid double /
                tmpURL = tmpURL.substring(0, tmpURL.length() - 1);
            }
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
}
