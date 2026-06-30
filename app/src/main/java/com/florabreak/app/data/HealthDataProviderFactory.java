package com.florabreak.app.data;

import android.content.Context;

import com.florabreak.app.data.demo.DemoStressDataProvider;
import com.florabreak.app.data.repository.DemoStressSettingsRepository;
import com.florabreak.app.health.HealthConnectDataProvider;
import com.florabreak.app.health.MockHealthDataProvider;

/**
 * Erstellt den passenden HealthDataProvider für die aktuelle App-Situation.
 *
 * Priorität:
 * 1. Demo-Modus aktiv -> DemoStressDataProvider
 * 2. Health Connect verfügbar -> HealthConnectDataProvider
 * 3. Fallback -> MockHealthDataProvider
 *
 * Dadurch muss die UI später nicht selbst entscheiden,
 * woher die Gesundheitsdaten kommen.
 */
public class HealthDataProviderFactory {

    private HealthDataProviderFactory() {
        // Utility-Klasse, keine Instanzen nötig.
    }

    public static HealthDataProvider create(Context context) {
        DemoStressSettingsRepository demoRepository =
                new DemoStressSettingsRepository(context);

        if (demoRepository.isDemoModeEnabled()) {
            return new DemoStressDataProvider(context);
        }

        if (HealthConnectDataProvider.isAvailable(context)) {
            return new HealthConnectDataProvider(context);
        }

        return new MockHealthDataProvider();
    }
}
