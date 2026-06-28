package com.florabreak.app.maps;

import com.florabreak.app.model.RouteResult;

/**
 * Entscheidet, ob ein Urban Walk oder eine Indoor-Pause empfohlen wird.
 */
public class FallbackBreakService {

    public String getRecommendation(RouteResult routeResult) {

        if (routeResult.isReachable()) {
            return "Urban Walk empfohlen";
        } else {
            return "Indoor-Pause empfohlen";
        }
    }

    /**
     * Erstellt einen kurzen Text, den die UI später anzeigen kann.
     */
    public String getRouteSummary(RouteResult routeResult) {
        return routeResult.getDestinationName()
                + "\n" + routeResult.getWalkingTimeMinutes() + " Min zu Fuß"
                + "\n" + getRecommendation(routeResult);
    }
}