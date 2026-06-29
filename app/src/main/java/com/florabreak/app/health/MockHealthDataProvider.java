package com.florabreak.app.health;

import com.florabreak.app.model.StressData;
import com.florabreak.app.data.HealthDataProvider;

/**
 * Liefert simulierte Gesundheitsdaten für den Prototyp.
 *
 * Diese Klasse wird genutzt, solange keine echte Smartwatch
 * oder Health Connect verfügbar ist.
 */
public class MockHealthDataProvider implements HealthDataProvider {

    /**
     * Diese Methode kommt aus dem Interface HealthDataProvider.
     * Sie gibt die aktuellen Gesundheitsdaten zurück.
     *
     * Für die erste Demo nutzen wir hohe Stresswerte,
     * damit die App direkt eine Pausenempfehlung anzeigen kann.
     */
    private String scenario = "HIGH";
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

    /**
     * Beispielwerte für niedrigen Stress.
     */
    public StressData getLowStressData() {
        return new StressData(
                50.0, // currentHrv
                50.0, // normalHrv
                72,   // heartRate
                120,  // systolicBloodPressure
                80,   // diastolicBloodPressure
                System.currentTimeMillis()
        );
    }

    /**
     * Beispielwerte für mittleren Stress.
     */
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

    /**
     * Beispielwerte für hohen Stress.
     */
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
