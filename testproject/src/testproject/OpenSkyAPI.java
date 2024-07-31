package testproject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OpenSkyAPI {

    private static final String ROOT_URL = "https://opensky-network.org/api";
    private static final Logger LOGGER = Logger.getLogger(OpenSkyAPI.class.getName());

    public static void main(String[] args) {
        try {
            // Retrieve all state vectors
            URL url = new URL(ROOT_URL + "/states/all");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
                reader.close();
            } else {
                LOGGER.log(Level.SEVERE, "Failed to retrieve state vectors. Response code: {0}", responseCode);
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error retrieving state vectors", ex);
        }
    }
}
