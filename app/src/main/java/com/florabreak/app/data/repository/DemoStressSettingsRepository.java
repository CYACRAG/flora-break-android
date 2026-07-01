package com.florabreak.app.data.repository;

import android.content.Context;
import android.content.SharedPreferences;

import com.florabreak.app.model.DemoStressSettings;

/**
 * Repository für Demo-Stressdaten.
 *
 * Diese Werte werden später z. B. über Slider im Profil-Screen gesetzt.
 * Sie ermöglichen eine stabile Demo ohne echte Smartwatch.
 */
public class DemoStressSettingsRepository {

    private static final String PREF_NAME = "flora_demo_stress";

    private static final String KEY_DEMO_ENABLED = "demo_enabled";
    private static final String KEY_CURRENT_HRV = "current_hrv";
    private static final String KEY_NORMAL_HRV = "normal_hrv";
    private static final String KEY_HEART_RATE = "heart_rate";
    private static final String KEY_SYSTOLIC = "systolic";
    private static final String KEY_DIASTOLIC = "diastolic";

    private final SharedPreferences preferences;

    public DemoStressSettingsRepository(Context context) {
        this.preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void saveSettings(DemoStressSettings settings) {
        preferences.edit()
                .putBoolean(KEY_DEMO_ENABLED, settings.isDemoModeEnabled())
                .putFloat(KEY_CURRENT_HRV, (float) settings.getCurrentHrv())
                .putFloat(KEY_NORMAL_HRV, (float) settings.getNormalHrv())
                .putInt(KEY_HEART_RATE, settings.getHeartRate())
                .putInt(KEY_SYSTOLIC, settings.getSystolicBloodPressure())
                .putInt(KEY_DIASTOLIC, settings.getDiastolicBloodPressure())
                .apply();
    }

    public DemoStressSettings getSettings() {
	return new DemoStressSettings(
	        preferences.getBoolean(KEY_DEMO_ENABLED, true),
	        preferences.getFloat(KEY_CURRENT_HRV, 45.0f),
	        preferences.getFloat(KEY_NORMAL_HRV, 60.0f),
	        preferences.getInt(KEY_HEART_RATE, 100),
	        preferences.getInt(KEY_SYSTOLIC, 135),
	        preferences.getInt(KEY_DIASTOLIC, 85)
	);
    }

    public boolean isDemoModeEnabled() {
        return preferences.getBoolean(KEY_DEMO_ENABLED, true);
    }

    public void clearSettings() {
        preferences.edit().clear().apply();
    }
}
