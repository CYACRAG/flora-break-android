package com.florabreak.app.health;

import com.florabreak.app.model.StressData;

/**
 * Gemeinsame Schnittstelle für Gesundheitsdaten.
 *
 * Dadurch kann die App später entweder Mock-Daten
 * oder echte Health-Connect-Daten verwenden.
 */
public interface HealthDataProvider {

    /**
     * Gibt aktuelle Gesundheitsdaten zurück.
     */
    StressData getCurrentHealthData();
}
