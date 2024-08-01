package testproject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OpenSkyAPI {

    private static final String ROOT_URL = "https://opensky-network.org/api";
    private static final Logger LOGGER = Logger.getLogger(OpenSkyAPI.class.getName());

    public static void main(String[] args) {
        String username = "thejimmyboy8";
        String password = "10151999Cs";

        try {
            URL url = new URL(ROOT_URL + "/states/all?lamin=45.8389&lomin=5.9962&lamax=47.8229&lomax=10.5226");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // Add basic authentication header
            String auth = username + ":" + password;
            String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
            connection.setRequestProperty("Authorization", "Basic " + encodedAuth);

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
