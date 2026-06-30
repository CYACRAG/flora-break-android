package com.florabreak.app.model;

/**
 * Lokal gespeicherte Route für Flora Break.
 *
 * Zweck:
 * - API-Aufrufe sparen
 * - bekannte Arbeits-/GPS-Umgebung wiederverwenden
 * - mehrere Routenvorschläge lokal abrufbar machen
 *
 * Eine Route kann eine Parkroute oder ein Urban Walk sein.
 */
public class CachedRoute {

    private final String id;
    private final String title;
    private final String type;
    private final String destinationName;

    private final double destinationLatitude;
    private final double destinationLongitude;

    private final int oneWayWalkingTimeMinutes;
    private final int totalWalkingTimeMinutes;

    private final boolean parkRoute;
    private final boolean reachableWithinLimit;

    private final long createdAt;
    private final String locationKey;

    public CachedRoute(
            String id,
            String title,
            String type,
            String destinationName,
            double destinationLatitude,
            double destinationLongitude,
            int oneWayWalkingTimeMinutes,
            int totalWalkingTimeMinutes,
            boolean parkRoute,
            boolean reachableWithinLimit,
            long createdAt,
            String locationKey
    ) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.destinationName = destinationName;
        this.destinationLatitude = destinationLatitude;
        this.destinationLongitude = destinationLongitude;
        this.oneWayWalkingTimeMinutes = oneWayWalkingTimeMinutes;
        this.totalWalkingTimeMinutes = totalWalkingTimeMinutes;
        this.parkRoute = parkRoute;
        this.reachableWithinLimit = reachableWithinLimit;
        this.createdAt = createdAt;
        this.locationKey = locationKey;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public String getDestinationName() {
        return destinationName;
    }

    public double getDestinationLatitude() {
        return destinationLatitude;
    }

    public double getDestinationLongitude() {
        return destinationLongitude;
    }

    public int getOneWayWalkingTimeMinutes() {
        return oneWayWalkingTimeMinutes;
    }

    public int getTotalWalkingTimeMinutes() {
        return totalWalkingTimeMinutes;
    }

    public boolean isParkRoute() {
        return parkRoute;
    }

    public boolean isReachableWithinLimit() {
        return reachableWithinLimit;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public String getLocationKey() {
        return locationKey;
    }
}
