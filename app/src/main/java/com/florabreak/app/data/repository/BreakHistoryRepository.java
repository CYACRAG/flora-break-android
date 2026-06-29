package com.florabreak.app.data.repository;

import android.content.Context;
import android.content.SharedPreferences;

import com.florabreak.app.model.SavedBreak;

/**
 * Repository für abgeschlossene Pausen.
 *
 * Für den Prototyp speichern wir zunächst die letzte abgeschlossene Pause.
 * TODO: Später durch Room Database ersetzen, um eine vollständige Verlaufsliste
 *       mit mehreren Pausen zu speichern.
 */
public class BreakHistoryRepository {

    private static final String PREF_NAME = "flora_break_history";

    private static final String KEY_HAS_BREAK = "has_break";
    private static final String KEY_TIMESTAMP = "timestamp";
    private static final String KEY_TITLE = "title";
    private static final String KEY_TYPE = "type";
    private static final String KEY_DURATION = "duration";
    private static final String KEY_STRESS_SCORE = "stress_score";
    private static final String KEY_STRESS_LABEL = "stress_label";
    private static final String KEY_ROUTE_NAME = "route_name";
    private static final String KEY_RATING = "rating";
    private static final String KEY_ROUTE_PROOF = "route_proof";

    private final SharedPreferences preferences;

    public BreakHistoryRepository(Context context) {
        this.preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void saveCompletedBreak(SavedBreak savedBreak) {
        preferences.edit()
                .putBoolean(KEY_HAS_BREAK, true)
                .putLong(KEY_TIMESTAMP, savedBreak.getTimestamp())
                .putString(KEY_TITLE, savedBreak.getTitle())
                .putString(KEY_TYPE, savedBreak.getType())
                .putInt(KEY_DURATION, savedBreak.getDurationMinutes())
                .putInt(KEY_STRESS_SCORE, savedBreak.getStressScore())
                .putString(KEY_STRESS_LABEL, savedBreak.getStressLabel())
                .putString(KEY_ROUTE_NAME, savedBreak.getRouteName())
                .putInt(KEY_RATING, savedBreak.getRating())
                .putBoolean(KEY_ROUTE_PROOF, savedBreak.isRouteProofCompleted())
                .apply();
    }

    public boolean hasSavedBreak() {
        return preferences.getBoolean(KEY_HAS_BREAK, false);
    }

    public SavedBreak getLatestBreak() {
        if (!hasSavedBreak()) {
            return null;
        }

        return new SavedBreak(
                preferences.getLong(KEY_TIMESTAMP, System.currentTimeMillis()),
                preferences.getString(KEY_TITLE, "Abgeschlossene Pause"),
                preferences.getString(KEY_TYPE, "URBAN_WALK"),
                preferences.getInt(KEY_DURATION, 0),
                preferences.getInt(KEY_STRESS_SCORE, 0),
                preferences.getString(KEY_STRESS_LABEL, "Unbekannt"),
                preferences.getString(KEY_ROUTE_NAME, "Keine Route gespeichert"),
                preferences.getInt(KEY_RATING, 0),
                preferences.getBoolean(KEY_ROUTE_PROOF, false)
        );
    }

    public void clearHistory() {
        preferences.edit().clear().apply();
    }
}
