package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

import org.json.JSONObject;

public class ApiClient {
    public static JSONObject postJSON(String apiUrl, JSONObject data) {
        JSONObject res = new JSONObject();
        try {
            URL url = URI.create(apiUrl).toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true); // required for POST, PUT, PATCH, DELETE  

            conn.setConnectTimeout(5000); // if cant connect to server within 5s getout
            conn.setReadTimeout(5000); // if server dont res in 5s getout

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = data.toString().getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int status = conn.getResponseCode();
            InputStream inputStream = (status < 400) ? conn.getInputStream() : conn.getErrorStream();

            StringBuilder response = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "utf-8"))) {
                String line;
                while ((line = br.readLine()) != null) {
                    response.append(line.trim());
                }
            }

            conn.disconnect();

            // Convert string response to JSONObject
            res = new JSONObject(response.toString());
            res.put("httpStatus", status);

        } catch (java.net.ConnectException e) {
            System.err.println("❌ Server is offline or unreachable: " + e.getMessage());
        } catch (java.net.UnknownHostException e) {
            System.err.println("❌ Unknown host — check your URL or internet connection.");
        } catch (java.net.SocketTimeoutException e) {
            System.err.println("❌ Connection timed out — server took too long to respond.");
        } catch (IOException e) {
            System.err.println("❌ General I/O error: " + e.getMessage());
        }

        return res;
    }
}
