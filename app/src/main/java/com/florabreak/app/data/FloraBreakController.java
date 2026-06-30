package com.florabreak.app.data;

import com.florabreak.app.model.BreakRecommendation;
import com.florabreak.app.model.FloraBreakSessionResult;
import com.florabreak.app.model.RouteResult;
import com.florabreak.app.model.StressData;
import com.florabreak.app.model.StressResult;
import com.florabreak.app.stress.StressDecisionEngine;

/**
 * Zentrale Steuerklasse der Flora-Break-App.
 *
 * Aufgabe:
 * - Gesundheitsdaten aus einem HealthDataProvider lesen
 * - Stress über die StressDecisionEngine bewerten
 * - bei Bedarf eine Route über den RouteProvider anfragen
 * - daraus eine einheitliche Pausenempfehlung erzeugen
 *
 * Wichtig:
 * Diese Klasse enthält keine UI-Logik.
 * Die UI soll später nur noch die Ergebnisse dieses Controllers anzeigen.
 */
public class FloraBreakController {

    private final HealthDataProvider healthDataProvider;
    private final RouteProvider routeProvider;
    private final StressDecisionEngine stressDecisionEngine;

    /**
     * Standard-Konstruktor für die App.
     *
     * @param healthDataProvider Quelle für Health-/Mock-/Demo-Daten
     * @param routeProvider Quelle für Maps-/Mock-/Fallback-Routen
     */
    public FloraBreakController(HealthDataProvider healthDataProvider, RouteProvider routeProvider) {
        this.healthDataProvider = healthDataProvider;
        this.routeProvider = routeProvider;
        this.stressDecisionEngine = new StressDecisionEngine();
    }

    /**
     * Testbarer Konstruktor.
     *
     * Damit kann später eine eigene StressDecisionEngine übergeben werden,
     * z. B. für Unit-Tests oder alternative Bewertungslogik.
     */
    public FloraBreakController(
            HealthDataProvider healthDataProvider,
            RouteProvider routeProvider,
            StressDecisionEngine stressDecisionEngine
    ) {
        this.healthDataProvider = healthDataProvider;
        this.routeProvider = routeProvider;
        this.stressDecisionEngine = stressDecisionEngine;
    }

    /**
     * Führt eine vollständige aktuelle Auswertung durch.
     *
     * Ablauf:
     * 1. Health-/Mock-/Demo-Daten lesen
     * 2. Stress bewerten
     * 3. falls nötig Route prüfen
     * 4. Pausenempfehlung erzeugen
     * 5. alles als FloraBreakSessionResult zurückgeben
     */
    public FloraBreakSessionResult evaluateCurrentSituation() {
        StressData stressData = healthDataProvider.getCurrentStressData();
        StressResult stressResult = stressDecisionEngine.analyzeStress(stressData);

        RouteResult routeResult = null;
        BreakRecommendation breakRecommendation;

        if (!stressResult.isBreakRecommended()) {
            breakRecommendation = createNoBreakRecommendation(stressResult);
        } else {
            routeResult = routeProvider.getNearestBreakRoute();
            breakRecommendation = createBreakRecommendation(stressResult, routeResult);
        }

        return new FloraBreakSessionResult(
                stressData,
                stressResult,
                breakRecommendation,
                routeResult,
                System.currentTimeMillis()
        );
    }

    /**
     * Gibt nur das aktuelle Stress-Ergebnis zurück.
     *
     * Diese Methode bleibt erhalten, damit bestehender Code nicht kaputtgeht.
     */
    public StressResult getCurrentStressResult() {
        return evaluateCurrentSituation().getStressResult();
    }

    /**
     * Gibt nur die aktuelle Pausenempfehlung zurück.
     *
     * Diese Methode bleibt erhalten, damit bestehender Code nicht kaputtgeht.
     */
    public BreakRecommendation getCurrentBreakRecommendation() {
        return evaluateCurrentSituation().getBreakRecommendation();
    }

    /**
     * Empfehlung, wenn aktuell keine Pause notwendig ist.
     */
    private BreakRecommendation createNoBreakRecommendation(StressResult stressResult) {
        return new BreakRecommendation(
                "NONE",
                "Keine Pause notwendig",
                stressResult.getExplanation(),
                0
        );
    }

    /**
     * Empfehlung, wenn Stress erhöht ist und eine Pause vorgeschlagen werden soll.
     *
     * Die Maps-Logik entscheidet, ob eine erreichbare Route verfügbar ist.
     * Falls keine echte Route verfügbar ist, wird eine fallback-sichere Empfehlung erzeugt.
     */
    private BreakRecommendation createBreakRecommendation(
            StressResult stressResult,
            RouteResult routeResult
    ) {
        if (routeResult != null && routeResult.isReachable()) {
            return new BreakRecommendation(
                    "URBAN_WALK",
                    "Urban Walk empfohlen",
                    "Ein Grünbereich ist in "
                            + routeResult.getWalkingTimeMinutes()
                            + " Minuten erreichbar: "
                            + routeResult.getDestinationName(),
                    routeResult.getWalkingTimeMinutes()
            );
        }

        return new BreakRecommendation(
                "FALLBACK_BREAK",
                "Pause empfohlen",
                "Dein Stresslevel ist erhöht. "
                        + "Aktuell konnte keine sichere echte Route berechnet werden. "
                        + stressResult.getExplanation(),
                5
        );
    }
}
