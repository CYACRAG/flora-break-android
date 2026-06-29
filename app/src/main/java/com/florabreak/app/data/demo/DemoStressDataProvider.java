package com.florabreak.app.data.demo;

import android.content.Context;

import com.florabreak.app.data.HealthDataProvider;
import com.florabreak.app.data.repository.DemoStressSettingsRepository;
import com.florabreak.app.model.DemoStressSettings;
import com.florabreak.app.model.StressData;

/**
 * HealthDataProvider für den Demo-Modus.
 *
 * Diese Klasse nutzt keine echte Smartwatch und kein echtes Health Connect.
 * Stattdessen liest sie simulierte HRV-/Pulswerte aus DemoStressSettingsRepository.
 *
 * Später können diese Werte über Slider im Profil-Screen gesetzt werden.
 * Wichtig: Die Daten laufen trotzdem durch dieselbe StressEngine wie echte Health-Daten.
 */
public class DemoStressDataProvider implements HealthDataProvider {

    private final DemoStressSettingsRepository settingsRepository;

    public DemoStressDataProvider(Context context) {
        this.settingsRepository = new DemoStressSettingsRepository(context);
    }

    @Override
    public StressData getCurrentStressData() {
        DemoStressSettings settings = settingsRepository.getSettings();

        return new StressData(
                settings.getCurrentHrv(),
                settings.getNormalHrv(),
                settings.getHeartRate(),
                settings.getSystolicBloodPressure(),
                settings.getDiastolicBloodPressure(),
                System.currentTimeMillis()
        );
    }

    public boolean isDemoModeEnabled() {
        return settingsRepository.isDemoModeEnabled();
    }
}
