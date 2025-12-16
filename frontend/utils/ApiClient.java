package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import org.json.JSONArray;
import org.json.JSONObject;

public class ApiClient {
    public static JSONObject postJSON(String apiUrl, JSONObject data, String token) {
        JSONObject res = new JSONObject();
        try {
            URL url = URI.create(apiUrl).toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");

            if (token != null && !token.isEmpty())
                conn.setRequestProperty("Authorization", "Bearer " + token);

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

    // 🔥🔥🔥 PHƯƠNG THỨC MỚI: PUT với Request Body (putJSON) 🔥🔥🔥
    public static JSONObject putJSON(String apiUrl, JSONObject data, String token) {
        JSONObject res = new JSONObject();
        try {
            URL url = URI.create(apiUrl).toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // Thiết lập phương thức là PUT
            conn.setRequestMethod("PUT");

            if (token != null && !token.isEmpty())
                conn.setRequestProperty("Authorization", "Bearer " + token);

            // Cần thiết lập Content-Type và Accept vì chúng ta gửi JSON body
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true); // Bắt buộc cho PUT có body

            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);

            // Gửi Request Body (JSON)
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

    public static JSONObject getJSON(String apiUrl, String token) {
        JSONObject res = new JSONObject();
        try {
            URL url = URI.create(apiUrl).toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");

            if (token != null && !token.isEmpty())
                conn.setRequestProperty("Authorization", "Bearer " + token);

            conn.setRequestProperty("Accept", "application/json");
            conn.setDoInput(true);

            conn.setConnectTimeout(7000); // if cant connect to server within 5s getout
            conn.setReadTimeout(7000); // if server dont res in 5s getout

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
            String body = response.toString();

            // Detect JSON type
            if (body.startsWith("[")) {
                // It's a raw array -> wrap it
                JSONArray arr = new JSONArray(body);
                res.put("array", arr);
            } else {
                // Normal JSON object
                res = new JSONObject(body);
            }
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

    public static JSONObject deleteJSON(String apiUrl, JSONObject data, String token) {
        JSONObject res = new JSONObject();
        try {
            URL url = URI.create(apiUrl).toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // Thiết lập phương thức là DELETE
            conn.setRequestMethod("DELETE");

            if (token != null && !token.isEmpty())
                conn.setRequestProperty("Authorization", "Bearer " + token);

            // Cần thiết lập Content-Type và Accept vì chúng ta gửi JSON body
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true); // Bắt buộc cho DELETE có body

            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);

            // Gửi Request Body (JSON)
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

    public static JSONObject uploadFile(String apiUrl, File file, String token) {
        JSONObject res = new JSONObject();
        String boundary = "---" + System.currentTimeMillis() + "---"; // Chuỗi phân cách các phần dữ liệu
        String LINE_FEED = "\r\n";
        String charset = "UTF-8";

        try {
            URL url = URI.create(apiUrl).toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // 1. Cấu hình Connection
            conn.setUseCaches(false);
            conn.setDoOutput(true); // Cho phép gửi body
            conn.setDoInput(true);
            conn.setRequestMethod("PUT"); // 🔥 Theo yêu cầu của bạn là PUT

            if (token != null && !token.isEmpty()) {
                conn.setRequestProperty("Authorization", "Bearer " + token);
            }

            // Quan trọng: Content-Type phải là multipart/form-data và kèm boundary
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

            // 2. Ghi Body (OutputStream)
            try (OutputStream outputStream = conn.getOutputStream();
                    PrintWriter writer = new PrintWriter(new OutputStreamWriter(outputStream, charset), true)) {

                // --- Bắt đầu phần File ---
                writer.append("--" + boundary).append(LINE_FEED);
                // "avatar" là tên field server mong đợi (cần khớp với backend:
                // avatar/image/file)
                writer.append("Content-Disposition: form-data; name=\"avatar\"; filename=\"" + file.getName() + "\"")
                        .append(LINE_FEED);

                // Xác định Content-Type của file (image/png, image/jpeg...)
                String contentType = java.net.URLConnection.guessContentTypeFromName(file.getName());
                if (contentType == null)
                    contentType = "application/octet-stream";
                writer.append("Content-Type: " + contentType).append(LINE_FEED);

                // Content-Transfer-Encoding
                writer.append("Content-Transfer-Encoding: binary").append(LINE_FEED);
                writer.append(LINE_FEED);
                writer.flush();

                // Ghi dữ liệu Binary của file
                Files.copy(file.toPath(), outputStream);
                outputStream.flush();

                writer.append(LINE_FEED);
                writer.flush();
                // --- Kết thúc phần File ---

                // Kết thúc toàn bộ request multipart
                writer.append("--" + boundary + "--").append(LINE_FEED);
                writer.close();
            }

            // 3. Đọc Response
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

            // Parse kết quả trả về
            if (response.length() > 0) {
                res = new JSONObject(response.toString());
            }
            res.put("httpStatus", status);

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Upload Error: " + e.getMessage());
            res.put("httpStatus", 500);
            res.put("message", "Connection error: " + e.getMessage());
        }

        return res;
    }

    public static String sendGetRequestRaw(String urlStr, String token) throws Exception {
        URL url = URI.create(urlStr).toURL();
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        // 1. Cấu hình Request
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(5000); // Timeout 5 giây
        conn.setReadTimeout(5000);

        // 2. Thêm Headers
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept", "application/json");
        if (token != null && !token.isEmpty()) {
            conn.setRequestProperty("Authorization", "Bearer " + token);
        }

        // 3. Kiểm tra mã lỗi HTTP (404, 500...)
        int responseCode = conn.getResponseCode();
        if (responseCode >= 300) {
            throw new Exception("HTTP Request Failed with Error Code: " + responseCode);
        }

        // 4. Đọc luồng dữ liệu (Stream) thành chuỗi (String)
        // Sử dụng UTF_8 để không bị lỗi font tiếng Việt
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {

            StringBuilder response = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }
            return response.toString(); // Trả về: "[{...}, {...}]"
        }
    }
}