package models;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class LoginHistoryResponse {
    private int countSuccess;
    private int countFailed;
    private int total;
    private List<LoginRecord> records;

    public LoginHistoryResponse() {
        this.records = new ArrayList<>();
    }

    public static LoginHistoryResponse fromJson(JSONObject json) {
        LoginHistoryResponse res = new LoginHistoryResponse();
        res.countSuccess = json.optInt("countSuccess");
        res.countFailed = json.optInt("countFailed");
        res.total = json.optInt("total");

        if (json.has("recordSignInResponseList")) {
            JSONArray arr = json.getJSONArray("recordSignInResponseList");
            for (int i = 0; i < arr.length(); i++) {
                res.records.add(LoginRecord.fromJson(arr.getJSONObject(i)));
            }
        }
        return res;
    }

    public int getCountSuccess() { return countSuccess; }
    public int getCountFailed() { return countFailed; }
    public int getTotal() { return total; }
    public List<LoginRecord> getRecords() { return records; }
}