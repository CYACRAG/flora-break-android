package com.florabreak.app.maps;

import android.content.Context;

import androidx.annotation.NonNull;

import com.florabreak.app.data.repository.ProfileRepository;
import com.florabreak.app.data.repository.RouteCacheRepository;
import com.florabreak.app.model.CachedRoute;
import com.florabreak.app.model.RouteResult;
import com.florabreak.app.model.UserProfile;

import java.util.ArrayList;
import java.util.List;

public class RealMapsBreakService {

    public interface BreakDecisionCallback {
        void onBreakDecisionReady(
                String recommendationTitle,
                String recommendationText,
                RouteResult routeResult,
                boolean usedRealLocation,
                boolean foundRealPlace,
                boolean usedRealRoute
        );
    }

    private final RealMapsRouteProvider realMapsRouteProvider;
    private final RouteCacheRepository routeCacheRepository;
    private final DeviceLocationService deviceLocationService;
    private final ProfileRepository profileRepository;

    public RealMapsBreakService(@NonNull Context context) {
        this.realMapsRouteProvider = new RealMapsRouteProvider(context);
        this.routeCacheRepository = new RouteCacheRepository(context);
        this.deviceLocationService = new DeviceLocationService(context);
        this.profileRepository = new ProfileRepository(context);
    }

    public void getBreakDecision(@NonNull BreakDecisionCallback callback) {
        UserProfile profile = profileRepository.getProfile();

        if (profile.isWorkLocationSaved()) {
            handleLocationForRoute(
                    profile.getWorkLatitude(),
                    profile.getWorkLongitude(),
                    true,
                    true,
                    callback
            );
            return;
        }

        deviceLocationService.getCurrentLocation((latitude, longitude, isRealLocation) ->
                handleLocationForRoute(
                        latitude,
                        longitude,
                        isRealLocation,
                        false,
                        callback
                )
        );
    }

    private void handleLocationForRoute(
            double latitude,
            double longitude,
            boolean isRealLocation,
            boolean usedWorkLocation,
            @NonNull BreakDecisionCallback callback
    ) {
        String locationKey;

        if (usedWorkLocation) {
            locationKey = "work_" + createRoundedLocationKey(latitude, longitude);
        } else {
            locationKey = deviceLocationService.createLocationKey(
                    latitude,
                    longitude,
                    isRealLocation
            );
        }

        List<CachedRoute> cachedRoutes =
                routeCacheRepository.getRoutesForLocation(locationKey);

        if (!cachedRoutes.isEmpty()) {
            CachedRoute cachedRoute = cachedRoutes.get(0);

            RouteResult routeResult = new RouteResult(
                    cachedRoute.getDestinationName(),
                    cachedRoute.getDestinationLatitude(),
                    cachedRoute.getDestinationLongitude(),
                    cachedRoute.getOneWayWalkingTimeMinutes(),
                    cachedRoute.isReachableWithinLimit()
            );

            callback.onBreakDecisionReady(
                    cachedRoute.getTitle(),
                    buildUserTextFromCachedRoute(cachedRoute, usedWorkLocation),
                    routeResult,
                    isRealLocation,
                    cachedRoute.isParkRoute(),
                    false
            );

            return;
        }

        realMapsRouteProvider.getRecommendedWalkingRoute(
                (routeResult, usedRealLocation, foundRealPlace, usedRealRoute) -> {
                    CachedRoute cachedRoute = createCachedRouteFromResult(
                            routeResult,
                            foundRealPlace,
                            locationKey
                    );

                    List<CachedRoute> routesToSave = new ArrayList<>();
                    routesToSave.add(cachedRoute);
                    routeCacheRepository.saveRoutes(locationKey, routesToSave);

                    callback.onBreakDecisionReady(
                            cachedRoute.getTitle(),
                            buildUserTextFromCachedRoute(cachedRoute, usedWorkLocation),
                            routeResult,
                            usedRealLocation,
                            foundRealPlace,
                            usedRealRoute
                    );
                }
        );
    }

    private CachedRoute createCachedRouteFromResult(
            RouteResult routeResult,
            boolean foundRealPlace,
            String locationKey
    ) {
        int oneWayMinutes = routeResult.getWalkingTimeMinutes();
        int totalMinutes = oneWayMinutes * 2;
        boolean reachable = routeResult.isReachable();

        boolean parkRoute = foundRealPlace && reachable;

        String type;
        String title;

        if (parkRoute) {
            type = "PARK_ROUTE";
            title = "Parkroute empfohlen";
        } else {
            type = "URBAN_WALK";
            title = "Urban Walk empfohlen";
        }

        return new CachedRoute(
                "route_" + System.currentTimeMillis(),
                title,
                type,
                routeResult.getDestinationName(),
                routeResult.getLatitude(),
                routeResult.getLongitude(),
                oneWayMinutes,
                totalMinutes,
                parkRoute,
                reachable,
                System.currentTimeMillis(),
                locationKey
        );
    }

    private String buildUserTextFromCachedRoute(CachedRoute route, boolean usedWorkLocation) {
        String locationText = usedWorkLocation
                ? "ausgehend von deinem gespeicherten Arbeitsort"
                : "ausgehend von deinem aktuellen Standort";

        if (route.isParkRoute()) {
            return "Eine passende Grünfläche wurde "
                    + locationText
                    + " gefunden. Gesamtweg: ca. "
                    + route.getTotalWalkingTimeMinutes()
                    + " Minuten bis "
                    + route.getDestinationName()
                    + " und zurück.";
        }

        return "Es wurde keine passende Grünfläche innerhalb von 20 Minuten Gesamtweg gefunden. "
                + "Flora Break empfiehlt deshalb einen kurzen Urban Walk "
                + locationText
                + ".";
    }

    private String createRoundedLocationKey(double latitude, double longitude) {
        double roundedLatitude = Math.round(latitude * 100.0) / 100.0;
        double roundedLongitude = Math.round(longitude * 100.0) / 100.0;

        return roundedLatitude + "_" + roundedLongitude;
    }
}
