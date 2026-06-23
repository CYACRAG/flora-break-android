package com.florabreak.app.model;

public class RouteResult {

    private String destinationName;
    private double latitude;
    private double longitude;
    private int walkingTimeMinutes;
    private boolean reachable;

    public RouteResult(String destinationName, double latitude, double longitude,
                       int walkingTimeMinutes, boolean reachable) {
        this.destinationName = destinationName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.walkingTimeMinutes = walkingTimeMinutes;
        this.reachable = reachable;
    }

    public String getDestinationName() {
        return destinationName;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public int getWalkingTimeMinutes() {
        return walkingTimeMinutes;
    }

    public boolean isReachable() {
        return reachable;
    }
}
