package com.florabreak.app.data;

import com.florabreak.app.model.BreakRecommendation;
import com.florabreak.app.model.RouteResult;
import com.florabreak.app.model.StressData;
import com.florabreak.app.model.StressResult;
import com.florabreak.app.stress.StressDecisionEngine;

public class FloraBreakController {

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
}
