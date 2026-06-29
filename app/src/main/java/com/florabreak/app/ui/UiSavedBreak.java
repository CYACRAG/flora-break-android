package com.florabreak.app.ui;

public class UiSavedBreak {

    private final String title;
    private final String details;

    public UiSavedBreak(String title, String details) {
        this.title = title;
        this.details = details;
    }

    public String getTitle() {
        return title;
    }

    public String getDetails() {
        return details;
    }
}