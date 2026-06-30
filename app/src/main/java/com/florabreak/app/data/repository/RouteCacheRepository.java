package com.florabreak.app.data.repository;

import android.content.Context;
import android.content.SharedPreferences;

import com.florabreak.app.model.CachedRoute;

import java.util.ArrayList;
import java.util.List;

/**
 * Speichert vorberechnete Routenvorschläge lokal.
 *
 * Für den Prototyp speichern wir maximal 5 Routen in SharedPreferences.
 * Später kann diese Klasse durch Room ersetzt werden.
 */
public class RouteCacheRepository {

    private static final String PREF_NAME = "flora_route_cache";

    private static final String KEY_LOCATION = "location_key";
    private static final String KEY_COUNT = "route_count";

    private static final int MAX_ROUTES = 5;

    private final SharedPreferences preferences;

    public RouteCacheRepository(Context context) {
        this.preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void saveRoutes(String locationKey, List<CachedRoute> routes) {
        SharedPreferences.Editor editor = preferences.edit();

        editor.clear();
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
        preferences.edit().clear().apply();
    }
}
