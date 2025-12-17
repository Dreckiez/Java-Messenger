package services;

import java.io.File;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Iterator;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;

import utils.ApiClient;
import utils.ApiUrl;
import utils.UserSession;

public class UserServices {
    public static JSONObject getMyProfile(String token) {
        return ApiClient.getJSON(ApiUrl.MYPROFILE, token);
    }

    public JSONObject updateProfile(String token, JSONObject updateData) {
        JSONObject result = new JSONObject();
        try {
            String url = ApiUrl.BASE + "chat/user/profile/update-profile";

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + token)
                    .method("PATCH", HttpRequest.BodyPublishers.ofString(updateData.toString()))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String responseBody = response.body();

            if (response.statusCode() == 200) {
                // Thành công: Parse body trả về (chứa userProfile, token...)
                return new JSONObject(responseBody);
            } else {
                // Thất bại: Xử lý lỗi và đóng gói vào key "error"
                StringBuilder errorMsg = new StringBuilder();
                try {
                    JSONObject errJson = new JSONObject(responseBody);
                    // Parse lỗi validation dạng map
                    Iterator<String> keys = errJson.keys();
                    while (keys.hasNext()) {
                        String key = keys.next();
                        // Bỏ qua các key hệ thống nếu có
                        if (!key.equals("status") && !key.equals("timestamp")) {
                            errorMsg.append("- ").append(errJson.optString(key)).append("\n");
                        }
                    }
                    if (errorMsg.length() == 0 && errJson.has("message")) {
                        errorMsg.append(errJson.getString("message"));
                    }
                } catch (Exception e) {
                    errorMsg.append("Error ").append(response.statusCode());
                }

                if (errorMsg.length() == 0)
                    errorMsg.append("Unknown error occurred");

                result.put("error", errorMsg.toString());
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.put("error", "Connection error: " + e.getMessage());
            return result;
        }
    }

    public String changePassword(String token, String oldPassword, String newPassword, String confirmPassword) {
        try {
            String url = ApiUrl.BASE + "chat/user/profile/change-password";

            JSONObject jsonBody = new JSONObject();
            jsonBody.put("oldPassword", oldPassword);
            jsonBody.put("newPassword", newPassword);
            jsonBody.put("confirmPassword", confirmPassword);

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + token)
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody.toString()))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String responseBody = response.body();

            if (response.statusCode() == 200) {
                return null; // Success (No error message)
            } else {
                // Try to parse error message from JSON
                try {
                    JSONObject errJson = new JSONObject(responseBody);
                    if (errJson.has("message")) {
                        return errJson.getString("message");
                    }
                    if (errJson.has("error")) {
                        return errJson.getString("error");
                    }
                } catch (Exception e) {
                    // Ignore parsing errors
                }
                // Fallback error message
                return responseBody != null && !responseBody.isEmpty() ? responseBody
                        : "Error " + response.statusCode();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Connection error: " + e.getMessage();
        }
    }

    public JSONObject updateAvatar(String token, File file) {
        JSONObject result = new JSONObject();
        try {
            String url = ApiUrl.BASE + "chat/user/profile/update-avatar";
            String boundary = "---boundary" + UUID.randomUUID().toString();

            // Build multipart body
            byte[] fileBytes = Files.readAllBytes(file.toPath());
            String mimeType = Files.probeContentType(file.toPath());
            if (mimeType == null)
                mimeType = "application/octet-stream";

            StringBuilder header = new StringBuilder();
            header.append("--").append(boundary).append("\r\n");
            header.append("Content-Disposition: form-data; name=\"avatar\"; filename=\"").append(file.getName())
                    .append("\"\r\n");
            header.append("Content-Type: ").append(mimeType).append("\r\n\r\n");

            byte[] headerBytes = header.toString().getBytes(StandardCharsets.UTF_8);
            byte[] footerBytes = ("\r\n--" + boundary + "--\r\n").getBytes(StandardCharsets.UTF_8);

            // Combine all bytes
            byte[] body = new byte[headerBytes.length + fileBytes.length + footerBytes.length];
            System.arraycopy(headerBytes, 0, body, 0, headerBytes.length);
            System.arraycopy(fileBytes, 0, body, headerBytes.length, fileBytes.length);
            System.arraycopy(footerBytes, 0, body, headerBytes.length + fileBytes.length, footerBytes.length);

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Authorization", "Bearer " + token)
                    .header("Content-Type", "multipart/form-data; boundary=" + boundary)
                    .method("PUT", HttpRequest.BodyPublishers.ofByteArray(body))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return new JSONObject(response.body());
            } else {
                result.put("error", "Failed to upload avatar (Error " + response.statusCode() + ")");
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.put("error", "Connection error: " + e.getMessage());
            return result;
        }
    }

    public JSONObject removeAvatar(String token) {
        JSONObject result = new JSONObject();
        try {
            // Endpoint: /api/chat/user/profile/remove-avatar
            String url = ApiUrl.BASE + "chat/user/profile/remove-avatar";

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Authorization", "Bearer " + token)
                    .DELETE() // Method DELETE
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                // Thành công: Trả về JSON rỗng hoặc message thành công
                // Giả sử backend trả về 200 OK là được
                result.put("success", true);
                return result;
            } else {
                // Xử lý lỗi
                String responseBody = response.body();
                StringBuilder errorMsg = new StringBuilder();
                try {
                    JSONObject errJson = new JSONObject(responseBody);
                    if (errJson.has("message")) {
                        errorMsg.append(errJson.getString("message"));
                    } else if (errJson.has("error")) {
                        errorMsg.append(errJson.getString("error"));
                    }
                } catch (Exception e) {
                    errorMsg.append("Error ").append(response.statusCode());
                }

                if (errorMsg.length() == 0)
                    errorMsg.append("Failed to remove avatar");

                result.put("error", errorMsg.toString());
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.put("error", "Connection error: " + e.getMessage());
            return result;
        }
    }

    public JSONArray getConversations(String token) {
        try {
            String url = ApiUrl.BASE + "chat/user/conversations";

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Authorization", "Bearer " + token)
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                // The API returns a direct JSON Array: [ {}, {} ]
                return new JSONArray(response.body());
            } else {
                System.err.println("Failed to fetch conversations. Status: " + response.statusCode());
                return new JSONArray(); // Return empty array on failure
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new JSONArray(); // Return empty array on error
        }
    }

    public JSONObject getPrivateConversationDetails(int conversationId) {
        String token = UserSession.getUser().getToken();

        String url = ApiUrl.BASE + "chat/user/private-conversations/" + conversationId
                + "/private-conversation-messages";

        System.out.println("DEBUG API CALL: " + url); // In ra để kiểm tra
        JSONObject res = ApiClient.getJSON(url, token);
        System.out.println(res.toString());
        return res;
    }

    public JSONObject getGroupConversationDetails(long groupId) {
        String url = utils.ApiUrl.GROUP_CONVERSATION + "/" + groupId + "/group-conversation-messages";
        try {
            return utils.ApiClient.getJSON(url, utils.UserSession.getUser().getToken());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
