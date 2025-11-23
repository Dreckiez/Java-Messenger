package utils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeHandler {
    public String formatTimeAgo(String timestamp) {
        if (timestamp == null || timestamp.isEmpty()) {
            return "Just now";
        }

        try {
            LocalDateTime sentTime = LocalDateTime.parse(timestamp, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            LocalDateTime now = LocalDateTime.now();
            Duration duration = Duration.between(sentTime, now);

            long seconds = duration.getSeconds();
            long minutes = seconds / 60;
            long hours = minutes / 60;
            long days = hours / 24;
            long weeks = days / 7;
            long months = days / 30;
            long years = days / 365;

            if (seconds < 60) {
                return "Just now";
            } else if (minutes < 60) {
                return minutes + (minutes == 1 ? " minute ago" : " minutes ago");
            } else if (hours < 24) {
                return hours + (hours == 1 ? " hour ago" : " hours ago");
            } else if (days < 7) {
                return days + (days == 1 ? " day ago" : " days ago");
            } else if (weeks < 4) {
                return weeks + (weeks == 1 ? " week ago" : " weeks ago");
            } else if (months < 12) {
                return months + (months == 1 ? " month ago" : " months ago");
            } else {
                return years + (years == 1 ? " year ago" : " years ago");
            }
        } catch (Exception e) {
            return "Recently";
        }
    }
}
