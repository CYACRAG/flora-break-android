package com.florabreak.app.data.repository;

import android.content.Context;
import android.content.SharedPreferences;

import com.florabreak.app.model.UserProfile;

/**
 * Repository für Profildaten.
 *
 * Aktuell nutzt der Prototyp SharedPreferences.
 * TODO: Für eine finale Version kann diese Klasse intern auf Room Database
 *       oder Firebase umgestellt werden, ohne die UI stark zu ändern.
 */
public class ProfileRepository {

    private static final String PREF_NAME = "flora_profile";

    private static final String KEY_NAME = "name";
    private static final String KEY_AGE = "age";
    private static final String KEY_HEIGHT_CM = "height_cm";
    private static final String KEY_WEIGHT_KG = "weight_kg";
    private static final String KEY_WORK_START = "work_start";
    private static final String KEY_WORK_END = "work_end";
    private static final String KEY_SUBJECTIVE_STRESS = "subjective_stress";
    private static final String KEY_PROFILE_COMPLETED = "profile_completed";

    private final SharedPreferences preferences;

    public ProfileRepository(Context context) {
        this.preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void saveProfile(UserProfile profile) {
        preferences.edit()
                .putString(KEY_NAME, profile.getName())
                .putInt(KEY_AGE, profile.getAge())
                .putInt(KEY_HEIGHT_CM, profile.getHeightCm())
                .putInt(KEY_WEIGHT_KG, profile.getWeightKg())
                .putString(KEY_WORK_START, profile.getWorkStartTime())
                .putString(KEY_WORK_END, profile.getWorkEndTime())
                .putInt(KEY_SUBJECTIVE_STRESS, profile.getSubjectiveStressLevel())
                .putBoolean(KEY_PROFILE_COMPLETED, true)
                .apply();
    }

    public UserProfile getProfile() {
        return new UserProfile(
                preferences.getString(KEY_NAME, ""),
                preferences.getInt(KEY_AGE, 0),
                preferences.getInt(KEY_HEIGHT_CM, 0),
                preferences.getInt(KEY_WEIGHT_KG, 0),
                preferences.getString(KEY_WORK_START, "09:00"),
                preferences.getString(KEY_WORK_END, "17:00"),
                preferences.getInt(KEY_SUBJECTIVE_STRESS, 3),
                preferences.getBoolean(KEY_PROFILE_COMPLETED, false)
        );
    }

    public boolean isProfileCompleted() {
        return preferences.getBoolean(KEY_PROFILE_COMPLETED, false);
    }

    public void clearProfile() {
        preferences.edit().clear().apply();
    }
}
