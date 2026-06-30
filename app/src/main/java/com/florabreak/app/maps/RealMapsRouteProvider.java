package com.florabreak.app.maps;

import android.content.Context;

import androidx.annotation.NonNull;

import com.florabreak.app.model.RouteResult;

public class RealMapsRouteProvider {

    public interface RealRouteCallback {
        void onRouteResultReady(RouteResult routeResult, boolean usedRealLocation, boolean foundRealPlace, boolean usedRealRoute);
    }

    private final DeviceLocationService deviceLocationService;
    private final NearbyGreenSpaceService nearbyGreenSpaceService;
    private final GoogleRoutesService googleRoutesService;

    public RealMapsRouteProvider(@NonNull Context context) {
        this.deviceLocationService = new DeviceLocationService(context);
        this.nearbyGreenSpaceService = new NearbyGreenSpaceService(context);
        this.googleRoutesService = new GoogleRoutesService(context);
    }

    public void getRecommendedWalkingRoute(@NonNull RealRouteCallback callback) {
        deviceLocationService.getCurrentLocation((userLatitude, userLongitude, usedRealLocation) ->
                nearbyGreenSpaceService.findNearbyGreenSpace(
                        userLatitude,
                        userLongitude,
                        (placeName, placeLatitude, placeLongitude, foundRealPlace) ->
                                googleRoutesService.calculateWalkingRoute(
                                        userLatitude,
                                        userLongitude,
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
                )
        );
    }
}