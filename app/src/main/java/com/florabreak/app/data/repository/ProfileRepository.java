package com.florabreak.app.data.repository;

import android.content.Context;
import android.content.SharedPreferences;

import com.florabreak.app.model.UserProfile;

/**
 * Repository für Profildaten.
 *
 * Aktuell nutzt der Prototyp SharedPreferences.
 * Der Arbeitsort wird lokal gespeichert und kann von Maps genutzt werden.
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

    private static final String KEY_WORK_LOCATION_NAME = "work_location_name";
    private static final String KEY_WORK_LATITUDE = "work_latitude";
    private static final String KEY_WORK_LONGITUDE = "work_longitude";
    private static final String KEY_WORK_LOCATION_SAVED = "work_location_saved";

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
                .putString(KEY_WORK_LOCATION_NAME, profile.getWorkLocationName())
                .putFloat(KEY_WORK_LATITUDE, (float) profile.getWorkLatitude())
                .putFloat(KEY_WORK_LONGITUDE, (float) profile.getWorkLongitude())
                .putBoolean(KEY_WORK_LOCATION_SAVED, profile.isWorkLocationSaved())
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
                preferences.getString(KEY_WORK_LOCATION_NAME, ""),
                preferences.getFloat(KEY_WORK_LATITUDE, 0f),
                preferences.getFloat(KEY_WORK_LONGITUDE, 0f),
                preferences.getBoolean(KEY_WORK_LOCATION_SAVED, false),
                preferences.getBoolean(KEY_PROFILE_COMPLETED, false)
        );
    }

    public boolean isProfileCompleted() {
        return preferences.getBoolean(KEY_PROFILE_COMPLETED, false);
    }

    public boolean hasWorkLocation() {
        return preferences.getBoolean(KEY_WORK_LOCATION_SAVED, false);
    }

    public void saveWorkLocation(String name, double latitude, double longitude) {
        preferences.edit()
                .putString(KEY_WORK_LOCATION_NAME, name)
                .putFloat(KEY_WORK_LATITUDE, (float) latitude)
                .putFloat(KEY_WORK_LONGITUDE, (float) longitude)
                .putBoolean(KEY_WORK_LOCATION_SAVED, true)
                .apply();
    }

    public void clearWorkLocation() {
        preferences.edit()
                .remove(KEY_WORK_LOCATION_NAME)
                .remove(KEY_WORK_LATITUDE)
                .remove(KEY_WORK_LONGITUDE)
                .putBoolean(KEY_WORK_LOCATION_SAVED, false)
                .apply();
    }

    public void clearProfile() {
        preferences.edit().clear().apply();
    }
}
