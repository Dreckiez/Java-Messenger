package services;

import models.FriendModel;
import models.LoginRecord;
import models.UserManagementItemList; 
import org.json.JSONArray;
import org.json.JSONObject;
import utils.ApiClient;
import utils.ApiUrl;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class UserAdminService {

    public List<UserManagementItemList> getUsers(String token, String keywordType, String keywordValue, 
                                                 String sortBy, String status, Integer greaterThan, Integer smallerThan) {
        
        List<UserManagementItemList> userList = new ArrayList<>();
        
        try {
            StringBuilder urlBuilder = new StringBuilder(ApiUrl.ADMIN_GET_USERS);
            urlBuilder.append("?");

            // ... (Logic tạo URL Query String giữ nguyên như cũ) ...
            if (keywordValue != null && !keywordValue.trim().isEmpty()) {
                String encodedValue = URLEncoder.encode(keywordValue, StandardCharsets.UTF_8);
                if ("Username".equals(keywordType)) urlBuilder.append("username=").append(encodedValue).append("&");
                else if ("Email".equals(keywordType)) urlBuilder.append("email=").append(encodedValue).append("&");
                else if ("Name".equals(keywordType)) urlBuilder.append("fullName=").append(encodedValue).append("&");
                else urlBuilder.append("keyword=").append(encodedValue).append("&");
            }
            
            // Map sort UI -> API
            if (sortBy != null) {
                String apiSortParam = switch (sortBy) {
                    case "Name (A-Z)" -> "fullName";
                    case "Name (Z-A)" -> "-fullName";
                    case "Date Created (Latest)" -> "-joinedAt";
                    case "Date Created (Oldest)" -> "joinedAt";
                    default -> "userId";
                };
                urlBuilder.append("sort=").append(apiSortParam).append("&");
            }

            if ("Active".equals(status)) urlBuilder.append("isActive=true&");
            else if ("Locked".equals(status)) urlBuilder.append("isActive=false&");

            if (greaterThan != null) urlBuilder.append("greaterThan=").append(greaterThan).append("&");
            if (smallerThan != null) urlBuilder.append("smallerThan=").append(smallerThan).append("&");


            System.out.println(urlBuilder);
            // Gọi API
            JSONObject response = ApiClient.getJSON(urlBuilder.toString(), token);
            int httpStatus = response.optInt("httpStatus", 500);
            
            if (httpStatus == 200) {
                if (response.has("array")) {
                    JSONArray jsonArray = response.getJSONArray("array");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject userJson = jsonArray.getJSONObject(i);
                        
                        // 👇 SỬ DỤNG CLASS MỚI TẠI ĐÂY
                        userList.add(UserManagementItemList.fromJson(userJson));
                    }
                }
            } else {
                System.err.println("API Error Status: " + httpStatus);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return userList;
    }

    public boolean updateUserStatus(String token, Long userId, boolean isActive) {
        try {
            // 1. Tạo URL endpoint: /api/chat/admin/update-user/{id}
            String url = ApiUrl.BASE + "chat/admin/update-user/" + userId;

            // 2. Tạo Body JSON: {"isActive": true/false}
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("isActive", isActive);

            // 3. Sử dụng HttpClient (Java 11+) để hỗ trợ method PATCH tốt nhất
            HttpClient client = HttpClient.newHttpClient();
            
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + token)
                    .method("PATCH", HttpRequest.BodyPublishers.ofString(jsonBody.toString()))
                    .build();

            // 4. Gửi Request
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // 5. Kiểm tra kết quả (200 OK)
            if (response.statusCode() == 200) {
                return true;
            } else {
                System.err.println("Update Failed. Status: " + response.statusCode());
                System.err.println("Response: " + response.body());
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String resetPassword(String token, Long userId) {
        try {
            // 1. Tạo URL: /api/chat/admin/reset-password/{id}
            String url = ApiUrl.BASE + "chat/admin/reset-password/" + userId;

            // 2. Tạo Body rỗng {}
            String jsonBody = "{}";

            // 3. Gọi API PATCH
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + token)
                    .method("POST", HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                // Parse JSON trả về để lấy mật khẩu mới
                JSONObject res = new JSONObject(response.body());
                return res.optString("password", null);
            } else {
                System.err.println("Reset Password Failed: " + response.statusCode());
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<LoginRecord> getLoginHistory(String token, Long userId) {
        List<LoginRecord> historyList = new ArrayList<>();
        try {
  
            String url = ApiUrl.BASE + "chat/admin/get-record-signin?userId=" + userId;

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Accept", "application/json")
                    .header("Authorization", "Bearer " + token)
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                // Parse mảng JSON
                JSONArray jsonArray = new JSONArray(response.body());
                for (int i = 0; i < jsonArray.length(); i++) {
                    historyList.add(LoginRecord.fromJson(jsonArray.getJSONObject(i)));
                }
            } else {
                System.err.println("Fetch History Failed: " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return historyList;
    }


    public List<FriendModel> getFriendsList(String token, Long userId) {
        List<FriendModel> list = new ArrayList<>();
        try {
            // URL: /api/chat/admin/get-friend/{id}
            String url = ApiUrl.BASE + "chat/admin/get-friend/" + userId;

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Accept", "application/json")
                    .header("Authorization", "Bearer " + token)
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JSONObject resObj = new JSONObject(response.body());
                
                // Lấy mảng từ key "listOfFriend"
                if (resObj.has("listOfFriend")) {
                    JSONArray jsonArray = resObj.getJSONArray("listOfFriend");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        list.add(FriendModel.fromJson(jsonArray.getJSONObject(i)));
                    }
                }
            } else {
                System.err.println("Fetch Friends Failed: " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public String createUser(String token, JSONObject userData) {
        try {
            String url = ApiUrl.BASE + "chat/admin/create-user";

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + token)
                    .POST(HttpRequest.BodyPublishers.ofString(userData.toString()))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200 || response.statusCode() == 201) {
                return null; // Thành công
            } else {
                // Parse lỗi từ backend trả về
                JSONObject errRes = new JSONObject(response.body());
                return errRes.optString("message", "Failed to create user (Error " + response.statusCode() + ")");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Connection error: " + e.getMessage();
        }
    }

    public String updateUser(String token, Long userId, JSONObject userData) {
        try {
            // URL: /api/chat/admin/update-user/{id}
            String url = ApiUrl.BASE + "chat/admin/update-user/" + userId;

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + token)
                    .method("PATCH", HttpRequest.BodyPublishers.ofString(userData.toString()))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return null; // Thành công
            } else {
                JSONObject errRes = new JSONObject(response.body());
                return errRes.optString("message", "Update failed (Error " + response.statusCode() + ")");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Connection error: " + e.getMessage();
        }
    }

    public boolean deleteUser(String token, Long userId) {
        try {
            // URL: /api/chat/admin/delete-user/{id}
            String url = ApiUrl.BASE + "chat/admin/delete-user/" + userId;

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Authorization", "Bearer " + token)
                    .DELETE() // Method DELETE
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200 || response.statusCode() == 204) {
                return true;
            } else {
                System.err.println("Delete Failed: " + response.statusCode() + " - " + response.body());
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}