package com.florabreak.app.ui;

/**
 * UI-Modell für eine gespeicherte Pause.
 *
 * Wichtig:
 * Diese Klasse ist aktuell nur ein UI-Placeholder.
 * Später können diese Daten aus einer echten Datenbank kommen.
 */
public class UiSavedBreak {

    private final String title;
    private final String details;
    private final int rating;

    public UiSavedBreak(String title, String details, int rating) {
        this.title = title;
        this.details = details;
        this.rating = rating;
    }

    public String getTitle() {
        return title;
    }

    public String getDetails() {
        return details;
    }

    public int getRating() {
        return rating;
    }
}