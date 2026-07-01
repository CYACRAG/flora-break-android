package com.florabreak.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.florabreak.app.R;
import com.florabreak.app.data.FloraBreakController;
import com.florabreak.app.data.FloraBreakControllerFactory;
import com.florabreak.app.data.local.BreakEntity;
import com.florabreak.app.data.repository.BreakSessionRepository;
import com.florabreak.app.model.FloraBreakSessionResult;
import com.florabreak.app.model.StressResult;

public class MainActivity extends AppCompatActivity {

    private Button showBreakButton;
    private Button refreshButton;

    private LinearLayout navHistoryButton;
    private LinearLayout navStatsButton;
    private LinearLayout navProfileButton;

    private TextView stressScoreText;
    private TextView stressLabelText;
    private TextView recentBreakTitleText;
    private TextView recentBreaksText;

    private StressGaugeView stressGaugeView;

    private FloraBreakController floraBreakController;
    private BreakSessionRepository breakSessionRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        floraBreakController = FloraBreakControllerFactory.create(this);
        breakSessionRepository = new BreakSessionRepository(this);

        bindViews();
        setupNavigation();
        updateStressFromController();
        updateRecentBreaks();

        refreshButton.setOnClickListener(view -> {
            updateStressFromController();
            updateRecentBreaks();
            Toast.makeText(this, "Stressdaten wurden aktualisiert", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        floraBreakController = FloraBreakControllerFactory.create(this);
        updateStressFromController();
        updateRecentBreaks();
    }

    private void bindViews() {
        showBreakButton = findViewById(R.id.showBreakButton);
        refreshButton = findViewById(R.id.refreshButton);

        stressGaugeView = findViewById(R.id.stressGaugeView);
        stressScoreText = findViewById(R.id.stressScoreText);
        stressLabelText = findViewById(R.id.stressLabelText);

        recentBreakTitleText = findViewById(R.id.recentBreakTitleText);
        recentBreaksText = findViewById(R.id.recentBreaksText);

        navHistoryButton = findViewById(R.id.navHistoryButton);
        navStatsButton = findViewById(R.id.navStatsButton);
        navProfileButton = findViewById(R.id.navProfileButton);
    }

    private void setupNavigation() {
        showBreakButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, BreakSuggestionActivity.class);
            startActivity(intent);
        });

        navHistoryButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
            startActivity(intent);
        });

        navStatsButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, StatsActivity.class);
            startActivity(intent);
        });

        navProfileButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(intent);
        });
    }

    private void updateStressFromController() {
        FloraBreakSessionResult sessionResult =
                floraBreakController.evaluateCurrentSituation();

        StressResult stressResult = sessionResult.getStressResult();

        int stressScore = stressResult.getScore();

        stressGaugeView.setStressScore(stressScore);
        stressScoreText.setText(String.valueOf(stressScore));
        stressLabelText.setText(stressResult.getLabel());
    }

    private void updateRecentBreaks() {
        BreakEntity latestBreak = breakSessionRepository.getLatestBreak();

        if (latestBreak == null) {
            recentBreakTitleText.setText("Keine Pause gespeichert");
            recentBreaksText.setText("Noch keine gespeicherten Pausen");
            return;
        }

        recentBreakTitleText.setText("Letzte gespeicherte Pause");

        String details =
                latestBreak.durationMinutes
                        + " Min · "
                        + safeText(latestBreak.routeName, "Route")
                        + " · Stress "
                        + latestBreak.stressScore
                        + "/10 · "
                        + createStarRating(latestBreak.rating);

        if (latestBreak.photoProofTaken) {
            details += " · Foto";
        }

        recentBreaksText.setText(details);
    }

    private String safeText(String value, String fallback) {
        if (value == null || value.trim().isEmpty()) {
            return fallback;
        }

        return value;
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
