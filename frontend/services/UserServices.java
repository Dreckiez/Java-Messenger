package services;

import org.json.JSONObject;

import utils.ApiClient;
import utils.ApiUrl;

public class UserServices {
    public static JSONObject getMyProfile(String token) {
        return ApiClient.getJSON(ApiUrl.MYPROFILE, token);
    }
}
