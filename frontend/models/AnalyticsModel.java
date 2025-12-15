package models;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class AnalyticsModel {
    private RegistrationData registration;
    private ActiveUsersData activeUsers;

    public static AnalyticsModel fromJson(JSONObject json) {
        AnalyticsModel model = new AnalyticsModel();
        if (json.has("registration")) 
            model.registration = RegistrationData.fromJson(json.getJSONObject("registration"));
        if (json.has("activeUsers")) 
            model.activeUsers = ActiveUsersData.fromJson(json.getJSONObject("activeUsers"));
        return model;
    }

    public RegistrationData getRegistration() { return registration; }
    public ActiveUsersData getActiveUsers() { return activeUsers; }

    // --- Sub Classes ---

    public static class RegistrationData {
        public int[] dataByMonth = new int[12];
        public RegStats stats;

        public static RegistrationData fromJson(JSONObject json) {
            RegistrationData r = new RegistrationData();
            JSONArray arr = json.optJSONArray("dataByMonth");
            if (arr != null) {
                for (int i = 0; i < 12 && i < arr.length(); i++) r.dataByMonth[i] = arr.optInt(i);
            }
            r.stats = RegStats.fromJson(json.optJSONObject("stats"));
            return r;
        }
    }

    public static class RegStats {
        public int totalRegistration;
        public int avgMonthly;
        public String highestMonth;
        public String growthPercentage;

        public static RegStats fromJson(JSONObject json) {
            RegStats s = new RegStats();
            if (json == null) return s;
            s.totalRegistration = json.optInt("totalRegistration");
            s.avgMonthly = json.optInt("avgMonthly");
            s.highestMonth = json.optString("highestMonth");
            s.growthPercentage = json.optString("growthPercentage");
            return s;
        }
    }

    public static class ActiveUsersData {
        public int[] dataByMonth = new int[12];
        public ActiveStats stats;

        public static ActiveUsersData fromJson(JSONObject json) {
            ActiveUsersData a = new ActiveUsersData();
            JSONArray arr = json.optJSONArray("dataByMonth");
            if (arr != null) {
                for (int i = 0; i < 12 && i < arr.length(); i++) a.dataByMonth[i] = arr.optInt(i);
            }
            a.stats = ActiveStats.fromJson(json.optJSONObject("stats"));
            return a;
        }
    }

    public static class ActiveStats {
        public int avgActivitiesMonthly;
        public String highestMonth;
        public String activityPercentage;
        public String trend;

        public static ActiveStats fromJson(JSONObject json) {
            ActiveStats s = new ActiveStats();
            if (json == null) return s;
            s.avgActivitiesMonthly = json.optInt("avgActivitiesMonthly");
            s.highestMonth = json.optString("highestMonth");
            s.activityPercentage = json.optString("activityPercentage");
            s.trend = json.optString("trend");
            return s;
        }
    }
}
