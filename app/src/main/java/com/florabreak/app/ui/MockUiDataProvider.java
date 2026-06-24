package com.florabreak.app.ui;

import java.util.ArrayList;
import java.util.List;

public class MockUiDataProvider {

    /*
     * Dieser Provider enthält nur Mock-Daten für die UI.
     *
     * Wichtig für das Team:
     * - Health Connect, Stress Engine und Maps/OSM werden hier NICHT wirklich eingebaut.
     * - Die UI simuliert nur, welche Daten später von diesen Modulen kommen.
     * - Dadurch können Health-, Maps- und Stress-Team unabhängig weiterarbeiten.
     */

    private static final int ROUTE_SCENARIO_MIXED = 1;
    private static final int ROUTE_SCENARIO_ONLY_GREEN = 2;
    private static final int ROUTE_SCENARIO_ONLY_URBAN = 3;

    /*
     * Für die Präsentation ist MIXED am sinnvollsten:
     * Vorschlag 1 = Grünfläche
     * Vorschlag 2 = Urban-Walk-Fallback
     *
     * Dadurch sieht man direkt die Fallback-Logik.
     */
    private static final int ACTIVE_ROUTE_SCENARIO = ROUTE_SCENARIO_MIXED;

    private MockUiDataProvider() {
        // Diese Klasse wird nicht als Objekt erstellt.
        // Sie liefert nur zentrale Mock-Daten für die UI.
    }

    public static UiStressState getCurrentStressState() {
        return new UiStressState(
                7.6,
                "Sehr gestresst",
                "Deine Werte zeigen eine erhöhte Belastung. Eine kurze Naturpause könnte dir helfen.",
                70,
                "Naturpause empfohlen"
        );
    }

    public static List<UiRouteSuggestion> getRouteSuggestions() {
        /*
         * Simuliert Ergebnisse aus Maps/OSM.
         *
         * Später entscheidet der echte RouteService:
         * - zwei Grünflächen, wenn zwei passende Grünflächen erreichbar sind
         * - eine Grünfläche + ein Urban Walk, wenn nur eine Grünfläche passt
         * - zwei Urban Walks, wenn keine Grünfläche in sinnvoller Gehzeit erreichbar ist
         */

        if (ACTIVE_ROUTE_SCENARIO == ROUTE_SCENARIO_ONLY_GREEN) {
            return getOnlyGreenRouteSuggestions();
        }

        if (ACTIVE_ROUTE_SCENARIO == ROUTE_SCENARIO_ONLY_URBAN) {
            return getOnlyUrbanWalkSuggestions();
        }

        return getMixedRouteSuggestions();
    }

    private static List<UiRouteSuggestion> getMixedRouteSuggestions() {
        List<UiRouteSuggestion> routes = new ArrayList<>();

        routes.add(new UiRouteSuggestion(
                "Grünfläche in der Nähe",
                "380 m",
                "18 Min",
                "Naturroute",
                false
        ));

        routes.add(new UiRouteSuggestion(
                "Ruhiger Urban Walk",
                "520 m",
                "20 Min",
                "Fallback-Route",
                true
        ));

        return routes;
    }

    private static List<UiRouteSuggestion> getOnlyGreenRouteSuggestions() {
        List<UiRouteSuggestion> routes = new ArrayList<>();

        routes.add(new UiRouteSuggestion(
                "Grünfläche in der Nähe",
                "380 m",
                "18 Min",
                "Naturroute",
                false
        ));

        routes.add(new UiRouteSuggestion(
                "Alternative Grünfläche",
                "650 m",
                "20 Min",
                "Parkroute",
                false
        ));

        return routes;
    }

    private static List<UiRouteSuggestion> getOnlyUrbanWalkSuggestions() {
        List<UiRouteSuggestion> routes = new ArrayList<>();

        routes.add(new UiRouteSuggestion(
                "Ruhiger Urban Walk",
                "520 m",
                "20 Min",
                "Fallback-Route",
                true
        ));

        routes.add(new UiRouteSuggestion(
                "Kurze Stadt-Route",
                "430 m",
                "15 Min",
                "Urban Walk",
                true
        ));

        return routes;
    }

    public static String getUserName() {
        return "Lisa";
    }

    public static String getTodayText() {
        return "Montag, 8. Juni 2026";
    }

    public static double getAfterBreakStressScore() {
        return 4.8;
    }

    public static String getAfterBreakStressLabel() {
        return "Leicht gestresst";
    }
}