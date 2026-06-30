package com.florabreak.app.model;

/**
 * Gemeinsames Ergebnis einer aktuellen Flora-Break-Auswertung.
 *
 * Diese Klasse bündelt alle Daten, die später die UI braucht:
 * - Rohdaten aus Health Connect / Mock / Demo
 * - berechnetes Stress-Ergebnis
 * - Pausenempfehlung
 * - Routenergebnis
 *
 * Dadurch muss die UI später nicht selbst Health-, Stress- und Maps-Logik mischen.
 */
public class FloraBreakSessionResult {

    private final StressData stressData;
    private final StressResult stressResult;
    private final BreakRecommendation breakRecommendation;
    private final RouteResult routeResult;
    private final long timestamp;

    public FloraBreakSessionResult(
            StressData stressData,
            StressResult stressResult,
            BreakRecommendation breakRecommendation,
            RouteResult routeResult,
            long timestamp
    ) {
        this.stressData = stressData;
        this.stressResult = stressResult;
        this.breakRecommendation = breakRecommendation;
        this.routeResult = routeResult;
        this.timestamp = timestamp;
    }

    public StressData getStressData() {
        return stressData;
    }

    public StressResult getStressResult() {
        return stressResult;
    }

    public BreakRecommendation getBreakRecommendation() {
        return breakRecommendation;
    }

    public RouteResult getRouteResult() {
        return routeResult;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
