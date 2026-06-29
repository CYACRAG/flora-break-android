package com.florabreak.app.data;

import android.content.Context;

import androidx.annotation.NonNull;

import com.florabreak.app.maps.RealMapsBreakService;
import com.florabreak.app.model.BreakRecommendation;
import com.florabreak.app.model.RouteResult;
import com.florabreak.app.model.StressData;
import com.florabreak.app.model.StressResult;
import com.florabreak.app.stress.StressDecisionEngine;

public class FloraBreakController {

    public interface BreakRecommendationCallback {
        void onRecommendationReady(com.florabreak.app.model.BreakRecommendation recommendation);
    }
    private final HealthDataProvider healthDataProvider;
    private final RouteProvider routeProvider;
    private final StressDecisionEngine stressDecisionEngine;

    public FloraBreakController(HealthDataProvider healthDataProvider, RouteProvider routeProvider) {
        this.healthDataProvider = healthDataProvider;
        this.routeProvider = routeProvider;
        this.stressDecisionEngine = new StressDecisionEngine();
    }

    public StressResult getCurrentStressResult() {
        StressData stressData = healthDataProvider.getCurrentStressData();
        return stressDecisionEngine.analyzeStress(stressData);
    }

    public BreakRecommendation getCurrentBreakRecommendation() {
        StressResult stressResult = getCurrentStressResult();

        if (!stressResult.isBreakRecommended()) {
            return new BreakRecommendation(
                    "NONE",
                    "Keine Pause notwendig",
                    stressResult.getExplanation(),
                    0
            );
        }

        RouteResult routeResult = routeProvider.getNearestBreakRoute();

        if (routeResult != null && routeResult.isReachable()) {
            return new BreakRecommendation(
                    "URBAN_WALK",
                    "Urban Walk empfohlen",
                    "Ein Grünbereich ist in " + routeResult.getWalkingTimeMinutes()
                            + " Minuten erreichbar: " + routeResult.getDestinationName(),
                    routeResult.getWalkingTimeMinutes()
            );
        }

        return new BreakRecommendation(
                "INDOOR_BREAK",
                "Indoor-Pause empfohlen",
                "Aktuell wurde kein geeigneter Grünbereich in 15 Minuten Gehzeit gefunden.",
                5
        );
    }
    public void getCurrentBreakRecommendationWithRealMaps(
            @NonNull Context context,
            @NonNull BreakRecommendationCallback callback
    ) {
        StressResult stressResult = getCurrentStressResult();

        if (!stressResult.isBreakRecommended()) {
            callback.onRecommendationReady(
                    new BreakRecommendation(
                            "NONE",
                            "Keine Pause notwendig",
                            stressResult.getExplanation(),
                            0
                    )
            );
            return;
        }

        RealMapsBreakService realMapsBreakService = new RealMapsBreakService(context);

        realMapsBreakService.getBreakDecision(
                (recommendationTitle, recommendationText, routeResult, usedRealLocation, foundRealPlace, usedRealRoute) -> {
                    BreakRecommendation recommendation = new BreakRecommendation(
                            routeResult.isReachable() ? "URBAN_WALK" : "INDOOR_BREAK",
                            recommendationTitle,
                            recommendationText,
                            routeResult.isReachable() ? routeResult.getWalkingTimeMinutes() : 5
                    );

                    callback.onRecommendationReady(recommendation);
                }
        );
    }
}
