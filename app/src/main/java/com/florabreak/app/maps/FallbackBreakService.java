package com.florabreak.app.maps;

/**
 * Entscheidet, ob ein Parkziel erreichbar ist oder alternativ
 * ein Urban Walk empfohlen wird.
 *
 * Für Nutzer wird keine Indoor-/Fallback-Logik angezeigt.
 * Wenn keine passende Grünfläche erreichbar ist, empfiehlt Flora Break
 * einen kurzen Urban Walk in der Umgebung.
 */
public class FallbackBreakService {

    public String getRecommendation(boolean parkReachable) {
        if (parkReachable) {
            return "Parkroute empfohlen";
        }

        return "Urban Walk empfohlen";
    }
}
