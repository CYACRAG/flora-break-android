package com.florabreak.app.maps;

/**
 * Prüft, ob eine Grünfläche innerhalb der Pausenzeit erreichbar ist.
 */
public class RouteCalculator {

    private static final int MAX_WALKING_TIME_MINUTES = 15;

    public boolean isReachableInBreakTime(int walkingTimeMinutes) {
        return walkingTimeMinutes <= MAX_WALKING_TIME_MINUTES;
    }
}