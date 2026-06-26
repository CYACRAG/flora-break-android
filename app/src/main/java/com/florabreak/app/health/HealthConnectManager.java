package com.florabreak.app.health;

import android.content.Context;

import com.florabreak.app.model.StressData;

/**
 * Vorbereitung für echte Health-Connect-Daten.
 *
 * In der ersten Version nutzt diese Klasse noch keine echte Health-Connect-API.
 * Stattdessen verwendet sie MockHealthDataProvider als sicheren Fallback.
 */
public class HealthConnectManager implements HealthDataProvider {

    private final Context context;
    private final MockHealthDataProvider fallbackProvider;

    /**
     * Konstruktor.
     *
     * @param context Android Context, wird später für Health Connect benötigt.
     */
    public HealthConnectManager(Context context) {
        this.context = context;
        this.fallbackProvider = new MockHealthDataProvider();
    }

    /**
     * Gibt aktuelle Gesundheitsdaten zurück.
     *
     * Später soll hier geprüft werden:
     * 1. Ist Health Connect verfügbar?
     * 2. Hat der Nutzer Berechtigungen gegeben?
     * 3. Gibt es echte Gesundheitsdaten?
     *
     * Aktuell geben wir sichere Mock-Daten zurück.
     */
    @Override
    public StressData getCurrentHealthData() {
        return fallbackProvider.getCurrentHealthData();
    }

    /**
     * Prüft später, ob Health Connect auf dem Gerät verfügbar ist.
     *
     * Für Version 1 geben wir false zurück,
     * weil die echte API noch nicht eingebunden ist.
     */
    public boolean isHealthConnectAvailable() {
        return false;
    }
}
