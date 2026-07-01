package com.florabreak.app.data.repository;

import android.content.Context;
import android.content.SharedPreferences;

import com.florabreak.app.model.CachedRoute;

import java.util.ArrayList;
import java.util.List;

public class RouteCacheRepository {

    private static final String PREF_NAME = "flora_route_cache";

    private static final String KEY_LOCATION = "location_key";
    private static final String KEY_COUNT = "route_count";

    private static final String KEY_REJECTED_COUNT = "rejected_route_count";

    private static final int MAX_ROUTES = 5;
    private static final int MAX_REJECTED_ROUTES = 10;
    private static final double SAME_ROUTE_DISTANCE_METERS = 120.0;

    private final SharedPreferences preferences;

    public RouteCacheRepository(Context context) {
        this.preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void saveRoutes(String locationKey, List<CachedRoute> routes) {
        SharedPreferences.Editor editor = preferences.edit();

        clearOnlyCachedRoutes(editor);

        editor.putString(KEY_LOCATION, locationKey);

        int count = Math.min(routes.size(), MAX_ROUTES);
        editor.putInt(KEY_COUNT, count);

        for (int i = 0; i < count; i++) {
            CachedRoute route = routes.get(i);
            String prefix = "route_" + i + "_";

            editor.putString(prefix + "id", route.getId());
            editor.putString(prefix + "title", route.getTitle());
            editor.putString(prefix + "type", route.getType());
            editor.putString(prefix + "destination_name", route.getDestinationName());
            editor.putFloat(prefix + "destination_lat", (float) route.getDestinationLatitude());
            editor.putFloat(prefix + "destination_lng", (float) route.getDestinationLongitude());
            editor.putInt(prefix + "one_way_minutes", route.getOneWayWalkingTimeMinutes());
            editor.putInt(prefix + "total_minutes", route.getTotalWalkingTimeMinutes());
            editor.putBoolean(prefix + "park_route", route.isParkRoute());
            editor.putBoolean(prefix + "reachable", route.isReachableWithinLimit());
            editor.putLong(prefix + "created_at", route.getCreatedAt());
            editor.putString(prefix + "location_key", route.getLocationKey());
        }

        editor.apply();
    }

    public List<CachedRoute> getRoutesForLocation(String locationKey) {
        List<CachedRoute> routes = new ArrayList<>();

        String cachedLocationKey = preferences.getString(KEY_LOCATION, "");

        if (!locationKey.equals(cachedLocationKey)) {
            return routes;
        }

        int count = preferences.getInt(KEY_COUNT, 0);

        for (int i = 0; i < count; i++) {
            String prefix = "route_" + i + "_";

            CachedRoute route = new CachedRoute(
                    preferences.getString(prefix + "id", "route_" + i),
                    preferences.getString(prefix + "title", "Route"),
                    preferences.getString(prefix + "type", "URBAN_WALK"),
                    preferences.getString(prefix + "destination_name", "Ziel"),
                    preferences.getFloat(prefix + "destination_lat", 0f),
                    preferences.getFloat(prefix + "destination_lng", 0f),
                    preferences.getInt(prefix + "one_way_minutes", 0),
                    preferences.getInt(prefix + "total_minutes", 0),
                    preferences.getBoolean(prefix + "park_route", false),
                    preferences.getBoolean(prefix + "reachable", false),
                    preferences.getLong(prefix + "created_at", 0L),
                    preferences.getString(prefix + "location_key", locationKey)
            );

            routes.add(route);
        }

        return routes;
    }

    public boolean hasRoutesForLocation(String locationKey) {
        return !getRoutesForLocation(locationKey).isEmpty();
    }

    public void clearRoutes() {
        SharedPreferences.Editor editor = preferences.edit();
        clearOnlyCachedRoutes(editor);
        editor.apply();
    }

    public void handleRouteFeedback(
            String routeName,
            double latitude,
            double longitude,
            int rating
    ) {
        if (rating <= 0) {
            return;
        }

        if (rating <= 2) {
            rejectRoute(routeName, latitude, longitude);
            clearRoutes();
        }
    }

    public void rejectRoute(
            String routeName,
            double latitude,
            double longitude
    ) {
        if (latitude == 0.0 && longitude == 0.0) {
            return;
        }

        int currentCount = preferences.getInt(KEY_REJECTED_COUNT, 0);
        int insertIndex = currentCount % MAX_REJECTED_ROUTES;

        String prefix = "rejected_" + insertIndex + "_";

        preferences.edit()
                .putString(prefix + "name", routeName == null ? "" : routeName)
                .putFloat(prefix + "lat", (float) latitude)
                .putFloat(prefix + "lng", (float) longitude)
                .putLong(prefix + "created_at", System.currentTimeMillis())
                .putInt(KEY_REJECTED_COUNT, currentCount + 1)
                .apply();
    }

    public boolean isRejectedRoute(
            String routeName,
            double latitude,
            double longitude
    ) {
        if (latitude == 0.0 && longitude == 0.0) {
            return false;
        }

        int rawCount = preferences.getInt(KEY_REJECTED_COUNT, 0);
        int count = Math.min(rawCount, MAX_REJECTED_ROUTES);

        for (int i = 0; i < count; i++) {
            String prefix = "rejected_" + i + "_";

            double rejectedLatitude = preferences.getFloat(prefix + "lat", 0f);
            double rejectedLongitude = preferences.getFloat(prefix + "lng", 0f);
            String rejectedName = preferences.getString(prefix + "name", "");

            double distance = calculateDistanceMeters(
                    latitude,
                    longitude,
                    rejectedLatitude,
                    rejectedLongitude
            );

            boolean sameName = routeName != null
                    && !routeName.trim().isEmpty()
                    && routeName.equalsIgnoreCase(rejectedName);

            if (sameName || distance <= SAME_ROUTE_DISTANCE_METERS) {
                return true;
            }
        }

        return false;
    }

    private void clearOnlyCachedRoutes(SharedPreferences.Editor editor) {
        int count = preferences.getInt(KEY_COUNT, 0);

        for (int i = 0; i < count; i++) {
            String prefix = "route_" + i + "_";

            editor.remove(prefix + "id");
            editor.remove(prefix + "title");
            editor.remove(prefix + "type");
            editor.remove(prefix + "destination_name");
            editor.remove(prefix + "destination_lat");
            editor.remove(prefix + "destination_lng");
            editor.remove(prefix + "one_way_minutes");
            editor.remove(prefix + "total_minutes");
            editor.remove(prefix + "park_route");
            editor.remove(prefix + "reachable");
            editor.remove(prefix + "created_at");
            editor.remove(prefix + "location_key");
        }

        editor.remove(KEY_LOCATION);
        editor.remove(KEY_COUNT);
    }

    private double calculateDistanceMeters(
            double startLatitude,
            double startLongitude,
            double endLatitude,
            double endLongitude
    ) {
        double earthRadiusMeters = 6371000.0;

        double startLatRad = Math.toRadians(startLatitude);
        double endLatRad = Math.toRadians(endLatitude);
        double deltaLat = Math.toRadians(endLatitude - startLatitude);
        double deltaLon = Math.toRadians(endLongitude - startLongitude);

        double a = Math.sin(deltaLat / 2.0) * Math.sin(deltaLat / 2.0)
                + Math.cos(startLatRad)
                * Math.cos(endLatRad)
                * Math.sin(deltaLon / 2.0)
                * Math.sin(deltaLon / 2.0);

        double c = 2.0 * Math.atan2(Math.sqrt(a), Math.sqrt(1.0 - a));

        return earthRadiusMeters * c;
    }
}
