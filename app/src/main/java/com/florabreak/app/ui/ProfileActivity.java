package com.florabreak.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.florabreak.app.R;

/**
 * Profil-Screen für Flora Break.
 *
 * Wichtig:
 * Diese Activity ist aktuell ein UI-Prototyp.
 * Die angezeigten Profildaten sind Mock-Daten.
 *
 * Später kann diese Activity erweitert werden:
 * - Nutzerdaten aus einer lokalen Datenbank laden
 * - Smartwatch-/Health-Connect-Status anzeigen
 * - Arbeitszeiten und Pausenziele bearbeiten
 * - Profiländerungen speichern
 */
public class ProfileActivity extends AppCompatActivity {

    private TextView backFromProfileButton;
    private Button editProfileButton;

    private LinearLayout navHomeFromProfile;
    private LinearLayout navHistoryFromProfile;
    private LinearLayout navStatsFromProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Header / Hauptbutton
        backFromProfileButton = findViewById(R.id.backFromProfileButton);
        editProfileButton = findViewById(R.id.editProfileButton);

        // Bottom Navigation
        navHomeFromProfile = findViewById(R.id.navHomeFromProfile);
        navHistoryFromProfile = findViewById(R.id.navHistoryFromProfile);
        navStatsFromProfile = findViewById(R.id.navStatsFromProfile);

        // Zurück zum vorherigen Screen
        backFromProfileButton.setOnClickListener(view -> finish());

        /*
         * Profilbearbeitung ist aktuell nur ein Platzhalter.
         * Später kann hier ein Bearbeiten-Screen geöffnet werden,
         * der Daten in einer lokalen Datenbank oder im Backend speichert.
         */
        editProfileButton.setOnClickListener(view ->
                Toast.makeText(this, "Profilbearbeitung wird später ergänzt", Toast.LENGTH_SHORT).show()
        );

        // Zurück zum Home-Screen
        navHomeFromProfile.setOnClickListener(view -> {
            Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

        // Wechsel zum Verlauf-Screen
        navHistoryFromProfile.setOnClickListener(view -> {
            Intent intent = new Intent(ProfileActivity.this, HistoryActivity.class);
            startActivity(intent);
        });

        // Statistik bleibt im aktuellen Prototyp ein Platzhalter
        navStatsFromProfile.setOnClickListener(view ->
                Toast.makeText(this, "Statistik wird später ergänzt", Toast.LENGTH_SHORT).show()
        );
    }
}