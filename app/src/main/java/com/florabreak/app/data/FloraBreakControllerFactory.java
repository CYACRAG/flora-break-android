package com.florabreak.app.data;

import android.content.Context;

/**
 * Baut einen vollständig konfigurierten FloraBreakController.
 *
 * Die UI muss später nur diese Factory nutzen und nicht mehr selbst
 * Health-, Stress- oder Routen-Komponenten zusammenbauen.
 */
public class FloraBreakControllerFactory {

    private FloraBreakControllerFactory() {
        // Utility-Klasse, keine Instanzen nötig.
    }

    public static FloraBreakController create(Context context) {
        HealthDataProvider healthDataProvider =
                HealthDataProviderFactory.create(context);

        RouteProvider routeProvider =
                RouteProviderFactory.create(context);

        return new FloraBreakController(
                healthDataProvider,
                routeProvider
        );
    }
}
