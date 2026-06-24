package com.florabreak.app.ui;

public class UiRouteSuggestion {

    private final String routeName;
    private final String distance;
    private final String duration;
    private final String routeType;
    private final boolean urbanFallback;

    public UiRouteSuggestion(
            String routeName,
            String distance,
            String duration,
            String routeType,
            boolean urbanFallback
    ) {
        this.routeName = routeName;
        this.distance = distance;
        this.duration = duration;
        this.routeType = routeType;
        this.urbanFallback = urbanFallback;
    }

    public String getRouteName() {
        return routeName;
    }

    public String getDistance() {
        return distance;
    }

    public String getDuration() {
        return duration;
    }

    public String getRouteType() {
        return routeType;
    }

    public boolean isUrbanFallback() {
        return urbanFallback;
    }
}