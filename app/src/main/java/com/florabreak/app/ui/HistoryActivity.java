package com.florabreak.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.florabreak.app.R;
import com.florabreak.app.data.local.BreakEntity;
import com.florabreak.app.data.repository.BreakSessionRepository;

/**
 * Verlauf-Screen für gespeicherte Pausen.
 *
 * Nutzt jetzt die lokale Room-Datenbank.
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

    private BreakSessionRepository breakSessionRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        breakSessionRepository = new BreakSessionRepository(this);

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

        navStatsFromHistory.setOnClickListener(view -> {
            Intent intent = new Intent(HistoryActivity.this, StatsActivity.class);
            startActivity(intent);
        });

        navProfileFromHistory.setOnClickListener(view -> {
            Intent intent = new Intent(HistoryActivity.this, ProfileActivity.class);
            startActivity(intent);
        });
    }

    private void updateHistory() {
        BreakEntity latestBreak = breakSessionRepository.getLatestBreak();

        if (latestBreak == null) {
            historyBreakCard.setVisibility(View.GONE);
            historyEmptyCard.setVisibility(View.VISIBLE);
            return;
        }

        historyBreakCard.setVisibility(View.VISIBLE);
        historyEmptyCard.setVisibility(View.GONE);

        historyBreakTitleText.setText("Abgeschlossene Pause");

        String details =
                latestBreak.durationMinutes
                        + " Min · "
                        + safeText(latestBreak.routeName, "Route")
                        + " · "
                        + formatRouteType(latestBreak.routeType);

        historyBreakDetailsText.setText(details);
        historyBreakRatingText.setText(createStarRating(latestBreak.rating));

        String photoText = latestBreak.photoProofTaken
                ? " · Foto-Beweis vorhanden"
                : " · Kein Foto-Beweis";

        historyBreakStressText.setText(
                "Stress: "
                        + latestBreak.stressScore
                        + "/10 · "
                        + safeText(latestBreak.stressLabel, "Unbekannt")
                        + photoText
        );
    }

    private String safeText(String value, String fallback) {
        if (value == null || value.trim().isEmpty()) {
            return fallback;
        }

        return value;
    }

    private String formatRouteType(String routeType) {
        if (routeType == null) {
            return "Aktive Pause";
        }

        if (routeType.contains("PARK")) {
            return "Parkroute";
        }

        if (routeType.contains("URBAN")) {
            return "Urban Walk";
        }

        return "Aktive Pause";
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
