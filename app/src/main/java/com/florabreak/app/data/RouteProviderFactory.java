package com.florabreak.app.data;

import android.content.Context;

import com.florabreak.app.maps.MockRouteProvider;

/**
 * Erstellt den RouteProvider für den Controller.
 *
 * Hinweis:
 * Die echte Google-Maps-Integration läuft aktuell asynchron über RealMapsBreakService.
 * Der Controller nutzt deshalb vorerst MockRouteProvider als stabile synchrone Route.
 *
 * Später kann RouteProvider entweder async erweitert werden
 * oder RealMapsRouteProvider bekommt einen synchron kompatiblen Adapter.
 */
public class RouteProviderFactory {

    private RouteProviderFactory() {
        // Utility-Klasse, keine Instanzen nötig.
    }

    public static RouteProvider create(Context context) {
        return new MockRouteProvider();
    }
}
