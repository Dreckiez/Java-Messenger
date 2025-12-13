package services;

import models.AnalyticsModel;
import models.FriendModel;
import models.GroupChatModel;
import models.GroupMemberModel;
import models.LoginHistoryResponse;
import models.LoginRecord;
import models.ReportModel;
import models.UserManagementItemList;
import models.UserRecordOnlineModel;

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

    /**
     * Lấy lịch sử đăng nhập (Full data + Stats)
     */
    public LoginHistoryResponse getLoginHistory(String token, Long userId, String username, String startDate, String endDate) {
        try {
            StringBuilder urlBuilder = new StringBuilder(ApiUrl.BASE + "chat/admin/get-record-signin?");
            
            // Build Query Params
            if (userId != null) urlBuilder.append("userId=").append(userId).append("&");
            if (username != null && !username.isEmpty()) urlBuilder.append("username=").append(username).append("&");
            if (startDate != null && !startDate.isEmpty()) urlBuilder.append("startDate=").append(startDate).append("&");
            if (endDate != null && !endDate.isEmpty()) urlBuilder.append("endDate=").append(endDate).append("&");

            // Xóa dấu & thừa ở cuối nếu có
            String url = urlBuilder.toString();
            if (url.endsWith("&") || url.endsWith("?")) url = url.substring(0, url.length() - 1);

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Accept", "application/json")
                    .header("Authorization", "Bearer " + token)
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return LoginHistoryResponse.fromJson(new JSONObject(response.body()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new LoginHistoryResponse();
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

    public List<GroupChatModel> getGroupChats(String token, String keyword, String sort) {
        List<GroupChatModel> list = new ArrayList<>();
        try {
            // URL: /api/chat/admin/get-group?keyword=...&sort=...
            StringBuilder urlBuilder = new StringBuilder(ApiUrl.BASE + "chat/admin/get-group?");
            
            if (keyword != null && !keyword.isEmpty()) {
                urlBuilder.append("keyword=").append(URLEncoder.encode(keyword, StandardCharsets.UTF_8)).append("&");
            }
            if (sort != null) {
                // Map lại giá trị sort từ UI sang Backend
                String backendSort = mapSortValue(sort);
                urlBuilder.append("sort=").append(backendSort);
            }

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(urlBuilder.toString()))
                    .header("Accept", "application/json")
                    .header("Authorization", "Bearer " + token)
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JSONArray jsonArray = new JSONArray(response.body());
                for (int i = 0; i < jsonArray.length(); i++) {
                    list.add(GroupChatModel.fromJson(jsonArray.getJSONObject(i)));
                }
            } else {
                System.err.println("Fetch Groups Failed: " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Helper map giá trị sort
    private String mapSortValue(String uiSort) {
        switch (uiSort) {
            case "Name (A-Z)": return "group_name";
            case "Name (Z-A)": return "-group_name";
            case "Date Created (Latest)": return "-created_at";
            case "Date Created (Oldest)": return "created_at";
            default: return "created_at";
        }
    }

    /**
     * Lấy danh sách thành viên của một Group Chat
     */
    public List<GroupMemberModel> getGroupMembers(String token, Long groupId) {
        List<GroupMemberModel> list = new ArrayList<>();
        try {
            // URL: /api/chat/admin/get-group/{id}/member
            String url = ApiUrl.BASE + "chat/admin/get-group/" + groupId + "/member";

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Accept", "application/json")
                    .header("Authorization", "Bearer " + token)
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JSONArray jsonArray = new JSONArray(response.body());
                for (int i = 0; i < jsonArray.length(); i++) {
                    list.add(GroupMemberModel.fromJson(jsonArray.getJSONObject(i)));
                }
            } else {
                System.err.println("Fetch Group Members Failed: " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Lấy danh sách Admin của một Group Chat
     */
    public List<GroupMemberModel> getGroupAdmins(String token, Long groupId) {
        List<GroupMemberModel> list = new ArrayList<>();
        try {
            // URL: /api/chat/admin/get-group/{id}/admin
            String url = ApiUrl.BASE + "chat/admin/get-group/" + groupId + "/admin";

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Accept", "application/json")
                    .header("Authorization", "Bearer " + token)
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JSONArray jsonArray = new JSONArray(response.body());
                for (int i = 0; i < jsonArray.length(); i++) {
                    list.add(GroupMemberModel.fromJson(jsonArray.getJSONObject(i)));
                }
            } else {
                System.err.println("Fetch Group Admins Failed: " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Lấy danh sách Spam Reports
     */
    /**
     * Lấy danh sách Spam Reports có sắp xếp
     */
    public List<ReportModel> getReports(String token, String searchType, String keyword, String startDate, String endDate, String sortBy) {
        List<ReportModel> list = new ArrayList<>();
        try {
            StringBuilder urlBuilder = new StringBuilder(ApiUrl.BASE + "chat/admin/get-report?");
            
            // 1. Filter Params
            if (keyword != null && !keyword.isEmpty()) {
                if ("Username".equals(searchType)) {
                    urlBuilder.append("username=").append(URLEncoder.encode(keyword, StandardCharsets.UTF_8)).append("&");
                } else if ("Email".equals(searchType)) {
                    urlBuilder.append("email=").append(URLEncoder.encode(keyword, StandardCharsets.UTF_8)).append("&");
                }
            }
            if (startDate != null && !startDate.isEmpty()) urlBuilder.append("startDate=").append(startDate).append("&");
            if (endDate != null && !endDate.isEmpty()) urlBuilder.append("endDate=").append(endDate).append("&");

            // 2. Sort Param (MỚI THÊM)
            if (sortBy != null && !sortBy.isEmpty()) {
                urlBuilder.append("sortBy=").append(sortBy).append("&");
            }

            // Clean URL
            String url = urlBuilder.toString();
            if (url.endsWith("&") || url.endsWith("?")) url = url.substring(0, url.length() - 1);

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
                if (resObj.has("reportResponseList")) {
                    JSONArray arr = resObj.getJSONArray("reportResponseList");
                    for (int i = 0; i < arr.length(); i++) {
                        list.add(ReportModel.fromJson(arr.getJSONObject(i)));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean updateReportStatus(String token, Long reporterId, Long reportedUserId, String reportedAt, int statusValue) {
        try {
            String url = ApiUrl.BASE + "chat/admin/update-report";

            // Chuẩn bị Body JSON
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("reporterId", reporterId);
            jsonBody.put("reportedUserId", reportedUserId);
            jsonBody.put("reportedAt", reportedAt);
            jsonBody.put("status", statusValue);

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Authorization", "Bearer " + token)
                    .header("Content-Type", "application/json")
                    .method("PATCH", HttpRequest.BodyPublishers.ofString(jsonBody.toString()))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                // Có thể kiểm tra message trong body nếu cần
                return true;
            } else {
                System.err.println("Update Report Status Failed: " + response.statusCode() + " - " + response.body());
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Lấy dữ liệu Analytics theo năm
     */
    public AnalyticsModel getAnalytics(String token, int year) {
        try {
            // URL: /api/chat/admin/analytics?year=2025
            String url = ApiUrl.BASE + "chat/admin/analytics?year=" + year;

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Accept", "application/json")
                    .header("Authorization", "Bearer " + token)
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return AnalyticsModel.fromJson(new JSONObject(response.body()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<UserRecordOnlineModel> getActiveUserRecords(String token, String keyword, String sort, Integer greaterThan, Integer smallerThan) {
        List<UserRecordOnlineModel> list = new ArrayList<>();
        try {
            // URL Base
            StringBuilder urlBuilder = new StringBuilder(ApiUrl.BASE + "chat/admin/get-record-online?");

            // 1. Param: keyword (fullName)
            if (keyword != null && !keyword.trim().isEmpty()) {
                urlBuilder.append("keyword=").append(URLEncoder.encode(keyword.trim(), StandardCharsets.UTF_8)).append("&");
            }

            // 2. Param: sort (fullName | -fullName | createdAt | -createdAt)
            if (sort != null && !sort.isEmpty()) {
                urlBuilder.append("sort=").append(sort).append("&");
            }

            // 3. Param: greaterThan (activityCount)
            if (greaterThan != null && greaterThan > 0) {
                urlBuilder.append("greaterThan=").append(greaterThan).append("&");
            }

            // 4. Param: smallerThan (activityCount)
            if (smallerThan != null && smallerThan > 0) {
                urlBuilder.append("smallerThan=").append(smallerThan).append("&");
            }

            // Xử lý chuỗi URL cuối cùng (bỏ dấu & thừa)
            String url = urlBuilder.toString();
            if (url.endsWith("&")) url = url.substring(0, url.length() - 1);

            // Tạo Request
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Accept", "application/json")
                    .header("Authorization", "Bearer " + token)
                    .GET()
                    .build();

            // Gửi Request
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                // Parse kết quả trả về là JSONArray
                JSONArray jsonArray = new JSONArray(response.body());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    // Sử dụng hàm fromJson static vừa tạo trong Model
                    list.add(UserRecordOnlineModel.fromJson(obj));
                }
            } else {
                System.err.println("Fetch Active Users Failed: " + response.statusCode());
                System.err.println("Body: " + response.body());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    

}