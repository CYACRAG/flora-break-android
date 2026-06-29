package com.florabreak.app.maps;

import android.content.Context;

import androidx.annotation.NonNull;

import com.florabreak.app.model.RouteResult;

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

    public RealMapsBreakService(@NonNull Context context) {
        this.realMapsRouteProvider = new RealMapsRouteProvider(context);
    }

    public void getBreakDecision(@NonNull BreakDecisionCallback callback) {
        realMapsRouteProvider.getRecommendedWalkingRoute(
                (routeResult, usedRealLocation, foundRealPlace, usedRealRoute) -> {
                    String title;
                    String text;

                    if (routeResult.isReachable()) {
                        title = "Urban Walk empfohlen";
                        text = "Eine erreichbare Grünfläche wurde gefunden. Gehzeit: "
                                + routeResult.getWalkingTimeMinutes()
                                + " Minuten bis "
                                + routeResult.getDestinationName()
                                + ".";
                    } else {
                        title = "Indoor-Pause empfohlen";
                        text = "In 10–15 Minuten wurde keine passende Grünfläche erreicht. "
                                + "Für diese Situation wird eine kurze Indoor-Pause empfohlen.";
                    }

                    callback.onBreakDecisionReady(
                            title,
                            text,
                            routeResult,
                            usedRealLocation,
                            foundRealPlace,
                            usedRealRoute
                    );
                }
        );
    }
}