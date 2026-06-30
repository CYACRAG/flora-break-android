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
                        title = "Urban Walk empfohlen";
                        text = "Es wurde keine passende Grünfläche innerhalb von 20 Minuten Gesamtweg gefunden. "
                                + "Flora Break empfiehlt deshalb einen kurzen Urban Walk in der Umgebung.";
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
