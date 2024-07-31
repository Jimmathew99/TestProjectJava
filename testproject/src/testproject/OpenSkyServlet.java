package testproject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/api/aircrafts")
public class OpenSkyServlet extends HttpServlet {

    private static final String ROOT_URL = "https://opensky-network.org/api";
    private static final Logger LOGGER = Logger.getLogger(OpenSkyServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        PrintWriter out = resp.getWriter();
        StringBuilder result = new StringBuilder();

        try {
            // Read bounding box parameters from request
            String lamin = req.getParameter("lamin");
            String lomin = req.getParameter("lomin");
            String lamax = req.getParameter("lamax");
            String lomax = req.getParameter("lomax");

            // Construct the API URL with bounding box parameters
            StringBuilder urlBuilder = new StringBuilder(ROOT_URL + "/states/all");
            if (lamin != null && lomin != null && lamax != null && lomax != null) {
                urlBuilder.append("?lamin=").append(lamin)
                          .append("&lomin=").append(lomin)
                          .append("&lamax=").append(lamax)
                          .append("&lomax=").append(lomax);
            }

            URL url = new URL(urlBuilder.toString());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                reader.close();
                out.print(result.toString());
            } else {
                LOGGER.log(Level.SEVERE, "Failed to retrieve state vectors. Response code: {0}", responseCode);
                out.print("{\"error\": \"Failed to retrieve state vectors.\"}");
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error retrieving state vectors", ex);
            out.print("{\"error\": \"Error: " + ex.getMessage() + "\"}");
        } finally {
            out.flush();
            out.close();
        }
    }
}
