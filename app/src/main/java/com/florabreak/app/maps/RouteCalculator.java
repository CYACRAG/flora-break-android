package com.florabreak.app.maps;

/**
 * Bewertet, ob eine Route für Flora Break geeignet ist.
 *
 * Regel:
 * Hinweg + Rückweg zusammen maximal 20 Minuten.
 * Dadurch darf der einfache Weg maximal 10 Minuten dauern.
 */
public class RouteCalculator {

    private static final int MAX_TOTAL_WALKING_TIME_MINUTES = 20;

    public boolean isReachable(int oneWayWalkingTimeMinutes) {
        int totalWalkingTimeMinutes = oneWayWalkingTimeMinutes * 2;
        return totalWalkingTimeMinutes <= MAX_TOTAL_WALKING_TIME_MINUTES;
    }

    /**
     * Kompatibilitätsmethode für bestehenden Code.
     *
     * Bedeutet fachlich dasselbe wie isReachable:
     * einfache Gehzeit * 2 muss maximal 20 Minuten ergeben.
     */
    public boolean isReachableInBreakTime(int oneWayWalkingTimeMinutes) {
        return isReachable(oneWayWalkingTimeMinutes);
    }

    public int calculateTotalWalkingTimeMinutes(int oneWayWalkingTimeMinutes) {
        return oneWayWalkingTimeMinutes * 2;
    }
}
