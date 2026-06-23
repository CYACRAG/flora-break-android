package com.florabreak.app.health;

import com.florabreak.app.data.HealthDataProvider;
import com.florabreak.app.model.StressData;

public class MockHealthDataProvider implements HealthDataProvider {

    @Override
    public StressData getCurrentStressData() {
        return new StressData(
                65.0,
                50.0,
                92,
                138,
                86,
                System.currentTimeMillis()
        );
    }
}
