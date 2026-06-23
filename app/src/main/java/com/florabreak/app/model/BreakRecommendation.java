package com.florabreak.app.model;

public class BreakRecommendation {

    private String type;
    private String title;
    private String description;
    private int durationMinutes;

    public BreakRecommendation(String type, String title, String description, int durationMinutes) {
        this.type = type;
        this.title = title;
        this.description = description;
        this.durationMinutes = durationMinutes;
    }

    public String getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }
}
