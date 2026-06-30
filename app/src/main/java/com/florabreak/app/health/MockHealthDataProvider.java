package com.florabreak.app.health;

import com.florabreak.app.data.HealthDataProvider;
import com.florabreak.app.model.StressData;

/**
 * Liefert simulierte Gesundheitsdaten für den Prototyp.
 *
 * Diese Klasse wird genutzt, solange keine echte Smartwatch,
 * kein Demo-Regler oder Health Connect verfügbar ist.
 */
public class MockHealthDataProvider implements HealthDataProvider {

    private String scenario = "HIGH";

    public MockHealthDataProvider() {
    }

    public MockHealthDataProvider(String scenario) {
        this.scenario = scenario;
    }

    public void setScenario(String scenario) {
        this.scenario = scenario;
    }

    /**
     * Diese Methode kommt aus dem Interface HealthDataProvider.
     * Sie gibt die aktuellen Gesundheitsdaten zurück.
     */
    @Override
    public StressData getCurrentStressData() {
        if ("LOW".equals(scenario)) {
            return getLowStressData();
        } else if ("MEDIUM".equals(scenario)) {
            return getMediumStressData();
        } else {
            return getHighStressData();
        }
    }

    public StressData getLowStressData() {
        return new StressData(
                50.0,
                50.0,
                72,
                120,
                80,
                System.currentTimeMillis()
        );
    }

    public StressData getMediumStressData() {
        return new StressData(
                58.0,
                50.0,
                84,
                130,
                84,
                System.currentTimeMillis()
        );
    }

    public StressData getHighStressData() {
        return new StressData(
                65.0,
                50.0,
                95,
                140,
                90,
                System.currentTimeMillis()
        );
    }
}
