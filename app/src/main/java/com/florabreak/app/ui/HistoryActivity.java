package com.florabreak.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.florabreak.app.R;

/**
 * Verlauf-Screen für gespeicherte Pausen.
 *
 * Wichtig:
 * Diese Activity ist aktuell noch ein UI-Prototyp.
 * Die Daten kommen aus MockUiDataProvider und nicht aus einer echten Datenbank.
 *
 * Später kann diese Klasse erweitert werden:
 * - echte Pausen aus einer lokalen Datenbank laden
 * - mehrere Pausen in einer RecyclerView anzeigen
 * - Daten aus Maps, Stress Engine und Health Connect zusammenführen
 */
public class HistoryActivity extends AppCompatActivity {

    private TextView backFromHistoryButton;

    private LinearLayout navHomeFromHistory;
    private LinearLayout navStatsFromHistory;
    private LinearLayout navProfileFromHistory;

    private LinearLayout historyBreakCard;
    private LinearLayout historyEmptyCard;

    private TextView historyBreakTitleText;
    private TextView historyBreakDetailsText;
    private TextView historyBreakRatingText;
    private TextView historyBreakStressText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        // Header / Navigation
        backFromHistoryButton = findViewById(R.id.backFromHistoryButton);

        // Bottom Navigation
        navHomeFromHistory = findViewById(R.id.navHomeFromHistory);
        navStatsFromHistory = findViewById(R.id.navStatsFromHistory);
        navProfileFromHistory = findViewById(R.id.navProfileFromHistory);

        // Karten für Verlauf
        historyBreakCard = findViewById(R.id.historyBreakCard);
        historyEmptyCard = findViewById(R.id.historyEmptyCard);

        // Textfelder der Pause
        historyBreakTitleText = findViewById(R.id.historyBreakTitleText);
        historyBreakDetailsText = findViewById(R.id.historyBreakDetailsText);
        historyBreakRatingText = findViewById(R.id.historyBreakRatingText);
        historyBreakStressText = findViewById(R.id.historyBreakStressText);

        /*
         * UI mit Mock-Daten aktualisieren.
         * Später wird hier eine echte Datenbank-Abfrage stehen.
         */
        updateHistory();

        backFromHistoryButton.setOnClickListener(view -> finish());

        navHomeFromHistory.setOnClickListener(view -> {
            Intent intent = new Intent(HistoryActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

        navStatsFromHistory.setOnClickListener(view ->
                Toast.makeText(this, "Statistik wird später ergänzt", Toast.LENGTH_SHORT).show()
        );

        navProfileFromHistory.setOnClickListener(view -> {
            Intent intent = new Intent(HistoryActivity.this, ProfileActivity.class);
            startActivity(intent);
        });
    }

    /**
     * Aktualisiert die Verlaufsliste.
     *
     * Aktuell:
     * - Es wird nur die zuletzt gespeicherte Mock-Pause angezeigt.
     *
     * Später:
     * - mehrere Pausen aus Datenbank laden
     * - pro Pause Route, Dauer, Distanz, Stresswerte und Bewertung anzeigen
     */
    private void updateHistory() {
        if (MockUiDataProvider.getSavedBreaks().isEmpty()) {
            historyBreakCard.setVisibility(View.GONE);
            historyEmptyCard.setVisibility(View.VISIBLE);
            return;
        }

        UiSavedBreak savedBreak = MockUiDataProvider.getSavedBreaks().get(0);

        historyBreakCard.setVisibility(View.VISIBLE);
        historyEmptyCard.setVisibility(View.GONE);

        /*
         * Platzhalterdaten:
         * Titel und Details kommen aktuell aus MockUiDataProvider.
         * Stresswerte und Bewertung sind aktuell noch UI-Mockwerte.
         */
        historyBreakTitleText.setText(savedBreak.getTitle());
        historyBreakDetailsText.setText(savedBreak.getDetails());
        historyBreakRatingText.setText(createStarRating(savedBreak.getRating()));
        historyBreakStressText.setText("Stress reduziert: 7.6 → 4.8");
    }
    /**
     * Erstellt die Sterneanzeige aus der gespeicherten Bewertung.
     *
     * Beispiel:
     * 3 Sterne → ★★★☆☆
     *
     * Später kann diese Logik auch in eine eigene UI-Hilfsklasse ausgelagert werden.
     */
    private String createStarRating(int rating) {
        StringBuilder stars = new StringBuilder();

        for (int i = 1; i <= 5; i++) {
            if (i <= rating) {
                stars.append("★");
            } else {
                stars.append("☆");
            }
        }

        return stars.toString();
    }
}