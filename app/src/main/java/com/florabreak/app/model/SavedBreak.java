package com.florabreak.app.model;

/**
 * Modell für eine abgeschlossene Pause.
 *
 * Wird später vom Feedback-/Verlaufssystem genutzt.
 * Aktuell ist es bewusst einfach gehalten, damit der Prototyp stabil bleibt.
 */
public class SavedBreak {

    private final long timestamp;
    private final String title;
    private final String type;
    private final int durationMinutes;
    private final int stressScore;
    private final String stressLabel;
    private final String routeName;
    private final int rating;
    private final boolean routeProofCompleted;

    public SavedBreak(long timestamp,
                      String title,
                      String type,
                      int durationMinutes,
                      int stressScore,
                      String stressLabel,
                      String routeName,
                      int rating,
                      boolean routeProofCompleted) {
        this.timestamp = timestamp;
        this.title = title;
        this.type = type;
        this.durationMinutes = durationMinutes;
        this.stressScore = stressScore;
        this.stressLabel = stressLabel;
        this.routeName = routeName;
        this.rating = rating;
        this.routeProofCompleted = routeProofCompleted;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public int getStressScore() {
        return stressScore;
    }

    public String getStressLabel() {
        return stressLabel;
    }

    public String getRouteName() {
        return routeName;
    }

    public int getRating() {
        return rating;
    }

    public boolean isRouteProofCompleted() {
        return routeProofCompleted;
    }
}
