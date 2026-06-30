package com.florabreak.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.florabreak.app.R;
import com.florabreak.app.data.repository.BreakHistoryRepository;
import com.florabreak.app.model.SavedBreak;

/**
 * Verlauf-Screen für gespeicherte Pausen.
 *
 * Aktuell speichert der Prototyp die letzte abgeschlossene Pause
 * über BreakHistoryRepository in SharedPreferences.
 *
 * Später kann dieses Repository durch Room oder Firebase ersetzt werden,
 * um mehrere Pausen dauerhaft zu speichern.
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

    private BreakHistoryRepository breakHistoryRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        breakHistoryRepository = new BreakHistoryRepository(this);

        bindViews();
        updateHistory();
        setupNavigation();
    }

    private void bindViews() {
        backFromHistoryButton = findViewById(R.id.backFromHistoryButton);

        navHomeFromHistory = findViewById(R.id.navHomeFromHistory);
        navStatsFromHistory = findViewById(R.id.navStatsFromHistory);
        navProfileFromHistory = findViewById(R.id.navProfileFromHistory);

        historyBreakCard = findViewById(R.id.historyBreakCard);
        historyEmptyCard = findViewById(R.id.historyEmptyCard);

        historyBreakTitleText = findViewById(R.id.historyBreakTitleText);
        historyBreakDetailsText = findViewById(R.id.historyBreakDetailsText);
        historyBreakRatingText = findViewById(R.id.historyBreakRatingText);
        historyBreakStressText = findViewById(R.id.historyBreakStressText);
    }

    private void setupNavigation() {
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

    private void updateHistory() {
        SavedBreak savedBreak = breakHistoryRepository.getLatestBreak();

        if (savedBreak == null) {
            historyBreakCard.setVisibility(View.GONE);
            historyEmptyCard.setVisibility(View.VISIBLE);
            return;
        }

        historyBreakCard.setVisibility(View.VISIBLE);
        historyEmptyCard.setVisibility(View.GONE);

        historyBreakTitleText.setText(savedBreak.getTitle());

        String details =
                savedBreak.getDurationMinutes()
                        + " Min · "
                        + savedBreak.getRouteName()
                        + " · "
                        + savedBreak.getType();

        historyBreakDetailsText.setText(details);
        historyBreakRatingText.setText(createStarRating(savedBreak.getRating()));

        historyBreakStressText.setText(
                "Stress beim Speichern: "
                        + savedBreak.getStressScore()
                        + "/10 · "
                        + savedBreak.getStressLabel()
        );
    }

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
