package models;

import org.json.JSONObject;

public class ReportModel {
    private Long reporterId;
    private Long reportedUserId;
    private String reporterUsername;
    private String reportedUserUsername; // Username bị báo cáo
    private String reporterFullName;
    private String reportedUserFullName;
    private String reason;
    private String status; // PENDING, PROCCESSED
    private String reportedAt;

    public static ReportModel fromJson(JSONObject json) {
        ReportModel r = new ReportModel();
        r.reporterId = json.optLong("reporterId");
        r.reportedUserId = json.optLong("reportedUserId");
        r.reporterUsername = json.optString("reporterUsername");
        r.reportedUserUsername = json.optString("reportedUserUsername");
        r.reporterFullName = json.optString("reporterFullName");
        r.reportedUserFullName = json.optString("reportedUserFullName");
        r.reason = json.optString("reason");
        r.status = json.optString("status");
        r.reportedAt = json.optString("reportedAt");
        return r;
    }

    // Getters
    public String getReporterUsername() { return reporterUsername; }
    public String getReportedUserUsername() { return reportedUserUsername; }
    public String getReason() { return reason; }
    public String getStatus() { return status; }
    public Long getReportedUserId() { return reportedUserId; }
    public Long getReporterId() { return reporterId; }
    public String getReportedAtRaw() { return reportedAt; }

    public String getReportedAt() {
        if (reportedAt != null && reportedAt.contains("T")) {
            return reportedAt.replace("T", " ").substring(0, 16);
        }
        return reportedAt;
    }
}