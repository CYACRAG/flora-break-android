package com.florabreak.app.maps;

import com.florabreak.app.model.RouteResult;

/**
 * Demo-Service für die Suche nach einer Grünfläche.
 *
 * Aktuell nutzt diese Klasse Demo-Daten.
 * Später kann hier echte Google Maps / Places API angebunden werden.
 */
public class ParkSearchService {

    private RouteCalculator routeCalculator;

    public ParkSearchService() {
        this.routeCalculator = new RouteCalculator();
    }

    public RouteResult findNearestPark() {

        String destinationName = "Grünfläche in der Nähe";

        // Beispiel-Koordinaten, grob Köln
        double latitude = 50.9413;
        double longitude = 6.9583;

        // Demo-Gehzeit
        int walkingTimeMinutes = 12;

        boolean reachable = routeCalculator.isReachableInBreakTime(walkingTimeMinutes);

        return new RouteResult(
                destinationName,
                latitude,
                longitude,
                walkingTimeMinutes,
                reachable
        );
    }
}