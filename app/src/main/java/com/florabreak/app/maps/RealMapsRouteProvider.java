package com.florabreak.app.maps;

import android.content.Context;

import androidx.annotation.NonNull;

import com.florabreak.app.model.RouteResult;

public class RealMapsRouteProvider {

    public interface RealRouteCallback {
        void onRouteResultReady(
                RouteResult routeResult,
                boolean usedRealLocation,
                boolean foundRealPlace,
                boolean usedRealRoute
        );
    }

    private final DeviceLocationService deviceLocationService;
    private final NearbyGreenSpaceService nearbyGreenSpaceService;
    private final GoogleRoutesService googleRoutesService;

    public RealMapsRouteProvider(@NonNull Context context) {
        this.deviceLocationService = new DeviceLocationService(context);
        this.nearbyGreenSpaceService = new NearbyGreenSpaceService(context);
        this.googleRoutesService = new GoogleRoutesService(context);
    }

    /**
     * Standardfall:
     * Route wird vom aktuellen Gerätestandort berechnet.
     */
    public void getRecommendedWalkingRoute(@NonNull RealRouteCallback callback) {
        deviceLocationService.getCurrentLocation((userLatitude, userLongitude, usedRealLocation) ->
                getRecommendedWalkingRouteFromLocation(
                        userLatitude,
                        userLongitude,
                        usedRealLocation,
                        callback
                )
        );
    }

    /**
     * Neuer Fall:
     * Route wird von einem übergebenen Startpunkt berechnet.
     *
     * Das wird z.B. genutzt, wenn im Profil ein Arbeitsort gespeichert ist.
     */
    public void getRecommendedWalkingRouteFromLocation(
            double startLatitude,
            double startLongitude,
            boolean usedRealLocation,
            @NonNull RealRouteCallback callback
    ) {
        nearbyGreenSpaceService.findNearbyGreenSpace(
                startLatitude,
                startLongitude,
                (placeName, placeLatitude, placeLongitude, foundRealPlace) ->
                        googleRoutesService.calculateWalkingRoute(
                                startLatitude,
                                startLongitude,
                                placeName,
                                placeLatitude,
                                placeLongitude,
                                (routeResult, usedRealRoute) ->
                                        callback.onRouteResultReady(
                                                routeResult,
                                                usedRealLocation,
                                                foundRealPlace,
                                                usedRealRoute
                                        )
                        )
        );
    }
}
