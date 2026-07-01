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

    private static final double MAX_DISTANCE_TO_WORK_METERS = 700.0;
    private static final double MAX_REASONABLE_DESTINATION_DISTANCE_METERS = 3000.0;

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

        deviceLocationService.getCurrentLocation((currentLatitude, currentLongitude, isRealLocation) -> {
			if (!isRealLocation || (currentLatitude == 0.0 && currentLongitude == 0.0)) {
			    RouteResult routeResult = new RouteResult(
			            "Standort nicht verfügbar",
			            0.0,
			            0.0,
			            0,
			            false
			    );

			    callback.onBreakDecisionReady(
			            "Standort benötigt",
			            "Flora Break braucht deinen aktuellen Standort, um eine Grünfläche oder einen Urban Walk in deiner Nähe vorzuschlagen.",
			            routeResult,
			            false,
			            false,
			            false
			    );
			    return;
			}
            if (profile.isWorkLocationSaved() && isRealLocation) {
                double distanceToWork = calculateDistanceMeters(
                        currentLatitude,
                        currentLongitude,
                        profile.getWorkLatitude(),
                        profile.getWorkLongitude()
                );

                if (distanceToWork <= MAX_DISTANCE_TO_WORK_METERS) {
                    handleLocationForRoute(
                            profile.getWorkLatitude(),
                            profile.getWorkLongitude(),
                            true,
                            true,
                            callback
                    );
                    return;
                }
            }

            handleLocationForRoute(
                    currentLatitude,
                    currentLongitude,
                    isRealLocation,
                    false,
                    callback
            );
        });
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

        CachedRoute validCachedRoute = findValidCachedRoute(
                cachedRoutes,
                latitude,
                longitude
        );

        if (validCachedRoute != null) {
            RouteResult routeResult = new RouteResult(
                    validCachedRoute.getDestinationName(),
                    validCachedRoute.getDestinationLatitude(),
                    validCachedRoute.getDestinationLongitude(),
                    validCachedRoute.getOneWayWalkingTimeMinutes(),
                    validCachedRoute.isReachableWithinLimit()
            );

            callback.onBreakDecisionReady(
                    validCachedRoute.getTitle(),
                    buildUserTextFromCachedRoute(validCachedRoute, usedWorkLocation),
                    routeResult,
                    isRealLocation,
                    validCachedRoute.isParkRoute(),
                    false
            );

            return;
        }

        realMapsRouteProvider.getRecommendedWalkingRouteFromLocation(
                latitude,
                longitude,
                isRealLocation,
                (routeResult, usedRealLocation, foundRealPlace, usedRealRoute) -> {
					if (routeCacheRepository.isRejectedRoute(
					        routeResult.getDestinationName(),
					        routeResult.getLatitude(),
					        routeResult.getLongitude()
					)) {
					    routeResult = createAlternativeUrbanWalkRoute(latitude, longitude);
					    foundRealPlace = false;
					    usedRealRoute = false;
					}
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

    private CachedRoute findValidCachedRoute(
            List<CachedRoute> cachedRoutes,
            double startLatitude,
            double startLongitude
    ) {
        if (cachedRoutes == null || cachedRoutes.isEmpty()) {
            return null;
        }

        for (CachedRoute route : cachedRoutes) {
            double distanceToDestination = calculateDistanceMeters(
                    startLatitude,
                    startLongitude,
                    route.getDestinationLatitude(),
                    route.getDestinationLongitude()
            );

            if (distanceToDestination <= MAX_REASONABLE_DESTINATION_DISTANCE_METERS) {
                return route;
            }
        }

        return null;
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

        return "Es wurde keine benannte Grünfläche innerhalb von 20 Minuten Gesamtweg gefunden. "
                + "Flora Break empfiehlt deshalb einen kurzen Urban Walk "
                + locationText
                + ".";
    }

    private String createRoundedLocationKey(double latitude, double longitude) {
        double roundedLatitude = Math.round(latitude * 100.0) / 100.0;
        double roundedLongitude = Math.round(longitude * 100.0) / 100.0;

        return roundedLatitude + "_" + roundedLongitude;
    }
	private RouteResult createAlternativeUrbanWalkRoute(
	        double startLatitude,
	        double startLongitude
	) {
	    return new RouteResult(
	            "Alternative ruhige Route",
	            startLatitude + 0.0040,
	            startLongitude - 0.0030,
	            7,
	            true
	    );
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

