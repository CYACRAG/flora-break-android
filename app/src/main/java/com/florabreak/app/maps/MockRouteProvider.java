package com.florabreak.app.maps;

import com.florabreak.app.data.RouteProvider;
import com.florabreak.app.model.RouteResult;

public class MockRouteProvider implements RouteProvider {

    @Override
    public RouteResult getNearestBreakRoute() {
        return new RouteResult(
                "Nächster Grünbereich",
                51.022,
                7.562,
                12,
                true
        );
    }
}
